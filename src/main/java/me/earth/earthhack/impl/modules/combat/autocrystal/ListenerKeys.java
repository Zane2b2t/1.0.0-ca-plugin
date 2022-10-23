//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\hp\Desktop\coding\mappings"!

//Decompiled by Procyon!

package me.earth.earthhack.impl.modules.combat.autocrystal;

import me.earth.earthhack.impl.event.listeners.*;
import me.earth.earthhack.impl.event.events.keyboard.*;
import me.earth.earthhack.api.cache.*;
import me.earth.earthhack.impl.modules.combat.offhand.*;
import me.earth.earthhack.impl.modules.combat.autocrystal.modes.*;
import me.earth.earthhack.api.util.bind.*;
import me.earth.earthhack.impl.modules.combat.offhand.modes.*;
import net.minecraft.init.*;
import me.earth.earthhack.impl.modules.*;

final class ListenerKeys extends ModuleListener<AutoCrystal, KeyboardEvent>
{
    private static final ModuleCache<Offhand> OFFHAND;
    
    public ListenerKeys(final AutoCrystal module) {
        super((Object)module, (Class)KeyboardEvent.class);
    }
    
    public void invoke(final KeyboardEvent event) {
        if (event.getEventState() && ((AutoCrystal)this.module).autoSwitch.getValue() == AutoSwitch.Bind && event.getKey() == ((Bind)((AutoCrystal)this.module).switchBind.getValue()).getKey()) {
            if (((AutoCrystal)this.module).isPingBypass()) {
                ListenerKeys.OFFHAND.computeIfPresent(o -> {
                    if (OffhandMode.CRYSTAL.equals(o.getMode())) {
                        o.setMode(OffhandMode.TOTEM);
                    }
                    else {
                        o.setMode(OffhandMode.CRYSTAL);
                    }
                });
                return;
            }
            ((AutoCrystal)this.module).setSwitching(!((AutoCrystal)this.module).isSwitching());
            if (!((AutoCrystal)this.module).isSwitching() && (boolean)((AutoCrystal)this.module).switchBack.getValue() && ListenerKeys.mc.player != null && ListenerKeys.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) {
                ListenerKeys.OFFHAND.computeIfPresent(o -> o.setMode(OffhandMode.TOTEM));
            }
        }
    }
    
    static {
        OFFHAND = Caches.getModule((Class)Offhand.class);
    }
}
