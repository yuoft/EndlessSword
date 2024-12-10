package com.yuo.es.Items;

import com.yuo.endless.Config;
import com.yuo.endless.EndlessTab;
import com.yuo.endless.Event.EventHandler;
import com.yuo.endless.Items.Tool.ColorText;
import com.yuo.endless.Items.Tool.InfinityDamageSource;
import com.yuo.endless.Items.Tool.MyItemTier;
import com.yuo.es.EndlessSword;
import mods.flammpfeil.slashblade.SlashBlade.RegistryEvents;
import mods.flammpfeil.slashblade.client.renderer.SlashBladeTEISR;
import mods.flammpfeil.slashblade.entity.EntityJudgementCut;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.util.RayTraceHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ForgeMod;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

import static mods.flammpfeil.slashblade.specialattack.JudgementCut.doJudgementCut;

/**
 * 无尽拔刀剑 宇宙最强之刃
 */
public class InfinitySB extends ItemSlashBlade {
    public InfinitySB() {
        super(MyItemTier.INFINITY_SWORD,Integer.MAX_VALUE, -2.4f,
                new Properties().group(EndlessTab.endless).maxStackSize(1).isImmuneToFire().setISTER(() -> SlashBladeTEISR::new));
    }

    @Override
    public boolean isDamaged(ItemStack stack) {
        return false;
    }

