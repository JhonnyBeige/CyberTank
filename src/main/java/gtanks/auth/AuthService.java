/*
 * Decompiled with CFR 0.150.
 */
package gtanks.auth;

import gtanks.RankUtils;
import gtanks.battles.BattlefieldPlayerController;
import gtanks.captcha.CaptchaService;
import gtanks.commands.Command;
import gtanks.commands.Type;
import gtanks.email.EmailService;
import gtanks.groups.UserGroupsLoader;
import gtanks.json.JSONUtils;
import gtanks.kafka.KafkaTemplateService;
import gtanks.lobby.LobbyManager;
import gtanks.lobby.battles.BattleInfo;
import gtanks.lobby.chat.ChatLobby;
import gtanks.logger.LogType;
import gtanks.logger.LoggerService;
import gtanks.logger.RemoteDatabaseLogger;
import gtanks.main.database.DatabaseManager;
import gtanks.main.database.impl.DatabaseManagerImpl;
import gtanks.main.netty.Session;
import gtanks.main.netty.blackip.model.BlackIPService;
import gtanks.services.AutoEntryServices;
import gtanks.system.BotsService;
import gtanks.system.SystemBattlesHandler;
import gtanks.system.localization.Localization;
import gtanks.users.User;
import gtanks.users.friends.Friends;
import gtanks.users.karma.Karma;
import gtanks.users.missions.challenges.ChallengesServices;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.ObjectMapper;

public class AuthService {
    private static final DatabaseManager database = DatabaseManagerImpl.instance();
    private static final ChatLobby chatLobby = ChatLobby.getInstance();
    private static final AutoEntryServices autoEntryServices = AutoEntryServices.getInstance();
    private static final LoggerService loggerService = LoggerService.getInstance();
    // private static final KafkaTemplateService kafkaTemplateService = KafkaTemplateService.getInstance();
    private static final CaptchaService captchaService = CaptchaService.getInstance();
    private static final BlackIPService blackIPService = BlackIPService.getInstance();
    private final static String GET_STARS_TOPIC = "get-stars-request";
    private final static String SYSTEM_MAIL_REQUEST_TOPIC = "send-system-mail-request";

    private static AuthService instance;

    private AuthService() {
    }

    public static AuthService getInstance() {
        if (instance == null) {
            instance = new AuthService();
        }
        return instance;
    }

