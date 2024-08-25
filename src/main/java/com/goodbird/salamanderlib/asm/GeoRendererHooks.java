package com.goodbird.salamanderlib.asm;

import com.goodbird.salamanderlib.particles.emitter.BedrockEmitter;
import com.goodbird.salamanderlib.util.PositionUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.launchwrapper.IClassTransformer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoModel;

import javax.vecmath.Matrix3f;
import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3d;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Map;

import static org.objectweb.asm.Opcodes.*;
import static software.bernie.geckolib3.renderers.geo.IGeoRenderer.MATRIX_STACK;

public class GeoRendererHooks  implements IClassTransformer {

    @Override
    public byte[] transform(String name, String transformedName, byte[] bytes) {
        if (!transformedName.equals("software.bernie.geckolib3.renderers.geo.IGeoRenderer")) {
            return bytes;
        }
        //writeClazzToFile(bytes,transformedName);
        ClassReader classReader = new ClassReader(bytes);
        ClassNode classNode = new ClassNode();
        classReader.accept(classNode, 0);

        for (MethodNode node : classNode.methods) {
            if (node.name.equals("render")) {
                changeRender(node);
                fixOpaqueTextures(node);
            }
        }

        ClassWriter classWriter = new ClassWriter(0);
        classNode.accept(classWriter);

        bytes = classWriter.toByteArray();
        //writeClazzToFile(bytes, transformedName + "_after");
        return bytes;
    }
    public void fixOpaqueTextures(MethodNode method){
        AbstractInsnNode[] nodes = method.instructions.toArray();
        for(int i=0;i<nodes.length;i++){
            AbstractInsnNode curNode = nodes[i];
            if(curNode.getOpcode()==INVOKESTATIC && ((MethodInsnNode)curNode).name.equals("disableCull")){
                method.instructions.insert(curNode,new MethodInsnNode(INVOKESTATIC, "com/goodbird/salamanderlib/asm/methods/GeoRendererMethods", "enableBlend", "()V", false));
            }
            if(curNode.getOpcode()==INVOKESTATIC && ((MethodInsnNode)curNode).name.equals("enableCull")){
                method.instructions.insert(curNode,new MethodInsnNode(INVOKESTATIC, "com/goodbird/salamanderlib/asm/methods/GeoRendererMethods", "disableBlend", "()V", false));
            }
        }
    }

    public void changeRender(MethodNode method){
        AbstractInsnNode[] nodes = method.instructions.toArray();
        for(int i=0;i<nodes.length;i++){
            AbstractInsnNode curNode = nodes[i];
            if(curNode.getOpcode()==INVOKEINTERFACE && ((MethodInsnNode)curNode).name.equals("renderAfter")){
                InsnList list = new InsnList();
                list.add(new VarInsnNode(Opcodes.ALOAD, 1));
                list.add(new VarInsnNode(Opcodes.ALOAD, 2));
                list.add(new VarInsnNode(Opcodes.FLOAD, 3));
                list.add(new MethodInsnNode(INVOKESTATIC, "com/goodbird/salamanderlib/asm/methods/GeoRendererMethods", "drawParticles", "(Lsoftware/bernie/geckolib3/geo/render/built/GeoModel;Ljava/lang/Object;F)V", false));
                method.instructions.insert(curNode, list);
                break;
            }
        }
    }
}
