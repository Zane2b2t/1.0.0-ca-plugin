//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\hp\Desktop\coding\mappings"!

//Decompiled by Procyon!

package me.earth.earthhack.impl.modules.combat.autocrystal;

import me.earth.earthhack.impl.event.listeners.*;
import me.earth.earthhack.impl.event.events.network.*;
import me.earth.earthhack.api.cache.*;
import me.earth.earthhack.impl.modules.combat.bomber.*;
import me.earth.earthhack.api.event.events.*;
import net.minecraft.init.*;
import me.earth.earthhack.impl.util.minecraft.entity.*;
import me.earth.earthhack.impl.*;
import me.earth.earthhack.impl.util.thread.*;
import net.minecraft.entity.*;
import me.earth.earthhack.impl.util.math.*;
import me.earth.earthhack.impl.util.minecraft.blocks.*;
import me.earth.earthhack.impl.util.minecraft.*;
import me.earth.earthhack.impl.managers.*;
import me.earth.earthhack.impl.modules.combat.autocrystal.modes.*;
import net.minecraft.network.*;
import net.minecraft.network.play.client.*;
import net.minecraft.util.math.*;
import java.util.*;
import me.earth.earthhack.impl.modules.*;

final class ListenerMotion extends ModuleListener<AutoCrystal, MotionUpdateEvent>
{
    private static final ModuleCache<CrystalBomber> BOMBER;
    
    public ListenerMotion(final AutoCrystal module) {
        super((Object)module, (Class)MotionUpdateEvent.class);
    }
    
