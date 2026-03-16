/*
 * Decompiled with CFR 0.150.
 */
package gtanks.battles;

import gtanks.StringUtils;
import gtanks.battles.anticheats.AnticheatModel;
import gtanks.battles.bonuses.Bonus;
import gtanks.battles.bonuses.BonusesSpawnService;
import gtanks.battles.bonuses.model.BonusTakeModel;
import gtanks.battles.chat.BattlefieldChatModel;
import gtanks.battles.ctf.CTFModel;
import gtanks.battles.ctf.flags.FlagState;
import gtanks.battles.dom.DominationModel;
import gtanks.battles.dom.DominationPoint;
import gtanks.battles.effects.model.EffectsVisualizationModel;
import gtanks.battles.jgr.JuggernautModel;
import gtanks.battles.managers.SpawnManager;
import gtanks.battles.maps.MapChecksumModel;
import gtanks.battles.mines.model.BattleMinesModel;
import gtanks.battles.spectator.SpectatorController;
import gtanks.battles.spectator.SpectatorModel;
import gtanks.battles.tanks.Tank;
import gtanks.battles.tanks.TankState;
import gtanks.battles.tanks.loaders.HullsFactory;
import gtanks.battles.tanks.loaders.WeaponsFactory;
import gtanks.battles.tanks.math.Vector3;
import gtanks.battles.tanks.module.ModuleFactory;
import gtanks.battles.tanks.module.Module;
import gtanks.battles.tanks.statistic.PlayersStatisticModel;
import gtanks.battles.timer.schedulers.bonuses.BonusesScheduler;
import gtanks.battles.timer.schedulers.runtime.TankRespawnScheduler;
import gtanks.collections.FastHashMap;
import gtanks.commands.Type;
import gtanks.json.JSONUtils;
import gtanks.lobby.battles.BattleInfo;
import gtanks.lobby.battles.BattlesList;
import gtanks.logger.LogType;
import gtanks.logger.LoggerService;
import gtanks.services.AutoEntryServices;
import gtanks.system.BattlesGC;
import gtanks.system.SystemBattlesHandler;
import gtanks.system.destroy.Destroyable;
import gtanks.system.quartz.QuartzService;
import gtanks.system.quartz.TimeType;
import gtanks.system.quartz.impl.QuartzServiceImpl;
import gtanks.users.garage.items.Item;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.List;

