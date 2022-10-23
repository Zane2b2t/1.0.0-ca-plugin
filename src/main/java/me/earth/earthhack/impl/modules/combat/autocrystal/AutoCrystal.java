//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\hp\Desktop\coding\mappings"!

//Decompiled by Procyon!

package me.earth.earthhack.impl.modules.combat.autocrystal;

import me.earth.earthhack.api.module.*;
import me.earth.earthhack.api.cache.*;
import me.earth.earthhack.impl.modules.client.pingbypass.*;
import me.earth.earthhack.api.util.bind.*;
import me.earth.earthhack.impl.modules.combat.autocrystal.modes.*;
import net.minecraft.util.math.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.*;
import me.earth.earthhack.impl.util.thread.*;
import java.util.concurrent.atomic.*;
import me.earth.earthhack.api.module.util.*;
import me.earth.earthhack.api.setting.settings.*;
import java.awt.*;
import me.earth.earthhack.impl.util.math.*;
import java.util.concurrent.*;
import io.netty.util.internal.*;
import me.earth.earthhack.impl.modules.combat.autocrystal.util.*;
import me.earth.earthhack.api.module.data.*;
import me.earth.earthhack.api.setting.*;
import me.earth.earthhack.impl.gui.visibility.*;
import me.earth.earthhack.impl.util.minecraft.*;
import me.earth.earthhack.impl.managers.*;
import me.earth.earthhack.impl.util.animation.*;
import org.lwjgl.input.*;
import net.minecraft.item.*;
import net.minecraft.client.gui.inventory.*;
import java.util.*;
import me.earth.earthhack.impl.modules.*;

public class AutoCrystal extends Module
{
    private static final ModuleCache<PingBypass> PINGBYPASS;
    final Setting<ACPage> pages;
    final Setting<Boolean> place;
    final Setting<Target> target;
    final Setting<Float> placeRange;
    final Setting<Float> placeTrace;
    final Setting<Float> minDamage;
    final Setting<Integer> placeDelay;
    final Setting<Float> maxSelfP;
    final Setting<Float> facePlace;
    final Setting<Integer> multiPlace;
    final Setting<Boolean> countMin;
    final Setting<Boolean> antiSurr;
    final Setting<Boolean> newerVer;
    final Setting<Attack> attack;
    final Setting<Boolean> explode;
    final Setting<Float> breakRange;
    final Setting<Float> breakTrace;
    final Setting<Float> breakMinDmg;
    final Setting<Float> slowBreak;
    final Setting<Integer> slowDelay;
    final Setting<Integer> breakDelay;
    final Setting<Float> maxSelfB;
    final Setting<Boolean> instant;
    final Setting<Rotate> rotate;
    final Setting<Boolean> stay;
    final Setting<Boolean> multiThread;
    final Setting<Boolean> suicide;
    final Setting<Float> range;
    final Setting<Boolean> override;
    final Setting<Float> minFP;
    final Setting<Boolean> noFriendP;
    final Setting<AutoSwitch> autoSwitch;
    final Setting<Boolean> mainHand;
    final Setting<Bind> switchBind;
    final Setting<Boolean> switchBack;
    final Setting<SwingType> swing;
    final Setting<Float> pbTrace;
    final Setting<Boolean> setDead;
    final Setting<Boolean> dangerous;
    final Setting<Float> targetRange;
    final Setting<Boolean> useYawLimit;
    final Setting<Boolean> useForPlace;
    final Setting<Integer> limit;
    final Setting<Integer> jitter;
    final Setting<Boolean> antiFeetPlace;
    final Setting<Integer> footDelay;
    final Setting<Boolean> flooder;
    final Setting<Integer> floodDelay;
    final Setting<Integer> floodDelayNs;
    final Setting<Integer> cooldown;
    final Setting<Float> partialT;
    final Setting<Boolean> fallBack;
    final Setting<Float> fallbackDmg;
    final Setting<Boolean> soundR;
    final Setting<Boolean> holdFP;
    final Setting<Boolean> multiTask;
    final Setting<ThreadMode> threadMode;
    final Setting<Integer> threadDelay;
    final Setting<Boolean> predict;
    final Setting<Boolean> noParticles;
    final Setting<Boolean> pingBypass;
    final Setting<Integer> interpolate;
    final Setting<Boolean> slowLegBreak;
    final Setting<Integer> legDelay;
    final Setting<Integer> tickThreshold;
    final Setting<Integer> maxTick;
    final Setting<Integer> serverDelay;
    final Setting<Boolean> antiWeakness;
    final Setting<Boolean> noFaceSpam;
    final Setting<Boolean> breakSwitch;
    final Setting<Boolean> fade;
    final Setting<Boolean> legSwitch;
    final Setting<Integer> animation;
    final ColorSetting fillColor;
    final ColorSetting outlineColor;
    private final DiscreteTimer placeTimer;
    private final DiscreteTimer breakTimer;
    private final Set<BlockPos> positions;
    private Calculation currentCalc;
    private EntityPlayer currentTarget;
    private Entity currentCrystal;
    private boolean tick;
    private boolean switching;
    protected boolean shouldBreak;
    protected boolean hasBroken;
    protected boolean shouldPlace;
    final Map<Integer, EntityTime> killed;
    final Set<Integer> attacked;
    final Set<BlockPos> slow;
    final StopWatch renderTimer;
    final StopWatch animationTimer;
    final StopWatch threadTimer;
    final HelperBreak breakHelper;
    final HelperPlace placeHelper;
    final StopWatch targetTimer;
    final StopWatch serverTimer;
    float[] rotations;
    float[] targetRotations;
    BlockPos renderPos;
    BlockPos lastRenderPos;
    TimeAnimation alphaAnimation;
    Runnable postRunnable;
    boolean confirmed;
    boolean setSafe;
    ExecutorService floodService;
    FloodThread currentRunnable;
    boolean shouldStop;
    private final TaskThread thread;
    private final AtomicBoolean started;
    
