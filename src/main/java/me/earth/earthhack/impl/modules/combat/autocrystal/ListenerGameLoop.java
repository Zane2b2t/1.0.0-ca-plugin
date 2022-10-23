//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\hp\Desktop\coding\mappings"!

//Decompiled by Procyon!

package me.earth.earthhack.impl.modules.combat.autocrystal;

import me.earth.earthhack.impl.event.listeners.*;
import me.earth.earthhack.impl.event.events.misc.*;
import me.earth.earthhack.impl.modules.combat.autocrystal.modes.*;
import me.earth.earthhack.impl.managers.*;

final class ListenerGameLoop extends ModuleListener<AutoCrystal, GameLoopEvent>
{
    public ListenerGameLoop(final AutoCrystal module) {
        super((Object)module, (Class)GameLoopEvent.class);
    }
    
    public void invoke(final GameLoopEvent event) {
        if ((boolean)((AutoCrystal)this.module).multiThread.getValue() && !((AutoCrystal)this.module).isPingBypass()) {
            if (((AutoCrystal)this.module).rotate.getValue() != Rotate.None) {
                if (ListenerGameLoop.mc.getRenderPartialTicks() >= (float)((AutoCrystal)this.module).partialT.getValue() && ((AutoCrystal)this.module).canTick()) {
                    ((AutoCrystal)this.module).setTick(true);
                    ((AutoCrystal)this.module).runThread();
                }
            }
            else if (((AutoCrystal)this.module).threadMode.getValue() == ThreadMode.Delay && ((AutoCrystal)this.module).threadTimer.passed((int)((AutoCrystal)this.module).threadDelay.getValue())) {
                ((AutoCrystal)this.module).runNonRotateThread(ThreadMode.Delay);
                ((AutoCrystal)this.module).threadTimer.reset();
            }
            else if (((AutoCrystal)this.module).threadMode.getValue() == ThreadMode.Server && Managers.TICK.getTimeIntoCurrentTickAdjustedForAutoCrystal() >= (int)((AutoCrystal)this.module).tickThreshold.getValue() && Managers.TICK.getTimeIntoCurrentTickAdjustedForAutoCrystal() <= (int)((AutoCrystal)this.module).maxTick.getValue() && ((AutoCrystal)this.module).serverTimer.passed((int)((AutoCrystal)this.module).serverDelay.getValue())) {
                ((AutoCrystal)this.module).runNonRotateThread(ThreadMode.Server);
                ((AutoCrystal)this.module).serverTimer.reset();
            }
        }
    }
}