import java.util.HashMap;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class BattlefieldModel implements Destroyable {
    public final String QUARTZ_NAME;
    public final String QUARTZ_RESTART_NAME;
    public final int JGR_MIN_PLAYERS = 4;
    public static final String QUARTZ_GROUP = BattlefieldModel.class.getName();
    private static final QuartzService quartzService = QuartzServiceImpl.getInstance();
    private static final LoggerService loggerService = LoggerService.getInstance();
    private static final AutoEntryServices autoEntryServices = AutoEntryServices.getInstance();
    private static final BattlesList battlesList = BattlesList.getInstance();
    public static final BonusTakeModel bonusTakeModel =BonusTakeModel.getInstance();
    private static final BattlesGC battlesGC = BattlesGC.getInstance();

    public FastHashMap<String, BattlefieldPlayerController> players;
    public HashMap<String, Bonus> activeBonuses;
    public BattleInfo battleInfo;
    public boolean battleFinish = false;
    public boolean queueRespawn = false;
    private long endBattleTime = 0L;
    private final JSONParser jsonParser = new JSONParser();
    public PlayersStatisticModel statistics;
    public TankKillModel tanksKillModel;
    public CTFModel ctfModel;
    public DominationModel domModel;
    public JuggernautModel jgrModel;
    public BattlefieldChatModel chatModel;
    public BonusesSpawnService bonusesSpawnService;
    public SpectatorModel spectatorModel;
    public EffectsVisualizationModel effectsModel;
    public BattleMinesModel battleMinesModel;
    public MapChecksumModel mapChecksumModel;

    public BattlefieldModel(BattleInfo battleInfo) {
        this.battleInfo = battleInfo;
        this.statistics = new PlayersStatisticModel(this);
        this.tanksKillModel = new TankKillModel(this);
        this.chatModel = new BattlefieldChatModel(this);
        this.spectatorModel = new SpectatorModel(this);
        this.effectsModel = new EffectsVisualizationModel(this);
        this.battleMinesModel = new BattleMinesModel(this);
        this.mapChecksumModel = new MapChecksumModel(this);
        this.QUARTZ_NAME = "TimeBattle " + this.hashCode() + " battle=" + battleInfo.battleId;
        this.QUARTZ_RESTART_NAME = "RestartBattle  battle=" + battleInfo.battleId;
        if (battleInfo.time > 0) {
            this.startTimeBattle();
        }
        if (battleInfo.battleType.equals("CTF")) {
            this.ctfModel = new CTFModel(this);
        }
        if (battleInfo.battleType.equals("DOM")) {
            this.domModel = new DominationModel(this);
        }
        if (battleInfo.battleType.equals("JGR")) {
            this.jgrModel = new JuggernautModel(this);
        }
        this.players = new FastHashMap();
        this.activeBonuses = new HashMap();
        if ((battleInfo.isPaid && battleInfo.inventory) || !battleInfo.isPaid) {
            this.bonusesSpawnService = new BonusesSpawnService(this);
            new Thread(this.bonusesSpawnService).start();
        }
        if (SystemBattlesHandler.newbieBattleToEnter != null &&
                SystemBattlesHandler.middleBattle != null &&
                SystemBattlesHandler.forAllBattle != null) {
            if (battleInfo.battleId == SystemBattlesHandler.newbieBattleToEnter.battleId ||
                    battleInfo.battleId == SystemBattlesHandler.middleBattle.battleId ||
                    battleInfo.battleId == SystemBattlesHandler.forAllBattle.battleId) {
                return;
            }
        }
        if(!battleInfo.unremoveable){
            battlesGC.addBattleForRemove(this);
        }
    }

    private void startTimeBattle() {
        this.endBattleTime = System.currentTimeMillis() + (long) (this.battleInfo.time * 1000);
        quartzService.addJob(this.QUARTZ_NAME, QUARTZ_GROUP, e -> {
            loggerService.log(LogType.DEBUG, "battle end...");
            this.tanksKillModel.restartBattle(true);
        }, TimeType.SEC, this.battleInfo.time);
    }

    public void sendTableMessageToPlayers(String msg) {
        this.sendToAllPlayers(Type.BATTLE, "show_warning_table", msg);
    }

    public void battleRestart() {
        if (this.battleInfo.team) {
            this.sendToAllPlayers(Type.BATTLE, "change_team_scores", "RED", String.valueOf(this.battleInfo.scoreRed));
            this.sendToAllPlayers(Type.BATTLE, "change_team_scores", "BLUE", String.valueOf(this.battleInfo.scoreBlue));
        }
        if(battleInfo.battleType.equals("JGR")){
            this.jgrModel.restartBattle();
        }
        this.battleFinish = false;
        for (BattlefieldPlayerController player : this.players.values()) {
            if (player == null) continue;
            player.statistic.clear();
            player.clearEffects();
            if (this.jgrModel != null) {
                if(player.tank.isJuggernaut){
                    player.tank.isJuggernaut = false;
                    player.tank.setHull(HullsFactory.getInstance().getHull(player.getGarage().mountHull.getId()));
                    player.tank.hullId = player.getGarage().mountHull.getId();
                    player.tank.setWeapon(WeaponsFactory.getInstance().getWeapon(player.getGarage().mountTurret.getId(), player, this));
                    player.tank.turretId = player.getGarage().mountTurret.getId();
                    // player.tank.setModule(ModuleFactory.getInstance().getModule(player.getGarage().mountModule.getId()));
                    List<Item> moduleIds = player.getGarage().mountModules;
                    player.tank.getModules().clear();
                    for (Item moduleId : moduleIds) {
                        Module module = ModuleFactory.getInstance().getModule(moduleId.getId());
                        player.tank.addModule(module);
                    }

                    player.tank.paintId = player.getGarage().mountColormap.getId();
                    this.jgrModel.sendSpecData(player.tank);
                    kPlayer(player, true);
                    rPlayer(player);
                    // ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

                    // // Schedule a task to run after 5 seconds
                    // scheduler.schedule(() -> {
                    //     player.parentLobby.send(Type.BATTLE, "init_suicide_garage");
                    // }, 5, TimeUnit.SECONDS);

                    // // Optional: shutdown the scheduler after the task execution
                    // scheduler.shutdown();
                }else{
                    if(this.battleInfo.model.jgrModel.juggernaut.owner == null && this.battleInfo.model.jgrModel.juggernaut.queuedOwner == null && this.battleInfo.model.players.size() > JGR_MIN_PLAYERS){
                        this.battleInfo.model.jgrModel.juggernaut.queuedOwner = player;
                        kPlayer(player, true);
                        rPlayer(player);
                    }
                }    
            }
            this.respawnPlayer(player, false);
        }
        this.queueRespawn = false;
        long currentTimeMillis = System.currentTimeMillis();
        int prepareTimeLeft = (int) ((currentTimeMillis + (long) (this.battleInfo.time * 1000) - currentTimeMillis) / 1000L);
        this.sendToAllPlayers(Type.BATTLE, "battle_restart", String.valueOf(prepareTimeLeft));
        if (this.battleInfo.time > 0) {
            this.startTimeBattle();
        }
    }

    public void beforeGarageChange(BattlefieldPlayerController player) {
        this.sendToAllPlayers(Type.BATTLE, "kill_tank1", player.tank.id, "suicide");
    }

    public void battleFinish() {
        if (this.players == null) {
            return;
        }
        this.battleFinish = true;
        this.queueRespawn = true;
        if (this.activeBonuses != null) {
            this.activeBonuses.clear();
        }
        if (this.battleInfo.battleType.equals("CTF")) {
            if (this.ctfModel.getBlueFlag().state == FlagState.TAKEN_BY && this.ctfModel.getBlueFlag().owner != null) {
                this.ctfModel.getBlueFlag().battleFinish();
            }
            if (this.ctfModel.getRedFlag().state == FlagState.TAKEN_BY && this.ctfModel.getRedFlag().owner != null) {
                this.ctfModel.getRedFlag().battleFinish();
            }
        }
        if ((battleInfo.isPaid && battleInfo.inventory) || !battleInfo.isPaid) {
            this.bonusesSpawnService.battleFinished();
        }
        this.tanksKillModel.setBattleFund(0);
        this.battleInfo.scoreBlue = 0;
        this.battleInfo.scoreRed = 0;
        for (BattlefieldPlayerController player : this.players.values()) {
            if (player == null) continue;
            TankRespawnScheduler.cancelRespawn(player);
        }
        autoEntryServices.battleRestarted(this);
    }

    public int getTimeLeft() {
        return (int) ((this.endBattleTime - System.currentTimeMillis()) / 1000L);
    }

    public void fire(BattlefieldPlayerController tank, String json) {
        this.sendToAllPlayers(tank, Type.BATTLE, "fire", tank.tank.id, json);
    }

    public void startFire(BattlefieldPlayerController tank) {
        this.sendToAllPlayers(tank, Type.BATTLE, "start_fire", tank.tank.id);
    }

    public void stopFire(BattlefieldPlayerController tank) {
        this.sendToAllPlayers(tank, Type.BATTLE, "stop_fire", tank.tank.id);
    }

    public void quickFire(BattlefieldPlayerController tank, String json) {
        this.sendToAllPlayers(tank, Type.BATTLE, "shaft_quick_shot", tank.tank.id, json);
    }

    public synchronized void onTakeBonus(BattlefieldPlayerController controller, String json) {
        try {
            JSONObject obj = (JSONObject) this.jsonParser.parse(json);
            String bonusId = (String) obj.get("bonus_id");
            Bonus bonus = this.activeBonuses.get(bonusId);
            if (bonus == null) {
                return;
            }
            if (bonusTakeModel.onTakeBonus(bonus, controller)) {
                this.sendToAllPlayers(Type.BATTLE, "take_bonus_by", bonusId);
                this.activeBonuses.remove(bonusId);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void spawnBonus(Bonus bonus, int inc, int disappearingTime) {
        if (bonus.position.x == 0.0f && bonus.position.y == 0.0f && bonus.position.z == 0.0f) {
            return;
        }
        String id = StringUtils.concatStrings(bonus.type.toString(), "_", String.valueOf(inc));
        this.activeBonuses.put(id, bonus);
        BonusesScheduler.runRemoveTask(this, id, disappearingTime);
        this.sendToAllPlayers(Type.BATTLE, "spawn_bonus", JSONUtils.parseBonusInfo(bonus, inc, disappearingTime));
    }

    public void kPlayer(final BattlefieldPlayerController battlefieldPlayerController, final boolean b) {
        if (!this.battleFinish) {
            battlefieldPlayerController.send(Type.BATTLE, "local_user_killed");
            this.battleMinesModel.playerDied(battlefieldPlayerController);
            if (b) {
                battlefieldPlayerController.clearEffects();
                //this.sendToAllPlayers(Type.BATTLE, "kill_tank1", battlefieldPlayerController.tank.id, "suicide");
                if(!this.queueRespawn){
                    test(battlefieldPlayerController);
                }
                this.sendToAllPlayers(Type.BATTLE, "res_user", battlefieldPlayerController.tank.id);
            }
        }
    }

    public void respawnPlayer(BattlefieldPlayerController controller, boolean kill) {
        if (this.battleFinish) {
            return;
        }
        if (this.jgrModel != null && !this.queueRespawn){
            if(this.battleInfo.model.jgrModel.juggernaut.owner == null && this.battleInfo.model.jgrModel.juggernaut.queuedOwner == null && this.battleInfo.model.players.size() > JGR_MIN_PLAYERS){
                this.battleInfo.model.jgrModel.setUserJuggernaut(controller);
                controller.parentLobby.send(Type.BATTLE, "init_suicide_garage");
                return;
            }
        }
        controller.send(Type.BATTLE, "local_user_killed");
        this.battleMinesModel.playerDied(controller);
        if (kill) {
            controller.clearEffects();
            if(controller.tank.isJuggernaut){
                this.jgrModel.userKillJuggernaut(controller);
            }
            this.sendToAllPlayers(Type.BATTLE, "kill_tank", controller.tank.id, "suicide");
            controller.statistic.addDeaths();
            this.statistics.changeStatistic(controller);
            if (this.ctfModel != null && controller.flag != null) {
                this.ctfModel.dropFlag(controller, controller.tank.position);
            }
        }
        controller.tank.state = TankState.suicide;
        TankRespawnScheduler.startRespawn(controller, false);
    }

    public void rPlayer(final BattlefieldPlayerController battlefieldPlayerController) {
        final Vector3 spawnState = SpawnManager.getSpawnState(this.battleInfo.map, battlefieldPlayerController.playerTeamType);
        if (this.battleInfo.battleType.equals("JGR")){
            if(this.jgrModel.isUserQueuedJuggernaut(battlefieldPlayerController)){
                this.battleInfo.model.jgrModel.setUserJuggernaut(battlefieldPlayerController);
                this.sendToAllPlayers(Type.BATTLE, "res1_user", JSONUtils.parseJuggernautTankData(this, battlefieldPlayerController, battlefieldPlayerController.parentLobby.getLocalUser().getGarage(), spawnState, true, battlefieldPlayerController.tank.id, battlefieldPlayerController.parentLobby.getLocalUser().getNickname(), battlefieldPlayerController.parentLobby.getLocalUser().getRang()));
            }else{
                this.sendToAllPlayers(Type.BATTLE, "res1_user", JSONUtils.parseTankData(this, battlefieldPlayerController, battlefieldPlayerController.parentLobby.getLocalUser().getGarage(), spawnState, true, battlefieldPlayerController.tank.id, battlefieldPlayerController.parentLobby.getLocalUser().getNickname(), battlefieldPlayerController.parentLobby.getLocalUser().getRang()));
            }
        }else{
            this.sendToAllPlayers(Type.BATTLE, "res1_user", JSONUtils.parseTankData(this, battlefieldPlayerController, battlefieldPlayerController.parentLobby.getLocalUser().getGarage(), spawnState, true, battlefieldPlayerController.tank.id, battlefieldPlayerController.parentLobby.getLocalUser().getNickname(), battlefieldPlayerController.parentLobby.getLocalUser().getRang()));
        }
        this.statistics.changeStatistic(battlefieldPlayerController);
        if (!this.battleFinish && battlefieldPlayerController.tank.state != TankState.suicide) {
            battlefieldPlayerController.tank.state = TankState.suicide;
            this.spawnPlayerAfterGarage(battlefieldPlayerController, spawnState);
        }
    }


    public void moveTank(BattlefieldPlayerController controller) {
        String json = JSONUtils.parseMoveCommand(controller);
        this.sendToAllPlayers(Type.BATTLE, "move", json);
    }

    public void spawnPlayer(BattlefieldPlayerController controller, Vector3 pos) {
        if (this.battleFinish) {
            return;
        }
        if (!battleInfo.isPaid || battleInfo.inventory) {
            this.bonusesSpawnService.sendAlreadyDroppedBonuses(controller);
            this.bonusesSpawnService.displayAllDroppzones(controller);
        }
        TankRespawnScheduler.startRespawn(controller, true);
    }

    public void spawnPlayerAfterGarage(final BattlefieldPlayerController player, final Vector3 vector3) {
        if (!this.battleFinish) {
            TankRespawnScheduler.startRespawn(player, false);
        }
    }

    public void setupTank(BattlefieldPlayerController controller) {
        controller.tank.id = controller.parentLobby.getLocalUser().getNickname();
    }

    public void addPlayer(BattlefieldPlayerController controller) {
        this.setupTank(controller);
        this.players.put(controller.tank.id, controller);
        battlesGC.cancelRemoving(this);
    }

    public void removeUser(BattlefieldPlayerController controller, boolean cache) {
        controller.clearEffects();
        this.battleMinesModel.playerDied(controller);
        this.players.remove(controller.parentLobby.getLocalUser().getNickname(), controller);
        if (!cache) {
            if (!this.battleInfo.team) {
                --battlesList.getBattleInfoById(this.battleInfo.battleId).countPeople;
            } else if (controller.playerTeamType.equals("RED")) {
                --battlesList.getBattleInfoById(this.battleInfo.battleId).redPeople;
            } else {
                --battlesList.getBattleInfoById(this.battleInfo.battleId).bluePeople;
            }
        }
        test(controller);
        this.sendToAllPlayers(Type.BATTLE, "remove_user", controller.tank.id);
        if (this.players.size() == 0) {
            if (SystemBattlesHandler.newbieBattleToEnter != null &&
                    SystemBattlesHandler.middleBattle != null &&
                    SystemBattlesHandler.forAllBattle != null) {
                if (battleInfo.battleId == SystemBattlesHandler.newbieBattleToEnter.battleId ||
                        battleInfo.battleId == SystemBattlesHandler.middleBattle.battleId ||
                        battleInfo.battleId == SystemBattlesHandler.forAllBattle.battleId) {
                    return;
                }
            }
            battlesGC.addBattleForRemove(this);
        }
    }

    private void test(BattlefieldPlayerController controller) {
        if (this.ctfModel != null && controller.flag != null) {
            this.ctfModel.dropFlag(controller, controller.tank.position);
        }
        if (this.domModel != null) {
            String pointId = null;
            for (DominationPoint point : this.domModel.getPoints()) {
                if (!point.getUserIds().contains(controller.getUser().getNickname())) continue;
                pointId = point.getId();
                break;
            }
            if (pointId != null) {
                this.domModel.tankLeaveCapturingPoint(controller, pointId);
            }
        }
        if(this.jgrModel != null){
            if(!this.queueRespawn){
                if(this.jgrModel.isUserJuggernaut(controller)){
                    this.jgrModel.userKillJuggernaut(controller);
                }
            }
        }
    }

    public void initLocalTank(BattlefieldPlayerController controller) {
        controller.userInited = true;
        Vector3 pos = SpawnManager.getSpawnState(this.battleInfo.map, controller.playerTeamType);
        if (this.battleInfo.battleType.equals("CTF")) {
            controller.send(Type.BATTLE, "init_ctf_model", JSONUtils.parseCTFModelData(this));
        }
        if (this.battleInfo.battleType.equals("DOM")) {
            this.domModel.sendInitData(controller);
        }
        if (this.battleInfo.battleType.equals("JGR")){
            if(this.battleInfo.model.jgrModel.juggernaut.owner == null && this.battleInfo.model.jgrModel.juggernaut.queuedOwner == null && this.battleInfo.model.players.size() > JGR_MIN_PLAYERS){
                this.battleInfo.model.jgrModel.setUserJuggernaut(controller);
            }
        }
        controller.send(Type.BATTLE, "init_gui_model", JSONUtils.parseBattleData(this));
        controller.inventory.init();
        this.battleMinesModel.initModel(controller);
        this.battleMinesModel.sendMines(controller);
        this.sendAllTanks(controller);
        if(controller.tank.isJuggernaut){
            this.sendToAllPlayers(Type.BATTLE, "init_tank", JSONUtils.parseJuggernautTankData(this, controller, controller.parentLobby.getLocalUser().getGarage(), pos, true, controller.tank.id, controller.parentLobby.getLocalUser().getNickname(), controller.parentLobby.getLocalUser().getRang()));
        }else{
            this.sendToAllPlayers(Type.BATTLE, "init_tank", JSONUtils.parseTankData(this, controller, controller.parentLobby.getLocalUser().getGarage(), pos, true, controller.tank.id, controller.parentLobby.getLocalUser().getNickname(), controller.parentLobby.getLocalUser().getRang()));
        }
        this.statistics.changeStatistic(controller);
        this.effectsModel.sendInitData(controller);
        this.spawnPlayer(controller, pos);
    }

    public void sendUserLogMessage(String idUser, String message) {
        this.sendToAllPlayers(Type.BATTLE, "user_log", idUser, message);
    }

    public void sendAllTanks(BattlefieldPlayerController controller) {
        for (BattlefieldPlayerController player : this.players.values()) {
            if (player == controller || !player.userInited) {
                continue;
            }
            
            if(player.tank.isJuggernaut){
                controller.send(Type.BATTLE, "init_tank", JSONUtils.parseJuggernautTankData(this, player, player.parentLobby.getLocalUser().getGarage(), player.tank.position, false, player.tank.id, player.parentLobby.getLocalUser().getNickname(), player.parentLobby.getLocalUser().getRang()));
            }else{
                controller.send(Type.BATTLE, "init_tank", JSONUtils.parseTankData(this, player, player.parentLobby.getLocalUser().getGarage(), player.tank.position, false, player.tank.id, player.parentLobby.getLocalUser().getNickname(), player.parentLobby.getLocalUser().getRang()));
            }
            this.statistics.changeStatistic(player);
        }
    }

    public void sendAllTanks(SpectatorController controller) {
        for (BattlefieldPlayerController player : this.players.values()) {
            if (!player.userInited) continue;
            
            if(player.tank.isJuggernaut){
                controller.sendCommand(Type.BATTLE, "init_tank", JSONUtils.parseJuggernautTankData(this, player, player.parentLobby.getLocalUser().getGarage(), player.tank.position, false, player.tank.id, player.parentLobby.getLocalUser().getNickname(), player.parentLobby.getLocalUser().getRang()));
            }else{
                controller.sendCommand(Type.BATTLE, "init_tank", JSONUtils.parseTankData(this, player, player.parentLobby.getLocalUser().getGarage(), player.tank.position, false, player.tank.id, player.parentLobby.getLocalUser().getNickname(), player.parentLobby.getLocalUser().getRang()));
            }
            controller.sendCommand(Type.BATTLE, "init_tank", JSONUtils.parseTankData(this, player, player.parentLobby.getLocalUser().getGarage(), player.tank.position, false, player.tank.id, player.parentLobby.getLocalUser().getNickname(), player.parentLobby.getLocalUser().getRang()));
            this.statistics.changeStatistic(player);
        }
    }

    public void activateTank(BattlefieldPlayerController player) {
        player.tank.state = TankState.active;
        this.sendToAllPlayers(Type.BATTLE, "activate_tank", player.tank.id);
    }

    public BattlefieldPlayerController getPlayer(String id) {
        return this.players.get(id);
    }

    public void sendToAllPlayers(Type type, String... args) {
        if (this.players == null) {
            return;
        }
        if (this.players.size() != 0) {
            for (BattlefieldPlayerController player : this.players.values()) {
                if (!player.userInited) continue;
                player.send(type, args);
            }
        }
        this.spectatorModel.sendCommandToSpectators(type, args);
    }

    public void sendToAllPlayers(BattlefieldPlayerController other, Type type, String... args) {
        if (this.players.size() != 0) {
            for (BattlefieldPlayerController player : this.players.values()) {
                if (!player.userInited || player == other) continue;
                player.send(type, args);
            }
        }
        this.spectatorModel.sendCommandToSpectators(type, args);
    }

    public void cheatDetected(BattlefieldPlayerController player, Class<?> anticheat) {
        AnticheatModel[] model = (AnticheatModel[]) anticheat.getAnnotationsByType(AnticheatModel.class);
        if (model != null && model.length >= 1) {
            loggerService.log(LogType.INFO, "Detected cheater![" + model[0].name() + "] " + player.getUser().getNickname() + " " + player.parentLobby.session.getIP());
        }
        this.kickPlayer(player);
    }

    public void kickPlayer(BattlefieldPlayerController player) {
        player.send(Type.BATTLE, "kick_for_cheats");
        player.parentLobby.session.closeConnection();
    }

    public void setTank(BattlefieldPlayerController player, Tank newTank) {
        this.players.get(player.parentLobby.getLocalUser().getNickname()).tank = newTank;
    }

    @Override
    public void destroy() {
        this.players.clear();
        this.activeBonuses.clear();
        quartzService.deleteJob(this.QUARTZ_NAME, QUARTZ_GROUP);
        this.tanksKillModel.destroy();
        this.tanksKillModel = null;
        this.players = null;
        this.activeBonuses = null;
        this.battleInfo = null;
        this.spectatorModel = null;
        if (this.domModel != null) {
            this.domModel.destroy();
        }
    }
}

