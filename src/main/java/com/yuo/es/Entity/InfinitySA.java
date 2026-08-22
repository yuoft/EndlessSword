package com.yuo.es.Entity;

import com.yuo.endless.EndlessUtils;
import com.yuo.endless.items.tool.InfinityDamageTypes;
import mods.flammpfeil.slashblade.entity.EntityAbstractSummonedSword;
import mods.flammpfeil.slashblade.entity.Projectile;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

public class InfinitySA extends EntityAbstractSummonedSword {

    public InfinitySA(EntityType<? extends Projectile> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
    }

    @Override
    protected void onHitEntity(EntityHitResult rayTraceResult) {
        super.onHitEntity(rayTraceResult);
        Entity entity = rayTraceResult.getEntity();
        Entity shooter = this.getShooter();

        if (entity.isAlive() && shooter instanceof LivingEntity living){
            entity.hurt(InfinityDamageTypes.infinity(living), Float.MAX_VALUE);
        }

        if (shooter instanceof Player player && entity instanceof LivingEntity living){
            EndlessUtils.atkInfinity(living, player);
        }
    }
}