    public AutoCrystal() {
        super("AutoCrystal", Category.Combat);
        this.pages = (Setting<ACPage>)this.register((Setting)new EnumSetting("Pages", (Enum)ACPage.Place));
        this.place = (Setting<Boolean>)this.register((Setting)new BooleanSetting("Place", Boolean.valueOf(true)));
        this.target = (Setting<Target>)this.register((Setting)new EnumSetting("Target", (Enum)Target.Closest));
        this.placeRange = (Setting<Float>)this.register((Setting)new NumberSetting("PlaceRange", (Number)6.0f, (Number)0.0f, (Number)6.0f));
        this.placeTrace = (Setting<Float>)this.register((Setting)new NumberSetting("PlaceTrace", (Number)6.0f, (Number)0.0f, (Number)6.0f));
        this.minDamage = (Setting<Float>)this.register((Setting)new NumberSetting("MinDamage", (Number)6.0f, (Number)0.0f, (Number)20.0f));
        this.placeDelay = (Setting<Integer>)this.register((Setting)new NumberSetting("PlaceDelay", (Number)0, (Number)0, (Number)500));
        this.maxSelfP = (Setting<Float>)this.register((Setting)new NumberSetting("MaxSelfPlace", (Number)9.0f, (Number)0.0f, (Number)20.0f));
        this.facePlace = (Setting<Float>)this.register((Setting)new NumberSetting("FacePlace", (Number)10.0f, (Number)0.0f, (Number)36.0f));
        this.multiPlace = (Setting<Integer>)this.register((Setting)new NumberSetting("MultiPlace", (Number)1, (Number)1, (Number)5));
        this.countMin = (Setting<Boolean>)this.register((Setting)new BooleanSetting("CountMin", Boolean.valueOf(false)));
        this.antiSurr = (Setting<Boolean>)this.register((Setting)new BooleanSetting("AntiSurround", Boolean.valueOf(true)));
        this.newerVer = (Setting<Boolean>)this.register((Setting)new BooleanSetting("1.13+", Boolean.valueOf(false)));
        this.attack = (Setting<Attack>)this.register((Setting)new EnumSetting("Attack", (Enum)Attack.BreakSlot));
        this.explode = (Setting<Boolean>)this.register((Setting)new BooleanSetting("Break", Boolean.valueOf(true)));
        this.breakRange = (Setting<Float>)this.register((Setting)new NumberSetting("BreakRange", (Number)6.0f, (Number)0.0f, (Number)6.0f));
        this.breakTrace = (Setting<Float>)this.register((Setting)new NumberSetting("BreakTrace", (Number)4.5f, (Number)0.0f, (Number)6.0f));
        this.breakMinDmg = (Setting<Float>)this.register((Setting)new NumberSetting("MinBreakDmg", (Number)2.0f, (Number)0.1f, (Number)20.0f));
        this.slowBreak = (Setting<Float>)this.register((Setting)new NumberSetting("SlowBreak", (Number)3.0f, (Number)0.1f, (Number)20.0f));
        this.slowDelay = (Setting<Integer>)this.register((Setting)new NumberSetting("SlowDelay", (Number)500, (Number)0, (Number)500));
        this.breakDelay = (Setting<Integer>)this.register((Setting)new NumberSetting("BreakDelay", (Number)0, (Number)0, (Number)500));
        this.maxSelfB = (Setting<Float>)this.register((Setting)new NumberSetting("MaxSelfBreak", (Number)10.0f, (Number)0.0f, (Number)20.0f));
        this.instant = (Setting<Boolean>)this.register((Setting)new BooleanSetting("Instant", Boolean.valueOf(false)));
        this.rotate = (Setting<Rotate>)this.register((Setting)new EnumSetting("Rotate", (Enum)Rotate.None));
        this.stay = (Setting<Boolean>)this.register((Setting)new BooleanSetting("Stay", Boolean.valueOf(false)));
        this.multiThread = (Setting<Boolean>)this.register((Setting)new BooleanSetting("MultiThread", Boolean.valueOf(false)));
        this.suicide = (Setting<Boolean>)this.register((Setting)new BooleanSetting("Suicide", Boolean.valueOf(false)));
        this.range = (Setting<Float>)this.register((Setting)new NumberSetting("Range", (Number)12.0f, (Number)6.0f, (Number)12.0f));
        this.override = (Setting<Boolean>)this.register((Setting)new BooleanSetting("Override", Boolean.valueOf(false)));
        this.minFP = (Setting<Float>)this.register((Setting)new NumberSetting("MinFace", (Number)2.0f, (Number)0.1f, (Number)4.0f));
        this.noFriendP = (Setting<Boolean>)this.register((Setting)new BooleanSetting("AntiFriendPop", Boolean.valueOf(true)));
        this.autoSwitch = (Setting<AutoSwitch>)this.register((Setting)new EnumSetting("AutoSwitch", (Enum)AutoSwitch.Bind));
        this.mainHand = (Setting<Boolean>)this.register((Setting)new BooleanSetting("MainHand", Boolean.valueOf(false)));
        this.switchBind = (Setting<Bind>)this.register((Setting)new BindSetting("SwitchBind", Bind.none()));
        this.switchBack = (Setting<Boolean>)this.register((Setting)new BooleanSetting("SwitchBack", Boolean.valueOf(true)));
        this.swing = (Setting<SwingType>)this.register((Setting)new EnumSetting("Swing", (Enum)SwingType.MainHand));
        this.pbTrace = (Setting<Float>)this.register((Setting)new NumberSetting("CombinedTrace", (Number)4.5f, (Number)0.0f, (Number)6.0f));
        this.setDead = (Setting<Boolean>)this.register((Setting)new BooleanSetting("SetDead", Boolean.valueOf(false)));
        this.dangerous = (Setting<Boolean>)this.register((Setting)new BooleanSetting("Dangerous", Boolean.valueOf(false)));
        this.targetRange = (Setting<Float>)this.register((Setting)new NumberSetting("T-Range", (Number)20.0f, (Number)0.0f, (Number)20.0f));
        this.useYawLimit = (Setting<Boolean>)this.register((Setting)new BooleanSetting("UseYawLimit", Boolean.valueOf(false)));
        this.useForPlace = (Setting<Boolean>)this.register((Setting)new BooleanSetting("PlaceYawLimit", Boolean.valueOf(false)));
        this.limit = (Setting<Integer>)this.register((Setting)new NumberSetting("YawLimit", (Number)20, (Number)1, (Number)180));
        this.jitter = (Setting<Integer>)this.register((Setting)new NumberSetting("RandomYaw", (Number)20, (Number)0, (Number)40));
        this.antiFeetPlace = (Setting<Boolean>)this.register((Setting)new BooleanSetting("AntiFeetPlace", Boolean.valueOf(true)));
        this.footDelay = (Setting<Integer>)this.register((Setting)new NumberSetting("FootDelay", (Number)12, (Number)0, (Number)100));
        this.flooder = (Setting<Boolean>)this.register((Setting)new BooleanSetting("Flooder", Boolean.valueOf(false)));
        this.floodDelay = (Setting<Integer>)this.register((Setting)new NumberSetting("FloodDelay", (Number)5, (Number)0, (Number)20));
        this.floodDelayNs = (Setting<Integer>)this.register((Setting)new NumberSetting("FloodDelayNs", (Number)500000, (Number)100000, (Number)1000000));
        this.cooldown = (Setting<Integer>)this.register((Setting)new NumberSetting("Cooldown", (Number)250, (Number)0, (Number)500));
        this.partialT = (Setting<Float>)this.register((Setting)new NumberSetting("PartialTicks", (Number)0.8f, (Number)0.0f, (Number)1.0f));
        this.fallBack = (Setting<Boolean>)this.register((Setting)new BooleanSetting("FallBack", Boolean.valueOf(true)));
        this.fallbackDmg = (Setting<Float>)this.register((Setting)new NumberSetting("FB-Dmg", (Number)2.0f, (Number)0.0f, (Number)6.0f));
        this.soundR = (Setting<Boolean>)this.register((Setting)new BooleanSetting("SoundRemove", Boolean.valueOf(false)));
        this.holdFP = (Setting<Boolean>)this.register((Setting)new BooleanSetting("HoldFP", Boolean.valueOf(true)));
        this.multiTask = (Setting<Boolean>)this.register((Setting)new BooleanSetting("MultiTask", Boolean.valueOf(true)));
        this.threadMode = (Setting<ThreadMode>)this.register((Setting)new EnumSetting("ThreadMode", (Enum)ThreadMode.Pre));
        this.threadDelay = (Setting<Integer>)this.register((Setting)new NumberSetting("ThreadDelay", (Number)50, (Number)5, (Number)500));
        this.predict = (Setting<Boolean>)this.register((Setting)new BooleanSetting("ID-Predict", Boolean.valueOf(false)));
        this.noParticles = (Setting<Boolean>)this.register((Setting)new BooleanSetting("NoOffhandParticles", Boolean.valueOf(false)));
        this.pingBypass = (Setting<Boolean>)this.register((Setting)new BooleanSetting("PingBypass", Boolean.valueOf(true)));
        this.interpolate = (Setting<Integer>)this.register((Setting)new NumberSetting("Interpolate", (Number)0, (Number)0, (Number)20));
        this.slowLegBreak = (Setting<Boolean>)this.register((Setting)new BooleanSetting("SlowLegBreak", Boolean.valueOf(false)));
        this.legDelay = (Setting<Integer>)this.register((Setting)new NumberSetting("LegDelay", (Number)100, (Number)1, (Number)500));
        this.tickThreshold = (Setting<Integer>)this.register((Setting)new NumberSetting("TickThreshold", (Number)40, (Number)1, (Number)50));
        this.maxTick = (Setting<Integer>)this.register((Setting)new NumberSetting("MaxTickTime", (Number)45, (Number)1, (Number)50));
        this.serverDelay = (Setting<Integer>)this.register((Setting)new NumberSetting("ServerDelay", (Number)3, (Number)0, (Number)20));
        this.antiWeakness = (Setting<Boolean>)this.register((Setting)new BooleanSetting("AntiWeakness", Boolean.valueOf(false)));
        this.noFaceSpam = (Setting<Boolean>)this.register((Setting)new BooleanSetting("No-Face-Spam", Boolean.valueOf(true)));
        this.breakSwitch = (Setting<Boolean>)this.register((Setting)new BooleanSetting("BreakSwitch", Boolean.valueOf(false)));
        this.fade = (Setting<Boolean>)this.register((Setting)new BooleanSetting("Fade", Boolean.valueOf(false)));
        this.legSwitch = (Setting<Boolean>)this.register((Setting)new BooleanSetting("LegSwitch", Boolean.valueOf(true)));
        this.animation = (Setting<Integer>)this.register((Setting)new NumberSetting("AnimationTime", (Number)250, (Number)0, (Number)500));
        this.fillColor = (ColorSetting)this.register((Setting)new ColorSetting("Fill", new Color(255, 255, 255, 128)));
        this.outlineColor = (ColorSetting)this.register((Setting)new ColorSetting("Outline", new Color(255, 255, 255, 255)));
        this.placeTimer = new GuardTimer(1000L, 5L).reset((int)this.placeDelay.getValue());
        this.breakTimer = new GuardTimer(1000L, 5L).reset((int)this.breakDelay.getValue());
        this.positions = Collections.newSetFromMap(new ConcurrentHashMap<BlockPos, Boolean>());
        this.hasBroken = false;
        this.shouldPlace = true;
        this.killed = new ConcurrentHashMap<Integer, EntityTime>();
        this.attacked = (Set<Integer>)new ConcurrentSet();
        this.slow = (Set<BlockPos>)new ConcurrentSet();
        this.renderTimer = new StopWatch();
        this.animationTimer = new StopWatch();
        this.threadTimer = new StopWatch();
        this.targetTimer = new StopWatch();
        this.serverTimer = new StopWatch();
        this.alphaAnimation = null;
        this.thread = new TaskThread("3arthh4ck-AutoCrystalThread");
        this.started = new AtomicBoolean();
        this.listeners.add(new ListenerTick(this));
        this.listeners.add(new ListenerMotion(this));
        this.listeners.add(new ListenerSpawnObject(this));
        this.listeners.add(new ListenerRender(this));
        this.listeners.add(new ListenerGameLoop(this));
        this.listeners.add(new ListenerPostKeys(this));
        this.listeners.add(new ListenerSound(this));
        this.listeners.add(new ListenerDestroyEntities(this));
        this.listeners.add(new ListenerKeys(this));
        this.listeners.add(new ListenerPlace(this));
        this.listeners.add(new ListenerAttack(this));
        this.listeners.add(new ListenerDeath(this));
        this.listeners.add(new ListenerLogout(this));
        this.listeners.add(new ListenerAnimation(this));
        this.breakHelper = new HelperBreak(this);
        this.placeHelper = new HelperPlace(this);
        this.setData((ModuleData)new AutoCrystalData(this));
        this.floodService = null;
        this.currentRunnable = null;
        this.shouldPlace = true;
        new PageBuilder((SettingContainer)this, (Setting)this.pages).addPage(v -> v == ACPage.Place, (Setting)this.place, (Setting)this.newerVer).addPage(v -> v == ACPage.Break, (Setting)this.attack, (Setting)this.instant).addPage(v -> v == ACPage.Misc, (Setting)this.rotate, (Setting)this.jitter).addPage(v -> v == ACPage.Dev, (Setting)this.antiFeetPlace, (Setting)this.legDelay).addPage(v -> v == ACPage.Render, (Setting)this.fade, (Setting)this.outlineColor).register(Visibilities.VISIBILITY_MANAGER);
    }
    
