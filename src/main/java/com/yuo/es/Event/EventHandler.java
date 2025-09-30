package com.yuo.es.Event;

import com.yuo.endless.Items.Tool.InfinityDamageTypes;
import com.yuo.es.EndlessSword;
import com.yuo.es.Items.InfinitySB;
import mods.flammpfeil.slashblade.entity.Projectile;
import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EndlessSword.MOD_ID)
public class EventHandler {

    @SubscribeEvent
    public static void onAttack(LivingAttackEvent event){
        LivingEntity living = event.getEntity();
        if(living instanceof Player player){
            if(hasInfinitySB(player) && !InfinityDamageTypes.isInfinity(event.getSource())){ //持有时免疫非无尽伤害
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onHurt(LivingHurtEvent event){
        LivingEntity living = event.getEntity();
        if(living instanceof Player player){
            if(hasInfinitySB(player) && !InfinityDamageTypes.isInfinity(event.getSource())){
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void reboundProjectile(ProjectileImpactEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Projectile projectile){
            HitResult rayTraceResult = event.getRayTraceResult();
            Type type = rayTraceResult.getType();
            if (type == Type.ENTITY){
                EntityHitResult result = (EntityHitResult) rayTraceResult;
                Entity entity1 = result.getEntity();
                if (entity1 instanceof Player){
                    Player player = (Player) entity1;
                    Level world = player.level(); //手持无尽拔刀剑 触发
                    if (!world.isClientSide && hasInfinitySB(player)){
                        BlockPos position = player.getOnPos(); //负面buff
                        AABB aabb = new AABB(position.offset(-16, -2, -16), position.offset(16, 2, 16));
                        world.getEntitiesOfClass(LivingEntity.class, aabb).forEach(e -> {
                            if (e instanceof Player) return;
                            e.addEffect(new MobEffectInstance(MobEffects.JUMP, 600, 149));
                            e.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 600, 255));
                            e.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 600, 124));
                        });

                        Entity shooter = projectile.getOwner();
                        if (shooter != null){ //移动
                            BlockPos pos = shooter.getOnPos();
                            Direction facing = shooter.getDirection();
                            BlockPos offset = pos.offset(facing.getNormal().multiply(180)); //翻转180
                            player.setPos(offset.getX(), offset.getY(), offset.getZ());
                            if (!hasInfinitySB(player)){
                                player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 40, 4));
                                player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 40, 9));
                                isHeight(player, world);
                            }
                        }
                        event.setCanceled(true);
                    }
                }
            }
        }
    }

    /**
     * 玩家是否持有 无尽拔刀剑
     * @param player 玩家
     */
    public static boolean hasInfinitySB(Player player){
        ItemStack stack = player.getMainHandItem().isEmpty() ? player.getOffhandItem() : player.getMainHandItem();
        return stack.isEmpty() && stack.getItem() instanceof InfinitySB;
    }

    private static void isHeight(Player player, Level world){
        BlockPos pos = player.getOnPos();
        int h = 0;
        for (int i = Math.min(pos.getY() - 1, 256); i <= 0; i--) {
            BlockPos blockPos = new BlockPos(pos.getX(), i, pos.getZ());
            if (!world.getBlockState(blockPos).isAir()){
                h = i;
                break;
            }
        }
        if (pos.getY() - h >= 3){
            player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 5 * (pos.getY() - h), 0));
        }
    }
}

