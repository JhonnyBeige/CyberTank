/*
 * Decompiled with CFR 0.150.
 */
package gtanks.battles.ctf.flags;

import java.util.ArrayList;

import gtanks.battles.BattlefieldPlayerController;
import gtanks.battles.ctf.FlagReturnTimer;
import gtanks.battles.ctf.flags.FlagState;
import gtanks.battles.tanks.math.Vector3;

public class FlagServer {
    public String flagTeamType;
    public BattlefieldPlayerController owner;
    public ArrayList<BattlefieldPlayerController> previousOwners = new ArrayList<BattlefieldPlayerController>();
    public Vector3 position;
    public Vector3 basePosition;
    public FlagState state;
    public FlagReturnTimer returnTimer;

    public void setOwner(BattlefieldPlayerController newOwner) {
        owner = newOwner;
        newOwner.flag = this;
        if(previousOwners.contains(newOwner)){
            previousOwners.remove(newOwner);
        }
    }

    public void dropFlag() {
        previousOwners.add(owner);
        owner = null;
    }

    public void previousOwnerDisconnect(BattlefieldPlayerController previousOwner) {
        if(previousOwners.contains(previousOwner)){
            previousOwners.remove(previousOwner);
        }
    }

    public void returnFlag() {
        if(owner != null){
            owner.flag = null;
            owner = null;
        }
        previousOwners.clear();
    }

    public void captureFlag() {
        previousOwners.clear();
        owner = null;
    }

    public void battleFinish() {
        owner.flag = null;
        owner = null;
        previousOwners.clear();
    }
}

