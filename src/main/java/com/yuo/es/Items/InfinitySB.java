package com.yuo.es.Items;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.yuo.endless.config.ModConfig;
import com.yuo.endless.event.EventHandler;
import com.yuo.endless.items.tool.ColorText;
import com.yuo.endless.items.tool.EndlessTiers;
import com.yuo.endless.items.tool.InfinityDamageTypes;
import com.yuo.es.Entity.InfinityJC;
import com.yuo.es.RlUtils;
import mods.flammpfeil.slashblade.SlashBlade.RegistryEvents;
import mods.flammpfeil.slashblade.capability.slashblade.ISlashBladeState;
import mods.flammpfeil.slashblade.event.SlashBladeEvent;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.registry.SlashArtsRegistry;
import mods.flammpfeil.slashblade.util.RayTraceHelper;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * 无尽拔刀剑 宇宙最强之刃
 */
public class InfinitySB extends ItemSlashBlade {
    public InfinitySB() {
        super(EndlessTiers.INFINITY_SWORD, Integer.MAX_VALUE, -2.4f,
                new Properties().stacksTo(1).fireResistant());
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> def = super.getAttributeModifiers(slot, stack);
        Multimap<Attribute, AttributeModifier> result = ArrayListMultimap.create();
        result.putAll(Attributes.ATTACK_DAMAGE, def.get(Attributes.ATTACK_DAMAGE));
        result.putAll(Attributes.ATTACK_SPEED, def.get(Attributes.ATTACK_SPEED));
        if (slot == EquipmentSlot.MAINHAND) {
            LazyOptional<ISlashBladeState> state = stack.getCapability(BLADESTATE);
            state.ifPresent((s) -> {
                s.setBaseAttackModifier(Float.MAX_VALUE);
                s.setModel(RlUtils.fa("model/infinity_sb.obj"));
                s.setTexture(RlUtils.fa("model/infinity_sb.png"));

                float damage = s.getAttackAmplifier();
                SlashBladeEvent.UpdateAttackEvent event = new SlashBladeEvent.UpdateAttackEvent(stack, s, damage);
                MinecraftForge.EVENT_BUS.post(event);

                result.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(ATTACK_DAMAGE_AMPLIFIER, "Weapon amplifier", damage, Operation.ADDITION));
                result.put(ForgeMod.ENTITY_REACH.get(), new AttributeModifier(PLAYER_REACH_AMPLIFIER, "Reach amplifer", s.isBroken() ? 0.0 : 1.5, Operation.ADDITION));
            });
        }

