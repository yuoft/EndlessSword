package com.yuo.es.Event;

import com.yuo.es.EndlessSword;
import com.yuo.es.Entity.InfinitySA;
import mods.flammpfeil.slashblade.SlashBlade.RegistryEvents;
import mods.flammpfeil.slashblade.capability.slashblade.ISlashBladeState;
import mods.flammpfeil.slashblade.event.InputCommandEvent;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.util.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceContext.BlockMode;
import net.minecraft.util.math.RayTraceContext.FluidMode;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.EnumSet;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = EndlessSword.MOD_ID)
public class ClientHandler {

    @SubscribeEvent
    public static void registerModels(InputCommandEvent event) {
        EnumSet<InputCommand> old = event.getOld();
        EnumSet<InputCommand> current = event.getCurrent();
        ServerPlayerEntity sender = event.getPlayer();
        boolean onDown = !old.contains(InputCommand.M_DOWN) && current.contains(InputCommand.M_DOWN);
        boolean var10000;
        if (old.contains(InputCommand.M_DOWN) && !current.contains(InputCommand.M_DOWN)) {
            var10000 = true;
        } else {
            var10000 = false;
        }

        if (onDown) {
            World worldIn = sender.world;
            sender.getHeldItemMainhand().getCapability(ItemSlashBlade.BLADESTATE).ifPresent((state) -> {
                if (sender.experienceLevel > 0) {
                    sender.giveExperiencePoints(-1);
                    Optional<Object> foundTarget = Stream.of(Optional.ofNullable(state.getTargetEntity(sender.world)), RayTraceHelper.rayTrace(sender.world, sender, sender.getEyePosition(1.0F), sender.getLookVec(), 12.0, 12.0,
                            (Predicate)null).filter((r) -> ((RayTraceResult) r).getType() == Type.ENTITY).filter((r) -> {
                        EntityRayTraceResult er = (EntityRayTraceResult)r;
                        Entity target = ((EntityRayTraceResult)r).getEntity();
                        boolean isMatch = true;
                        if (target instanceof LivingEntity) {
                            isMatch = TargetSelector.lockon_focus.canTarget(sender, (LivingEntity)target);
                        }

                        return isMatch;
                    }).map((r) -> ((EntityRayTraceResult)r).getEntity())).filter(Optional::isPresent).map(Optional::get).findFirst();
                    Vector3d targetPos = foundTarget.map((e) -> {
                        if (e instanceof Entity){
                            Entity e1 = (Entity)e;
                            return new Vector3d(e1.getPosX(), e1.getPosY() + (double)e1.getEyeHeight() * 0.5, e1.getPosZ());
                        }
                        return Vector3d.ZERO;
                    }).orElseGet(() -> {
                        Vector3d start = sender.getEyePosition(1.0F);
                        Vector3d end = start.add(sender.getLookVec().scale(40.0));
                        RayTraceResult result = worldIn.rayTraceBlocks(new RayTraceContext(start, end, BlockMode.COLLIDER, FluidMode.NONE, sender));
                        return result.getHitVec();
                    });
                    int counter = StatHelper.increase(sender, RegistryEvents.SWORD_SUMMONED, 1);
                    boolean sided = counter % 2 == 0;
                    for (int i = 0; i < 9; i++)
                        sa(worldIn, sender, targetPos, state, sided);
                    sender.playSound(SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, SoundCategory.PLAYERS, 0.2F, 1.45F);
                }
            });
        }
    }

    /**
     * 生成sa
     * @param worldIn
     * @param sender
     * @param targetPos
     * @param state
     * @param sided
     */
    private static void sa(World worldIn, PlayerEntity sender, Vector3d targetPos, ISlashBladeState state, boolean sided){
        InfinitySA ss = new InfinitySA(RegistryEvents.SummonedSword, worldIn);
        Vector3d pos = sender.getEyePosition(1.0F).add(VectorHelper.getVectorForRotation(0.0F, sender.getYaw(0.0F) + 90.0F).scale(sided ? 1.0 : -1.0));
        ss.setPosition(pos.x, pos.y, pos.z);
        Vector3d dir = targetPos.subtract(pos).normalize();
        ss.shoot(dir.x, dir.y, dir.z, 3.0F, 0.0F);
        ss.setShooter(sender);
        ss.setDamage(Float.POSITIVE_INFINITY);
        ss.setColor(state.getColorCode());
        ss.setRoll(sender.getRNG().nextFloat() * 360.0F);
        worldIn.addEntity(ss);
    }
}
