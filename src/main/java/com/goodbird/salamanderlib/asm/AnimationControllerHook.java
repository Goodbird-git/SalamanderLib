package com.goodbird.salamanderlib.asm;

import com.goodbird.salamanderlib.particles.BedrockLibrary;
import com.goodbird.salamanderlib.particles.emitter.BedrockEmitter;
import net.minecraft.client.Minecraft;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.ParticleKeyFrameEvent;

import java.util.ArrayList;
import java.util.ListIterator;

import static org.objectweb.asm.Opcodes.*;

public class AnimationControllerHook implements IClassTransformer {

    @Override
    public byte[] transform(String name, String transformedName, byte[] bytes) {
        if (!transformedName.equals("software.bernie.geckolib3.core.controller.AnimationController")) {
            return bytes;
        }
        //writeClazzToFile(bytes,transformedName);
        ClassReader classReader = new ClassReader(bytes);
        ClassNode classNode = new ClassNode();
        classReader.accept(classNode, 0);
        classNode.interfaces.add("com/goodbird/salamanderlib/asm/IAdvController");

        addEmitterFieldAndGetter(classNode);
        addLastTickFieldAndGetter(classNode);

        for (MethodNode node : classNode.methods) {
            if (node.name.equals("<init>")) {
                addInitializationToConstructor(classNode, node);
            }
            if (node.name.equals("process")) {
                changeProcess(node);
            }
            if (node.name.equals("processCurrentAnimation")) {
                changeProcessCurrentAnim(node);
            }
        }

        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        classNode.accept(classWriter);

        bytes = classWriter.toByteArray();
        //writeClazzToFile(bytes, transformedName + "_after");
        return bytes;
    }

    public void addEmitterFieldAndGetter(ClassNode classNode) {
        //Type listType = Type.getType("Ljava/util/ArrayList<Lcom/goodbird/salamanderlib/particles/emitter/BedrockEmitter;>;");
        FieldNode fieldNode = new FieldNode(Opcodes.ACC_PUBLIC, "emitters", "Ljava/util/ArrayList;", "Ljava/util/ArrayList<Lcom/goodbird/salamanderlib/particles/emitter/BedrockEmitter;>;", null);
        classNode.fields.add(fieldNode);

        MethodNode getterMethod = new MethodNode(Opcodes.ACC_PUBLIC, "getEmitters",
                "()Ljava/util/ArrayList;", "()Ljava/util/ArrayList<Lcom/goodbird/salamanderlib/particles/emitter/BedrockEmitter;>;", null);
        InsnList getterInstructions = new InsnList();
        getterInstructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
        getterInstructions.add(new FieldInsnNode(Opcodes.GETFIELD, classNode.name, "emitters", "Ljava/util/ArrayList;"));
        getterInstructions.add(new InsnNode(Opcodes.ARETURN));
        getterMethod.instructions = getterInstructions;
        classNode.methods.add(getterMethod);
    }

    public void addLastTickFieldAndGetter(ClassNode classNode) {
        FieldNode fieldNode = new FieldNode(Opcodes.ACC_PUBLIC, "lastTick", "J", null, null);
        classNode.fields.add(fieldNode);


        MethodNode getterMethod = new MethodNode(Opcodes.ACC_PUBLIC, "getLastTick",
                "()J", null, null);
        InsnList getterInstructions = new InsnList();
        getterInstructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
        getterInstructions.add(new FieldInsnNode(Opcodes.GETFIELD, classNode.name, "lastTick", "J"));
        getterInstructions.add(new InsnNode(Opcodes.LRETURN));
        getterMethod.instructions = getterInstructions;
        classNode.methods.add(getterMethod);

        MethodNode setterMethod = new MethodNode(Opcodes.ACC_PUBLIC, "setLastTick",
                "(J)V", null, null);
        InsnList setterInstructions = new InsnList();
        setterInstructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
        setterInstructions.add(new VarInsnNode(Opcodes.LLOAD, 1));
        setterInstructions.add(new FieldInsnNode(Opcodes.PUTFIELD, classNode.name, "lastTick", "J"));
        setterInstructions.add(new InsnNode(Opcodes.RETURN));
        setterMethod.instructions = setterInstructions;
        classNode.methods.add(setterMethod);
    }