        return result;
    }

    @Override
    public boolean isDamaged(ItemStack stack) {
        return false;
    }

    @Override
    public void setDamage(ItemStack stack, int damage) {
    }

    @Override
    public void appendHoverText(ItemStack stack, @org.jetbrains.annotations.Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(Component.literal(ColorText.makeFabulous(I18n.get("endless.text.itemInfo.infinity")) + I18n.get("attribute.name.generic.attack_damage")));

        tooltip.add(Component.literal(ColorText.makeSANIC(I18n.get("tips.endless_sword.infinity_sb_info0"))));
        tooltip.add(Component.literal(ColorText.makeSANIC(I18n.get("tips.endless_sword.infinity_sb_info1"))));
        tooltip.add(Component.literal(ColorText.makeSANIC(I18n.get("tips.endless_sword.infinity_sb_info2"))));
        tooltip.add(Component.literal(ColorText.makeSANIC(I18n.get("tips.endless_sword.infinity_sb_info3"))));
        tooltip.add(Component.literal(ColorText.makeSANIC(I18n.get("tips.endless_sword.infinity_sb_info4"))));
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public Component getName(ItemStack stack) {
        return Component.literal(ColorText.makeFabulous(I18n.get(this.getDescriptionId(stack))));
    }

    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
        return 0;
    }

    @Override
    public @NotNull ItemStack getDefaultInstance() {
        ItemStack stack = super.getDefaultInstance();
        stack.getCapability(ItemSlashBlade.BLADESTATE).ifPresent(e ->{
            e.setModel(RlUtils.fa( "model/infinity_sb.obj"));
            e.setTexture(RlUtils.fa("model/infinity_sb.png"));
            e.setSlashArtsKey(SlashArtsRegistry.CIRCLE_SLASH.getId());
        });
        return stack;
    }

    @Override
    public void onStopUsing(ItemStack stack, LivingEntity entity, int count) {
        super.onStopUsing(stack, entity, count);
    }

    public static InfinityJC spawnJudgementCutJust(LivingEntity user) {
        InfinityJC sa = doJudgementCut(user);
        sa.setDamage(Float.POSITIVE_INFINITY);
        sa.setIsCritical(true);
        return sa;
    }

    public static InfinityJC doJudgementCut(LivingEntity user) {
        Level worldIn = user.level();
        Vec3 eyePos = user.getEyePosition(1.0F);
        double airReach = 5.0;
        double entityReach = 7.0;
        ItemStack stack = user.getMainHandItem();
        Optional<Vec3> resultPos = stack.getCapability(ItemSlashBlade.BLADESTATE).filter((s) -> s.getTargetEntity(worldIn) != null).map(s
                -> s.getTargetEntity(worldIn).getEyePosition(1.0F));
        if (resultPos.isEmpty()) {
            Optional<HitResult> hitResult = RayTraceHelper.rayTrace(worldIn, user, eyePos, user.getLookAngle(), airReach, entityReach,
                    (entity) -> !entity.isSpectator() && entity.isAlive() && entity.canBeCollidedWith() && entity != user);
            resultPos = hitResult.map((rtr) -> {
                Vec3 pos = null;
                HitResult.Type type = rtr.getType();
                pos = switch (type) {
                    case ENTITY -> {
                        Entity target = ((EntityHitResult) rtr).getEntity();
                        yield target.getEyePosition().add(0.0, target.getEyeHeight() / 2.0F, 0.0);
                    }
                    case BLOCK -> rtr.getLocation();
                    default -> pos;
                };

                return pos;
            });
        }

        Vec3 pos = resultPos.orElseGet(() -> eyePos.add(user.getLookAngle().scale(airReach)));
        InfinityJC jc = new InfinityJC(RegistryEvents.JudgementCut, worldIn);
        jc.setPos(pos.x, pos.y, pos.z);
        jc.setShooter(user);
        stack.getCapability(ItemSlashBlade.BLADESTATE).ifPresent((state) -> {
            jc.setColor(state.getColorCode());
        });
        worldIn.addFreshEntity(jc);
        worldIn.playSound(null, jc.getX(), jc.getBlockY(), jc.getZ(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 0.5F, 0.8F / (user.getRandom().nextFloat() * 0.4F + 0.8F));
        return jc;
    }

    @Override
    public boolean onLeftClickEntity(ItemStack itemstack, Player playerIn, Entity entity) {
        if (entity instanceof LivingEntity livingEntity) {
            hit(livingEntity, playerIn);
            spawnJudgementCutJust(playerIn);
        }
        hitDisEntity(playerIn, itemstack);
        return super.onLeftClickEntity(itemstack, playerIn, entity);
    }

    /**
     * 模拟远距离攻击
     * @param playerIn 玩家
     */
    private static void hitDisEntity(Player playerIn, ItemStack stack) {
        Attribute attribute = ForgeMod.ENTITY_REACH.get();
        double dis = attribute.getDefaultValue() * 10;

        Optional<HitResult> hitResult = RayTraceHelper.rayTrace(playerIn.level(), playerIn, playerIn.getEyePosition(1.0f), playerIn.getLookAngle(),
                dis, dis, e -> e instanceof LivingEntity && e.isAlive() && e != playerIn);

        hitResult.map(hr -> {
            Type type = hr.getType();
            if (type == Type.ENTITY) {
                Entity target = ((EntityHitResult) hr).getEntity();
                if (target instanceof LivingEntity living) {
                    living.setGlowingTag(true);
                    living.kill();
                    stack.getCapability(ItemSlashBlade.BLADESTATE).ifPresent((state) -> {
                        SlashBladeEvent.AddKillCountEvent killCountEvent = new SlashBladeEvent.AddKillCountEvent(stack, state, 1);
                        MinecraftForge.EVENT_BUS.post(killCountEvent);
                        state.setKillCount(state.getKillCount() + 1);
                    });
                }
            }
            return 0;
        });

    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        hit(target, attacker);
        stack.getCapability(ItemSlashBlade.BLADESTATE).ifPresent((state) -> {
            state.setKillCount(state.getKillCount() + 1);
        });
        return super.hurtEnemy(stack, target, attacker);
    }

    //掉落地面
    @Override
    public boolean onDroppedByPlayer(ItemStack item, Player player) {
        item.getCapability(ItemSlashBlade.BLADESTATE).ifPresent(e ->{
            e.setModel(RlUtils.fa("model/infinity_sb.obj"));
            e.setTexture(RlUtils.fa("model/infinity_sb.png"));
        });
        return super.onDroppedByPlayer(item, player);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        if (!worldIn.isClientSide && worldIn instanceof ServerLevel) {
            if (!playerIn.isCrouching()){
                hitDisEntity(playerIn, playerIn.getUseItem());
            }else addBlot(worldIn, playerIn);
        }
        return super.use(worldIn, playerIn, handIn);
    }

    /**
     * 添加闪电实体
     */
    private static void addBlot(Level world, Player player){
        BlockPos pos = player.getOnPos();
        AABB alignedBB = new AABB(pos.offset(-16, -8, -16), pos.offset(16, 8, 16));
        for (LivingEntity living : world.getEntitiesOfClass(LivingEntity.class, alignedBB)) {
            if (living.isAlive() && !(living instanceof Player)) {
                BlockPos onPos = living.getOnPos();
                LightningBolt colorLightBolt = new LightningBolt(EntityType.LIGHTNING_BOLT, world);
                colorLightBolt.moveTo(Vec3.atBottomCenterOf(onPos));
                colorLightBolt.setCause(player instanceof ServerPlayer ? (ServerPlayer) player : null);
                colorLightBolt.setDamage(Float.POSITIVE_INFINITY);
                colorLightBolt.setCustomName(Component.literal("endless_swrod:color_light_bolt"));
                world.addFreshEntity(colorLightBolt);
            }
        }
    }


    @Nullable
    @Override
    public Entity createEntity(Level world, Entity location, ItemStack itemstack) {
        InfinitySbItemEntity e = new InfinitySbItemEntity(RegistryEvents.BladeItem, world);
        e.setItem(itemstack);
        e.copyPosition(location);
        e.setModel(RlUtils.fa("model/infinity_sb.obj"));
        e.setTexture(RlUtils.fa("model/infinity_sb.png"));
        e.init();
        return e;
    }

    @Override
    public boolean hasCustomEntity(ItemStack stack) {
        return true;
    }

    @Override
    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
//        if (!(entity instanceof BladeItemEntity)) {
//            Level world = entity.level();
//            BladeItemEntity e = new BladeItemEntity(RegistryEvents.BladeItem, world);
//            e.restoreFrom(entity);
//            e.init();
//            entity.discard();
//            world.addFreshEntity(e);
//        }

        return false;
    }

    /**
     * 攻击 无尽剑相同攻击方式
     * @param target 目标
     * @param attacker 攻击者
     */
    public static void hit(LivingEntity target, LivingEntity attacker){
        if (target instanceof EnderDragon dragon && attacker instanceof Player) {
            dragon.hurt(dragon.head, InfinityDamageTypes.infinity(attacker), Float.MAX_VALUE);
        } else if (target instanceof WitherBoss wither) {
            wither.setInvulnerableTicks(0);
            wither.hurt(InfinityDamageTypes.infinity(attacker), Float.MAX_VALUE);
        } else {
            if (target instanceof ArmorStand) {
                target.hurt(attacker.damageSources().generic(), 10.0F);
                return ;
            }

            if (target instanceof Player player) {
                if (EventHandler.isInfinite(player)) {
                    if (EventHandler.isInfinityItem(player)) {
                        target.hurt(InfinityDamageTypes.infinity(attacker), (float) ModConfig.SERVER.infinityBearDamage.get());
                    } else {
                        target.hurt(InfinityDamageTypes.infinity(attacker), (float) ModConfig.SERVER.infinityArmorBearDamage.get());
                    }
                } else {
                    target.hurt(InfinityDamageTypes.infinity(attacker), Float.MAX_VALUE);
                }
            } else {
                target.hurt(InfinityDamageTypes.infinity(attacker), Float.MAX_VALUE);
            }
        }

        if (target instanceof Player player) {
            if (EventHandler.isInfinite(player)) {
                return ;
            }
        }

        if (target.isAlive() || target.getHealth() > 0.0F) {
            target.setHealth(-1.0F);
            if (!target.level().isClientSide) {
                target.die(InfinityDamageTypes.infinity(attacker));
            }

            if (ModConfig.SERVER.swordKill.get()) {
                target.kill();
                target.deathTime = 20;
                target.remove(RemovalReason.KILLED);
            }
        }
    }
}
