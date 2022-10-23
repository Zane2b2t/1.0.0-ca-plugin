//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\hp\Desktop\coding\mappings"!

//Decompiled by Procyon!

package me.earth.earthhack.impl.modules.combat.autocrystal.modes;

import net.minecraft.init.*;
import me.earth.earthhack.impl.util.minecraft.*;

public enum Attack
{
    Always {
        @Override
        public boolean shouldCalc() {
            return true;
        }
        
        @Override
        public boolean shouldAttack() {
            return true;
        }
    }, 
    BreakSlot {
        @Override
        public boolean shouldCalc() {
            return InventoryUtil.isHolding(Items.END_CRYSTAL);
        }
        
        @Override
        public boolean shouldAttack() {
            return InventoryUtil.isHolding(Items.END_CRYSTAL);
        }
    }, 
    Calc {
        @Override
        public boolean shouldCalc() {
            return true;
        }
        
        @Override
        public boolean shouldAttack() {
            return InventoryUtil.isHolding(Items.END_CRYSTAL);
        }
    };
    
    public abstract boolean shouldCalc();
    
    public abstract boolean shouldAttack();
}
