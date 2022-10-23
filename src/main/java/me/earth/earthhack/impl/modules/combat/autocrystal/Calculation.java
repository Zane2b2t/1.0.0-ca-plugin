//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\hp\Desktop\coding\mappings"!

//Decompiled by Procyon!

package me.earth.earthhack.impl.modules.combat.autocrystal;

import me.earth.earthhack.impl.util.helpers.*;
import me.earth.earthhack.api.util.interfaces.*;
import me.earth.earthhack.api.cache.*;
import me.earth.earthhack.impl.modules.combat.legswitch.*;
import me.earth.earthhack.impl.modules.combat.offhand.*;
import net.minecraft.network.*;
import net.minecraft.entity.player.*;
import java.util.concurrent.*;
import java.util.function.*;
import me.earth.earthhack.impl.managers.*;
import me.earth.earthhack.impl.util.minecraft.entity.*;
import net.minecraft.entity.*;
import me.earth.earthhack.impl.modules.combat.autocrystal.util.*;
import net.minecraft.init.*;
import net.minecraft.item.*;
import me.earth.earthhack.impl.util.minecraft.*;
import net.minecraft.util.*;
import net.minecraft.network.play.client.*;
import me.earth.earthhack.impl.modules.combat.offhand.modes.*;
import me.earth.earthhack.impl.util.minecraft.blocks.*;
import net.minecraft.entity.item.*;
import net.minecraft.util.math.*;
import me.earth.earthhack.impl.util.math.*;
import java.util.*;
import me.earth.earthhack.impl.modules.combat.autocrystal.modes.*;
import me.earth.earthhack.impl.modules.*;

final class Calculation extends Wrapper<AutoCrystal> implements Runnable, Globals
{
    private static final ModuleCache<LegSwitch> LEG_SWITCH;
    private static final ModuleCache<Offhand> OFFHAND;
    private final List<Packet<?>> packets;
    private final List<EntityPlayer> players;
    private final List<Entity> crystals;
    private final HelperBreak breakHelper;
    private final HelperPlace placeHelper;
    private BlockPos pos;
    private EntityPlayer target;
    private Entity crystal;
    private float[] rotations;
    private boolean attacking;
    private BreakData breakData;
    private boolean doneRotating;
    private final Random random;
    
    public Calculation(final AutoCrystal module, final List<EntityPlayer> players, final List<Entity> crystals) {
        super(module);
        this.packets = new CopyOnWriteArrayList<Packet<?>>();
        this.random = new Random();
        this.players = players;
        this.crystals = crystals;
        this.breakHelper = module.breakHelper;
        this.placeHelper = module.placeHelper;
    }
    
