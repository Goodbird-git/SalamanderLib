package com.goodbird.salamanderlib.asm;

import com.goodbird.salamanderlib.particles.BedrockLibrary;
import com.goodbird.salamanderlib.util.ObfuscationUtils;
import net.minecraft.client.resources.*;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.FMLFolderResourcePack;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import software.bernie.geckolib3.GeckoLib;

import java.io.File;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Predicate;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static com.goodbird.salamanderlib.util.AsmUtils.writeClazzToFile;
import static org.objectweb.asm.Opcodes.*;

public class GeckolibCacheHook implements IClassTransformer {

    @Override
    public byte[] transform(String name, String transformedName, byte[] bytes) {
        if (!transformedName.equals("software.bernie.geckolib3.resource.GeckoLibCache")) {
            return bytes;
        }
        ClassReader classReader = new ClassReader(bytes);
        ClassNode classNode = new ClassNode();
        classReader.accept(classNode,0);

        for(MethodNode node: classNode.methods){
            if(node.name.equals("onResourceManagerReload")||node.name.equals("func_110549_a")){
                changeResourceManagerReload(node);
            }
        }

        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES|ClassWriter.COMPUTE_MAXS);
        classNode.accept(classWriter);

        bytes = classWriter.toByteArray();
        return bytes;
    }

    public void changeResourceManagerReload(MethodNode method){
        AbstractInsnNode[] nodes = method.instructions.toArray();
        for(int i=0;i<nodes.length-1;i++){
            AbstractInsnNode curNode = nodes[i];
            AbstractInsnNode nextNode = nodes[i+1];
            if(curNode.getOpcode()==CHECKCAST && ((TypeInsnNode)curNode).desc.equals("net/minecraft/client/resources/IResourcePack") && nextNode.getOpcode()==ASTORE){
                InsnList list = new InsnList();
                list.add(new VarInsnNode(Opcodes.ALOAD, ((VarInsnNode)nextNode).var));
                list.add(new MethodInsnNode(INVOKESTATIC, "com/goodbird/salamanderlib/asm/methods/GeckolibCacheMethods", "onRMReload", "(Lnet/minecraft/client/resources/IResourcePack;)V", false));
                method.instructions.insert(nextNode, list);
                break;
            }
        }
        for(int i=0;i<nodes.length;i++){
            AbstractInsnNode curNode = nodes[i];
            if(curNode.getOpcode()==PUTFIELD && ((FieldInsnNode)curNode).name.equals("geoModels")){
                InsnList list = new InsnList();
                list.add(new MethodInsnNode(INVOKESTATIC, "com/goodbird/salamanderlib/asm/methods/GeckolibCacheMethods", "reloadParticleLib", "()V", false));
                method.instructions.insert(curNode, list);
                break;
            }
        }
    }
    
}
