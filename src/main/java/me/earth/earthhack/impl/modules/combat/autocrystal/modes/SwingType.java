//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\hp\Desktop\coding\mappings"!

//Decompiled by Procyon!

package me.earth.earthhack.impl.modules.combat.autocrystal.modes;

import net.minecraft.util.*;

public enum SwingType
{
    None {
        @Override
        public EnumHand getHand() {
            return null;
        }
    }, 
    MainHand {
        @Override
        public EnumHand getHand() {
            return EnumHand.MAIN_HAND;
        }
    }, 
    OffHand {
        @Override
        public EnumHand getHand() {
            return EnumHand.OFF_HAND;
        }
    };
    
    public abstract EnumHand getHand();
}