    @Override
    public void run() {
        if ((boolean)((AutoCrystal)this.value).legSwitch.getValue() && (boolean)Calculation.LEG_SWITCH.returnIfPresent((Function)LegSwitch::isActive, (Object)false)) {
            return;
        }
        if ((((Attack)((AutoCrystal)this.value).attack.getValue()).shouldCalc() || ((AutoCrystal)this.value).isSwitching()) && Managers.SWITCH.getLastSwitch() >= (int)((AutoCrystal)this.value).cooldown.getValue()) {
            boolean flag = false;
            ((AutoCrystal)this.value).setSafe = false;
            final int count = this.explode();
            if (count != 6 && (boolean)((AutoCrystal)this.value).place.getValue() && ((AutoCrystal)this.value).getPlaceTimer().passed((int)((AutoCrystal)this.value).placeDelay.getValue()) && (count < (int)((AutoCrystal)this.value).multiPlace.getValue() || (boolean)((AutoCrystal)this.value).antiSurr.getValue()) && this.shouldPlaceCalc()) {
                final PlaceData data = this.placeHelper.createData(this.players, this.crystals);
                if (this.checkPos(data.getPos(), count)) {
                    final float damage = data.getDamage();
                    if (damage > (float)((AutoCrystal)this.value).minDamage.getValue() || (EntityUtil.getHealth((EntityLivingBase)data.getTarget()) <= (float)((AutoCrystal)this.value).facePlace.getValue() && (!(boolean)((AutoCrystal)this.value).noFaceSpam.getValue() || this.attacking || (this.breakData != null && this.breakData.getMinDmgCount() == 0))) || (((AutoCrystal)this.value).shouldFacePlace() && (!(boolean)((AutoCrystal)this.value).noFaceSpam.getValue() || this.attacking || (this.breakData != null && this.breakData.getMinDmgCount() == 0)) && damage > 2.0)) {
                        this.target = data.getTarget();
                        this.place(data);
                        flag = true;
                    }
                }
            }
            else if ((boolean)((AutoCrystal)this.value).place.getValue() && (boolean)((AutoCrystal)this.value).useForPlace.getValue() && !flag && this.shouldPlaceCalc()) {
                final PlaceData data = this.placeHelper.createData(this.players, this.crystals);
                if (this.checkPos(data.getPos(), count)) {
                    final float damage = data.getDamage();
                    if (damage > (float)((AutoCrystal)this.value).minDamage.getValue() || (EntityUtil.getHealth((EntityLivingBase)data.getTarget()) <= (float)((AutoCrystal)this.value).facePlace.getValue() && (!(boolean)((AutoCrystal)this.value).noFaceSpam.getValue() || this.attacking || (this.breakData != null && this.breakData.getMinDmgCount() == 0))) || (((AutoCrystal)this.value).shouldFacePlace() && (!(boolean)((AutoCrystal)this.value).noFaceSpam.getValue() || this.attacking || (this.breakData != null && this.breakData.getMinDmgCount() == 0)) && damage > 2.0)) {
                        this.target = data.getTarget();
                        this.updatePlaceRotations(data);
                    }
                }
            }
        }
    }
    
    public boolean isRotating() {
        return this.rotations != null;
    }
    
    public float[] getRotations() {
        return this.rotations;
    }
    
    public List<Packet<?>> getPackets() {
        return this.packets;
    }
    
    public EntityPlayer getTarget() {
        return this.target;
    }
    
    private int explode() {
        this.breakData = this.breakHelper.createData(this.players, this.crystals);
        this.crystal = this.breakData.getCrystal();
        if (this.attack(this.crystal, this.breakData.getDamage()) && (!((Rotate)((AutoCrystal)this.value).rotate.getValue()).noRotate(Rotate.Place) || !(boolean)((AutoCrystal)this.value).multiTask.getValue())) {
            return 6;
        }
        return this.breakData.getCount();
    }
    
