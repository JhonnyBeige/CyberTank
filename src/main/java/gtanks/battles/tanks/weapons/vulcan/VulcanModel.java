package gtanks.battles.tanks.weapons.vulcan;

import gtanks.RandomUtils;
import gtanks.battles.BattlefieldModel;
import gtanks.battles.BattlefieldPlayerController;
import gtanks.battles.anticheats.AnticheatModel;
import gtanks.battles.tanks.Tank;
import gtanks.battles.tanks.weapons.IEntity;
import gtanks.battles.tanks.weapons.IWeapon;
import gtanks.battles.tanks.weapons.WeaponUtils;
import gtanks.battles.tanks.weapons.WeaponWeakeningData;
import gtanks.battles.tanks.weapons.anticheats.FireableWeaponAnticheatModel;
import gtanks.commands.Type;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.util.Timer;
import java.util.TimerTask;

@AnticheatModel(
   name = "VulcanModel",
   actionInfo = "Child FireableWeaponAnticheatModel"
)
public class VulcanModel extends FireableWeaponAnticheatModel implements IWeapon {
   private BattlefieldModel bfModel;
   private BattlefieldPlayerController player;
   private VulcanEntity entity;
   private WeaponWeakeningData weakeingData;

   public VulcanModel(VulcanEntity entity, WeaponWeakeningData weakeingData, BattlefieldModel bfModel, BattlefieldPlayerController player) {
      super(entity.getShotData().reloadMsec);
      this.entity = entity;
      this.bfModel = bfModel;
      this.player = player;
      this.weakeingData = weakeingData;
   }

   public void fire(String json) {
      JSONParser js = new JSONParser();
      JSONObject jo = null;

      try {
         jo = (JSONObject) js.parse(json);
      } catch (ParseException var5) {
         var5.printStackTrace();
      }

      if (jo != null) {
         this.bfModel.fire(this.player, json);
         BattlefieldPlayerController victim = (BattlefieldPlayerController)this.bfModel.players.get(jo.get("victimId"));

         if (victim != null) {
            double distance = 1;//Double.parseDouble(String.valueOf(jo.get("distance")));
            this.onTarget(new BattlefieldPlayerController[]{victim}, (int) distance);
         }
      }
   }

   public void quickFire(String json) {}

   private Timer heatTimer;
   private boolean selfDestruct = false;

   public void startHeat() {
      System.out.println("[VulcanModel]::startHeat() Warning!");
      this.bfModel.sendToAllPlayers(Type.BATTLE, "change_temperature_tank", this.player.tank.id, String.valueOf(0.5));
      Tank tank = this.player.tank;
      selfDestruct = false;

      heatTimer = new Timer();
        heatTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                tank.healthPoints -= WeaponUtils.calculateHealth(tank, 100);
                changeHealth(tank, tank.healthPoints);

                if (tank.healthPoints <= 0) {
                    selfDestruct = true;
                    tank.healthPoints = 0;
                    bfModel.respawnPlayer(player, true);
                    heatTimer.cancel();
                }
            }
        }, 0, 325);
   }

   public void stopHeat() {
      System.out.println("[VulcanModel]::stopHeat() Warning!");
      this.bfModel.sendToAllPlayers(Type.BATTLE, "change_temperature_tank", this.player.tank.id, String.valueOf(0));
      if (heatTimer != null) {
         heatTimer.cancel();
      }
      if (!selfDestruct) {
         Timer postHeatTimer = new Timer();
         Tank tank = this.player.tank;
         postHeatTimer.scheduleAtFixedRate(new TimerTask() {
             int damageCount = 0;

             @Override
             public void run() {
                 tank.healthPoints -= WeaponUtils.calculateHealth(tank, 80);
                 changeHealth(tank, tank.healthPoints);
                 damageCount++;

                 if (damageCount >= 5 || tank.healthPoints <= 0) {
                     if (tank.healthPoints <= 0) {
                        selfDestruct = true;
                        tank.healthPoints = 0;
                        bfModel.respawnPlayer(player, true);
                     }
                     postHeatTimer.cancel();
                 }
             }
         }, 0, 275);
      }
   }

   public void changeHealth(Tank tank, int value) {
        if (tank != null) {
            tank.healthPoints = value;
            this.bfModel.sendToAllPlayers(Type.BATTLE, "change_health", tank.id, String.valueOf(tank.healthPoints));
        }
   }

   public void startFire(String json) {
      this.bfModel.sendToAllPlayers(Type.BATTLE, "start_fire", this.player.tank.id, json);
   }

   public void onTarget(BattlefieldPlayerController[] targetsTanks, int distance) {
      if (targetsTanks.length != 0) {
         if (targetsTanks.length > 1) {
            System.out.println("VulcanModel::onTarget() Warning! targetsTanks length = " + targetsTanks.length);
         }

         for (BattlefieldPlayerController target : targetsTanks) {
            if (target != null) {
               float damage = RandomUtils.getRandom(this.entity.damage_min, this.entity.damage_max) * 4;
               if ((double)distance >= this.weakeingData.minimumDamageRadius) {
                  damage = WeaponUtils.calculateDamageFromDistance(damage, (int)this.weakeingData.minimumDamagePercent);
               }

               this.bfModel.tanksKillModel.damageTank(target, this.player, damage, true);
            }
         }
      }
   }


   public IEntity getEntity() {
      return this.entity;
   }

   public void stopFire() {
      this.bfModel.stopFire(this.player);

   }
}
