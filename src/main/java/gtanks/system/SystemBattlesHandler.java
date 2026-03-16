package gtanks.system;

import gtanks.battles.maps.MapsLoaderService;
import gtanks.lobby.battles.BattleInfo;
import gtanks.lobby.battles.BattlesList;

public class SystemBattlesHandler {
    public static BattleInfo newbieBattleToEnter;
    public static BattleInfo middleBattle;
    public static BattleInfo forAllBattle;
    public static BattlesList battleList = BattlesList.getInstance();
    public static BattlesGC battlesGC = BattlesGC.getInstance();

    public static void systemBattlesInit() {
        SystemBattlesHandler.newbiesMapConfigSetup();
        SystemBattlesHandler.middleMapConfigSetup();
        SystemBattlesHandler.noLimitMapConfigSetup();
        SystemBattlesHandler.aleksandrovsk();
        SystemBattlesHandler.atra();
        SystemBattlesHandler.barda();
        SystemBattlesHandler.bridges();
        SystemBattlesHandler.deathtrack();
        SystemBattlesHandler.desert();
        SystemBattlesHandler.fortknox();
        SystemBattlesHandler.fusion();
        SystemBattlesHandler.future();
        SystemBattlesHandler.highland();
        SystemBattlesHandler.indzone();
        SystemBattlesHandler.iran();
        SystemBattlesHandler.kungur();
        SystemBattlesHandler.kungur2();
        SystemBattlesHandler.madness();
        SystemBattlesHandler.molotov();
        SystemBattlesHandler.montecarlo();
        SystemBattlesHandler.novel();
        SystemBattlesHandler.pass();
        SystemBattlesHandler.polygon();
        SystemBattlesHandler.sandal();
        SystemBattlesHandler.rio();
        SystemBattlesHandler.sandbox();
        SystemBattlesHandler.serpuhov();
        SystemBattlesHandler.wolfenstein();
        SystemBattlesHandler.boombox();
        SystemBattlesHandler.cross();
        SystemBattlesHandler.farm();
        SystemBattlesHandler.deck9();
        SystemBattlesHandler.island();
        SystemBattlesHandler.magadan();
    }

    public static void newbiesMapConfigSetup() {
        BattleInfo battle = new BattleInfo();
        battle.unremoveable = true;
        battle.battleType = "DM";
        battle.team = false;
        battle.numKills = 15;
        battle.minRank = 1;
        battle.maxRank = 4;
        battle.isPaid = false;
        battle.isPrivate = false;
        battle.friendlyFire = false;
        battle.withoutBonuses = true;
        battle.name = "For newbies DM";
        battle.map = MapsLoaderService.maps.get("map_sandbox");
        battle.maxPeople = 12;
        battle.autobalance = true;
        battle.equipmentChange = true;
        battle.time = 600;
        battleList.tryCreateBatle(battle);
        battlesGC.cancelRemoving(battle.model);
        newbieBattleToEnter = battleList.getBattleInfoById(battle.battleId);
    }

    public static void middleMapConfigSetup() {
        BattleInfo battle = new BattleInfo();
        battle.unremoveable = true;
        battle.battleType = "CTF";
        battle.team = true;
        battle.minRank = 5;
        battle.maxRank = 27;
        battle.isPaid = true;
        battle.isPrivate = false;
        battle.friendlyFire = false;
        battle.withoutBonuses = false;
        battle.inventory = false;
        battle.name = "Sandbox CTF XP/BP";
        battle.map = MapsLoaderService.maps.get("map_sandbox");
        battle.maxPeople = 4;
        battle.numFlags = 7;
        battle.autobalance = true;
        battle.equipmentChange = false;
        battle.battleFormat = 1;
        battle.time = 999;
        battleList.tryCreateBatle(battle);
        battlesGC.cancelRemoving(battle.model);
        middleBattle = battleList.getBattleInfoById(battle.battleId);
    }

    public static void noLimitMapConfigSetup() {
        BattleInfo battle = new BattleInfo();
        battle.unremoveable = true;
        battle.battleType = "DM";
        battle.team = false;
        battle.numKills = 300;
        battle.minRank = 1;
        battle.maxRank = 31;
        battle.isPaid = false;
        battle.isPrivate = false;
        battle.friendlyFire = false;
        battle.withoutBonuses = false;
        battle.inventory = true;
        battle.name = "Barda DM";
        battle.map = MapsLoaderService.maps.get("map_barda");
        battle.maxPeople = 26;
        battle.autobalance = true;
        battle.time = 1500;
        battle.microUpgrades = true;
        battle.equipmentChange = true;
        battleList.tryCreateBatle(battle);
        battlesGC.cancelRemoving(battle.model);
        forAllBattle = battleList.getBattleInfoById(battle.battleId);
    }

