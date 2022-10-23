//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\hp\Desktop\coding\mappings"!

//Decompiled by Procyon!

package me.earth.earthhack.impl.modules.combat.autocrystal;

import me.earth.earthhack.impl.event.listeners.*;
import me.earth.earthhack.impl.event.events.render.*;
import org.lwjgl.opengl.*;
import java.awt.*;
import me.earth.earthhack.impl.util.render.*;
import net.minecraft.util.math.*;

final class ListenerRender extends ModuleListener<AutoCrystal, Render3DEvent>
{
    public ListenerRender(final AutoCrystal module) {
        super((Object)module, (Class)Render3DEvent.class);
    }
    
    public void invoke(final Render3DEvent event) {
        if (((AutoCrystal)this.module).alphaAnimation != null) {
            ((AutoCrystal)this.module).alphaAnimation.add(event.getPartialTicks());
        }
        if (((AutoCrystal)this.module).renderPos != null && !((AutoCrystal)this.module).isPingBypass()) {
            this.doRender(((AutoCrystal)this.module).renderPos);
        }
    }
    
    public void doRender(final BlockPos pos) {
        GL11.glPushMatrix();
        GL11.glPushAttrib(1048575);
        final AxisAlignedBB bb = Interpolation.interpolatePos(pos, 1.0f);
        final Color boxColor = new Color(((AutoCrystal)this.module).fillColor.getRed(), ((AutoCrystal)this.module).fillColor.getGreen(), ((AutoCrystal)this.module).fillColor.getBlue(), ((AutoCrystal)this.module).fillColor.getAlpha());
        final Color outlineColor = new Color(((AutoCrystal)this.module).outlineColor.getRed(), ((AutoCrystal)this.module).outlineColor.getGreen(), ((AutoCrystal)this.module).outlineColor.getBlue(), ((AutoCrystal)this.module).outlineColor.getAlpha());
        RenderUtil.startRender();
        RenderUtil.drawOutline(bb, 1.5f, outlineColor);
        RenderUtil.endRender();
        RenderUtil.startRender();
        RenderUtil.drawBox(bb, boxColor);
        RenderUtil.endRender();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPopAttrib();
        GL11.glPopMatrix();
    }
}
