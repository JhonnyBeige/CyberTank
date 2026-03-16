package gtanks.lobby.matchmaking;

import java.util.List;
import java.util.Map;

import gtanks.battles.BattlefieldPlayerController;
import gtanks.battles.maps.MapsLoaderService;
import gtanks.lobby.LobbyManager;
import gtanks.lobby.battles.BattleInfo;
import gtanks.lobby.battles.BattlesList;
import gtanks.lobby.group.GroupServices;

public class MatchmakingServices {

    private static MatchmakingServices instance;

    private final static BattlesList battlesList = BattlesList.getInstance();

    public static MatchmakingServices getInstance() {
        if (instance == null) {
            instance = new MatchmakingServices();
        }
        return instance;
    }

    public void play(LobbyManager userLobby, String mode) {
        String userNickname = userLobby.getLocalUser().getNickname();
        // Find the group where the user is present
        String groupOwnerUsername = null;
        List<LobbyManager> userGroup = null;

        // Search for the group that contains the userLobby
        for (Map.Entry<String, List<LobbyManager>> entry : GroupServices.getInstance().groups.entrySet()) {
            if (entry.getKey().equals(userNickname)) {
                groupOwnerUsername = userNickname;
                userGroup = entry.getValue();
                handleGroupJoin(userLobby, mode, userGroup);
                break;
            }
        }

        if(groupOwnerUsername == null){
            handleSingleUserJoin(userLobby, mode);
        }
    }

    private void handleSingleUserJoin(LobbyManager userLobby, String mode) {
        MatchmakingBattle battle = getRandomBattle(mode, 0, userLobby);
        String battleId = battle.battleId;
        if(battlesList.getBattleInfoById(battleId).team){
            addUserToTeamBattle(userLobby, battleId, battle.red);
        }else{
            addUserToBattle(userLobby, battleId);
        }
    }

    private void handleGroupJoin(LobbyManager userLobby, String mode, List<LobbyManager> userGroup) {
        MatchmakingBattle battle = getRandomBattle(mode, userGroup.size(), userLobby);
        String battleId = battle.battleId;
        boolean redTeam = battle.red;
        addUserToTeamBattle(userLobby, battleId, redTeam);
        for (LobbyManager userInGroup : userGroup) {
            addUserToTeamBattle(userInGroup, battleId, redTeam);
        }
    }

    private void addUserToBattle(LobbyManager userLobby, String battleId) {
        userLobby.onEnterInBattle(battleId);
    }

    private void addUserToTeamBattle(LobbyManager userLobby, String battleId, boolean red) {
        userLobby.onEnterInTeamBattle(battleId, red);
    }

