package com.yuo.es.Entity;

import com.yuo.endless.Event.EventHandler;
import mods.flammpfeil.slashblade.SlashBlade.RegistryEvents;
import mods.flammpfeil.slashblade.entity.EntityJudgementCut;
import mods.flammpfeil.slashblade.entity.EntitySlashEffect;
import mods.flammpfeil.slashblade.util.AttackManager;
import mods.flammpfeil.slashblade.util.KnockBacks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class InfinityJC extends EntityJudgementCut {
    public InfinityJC(EntityType<? extends ProjectileEntity> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.ticksExisted < 8 && this.ticksExisted % 2 == 0) {
            this.playSound(this.getHitEntitySound(), 0.2F, 0.5F + 0.25F * this.rand.nextFloat());
        }

        if (this.getShooter() != null) {
            AxisAlignedBB bb = this.getBoundingBox();
            if (this.ticksExisted % 2 == 0) {
                KnockBacks knockBackType = this.getIsCritical() ? KnockBacks.toss : KnockBacks.cancel;
                AttackManager.areaAttack(this, knockBackType.action, 4.0, this.doCycleHit(), false);
            }

            if (this.ticksExisted % 2 == 0) {
                BlockPos pos = this.getPosition();
                AxisAlignedBB aabb = new AxisAlignedBB(pos.add(-16, -8, -16), pos.add(16,16,16));
                for (LivingEntity living : world.getEntitiesWithinAABB(LivingEntity.class, aabb)) {
                    if (living.isAlive() && living != this.getShooter()){
                        double dist = Math.sqrt(living.getPosition().distanceSq(pos));
                        if (dist <= 16)
                            setEntityMotionFromVector(living, pos, 0.5f);
                    }
                }

            }

            if (this.getIsCritical() && 0 < this.ticksExisted && this.ticksExisted <= 3) {
                EntitySlashEffect jc = new EntitySlashEffect(RegistryEvents.SlashEffect, this.world);
                jc.setPositionAndRotation(this.getPosX(), this.getPosY(), this.getPosZ(), 120.0F * (float)this.ticksExisted + (float) this.getSeed(), 0.0F);
                jc.setRotationRoll(30.0F);
                jc.setShooter(this.getShooter());
                jc.setMute(false);
                jc.setIsCritical(true);
                jc.setDamage(1.0);
                jc.setColor(this.getColor());
                jc.setBaseSize(0.5F);
                jc.setKnockBack(KnockBacks.cancel);
                jc.setIndirect(true);
                this.world.addEntity(jc);
            }
        }

        this.tryDespawn();
    }

    /**
     * 设置实体移动
     * @param entity 要移动的实体
     * @param pos 目标坐标
     * @param modifier 移动距离 负数为排斥
     */
    public static void setEntityMotionFromVector(LivingEntity entity, BlockPos pos, double modifier) {
        Vector3d originalPosVector = new Vector3d(pos.getX(), pos.getY(), pos.getZ());
        Vector3d finalVector = originalPosVector.subtract(entity.getPositionVec());
        if (finalVector.length() > 1) { //向量长度超过1
            finalVector.normalize(); //化为标准1单位
        }
        double motionX = finalVector.x * modifier;
        double motionY = finalVector.y * modifier;
        double motionZ = finalVector.z * modifier;
        if (entity instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) entity;
            if (player.isCreative() || EventHandler.isInfinite(player) || player.abilities.isFlying) return; //创造或全套无尽 不会被吸引
            Vector3d vector3d = new Vector3d(motionX, motionY, motionZ).normalize();
            player.addVelocity(vector3d.x, vector3d.y, vector3d.z);
        }
        entity.setMotion(motionX, motionY, motionZ);
    }
}
