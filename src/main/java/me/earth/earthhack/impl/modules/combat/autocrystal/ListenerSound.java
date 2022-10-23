//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\hp\Desktop\coding\mappings"!

//Decompiled by Procyon!

package me.earth.earthhack.impl.modules.combat.autocrystal;

import me.earth.earthhack.impl.event.listeners.*;
import me.earth.earthhack.impl.event.events.network.*;
import net.minecraft.network.play.server.*;
import net.minecraft.util.*;
import net.minecraft.init.*;
import net.minecraft.util.math.*;
import net.minecraft.entity.*;
import net.minecraft.entity.item.*;
import me.earth.earthhack.impl.util.math.*;
import java.util.*;

final class ListenerSound extends ModuleListener<AutoCrystal, PacketEvent.Receive<SPacketSoundEffect>>
{
    public ListenerSound(final AutoCrystal module) {
        super((Object)module, (Class)PacketEvent.Receive.class, (Class)SPacketSoundEffect.class);
    }
    
    public void invoke(final PacketEvent.Receive<SPacketSoundEffect> event) {
        final SPacketSoundEffect packet = (SPacketSoundEffect)event.getPacket();
        if (packet.getCategory() == SoundCategory.BLOCKS && packet.getSound() == SoundEvents.ENTITY_GENERIC_EXPLODE) {
            final BlockPos pos = new BlockPos(packet.getX(), packet.getY(), packet.getZ());
            if (((AutoCrystal)this.module).getPositions().remove(pos) && !((AutoCrystal)this.module).isPingBypass()) {
                ((AutoCrystal)this.module).confirmed = true;
            }
            if (((AutoCrystal)this.module).soundR.getValue()) {
                this.killEntities(pos);
            }
        }
    }
    
    private void killEntities(final BlockPos pos) {
        final Iterator<Entity> iterator;
        Entity entity;
        ListenerSound.mc.addScheduledTask(() -> {
            ListenerSound.mc.world.loadedEntityList.iterator();
            while (iterator.hasNext()) {
                entity = iterator.next();
                if (entity instanceof EntityEnderCrystal && entity.getDistanceSq(pos) <= 36.0) {
                    ((AutoCrystal)this.module).getPositions().remove(PositionUtil.getPosition(entity));
                    entity.setDead();
                }
            }
        });
    }
}