    public static void aleksandrovsk() {
        BattleInfo battle = new BattleInfo();
        battle.unremoveable = true;
        battle.battleType = "CTF";
        battle.team = true;
        battle.minRank = 1;
        battle.maxRank = 5;
        battle.isPaid = false;
        battle.isPrivate = false;
        battle.friendlyFire = false;
        battle.withoutBonuses = false;
        battle.inventory = true;
        battle.name = "Aleksandrovsk CTF";
        battle.map = MapsLoaderService.maps.get("map_alexandrovsk");
        battle.maxPeople = 11;
        battle.numFlags = 15;
        battle.autobalance = true;
        battle.equipmentChange = true;
        battle.time = 720;
        battle.microUpgrades = true;
        battleList.tryCreateBatle(battle);
        battlesGC.cancelRemoving(battle.model);
        BotsService.getInstance().addBot("Flaviusbatatare", battle);
        BotsService.getInstance().addBot("LindiLegerMen", battle);
        BotsService.getInstance().addBot("wzx13813812138", battle);
        BotsService.getInstance().addBot("elcan_killer_hard", battle);
        BotsService.getInstance().addBot("LEON", battle);
        BotsService.getInstance().addBot("VIRGIL", battle);
    }

    public static void sandbox() {
        BattleInfo battle = new BattleInfo();
        battle.unremoveable = true;
        battle.battleType = "CTF";
        battle.team = true;
        battle.minRank = 1;
        battle.maxRank = 5;
        battle.isPaid = false;
        battle.isPrivate = false;
        battle.friendlyFire = false;
        battle.withoutBonuses = false;
        battle.inventory = true;
        battle.name = "Sandbox CTF";
        battle.map = MapsLoaderService.maps.get("map_sandbox");
        battle.maxPeople = 4;
        battle.numFlags = 15;
        battle.autobalance = true;
        battle.equipmentChange = true;
        battle.time = 720;
        battle.microUpgrades = true;
        battleList.tryCreateBatle(battle);
        battlesGC.cancelRemoving(battle.model);
        BotsService.getInstance().addBot("Andy730", battle);
        BotsService.getInstance().addBot("Jageroou", battle);
        BotsService.getInstance().addBot("BRADY", battle);
        BotsService.getInstance().addBot("winston", battle);
    }

    public static void cross() {
        BattleInfo battle = new BattleInfo();
        battle.unremoveable = true;
        battle.battleType = "CTF";
        battle.team = true;
        battle.minRank = 1;
        battle.maxRank = 5;
        battle.isPaid = false;
        battle.isPrivate = false;
        battle.friendlyFire = false;
        battle.withoutBonuses = false;
        battle.inventory = true;
        battle.name = "Cross CTF";
        battle.map = MapsLoaderService.maps.get("map_cross");
        battle.maxPeople = 4;
        battle.numFlags = 15;
        battle.autobalance = true;
        battle.equipmentChange = true;
        battle.time = 720;
        battle.microUpgrades = true;
        battleList.tryCreateBatle(battle);
        battlesGC.cancelRemoving(battle.model);
        BotsService.getInstance().addBot("Joshua", battle);
        BotsService.getInstance().addBot("Cooper", battle);
        BotsService.getInstance().addBot("ROBERTO", battle);
        BotsService.getInstance().addBot("NoScopeLegend", battle);
        BotsService.getInstance().addBot("BILL", battle);
    }

    public static void highland() {
        BattleInfo battle = new BattleInfo();
        battle.unremoveable = true;
        battle.battleType = "CTF";
        battle.team = true;
        battle.minRank = 6;
        battle.maxRank = 9;
        battle.isPaid = false;
        battle.isPrivate = false;
        battle.friendlyFire = false;
        battle.withoutBonuses = false;
        battle.inventory = true;
        battle.name = "Highland CTF";
        battle.map = MapsLoaderService.maps.get("map_highland");
        battle.maxPeople = 6;
        battle.numFlags = 15;
        battle.autobalance = true;
        battle.equipmentChange = true;
        battle.time = 720;
        battle.microUpgrades = true;
        battleList.tryCreateBatle(battle);
        battlesGC.cancelRemoving(battle.model);
        BotsService.getInstance().addBot("C.A.H.9I", battle);
        BotsService.getInstance().addBot("netanel123", battle);
        BotsService.getInstance().addBot("xameron1", battle);
        BotsService.getInstance().addBot("FV4005", battle);
        BotsService.getInstance().addBot("VK7201K", battle);
    }

    public static void deck9() {
        BattleInfo battle = new BattleInfo();
        battle.unremoveable = true;
        battle.battleType = "CTF";
        battle.team = true;
        battle.minRank = 6;
        battle.maxRank = 9;
        battle.isPaid = false;
        battle.isPrivate = false;
        battle.friendlyFire = false;
        battle.withoutBonuses = false;
        battle.inventory = true;
        battle.name = "Deck 9 CTF";
        battle.map = MapsLoaderService.maps.get("map_deck9");
        battle.maxPeople = 4;
        battle.numFlags = 15;
        battle.autobalance = true;
        battle.equipmentChange = true;
        battle.time = 720;
        battle.microUpgrades = true;
        battleList.tryCreateBatle(battle);
        battlesGC.cancelRemoving(battle.model);
        BotsService.getInstance().addBot("ANDREW", battle);
        BotsService.getInstance().addBot("FrankClark", battle);
        BotsService.getInstance().addBot("Malik", battle);
    }