    public void changeProcess(MethodNode method){
        AbstractInsnNode[] nodes = method.instructions.toArray();
        for(int i=0;i<nodes.length-1;i++){
            AbstractInsnNode curNode = nodes[i];
            AbstractInsnNode nextNode = nodes[i+1];
            if(curNode.getOpcode()==INVOKEVIRTUAL && ((MethodInsnNode)curNode).name.equals("adjustTick")){
                InsnList list = new InsnList();
                list.add(new VarInsnNode(Opcodes.ALOAD, 0));
                list.add(new MethodInsnNode(INVOKESTATIC, "com/goodbird/salamanderlib/asm/methods/AnimationControllerMethods", "updateEmitters", "(Lsoftware/bernie/geckolib3/core/controller/AnimationController;)V", false));
                method.instructions.insert(nextNode, list);
                break;
            }
        }

        for(int i=0;i<nodes.length;i++){
            AbstractInsnNode curNode = nodes[i];
            AbstractInsnNode nextNode = nodes[i+1];
            if(curNode.getOpcode()==INVOKESPECIAL && ((MethodInsnNode)curNode).name.equals("resetEventKeyFrames")){
                InsnList list = new InsnList();
                list.add(new VarInsnNode(Opcodes.ALOAD, 0));
                list.add(new MethodInsnNode(INVOKESTATIC, "com/goodbird/salamanderlib/asm/methods/AnimationControllerMethods", "setAllStopping", "(Lsoftware/bernie/geckolib3/core/controller/AnimationController;)V", false));
                method.instructions.insert(nextNode, list);
                break;
            }
        }
    }

