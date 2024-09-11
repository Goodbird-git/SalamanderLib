package com.goodbird.salamanderlib.util;


import com.goodbird.salamanderlib.mclib.utils.MatrixUtils;
import com.goodbird.salamanderlib.mixin.impl.IMatrix4f;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.settings.PointOfView;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;
import software.bernie.shadowed.eliotlash.mclib.math.functions.limit.Min;

import javax.vecmath.Matrix3f;
import javax.vecmath.Matrix4d;
import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3d;

public class PositionUtils {
    public static void setInitialWorldPos() {
        Entity camera = Minecraft.getInstance().cameraEntity;
        GL11.glLoadIdentity();
        if (Minecraft.getInstance().options.getCameraType() == PointOfView.FIRST_PERSON) {
            GL11.glScaled(-1, 1, -1);
        } else if (Minecraft.getInstance().options.getCameraType() == PointOfView.THIRD_PERSON_BACK){ //1
            GL11.glScaled(-1, 1, -1);
        } else {
            GL11.glScaled(1, 1, 1);
        }
        GL11.glRotatef(-camera.xRot, 1, 0, 0);
        GL11.glRotatef(camera.yRot, 0, 1, 0);
        double renderPosX = camera.xOld + (camera.getX() - camera.xOld) * (double) Minecraft.getInstance().getFrameTime();
        double renderPosY = camera.yOld + (camera.getY() - camera.yOld) * (double) Minecraft.getInstance().getFrameTime();
        double renderPosZ = camera.zOld + (camera.getZ() - camera.zOld) * (double) Minecraft.getInstance().getFrameTime();
        GL11.glTranslated(-renderPosX, -renderPosY, -renderPosZ);
        Vector3d additional = getCameraShift();
        GL11.glTranslated(additional.x,additional.y,additional.z);
    }

    public static Vector3d getCameraShift(){
        Vector3d res = new Vector3d(0,0,0);
        if (Minecraft.getInstance().options.getCameraType() == PointOfView.FIRST_PERSON) {
            return res;
        } else if (Minecraft.getInstance().options.getCameraType() == PointOfView.THIRD_PERSON_BACK){ //1
            net.minecraft.util.math.vector.Vector3d look = Minecraft.getInstance().player.getLookAngle();
            res = new Vector3d(look.x,look.y,look.z);
            res.scale(4);
            return res;
        } else {
            net.minecraft.util.math.vector.Vector3d look = Minecraft.getInstance().player.getLookAngle();
            res = new Vector3d(look.x,look.y,look.z);
            res.scale(-4);
            return res;
        }
    }

    public static Vector3d getCurrentRenderPos(MatrixStack matrixStackIn) {
        Entity camera = Minecraft.getInstance().cameraEntity;
        Matrix4f matrix4f = getCurrentMatrix(matrixStackIn);
        MatrixUtils.Transformation transformation = MatrixUtils.extractTransformations(null, matrix4f);
        double dl = matrix4f.m03;
        double du = matrix4f.m13;
        double dz = matrix4f.m23;
        if(Minecraft.getInstance().options.getCameraType()==PointOfView.THIRD_PERSON_BACK){ //1
            dz+=4;
        }
        if(Minecraft.getInstance().options.getCameraType()==PointOfView.THIRD_PERSON_FRONT){ //2
            dz*=-1;
            dl*=-1;
            dz-=4;
        }
        Matrix4d rotMatrixX = new Matrix4d();
        rotMatrixX.rotX((camera.xRot) / 360 * Math.PI * 2);
        Matrix4d rotMatrixY = new Matrix4d();
        rotMatrixY.rotY((-camera.yRot) / 360 * Math.PI * 2);
        Vector3d vecZ = new Vector3d(0, 0, 1);
        rotMatrixX.transform(vecZ);
        rotMatrixY.transform(vecZ);
        vecZ.scale(-1);

        Vector3d vecL = new Vector3d(1, 0, 0);
        rotMatrixX.transform(vecL);
        rotMatrixY.transform(vecL);
        vecL.scale(-1);

        Vector3d vecU = new Vector3d(0, 1, 0);
        rotMatrixX.transform(vecU);
        rotMatrixY.transform(vecU);

        vecZ.scale(dz);
        vecU.scale(du);
        vecL.scale(dl);
        Vector3d pos = new Vector3d(vecZ.x + vecU.x + vecL.x, vecZ.y + vecU.y + vecL.y, vecZ.z + vecU.z + vecL.z);
        double renderPosX = camera.xOld + (camera.getX() - camera.xOld) * (double) Minecraft.getInstance().getFrameTime();
        double renderPosY = camera.yOld + (camera.getY() - camera.yOld) * (double) Minecraft.getInstance().getFrameTime();
        double renderPosZ = camera.zOld + (camera.getZ() - camera.zOld) * (double) Minecraft.getInstance().getFrameTime();
        Vector3d res = new Vector3d(pos.x + renderPosX, pos.y + renderPosY+1.6, pos.z + renderPosZ);
        return res;
    }
    //MatrixStack matrixStackIn

    public static Matrix4f getCurrentMatrix(MatrixStack matrixStackIn) {
        MatrixUtils.matrix = null;
        IMatrix4f cur = (IMatrix4f)(Object) matrixStackIn.last().pose();
        MatrixUtils.matrix = new Matrix4f(cur.m00(), cur.m01(), cur.m02(), cur.m03(), cur.m10(), cur.m11(), cur.m12(), cur.m13(), cur.m20(), cur.m21(), cur.m22(), cur.m23(), cur.m30(), cur.m31(), cur.m32(), cur.m33());
        return MatrixUtils.matrix;
    }

//    public static Matrix4f getCurrentMatrix() {
//        MatrixUtils.matrix = null;
//        MatrixUtils.captureMatrix();
//        return MatrixUtils.matrix;
//    }

    public static Matrix4f getBasicRotation() {
        Entity camera = Minecraft.getInstance().cameraEntity;
        Matrix4f basicRot = new Matrix4f();
        basicRot.rotX((float) (camera.xRot / 360 * Math.PI / 2));
        Matrix4f yRot = new Matrix4f();
        yRot.rotY((float) (camera.yRot / 360 * Math.PI / 2));
        basicRot.mul(yRot);
        return basicRot;
    }

    public static void changeToRot(Matrix4f current, Matrix4f rot) {
        current.m00 = rot.m00;
        current.m01 = rot.m01;
        current.m02 = rot.m02;
        current.m10 = rot.m10;
        current.m11 = rot.m11;
        current.m12 = rot.m12;
        current.m20 = rot.m20;
        current.m21 = rot.m21;
        current.m22 = rot.m22;
    }

    public static Matrix3f getRotBlock(Matrix4f rot) {
        return new Matrix3f(rot.m00, rot.m01, rot.m02, rot.m10, rot.m11, rot.m12, rot.m20, rot.m21, rot.m22);
    }

    public static Matrix4f getCurrentRotation(Matrix4f old, Matrix4f base) {
        base.invert();
        Matrix4f rot = new Matrix4f(old.m00, old.m01, old.m02, 0, old.m10, old.m11, old.m12, 0, old.m20, old.m21, old.m22, 0, 0, 0, 0, old.m33);
        base.mul(rot);

        return base;
    }
}
