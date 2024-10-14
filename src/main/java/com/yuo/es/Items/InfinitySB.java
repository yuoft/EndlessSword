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
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

/**
 * 无尽拔刀剑 宇宙最强之刃
 */
public class InfinitySB extends ItemSlashBlade {
    public InfinitySB() {
        super(MyItemTier.INFINITY_SWORD,Integer.MAX_VALUE, -2.4f,
                new Properties().group(EndlessTab.endless).maxStackSize(1).isImmuneToFire().setISTER(() -> SlashBladeTEISR::new));
    }

//    @Override
//    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack) {
//        Multimap<Attribute, AttributeModifier> def = super.getAttributeModifiers(slot, stack);
//        Multimap<Attribute, AttributeModifier> result = ArrayListMultimap.create();
//        result.putAll(Attributes.ATTACK_DAMAGE, def.get(Attributes.ATTACK_DAMAGE));
//        result.putAll(Attributes.ATTACK_SPEED, def.get(Attributes.ATTACK_SPEED));
//        if (slot == EquipmentSlotType.MAINHAND) {
//            LazyOptional<ISlashBladeState> state = stack.getCapability(BLADESTATE);
//            state.ifPresent((s) -> {
//                s.setBaseAttackModifier(Float.POSITIVE_INFINITY);
//                s.setModel(new ResourceLocation(EndlessSword.MOD_ID, "model/infinity_sb.obj"));
//                s.setTexture(new ResourceLocation(EndlessSword.MOD_ID, "model/infinity_sb.png"));
//                float baseAttackModifier = s.getBaseAttackModifier();
//                AttributeModifier base = new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", (double)baseAttackModifier, Operation.ADDITION);
//                result.remove(Attributes.ATTACK_DAMAGE, base);
//                result.put(Attributes.ATTACK_DAMAGE, base);
//                float rankAttackAmplifier = s.getAttackAmplifier();
//                result.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(ATTACK_DAMAGE_AMPLIFIER, "Weapon amplifier", (double)rankAttackAmplifier, Operation.ADDITION));
//                result.put(ForgeMod.REACH_DISTANCE.get(), new AttributeModifier(PLAYER_REACH_AMPLIFIER, "Reach amplifer", s.isBroken() ? 0.0 : 1.5, Operation.ADDITION));
//            });
//        }
//
//        return result;
//    }

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
        BlockPos pos = playerIn.getPosition();
        for (int i = 2; i < 20; i+=4){
            for (int j = 2; j < 20; j+=4){
                BlockPos blockPos = pos.add(i,-1, j);
                BlockPos blockPos0 = pos.add(-i,-1, -j);
                BlockPos blockPos1 = pos.add(i,-1, -j);
                BlockPos blockPos2 = pos.add(-i,-1, j);
                addBlot(worldIn, blockPos, playerIn);
                addBlot(worldIn, blockPos0, playerIn);
                addBlot(worldIn, blockPos1, playerIn);
                addBlot(worldIn, blockPos2, playerIn);
            }
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    /**
     * 添加闪电实体
     */
    private static void addBlot(World world, BlockPos pos, PlayerEntity player){
        LightningBoltEntity lightningBolt = EntityType.LIGHTNING_BOLT.create(world);
        if (lightningBolt != null) {
            lightningBolt.moveForced(Vector3d.copyCenteredHorizontally(pos));
            lightningBolt.setCaster(player instanceof ServerPlayerEntity ? (ServerPlayerEntity)player : null);
            world.addEntity(lightningBolt);
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
