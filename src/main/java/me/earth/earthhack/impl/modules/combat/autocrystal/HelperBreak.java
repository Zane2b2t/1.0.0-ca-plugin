//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\hp\Desktop\coding\mappings"!

//Decompiled by Procyon!

package me.earth.earthhack.impl.modules.combat.autocrystal;

import me.earth.earthhack.impl.util.helpers.*;
import me.earth.earthhack.api.util.interfaces.*;
import net.minecraft.entity.player.*;
import me.earth.earthhack.impl.modules.combat.autocrystal.util.*;
import me.earth.earthhack.impl.util.minecraft.*;
import me.earth.earthhack.impl.util.minecraft.entity.*;
import net.minecraft.entity.*;
import java.util.*;
import me.earth.earthhack.impl.managers.*;
import net.minecraft.entity.item.*;
import me.earth.earthhack.impl.util.math.*;

final class HelperBreak extends Wrapper<AutoCrystal> implements Globals
{
    public HelperBreak(final AutoCrystal module) {
        super(module);
    }
    
    public BreakData createData(final List<EntityPlayer> players, final List<Entity> crystals) {
        final BreakData data = new BreakData();
        for (final Entity crystal : crystals) {
            if (this.isValid(crystal)) {
                final float self = ((AutoCrystal)this.value).suicide.getValue() ? -1.0f : DamageUtil.calculate(crystal);
                final float health = EntityUtil.getHealth((EntityLivingBase)HelperBreak.mc.player);
                final boolean safe = self < health + 1.0;
                if ((safe || (boolean)((AutoCrystal)this.value).suicide.getValue()) && this.evaluate(data, crystal, players, self)) {
                    data.setFallBack(crystal, self);
                }
                if (safe) {
                    continue;
                }
                ((AutoCrystal)this.value).setUnsafe();
            }
        }
        return data;
    }
    
    private boolean evaluate(final BreakData data, final Entity crystal, final List<EntityPlayer> players, final float self) {
        boolean validFallBack = self <= (float)((AutoCrystal)this.value).fallbackDmg.getValue();
        boolean count = false;
        boolean countMin = false;
        final Entity previous = data.getCrystal();
        final float previousD = data.getDamage();
        for (final EntityPlayer player : players) {
            if (this.isValid(player, crystal)) {
                final float damage = DamageUtil.calculate(crystal, (EntityLivingBase)player);
                final boolean friend = Managers.FRIENDS.contains(player);
                if ((boolean)((AutoCrystal)this.value).noFriendP.getValue() && friend) {
                    if (damage > EntityUtil.getHealth((EntityLivingBase)player) + 1.0f) {
                        count = false;
                        countMin = false;
                        data.setCrystal(previous);
                        data.setDamage(previousD);
                        validFallBack = false;
                        break;
                    }
                    continue;
                }
                else {
                    if (friend || !this.counts(player, self, damage)) {
                        continue;
                    }
                    if (!count) {
                        count = (damage > (float)((AutoCrystal)this.value).minDamage.getValue() || ((boolean)((AutoCrystal)this.value).countMin.getValue() && damage > (float)((AutoCrystal)this.value).minFP.getValue()));
                    }
                    if (!countMin) {
                        countMin = (damage > (float)((AutoCrystal)this.value).minFP.getValue());
                    }
                    if (damage <= data.getDamage()) {
                        continue;
                    }
                    data.setCrystal(crystal);
                    data.setDamage(damage);
                }
            }
        }
        if (count) {
            data.increment();
        }
        if (countMin) {
            data.incrementMinDmgCount();
        }
        return validFallBack;
    }
    
    private boolean isValid(final Entity crystal) {
        if (crystal instanceof EntityEnderCrystal && !crystal.isDead) {
            final double distance = HelperBreak.mc.player.getDistanceSq(crystal);
            return distance <= MathUtil.square((float)((AutoCrystal)this.value).breakRange.getValue()) && (distance <= MathUtil.square((float)((AutoCrystal)this.value).breakTrace.getValue()) || HelperBreak.mc.player.canEntityBeSeen(crystal));
        }
        return false;
    }
    
    private boolean isValid(final EntityPlayer player, final Entity crystal) {
        return player != null && !EntityUtil.isDead((Entity)player) && !player.equals((Object)HelperBreak.mc.player) && player.getDistanceSq(crystal) <= MathUtil.square((float)((AutoCrystal)this.value).range.getValue());
    }
    
    private boolean counts(final EntityPlayer player, final float self, final float damage) {
        if (self > (float)((AutoCrystal)this.value).maxSelfB.getValue() && damage > (float)((AutoCrystal)this.value).breakMinDmg.getValue()) {
            final float otherH = EntityUtil.getHealth((EntityLivingBase)player);
            return ((boolean)((AutoCrystal)this.value).override.getValue() && damage > otherH + 1.0f) || ((boolean)((AutoCrystal)this.value).suicide.getValue() && (damage > (float)((AutoCrystal)this.value).minDamage.getValue() || (otherH < (float)((AutoCrystal)this.value).facePlace.getValue() && damage > (float)((AutoCrystal)this.value).minFP.getValue())));
        }
        return damage > self;
    }
}