    protected void onLoad() {
        if (!this.started.getAndSet(true)) {
            this.thread.start();
        }
    }
    
    protected void onEnable() {
        this.shouldPlace = true;
        this.positions.clear();
        this.renderPos = null;
        this.alphaAnimation = null;
        this.animationTimer.reset();
        this.floodService = null;
        this.currentRunnable = null;
    }
    
    public String getDisplayInfo() {
        if (this.switching) {
            return "§aSwitching";
        }
        return (this.currentTarget == null) ? null : this.currentTarget.getName();
    }
    
    public DiscreteTimer getBreakTimer() {
        return this.breakTimer;
    }
    
    public DiscreteTimer getPlaceTimer() {
        return this.placeTimer;
    }
    
    public EntityPlayer getTarget() {
        return this.currentTarget;
    }
    
    public Set<BlockPos> getPositions() {
        return this.positions;
    }
    
    protected void setTick(final boolean tick) {
        this.tick = tick;
    }
    
    protected boolean canTick() {
        return !this.tick;
    }
    
    protected void runNonRotateThread(final ThreadMode mode) {
        if (this.threadMode.getValue() == mode && (boolean)this.multiThread.getValue() && this.rotate.getValue() == Rotate.None) {
            this.runThread();
        }
    }
    
    protected void runThread() {
        if (AutoCrystal.mc.world != null && AutoCrystal.mc.player != null && !this.isPingBypass()) {
            final List<EntityPlayer> players = new ArrayList<EntityPlayer>(AutoCrystal.mc.world.playerEntities);
            final List<Entity> crystals = new ArrayList<Entity>(AutoCrystal.mc.world.loadedEntityList);
            final Calculation calc = new Calculation(this, players, crystals);
            this.setCurrentCalc(calc);
            this.thread.submit(calc);
        }
    }
    
