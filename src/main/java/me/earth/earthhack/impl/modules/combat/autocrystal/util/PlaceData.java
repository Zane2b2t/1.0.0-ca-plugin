//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\hp\Desktop\coding\mappings"!

//Decompiled by Procyon!

package me.earth.earthhack.impl.modules.combat.autocrystal.util;

import java.util.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.util.math.*;

public class PlaceData
{
    private final List<Entity> entities;
    private float damage;
    private float selfDamage;
    private EntityPlayer target;
    private BlockPos pos;
    
    public PlaceData(final EntityPlayer target, final List<Entity> crystals) {
        this.selfDamage = -2.0f;
        this.target = target;
        this.entities = crystals;
    }
    
    public EntityPlayer getTarget() {
        return this.target;
    }
    
    public void setTarget(final EntityPlayer target) {
        this.target = target;
    }
    
    public float getDamage() {
        return this.damage;
    }
    
    public void setDamage(final float damage) {
        this.damage = damage;
    }
    
    public BlockPos getPos() {
        return this.pos;
    }
    
    public void setPos(final BlockPos pos) {
        this.pos = pos;
    }
    
    public float getSelfDamage() {
        return this.selfDamage;
    }
    
    public void setSelfDamage(final float selfDamage) {
        this.selfDamage = selfDamage;
    }
    
    public List<Entity> getEntities() {
        return this.entities;
    }
}
