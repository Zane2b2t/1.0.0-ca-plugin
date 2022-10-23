//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\hp\Desktop\coding\mappings"!

//Decompiled by Procyon!

package me.earth.earthhack.impl.modules.combat.autocrystal;

import me.earth.earthhack.impl.util.helpers.*;
import me.earth.earthhack.api.util.interfaces.*;
import net.minecraft.entity.player.*;
import me.earth.earthhack.impl.modules.combat.autocrystal.util.*;
import me.earth.earthhack.impl.modules.combat.autocrystal.modes.*;
import me.earth.earthhack.impl.util.minecraft.*;
import me.earth.earthhack.impl.util.minecraft.entity.*;
import net.minecraft.entity.*;
import me.earth.earthhack.impl.util.minecraft.blocks.*;
import java.util.*;
import me.earth.earthhack.impl.managers.*;
import me.earth.earthhack.impl.util.math.*;
import net.minecraft.util.math.*;
import net.minecraft.client.entity.*;
import net.minecraft.world.*;

final class HelperPlace extends Wrapper<AutoCrystal> implements Globals
{
    public HelperPlace(final AutoCrystal module) {
        super(module);
    }
    
    public PlaceData createData(final List<EntityPlayer> players, final List<Entity> crystals) {
        final PlaceData data = new PlaceData(((Target)((AutoCrystal)this.value).target.getValue()).getTarget(players, (float)((AutoCrystal)this.value).targetRange.getValue()), crystals);
        if (data.getTarget() != null || ((AutoCrystal)this.value).target.getValue() == Target.Damage) {
            this.evaluate(data, players);
        }
        return data;
    }
    
    private void evaluate(final PlaceData data, final List<EntityPlayer> players) {
        float self;
        final Iterator<EntityPlayer> iterator;
        EntityPlayer player;
        BlockUtil.sphere((float)((AutoCrystal)this.value).placeRange.getValue(), pos -> {
            if (this.isValid(data, pos)) {
                self = (((AutoCrystal)this.value).suicide.getValue() ? -1.0f : DamageUtil.calculate(pos));
                if (self < EntityUtil.getHealth((EntityLivingBase)HelperPlace.mc.player) - 1.0f) {
                    if ((self > (float)((AutoCrystal)this.value).maxSelfP.getValue() && !(boolean)((AutoCrystal)this.value).override.getValue()) || this.calcFriends(pos, players)) {
                        return false;
                    }
                    else if (((AutoCrystal)this.value).target.getValue() == Target.Damage) {
                        players.iterator();
                        while (iterator.hasNext()) {
                            player = iterator.next();
                            this.calc(pos, data, player, self);
                        }
                    }
                    else {
                        this.calc(pos, data, data.getTarget(), self);
                    }
                }
                else {
                    ((AutoCrystal)this.value).setUnsafe();
                }
            }
            return false;
        });
    }
    
    private void calc(final BlockPos pos, final PlaceData data, final EntityPlayer player, final float self) {
        if (!Managers.FRIENDS.contains(player) && this.isValid(player, pos)) {
            final float damage = DamageUtil.calculate(pos, (EntityLivingBase)this.getPlayer(player));
            if ((self <= (float)((AutoCrystal)this.value).maxSelfP.getValue() || ((boolean)((AutoCrystal)this.value).override.getValue() && damage > EntityUtil.getHealth((EntityLivingBase)player) + 1.0)) && (damage > data.getDamage() || (damage >= data.getDamage() && data.getSelfDamage() > self))) {
                data.setDamage(damage);
                data.setSelfDamage(self);
                data.setTarget(player);
                data.setPos(pos);
            }
        }
    }
    
    private boolean calcFriends(final BlockPos pos, final List<EntityPlayer> players) {
        if (((AutoCrystal)this.value).noFriendP.getValue()) {
            for (final EntityPlayer player : players) {
                if (this.isValid(player, pos) && Managers.FRIENDS.contains(player)) {
                    final float damage = DamageUtil.calculate(pos, (EntityLivingBase)this.getPlayer(player));
                    if (damage > EntityUtil.getHealth((EntityLivingBase)player) + 1.0) {
                        return true;
                    }
                    continue;
                }
            }
        }
        return false;
    }
    
    private boolean isValid(final EntityPlayer player, final BlockPos pos) {
        return player != null && !EntityUtil.isDead((Entity)player) && !player.equals((Object)HelperPlace.mc.player) && player.getDistanceSq(pos) <= MathUtil.square((float)((AutoCrystal)this.value).range.getValue());
    }
    
    private boolean isValid(final PlaceData data, final BlockPos pos) {
        return BlockUtil.getDistanceSq(pos) <= MathUtil.square((float)((AutoCrystal)this.value).placeRange.getValue()) && BlockUtil.canPlaceCrystal(pos, (boolean)((AutoCrystal)this.value).antiSurr.getValue(), (boolean)((AutoCrystal)this.value).newerVer.getValue(), this.getEntityList(data)) && (HelperPlace.mc.player.getDistanceSq(pos) <= MathUtil.square((float)((AutoCrystal)this.value).placeTrace.getValue()) || RayTraceUtil.raytracePlaceCheck((Entity)HelperPlace.mc.player, pos)) && this.combinedTraceCheck(pos);
    }
    
    private boolean combinedTraceCheck(final BlockPos pos) {
        return BlockUtil.getDistanceSq(pos) <= MathUtil.square((float)((AutoCrystal)this.value).pbTrace.getValue()) || RayTraceUtil.canBeSeen(new Vec3d(pos.getX() + 0.5, pos.getY() + 2.700000047683716, pos.getZ() + 0.5), (Entity)HelperPlace.mc.player);
    }
    
    private List<Entity> getEntityList(final PlaceData data) {
        return ((AutoCrystal)this.value).multiThread.getValue() ? data.getEntities() : null;
    }
    
    private EntityPlayer getPlayer(final EntityPlayer player) {
        if ((int)((AutoCrystal)this.value).interpolate.getValue() > 0) {
            return this.interpolate(player);
        }
        return player;
    }
    
    private EntityPlayer interpolate(final EntityPlayer player) {
        final Vec3d last = new Vec3d(player.lastTickPosX, player.lastTickPosY, player.lastTickPosZ);
        Vec3d current = player.getPositionVector();
        final Vec3d diff = last.subtract(current);
        if (diff.lengthSquared() < 0.001) {
            return player;
        }
        final EntityPlayer out = (EntityPlayer)new EntityOtherPlayerMP((World)HelperPlace.mc.world, player.getGameProfile());
        out.inventory.copyInventory(player.inventory);
        out.setHealth(EntityUtil.getHealth((EntityLivingBase)player));
        out.setAbsorptionAmount(player.getAbsorptionAmount());
        for (int i = 0; i < (int)((AutoCrystal)this.value).interpolate.getValue() && HelperPlace.mc.world.getBlockState(new BlockPos(current.add(diff))).getMaterial().isReplaceable(); current = current.add(diff), ++i) {}
        out.setPosition(current.x, current.y, current.z);
        return out;
    }
}
