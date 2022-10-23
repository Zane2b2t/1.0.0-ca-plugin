//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\hp\Desktop\coding\mappings"!

//Decompiled by Procyon!

package me.earth.earthhack.impl.modules.combat.autocrystal;

import me.earth.earthhack.impl.event.listeners.*;
import me.earth.earthhack.impl.event.events.network.*;
import net.minecraft.network.play.server.*;
import net.minecraft.entity.item.*;
import me.earth.earthhack.impl.util.math.*;
import net.minecraft.entity.*;

final class ListenerDestroyEntities extends ModuleListener<AutoCrystal, PacketEvent.Receive<SPacketDestroyEntities>>
{
    public ListenerDestroyEntities(final AutoCrystal module) {
        super((Object)module, (Class)PacketEvent.Receive.class, (Class)SPacketDestroyEntities.class);
    }
    
    public void invoke(final PacketEvent.Receive<SPacketDestroyEntities> event) {
        SPacketDestroyEntities packet;
        final int[] array;
        int length;
        int i = 0;
        int id;
        Entity entity;
        ListenerDestroyEntities.mc.addScheduledTask(() -> {
            if (ListenerDestroyEntities.mc.world != null && !((AutoCrystal)this.module).isPingBypass()) {
                packet = (SPacketDestroyEntities)event.getPacket();
                packet.getEntityIDs();
                for (length = array.length; i < length; ++i) {
                    id = array[i];
                    ((AutoCrystal)this.module).killed.remove(id);
                    ((AutoCrystal)this.module).attacked.remove(id);
                    entity = ListenerDestroyEntities.mc.world.getEntityByID(id);
                    if (entity instanceof EntityEnderCrystal) {
                        ((AutoCrystal)this.module).getPositions().remove(PositionUtil.getPosition(entity));
                    }
                }
            }
        });
    }
}