    public static void desert() {
        BattleInfo battle = new BattleInfo();
        battle.unremoveable = true;
        battle.battleType = "CTF";
        battle.team = true;
        battle.minRank = 10;
        battle.maxRank = 14;
        battle.isPaid = false;
        battle.isPrivate = false;
        battle.friendlyFire = false;
        battle.withoutBonuses = false;
        battle.inventory = true;
        battle.name = "Desert CTF";
        battle.map = MapsLoaderService.maps.get("map_desert");
        battle.maxPeople = 12;
        battle.numFlags = 15;
        battle.autobalance = true;
        battle.equipmentChange = true;
        battle.time = 720;
        battle.microUpgrades = true;
        battleList.tryCreateBatle(battle);
        battlesGC.cancelRemoving(battle.model);
        BotsService.getInstance().addBot("Inceptimash444", battle);
        BotsService.getInstance().addBot("grom_gt_hard", battle);
        BotsService.getInstance().addBot("TeliTalicska", battle);
        BotsService.getInstance().addBot("Barninokak", battle);
        BotsService.getInstance().addBot("OTIS", battle);
        BotsService.getInstance().addBot("Chieftain", battle);
    }

    public static void farm() {
        BattleInfo battle = new BattleInfo();
        battle.unremoveable = true;
        battle.battleType = "CTF";
        battle.team = true;
        battle.minRank = 10;
        battle.maxRank = 14;
        battle.isPaid = false;
        battle.isPrivate = false;
        battle.friendlyFire = false;
        battle.withoutBonuses = false;
        battle.inventory = true;
        battle.name = "Farm CTF";
        battle.map = MapsLoaderService.maps.get("map_farm");
        battle.maxPeople = 4;
        battle.numFlags = 15;
        battle.autobalance = true;
        battle.equipmentChange = true;
        battle.time = 720;
        battle.microUpgrades = true;
        battleList.tryCreateBatle(battle);
        battlesGC.cancelRemoving(battle.model);
        BotsService.getInstance().addBot("Abel", battle);
        BotsService.getInstance().addBot("Badger", battle);
        BotsService.getInstance().addBot("WARREN", battle);
    }

    public static void indzone() {
        BattleInfo battle = new BattleInfo();
        battle.unremoveable = true;
        battle.battleType = "CTF";
        battle.team = true;
        battle.minRank = 15;
        battle.maxRank = 18;
        battle.isPaid = false;
        battle.isPrivate = false;
        battle.friendlyFire = false;
        battle.withoutBonuses = false;
        battle.inventory = true;
        battle.name = "Industrial Zone CTF";
        battle.map = MapsLoaderService.maps.get("map_industrial_zone");
        battle.maxPeople = 12;
        battle.numFlags = 15;
        battle.autobalance = true;
        battle.equipmentChange = true;
        battle.time = 720;
        battle.microUpgrades = true;
        battleList.tryCreateBatle(battle);
        battlesGC.cancelRemoving(battle.model);
        BotsService.getInstance().addBot("SouthWest112", battle);
        BotsService.getInstance().addBot("s_Sn1ckerS_s", battle);
        BotsService.getInstance().addBot("LLL_555", battle);
        BotsService.getInstance().addBot("D3s_1", battle);
        BotsService.getInstance().addBot("Nelson", battle);
        BotsService.getInstance().addBot("DEXTER", battle);
    }

    public static void boombox() {
        BattleInfo battle = new BattleInfo();
        battle.unremoveable = true;
        battle.battleType = "CTF";
        battle.team = true;
        battle.minRank = 15;
        battle.maxRank = 18;
        battle.isPaid = false;
        battle.isPrivate = false;
        battle.friendlyFire = false;
        battle.withoutBonuses = false;
        battle.inventory = true;
        battle.name = "Boombox CTF";
        battle.map = MapsLoaderService.maps.get("map_boombox");
        battle.maxPeople = 4;
        battle.numFlags = 15;
        battle.autobalance = true;
        battle.equipmentChange = true;
        battle.time = 720;
        battle.microUpgrades = true;
        battleList.tryCreateBatle(battle);
        battlesGC.cancelRemoving(battle.model);
        BotsService.getInstance().addBot("Tiger_Commander", battle);
        BotsService.getInstance().addBot("FaZe_Recruit", battle);
        BotsService.getInstance().addBot("IS7_King", battle);
    }

    public static void atra() {
        BattleInfo battle = new BattleInfo();
        battle.unremoveable = true;
        battle.battleType = "CTF";
        battle.team = true;
        battle.minRank = 19;
        battle.maxRank = 21;
        battle.isPaid = false;
        battle.isPrivate = false;
        battle.friendlyFire = false;
        battle.withoutBonuses = false;
        battle.inventory = true;
        battle.name = "Atra CTF";
        battle.map = MapsLoaderService.maps.get("map_atra");
        battle.maxPeople = 10;
        battle.numFlags = 15;
        battle.autobalance = true;
        battle.equipmentChange = true;
        battle.time = 720;
        battle.microUpgrades = true;
        battleList.tryCreateBatle(battle);
        battlesGC.cancelRemoving(battle.model);
        BotsService.getInstance().addBot("chymazaya", battle);
        BotsService.getInstance().addBot("TOGRUL", battle);
        BotsService.getInstance().addBot("E100_Lover", battle);
        BotsService.getInstance().addBot("Anatolik", battle);
        BotsService.getInstance().addBot("MARVIN", battle);
        BotsService.getInstance().addBot("360trickshotter", battle);
    }

