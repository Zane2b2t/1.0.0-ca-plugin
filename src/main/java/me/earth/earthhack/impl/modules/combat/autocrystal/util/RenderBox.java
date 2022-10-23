//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\hp\Desktop\coding\mappings"!

//Decompiled by Procyon!

package me.earth.earthhack.impl.modules.combat.autocrystal.util;

import org.lwjgl.opengl.*;
import java.awt.*;
import me.earth.earthhack.impl.util.render.*;
import net.minecraft.util.math.*;

public class RenderBox
{
    public static void renderBox(final BlockPos pos) {
        GL11.glPushMatrix();
        GL11.glPushAttrib(1048575);
        final AxisAlignedBB bb = Interpolation.interpolatePos(pos, 1.0f);
        Color boxColor = new Color(1.0f, 1.0f, 1.0f, 0.9f);
        RenderUtil.startRender();
        RenderUtil.drawOutline(bb, 1.5f, boxColor);
        RenderUtil.endRender();
        boxColor = new Color(1.0f, 1.0f, 1.0f, 0.3f);
        RenderUtil.startRender();
        RenderUtil.drawBox(bb, boxColor);
        RenderUtil.endRender();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPopAttrib();
        GL11.glPopMatrix();
    }
}
