package com.yuo.es.Items;

import com.yuo.endless.Config;
import com.yuo.endless.Entity.InfinityArrowEntity;
import mods.flammpfeil.slashblade.SlashBlade.RegistryEvents;
import mods.flammpfeil.slashblade.entity.BladeItemEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.network.PlayMessages;

public class InfinitySbItemEntity extends BladeItemEntity {
    private final Entity entityItem;

    public InfinitySbItemEntity(EntityType<? extends BladeItemEntity> type, Level world, Entity entityIn) {
        super(type, world);
        this.entityItem = entityIn;
    }

    public InfinitySbItemEntity(EntityType<? extends BladeItemEntity> entityType, Level level) {
        super(entityType, level);
        this.entityItem = null;
    }

    public static InfinitySbItemEntity createInstanceFromPacket(PlayMessages.SpawnEntity packet, Level worldIn) {
        return new InfinitySbItemEntity(RegistryEvents.BladeItem, worldIn);
    }

    @Override
    public boolean causeFallDamage(float distance, float damageMultiplier, DamageSource ds) {
        rangHit(this);
        return super.causeFallDamage(distance, damageMultiplier, ds);
    }

    public void init() {
        this.setInvulnerable(true);
        CompoundTag compoundnbt = this.saveWithoutId(new CompoundTag());
        compoundnbt.remove("Dimension");
        compoundnbt.putShort("Health", (short)100);
        compoundnbt.putInt("PickupDelay",40);
        float f1 = Mth.sin(this.getXRot() * 0.017453292F);
        float f2 = Mth.cos(this.getXRot() * 0.17453292F);
        float f3 = Mth.sin(this.getYRot() * 0.017453292F);
        float f4 = Mth.cos(this.getYRot() * 0.017453292F);
        float f5 = this.random.nextFloat() * 6.2831855F;
        float f6 = 0.02F * this.random.nextFloat();
        this.setDeltaMovement((double)(-f3 * f2 * 0.3F) + Math.cos((double)f5) * (double)f6, (double)(-f1 * 0.3F + 0.1F + (this.random.nextFloat() - this.random.nextFloat()) * 0.1F), (double)(f4 * f2 * 0.3F) + Math.sin((double)f5) * (double)f6);
        this.load(compoundnbt);
    }

    @Override
    public void tick() {
        super.tick();
        if (level().isClientSide) return;
        if (tickCount % 5 == 0){
            AABB axisalignedbb = this.getBoundingBox().deflate(5.0);
            this.level().getEntitiesOfClass(LivingEntity.class, axisalignedbb).forEach(entity -> {
                if (!(entity instanceof Player) && entity.isAlive() && entity instanceof LivingEntity)
                    hit(entity);
            });
        }
    }

    /**
     * 掉落到地面时 触发一次范围攻击。
     * @param item 物品实体
     */
    public void rangHit(Entity item){
        BlockPos pos = getOnPos();
        AABB axisalignedbb = new AABB(pos.offset(-3,-2,-3), pos.offset(3,2,3));
        item.level().getEntitiesOfClass(LivingEntity.class, axisalignedbb).forEach(entity -> {
            if (!(entity instanceof Player) && entity.isAlive() && entity instanceof LivingEntity)
                hit(entity);
        });
    }

    public static void hit(LivingEntity target){
        if (target instanceof EnderDragon dragon) {
            dragon.hurt(dragon.head, target.damageSources().fellOutOfWorld(), Float.POSITIVE_INFINITY);
        } else if (target instanceof WitherBoss wither) {
            wither.setInvulnerableTicks(0);
            wither.hurt(target.damageSources().fellOutOfWorld(), Float.POSITIVE_INFINITY);
        }

        if (target.isAlive() || target.getHealth() > 0.0F) {
            target.setHealth(-1.0F);
            if (!target.level().isClientSide) {
                target.die(target.damageSources().fellOutOfWorld());
            }

            if (Config.SERVER.swordKill.get()) {
                target.kill();
                target.deathTime = 20;
                target.remove(RemovalReason.KILLED);
            }
        }
    }
}