    public static void hill() {
        BattleInfo battle = new BattleInfo();
        battle.unremoveable = true;
        battle.battleType = "CTF";
        battle.team = true;
        battle.minRank = 19;
        battle.maxRank = 21;
        battle.isPaid = false;
        battle.isPrivate = false;
        battle.friendlyFire = false;
        battle.withoutBonuses = false;
        battle.inventory = true;
        battle.name = "Hill CTF";
        battle.map = MapsLoaderService.maps.get("map_hill");
        battle.maxPeople = 4;
        battle.numFlags = 15;
        battle.autobalance = true;
        battle.equipmentChange = true;
        battle.time = 720;
        battle.microUpgrades = true;
        battleList.tryCreateBatle(battle);
        battlesGC.cancelRemoving(battle.model);
        BotsService.getInstance().addBot("Obj268v4", battle);
        BotsService.getInstance().addBot("T95_DoomTurtle", battle);
        BotsService.getInstance().addBot("FV4005_StageII", battle);
    }

    public static void magadan() {
        BattleInfo battle = new BattleInfo();
        battle.unremoveable = true;
        battle.battleType = "CTF";
        battle.team = true;
        battle.minRank = 22;
        battle.maxRank = 25;
        battle.isPaid = false;
        battle.isPrivate = false;
        battle.friendlyFire = false;
        battle.withoutBonuses = false;
        battle.inventory = true;
        battle.name = "Magadan CTF";
        battle.map = MapsLoaderService.maps.get("map_magadan");
        battle.maxPeople = 4;
        battle.numFlags = 15;
        battle.autobalance = true;
        battle.equipmentChange = true;
        battle.time = 720;
        battle.microUpgrades = true;
        battleList.tryCreateBatle(battle);
        battlesGC.cancelRemoving(battle.model);
        BotsService.getInstance().addBot("VK7201", battle);
        BotsService.getInstance().addBot("Obj907", battle);
        BotsService.getInstance().addBot("BatChat25t", battle);
    }

    public static void iran() {
        BattleInfo battle = new BattleInfo();
        battle.unremoveable = true;
        battle.battleType = "CTF";
        battle.team = true;
        battle.minRank = 22;
        battle.maxRank = 25;
        battle.isPaid = false;
        battle.isPrivate = false;
        battle.friendlyFire = false;
        battle.withoutBonuses = false;
        battle.inventory = true;
        battle.name = "Iran CTF";
        battle.map = MapsLoaderService.maps.get("map_iran");
        battle.maxPeople = 4;
        battle.numFlags = 15;
        battle.autobalance = true;
        battle.equipmentChange = true;
        battle.time = 720;
        battle.microUpgrades = true;
        battleList.tryCreateBatle(battle);
        battlesGC.cancelRemoving(battle.model);
        BotsService.getInstance().addBot("CLARENCE", battle);
        BotsService.getInstance().addBot("mamarauldanonino", battle);
        BotsService.getInstance().addBot("R.O.B.E.R.T", battle);
        BotsService.getInstance().addBot("AwesomeNinja", battle);
        BotsService.getInstance().addBot("Kranvagn", battle);
        BotsService.getInstance().addBot("Grille15", battle);
    }

    public static void montecarlo() {
        BattleInfo battle = new BattleInfo();
        battle.unremoveable = true;
        battle.battleType = "CTF";
        battle.team = true;
        battle.minRank = 25;
        battle.maxRank = 29;
        battle.isPaid = false;
        battle.isPrivate = false;
        battle.friendlyFire = false;
        battle.withoutBonuses = false;
        battle.inventory = true;
        battle.name = "Monte Carlo CTF";
        battle.map = MapsLoaderService.maps.get("map_monte_carlo");
        battle.maxPeople = 12;
        battle.numFlags = 15;
        battle.autobalance = true;
        battle.equipmentChange = true;
        battle.time = 720;
        battle.microUpgrades = true;
        battleList.tryCreateBatle(battle);
        battlesGC.cancelRemoving(battle.model);
        BotsService.getInstance().addBot("TIMOTHY", battle);
        BotsService.getInstance().addBot("salvador", battle);
        BotsService.getInstance().addBot("UltimateDestroyer", battle);
        BotsService.getInstance().addBot("Lonnie", battle);
        BotsService.getInstance().addBot("M48_Patton", battle);
        BotsService.getInstance().addBot("CentAX", battle);
    }

    public static void novel() {
        BattleInfo battle = new BattleInfo();
        battle.unremoveable = true;
        battle.battleType = "CTF";
        battle.team = true;
        battle.minRank = 25;
        battle.maxRank = 29;
        battle.isPaid = false;
        battle.isPrivate = false;
        battle.friendlyFire = false;
        battle.withoutBonuses = false;
        battle.inventory = true;
        battle.name = "Novel CTF";
        battle.map = MapsLoaderService.maps.get("map_novel");
        battle.maxPeople = 9;
        battle.numFlags = 15;
        battle.autobalance = true;
        battle.equipmentChange = true;
        battle.time = 720;
        battle.microUpgrades = true;
        battleList.tryCreateBatle(battle);
        battlesGC.cancelRemoving(battle.model);
        BotsService.getInstance().addBot("JAMES", battle);
        BotsService.getInstance().addBot("SupremeVictor", battle);
        BotsService.getInstance().addBot("WZ111_5A", battle);
        BotsService.getInstance().addBot("Obj277", battle);
    }