    private MatchmakingBattle getRandomBattle(String mode, int groupSize, LobbyManager userLobby){
        MatchmakingBattle info = new MatchmakingBattle();
        groupSize++;
        switch (mode) {
            case "HOLIDAY":
                for (BattleInfo battle : battlesList.battles) {
                    if (isBattleNormal(battle) && !battle.team && battle.battleType.equals("JGR") && (battle.maxRank >= userLobby.getLocalUser().getRang() + 1 && userLobby.getLocalUser().getRang() + 1 >= battle.minRank) && battle.battleId.contains("_hw_")) {
                        if(battle.model.players.size() <= (battle.maxPeople - groupSize)){
                            // count players in both teams
                            int playersCount = 0;
                            for (Map.Entry<String, BattlefieldPlayerController> entry : battle.model.players.entys()) {
                                playersCount++;
                            }
                            if(playersCount <= battle.maxPeople - groupSize) {
                                info.battleId = battle.battleId;
                            }
                        }
                    }
                }
                if(info.battleId.equals("")){
                    info.battleId = createNewBattle(mode, userLobby);
                }
                break;
            case "TEAM_MODE":
                // Quick play random battle team
                for (BattleInfo battle : battlesList.battles) {
                    if (isBattleNormal(battle) && battle.team && (battle.maxRank >= userLobby.getLocalUser().getRang() + 1 && userLobby.getLocalUser().getRang() + 1 >= battle.minRank)) {
                        if(battle.model.players.size() <= (battle.maxPeople - groupSize)){
                            // count players in both teams
                            int bluePlayers = 0;
                            int redPlayers = 0;
                            for (Map.Entry<String, BattlefieldPlayerController> entry : battle.model.players.entys()) {
                                String playerName = entry.getKey();
                                BattlefieldPlayerController playerController = entry.getValue();
                                if(playerController.playerTeamType.equals("RED")){
                                    redPlayers++;
                                }else{
                                    bluePlayers++;
                                }
                            }
                            if(redPlayers <= battle.maxPeople / 2 - groupSize) {
                                info.battleId = battle.battleId;
                                info.red = true;
                            }else if(bluePlayers <= battle.maxPeople / 2 - groupSize) {
                                info.battleId = battle.battleId;
                                info.red = false;
                            }
                        }
                    }
                }
                if(info.battleId.equals("")){
                    info.battleId = createNewBattle(mode, userLobby);
                    info.red = true;
                }
                break;
            case "TDM_ONLY":
                for (BattleInfo battle : battlesList.battles) {
                    if (isBattleNormal(battle) && battle.team && battle.battleType.equals("TDM") && (battle.maxRank >= userLobby.getLocalUser().getRang() + 1 && userLobby.getLocalUser().getRang() + 1 >= battle.minRank)) {
                        if(battle.model.players.size() <= (battle.maxPeople - groupSize)){
                            // count players in both teams
                            int bluePlayers = 0;
                            int redPlayers = 0;
                            for (Map.Entry<String, BattlefieldPlayerController> entry : battle.model.players.entys()) {
                                String playerName = entry.getKey();
                                BattlefieldPlayerController playerController = entry.getValue();
                                if(playerController.playerTeamType.equals("RED")){
                                    redPlayers++;
                                }else{
                                    bluePlayers++;
                                }
                            }
                            if(redPlayers <= battle.maxPeople / 2 - groupSize) {
                                info.battleId = battle.battleId;
                                info.red = true;
                            }else if(bluePlayers <= battle.maxPeople / 2 - groupSize) {
                                info.battleId = battle.battleId;
                                info.red = false;
                            }
                        }
                    }
                }
                if(info.battleId.equals("")){
                    info.battleId = createNewBattle(mode, userLobby);
                    info.red = true;
                }
                break;
            case "DM_ONLY":
                for (BattleInfo battle : battlesList.battles) {
                    if (isBattleNormal(battle) && !battle.team && battle.battleType.equals("DM") && (battle.maxRank >= userLobby.getLocalUser().getRang() + 1 && userLobby.getLocalUser().getRang() + 1 >= battle.minRank)) {
                        if(battle.model.players.size() <= (battle.maxPeople - groupSize)){
                            // count players in both teams
                            int playersCount = 0;
                            for (Map.Entry<String, BattlefieldPlayerController> entry : battle.model.players.entys()) {
                                BattlefieldPlayerController playerController = entry.getValue();
                                playersCount++;
                            }
                            if(playersCount <= battle.maxPeople - groupSize) {
                                info.battleId = battle.battleId;
                            }
                        }
                    }
                }
                if(info.battleId.equals("")){
                    info.battleId = createNewBattle(mode, userLobby);
                }
                break;
            case "CTF_ONLY":
                for (BattleInfo battle : battlesList.battles) {
                    if (isBattleNormal(battle) && battle.team && battle.battleType.equals("CTF") && (battle.maxRank >= userLobby.getLocalUser().getRang() + 1 && userLobby.getLocalUser().getRang() + 1 >= battle.minRank)) {
                        if(battle.model.players.size() <= (battle.maxPeople - groupSize)){
                            // count players in both teams
                            int bluePlayers = 0;
                            int redPlayers = 0;
                            for (Map.Entry<String, BattlefieldPlayerController> entry : battle.model.players.entys()) {
                                String playerName = entry.getKey();
                                BattlefieldPlayerController playerController = entry.getValue();
                                if(playerController.playerTeamType.equals("RED")){
                                    redPlayers++;
                                }else{
                                    bluePlayers++;
                                }
                            }
                            if(redPlayers <= battle.maxPeople / 2 - groupSize) {
                                info.battleId = battle.battleId;
                                info.red = true;
                            }else if(bluePlayers <= battle.maxPeople / 2 - groupSize) {
                                info.battleId = battle.battleId;
                                info.red = false;
                            }
                        }
                    }
                }
                if(info.battleId.equals("")){
                    info.battleId = createNewBattle(mode, userLobby);
                    info.red = true;
                }
                break;
            case "CP_ONLY":
                for (BattleInfo battle : battlesList.battles) {
                    if (isBattleNormal(battle) && battle.team && battle.battleType.equals("CP") && (battle.maxRank >= userLobby.getLocalUser().getRang() + 1 && userLobby.getLocalUser().getRang() + 1 >= battle.minRank)) {
                        if(battle.model.players.size() <= (battle.maxPeople - groupSize)){
                            // count players in both teams
                            int bluePlayers = 0;
                            int redPlayers = 0;
                            for (Map.Entry<String, BattlefieldPlayerController> entry : battle.model.players.entys()) {
                                String playerName = entry.getKey();
                                BattlefieldPlayerController playerController = entry.getValue();
                                if(playerController.playerTeamType.equals("RED")){
                                    redPlayers++;
                                }else{
                                    bluePlayers++;
                                }
                            }
                            if(redPlayers <= battle.maxPeople / 2 - groupSize) {
                                info.battleId = battle.battleId;
                                info.red = true;
                            }else if(bluePlayers <= battle.maxPeople / 2 - groupSize) {
                                info.battleId = battle.battleId;
                                info.red = false;
                            }
                        }
                    }
                }
                if(info.battleId.equals("")){
                    info.battleId = createNewBattle(mode, userLobby);
                    info.red = true;
                }
                break;
            case "JGR_ONLY":
                for (BattleInfo battle : battlesList.battles) {
                    if (isBattleNormal(battle) && !battle.team && battle.battleType.equals("JGR") && (battle.maxRank >= userLobby.getLocalUser().getRang() + 1 && userLobby.getLocalUser().getRang() + 1 >= battle.minRank)) {
                        if(battle.model.players.size() <= (battle.maxPeople - groupSize)){
                            // count players in both teams
                            int playersCount = 0;
                            for (Map.Entry<String, BattlefieldPlayerController> entry : battle.model.players.entys()) {
                                playersCount++;
                            }
                            if(playersCount <= battle.maxPeople - groupSize) {
                                info.battleId = battle.battleId;
                            }
                        }
                    }
                }
                if(info.battleId.equals("")){
                    info.battleId = createNewBattle(mode, userLobby);
                }
                break;
            default:
                break;
        }
        return info;
    }

