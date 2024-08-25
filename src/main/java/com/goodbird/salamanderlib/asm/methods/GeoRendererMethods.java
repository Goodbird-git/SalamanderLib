package com.goodbird.salamanderlib.asm.methods;

import com.goodbird.salamanderlib.asm.IAdvController;
import com.goodbird.salamanderlib.particles.emitter.BedrockEmitter;
import com.goodbird.salamanderlib.util.PositionUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoModel;

import javax.vecmath.Matrix3f;
import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3d;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Map;

import static software.bernie.geckolib3.renderers.geo.IGeoRenderer.MATRIX_STACK;

public class GeoRendererMethods {
    public static void drawParticles(GeoModel model, Object animatableArg, float ticks) {
        if (!(animatableArg instanceof IAnimatable)) return;
        IAnimatable animatable = (IAnimatable) animatableArg;
        Map<String, AnimationController> controllerMap = animatable.getFactory().getOrCreateAnimationData(animatableArg.hashCode()).getAnimationControllers();
        for (AnimationController controller : controllerMap.values()) {
            ArrayList<BedrockEmitter> emitters = ((IAdvController)controller).getEmitters();
            for (BedrockEmitter emitter : emitters) {
                String locator = emitter.locator + "_locator";//emitter.locator+"_locator";
                if (emitter.locator != null && model.getBone(locator).isPresent()) {
                    GeoBone bone = model.getBone(locator).get();
                    renderParticle(emitter, bone, ticks);
                }
            }
        }
    }

    public static void enableBlend(){
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.enableBlend();
    }

    public static void disableBlend(){
        GlStateManager.disableBlend();
    }

    public static void renderParticle(BedrockEmitter emitter, GeoBone locator, float ticks) {
        emitter.prevGlobal.x = emitter.lastGlobal.x;
        emitter.prevGlobal.y = emitter.lastGlobal.y;
        emitter.prevGlobal.z = emitter.lastGlobal.z;

        FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
        Vector3d position = PositionUtils.getCurrentRenderPos();
        //Vector3d position = new Vector3d(0,0,0);
        double posX = position.x;//-376.5;
        double posY = position.y;//8;
        double posZ = position.z;//569.5;

        emitter.lastGlobal.x = posX; //TODO
        emitter.lastGlobal.y = posY; //TODO
        emitter.lastGlobal.z = posZ; //TODO
        RenderHelper.disableStandardItemLighting();

        GL11.glPushMatrix();

        Matrix4f curRot = PositionUtils.getCurrentMatrix();

        PositionUtils.setInitialWorldPos();

        Matrix4f cur2 = PositionUtils.getCurrentRotation(curRot, PositionUtils.getCurrentMatrix());

        emitter.rotation.setIdentity();

        MATRIX_STACK.push();
        MATRIX_STACK.getModelMatrix().mul(new Matrix4f(cur2.m00,cur2.m01,cur2.m02,0, cur2.m10,cur2.m11,cur2.m12,0,cur2.m20,cur2.m21,cur2.m22,0,0,0,0,1));
        GeoBone[] bonePath = getPathFromRoot(locator);
        for (int i = 0; i < bonePath.length; i++) {
            GeoBone bone = bonePath[i];
            MATRIX_STACK.translate(bone);
            MATRIX_STACK.moveToPivot(bone);
            MATRIX_STACK.rotate(bone);
            MATRIX_STACK.scale(bone);
            MATRIX_STACK.moveBackFromPivot(bone);
        }
        MATRIX_STACK.moveToPivot(locator);
        //MATRIX_STACK.translate(6f/16f,16f/16f,0);
        //MATRIX_STACK.rotateX((float) (Math.PI/2));
        //MATRIX_STACK.scale(0.5f,0.5f,0.5f);

        Matrix4f full = MATRIX_STACK.getModelMatrix();
        emitter.rotation = new Matrix3f(full.m00, full.m01, full.m02, full.m10, full.m11, full.m12, full.m20, full.m21, full.m22);
        emitter.lastGlobal.x += full.m03;
        emitter.lastGlobal.y += full.m13;
        emitter.lastGlobal.z += full.m23;

        MATRIX_STACK.pop();
        emitter.render(Minecraft.getMinecraft().getRenderPartialTicks());
        RenderHelper.enableStandardItemLighting();
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
