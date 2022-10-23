//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\hp\Desktop\coding\mappings"!

//Decompiled by Procyon!

package me.earth.earthhack.impl.modules.combat.autocrystal;

import me.earth.earthhack.impl.event.listeners.*;
import me.earth.earthhack.impl.event.events.network.*;
import net.minecraft.init.*;
import me.earth.earthhack.impl.core.ducks.network.*;
import net.minecraft.network.*;
import net.minecraft.util.*;
import net.minecraft.network.play.client.*;
import net.minecraft.entity.player.*;
import me.earth.earthhack.impl.util.minecraft.*;
import net.minecraft.item.*;
import net.minecraft.entity.*;
import java.util.*;

final class ListenerPlace extends ModuleListener<AutoCrystal, PacketEvent.Post<CPacketPlayerTryUseItemOnBlock>>
{
    public ListenerPlace(final AutoCrystal module) {
        super((Object)module, (Class)PacketEvent.Post.class, (Class)CPacketPlayerTryUseItemOnBlock.class);
    }
    
    public void invoke(final PacketEvent.Post<CPacketPlayerTryUseItemOnBlock> event) {
        if ((boolean)((AutoCrystal)this.module).predict.getValue() && !((AutoCrystal)this.module).isPingBypass()) {
            final CPacketPlayerTryUseItemOnBlock packet = (CPacketPlayerTryUseItemOnBlock)event.getPacket();
            if (ListenerPlace.mc.player.getHeldItem(packet.getHand()).getItem() == Items.END_CRYSTAL) {
                final int id;
                ICPacketUseEntity useEntity;
                ListenerPlace.mc.addScheduledTask(() -> {
                    id = this.getID();
                    if (id != -1 && ((AutoCrystal)this.module).getBreakTimer().passed((int)((AutoCrystal)this.module).breakDelay.getValue())) {
                        useEntity = (ICPacketUseEntity)new CPacketUseEntity();
                        useEntity.setAction(CPacketUseEntity.Action.ATTACK);
                        useEntity.setEntityId(id);
                        ListenerPlace.mc.player.connection.sendPacket((Packet)useEntity);
                        ListenerPlace.mc.player.connection.sendPacket((Packet)new CPacketAnimation(EnumHand.MAIN_HAND));
                        ((AutoCrystal)this.module).getBreakTimer().reset((int)((AutoCrystal)this.module).breakDelay.getValue());
                    }
                });
            }
        }
    }
    
    private int getID() {
        if (ListenerPlace.mc.world == null || ListenerPlace.mc.player == null) {
            return -1;
        }
        for (final EntityPlayer player : ListenerPlace.mc.world.playerEntities) {
            if (player != null && (player.isDead || InventoryUtil.isHolding((EntityLivingBase)player, (Item)Items.BOW) || InventoryUtil.isHolding((EntityLivingBase)player, Items.EXPERIENCE_BOTTLE))) {
                return -1;
            }
        }
        int highest = -1;
        for (final Entity entity : ListenerPlace.mc.world.loadedEntityList) {
            if (entity != null && entity.getEntityId() > highest) {
                highest = entity.getEntityId();
            }
        }
        return highest + 1;
    }
}
