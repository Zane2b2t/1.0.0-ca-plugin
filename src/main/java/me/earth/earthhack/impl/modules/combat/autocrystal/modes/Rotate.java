//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\hp\Desktop\coding\mappings"!

//Decompiled by Procyon!

package me.earth.earthhack.impl.modules.combat.autocrystal.modes;

public enum Rotate
{
    None {
        @Override
        public boolean noRotate(final Rotate rotate) {
            return true;
        }
    }, 
    Break {
        @Override
        public boolean noRotate(final Rotate rotate) {
            return rotate == Rotate$2.Place;
        }
    }, 
    Place {
        @Override
        public boolean noRotate(final Rotate rotate) {
            return rotate == Rotate$3.Break;
        }
    }, 
    All {
        @Override
        public boolean noRotate(final Rotate rotate) {
            return false;
        }
    };
    
    public abstract boolean noRotate(final Rotate p0);
}