    public static void combe() {
        BattleInfo battle = new BattleInfo();
        battle.unremoveable = true;
        battle.battleType = "CTF";
        battle.team = true;
        battle.minRank = 25;
        battle.maxRank = 29;
        battle.isPaid = false;
        battle.isPrivate = false;
        battle.friendlyFire = false;
        battle.withoutBonuses = false;
        battle.inventory = true;
        battle.name = "Combe CTF";
        battle.map = MapsLoaderService.maps.get("map_combe");
        battle.maxPeople = 4;
        battle.numFlags = 15;
        battle.autobalance = true;
        battle.equipmentChange = true;
        battle.time = 720;
        battle.microUpgrades = true;
        battleList.tryCreateBatle(battle);
        battlesGC.cancelRemoving(battle.model);
        BotsService.getInstance().addBot("FV217_Badger", battle);
        BotsService.getInstance().addBot("JgPzE100", battle);
        BotsService.getInstance().addBot("T92", battle);
    }

    public static void pass() {
        BattleInfo battle = new BattleInfo();
        battle.unremoveable = true;
        battle.battleType = "CTF";
        battle.team = true;
        battle.minRank = 31;
        battle.maxRank = 31;
        battle.isPaid = false;
        battle.isPrivate = false;
        battle.friendlyFire = false;
        battle.withoutBonuses = false;
        battle.inventory = true;
        battle.name = "Pass CTF";
        battle.map = MapsLoaderService.maps.get("map_pass");
        battle.maxPeople = 7;
        battle.numFlags = 15;
        battle.autobalance = true;
        battle.equipmentChange = true;
        battle.time = 720;
        battle.microUpgrades = true;
        battleList.tryCreateBatle(battle);
        battlesGC.cancelRemoving(battle.model);
        BotsService.getInstance().addBot("Matthew.Harris", battle);
        BotsService.getInstance().addBot("DanielJackson", battle);
        BotsService.getInstance().addBot("sergio", battle);
        BotsService.getInstance().addBot("Glitchr", battle);
        BotsService.getInstance().addBot("Zorp_Unit", battle);
    }

    public static void island() {
        BattleInfo battle = new BattleInfo();
        battle.unremoveable = true;
        battle.battleType = "CTF";
        battle.team = true;
        battle.minRank = 31;
        battle.maxRank = 31;
        battle.isPaid = false;
        battle.isPrivate = false;
        battle.friendlyFire = false;
        battle.withoutBonuses = false;
        battle.inventory = true;
        battle.name = "Island CTF";
        battle.map = MapsLoaderService.maps.get("map_island");
        battle.maxPeople = 4;
        battle.numFlags = 15;
        battle.autobalance = true;
        battle.equipmentChange = true;
        battle.time = 720;
        battle.microUpgrades = true;
        battleList.tryCreateBatle(battle);
        battlesGC.cancelRemoving(battle.model);
        BotsService.getInstance().addBot("JibberX", battle);
        BotsService.getInstance().addBot("DribbleX", battle);
        BotsService.getInstance().addBot("Quuzzle", battle);
    }

    public static void kungur() {
        BattleInfo battle = new BattleInfo();
        battle.unremoveable = true;
        battle.battleType = "DM";
        battle.team = false;
        battle.numKills = 50;
        battle.minRank = 1;
        battle.maxRank = 5;
        battle.isPaid = false;
        battle.isPrivate = false;
        battle.friendlyFire = false;
        battle.withoutBonuses = false;
        battle.inventory = true;
        battle.name = "Kungur DM";
        battle.map = MapsLoaderService.maps.get("map_kungur");
        battle.maxPeople = 24;
        battle.autobalance = true;
        battle.time = 600;
        battle.microUpgrades = true;
        battle.equipmentChange = true;
        battleList.tryCreateBatle(battle);
        battlesGC.cancelRemoving(battle.model);
        BotsService.getInstance().addBot("DALLAS1147", battle);
        BotsService.getInstance().addBot("Zefffrry", battle);
        BotsService.getInstance().addBot("Sandisr", battle);
        BotsService.getInstance().addBot("057rus", battle);
        BotsService.getInstance().addBot("JamesJones", battle);
        BotsService.getInstance().addBot("LARRY", battle);
    }

