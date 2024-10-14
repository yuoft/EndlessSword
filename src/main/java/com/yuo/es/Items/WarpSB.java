package com.yuo.es.Items;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.yuo.endless.EndlessTab;
import com.yuo.endless.Items.Tool.ColorText;
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
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
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

import static com.yuo.es.Items.InfinitySB.hit;

/**
 * 异次元拔刀剑
 */
public class WarpSB extends ItemSlashBlade {
    public WarpSB() {
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
                s.setModel(new ResourceLocation(EndlessSword.MOD_ID, "model/warp_sb.obj"));
                s.setTexture(new ResourceLocation(EndlessSword.MOD_ID, "model/warp_sb.png"));
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
        components.add(new StringTextComponent(ColorText.makeFabulous(I18n.format("endless.text.itemInfo.infinity", new Object[0])) + I18n.format("attribute.name.generic.attack_damage", new Object[0])));
//
//        components.add(new StringTextComponent(ColorText.makeSANIC(I18n.format("tips.endless_sword.infinity_sb_info0"))));
//        components.add(new StringTextComponent(ColorText.makeSANIC(I18n.format("tips.endless_sword.infinity_sb_info1"))));
//        components.add(new StringTextComponent(ColorText.makeSANIC(I18n.format("tips.endless_sword.infinity_sb_info2"))));
//        components.add(new StringTextComponent(ColorText.makeSANIC(I18n.format("tips.endless_sword.infinity_sb_info3"))));
//        components.add(new StringTextComponent(ColorText.makeSANIC(I18n.format("tips.endless_sword.infinity_sb_info4"))));
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
                e.setModel(new ResourceLocation(EndlessSword.MOD_ID, "model/warp_sb.obj"));
                e.setTexture(new ResourceLocation(EndlessSword.MOD_ID, "model/warp_sb.png"));
            });
            items.add(stack);
        }
    }

    @Override
    public ItemStack getDefaultInstance() {
        ItemStack stack = super.getDefaultInstance();
        stack.getCapability(ItemSlashBlade.BLADESTATE).ifPresent(e ->{
            e.setModel(new ResourceLocation(EndlessSword.MOD_ID, "model/warp_sb.obj"));
            e.setTexture(new ResourceLocation(EndlessSword.MOD_ID, "model/warp_sb.png"));
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
            e.setModel(new ResourceLocation(EndlessSword.MOD_ID, "model/warp_sb.obj"));
            e.setTexture(new ResourceLocation(EndlessSword.MOD_ID, "model/warp_sb.png"));
        });
        return super.onDroppedByPlayer(item, player);
    }
}
