package com.goodbird.salamanderlib.example.client.renderer;

import com.goodbird.salamanderlib.example.client.model.ParticleExampleEntityModel;
import com.goodbird.salamanderlib.example.entity.ParticleExampleEntity;
import com.goodbird.salamanderlib.particles.emitter.BedrockEmitter;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.opengl.GL11;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;
import software.bernie.shadowed.eliotlash.mclib.utils.Interpolations;

import javax.vecmath.Matrix3f;
import javax.vecmath.Matrix4f;

public class ParticleExampleEntityRenderer extends GeoEntityRenderer<ParticleExampleEntity> {

    public ParticleExampleEntityRenderer(RenderManager renderManager) {
        super(renderManager, new ParticleExampleEntityModel());
    }

    @Override
    public void renderAfter(ParticleExampleEntity animatable, float ticks, float red, float green, float blue, float alpha) {
        BedrockEmitter emitter = animatable.emitter;
        emitter.prevGlobal.x=emitter.lastGlobal.x;
        emitter.prevGlobal.y=emitter.lastGlobal.y;
        emitter.prevGlobal.z=emitter.lastGlobal.z;

        emitter.lastGlobal.x=animatable.posX;
        emitter.lastGlobal.y=animatable.posY;
        emitter.lastGlobal.z=animatable.posZ;
        RenderHelper.disableStandardItemLighting();

        boolean shouldSit = (animatable.getRidingEntity() != null && animatable.getRidingEntity().shouldRiderSit());
        Pair<Float,Float> rotations = calculateRotations(animatable,ticks,shouldSit);
        deapplyRotations(animatable,this.handleRotationFloat(animatable, ticks),rotations.getKey(),ticks);

        GL11.glTranslated(-animatable.posX,-animatable.posY,-animatable.posZ);
        emitter.rotation.setIdentity();

        MATRIX_STACK.push();
        //MATRIX_STACK.rotateY((float) (Math.PI/2));
//		MATRIX_STACK.translate(1,1,0);
//		MATRIX_STACK.rotateY((float) (Math.PI/2));
//		MATRIX_STACK.scale(5,5,5);

        Matrix4f full = MATRIX_STACK.getModelMatrix();
        emitter.rotation = new Matrix3f(full.m00,full.m01,full.m02,full.m10,full.m11,full.m12,full.m20,full.m21,full.m22);
        emitter.lastGlobal.x+=full.m03;
        emitter.lastGlobal.y+=full.m13;
        emitter.lastGlobal.z+=full.m23;

        MATRIX_STACK.pop();
        emitter.render(ticks);
        RenderHelper.enableStandardItemLighting();
    }

    protected void deapplyRotations(ParticleExampleEntity entityLiving, float ageInTicks, float rotationYaw, float partialTicks) {
        if (entityLiving.deathTime > 0) {
            float f = ((float) entityLiving.deathTime + partialTicks - 1.0F) / 20.0F * 1.6F;
            f = MathHelper.sqrt(f);
            if (f > 1.0F) {
                f = 1.0F;
            }

            GlStateManager.rotate(-f * this.getDeathMaxRotation(entityLiving), 0, 0, 1);
        }
        else if ((entityLiving instanceof EntityLiving && ((EntityLiving)entityLiving).hasCustomName())) {
            String s = ChatFormatting.stripFormatting(entityLiving.getDisplayName().getUnformattedText());
            if (("Dinnerbone".equals(s) || "Grumm".equals(s))) {
                GlStateManager.translate(0.0D, (double) (entityLiving.height + 0.1F), 0.0D);
                GlStateManager.rotate(-180, 0, 0, 1);
            }
        }

        if (!entityLiving.isPlayerSleeping()) {
            GlStateManager.rotate(-(180.0F - rotationYaw), 0, 1, 0);
        }
    }

    public Pair<Float,Float> calculateRotations(EntityLivingBase entity, float partialTicks, boolean shouldSit){
        float f = Interpolations.lerpYaw(entity.prevRenderYawOffset, entity.renderYawOffset, partialTicks);
        float f1 = Interpolations.lerpYaw(entity.prevRotationYawHead, entity.rotationYawHead, partialTicks);
        float netHeadYaw = f1 - f;
        if (shouldSit && entity.getRidingEntity() instanceof EntityLivingBase) {
            EntityLivingBase livingentity = (EntityLivingBase) entity.getRidingEntity();
            f = Interpolations.lerpYaw(livingentity.prevRenderYawOffset, livingentity.renderYawOffset, partialTicks);
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