    public static void serpuhov() {
        BattleInfo battle = new BattleInfo();
        battle.unremoveable = true;
        battle.battleType = "DM";
        battle.team = false;
        battle.numKills = 50;
        battle.minRank = 6;
        battle.maxRank = 10;
        battle.isPaid = false;
        battle.isPrivate = false;
        battle.friendlyFire = false;
        battle.withoutBonuses = false;
        battle.inventory = true;
        battle.name = "Serpuhov DM";
        battle.map = MapsLoaderService.maps.get("map_serpuhov");
        battle.maxPeople = 20;
        battle.autobalance = true;
        battle.time = 600;
        battle.microUpgrades = true;
        battle.equipmentChange = true;
        battleList.tryCreateBatle(battle);
        battlesGC.cancelRemoving(battle.model);
        BotsService.getInstance().addBot("Ashraf.Mohsen", battle);
        BotsService.getInstance().addBot("Hend777", battle);
        BotsService.getInstance().addBot("ooyoot", battle);
        BotsService.getInstance().addBot("ISAIAH", battle);
        BotsService.getInstance().addBot("JOSE", battle);
    }

    public static void deathtrack() {
        BattleInfo battle = new BattleInfo();
        battle.unremoveable = true;
        battle.battleType = "DM";
        battle.team = false;
        battle.numKills = 50;
        battle.minRank = 10;
        battle.maxRank = 15;
        battle.isPaid = false;
        battle.isPrivate = false;
        battle.friendlyFire = false;
        battle.withoutBonuses = false;
        battle.inventory = true;
        battle.name = "Deathtrack DM";
        battle.map = MapsLoaderService.maps.get("map_deathtrack");
        battle.maxPeople = 28;
        battle.autobalance = true;
        battle.time = 600;
        battle.microUpgrades = true;
        battle.equipmentChange = true;
        battleList.tryCreateBatle(battle);
        battlesGC.cancelRemoving(battle.model);
        BotsService.getInstance().addBot("Givenchy_123", battle);
        BotsService.getInstance().addBot("illkickyou", battle);
        BotsService.getInstance().addBot("riadAZE808", battle);
        BotsService.getInstance().addBot("B5_W8", battle);
        BotsService.getInstance().addBot("opticspecialist", battle);
        BotsService.getInstance().addBot("Sheridan105", battle);
    }

    public static void wolfenstein() {
        BattleInfo battle = new BattleInfo();
        battle.unremoveable = true;
        battle.battleType = "DM";
        battle.team = false;
        battle.numKills = 50;
        battle.minRank = 15;
        battle.maxRank = 18;
        battle.isPaid = false;
        battle.isPrivate = false;
        battle.friendlyFire = false;
        battle.withoutBonuses = false;
        battle.inventory = true;
        battle.name = "Wolfenstein DM";
        battle.map = MapsLoaderService.maps.get("map_wolfenstein");
        battle.maxPeople = 24;
        battle.autobalance = true;
        battle.time = 600;
        battle.microUpgrades = true;
        battle.equipmentChange = true;
        battleList.tryCreateBatle(battle);
        battlesGC.cancelRemoving(battle.model);
        // BotsService.getInstance().addBot("MAREKOS", battle);
        // BotsService.getInstance().addBot("kakvydostali", battle);
        // BotsService.getInstance().addBot("B_E_C_H_A", battle);
        // BotsService.getInstance().addBot("Renn1x", battle);
        // BotsService.getInstance().addBot("ScopeKing", battle);
        // BotsService.getInstance().addBot("DeadeyeAssassin", battle);
    }

    public static void fortknox() {
        BattleInfo battle = new BattleInfo();
        battle.unremoveable = true;
        battle.battleType = "DM";
        battle.team = false;
        battle.numKills = 50;
        battle.minRank = 19;
        battle.maxRank = 21;
        battle.isPaid = false;
        battle.isPrivate = false;
        battle.friendlyFire = false;
        battle.withoutBonuses = false;
        battle.inventory = true;
        battle.name = "Fort Knox DM";
        battle.map = MapsLoaderService.maps.get("map_fort_knox");
        battle.maxPeople = 24;
        battle.autobalance = true;
        battle.time = 600;
        battle.microUpgrades = true;
        battle.equipmentChange = true;
        battleList.tryCreateBatle(battle);
        battlesGC.cancelRemoving(battle.model);
        BotsService.getInstance().addBot("Lo9haim", battle);
        BotsService.getInstance().addBot("Wenentta", battle);
        BotsService.getInstance().addBot("Somersby", battle);
        BotsService.getInstance().addBot("Super_Conqueror", battle);
        BotsService.getInstance().addBot("Maus_Monster", battle);
        BotsService.getInstance().addBot("Parz1val.55", battle);
    }

    public static void madness() {
        BattleInfo battle = new BattleInfo();
        battle.unremoveable = true;
        battle.battleType = "DM";
        battle.team = false;
        battle.numKills = 50;
        battle.minRank = 22;
        battle.maxRank = 25;
        battle.isPaid = false;
        battle.isPrivate = false;
        battle.friendlyFire = false;
        battle.withoutBonuses = false;
        battle.inventory = true;
        battle.name = "Madness DM";
        battle.map = MapsLoaderService.maps.get("map_madness");
        battle.maxPeople = 32;
        battle.autobalance = true;
        battle.time = 600;
        battle.microUpgrades = true;
        battle.equipmentChange = true;
        battleList.tryCreateBatle(battle);
        battlesGC.cancelRemoving(battle.model);
        BotsService.getInstance().addBot("InfiniteObliterator", battle);
        BotsService.getInstance().addBot("FLOYD", battle);
        BotsService.getInstance().addBot("J.O.S.E.P.H", battle);
        BotsService.getInstance().addBot("DONALD", battle);
        BotsService.getInstance().addBot("TVP_T50_51", battle);
        BotsService.getInstance().addBot("Progetto46", battle);
    }

