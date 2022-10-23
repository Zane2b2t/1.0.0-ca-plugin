//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\hp\Desktop\coding\mappings"!

//Decompiled by Procyon!

package me.earth.earthhack.impl.modules.combat.autocrystal;

import me.earth.earthhack.impl.event.listeners.*;
import me.earth.earthhack.impl.event.events.network.*;
import net.minecraft.network.play.server.*;
import net.minecraft.item.*;

final class ListenerAnimation extends ModuleListener<AutoCrystal, PacketEvent.Receive<SPacketAnimation>>
{
    public ListenerAnimation(final AutoCrystal module) {
        super((Object)module, (Class)PacketEvent.Receive.class, (Class)SPacketAnimation.class);
    }
    
    public void invoke(final PacketEvent.Receive<SPacketAnimation> event) {
        if (((AutoCrystal)this.module).noParticles.getValue()) {
            final SPacketAnimation packet = (SPacketAnimation)event.getPacket();
            if (packet.getAnimationType() == 5 && (ListenerAnimation.mc.player.getHeldItemMainhand().getItem() instanceof ItemAxe || ListenerAnimation.mc.player.getHeldItemMainhand().getItem() instanceof ItemSword) && ListenerAnimation.mc.player.getHeldItemOffhand().getItem() instanceof ItemEndCrystal && !((AutoCrystal)this.module).getBreakTimer().passed(500L)) {
                event.setCancelled(true);
            }
        }
    }
}
