//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\hp\Desktop\coding\mappings"!

//Decompiled by Procyon!

package me.earth.earthhack.impl.modules.combat.autocrystal;

import me.earth.earthhack.api.util.interfaces.*;
import net.minecraft.util.math.*;
import net.minecraft.entity.player.*;
import me.earth.earthhack.impl.util.minecraft.entity.*;
import me.earth.earthhack.impl.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import net.minecraft.util.*;
import net.minecraft.init.*;

public class FloodThread implements Runnable, Globals
{
    private BlockPos pos;
    private final AutoCrystal module;
    private final EntityPlayer target;
    private long last;
    
    public FloodThread(final BlockPos pos, final AutoCrystal module) {
        this.pos = pos;
        this.module = module;
        this.target = EntityUtil.getClosestEnemy();
        this.last = System.currentTimeMillis();
        Earthhack.getLogger().info("wowza");
        module.shouldPlace = false;
    }
    
    @Override
    public void run() {
        while (true) {
            final CPacketPlayerTryUseItemOnBlock place = new CPacketPlayerTryUseItemOnBlock(this.pos, EnumFacing.UP, this.getHand(), 0.5f, 1.0f, 0.5f);
            final CPacketAnimation animation = new CPacketAnimation(this.getHand());
            FloodThread.mc.player.connection.sendPacket((Packet)place);
            FloodThread.mc.player.connection.sendPacket((Packet)animation);
            this.last = System.currentTimeMillis();
            if (!this.module.isEnabled() || this.target == null || this.module.shouldStop) {
                break;
            }
            try {
                Thread.sleep((int)this.module.floodDelay.getValue(), (int)this.module.floodDelayNs.getValue());
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.module.shouldPlace = true;
        this.module.floodService.shutdownNow();
        Earthhack.getLogger().info("unfortunate");
        this.module.floodService = null;
        this.module.currentRunnable = null;
        this.module.shouldStop = false;
    }
    
    private EnumHand getHand() {
        return (FloodThread.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
    }
    
    public BlockPos getPos() {
        return this.pos;
    }
    
    public void setPos(final BlockPos pos) {
        this.pos = pos;
    }
    
    public EntityPlayer getTarget() {
        return this.target;
    }
}