    protected void setCurrentCalc(final Calculation calc) {
        this.currentCalc = calc;
    }
    
    protected Calculation getCurrentCalc() {
        return this.currentCalc;
    }
    
    protected void setTarget(final EntityPlayer target) {
        this.currentTarget = target;
        this.targetTimer.reset();
    }
    
    protected void swing() {
        if (this.swing.getValue() != SwingType.None) {
            Swing.Client.swing(((SwingType)this.swing.getValue()).getHand());
        }
    }
    
    public boolean isSwitching() {
        return this.switching;
    }
    
    public void setSwitching(final boolean switching) {
        this.switching = switching;
    }
    
    protected void setUnsafe() {
        if (!this.setSafe) {
            Managers.SAFETY.setSafe(false);
            this.setSafe = true;
        }
    }
    
    public void setRenderPos(final BlockPos pos) {
        if (pos != null || this.renderTimer.passed(250L)) {
            this.lastRenderPos = this.renderPos;
            this.renderPos = pos;
            if (this.alphaAnimation == null) {
                this.alphaAnimation = new TimeAnimation((int)this.animation.getValue(), 0.0, this.fillColor.getAlpha(), false, true, AnimationMode.LINEAR);
            }
            else if (this.animationTimer.passed(250L)) {
                this.alphaAnimation.play();
                this.alphaAnimation.setCurrent(0.0);
                this.animationTimer.reset();
            }
            this.renderTimer.reset();
        }
    }
    
