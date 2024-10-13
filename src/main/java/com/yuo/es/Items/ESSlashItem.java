package com.yuo.es.Items;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.yuo.endless.Config;
import com.yuo.endless.EndlessTab;
import com.yuo.endless.Event.EventHandler;
import com.yuo.endless.Items.Tool.ColorText;
import com.yuo.endless.Items.Tool.InfinityDamageSource;
import com.yuo.endless.Items.Tool.MyItemTier;
import com.yuo.es.EndlessSword;
import mods.flammpfeil.slashblade.capability.slashblade.ISlashBladeState;
import mods.flammpfeil.slashblade.client.renderer.SlashBladeTEISR;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

public class ESSlashItem extends ItemSlashBlade {
    public ESSlashItem() {
        super(MyItemTier.INFINITY_SWORD,Integer.MAX_VALUE, -2.4f,
                new Properties().group(EndlessTab.endless).maxStackSize(1).isImmuneToFire().setISTER(() -> SlashBladeTEISR::new));
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> def = super.getAttributeModifiers(slot, stack);
        Multimap<Attribute, AttributeModifier> result = ArrayListMultimap.create();
        result.putAll(Attributes.ATTACK_DAMAGE, def.get(Attributes.ATTACK_DAMAGE));
        result.putAll(Attributes.ATTACK_SPEED, def.get(Attributes.ATTACK_SPEED));
        if (slot == EquipmentSlotType.MAINHAND) {
            LazyOptional<ISlashBladeState> state = stack.getCapability(BLADESTATE);
            state.ifPresent((s) -> {
                s.setBaseAttackModifier(Float.POSITIVE_INFINITY);
                s.setModel(new ResourceLocation(EndlessSword.MOD_ID, "model/es.obj"));
                s.setTexture(new ResourceLocation(EndlessSword.MOD_ID, "model/es.png"));
                s.setKillCount(0);
                s.setRefine(0);
                s.setNoScabbard(false);
                float baseAttackModifier = s.getBaseAttackModifier();
                AttributeModifier base = new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", (double)baseAttackModifier, Operation.ADDITION);
                result.remove(Attributes.ATTACK_DAMAGE, base);
                result.put(Attributes.ATTACK_DAMAGE, base);
                float rankAttackAmplifier = s.getAttackAmplifier();
                result.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(ATTACK_DAMAGE_AMPLIFIER, "Weapon amplifier", (double)rankAttackAmplifier, Operation.ADDITION));
                result.put(ForgeMod.REACH_DISTANCE.get(), new AttributeModifier(PLAYER_REACH_AMPLIFIER, "Reach amplifer", s.isBroken() ? 0.0 : 1.5, Operation.ADDITION));
            });
        }

        return result;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> components, ITooltipFlag flag) {
        addInfo(components);
    }

    public static void addInfo(List<ITextComponent> components){
        components.add(new StringTextComponent(ColorText.makeFabulous(I18n.format("endless.text.itemInfo.infinity", new Object[0])) + I18n.format("attribute.name.generic.attack_damage", new Object[0])));

        components.add(new StringTextComponent(ColorText.makeSANIC(I18n.format("tips.endless_sword.es0"))));
        components.add(new StringTextComponent(ColorText.makeSANIC(I18n.format("tips.endless_sword.es1"))));
        components.add(new StringTextComponent(ColorText.makeSANIC(I18n.format("tips.endless_sword.es2"))));
        components.add(new StringTextComponent(ColorText.makeSANIC(I18n.format("tips.endless_sword.es3"))));
        components.add(new StringTextComponent(ColorText.makeSANIC(I18n.format("tips.endless_sword.es4"))));
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
                e.setModel(new ResourceLocation(EndlessSword.MOD_ID, "model/es.obj"));
                e.setTexture(new ResourceLocation(EndlessSword.MOD_ID, "model/es.png"));
            });
            items.add(stack);
        }
    }

    @Override
    public ItemStack getDefaultInstance() {
        ItemStack stack = super.getDefaultInstance();
        stack.getCapability(ItemSlashBlade.BLADESTATE).ifPresent(e ->{
            e.setModel(new ResourceLocation(EndlessSword.MOD_ID, "model/es.obj"));
            e.setTexture(new ResourceLocation(EndlessSword.MOD_ID, "model/es.png"));
        });
        return stack;
    }

    @Override
    public boolean hitEntity(ItemStack stackF, LivingEntity target, LivingEntity attacker) {
        super.hitEntity(stackF, target, attacker);
        return hit(target, attacker);
    }

    //掉落地面
    @Override
    public boolean onDroppedByPlayer(ItemStack item, PlayerEntity player) {
        item.getCapability(ItemSlashBlade.BLADESTATE).ifPresent(e ->{
            e.setModel(new ResourceLocation(EndlessSword.MOD_ID, "model/es.obj"));
            e.setTexture(new ResourceLocation(EndlessSword.MOD_ID, "model/es.png"));
        });
        return super.onDroppedByPlayer(item, player);
    }

    public static boolean hit(LivingEntity target, LivingEntity attacker){
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
                return true;
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
                return true;
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
        return true;
    }
}
