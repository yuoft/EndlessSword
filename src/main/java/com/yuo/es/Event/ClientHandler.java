package com.yuo.es.Event;

import com.yuo.endless.Client.Lib.ColorUtils;
import com.yuo.endless.Client.Lib.ColourRGBA;
import com.yuo.es.EndlessSword;
import com.yuo.es.Entity.EsEntityTypes;
import com.yuo.es.Entity.InfinitySA;
import com.yuo.es.Items.ESItems;
import mods.flammpfeil.slashblade.SlashBlade.RegistryEvents;
import mods.flammpfeil.slashblade.capability.slashblade.ISlashBladeState;
import mods.flammpfeil.slashblade.client.renderer.entity.BladeItemEntityRenderer;
import mods.flammpfeil.slashblade.event.handler.InputCommandEvent;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.util.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.awt.*;
import java.util.EnumSet;
import java.util.Optional;
import java.util.stream.Stream;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = EndlessSword.MOD_ID)
public class ClientHandler {

    //实体渲染注册
    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EsEntityTypes.INFINITY_SB.get(), BladeItemEntityRenderer::new); //投掷物渲染
    }

    @SubscribeEvent
    public static void registerModels(InputCommandEvent event) {
        EnumSet<InputCommand> old = event.getOld();
        EnumSet<InputCommand> current = event.getCurrent();
        ServerPlayer sender = event.getEntity();
        boolean onDown = !old.contains(InputCommand.M_DOWN) && current.contains(InputCommand.M_DOWN);
        ItemStack handItem = sender.getMainHandItem();
        if (onDown && handItem.getItem() == ESItems.infinitySb.get()) {
            Level worldIn = sender.level();
            handItem.getCapability(ItemSlashBlade.BLADESTATE).ifPresent((state) -> {

                Optional<HitResult> optional = RayTraceHelper.rayTrace(sender.level(), sender, sender.getEyePosition(1.0F), sender.getLookAngle(), 12.0, 12.0,
                        e -> e instanceof LivingEntity);
                Optional<Entity> entity = optional.filter((r) -> r.getType() == Type.ENTITY).filter((r) -> {
                    EntityHitResult er = (EntityHitResult) r;
                    Entity target = er.getEntity();
                    boolean isMatch = true;
                    if (target instanceof LivingEntity living) {
                        isMatch = TargetSelector.lockon.test(sender, living);
                    }

                    return isMatch;
                }).map((r) -> ((EntityHitResult) r).getEntity());

                Entity entity1 = null;
                if (entity.isPresent()) {
                    entity1 = entity.stream().filter(Entity::isAlive).findFirst().get();
                }

                Vec3 targetPos;
                Entity targetEntity = state.getTargetEntity(sender.level());
                if (targetEntity != null) {
                    targetPos = new Vec3(targetEntity.getX(), targetEntity.getY() + (double) targetEntity.getEyeHeight() * 0.5, targetEntity.getZ());
                } else if (entity1 != null) {
                    targetPos = new Vec3(entity1.getX(), entity1.getY(), entity1.getZ());
                } else {
                    Vec3 start = sender.getEyePosition(1.0F);
                    Vec3 end = start.add(sender.getLookAngle().scale(40.0));
                    HitResult result = worldIn.clip(new ClipContext(start, end, Block.COLLIDER, Fluid.NONE, sender));
                    targetPos = result.getLocation();
                }
                int counter = StatHelper.increase(sender, RegistryEvents.SWORD_SUMMONED, 1);
                boolean sided = counter % 2 == 0;
                for (int i = 0; i < 9; i++)
                    sa(worldIn, sender, targetPos, state, sided);
                sender.playNotifySound(SoundEvents.CHORUS_FRUIT_TELEPORT, SoundSource.PLAYERS, 0.2F, 1.45F);
            });
        }
    }

    /**
     * 生成sa
     *
     * @param worldIn
     * @param sender
     * @param targetPos 目标位置
     * @param state
     * @param sided
     */
    private static void sa(Level worldIn, Player sender, Vec3 targetPos, ISlashBladeState state, boolean sided) {
        InfinitySA ss = new InfinitySA(RegistryEvents.SummonedSword, worldIn);
        Vec3 pos = sender.getEyePosition(1.0F).add(VectorHelper.getVectorForRotation(0.0F, sender.yRotO + 90.0F).scale(sided ? 1.0 : -1.0));
        ss.setPos(pos.x, pos.y, pos.z);
        Vec3 dir = targetPos.subtract(pos).normalize();
        ss.shoot(dir.x, dir.y, dir.z, 3.0F, 0.0F);
        ss.setShooter(sender);
        ss.setDamage(Float.MAX_VALUE);

        int r = Math.abs((int) targetPos.x());
        int g = Math.abs((int) targetPos.y());
        int b = Math.abs((int) targetPos.z());
        Color color = new Color(r > 255 ? r % 255 : r, g > 255 ? g % 255 : g, b > 255 ? b % 255 : b);

        ss.setColor(color.getRGB());
        ss.setRoll(sender.getRandom().nextFloat() * 360.0F);
        worldIn.addFreshEntity(ss);
    }
}
