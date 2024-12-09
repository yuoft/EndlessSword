package com.yuo.es.Entity;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class ColorLightBolt extends Entity {
    private int lightningState;
    public long boltVertex;
    private int boltLivingTime;
    private boolean effectOnly;
    @Nullable
    private ServerPlayerEntity caster;
    private float damage = 10000.0F;

    public ColorLightBolt(EntityType<? extends ColorLightBolt> entityType, World world) {
        super(entityType, world);
        this.ignoreFrustumCheck = true;
        this.lightningState = 2;
        this.boltVertex = this.rand.nextLong();
        this.boltLivingTime = this.rand.nextInt(3) + 1;
    }

    public ColorLightBolt(World world){
        this(EsEntityTypes.COLOR_LIGHT_BOLT.get(), world);
    }

    public void setEffectOnly(boolean b) {
        this.effectOnly = b;
    }

    public SoundCategory getSoundCategory() {
        return SoundCategory.WEATHER;
    }

    public void setCaster(@Nullable ServerPlayerEntity serverPlayer) {
        this.caster = serverPlayer;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public float getDamage() {
        return this.damage;
    }

    public void tick() {
        super.tick();
        if (this.lightningState == 2) {
            Difficulty difficulty = this.world.getDifficulty();
            if (difficulty == Difficulty.NORMAL || difficulty == Difficulty.HARD) {
                this.igniteBlocks(4);
            }

            this.world.playSound(null, this.getPosX(), this.getPosY(), this.getPosZ(), SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER, SoundCategory.WEATHER, 10000.0F, 0.8F + this.rand.nextFloat() * 0.2F);
            this.world.playSound(null, this.getPosX(), this.getPosY(), this.getPosZ(), SoundEvents.ENTITY_LIGHTNING_BOLT_IMPACT, SoundCategory.WEATHER, 2.0F, 0.5F + this.rand.nextFloat() * 0.2F);
        }

        --this.lightningState;
        if (this.lightningState < 0) {
            if (this.boltLivingTime == 0) {
                this.remove();
            } else if (this.lightningState < -this.rand.nextInt(10)) {
                --this.boltLivingTime;
                this.lightningState = 1;
                this.boltVertex = this.rand.nextLong();
                this.igniteBlocks(0);
            }
        }

        if (this.lightningState >= 0) {
            if (!(this.world instanceof ServerWorld)) {
                this.world.setTimeLightningFlash(2);
            } else if (!this.effectOnly) {
                double d0 = 3.0;
                List<Entity> list = this.world.getEntitiesInAABBexcluding(this, new AxisAlignedBB(this.getPosX() - 3.0, this.getPosY() - 3.0, this.getPosZ() - 3.0, this.getPosX() + 3.0, this.getPosY() + 6.0 + 3.0, this.getPosZ() + 3.0), Entity::isAlive);

                for (Entity entity : list) {
                    if (entity instanceof LivingEntity && entity.isAlive()) {
                        entity.forceFireTicks(entity.getFireTimer() + 1);
                        if (entity.getFireTimer() == 0) {
                            this.setFire(8);
                        }
                        entity.attackEntityFrom(DamageSource.LIGHTNING_BOLT, this.damage);
                    }
                }

                if (this.caster != null) {
                    CriteriaTriggers.CHANNELED_LIGHTNING.trigger(this.caster, list);
                }
            }
        }

    }

    private void igniteBlocks(int j) {
        if (!this.effectOnly && !this.world.isRemote && this.world.getGameRules().getBoolean(GameRules.DO_FIRE_TICK)) {
            BlockPos blockpos = this.getPosition();
            BlockState blockstate = AbstractFireBlock.getFireForPlacement(this.world, blockpos);
            if (this.world.getBlockState(blockpos).isAir() && blockstate.isValidPosition(this.world, blockpos)) {
                this.world.setBlockState(blockpos, blockstate);
            }

            for(int i = 0; i < j; ++i) {
                BlockPos blockpos1 = blockpos.add(this.rand.nextInt(3) - 1, this.rand.nextInt(3) - 1, this.rand.nextInt(3) - 1);
                blockstate = AbstractFireBlock.getFireForPlacement(this.world, blockpos1);
                if (this.world.getBlockState(blockpos1).isAir() && blockstate.isValidPosition(this.world, blockpos1)) {
                    this.world.setBlockState(blockpos1, blockstate);
                }
            }
        }

    }

    @OnlyIn(Dist.CLIENT)
    public boolean isInRangeToRenderDist(double v) {
        double d0 = 64.0 * getRenderDistanceWeight();
        return v < d0 * d0;
    }

    protected void registerData() {
    }

    protected void readAdditional(CompoundNBT nbt) {
    }

    protected void writeAdditional(CompoundNBT nbt) {
    }

    public IPacket<?> createSpawnPacket() {
        return new SSpawnObjectPacket(this);
    }
}