    private boolean attack(final Entity crystal, final float damage) {
        int delay = (int)((damage <= (float)((AutoCrystal)this.value).slowBreak.getValue()) ? ((AutoCrystal)this.value).slowDelay.getValue() : ((int)((AutoCrystal)this.value).breakDelay.getValue()));
        final EntityPlayer closest = EntityUtil.getClosestEnemy();
        if (crystal != null && closest != null && ((AutoCrystal)this.value).getTarget() != null && (boolean)((AutoCrystal)this.value).slowLegBreak.getValue() && PlayerUtil.isValidFootCrystal(crystal, closest) && PlayerUtil.isInHole(closest)) {
            delay = (int)((AutoCrystal)this.value).legDelay.getValue();
        }
        if ((boolean)((AutoCrystal)this.value).explode.getValue() && crystal != null && ((Attack)((AutoCrystal)this.value).attack.getValue()).shouldAttack() && ((AutoCrystal)this.value).getBreakTimer().passed(delay)) {
            Calculation.mc.addScheduledTask(() -> ((AutoCrystal)this.value).setCurrentCrystal(crystal));
            final CPacketUseEntity useEntity = new CPacketUseEntity(crystal);
            final CPacketAnimation animation = new CPacketAnimation(EnumHand.MAIN_HAND);
            ((AutoCrystal)this.value).getBreakTimer().reset(delay);
            this.attacking = true;
            if ((boolean)((AutoCrystal)this.value).multiThread.getValue() && ((AutoCrystal)this.value).rotate.getValue() == Rotate.None) {
                Calculation.mc.addScheduledTask((AutoCrystal)this.value::swing);
            }
            if ((boolean)((AutoCrystal)this.value).setDead.getValue() && !(boolean)((AutoCrystal)this.value).useYawLimit.getValue()) {
                crystal.setDead();
                if (((AutoCrystal)this.value).dangerous.getValue()) {
                    Calculation.mc.world.removeEntityDangerously(crystal);
                }
                ((AutoCrystal)this.value).killed.put(crystal.getEntityId(), new EntityTime(crystal, System.nanoTime()));
            }
            if (((Rotate)((AutoCrystal)this.value).rotate.getValue()).noRotate(Rotate.Break) || RotationUtil.isLegit(crystal) || RotationUtil.isLegitRaytrace(crystal, Managers.ROTATION.getServerYaw(), Managers.ROTATION.getServerPitch()) || Arrays.equals(new int[] { Math.round(Managers.ROTATION.getServerYaw()), Math.round(Managers.ROTATION.getServerPitch()) }, new int[] { Math.round(MathHelper.wrapDegrees(RotationUtil.getRotations(crystal)[0])), Math.round(RotationUtil.getRotations(crystal)[1]) })) {
                boolean flag = false;
                final int toolSlot = InventoryUtil.findHotbarItem(Items.DIAMOND_SWORD, Items.DIAMOND_PICKAXE);
                final int lastSlot = Calculation.mc.player.inventory.currentItem;
                if (!DamageUtil.canBreakWeakness(true) && toolSlot != -1) {
                    InventoryUtil.switchTo(toolSlot);
                    flag = true;
                }
                Calculation.mc.player.connection.sendPacket((Packet)useEntity);
                Calculation.mc.player.connection.sendPacket((Packet)animation);
                if (flag) {
                    InventoryUtil.switchTo(lastSlot);
                }
                return !(boolean)((AutoCrystal)this.value).multiTask.getValue();
            }
            if (((AutoCrystal)this.value).useYawLimit.getValue()) {
                final float[] rotation = RotationUtil.getRotationsMaxYaw(crystal, (float)((int)((AutoCrystal)this.value).limit.getValue() + (this.random.nextBoolean() ? (-this.random.nextInt((int)((AutoCrystal)this.value).jitter.getValue())) : this.random.nextInt((int)((AutoCrystal)this.value).jitter.getValue()))), Managers.ROTATION.getServerYaw());
                final float[] target = RotationUtil.getRotations(crystal);
                this.rotations = RotationUtil.getRotationsMaxYaw(crystal, (float)((int)((AutoCrystal)this.value).limit.getValue() + (this.random.nextBoolean() ? (-this.random.nextInt((int)((AutoCrystal)this.value).jitter.getValue())) : this.random.nextInt((int)((AutoCrystal)this.value).jitter.getValue()))), Managers.ROTATION.getServerYaw());
                if (Arrays.equals(this.rotations, RotationUtil.getRotations(crystal)) || RotationUtil.isLegitRaytrace(crystal, Managers.ROTATION.getServerYaw(), Managers.ROTATION.getServerPitch()) || RotationUtil.isLegit(crystal, Managers.ROTATION.getServerYaw(), Managers.ROTATION.getServerPitch()) || Arrays.equals(new int[] { Math.round(Managers.ROTATION.getServerYaw()), Math.round(Managers.ROTATION.getServerPitch()) }, new int[] { Math.round(MathHelper.wrapDegrees(RotationUtil.getRotations(crystal)[0])), Math.round(RotationUtil.getRotations(crystal)[1]) })) {
                    this.packets.add((Packet<?>)useEntity);
                    this.packets.add((Packet<?>)animation);
                }
            }
            else {
                this.rotations = RotationUtil.getRotations(crystal);
                this.packets.add((Packet<?>)useEntity);
                this.packets.add((Packet<?>)animation);
            }
            return true;
        }
        else {
            if ((boolean)((AutoCrystal)this.value).explode.getValue() && crystal != null && !((Rotate)((AutoCrystal)this.value).rotate.getValue()).noRotate(Rotate.Break) && (boolean)((AutoCrystal)this.value).useYawLimit.getValue()) {
                this.rotations = RotationUtil.getRotationsMaxYaw(crystal, (float)((int)((AutoCrystal)this.value).limit.getValue() + (this.random.nextBoolean() ? (-this.random.nextInt((int)((AutoCrystal)this.value).jitter.getValue())) : this.random.nextInt((int)((AutoCrystal)this.value).jitter.getValue()))), Managers.ROTATION.getServerYaw());
                return false;
            }
            return false;
        }
    }
    
