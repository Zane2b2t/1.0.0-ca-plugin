//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\hp\Desktop\coding\mappings"!

//Decompiled by Procyon!

package me.earth.earthhack.impl.modules.combat.autocrystal;

import me.earth.earthhack.impl.event.listeners.*;
import me.earth.earthhack.impl.event.events.keyboard.*;
import me.earth.earthhack.impl.modules.combat.autocrystal.modes.*;

final class ListenerPostKeys extends ModuleListener<AutoCrystal, KeyboardEvent.Post>
{
    public ListenerPostKeys(final AutoCrystal module) {
        super((Object)module, (Class)KeyboardEvent.Post.class);
    }
    
    public void invoke(final KeyboardEvent.Post event) {
        ((AutoCrystal)this.module).runNonRotateThread(ThreadMode.Keys);
    }
}