    private boolean isBattleNormal(BattleInfo battle){
        return !battle.isPaid && battle.microUpgrades && battle.equipmentChange && battle.battleFormat == 0;
    }

    private String createNewBattle(String mode, LobbyManager userLobby) {
        boolean team = false;
        String battleType = "DM";
        switch (mode) {
            case "TEAM_MODE":
                team = true;
                battleType = "CTF";
                break;
            case "TDM_ONLY":
                team = true;
                battleType = "TDM";
                break;
            case "DM_ONLY":
                team = false;
                battleType = "DM";
                break;
            case "CTF_ONLY":
                team = true;
                battleType = "CTF";
                break;
            case "CP_ONLY":
                team = true;
                battleType = "DOM";
                break;
            case "JGR_ONLY":
                team = false;
                battleType = "JGR";
                break;
            case "HOLIDAY":
                team = false;
                battleType = "JGR";
                break;
            default:
                break;
        }

        BattleInfo battle = new BattleInfo();
        battle.unremoveable = false;
        battle.battleType = battleType;
        battle.team = team;
        if(userLobby.getLocalUser().getRang() + 1 <= 5){
            battle.minRank = 1;
        }else{
            battle.minRank = userLobby.getLocalUser().getRang() - 5;
        }
        battle.maxRank = userLobby.getLocalUser().getRang() + 5;
        battle.isPaid = false;
        battle.isPrivate = false;
        battle.friendlyFire = false;
        battle.withoutBonuses = false;
        battle.inventory = true;
        battle.microUpgrades = true;
        if(mode.equals("HOLIDAY")){
            battle.map = MapsLoaderService.getRandomHalloweenMap();
        }else{
            battle.map = MapsLoaderService.getRandomMap();
            while (battle.map.maxPlayers <= 6) {
                battle.map = MapsLoaderService.getRandomMap();
            }
        }
        if(battleType.equals("CTF")){
            while(!battle.map.ctf && battle.map.maxPlayers <= 6){
                battle.map = MapsLoaderService.getRandomMap();
            }
            battle.numFlags = 7;
        }
        if(battleType.equals("DOM")){
            while(!battle.map.dom && battle.map.maxPlayers <= 6){
                battle.map = MapsLoaderService.getRandomMap();
            }
            battle.numFlags = 100;
        }
        if(battleType.equals("TDM")){
            while(!battle.map.tdm && battle.map.maxPlayers <= 6){
                battle.map = MapsLoaderService.getRandomMap();
            }
            battle.numKills = 250;
        }
        if(battleType.equals("DM")){
            battle.numKills = 50;
        }
        if(battleType.equals("JGR")){
            battle.numKills = 50;
        }
        battle.name = battle.map.name + " " + battleType;
        battle.maxPeople = battle.map.maxPlayers;
        battle.autobalance = true;
        battle.equipmentChange = true;
        battle.battleFormat = 0;
        battle.time = 600;
        //battle.time = 50;
        battlesList.tryCreateBatle(battle);
        return battle.battleId;
    }

}
