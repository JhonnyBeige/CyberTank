package gtanks.battles.bonuses;

import gtanks.battles.BattlefieldModel;

public class GoldSchedule implements Runnable
{
    public long time = 28000;
    public int inc;
    Bonus bonus;
    BattlefieldModel battlefieldModel;
    public GoldSchedule(BattlefieldModel bfModel, Bonus bonusToDropp, int incNum) {
        new Thread(this).start();
		battlefieldModel = bfModel;
        this.inc = incNum;
        this.bonus = bonusToDropp;
    }
    
    @Override
    public void run() {
        try {
            Thread.sleep(this.time);
            this.battlefieldModel.spawnBonus(bonus, this.inc, 9999);
            new Thread(this).stop();
        }
        catch (InterruptedException var3) {
            var3.printStackTrace();
        }
    }
    
}
