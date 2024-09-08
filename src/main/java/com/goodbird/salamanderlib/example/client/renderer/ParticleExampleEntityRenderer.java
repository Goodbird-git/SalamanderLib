package com.goodbird.salamanderlib.example.client.renderer;

import com.goodbird.salamanderlib.example.client.model.ParticleExampleEntityModel;
import com.goodbird.salamanderlib.example.entity.ParticleExampleEntity;
import com.goodbird.salamanderlib.mixin.impl.IMatrix4f;
import com.goodbird.salamanderlib.particles.emitter.BedrockEmitter;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.TextFormatting;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.opengl.GL11;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;
import software.bernie.shadowed.eliotlash.mclib.utils.Interpolations;

import javax.vecmath.Matrix3f;

public class ParticleExampleEntityRenderer extends GeoEntityRenderer<ParticleExampleEntity> {

    public ParticleExampleEntityRenderer(EntityRendererManager renderManager) {
        super(renderManager, new ParticleExampleEntityModel());
    }

    public void renderLate(ParticleExampleEntity animatable, MatrixStack MATRIX_STACK, float ticks, IRenderTypeBuffer renderTypeBuffer, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        BedrockEmitter emitter = animatable.emitter;
        emitter.prevGlobal.x=emitter.lastGlobal.x;
        emitter.prevGlobal.y=emitter.lastGlobal.y;
        emitter.prevGlobal.z=emitter.lastGlobal.z;

        emitter.lastGlobal.x=animatable.getX();
        emitter.lastGlobal.y=animatable.getY();
        emitter.lastGlobal.z=animatable.getZ();
        //RenderHelper.disableStandardItemLighting();

//        boolean shouldSit = (animatable.getControllingPassenger() != null && animatable.getControllingPassenger().shouldRiderSit());
//        Pair<Float,Float> rotations = calculateRotations(animatable,ticks,shouldSit);
//        deapplyRotations(MATRIX_STACK, animatable,this.handleRotationFloat(animatable, ticks),rotations.getKey(),ticks);

        GL11.glTranslated(-animatable.getX(),-animatable.getY(),-animatable.getZ());
        emitter.rotation.setIdentity();

        MATRIX_STACK.pushPose();
        //MATRIX_STACK.rotateY((float) (Math.PI/2));
//		MATRIX_STACK.translate(1,1,0);
//		MATRIX_STACK.rotateY((float) (Math.PI/2));
//		MATRIX_STACK.scale(5,5,5);

        IMatrix4f full = (IMatrix4f)(Object)MATRIX_STACK.last().pose();
        emitter.rotation = new Matrix3f(full.m00(),full.m01(),full.m02(),full.m10(),full.m11(),full.m12(),full.m20(),full.m21(),full.m22());
        emitter.lastGlobal.x+=full.m03();
        emitter.lastGlobal.y+=full.m13();
        emitter.lastGlobal.z+=full.m23();

        MATRIX_STACK.popPose();
        emitter.render(ticks);
        //RenderHelper.enableStandardItemLighting();
    }

    protected void deapplyRotations(MatrixStack matrixStack, ParticleExampleEntity entityLiving, float ageInTicks, float rotationYaw, float partialTicks) {
        if (entityLiving.deathTime > 0) {
            float f = ((float) entityLiving.deathTime + partialTicks - 1.0F) / 20.0F * 1.6F;
            f = MathHelper.sqrt(f);
            if (f > 1.0F) {
                f = 1.0F;
            }
            matrixStack.mulPose(Vector3f.ZN.rotationDegrees(f * this.getDeathMaxRotation(entityLiving)));
        }
        else if ((entityLiving instanceof LivingEntity && ((LivingEntity)entityLiving).hasCustomName())) {
            String s = TextFormatting.stripFormatting(entityLiving.getDisplayName().getContents());
            if (("Dinnerbone".equals(s) || "Grumm".equals(s))) {
                matrixStack.translate(0.0D, (double) (entityLiving.getBbHeight() + 0.1F), 0.0D);
                matrixStack.mulPose(Vector3f.ZN.rotationDegrees(180));
            }
        }

        if (!entityLiving.isSleeping()) {
            matrixStack.mulPose(Vector3f.YN.rotationDegrees(180.0F - rotationYaw));
        }
    }

    public Pair<Float,Float> calculateRotations(LivingEntity entity, float partialTicks, boolean shouldSit){
        float f = Interpolations.lerpYaw(entity.yBodyRotO, entity.yBodyRot, partialTicks);
        float f1 = Interpolations.lerpYaw(entity.yHeadRotO, entity.yHeadRot, partialTicks);
        float netHeadYaw = f1 - f;
        if (shouldSit && entity.getControllingPassenger() instanceof LivingEntity) {
            LivingEntity livingentity = (LivingEntity) entity.getControllingPassenger();
            f = Interpolations.lerpYaw(livingentity.yBodyRotO, livingentity.yBodyRot, partialTicks);
            netHeadYaw = f1 - f;
            float f3 = software.bernie.shadowed.eliotlash.mclib.utils.MathHelper.wrapDegrees(netHeadYaw);
            if (f3 < -85.0F) {
                f3 = -85.0F;
            }

            if (f3 >= 85.0F) {
                f3 = 85.0F;
            }

            f = f1 - f3;
            if (f3 * f3 > 2500.0F) {
                f += f3 * 0.2F;
            }

            netHeadYaw = f1 - f;
        }
        return new ImmutablePair<>(f,netHeadYaw);
    }
}