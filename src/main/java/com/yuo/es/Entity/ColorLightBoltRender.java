package com.yuo.es.Entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class ColorLightBoltRender extends EntityRenderer<ColorLightBolt> {

    public ColorLightBoltRender(EntityRendererManager manager) {
        super(manager);
    }

    @Override
    public void render(ColorLightBolt lightningBolt, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer typeBuffer, int packedLightIn) {
        float[] lvt_7_1_ = new float[8];
        float[] lvt_8_1_ = new float[8];
        float lvt_9_1_ = 0.0F;
        float lvt_10_1_ = 0.0F;
        Random lvt_11_1_ = new Random(lightningBolt.boltVertex);

        for(int lvt_12_1_ = 7; lvt_12_1_ >= 0; --lvt_12_1_) {
            lvt_7_1_[lvt_12_1_] = lvt_9_1_;
            lvt_8_1_[lvt_12_1_] = lvt_10_1_;
            lvt_9_1_ += (float)(lvt_11_1_.nextInt(11) - 5);
            lvt_10_1_ += (float)(lvt_11_1_.nextInt(11) - 5);
        }

        IVertexBuilder lvt_11_2_ = typeBuffer.getBuffer(RenderType.getLightning());
        Matrix4f lvt_12_2_ = matrixStack.getLast().getMatrix();

        for(int lvt_13_1_ = 0; lvt_13_1_ < 4; ++lvt_13_1_) {
            Random lvt_14_1_ = new Random(lightningBolt.boltVertex);

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
    }

    private static void quad(Matrix4f matrix4f, IVertexBuilder vertexBuilder, float f1, float f2, int f3, float f4, float f5, float r, float g, float b, float v1, float v2, boolean v3, boolean v4, boolean v5, boolean v6) {
        Random random = new Random();
        float r1 = random.nextFloat();
        float g1 = random.nextFloat();
        float b1 = random.nextFloat();
        vertexBuilder.pos(matrix4f, f1 + (v3 ? v2 : -v2), (float)(f3 * 16), f2 + (v4 ? v2 : -v2)).color(r1, g1, b1, 0.3F).endVertex();
        vertexBuilder.pos(matrix4f, f4 + (v3 ? v1 : -v1), (float)((f3 + 1) * 16), f5 + (v4 ? v1 : -v1)).color(r1, g1, b1, 0.3F).endVertex();
        vertexBuilder.pos(matrix4f, f4 + (v5 ? v1 : -v1), (float)((f3 + 1) * 16), f5 + (v6 ? v1 : -v1)).color(r1, g1, b1, 0.3F).endVertex();
        vertexBuilder.pos(matrix4f, f1 + (v5 ? v2 : -v2), (float)(f3 * 16), f2 + (v6 ? v2 : -v2)).color(r1, g1, b1, 0.3F).endVertex();
    }

    @Override
    public ResourceLocation getEntityTexture(ColorLightBolt lightBolt) {
        return PlayerContainer.LOCATION_BLOCKS_TEXTURE;
    }
}