    private void place(final PlaceData data) {
        if (InventoryUtil.isHolding(Items.END_CRYSTAL) && ((AutoCrystal)this.value).shouldPlace) {
            this.pos = data.getPos();
            RayTraceResult result;
            if (!((Rotate)((AutoCrystal)this.value).rotate.getValue()).noRotate(Rotate.Place)) {
                final float[] rotation = RotationUtil.getRotations(this.pos.up());
                if (((AutoCrystal)this.value).useForPlace.getValue()) {
                    this.rotations = RotationUtil.getRotationsMaxYaw(this.pos.up(), (float)(int)((AutoCrystal)this.value).limit.getValue(), Managers.ROTATION.getServerYaw());
                    result = new RayTraceResult(new Vec3d(0.5, 1.0, 0.5), EnumFacing.UP);
                }
                else {
                    this.rotations = RotationUtil.getRotations(this.pos.up());
                    result = RayTraceUtil.getRayTraceResult(this.rotations[0], this.rotations[1], (float)((AutoCrystal)this.value).placeRange.getValue());
                }
            }
            else {
                result = new RayTraceResult(new Vec3d(0.5, 1.0, 0.5), EnumFacing.UP);
            }
            if (data.getDamage() < (float)((AutoCrystal)this.value).minDamage.getValue() && ((AutoCrystal)this.value).shouldFacePlace()) {
                ((AutoCrystal)this.value).slow.add(this.pos.up());
            }
            final CPacketPlayerTryUseItemOnBlock place = new CPacketPlayerTryUseItemOnBlock(this.pos, result.sideHit, this.getHand(), (float)result.hitVec.x, (float)result.hitVec.y, (float)result.hitVec.z);
            final CPacketAnimation animation = new CPacketAnimation(this.getHand());
            ((AutoCrystal)this.value).getPlaceTimer().reset((int)((AutoCrystal)this.value).placeDelay.getValue());
            ((AutoCrystal)this.value).getPositions().add(this.pos.up());
            if ((((Rotate)((AutoCrystal)this.value).rotate.getValue()).noRotate(Rotate.Place) || RotationUtil.isLegit(this.pos)) && this.packets.isEmpty()) {
                InventoryUtil.syncItem();
                Calculation.mc.player.connection.sendPacket((Packet)place);
                Calculation.mc.player.connection.sendPacket((Packet)animation);
            }
            else if ((boolean)((AutoCrystal)this.value).useForPlace.getValue() && RotationUtil.isLegit(this.pos)) {
                this.packets.add((Packet<?>)place);
                this.packets.add((Packet<?>)animation);
            }
            else if (!(boolean)((AutoCrystal)this.value).useForPlace.getValue()) {
                this.packets.add((Packet<?>)place);
                this.packets.add((Packet<?>)animation);
            }
            this.setRenderPos(data);
        }
        else if (((AutoCrystal)this.value).isSwitching()) {
            int slot;
            final Runnable runnable = () -> {
                if (((AutoCrystal)this.value).mainHand.getValue()) {
                    slot = InventoryUtil.findHotbarItem(Items.END_CRYSTAL, new Item[0]);
                    InventoryUtil.switchTo(slot);
                }
                else {
                    Calculation.OFFHAND.computeIfPresent(o -> o.setMode(OffhandMode.CRYSTAL));
                }
                ((AutoCrystal)this.value).setSwitching(false);
                return;
            };
            final Object o2;
            Calculation.mc.addScheduledTask(() -> {
                ((AutoCrystal)this.value).postRunnable = runnable;
                return o2;
            });
        }
    }
    
