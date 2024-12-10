package com.yuo.es.Mixin;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LightningBoltRenderer;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.util.math.vector.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;
import java.util.Random;

@Mixin(LightningBoltRenderer.class)
public abstract class LightBoltRenderMixin {

    @Inject(method = "render(Lnet/minecraft/entity/effect/LightningBoltEntity;FFLcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;I)V"
    , at = @At("HEAD"), cancellable = true)
    public void render(LightningBoltEntity p_225623_1_, float p_225623_2_, float p_225623_3_, MatrixStack p_225623_4_, IRenderTypeBuffer p_225623_5_, int p_225623_6_, CallbackInfo ci) {
        boolean equals = "endless_swrod:color_light_bolt".equals(Objects.requireNonNull(p_225623_1_.getCustomName()).getString());
        if (equals){
            float[] lvt_7_1_ = new float[8];
            float[] lvt_8_1_ = new float[8];
            float lvt_9_1_ = 0.0F;
            float lvt_10_1_ = 0.0F;
            Random lvt_11_1_ = new Random(p_225623_1_.boltVertex);

            for(int lvt_12_1_ = 7; lvt_12_1_ >= 0; --lvt_12_1_) {
                lvt_7_1_[lvt_12_1_] = lvt_9_1_;
                lvt_8_1_[lvt_12_1_] = lvt_10_1_;
                lvt_9_1_ += (float)(lvt_11_1_.nextInt(11) - 5);
                lvt_10_1_ += (float)(lvt_11_1_.nextInt(11) - 5);
            }

            IVertexBuilder lvt_11_2_ = p_225623_5_.getBuffer(RenderType.getLightning());
            Matrix4f lvt_12_2_ = p_225623_4_.getLast().getMatrix();

            for(int lvt_13_1_ = 0; lvt_13_1_ < 4; ++lvt_13_1_) {
                Random lvt_14_1_ = new Random(p_225623_1_.boltVertex);

                for(int lvt_15_1_ = 0; lvt_15_1_ < 3; ++lvt_15_1_) {
                    int lvt_16_1_ = 7;
                    int lvt_17_1_ = 0;
                    if (lvt_15_1_ > 0) {
                        lvt_16_1_ = 7 - lvt_15_1_;
                    }

                    if (lvt_15_1_ > 0) {
                        lvt_17_1_ = lvt_16_1_ - 2;
                    }

                    float lvt_18_1_ = lvt_7_1_[lvt_16_1_] - lvt_9_1_;
                    float lvt_19_1_ = lvt_8_1_[lvt_16_1_] - lvt_10_1_;

                    for(int lvt_20_1_ = lvt_16_1_; lvt_20_1_ >= lvt_17_1_; --lvt_20_1_) {
                        float lvt_21_1_ = lvt_18_1_;
                        float lvt_22_1_ = lvt_19_1_;
                        if (lvt_15_1_ == 0) {
                            lvt_18_1_ += (float)(lvt_14_1_.nextInt(11) - 5);
                            lvt_19_1_ += (float)(lvt_14_1_.nextInt(11) - 5);
                        } else {
                            lvt_18_1_ += (float)(lvt_14_1_.nextInt(31) - 15);
                            lvt_19_1_ += (float)(lvt_14_1_.nextInt(31) - 15);
                        }

                        float lvt_23_1_ = 0.5F;
                        float lvt_24_1_ = 0.45F;
                        float lvt_25_1_ = 0.45F;
                        float lvt_26_1_ = 0.5F;
                        float lvt_27_1_ = 0.1F + (float)lvt_13_1_ * 0.2F;
                        if (lvt_15_1_ == 0) {
                            lvt_27_1_ = (float)((double)lvt_27_1_ * ((double)lvt_20_1_ * 0.1 + 1.0));
                        }

                        float lvt_28_1_ = 0.1F + (float)lvt_13_1_ * 0.2F;
                        if (lvt_15_1_ == 0) {
                            lvt_28_1_ *= (float)(lvt_20_1_ - 1) * 0.1F + 1.0F;
                        }

                        quad(lvt_12_2_, lvt_11_2_, lvt_18_1_, lvt_19_1_, lvt_20_1_, lvt_21_1_, lvt_22_1_, 0.45F, 0.45F, 0.5F, lvt_27_1_, lvt_28_1_, false, false, true, false);
                        quad(lvt_12_2_, lvt_11_2_, lvt_18_1_, lvt_19_1_, lvt_20_1_, lvt_21_1_, lvt_22_1_, 0.45F, 0.45F, 0.5F, lvt_27_1_, lvt_28_1_, true, false, true, true);
                        quad(lvt_12_2_, lvt_11_2_, lvt_18_1_, lvt_19_1_, lvt_20_1_, lvt_21_1_, lvt_22_1_, 0.45F, 0.45F, 0.5F, lvt_27_1_, lvt_28_1_, true, true, false, true);
                        quad(lvt_12_2_, lvt_11_2_, lvt_18_1_, lvt_19_1_, lvt_20_1_, lvt_21_1_, lvt_22_1_, 0.45F, 0.45F, 0.5F, lvt_27_1_, lvt_28_1_, false, true, false, false);
                    }
                }
            }
            ci.cancel();
        }
    }

    private static void quad(Matrix4f p_229116_0_, IVertexBuilder p_229116_1_, float p_229116_2_, float p_229116_3_, int p_229116_4_, float p_229116_5_, float p_229116_6_, float p_229116_7_, float p_229116_8_, float p_229116_9_, float p_229116_10_, float p_229116_11_, boolean p_229116_12_, boolean p_229116_13_, boolean p_229116_14_, boolean p_229116_15_) {
        Random random = new Random();
        float r1 = random.nextFloat();
        float g1 = random.nextFloat();
        float b1 = random.nextFloat();
        p_229116_1_.pos(p_229116_0_, p_229116_2_ + (p_229116_12_ ? p_229116_11_ : -p_229116_11_), (float)(p_229116_4_ * 16), p_229116_3_ + (p_229116_13_ ? p_229116_11_ : -p_229116_11_)).color(r1, g1, b1, 1.0F).endVertex();
        p_229116_1_.pos(p_229116_0_, p_229116_5_ + (p_229116_12_ ? p_229116_10_ : -p_229116_10_), (float)((p_229116_4_ + 1) * 16), p_229116_6_ + (p_229116_13_ ? p_229116_10_ : -p_229116_10_)).color(r1, g1, b1, 1.0F).endVertex();
        p_229116_1_.pos(p_229116_0_, p_229116_5_ + (p_229116_14_ ? p_229116_10_ : -p_229116_10_), (float)((p_229116_4_ + 1) * 16), p_229116_6_ + (p_229116_15_ ? p_229116_10_ : -p_229116_10_)).color(r1, g1, b1, 1.0F).endVertex();
        p_229116_1_.pos(p_229116_0_, p_229116_2_ + (p_229116_14_ ? p_229116_11_ : -p_229116_11_), (float)(p_229116_4_ * 16), p_229116_3_ + (p_229116_15_ ? p_229116_11_ : -p_229116_11_)).color(r1, g1, b1, 1.0F).endVertex();
    }
}
