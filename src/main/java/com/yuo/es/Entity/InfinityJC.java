package com.yuo.es.Entity;

import com.yuo.endless.Event.EventHandler;
import mods.flammpfeil.slashblade.SlashBlade.RegistryEvents;
import mods.flammpfeil.slashblade.entity.EntityJudgementCut;
import mods.flammpfeil.slashblade.entity.EntitySlashEffect;
import mods.flammpfeil.slashblade.entity.Projectile;
import mods.flammpfeil.slashblade.util.AttackManager;
import mods.flammpfeil.slashblade.util.KnockBacks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class InfinityJC extends EntityJudgementCut {

    public InfinityJC(EntityType<? extends Projectile> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.tickCount < 8 && this.tickCount % 2 == 0) {
            this.playSound(this.getHitEntitySound(), 0.2F, 0.5F + 0.25F * this.random.nextFloat());
        }

        if (this.getShooter() != null) {
            AABB bb = this.getBoundingBox();
            if (this.tickCount % 2 == 0) {
                KnockBacks knockBackType = this.getIsCritical() ? KnockBacks.toss : KnockBacks.cancel;
                AttackManager.areaAttack(this, knockBackType.action, 4.0, this.doCycleHit(), false);
            }

            if (this.tickCount % 2 == 0) {
                BlockPos pos = this.getOnPos();
                AABB aabb = new AABB(pos.offset(-16, -8, -16), pos.offset(16,16,16));
                for (LivingEntity living : level().getEntitiesOfClass(LivingEntity.class, aabb)) {
                    if (living.isAlive() && living != this.getShooter()){
                        double dist = Math.sqrt(living.getOnPos().distToCenterSqr(pos.getX(), pos.getY(), pos.getZ()));
                        if (dist <= 16)
                            setEntityMotionFromVector(living, pos, 0.5f);
                    }
                }

            }

            if (this.getIsCritical() && 0 < this.tickCount && this.tickCount <= 3) {
                EntitySlashEffect jc = new EntitySlashEffect(RegistryEvents.SlashEffect, this.level());
                jc.shoot(this.getX(), this.getY(), this.getZ(), 120.0F * (float)this.tickCount + (float) this.getSeed(), 0.0F);
                jc.setRotationRoll(30.0F);
                jc.setShooter(this.getShooter());
                jc.setMute(false);
                jc.setIsCritical(true);
                jc.setDamage(1.0);
                jc.setColor(this.getColor());
                jc.setBaseSize(0.5F);
                jc.setKnockBack(KnockBacks.cancel);
                jc.setIndirect(true);
                this.level().addFreshEntity(jc);
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
        Vec3 originalPosVector = new Vec3(pos.getX(), pos.getY(), pos.getZ());
        Vec3 finalVector = originalPosVector.subtract(entity.position());
        if (finalVector.length() > 1) { //向量长度超过1
            finalVector.normalize(); //化为标准1单位
        }
        double motionX = finalVector.x * modifier;
        double motionY = finalVector.y * modifier;
        double motionZ = finalVector.z * modifier;
        if (entity instanceof Player player){
            if (player.isCreative() || EventHandler.isInfinite(player) || player.getAbilities().flying) return; //创造或全套无尽 不会被吸引
            Vec3 vector3d = new Vec3(motionX, motionY, motionZ).normalize();
            player.setDeltaMovement(vector3d.x, vector3d.y, vector3d.z);
        }
        entity.setDeltaMovement(motionX, motionY, motionZ);
    }
}