    public static void polygon() {
        BattleInfo battle = new BattleInfo();
        battle.unremoveable = true;
        battle.battleType = "DM";
        battle.team = false;
        battle.numKills = 50;
        battle.minRank = 26;
        battle.maxRank = 30;
        battle.isPaid = false;
        battle.isPrivate = false;
        battle.friendlyFire = false;
        battle.withoutBonuses = false;
        battle.inventory = true;
        battle.name = "Polygon DM";
        battle.map = MapsLoaderService.maps.get("map_polygon");
        battle.maxPeople = 12;
        battle.autobalance = true;
        battle.time = 600;
        battle.microUpgrades = true;
        battle.equipmentChange = true;
        battleList.tryCreateBatle(battle);
        battlesGC.cancelRemoving(battle.model);
        BotsService.getInstance().addBot("elliot", battle);
        BotsService.getInstance().addBot("T110E5", battle);
        BotsService.getInstance().addBot("AMX50B", battle);
        BotsService.getInstance().addBot("David.Davis", battle);
    }
    
    public static void sandal() {
        BattleInfo battle = new BattleInfo();
        battle.unremoveable = true;
        battle.battleType = "DM";
        battle.team = false;
        battle.numKills = 50;
        battle.minRank = 30;
        battle.maxRank = 31;
        battle.isPaid = false;
        battle.isPrivate = false;
        battle.friendlyFire = false;
        battle.withoutBonuses = false;
        battle.inventory = true;
        battle.name = "Sandal DM";
        battle.map = MapsLoaderService.maps.get("map_sandal");
        battle.maxPeople = 14;
        battle.autobalance = true;
        battle.time = 600;
        battle.microUpgrades = true;
        battle.equipmentChange = true;
        battleList.tryCreateBatle(battle);
        battlesGC.cancelRemoving(battle.model);
        forAllBattle = battleList.getBattleInfoById(battle.battleId);
        BotsService.getInstance().addBot("ANDY", battle);
        BotsService.getInstance().addBot("William.Williams", battle);
        BotsService.getInstance().addBot("STEVEN", battle);
        BotsService.getInstance().addBot("T95", battle);
        BotsService.getInstance().addBot("Zoogle", battle);
    }

    public static void fusion() {
        BattleInfo battle = new BattleInfo();
        battle.unremoveable = true;
        battle.battleType = "TDM";
        battle.team = true;
        battle.minRank = 6;
        battle.maxRank = 9;
        battle.isPaid = false;
        battle.isPrivate = false;
        battle.friendlyFire = false;
        battle.withoutBonuses = false;
        battle.inventory = true;
        battle.name = "Fusion TDM";
        battle.map = MapsLoaderService.maps.get("map_fusion");
        battle.maxPeople = 18;
        battle.numKills = 150;
        battle.autobalance = true;
        battle.equipmentChange = true;
        battle.time = 720;
        battle.microUpgrades = true;
        battleList.tryCreateBatle(battle);
        battlesGC.cancelRemoving(battle.model);
        BotsService.getInstance().addBot("BoB-XII", battle);
        BotsService.getInstance().addBot("VadossKiller", battle);
        BotsService.getInstance().addBot("ALLOOHE", battle);
        BotsService.getInstance().addBot("PHILIP", battle);
        BotsService.getInstance().addBot("Jeffrey-Robinson", battle);
    }

    public static void bridges() {
        BattleInfo battle = new BattleInfo();
        battle.unremoveable = true;
        battle.battleType = "TDM";
        battle.team = true;
        battle.minRank = 10;
        battle.maxRank = 14;
        battle.isPaid = false;
        battle.isPrivate = false;
        battle.friendlyFire = false;
        battle.withoutBonuses = false;
        battle.inventory = true;
        battle.name = "Birdges TDM";
        battle.map = MapsLoaderService.maps.get("map_bridges");
        battle.maxPeople = 24;
        battle.numKills = 150;
        battle.autobalance = true;
        battle.equipmentChange = true;
        battle.time = 720;
        battle.microUpgrades = true;
        battleList.tryCreateBatle(battle);
        battlesGC.cancelRemoving(battle.model);
        BotsService.getInstance().addBot("Guus2", battle);
        BotsService.getInstance().addBot("aboba", battle);
        BotsService.getInstance().addBot("fertienne", battle);
        BotsService.getInstance().addBot("maks1m4321", battle);
        BotsService.getInstance().addBot("T110E3", battle);
        BotsService.getInstance().addBot("AMX105AM", battle);
    }

