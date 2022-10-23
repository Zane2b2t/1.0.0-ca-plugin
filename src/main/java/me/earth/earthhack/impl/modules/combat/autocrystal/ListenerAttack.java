//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\hp\Desktop\coding\mappings"!

//Decompiled by Procyon!

package me.earth.earthhack.impl.modules.combat.autocrystal;

import me.earth.earthhack.impl.event.listeners.*;
import me.earth.earthhack.impl.event.events.network.*;
import me.earth.earthhack.impl.core.ducks.network.*;
import net.minecraft.entity.item.*;
import net.minecraft.world.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import me.earth.earthhack.impl.managers.*;
import me.earth.earthhack.impl.util.thread.*;
import net.minecraft.util.math.*;
import net.minecraft.util.*;
import net.minecraft.init.*;

final class ListenerAttack extends ModuleListener<AutoCrystal, PacketEvent.Post<CPacketUseEntity>>
{
    public ListenerAttack(final AutoCrystal module) {
        super((Object)module, (Class)PacketEvent.Post.class, (Class)CPacketUseEntity.class);
    }
    
    public void invoke(final PacketEvent.Post<CPacketUseEntity> event) {
        if (((CPacketUseEntity)event.getPacket()).getAction() == CPacketUseEntity.Action.ATTACK && !((AutoCrystal)this.module).isPingBypass()) {
            final ICPacketUseEntity packet = (ICPacketUseEntity)event.getPacket();
            ((AutoCrystal)this.module).attacked.add(packet.getEntityID());
            if (((CPacketUseEntity)event.getPacket()).getEntityFromWorld((World)ListenerAttack.mc.world) instanceof EntityEnderCrystal && (boolean)((AutoCrystal)this.module).antiFeetPlace.getValue()) {
                final BlockPos antiPos = ((CPacketUseEntity)event.getPacket()).getEntityFromWorld((World)ListenerAttack.mc.world).getPosition().down();
                final CPacketPlayerTryUseItemOnBlock place;
                final CPacketAnimation animation1;
                ThreadUtil.run(() -> {
                    place = new CPacketPlayerTryUseItemOnBlock(antiPos, EnumFacing.UP, this.getHand(), 0.5f, 1.0f, 0.5f);
                    animation1 = new CPacketAnimation(this.getHand());
                    ListenerAttack.mc.player.connection.sendPacket((Packet)place);
                    ListenerAttack.mc.player.connection.sendPacket((Packet)animation1);
                }, Managers.TICK.getServerTickLengthMS() + 11 - Managers.TICK.getTimeIntoCurrentTickAdjusted());
            }
        }
    }
    
    private EnumHand getHand() {
        return (ListenerAttack.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
    }
}
