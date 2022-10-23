

package me.earth.earthhack.impl.modules.combat.autocrystal.util;

import net.minecraft.entity.*;

public class BreakData
{
    private float damage;
    private Entity crystal;
    private int count;
    private Entity fallBack;
    private float fallBackDamage;
    private int minDmgCount;
    
    public BreakData() {
        this.fallBackDamage = 1000.0f;
    }
    
    public int getCount() {
        return this.count;
    }
    
    public int getMinDmgCount() {
        return this.minDmgCount;
    }
    
    public void incrementMinDmgCount() {
        ++this.minDmgCount;
    }
    
    public void increment() {
        ++this.count;
    }
    
    public float getDamage() {
        return this.damage;
    }
    
    public Entity getCrystal() {
        return this.crystal;
    }
    
    public Entity getFallBack() {
        return this.fallBack;
    }
    
    public void setFallBack(final Entity fallBack, final float damage) {
        if (this.fallBackDamage > damage) {
            this.fallBackDamage = damage;
            this.fallBack = fallBack;
        }
    }
    
    public float getFallBackDamage() {
        return this.fallBackDamage;
    }
    
    public void setCrystal(final Entity crystal) {
        this.crystal = crystal;
    }
    
    public void setDamage(final float damage) {
        this.damage = damage;
    }
}