    public Entity getCurrentCrystal() {
        return this.currentCrystal;
    }
    
    protected void setCurrentCrystal(final Entity currentCrystal) {
        this.currentCrystal = currentCrystal;
    }
    
    protected boolean shouldFacePlace() {
        return (boolean)this.holdFP.getValue() && Mouse.isButtonDown(0) && !AutoCrystal.mc.playerController.getIsHittingBlock() && !(AutoCrystal.mc.player.getHeldItemMainhand().getItem() instanceof ItemPickaxe) && !(AutoCrystal.mc.currentScreen instanceof GuiContainer);
    }
    
    protected void checkKilled() {
        if (AutoCrystal.mc.world != null && !this.isPingBypass()) {
            for (final Map.Entry<Integer, EntityTime> entry : this.killed.entrySet()) {
                if (entry.getValue() == null) {
                    this.killed.remove(entry.getKey());
                }
                else {
                    if (System.nanoTime() - entry.getValue().getTime() <= 500000000L) {
                        continue;
                    }
                    final Entity entity = entry.getValue().getEntity();
                    entity.isDead = false;
                    if (AutoCrystal.mc.world.loadedEntityList.contains(entity)) {
                        continue;
                    }
                    AutoCrystal.mc.world.addEntityToWorld((int)entry.getKey(), entity);
                    entity.isDead = false;
                    this.killed.remove(entry.getKey());
                }
            }
        }
    }
    
    protected void reset() {
        this.killed.clear();
        this.attacked.clear();
        this.positions.clear();
        this.slow.clear();
    }
    
    public boolean isPingBypass() {
        return (boolean)this.pingBypass.getValue() && AutoCrystal.PINGBYPASS.isEnabled();
    }
    
    static {
        PINGBYPASS = Caches.getModule((Class)PingBypass.class);
    }
}