    private void updatePlaceRotations(final PlaceData data) {
        if (InventoryUtil.isHolding(Items.END_CRYSTAL) && BlockUtil.canPlaceCrystal(data.getPos(), false, false)) {
            this.pos = data.getPos();
            if (!((Rotate)((AutoCrystal)this.value).rotate.getValue()).noRotate(Rotate.Place)) {
                final float[] rotation = RotationUtil.getRotations(this.pos.up());
                if (((AutoCrystal)this.value).useForPlace.getValue()) {
                    this.rotations = RotationUtil.getRotationsMaxYaw(this.pos.up(), (float)(int)((AutoCrystal)this.value).limit.getValue(), Managers.ROTATION.getServerYaw());
                    final RayTraceResult result = new RayTraceResult(new Vec3d(0.5, 1.0, 0.5), EnumFacing.UP);
                }
                else {
                    this.rotations = RotationUtil.getRotations(this.pos.up());
                    final RayTraceResult result = RayTraceUtil.getRayTraceResult(this.rotations[0], this.rotations[1], (float)((AutoCrystal)this.value).placeRange.getValue());
                }
            }
            else {
                final RayTraceResult result = new RayTraceResult(new Vec3d(0.5, 1.0, 0.5), EnumFacing.UP);
            }
            if (data.getDamage() < (float)((AutoCrystal)this.value).minDamage.getValue() && ((AutoCrystal)this.value).shouldFacePlace()) {
                ((AutoCrystal)this.value).slow.add(this.pos.up());
            }
        }
    }
    
    private boolean checkPos(final BlockPos pos, final int count) {
        boolean rotating = false;
        if (!this.attacking && (boolean)((AutoCrystal)this.value).fallBack.getValue()) {
            final Entity fallBack = this.breakData.getFallBack();
            if (((AutoCrystal)this.value).antiSurr.getValue()) {
                if (pos != null) {
                    for (final Entity entity : this.crystals) {
                        if (entity instanceof EntityEnderCrystal && !entity.isDead && (entity.getEntityBoundingBox().intersects(new AxisAlignedBB(pos.up())) || (!(boolean)((AutoCrystal)this.value).newerVer.getValue() && entity.getEntityBoundingBox().intersects(new AxisAlignedBB(pos.up(2)))))) {
                            final BlockPos entityPos = PositionUtil.getPosition(entity);
                            if (entityPos.equals((Object)pos.up())) {
                                continue;
                            }
                            if (fallBack != null) {
                                rotating = (this.attack(fallBack, this.breakData.getFallBackDamage()) && !((Rotate)((AutoCrystal)this.value).rotate.getValue()).noRotate(Rotate.Place) && !RotationUtil.isLegit(pos.getX(), pos.getY(), pos.getZ(), this.rotations[0], this.rotations[1]));
                            }
                            if (fallBack == null || !this.attacking) {
                                return false;
                            }
                            break;
                        }
                    }
                }
            }
            else if (pos == null && fallBack != null) {
                this.attack(fallBack, this.breakData.getFallBackDamage());
                return false;
            }
        }
        return pos != null && !rotating && (count < (int)((AutoCrystal)this.value).multiPlace.getValue() || ((boolean)((AutoCrystal)this.value).antiSurr.getValue() && !BlockUtil.canPlaceCrystal(pos, false, (boolean)((AutoCrystal)this.value).newerVer.getValue())));
    }
    
    private boolean shouldPlaceCalc() {
        return InventoryUtil.isHolding(Items.END_CRYSTAL) || ((AutoCrystal)this.value).attack.getValue() == Attack.Calc || ((AutoCrystal)this.value).autoSwitch.getValue() == AutoSwitch.Always || ((AutoCrystal)this.value).isSwitching();
    }
    
    private EnumHand getHand() {
        return (Calculation.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
    }
    
    private void setRenderPos(final PlaceData data) {
        if ((boolean)((AutoCrystal)this.value).multiThread.getValue() && ((AutoCrystal)this.value).rotate.getValue() == Rotate.None) {
            Calculation.mc.addScheduledTask(() -> {
                ((AutoCrystal)this.value).setRenderPos(data.getPos());
                ((AutoCrystal)this.value).setTarget(data.getTarget());
            });
        }
    }
    
    public BlockPos getPos() {
        return this.pos;
    }
    
    public boolean isAttacking() {
        return this.attacking;
    }
    
    public Entity getCrystal() {
        return this.crystal;
    }
    
    static {
        LEG_SWITCH = Caches.getModule((Class)LegSwitch.class);
        OFFHAND = Caches.getModule((Class)Offhand.class);
    }
}