    public void invoke(final MotionUpdateEvent event) {
        if (((AutoCrystal)this.module).isPingBypass()) {
            return;
        }
        Calculation calc = ((AutoCrystal)this.module).getCurrentCalc();
        if (event.getStage() == Stage.PRE) {
            if (((AutoCrystal)this.module).flooder.getValue()) {
                if (((AutoCrystal)this.module).floodService == null && InventoryUtil.isHolding(Items.END_CRYSTAL) && EntityUtil.getClosestEnemy() != null && PlayerUtil.isInHole(EntityUtil.getClosestEnemy()) && PlayerUtil.isFeetPlaceable(EntityUtil.getClosestEnemy(), true, false, (float)((AutoCrystal)this.module).placeRange.getValue()) && !ListenerMotion.BOMBER.isEnabled()) {
                    Earthhack.getLogger().info("help me");
                    ((AutoCrystal)this.module).currentRunnable = new FloodThread(PlayerUtil.getFeetPos(EntityUtil.getClosestEnemy(), true, false, (float)((AutoCrystal)this.module).placeRange.getValue()), (AutoCrystal)this.module);
                    ((AutoCrystal)this.module).floodService = ThreadUtil.keepRunning((Runnable)((AutoCrystal)this.module).currentRunnable);
                    ((AutoCrystal)this.module).shouldPlace = false;
                }
                else if (((AutoCrystal)this.module).floodService != null && (((AutoCrystal)this.module).floodService.isTerminated() || ((AutoCrystal)this.module).floodService.isShutdown())) {
                    ((AutoCrystal)this.module).floodService = null;
                    ((AutoCrystal)this.module).shouldPlace = true;
                }
                else if (((AutoCrystal)this.module).floodService != null && ((AutoCrystal)this.module).currentRunnable != null && (ListenerMotion.mc.player.getDistanceSq((Entity)((AutoCrystal)this.module).currentRunnable.getTarget()) >= MathUtil.square((float)((AutoCrystal)this.module).targetRange.getValue()) || ListenerMotion.mc.player.getDistanceSq(((AutoCrystal)this.module).currentRunnable.getPos()) >= MathUtil.square((float)((AutoCrystal)this.module).placeRange.getValue()) || !BlockUtil.canPlaceCrystalFuture(((AutoCrystal)this.module).currentRunnable.getPos(), true, false) || !PlayerUtil.isFootPlace(((AutoCrystal)this.module).currentRunnable.getPos(), ((AutoCrystal)this.module).currentRunnable.getTarget(), true, false, (float)((AutoCrystal)this.module).placeRange.getValue()) || DamageUtil.calculate(((AutoCrystal)this.module).currentRunnable.getPos()) >= (float)((AutoCrystal)this.module).maxSelfP.getValue() || !InventoryUtil.isHolding(Items.END_CRYSTAL))) {
                    ((AutoCrystal)this.module).floodService.shutdownNow();
                    ((AutoCrystal)this.module).floodService = null;
                    ((AutoCrystal)this.module).shouldPlace = true;
                    ((AutoCrystal)this.module).currentRunnable = null;
                    ((AutoCrystal)this.module).shouldStop = true;
                }
            }
            else {
                if (((AutoCrystal)this.module).floodService != null && (((AutoCrystal)this.module).floodService.isTerminated() || ((AutoCrystal)this.module).floodService.isShutdown())) {
                    ((AutoCrystal)this.module).floodService = null;
                    ((AutoCrystal)this.module).shouldPlace = true;
                }
                if (((AutoCrystal)this.module).floodService != null) {
                    ((AutoCrystal)this.module).floodService.shutdown();
                    ((AutoCrystal)this.module).floodService = null;
                    ((AutoCrystal)this.module).shouldPlace = true;
                }
            }
            if (InventoryUtil.isHolding(Items.END_CRYSTAL)) {
                ((AutoCrystal)this.module).setSwitching(false);
            }
            if (((AutoCrystal)this.module).rotate.getValue() != Rotate.None && (boolean)((AutoCrystal)this.module).stay.getValue() && ((AutoCrystal)this.module).rotations != null && !Managers.ROTATION.isBlocking() && !((AutoCrystal)this.module).getBreakTimer().passed(600L) && Managers.SWITCH.getLastSwitch() > (int)((AutoCrystal)this.module).cooldown.getValue()) {
                event.setYaw(((AutoCrystal)this.module).rotations[0]);
                event.setPitch(((AutoCrystal)this.module).rotations[1]);
            }
            if (!(boolean)((AutoCrystal)this.module).multiThread.getValue()) {
                calc = new Calculation((AutoCrystal)this.module, ListenerMotion.mc.world.playerEntities, ListenerMotion.mc.world.loadedEntityList);
                ((AutoCrystal)this.module).setCurrentCalc(calc);
                calc.run();
            }
            if (calc != null && calc.isRotating()) {
                ((AutoCrystal)this.module).rotations = calc.getRotations();
                event.setYaw(((AutoCrystal)this.module).rotations[0]);
                event.setPitch(((AutoCrystal)this.module).rotations[1]);
            }
            ((AutoCrystal)this.module).runNonRotateThread(ThreadMode.Pre);
        }
        else {
            if (calc != null) {
                ((AutoCrystal)this.module).setRenderPos(calc.getPos());
                if (calc.getTarget() == null && ((AutoCrystal)this.module).targetTimer.passed(250L)) {
                    ((AutoCrystal)this.module).setTarget(calc.getTarget());
                }
                for (final Packet<?> packet : calc.getPackets()) {
                    if (packet instanceof CPacketPlayerTryUseItemOnBlock) {
                        InventoryUtil.syncItem();
                    }
                    ListenerMotion.mc.player.connection.sendPacket((Packet)packet);
                }
                if (calc.isAttacking()) {
                    ((AutoCrystal)this.module).swing();
                }
            }
            ((AutoCrystal)this.module).runNonRotateThread(ThreadMode.Post);
            ((AutoCrystal)this.module).setCurrentCalc((Calculation)null);
            if (!InventoryUtil.isHolding(Items.END_CRYSTAL) || ((AutoCrystal)this.module).renderTimer.passed(300L)) {
                ((AutoCrystal)this.module).setRenderPos((BlockPos)null);
            }
            if (((AutoCrystal)this.module).postRunnable != null) {
                ((AutoCrystal)this.module).postRunnable.run();
                ((AutoCrystal)this.module).postRunnable = null;
            }
        }
    }
    
    static {
        BOMBER = Caches.getModule((Class)CrystalBomber.class);
    }
}