    public static void future() {
        BattleInfo battle = new BattleInfo();
        battle.unremoveable = true;
        battle.battleType = "TDM";
        battle.team = true;
        battle.minRank = 10;
        battle.maxRank = 14;
        battle.isPaid = false;
        battle.isPrivate = false;
        battle.friendlyFire = false;
        battle.withoutBonuses = false;
        battle.inventory = true;
        battle.name = "Future TDM";
        battle.map = MapsLoaderService.maps.get("map_future");
        battle.maxPeople = 18;
        battle.numKills = 150;
        battle.autobalance = true;
        battle.equipmentChange = true;
        battle.time = 720;
        battle.microUpgrades = true;
        battleList.tryCreateBatle(battle);
        battlesGC.cancelRemoving(battle.model);
        BotsService.getInstance().addBot("Raziel", battle);
        BotsService.getInstance().addBot("Poccelmash", battle);
        BotsService.getInstance().addBot("Ingilo", battle);
        BotsService.getInstance().addBot("ScopeKing", battle);
        BotsService.getInstance().addBot("DeadeyeAssassin", battle);
        
    }

    public static void barda() {
        BattleInfo battle = new BattleInfo();
        battle.unremoveable = true;
        battle.battleType = "TDM";
        battle.team = true;
        battle.minRank = 19;
        battle.maxRank = 21;
        battle.isPaid = false;
        battle.isPrivate = false;
        battle.friendlyFire = false;
        battle.withoutBonuses = false;
        battle.inventory = true;
        battle.name = "Barda TDM";
        battle.map = MapsLoaderService.maps.get("map_barda");
        battle.maxPeople = 20;
        battle.numKills = 150;
        battle.autobalance = true;
        battle.equipmentChange = true;
        battle.time = 720;
        battle.microUpgrades = true;
        battleList.tryCreateBatle(battle);
        battlesGC.cancelRemoving(battle.model);
        BotsService.getInstance().addBot("Cama_OTTacHocTb", battle);
        BotsService.getInstance().addBot("Noquit", battle);
        BotsService.getInstance().addBot("gurukilowik", battle);
        BotsService.getInstance().addBot("epicnoscoper", battle);
        BotsService.getInstance().addBot("420noscoper", battle);
    }

    public static void kungur2() {
        BattleInfo battle = new BattleInfo();
        battle.unremoveable = true;
        battle.battleType = "TDM";
        battle.team = true;
        battle.minRank = 22;
        battle.maxRank = 25;
        battle.isPaid = false;
        battle.isPrivate = false;
        battle.friendlyFire = false;
        battle.withoutBonuses = false;
        battle.inventory = true;
        battle.name = "Kungur TDM";
        battle.map = MapsLoaderService.maps.get("map_kungur");
        battle.maxPeople = 24;
        battle.numKills = 150;
        battle.autobalance = true;
        battle.equipmentChange = true;
        battle.time = 720;
        battle.microUpgrades = true;
        battleList.tryCreateBatle(battle);
        battlesGC.cancelRemoving(battle.model);
        BotsService.getInstance().addBot("CHRISTOPHER", battle);
        BotsService.getInstance().addBot("tyrone", battle);
        BotsService.getInstance().addBot("JPE100", battle);
        BotsService.getInstance().addBot("Sheridan", battle);
        BotsService.getInstance().addBot("M.I.C.H.A.E.L", battle);
    }

    public static void molotov() {
        BattleInfo battle = new BattleInfo();
        battle.unremoveable = true;
        battle.battleType = "TDM";
        battle.team = true;
        battle.minRank = 26;
        battle.maxRank = 28;
        battle.isPaid = false;
        battle.isPrivate = false;
        battle.friendlyFire = false;
        battle.withoutBonuses = false;
        battle.inventory = true;
        battle.name = "Molotov TDM";
        battle.map = MapsLoaderService.maps.get("map_molotov");
        battle.maxPeople = 20;
        battle.numKills = 150;
        battle.autobalance = true;
        battle.equipmentChange = true;
        battle.time = 720;
        battle.microUpgrades = true;
        battleList.tryCreateBatle(battle);
        battlesGC.cancelRemoving(battle.model);
        BotsService.getInstance().addBot("RadWizard", battle);
        BotsService.getInstance().addBot("gordon", battle);
        BotsService.getInstance().addBot("Leopard1", battle);
        BotsService.getInstance().addBot("STB1", battle);
        BotsService.getInstance().addBot("John-Smith", battle);
    }

    public static void rio() {
        BattleInfo battle = new BattleInfo();
        battle.unremoveable = true;
        battle.battleType = "TDM";
        battle.team = true;
        battle.minRank = 31;
        battle.maxRank = 31;
        battle.isPaid = false;
        battle.isPrivate = false;
        battle.friendlyFire = false;
        battle.withoutBonuses = false;
        battle.inventory = true;
        battle.name = "Rio TDM";
        battle.map = MapsLoaderService.maps.get("map_rio");
        battle.maxPeople = 24;
        battle.numKills = 150;
        battle.autobalance = true;
        battle.equipmentChange = true;
        battle.time = 720;
        battle.microUpgrades = true;
        battleList.tryCreateBatle(battle);
        battlesGC.cancelRemoving(battle.model);
        BotsService.getInstance().addBot("isaac", battle);
        BotsService.getInstance().addBot("EpicSniper", battle);
        BotsService.getInstance().addBot("DEAN", battle);
        BotsService.getInstance().addBot("Snazzle", battle);
        BotsService.getInstance().addBot("WobbleX", battle);
        BotsService.getInstance().addBot("Charles-Taylor", battle);
        
    }


}
