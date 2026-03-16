/*
 * Decompiled with CFR 0.150.
 */
package gtanks.battles.tanks;

import gtanks.battles.BattlefieldPlayerController;
import gtanks.battles.effects.Effect;
import gtanks.battles.effects.EffectType;
import gtanks.battles.tanks.data.DamageTankData;
import gtanks.battles.tanks.hulls.Hull;
import gtanks.battles.tanks.loaders.HullsFactory;
import gtanks.battles.tanks.loaders.WeaponsFactory;
import gtanks.battles.tanks.math.Vector3;
import gtanks.battles.tanks.module.Module;
import gtanks.battles.tanks.module.ModuleFactory;
import gtanks.battles.tanks.weapons.IWeapon;
import gtanks.battles.tanks.weapons.frezee.effects.FrezeeEffectModel;

import java.util.*;

public class Tank {
    public static final int MAX_HEALTH_TANK = 10000;
    public Vector3 position;
    public Vector3 orientation;
    public Vector3 linVel;
    public Vector3 angVel;
    public double turretDir;
    public int controllBits;
    private IWeapon weapon;
    private Hull hull;
    // private Module module;
    private List<Module> modules;
    private float microUpgradesHealth;
    public String hullId;
    public String turretId;
    public String hullSkin;
    public String turretSkin;
    public String shotEffect;
    public String paintId;
    public String id;
    public float speed;
    public float turnSpeed;
    public float turretRotationSpeed;
    public int healthPoints = MAX_HEALTH_TANK;
    public float maxHp = 0;
    public BattlefieldPlayerController bfModel;

    public boolean isJuggernaut = false;

    public String state = TankState.newcome;
    public FrezeeEffectModel frezeeEffect;
    public ArrayList<Effect> activeEffects;
    public final Map<EffectType, Long> lockEffects = new HashMap<>();
    public LinkedHashMap<BattlefieldPlayerController, DamageTankData> lastDamagers;

    public Tank(Vector3 position) {
        this.position = position;
        this.activeEffects = new ArrayList();
        this.modules = new ArrayList();
        this.lastDamagers = new LinkedHashMap();
    }

    public IWeapon getWeaponInfo() {
        if(isJuggernaut){
            return WeaponsFactory.getInstance().getWeapon("terminator_m0", this.bfModel, this.bfModel.battle);
        }
        return this.weapon;
    }

    public Hull getHullInfo() {
        if(isJuggernaut){
            return HullsFactory.getInstance().getHull("juggernaut_m0");
        }
        return this.hull;
    }

    public void setWeapon(IWeapon weapon) {
        this.weapon = weapon;
        this.turretRotationSpeed = weapon.getEntity().getShotData().turretRotationSpeed;
    }

    public void setHull(Hull hull) {
        this.hull = hull;
        this.speed = hull.speed;
        this.turnSpeed = hull.turnSpeed;
    }

    public List<Module> getModules() {
        return this.modules;
    }

    // Add individual modules
    public void addModule(Module module) {
        if (this.modules.size() < 3) { // Limit to 3 modules
            this.modules.add(module);
        } else {
            throw new IllegalStateException("Cannot add more than 3 modules.");
        }
    }

    // Remove a specific module if needed
    public void removeModule(Module module) {
        this.modules.remove(module);
    }

    public Module getModule(int i) {
        if (i < 0 || i >= this.modules.size()) {
            return null;
        }
        return this.modules.get(i);
    }

    public void setMicroUpgradesHealth(float value) {
        this.microUpgradesHealth = value;
        this.maxHp += value;
    }

    public boolean isUsedEffect(EffectType type) {
        for (Effect effect : this.activeEffects) {
            if (effect.getEffectType() != type)
                continue;
            return true;
        }
        return false;
    }
}
