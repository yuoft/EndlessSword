package com.yuo.es.Items;

import com.yuo.endless.Config;
import mods.flammpfeil.slashblade.entity.BladeItemEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class InfinitySbItemEntity extends BladeItemEntity {
    private final Entity entityItem;

    public InfinitySbItemEntity(EntityType<? extends BladeItemEntity> type, World world, Entity entityIn) {
        super(type, world);
        this.entityItem = entityIn;
    }

    @Override
    public boolean onLivingFall(float distance, float damageMultiplier) {
        if (entityItem != null)
            rangHit(entityItem);
        return super.onLivingFall(distance, damageMultiplier);
    }

    /**
     * 掉落到地面时 触发一次范围攻击。
     * @param item 物品实体
     */
    public void rangHit(Entity item){
        BlockPos pos = getPosition();
        AxisAlignedBB axisalignedbb = new AxisAlignedBB(pos.add(-3,-2,-3), pos.add(3,2,3));
        item.world.getEntitiesWithinAABB(LivingEntity.class, axisalignedbb).forEach(entity -> {
            if (!(entity instanceof PlayerEntity) && entity.isAlive() && entity instanceof LivingEntity)
                hit(entity);
        });
    }

    public static void hit(LivingEntity target){
        if (target instanceof EnderDragonEntity) {
            EnderDragonEntity dragon = (EnderDragonEntity)target;
            dragon.attackEntityPartFrom(dragon.dragonPartHead, DamageSource.OUT_OF_WORLD, Float.POSITIVE_INFINITY);
        } else if (target instanceof WitherEntity) {
            WitherEntity wither = (WitherEntity)target;
            wither.setInvulTime(0);
            wither.attackEntityFrom(DamageSource.OUT_OF_WORLD, Float.POSITIVE_INFINITY);
        }

        if (target.isAlive() || target.getHealth() > 0.0F) {
            target.setHealth(-1.0F);
            if (!target.world.isRemote) {
                target.onDeath(DamageSource.OUT_OF_WORLD);
            }

            if (Config.SERVER.swordKill.get()) {
                target.onKillCommand();
                target.deathTime = 20;
                target.remove(true);
            }
        }
    }
}
