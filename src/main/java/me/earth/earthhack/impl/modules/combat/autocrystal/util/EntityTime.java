//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\hp\Desktop\coding\mappings"!

//Decompiled by Procyon!

package me.earth.earthhack.impl.modules.combat.autocrystal.util;

import net.minecraft.entity.*;

public class EntityTime
{
    private final long time;
    private final Entity entity;
    
    public EntityTime(final Entity entity, final long time) {
        this.entity = entity;
        this.time = time;
    }
    
    public long getTime() {
        return this.time;
    }
    
    public Entity getEntity() {
        return this.entity;
    }
}
