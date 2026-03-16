package gtanks.battles.jgr;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import gtanks.battles.BattlefieldModel;
import gtanks.battles.BattlefieldPlayerController;
import gtanks.battles.tanks.Tank;
import gtanks.battles.tanks.loaders.HullsFactory;
import gtanks.battles.tanks.loaders.WeaponsFactory;
import gtanks.battles.tanks.module.ModuleFactory;
import gtanks.commands.Type;
import gtanks.json.JSONUtils;
import gtanks.services.TanksServices;

public class JuggernautModel {
    private BattlefieldModel bfModel;
    private TanksServices tanksServices = TanksServices.getInstance();

    public JuggernautServer juggernaut = new JuggernautServer();

    public JuggernautModel(BattlefieldModel bfModel) {
        this.bfModel = bfModel;
    }

    public boolean isUserJuggernaut(BattlefieldPlayerController player) {
        return juggernaut.owner == player;
    }

    public boolean isUserQueuedJuggernaut(BattlefieldPlayerController player) {
        return juggernaut.queuedOwner == player;
    }

    public void setUserJuggernaut(BattlefieldPlayerController player) {
        juggernaut.owner = player;
        juggernaut.queuedOwner = null;
        this.bfModel.sendToAllPlayers(Type.BATTLE, "spawnjuggernaut", player.getUser().getNickname());
        player.tank.isJuggernaut = true;
        player.tank.setHull(HullsFactory.getInstance().getHull("juggernaut_m0"));
        player.tank.setWeapon(WeaponsFactory.getInstance().getWeapon("terminator_m0", player, bfModel));
        sendSpecData(player.tank);
    }

    public void userKillJuggernaut(BattlefieldPlayerController player) {
        BattlefieldPlayerController previousJGR = juggernaut.owner;
        if(player == juggernaut.owner){
            previousJGR.tank.isJuggernaut = false;
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    
            // Schedule a task to run after 5 seconds
            scheduler.schedule(() -> {
                previousJGR.parentLobby.send(Type.BATTLE, "init_suicide_garage");
            }, 5, TimeUnit.SECONDS);
    
            // Optional: shutdown the scheduler after the task execution
            scheduler.shutdown();
            juggernaut.owner = null;
            juggernaut.queuedOwner = null;
            juggernaut.kills = 0;
            this.bfModel.sendToAllPlayers(Type.BATTLE, "destroyedjuggernaut", "");
            return;
        }
        tanksServices.addScore(player.parentLobby, 40);
        player.parentLobby.send(Type.BATTLE, "init_suicide_garage");
        previousJGR.tank.isJuggernaut = false;
        previousJGR.tank.setHull(HullsFactory.getInstance().getHull(previousJGR.getGarage().mountHull.getId()));
        previousJGR.tank.setWeapon(WeaponsFactory.getInstance().getWeapon(previousJGR.getGarage().mountTurret.getId(), previousJGR, bfModel));
        sendSpecData(previousJGR.tank);
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        // Schedule a task to run after 5 seconds
        scheduler.schedule(() -> {
            previousJGR.parentLobby.send(Type.BATTLE, "init_suicide_garage");
        }, 5, TimeUnit.SECONDS);

        // Optional: shutdown the scheduler after the task execution
        scheduler.shutdown();
        juggernaut.queuedOwner = player;
        juggernaut.owner = null;
        juggernaut.kills = 0;
        this.bfModel.sendToAllPlayers(Type.BATTLE, "destroyedjuggernaut", player.getUser().getNickname());
    }

    public void restartBattle() {
        BattlefieldPlayerController previousJGR = juggernaut.owner;
        juggernaut.owner = null;
        juggernaut.queuedOwner = null;
        juggernaut.kills = 0;
    }

    public void sendSpecData(Tank tank) {
        this.bfModel.sendToAllPlayers(Type.BATTLE, "change_spec_tank", tank.id, JSONUtils.parseTankSpec(tank, false));
    }

    public void juggernautKillUser() {
        juggernaut.kills++;
        String alertType = "";
        switch (juggernaut.kills) {
            case 1:
                alertType = "firstkill";
                break;
            case 3:
                alertType = "hunttime";
                break;
            case 5:
                alertType = "domination";
                break;
            case 7:
                alertType = "enraged";
                break;
            case 10:
                alertType = "unstoppable";
                break;
            case 13:
                alertType = "insane";
                break;
            case 15:
                alertType = "crushing";
                break;
            case 17:
                alertType = "godlike";
                break;
            case 20:
                alertType = "makeitstop";
                break;
            default:
                break;
        }
        if(!alertType.equals("")){
            this.bfModel.sendToAllPlayers(Type.BATTLE, alertType, this.juggernaut.owner.getUser().getNickname());
        }
    }


}