    public void changeProcessCurrentAnim(MethodNode method){
        AbstractInsnNode[] nodes = method.instructions.toArray();
        for(int i=0;i<nodes.length-1;i++){
            AbstractInsnNode curNode = nodes[i];
            AbstractInsnNode nextNode = nodes[i+1];
            if(curNode.getOpcode()==INVOKEINTERFACE && ((MethodInsnNode)curNode).name.equals("isRepeatingAfterEnd")){
                InsnList list = new InsnList();
                list.add(new VarInsnNode(Opcodes.ALOAD, 0));
                list.add(new MethodInsnNode(INVOKESTATIC, "com/goodbird/salamanderlib/asm/methods/AnimationControllerMethods", "setAllStopping", "(Lsoftware/bernie/geckolib3/core/controller/AnimationController;)V", false));
                method.instructions.insert(nextNode, list);
                break;
            }
        }
        for(int i=0;i<nodes.length;i++){
            AbstractInsnNode curNode = nodes[i];
            if(curNode.getOpcode()==INVOKESPECIAL && ((MethodInsnNode)curNode).name.equals("setAnimTime")){
                InsnList list = new InsnList();
                list.add(new VarInsnNode(Opcodes.ALOAD, 0));
                list.add(new MethodInsnNode(INVOKESTATIC, "com/goodbird/salamanderlib/asm/methods/AnimationControllerMethods", "removeAllStopped", "(Lsoftware/bernie/geckolib3/core/controller/AnimationController;)V", false));
                method.instructions.insert(curNode, list);
                break;
            }
        }
        for(int i=0;i<nodes.length-4;i++){
            AbstractInsnNode curNode = nodes[i];
            AbstractInsnNode fourthNode = nodes[i+3];
            if(curNode.getOpcode()==INVOKEVIRTUAL && ((MethodInsnNode)curNode).name.equals("getStartTick")
              && ((MethodInsnNode)curNode).owner.equals("software/bernie/geckolib3/core/keyframe/ParticleEventKeyFrame")
              && fourthNode.getOpcode()==IFLT
            ){
                LabelNode label = ((JumpInsnNode) fourthNode).label;

                InsnList list = new InsnList();
                list.add(new VarInsnNode(Opcodes.ALOAD, 0));
                list.add(new VarInsnNode(Opcodes.ALOAD, 9));
                list.add(new FieldInsnNode(GETFIELD,"software/bernie/geckolib3/core/keyframe/ParticleEventKeyFrame","effect","Ljava/lang/String;"));
                list.add(new VarInsnNode(Opcodes.ALOAD, 9));
                list.add(new FieldInsnNode(GETFIELD,"software/bernie/geckolib3/core/keyframe/ParticleEventKeyFrame","locator","Ljava/lang/String;"));
                list.add(new MethodInsnNode(INVOKESTATIC, "com/goodbird/salamanderlib/asm/methods/AnimationControllerMethods", "hasParticleOnLoc", "(Lsoftware/bernie/geckolib3/core/controller/AnimationController;Ljava/lang/String;Ljava/lang/String;)Z", false));
                list.add(new JumpInsnNode(IFNE,label));
                method.instructions.insert(fourthNode, list);
                break;
            }
        }

        for(int i=0;i<nodes.length-1;i++){
            AbstractInsnNode curNode = nodes[i];
            AbstractInsnNode nextNode = nodes[i+1];
            if(curNode.getOpcode()==INVOKESPECIAL && ((MethodInsnNode)curNode).name.equals("<init>")
                && ((MethodInsnNode)curNode).owner.equals("software/bernie/geckolib3/core/event/ParticleKeyFrameEvent")
                && nextNode.getOpcode()==ASTORE
            ){
                InsnList list = new InsnList();
                list.add(new VarInsnNode(Opcodes.ALOAD, 0));
                list.add(new VarInsnNode(Opcodes.ALOAD, 10));
                list.add(new MethodInsnNode(INVOKESTATIC, "com/goodbird/salamanderlib/asm/methods/AnimationControllerMethods", "processBedrockParticleEvent", "(Lsoftware/bernie/geckolib3/core/controller/AnimationController;Lsoftware/bernie/geckolib3/core/event/ParticleKeyFrameEvent;)V", false));
                method.instructions.insert(nextNode, list);
                break;
            }
        }

        for(int i=0;i<nodes.length-1;i++){
            AbstractInsnNode curNode = nodes[i];
            AbstractInsnNode nextNode = nodes[i+1];
            if(curNode.getOpcode()==GETFIELD && ((FieldInsnNode)curNode).name.equals("soundListener") && nextNode.getOpcode()==IFNONNULL){
                InsnList list = new InsnList();
                list.add(new VarInsnNode(Opcodes.ALOAD, 0));
                list.add(new FieldInsnNode(GETFIELD,"software/bernie/geckolib3/core/controller/AnimationController","soundListener","Lsoftware/bernie/geckolib3/core/controller/AnimationController$ISoundListener;"));
                list.add(new JumpInsnNode(IFNULL, ((JumpInsnNode)nextNode).label));
                method.instructions.insert(nextNode, list);
                break;
            }
        }

        for(int i=0;i<nodes.length-1;i++){
            AbstractInsnNode curNode = nodes[i];
            AbstractInsnNode nextNode = nodes[i+1];
            if(curNode.getOpcode()==INVOKEINTERFACE && ((MethodInsnNode)curNode).name.equals("summonParticle")){
                LabelNode labelNode = (LabelNode) nextNode;
                InsnList list = new InsnList();
                list.add(new VarInsnNode(Opcodes.ALOAD, 0));
                list.add(new FieldInsnNode(GETFIELD,"software/bernie/geckolib3/core/controller/AnimationController","particleListener","Lsoftware/bernie/geckolib3/core/controller/AnimationController$IParticleListener;"));
                list.add(new JumpInsnNode(IFNULL, labelNode));
                method.instructions.insert(nodes[i-4], list);
                break;
            }
        }
    }
    public void addInitializationToConstructor(ClassNode classNode, MethodNode method) {
        InsnList instructions = method.instructions;

        ListIterator<AbstractInsnNode> iterator = method.instructions.iterator();
        while (iterator.hasNext()) {
            AbstractInsnNode insn = iterator.next();
            if(insn.getOpcode()==RETURN){
                InsnList list = new InsnList();
                list.add(new VarInsnNode(Opcodes.ALOAD, 0));
                list.add(new TypeInsnNode(Opcodes.NEW, "java/util/ArrayList"));
                list.add(new InsnNode(Opcodes.DUP));
                list.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "java/util/ArrayList", "<init>", "()V", false));
                list.add(new FieldInsnNode(Opcodes.PUTFIELD, classNode.name, "emitters", "Ljava/util/ArrayList;"));

                list.add(new VarInsnNode(Opcodes.ALOAD, 0));
                list.add(new LdcInsnNode(0L));
                list.add(new FieldInsnNode(Opcodes.PUTFIELD, classNode.name, "lastTick", "J"));

                instructions.insertBefore(insn, list);
            }
        }
    }


    
}

