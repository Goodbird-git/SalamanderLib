package com.goodbird.salamanderlib.helper;

import com.goodbird.salamanderlib.mixin.IAdvController;
import com.goodbird.salamanderlib.mixin.impl.IMatrix4f;
import com.goodbird.salamanderlib.particles.emitter.BedrockEmitter;
import com.goodbird.salamanderlib.util.PositionUtils;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;
import software.bernie.geckolib3.util.RenderUtils;

import javax.vecmath.Matrix3f;
import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3d;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ParticleDrawingHelper {

    public static void drawParticles(IGeoRenderer renderer, GeoModel model, MatrixStack matrixStackIn, Object animatableArg, float ticks) {
        if (!(animatableArg instanceof IAnimatable)) return;
        IAnimatable animatable = (IAnimatable) animatableArg;
        Map<String, AnimationController> controllerMap = animatable.getFactory().getOrCreateAnimationData(renderer.getUniqueID(animatableArg)).getAnimationControllers();
        for (AnimationController controller : controllerMap.values()) {
            List<BedrockEmitter> emitters = ((IAdvController)controller).getEmitters();
            for (BedrockEmitter emitter : emitters) {
                String locator = emitter.locator + "_locator";//emitter.locator+"_locator";
                if (emitter.locator != null && model.getBone(locator).isPresent()) {
                    GeoBone bone = model.getBone(locator).get();
                    renderParticle(emitter, matrixStackIn, bone, ticks);
                }
            }
        }
    }

    public static void renderParticle(BedrockEmitter emitter, MatrixStack matrixStackIn, GeoBone locator, float ticks) {
        emitter.prevGlobal.x = emitter.lastGlobal.x;
        emitter.prevGlobal.y = emitter.lastGlobal.y;
        emitter.prevGlobal.z = emitter.lastGlobal.z;


        Vector3d position = PositionUtils.getCurrentRenderPos(matrixStackIn);
        //Vector3d position = new Vector3d(0,0,0);
        double posX = position.x;//-376.5;
        double posY = position.y;//8;
        double posZ = position.z;//569.5;

        emitter.lastGlobal.x = posX; //TODO
        emitter.lastGlobal.y = posY; //TODO
        emitter.lastGlobal.z = posZ; //TODO

        GL11.glPushMatrix();
        //GL11.glTranslated(0,-2,0);

        Matrix4f curRot = PositionUtils.getCurrentMatrix(matrixStackIn);

        PositionUtils.setInitialWorldPos();

        Matrix4f cur2 = PositionUtils.getCurrentRotation(curRot, PositionUtils.getCurrentMatrix(matrixStackIn));

        emitter.rotation.setIdentity();

        matrixStackIn.pushPose();
        matrixStackIn.last().pose().multiply(new net.minecraft.util.math.vector.Matrix4f(new float[]{cur2.m00,cur2.m01,cur2.m02,0, cur2.m10,cur2.m11,cur2.m12,0,cur2.m20,cur2.m21,cur2.m22,0,0,0,0,1}));
        GeoBone[] bonePath = getPathFromRoot(locator);

        MatrixStack geoStack = new MatrixStack();
        for (int i = 0; i < bonePath.length; i++) {
            GeoBone bone = bonePath[i];
            RenderUtils.translate(bone, geoStack);
            RenderUtils.moveToPivot(bone, geoStack);
            RenderUtils.rotate(bone, geoStack);
            RenderUtils.scale(bone, geoStack);
            RenderUtils.moveBackFromPivot(bone, geoStack);
        }
        RenderUtils.moveToPivot(locator, geoStack);
        //MATRIX_STACK.translate(6f/16f,16f/16f,0);
        //MATRIX_STACK.rotateX((float) (Math.PI/2));
        //MATRIX_STACK.scale(0.5f,0.5f,0.5f);

        IMatrix4f full = (IMatrix4f)(Object)geoStack.last().pose();
        emitter.rotation = new Matrix3f(full.m00(),full.m01(),full.m02(),full.m10(),full.m11(),full.m12(),full.m20(),full.m21(),full.m22());
        emitter.lastGlobal.x+=full.m03();
        emitter.lastGlobal.y+=full.m13();
        emitter.lastGlobal.z+=full.m23();

        matrixStackIn.popPose();
        emitter.render(Minecraft.getInstance().getFrameTime());
        GL11.glPopMatrix();
    }

    public static GeoBone[] getPathFromRoot(GeoBone bone) {
        ArrayList<GeoBone> bones = new ArrayList<>();
        while (bone != null) {
            bones.add(0, bone);
            bone = bone.parent;
        }
        return bones.toArray(new GeoBone[0]);
    }
}
