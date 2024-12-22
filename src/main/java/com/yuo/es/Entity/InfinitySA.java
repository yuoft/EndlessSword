package com.yuo.es.Entity;

import com.yuo.endless.Items.Tool.InfinitySword;
import mods.flammpfeil.slashblade.entity.EntityAbstractSummonedSword;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.world.World;

public class InfinitySA extends EntityAbstractSummonedSword {

    public InfinitySA(EntityType<? extends ProjectileEntity> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
    }

    @Override
    protected void onHitEntity(EntityRayTraceResult rayTraceResult) {
        super.onHitEntity(rayTraceResult);
        Entity entity = rayTraceResult.getEntity();
        Entity shooter = this.getShooter();
        if (shooter instanceof PlayerEntity){
            InfinitySword.damageGuardian(entity, (PlayerEntity)shooter);
        }
    }
}