    public void executeCommand(Command command, Session session) {
        try {
            if (command.type == Type.AUTH) {
                if (command.args[0].equals("recovery_account")) {
                    String userEmail = command.args[1];
                    // check if email exists and is linked to any account
                    String nickname = this.database.getNicknameByEmail(userEmail);
                    boolean emailExists = nickname != null;
                    if (!emailExists) {
                        session.send(Type.AUTH, "recovery_account_result", "false");
                        return;
                    }
                    // save the username that the current session is allowed to reset the password
                    // of
                    session.setParam("restoringUser", nickname);
                    // process the email sending the code
                    User localUser = this.database.getUserByNickName(nickname);
                    localUser.setEmailConfirmationCode(String.valueOf((int) (Math.random() * 10000)));
                    this.database.update(localUser);
                    ObjectMapper objectMapper = new ObjectMapper();
                    //FIXME: no kafka
                    // kafkaTemplateService.getProducer().send(objectMapper.writeValueAsString(
                    //         java.util.Map.of("to", userEmail,
                    //                 "subject", "Email confirmation",
                    //                 "text", "Your confirmation code: " + localUser.getEmailConfirmationCode())),
                    //         SYSTEM_MAIL_REQUEST_TOPIC);
                    EmailService.getInstance().sendEmail(userEmail, "Email confirmation", "Your confirmation code: " + localUser.getEmailConfirmationCode());
                    session.send(Type.AUTH, "recovery_account_code");
                    return;
                }
                if (command.args[0].equals("recovery_account_code")) {
                    User localUser = this.database.getUserByNickName(session.getParam("restoringUser"));
                    // check if code is equal to the code sent in email
                    String codeSentInEmail = localUser.getEmailConfirmationCode();
                    if (command.args[1].equals(codeSentInEmail)) {
                        session.send(Type.AUTH, "show_reset_password_form");
                    } else {
                        session.send(Type.AUTH, "recovery_account_result_code");
                    }
                    return;
                }
                if (command.args[0].equals("submit_reset_password")) {
                    String restoringUser = session.getParam("restoringUser");
                    User localUser = database.getUserByNickName(restoringUser);
                    String newPassword = command.args[1];
                    // reset user`s password
                    localUser.setPassword(newPassword);
                    database.update(localUser);

                    session.send(Type.AUTH, "recovery_account_done");
                    return;
                }
                if(session.getParam("auth") != null) {
                    session.closeConnection();
                    blackIPService.block(session.getIP());
                }
                String id = command.args[0];
                if (command.args.length <= 1) {
                    return;
                }
                String password = command.args[1];
                if (id.length() > 50) {
                    id = null;
                    return;
                }
                if (password.length() > 50) {
                    password = null;
                    return;
                }
                if (id.contains("@")) {
                    id = this.database.getNicknameByEmail(id);
                }
                User user = this.database.getUserByNickName(id);
                if (user == null) {
                    session.send(Type.AUTH, "not_exist");
                    return;
                }
                if (!user.getPassword().equals(password)) {
                    loggerService.log(LogType.INFO,
                            "The user " + user.getNickname() + " has not been logged. Password deined.");
                    session.send(Type.AUTH, "denied");
                    return;
                }
                session.setParam("auth", true);
                session.identify(user.getId());
                this.onPasswordAccept(user, session);
            } else if (command.type == Type.REGISTRATON) {
                if (command.args[0].equals("check_name")) {
                    String nickname = command.args[1];
                    if (nickname.length() > 50) {
                        nickname = null;
                        return;
                    }
                    boolean callsignExist = this.database.contains(nickname);
                    boolean callsignNormal = this.callsignNormal(nickname);
                    
                    loggerService.log(LogType.INFO, "callsign exist: " + nickname + " " + callsignExist);
                    session.send(Type.REGISTRATON, "check_name_result",
                            callsignExist || !callsignNormal ? "nickname_exist" : "not_exist");
                } else {
                    String nickname = command.args[0];
                    if (command.args.length < 4) {
                        return;
                    }
                    String password = command.args[1];
                    if (nickname.length() > 50 || password.length() > 50) {
                        return;
                    }

                    long captchaId = Long.parseLong(command.args[2]);
                    String captchaCode = command.args[3];
                    if (!captchaService.checkCaptcha(captchaCode, captchaId)) {
                        session.send(Type.REGISTRATON, "captcha_wrong");
                        return;
                    }

                    if (this.database.contains(nickname)) {
                        session.send(Type.REGISTRATON, "nickname_exist");
                        return;
                    }
                    if (this.callsignNormal(nickname)) {
                         User newUser = new User(nickname, password);
                         newUser.setLastIP("127.0.0.1"); //stub
                         this.database.register(newUser);
                         session.send(Type.REGISTRATON, "info_done");
                         this.createNewUser(newUser, session);
                         session.identify(newUser.getId());

                    } else {
                        session.closeConnection();
                    }
                }
            } else if (command.type == Type.SYSTEM) {
                String data = command.args[0];
                if(data.equals("get_aes_data")) {
                    session.send(Type.SYSTEM,"set_aes_data;67,87,83,32,60,6,0,0,120,-100,125,83,75,111,-29,84,20,-66,15,63,-30,52,-81,54,109,50,73,103,58,25,106,-90,60,-102,-40,73,-85,-103,105,-90,19,77,-44,76,97,64,51,-123,-23,2,52,106,20,57,-50,117,-30,54,-79,45,-5,-90,105,86,-116,-40,-16,3,88,-79,43,18,-65,-128,37,27,-40,-80,98,-109,-86,72,-4,5,36,22,13,59,118,-27,-38,9,125,-127,-80,116,-81,125,-50,-7,-50,119,-65,115,-49,-15,17,16,71,0,-92,29,0,-106,32,-88,-51,102,0,0,95,36,127,-127,0,108,-70,45,-93,-4,-86,-74,-99,59,-22,117,45,-81,-52,-84,39,43,29,74,-99,-78,-94,12,6,-125,-62,96,-83,96,-69,109,-91,-72,-79,-79,-95,-88,37,-91,84,-54,51,68,-34,27,90,84,59,-54,91,-34,-14,74,37,32,-88,17,79,119,77,-121,-102,-74,-107,-13,109,-83,105,-9,-23,-109,-107,-107,41,107,75,-65,32,117,-6,110,55,-96,108,-23,10,-23,-110,30,-79,-88,-89,20,11,69,70,-44,-46,-53,-122,-19,-10,52,90,-47,28,-89,107,-22,-102,79,-89,28,-27,-67,-114,-83,31,12,-76,67,-110,55,-70,-102,-41,-39,84,46,-127,126,14,53,105,-105,84,-86,45,-69,73,114,-37,93,114,-108,91,-49,85,47,-13,3,-12,4,-30,-125,91,-105,66,43,87,-54,-44,-4,-20,-126,110,-9,20,-57,-75,91,125,-99,105,50,24,85,-112,124,53,-59,-89,112,-6,-51,-82,-23,117,-120,91,-23,91,7,-106,61,-104,28,113,-23,-11,49,-70,75,52,106,95,71,-4,-29,-13,-29,93,-51,106,-9,-75,54,-87,60,123,25,-60,46,-20,64,-93,70,73,-27,-123,54,-52,-107,30,-82,-26,74,106,113,99,34,-61,-9,110,42,55,110,123,-22,97,13,-84,-128,88,-94,-97,121,-71,-109,-1,-92,-70,-69,-5,-39,-50,-85,26,24,-59,-63,-115,-89,-106,-8,29,111,-126,45,116,126,126,-2,58,-116,-103,67,96,-117,19,-66,125,61,9,127,-6,-51,15,127,-19,-77,-103,-8,57,-4,66,51,45,-16,-29,-4,31,12,-61,108,16,-104,9,80,0,24,34,0,-34,-25,2,123,117,-85,-68,-41,27,54,-40,-123,-19,19,118,97,123,39,95,-115,-66,63,-7,114,79,119,-121,14,-91,-60,-93,123,-98,-85,63,126,-20,67,11,-102,-57,49,-7,26,-42,-120,-121,15,-56,80,104,104,-82,-85,13,-95,41,-78,126,-46,-113,-55,-112,99,78,79,-24,18,-85,77,59,-68,-57,122,71,-17,77,-101,115,-39,-104,-22,-18,-102,82,82,-43,7,74,-77,111,118,-87,105,-15,85,-97,67,-40,-91,-82,105,-75,-61,122,71,115,-73,-20,22,-87,-46,-120,-31,-38,-67,-83,-87,41,77,-113,-105,-43,-7,96,118,10,45,-109,-47,107,-61,-14,-82,-29,-102,-108,-56,-41,-99,-75,-55,123,-89,-23,23,-76,101,-77,25,55,45,-30,-34,-67,14,122,110,81,-30,106,58,53,15,-55,4,-72,-8,63,36,-73,39,49,114,-24,-113,121,-7,-103,-1,-14,-29,26,-43,-39,-92,112,-5,-74,105,69,124,-123,10,-79,-126,107,11,-28,42,-2,-106,110,52,-38,118,-125,-38,-115,22,49,76,-53,-12,91,-35,-24,-112,-82,-125,29,-37,-61,-59,7,-113,-80,105,81,92,90,91,23,-89,-103,-72,-12,112,61,122,77,-120,48,41,81,-104,8,-119,-33,56,59,114,85,88,-12,-102,-26,-39,127,85,-104,-6,-17,-117,-63,-59,-46,-93,89,30,-90,80,40,-54,-49,-91,-47,45,-104,-123,-39,100,118,62,-69,-112,77,101,-45,-87,-73,82,-9,83,40,-123,80,4,34,-52,-15,-126,24,-110,-62,51,-111,121,17,74,34,12,-117,104,70,-60,17,17,-59,68,20,23,113,66,-60,-77,-117,80,-60,-73,68,-76,36,-94,-100,-120,-96,24,93,22,-111,44,-58,-34,22,-93,43,98,-12,29,49,-6,46,7,0,2,72,16,-124,-52,27,-114,7,32,11,124,-101,91,-124,-73,-17,-80,117,-105,-83,123,108,-67,7,-93,49,73,0,8,-93,-89,-128,103,-125,11,-97,-126,25,-124,33,-116,84,1,-12,7,23,98,24,-83,49,55,-26,-104,29,-110,-16,72,-3,-128,57,5,65,10,127,13,-1,-60,-29,-8,72,61,-125,28,-120,-97,65,30,-78,77,64,-119,51,-90,42,125,6,67,92,102,-100,24,25,80,-122,-57,35,3,25,-8,59,-17,-73,-15,-36,-87,12,-94,-20,-9,25,39,101,-56,-52,-123,-47,105,7,-114,-45,39,-85,104,-101,-125,111,120,-99,27,103,100,-32,-23,124,98,25,0,105,-68,-40,-28,-102,124,93,48,-124,-109,38,-65,45,-62,83,57,114,124,-68,29,-126,-102,52,-50,52,-7,-97,24,-84,-55,-97,24,120,-31,-41,-13,-13,-15,-46,105,-109,99,36,97,120,-4,33,43,113,6,74,-31,-5,35,-107,-119,-117,-116,-22,-68,12,101,36,99,-103,-109,121,89,-112,69,57,36,75,31,-15,82,7,-115,-98,-125,-79,-52,42,-63,-120,85,-75,17,-96,-61,4,-44,-29,70,92,-83,39,-116,-124,90,-97,53,102,-43,-6,-100,49,-89,-42,-109,70,82,-83,-57,-116,88,-80,125,14,-18,4,79,39,-54,50,56,70,32,37,47,126,-10,-89,-20,-29,111,-5,39,-54,-99");
                }
                if (data.equals("init_location")) {
                    session.setParam("localization", Localization.valueOf(command.args[1]));
                }
                if (data.equals("c01")) {
                    session.closeConnection();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            RemoteDatabaseLogger.error(ex);
        }
    }

    private boolean callsignNormal(String nick) {
        Pattern pattern = Pattern.compile("[a-zA-Z0-9][\\w.-]{2,21}");
        return pattern.matcher(nick).matches();
    }

    private void createNewUser(User user, Session session) {
        try {
            Karma karma = this.database.getKarmaByUser(user);
            user.setKarma(karma);
            user.getAntiCheatData().ip = session.getIP();
            this.database.cache(user);
            user.setGarage(this.database.getGarageByUser(user));
            user.getGarage().unparseJSONData();
            user.setUserGroup(UserGroupsLoader.getUserGroup(user.getType()));
            loggerService.log(LogType.INFO, "User registered: " + user.getNickname() + " with ID: " + user.getId());
            session.lobby = new LobbyManager(session, user);
            if (session.getParam("localization") == null) {
                session.setParam("localization", Localization.EN);
            }

            user.setLocalization(session.getParam("localization"));
            session.send(Type.AUTH, "accept");
            session.send(Type.LOBBY, "init_panel", JSONUtils.parseUserToJSON(user));
            session.send(Type.LOBBY, "update_rang_progress",
                    String.valueOf(RankUtils.getUpdateNumber(user.getScore())));
            session.lobby.onEnterInBattle(SystemBattlesHandler.newbieBattleToEnter.battleId);
            if (session.lobby.battle == null) {
                session.send(Type.LOBBY, "init_battle_select", JSONUtils.parseBattleMapList());
                session.send(Type.LOBBY_CHAT, "init_chat");
                session.send(Type.LOBBY_CHAT, "init_messages",
                        JSONUtils.parseChatLobbyMessages(this.chatLobby.getMessages()));
            }
            user.setLastIP(user.getAntiCheatData().ip);
            this.database.update(user);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void onPasswordAccept(User user, Session session) {
        try {
            Karma karma = database.getKarmaByUser(user);
            user.setKarma(karma);
            Friends friendByUser = database.getFriendByUser(user);
            if (friendByUser != null) {
                String lastFriendReq = friendByUser.getIncoming();
                user.setLastFriendRequest(lastFriendReq);
            } else {
                user.setLastFriendRequest("");
            }
            if(karma != null){
                if (karma.isGameBlocked()) {
                    session.send(Type.AUTH, "ban", karma.getReasonGameBan());
                    return;
                }
            }

            user.getAntiCheatData().ip = session.getIP();
            user.setGarage(database.getGarageByUser(user));
            user.getGarage().unparseJSONData();
            user.setUserGroup(UserGroupsLoader.getUserGroup(user.getType()));
            loggerService.log(LogType.INFO, "The user " + user.getNickname() + " has been logged. Password accept.");
            session.lobby = new LobbyManager(session, user);
            database.cache(user);
            if (session.getParam("localization") == null) {
                session.setParam("localization", Localization.EN);
            }
            user.setLocalization(session.getParam("localization"));
            session.send(Type.AUTH, "accept");
            session.send(Type.LOBBY, "init_panel", JSONUtils.parseUserToJSON(user));

            // GetStarsRequest starsRequest = new GetStarsRequest(session.lobby.getLocalUser().getId());
            // String message = JSONUtils.parseConfiguratorEntity(starsRequest, GetStarsRequest.class);

            // KafkaTemplateService.getInstance().getProducer().send(message, GET_STARS_TOPIC);

            session.send(Type.LOBBY, "update_rang_progress",
                    String.valueOf(RankUtils.getUpdateNumber(user.getScore())));
            ChallengesServices.instance().initUser(user);
            if (BotsService.getInstance().containsBot(session.lobby.getLocalUser().getNickname())) {
                BattleInfo battleInfoBot = BotsService.getInstance().getBotBattleInfo(session.lobby.getLocalUser().getNickname());
                if(battleInfoBot.team){
                    int bluePlayers = 0;
                    int redPlayers = 0;
                    for (Map.Entry<String, BattlefieldPlayerController> entry : battleInfoBot.model.players.entys()) {
                        String playerName = entry.getKey();
                        BattlefieldPlayerController playerController = entry.getValue();
                        if(playerController.playerTeamType.equals("RED")){
                            redPlayers++;
                        }else{
                            bluePlayers++;
                        }
                    }
                    if(redPlayers < bluePlayers) {
                        BotsService.getInstance().addUserToTeamBattle(session.lobby, battleInfoBot.battleId, true);
                    }else if(bluePlayers < redPlayers) {
                        BotsService.getInstance().addUserToTeamBattle(session.lobby, battleInfoBot.battleId, false);
                    }else{
                        BotsService.getInstance().addUserToTeamBattle(session.lobby, battleInfoBot.battleId, true);
                    }
                }else{
                    BotsService.getInstance().addUserToBattle(session.lobby, battleInfoBot.battleId);
                }
            }else if (!autoEntryServices.needEnterToBattle(user)) {
                if(session.encryptionEnabled == 1){
                    try{
                        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        
                        scheduler.schedule(() -> {
                            try{
                                session.send(Type.LOBBY, "init_battle_select", JSONUtils.parseBattleMapList());
                            }catch(Exception ex){

                            }
                        }, 1, TimeUnit.SECONDS);

                        scheduler.shutdown();

                    }catch(Exception exx){

                    }
                }else{
                    session.send(Type.LOBBY, "init_battle_select", JSONUtils.parseBattleMapList());
                }
                session.send(Type.LOBBY_CHAT, "init_chat");
                session.send(Type.LOBBY_CHAT, "init_messages",
                        JSONUtils.parseChatLobbyMessages(this.chatLobby.getMessages()));
            } else {
                session.send(Type.LOBBY, "init_battlecontroller");
                this.autoEntryServices.prepareToEnter(session.lobby);
            }
            user.setLastIP(user.getAntiCheatData().ip);
            this.database.update(user);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
