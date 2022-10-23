//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\hp\Desktop\coding\mappings"!

//Decompiled by Procyon!

package me.earth.earthhack.impl.modules.combat.autocrystal;

import me.earth.earthhack.impl.event.listeners.*;
import me.earth.earthhack.impl.event.events.misc.*;

final class ListenerDeath extends ModuleListener<AutoCrystal, DeathEvent>
{
    public ListenerDeath(final AutoCrystal module) {
        super((Object)module, (Class)DeathEvent.class);
    }
    
    public void invoke(final DeathEvent event) {
        if (event.getEntity().equals((Object)ListenerDeath.mc.player)) {
            ((AutoCrystal)this.module).reset();
        }
    }
}
