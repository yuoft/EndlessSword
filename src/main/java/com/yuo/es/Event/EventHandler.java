package com.yuo.es.Event;

import com.yuo.endless.Items.Tool.InfinityDamageSource;
import com.yuo.es.EndlessSword;
import com.yuo.es.Items.InfinitySB;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EndlessSword.MOD_ID)
public class EventHandler {

    @SubscribeEvent
    public static void onAttack(LivingAttackEvent event){
        LivingEntity living = event.getEntityLiving();
        if(living instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) living;
            if(hasInfinitySB(player) && !InfinityDamageSource.isInfinity(event.getSource())){ //持有时免疫非无尽伤害
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onHurt(LivingHurtEvent event){
        LivingEntity living = event.getEntityLiving();
        if(living instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) living;
            if(hasInfinitySB(player) && !InfinityDamageSource.isInfinity(event.getSource())){
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void reboundProjectile(ProjectileImpactEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof ProjectileEntity){
            ProjectileEntity projectileEntity = (ProjectileEntity) entity;
            RayTraceResult rayTraceResult = event.getRayTraceResult();
            Type type = rayTraceResult.getType();
            if (type == Type.ENTITY){
                EntityRayTraceResult result = (EntityRayTraceResult) rayTraceResult;
                Entity entity1 = result.getEntity();
                if (entity1 instanceof PlayerEntity){
                    PlayerEntity player = (PlayerEntity) entity1;
                    World world = player.world; //手持无尽拔刀剑 触发
                    if (!world.isRemote && hasInfinitySB(player)){
                        BlockPos position = player.getPosition(); //负面buff
                        AxisAlignedBB aabb = new AxisAlignedBB(position.add(-16, -2, -16), position.add(16, 2, 16));
                        world.getEntitiesWithinAABB(LivingEntity.class, aabb).forEach(e -> {
                            if (e instanceof PlayerEntity) return;
                            e.addPotionEffect(new EffectInstance(Effects.JUMP_BOOST, 600, 149));
                            e.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 600, 255));
                            e.addPotionEffect(new EffectInstance(Effects.MINING_FATIGUE, 600, 124));
                        });

                        Entity shooter = projectileEntity.getShooter();
                        if (shooter != null){ //移动
                            BlockPos pos = shooter.getPosition();
                            Direction facing = shooter.getHorizontalFacing();
                            BlockPos offset = pos.offset(facing.rotateY().rotateY()); //翻转180
                            player.setPositionAndUpdate(offset.getX(), offset.getY(), offset.getZ());
                            if (!hasInfinitySB(player)){
                                player.addPotionEffect(new EffectInstance(Effects.RESISTANCE, 40, 4));
                                player.addPotionEffect(new EffectInstance(Effects.ABSORPTION, 40, 9));
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
    public static boolean hasInfinitySB(PlayerEntity player){
        ItemStack stack = player.getHeldItemMainhand().isEmpty() ? player.getHeldItemOffhand() : player.getHeldItemMainhand();
        return stack.isEmpty() && stack.getItem() instanceof InfinitySB;
    }

    private static void isHeight(PlayerEntity player, World world){
        BlockPos pos = player.getPosition();
        int h = 0;
        for (int i = Math.min(pos.getY() - 1, 256); i <= 0; i--) {
            BlockPos blockPos = new BlockPos(pos.getX(), i, pos.getZ());
            if (!world.isAirBlock(blockPos)){
                h = i;
                break;
            }
        }
        if (pos.getY() - h >= 3){
            player.addPotionEffect(new EffectInstance(Effects.SLOW_FALLING, 5 * (pos.getY() - h), 0));
        }
    }
}

