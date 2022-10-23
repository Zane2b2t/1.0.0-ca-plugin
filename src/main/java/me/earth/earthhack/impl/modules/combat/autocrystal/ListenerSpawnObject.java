//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\hp\Desktop\coding\mappings"!

//Decompiled by Procyon!

package me.earth.earthhack.impl.modules.combat.autocrystal;

import me.earth.earthhack.impl.event.listeners.*;
import me.earth.earthhack.impl.event.events.network.*;
import net.minecraft.network.play.server.*;
import me.earth.earthhack.impl.util.minecraft.entity.*;
import me.earth.earthhack.impl.util.minecraft.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import me.earth.earthhack.impl.core.ducks.network.*;
import net.minecraft.network.*;
import net.minecraft.util.*;
import net.minecraft.network.play.client.*;
import net.minecraft.util.math.*;
import me.earth.earthhack.impl.modules.combat.autocrystal.modes.*;
import me.earth.earthhack.impl.util.math.*;

final class ListenerSpawnObject extends ModuleListener<AutoCrystal, PacketEvent.Receive<SPacketSpawnObject>>
{
    public ListenerSpawnObject(final AutoCrystal module) {
        super((Object)module, (Class)PacketEvent.Receive.class, (Class)SPacketSpawnObject.class);
    }
    
    public void invoke(final PacketEvent.Receive<SPacketSpawnObject> event) {
        if ((boolean)((AutoCrystal)this.module).instant.getValue() && !((AutoCrystal)this.module).isPingBypass()) {
            final SPacketSpawnObject packet = (SPacketSpawnObject)event.getPacket();
            final EntityPlayer target = ((AutoCrystal)this.module).getTarget();
            if (packet.getType() == 51 && target != null && !EntityUtil.isDead((Entity)target) && ListenerSpawnObject.mc.player != null) {
                final BlockPos pos = new BlockPos(packet.getX(), packet.getY(), packet.getZ());
                if (((AutoCrystal)this.module).getPositions().contains(pos) && this.isValid(pos) && this.rotationCheck(pos)) {
                    final float damage = DamageUtil.calculate(pos.down());
                    if (damage <= (float)((AutoCrystal)this.module).maxSelfB.getValue() && damage < EntityUtil.getHealth((EntityLivingBase)ListenerSpawnObject.mc.player) + 1.0) {
                        this.attack(packet, ((AutoCrystal)this.module).slow.remove(pos) ? ((int)((AutoCrystal)this.module).slowDelay.getValue()) : ((int)((AutoCrystal)this.module).breakDelay.getValue()));
                    }
                }
            }
        }
    }
    
    private void attack(final SPacketSpawnObject packetIn, final int delay) {
        if (((AutoCrystal)this.module).getBreakTimer().passed(delay)) {
            final ICPacketUseEntity useEntity = (ICPacketUseEntity)new CPacketUseEntity();
            useEntity.setAction(CPacketUseEntity.Action.ATTACK);
            useEntity.setEntityId(packetIn.getEntityID());
            ListenerSpawnObject.mc.player.connection.sendPacket((Packet)useEntity);
            ListenerSpawnObject.mc.player.connection.sendPacket((Packet)new CPacketAnimation(EnumHand.MAIN_HAND));
            ListenerSpawnObject.mc.addScheduledTask((AutoCrystal)this.module::swing);
            ((AutoCrystal)this.module).getBreakTimer().reset(delay);
        }
    }
    
    private boolean isValid(final BlockPos pos) {
        return ListenerSpawnObject.mc.player.getDistanceSq(pos.getX() + 0.5, (double)pos.getY(), pos.getZ() + 0.5) <= MathUtil.square((float)((AutoCrystal)this.module).breakRange.getValue()) && (ListenerSpawnObject.mc.player.getDistanceSq(pos.getX() + 0.5, (double)pos.getY(), pos.getZ() + 0.5) <= MathUtil.square((float)((AutoCrystal)this.module).breakTrace.getValue()) || RayTraceUtil.canBeSeen(new Vec3d(pos.getX() + 0.5, pos.getY() + 1.700000047683716, pos.getZ() + 0.5), (Entity)ListenerSpawnObject.mc.player));
    }
    
    private boolean rotationCheck(final BlockPos pos) {
        return ((Rotate)((AutoCrystal)this.module).rotate.getValue()).noRotate(Rotate.Break) || RotationUtil.isLegit(pos) || RotationUtil.isLegit(pos.up());
    }
}