    @Override
    public void setDamage(ItemStack stack, int damage) {
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> components, ITooltipFlag flag) {
        components.add(new StringTextComponent(ColorText.makeFabulous(I18n.format("endless.text.itemInfo.infinity", new Object[0])) + I18n.format("attribute.name.generic.attack_damage", new Object[0])));

        components.add(new StringTextComponent(ColorText.makeSANIC(I18n.format("tips.endless_sword.infinity_sb_info0"))));
        components.add(new StringTextComponent(ColorText.makeSANIC(I18n.format("tips.endless_sword.infinity_sb_info1"))));
        components.add(new StringTextComponent(ColorText.makeSANIC(I18n.format("tips.endless_sword.infinity_sb_info2"))));
        components.add(new StringTextComponent(ColorText.makeSANIC(I18n.format("tips.endless_sword.infinity_sb_info3"))));
        components.add(new StringTextComponent(ColorText.makeSANIC(I18n.format("tips.endless_sword.infinity_sb_info4"))));
    }

    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
        return new StringTextComponent(ColorText.makeFabulous(I18n.format(this.getTranslationKey(stack))));
    }

    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
        return 0;
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if (group == EndlessTab.endless) {
            ItemStack stack = new ItemStack(this);
            stack.getCapability(ItemSlashBlade.BLADESTATE).ifPresent(e ->{
                e.setModel(new ResourceLocation(EndlessSword.MOD_ID, "model/infinity_sb.obj"));
                e.setTexture(new ResourceLocation(EndlessSword.MOD_ID, "model/infinity_sb.png"));
            });
            items.add(stack);
        }
    }

    @Override
    public ItemStack getDefaultInstance() {
        ItemStack stack = super.getDefaultInstance();
        stack.getCapability(ItemSlashBlade.BLADESTATE).ifPresent(e ->{
            e.setModel(new ResourceLocation(EndlessSword.MOD_ID, "model/infinity_sb.obj"));
            e.setTexture(new ResourceLocation(EndlessSword.MOD_ID, "model/infinity_sb.png"));
        });
        return stack;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {
        super.onPlayerStoppedUsing(stack, worldIn, entityLiving, timeLeft);
    }

    public static EntityJudgementCut spawnJudgementCutJust(LivingEntity user) {
        EntityJudgementCut sa = doJudgementCut(user);
        sa.setDamage(Float.POSITIVE_INFINITY);
        sa.setIsCritical(true);
        return sa;
    }

    @Override
    public boolean onLeftClickEntity(ItemStack itemstack, PlayerEntity playerIn, Entity entity) {
        if (entity instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity) entity;
            hit(livingEntity, playerIn);
            spawnJudgementCutJust(playerIn);
        }
        hitDisEntity(playerIn);
        return super.onLeftClickEntity(itemstack, playerIn, entity);
    }

    /**
     * 模拟远距离攻击
     * @param playerIn 玩家
     */
    private static void hitDisEntity(PlayerEntity playerIn) {
        Attribute attribute = ForgeMod.REACH_DISTANCE.get();
        double dis = attribute.getDefaultValue() * 10;
        RayTraceHelper.rayTrace(playerIn.world, playerIn, playerIn.getEyePosition(1.0f), playerIn.getLookVec(),
                dis, dis, entity -> {
            if (entity instanceof LivingEntity){
                LivingEntity living = (LivingEntity) entity;
                if (living.isAlive() && !(living instanceof PlayerEntity)){
                    living.setGlowing(true);
                    living.onKillCommand();
                }
            }
            return true;
        });
    }

    @Override
    public boolean hitEntity(ItemStack stackF, LivingEntity target, LivingEntity attacker) {
        hit(target, attacker);
        return super.hitEntity(stackF, target, attacker);
    }

    //掉落地面
    @Override
    public boolean onDroppedByPlayer(ItemStack item, PlayerEntity player) {
        item.getCapability(ItemSlashBlade.BLADESTATE).ifPresent(e ->{
            e.setModel(new ResourceLocation(EndlessSword.MOD_ID, "model/infinity_sb.obj"));
            e.setTexture(new ResourceLocation(EndlessSword.MOD_ID, "model/infinity_sb.png"));
        });
        return super.onDroppedByPlayer(item, player);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        if (!worldIn.isRemote && worldIn instanceof ServerWorld) {
            if (!playerIn.isSneaking()){
                hitDisEntity(playerIn);
            }else addBlot(worldIn, playerIn);
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    /**
     * 添加闪电实体
     */
    private static void addBlot(World world, PlayerEntity player){
        BlockPos pos = player.getPosition();
        AxisAlignedBB alignedBB = new AxisAlignedBB(pos.add(-16, -8, -16), pos.add(16, 8, 16));
        for (LivingEntity living : world.getEntitiesWithinAABB(LivingEntity.class, alignedBB)) {
            if (living.isAlive() && !(living instanceof PlayerEntity)) {
                LightningBoltEntity colorLightBolt = new LightningBoltEntity(EntityType.LIGHTNING_BOLT, world);
                colorLightBolt.moveForced(Vector3d.copyCenteredHorizontally(living.getPosition()));
                colorLightBolt.setCaster(player instanceof ServerPlayerEntity ? (ServerPlayerEntity) player : null);
                colorLightBolt.setEffectOnly(false);
                colorLightBolt.setDamage(Float.POSITIVE_INFINITY);
                colorLightBolt.setCustomName(new StringTextComponent("endless_swrod:color_light_bolt"));
                world.addEntity(colorLightBolt);
            }
        }
    }

    @Nullable
    @Override
    public Entity createEntity(World world, Entity location, ItemStack itemstack) {
        InfinitySbItemEntity e = new InfinitySbItemEntity(RegistryEvents.BladeItem, world, location);
        e.copyDataFromOld(location);
        e.init();
        return e;
    }

    /**
     * 攻击 无尽剑相同攻击方式
     * @param target 目标
     * @param attacker 攻击者
     */
    public static void hit(LivingEntity target, LivingEntity attacker){
        PlayerEntity player;
        if (target instanceof EnderDragonEntity && attacker instanceof PlayerEntity) {
            EnderDragonEntity dragon = (EnderDragonEntity)target;
            dragon.attackEntityPartFrom(dragon.dragonPartHead, new InfinityDamageSource(attacker), Float.POSITIVE_INFINITY);
        } else if (target instanceof WitherEntity) {
            WitherEntity wither = (WitherEntity)target;
            wither.setInvulTime(0);
            wither.attackEntityFrom(new InfinityDamageSource(attacker), Float.POSITIVE_INFINITY);
        } else {
            if (target instanceof ArmorStandEntity) {
                target.attackEntityFrom(DamageSource.GENERIC, 10.0F);
                return ;
            }

            if (target instanceof PlayerEntity) {
                player = (PlayerEntity)target;
                if (EventHandler.isInfinite(player)) {
                    if (EventHandler.isInfinityItem(player)) {
                        target.attackEntityFrom(new InfinityDamageSource(attacker), (float)(Integer) Config.SERVER.infinityBearDamage.get());
                    } else {
                        target.attackEntityFrom(new InfinityDamageSource(attacker), (float)(Integer)Config.SERVER.infinityArmorBearDamage.get());
                    }
                } else {
                    target.attackEntityFrom(new InfinityDamageSource(attacker), Float.POSITIVE_INFINITY);
                }
            } else {
                target.attackEntityFrom(new InfinityDamageSource(attacker), Float.POSITIVE_INFINITY);
            }
        }

        if (target instanceof PlayerEntity) {
            player = (PlayerEntity)target;
            if (EventHandler.isInfinite(player)) {
                return ;
            }
        }

        if (target.isAlive() || target.getHealth() > 0.0F) {
            target.setHealth(-1.0F);
            if (!target.world.isRemote) {
                target.onDeath(new InfinityDamageSource(attacker));
            }

            if (Config.SERVER.swordKill.get()) {
                target.onKillCommand();
                target.deathTime = 20;
                target.remove(true);
            }
        }
    }
}
