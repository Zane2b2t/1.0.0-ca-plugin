//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\hp\Desktop\coding\mappings"!

//Decompiled by Procyon!

package me.earth.earthhack.impl.modules.combat.autocrystal;

import me.earth.earthhack.impl.event.listeners.*;
import me.earth.earthhack.impl.event.events.network.*;

final class ListenerLogout extends ModuleListener<AutoCrystal, DisconnectEvent>
{
    public ListenerLogout(final AutoCrystal module) {
        super((Object)module, (Class)DisconnectEvent.class);
    }
    
    public void invoke(final DisconnectEvent event) {
        ((AutoCrystal)this.module).reset();
    }
}
