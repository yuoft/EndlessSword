package com.yuo.es.Mixin;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.LightningBoltRenderer;
import net.minecraft.util.math.vector.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(LightningBoltRenderer.class)
public abstract class SwordArtsMixin {

    @Inject(method = "func_229116_a_", at = @At(value = "HEAD"), cancellable = true)
    private static void ee(Matrix4f p_229116_0_, IVertexBuilder p_229116_1_, float p_229116_2_, float p_229116_3_, int p_229116_4_, float p_229116_5_, float p_229116_6_, float r1, float g1, float b1, float p_229116_10_, float p_229116_11_, boolean p_229116_12_, boolean p_229116_13_, boolean p_229116_14_, boolean p_229116_15_, CallbackInfo ci)  {
        Random random = new Random();
        float r = random.nextFloat();
        float g = random.nextFloat();
        float b = random.nextFloat();
        p_229116_1_.pos(p_229116_0_, p_229116_2_ + (p_229116_12_ ? p_229116_11_ : -p_229116_11_), (float)(p_229116_4_ * 16), p_229116_3_ + (p_229116_13_ ? p_229116_11_ : -p_229116_11_)).color(r, g, b, 0.3F).endVertex();
        p_229116_1_.pos(p_229116_0_, p_229116_5_ + (p_229116_12_ ? p_229116_10_ : -p_229116_10_), (float)((p_229116_4_ + 1) * 16), p_229116_6_ + (p_229116_13_ ? p_229116_10_ : -p_229116_10_)).color(r, g, b, 0.3F).endVertex();
        p_229116_1_.pos(p_229116_0_, p_229116_5_ + (p_229116_14_ ? p_229116_10_ : -p_229116_10_), (float)((p_229116_4_ + 1) * 16), p_229116_6_ + (p_229116_15_ ? p_229116_10_ : -p_229116_10_)).color(r, g, b, 0.3F).endVertex();
        p_229116_1_.pos(p_229116_0_, p_229116_2_ + (p_229116_14_ ? p_229116_11_ : -p_229116_11_), (float)(p_229116_4_ * 16), p_229116_3_ + (p_229116_15_ ? p_229116_11_ : -p_229116_11_)).color(r, g, b, 0.3F).endVertex();
        return;
    }
}
