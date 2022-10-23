//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\hp\Desktop\coding\mappings"!

//Decompiled by Procyon!

package me.earth.earthhack.impl.modules.combat.autocrystal;

import me.earth.earthhack.impl.event.listeners.*;
import me.earth.earthhack.impl.event.events.misc.*;
import me.earth.earthhack.impl.modules.combat.autocrystal.modes.*;

final class ListenerTick extends ModuleListener<AutoCrystal, TickEvent>
{
    public ListenerTick(final AutoCrystal module) {
        super((Object)module, (Class)TickEvent.class);
    }
    
    public void invoke(final TickEvent event) {
        ((AutoCrystal)this.module).setTick(false);
        ((AutoCrystal)this.module).checkKilled();
        ((AutoCrystal)this.module).runNonRotateThread(ThreadMode.Tick);
    }
}
