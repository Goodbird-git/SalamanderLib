package com.goodbird.salamanderlib.hooklib.geckohooks;

import com.goodbird.salamanderlib.hooklib.minecraft.HookLibPlugin;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ListIterator;

import static org.objectweb.asm.Opcodes.*;

public class GeoBlockRendererFix  implements IClassTransformer {

    private static final String unobfLightmapTexUnit = "lightmapTexUnit";
    private static final String obfLightmapTexUnit = "field_77476_b";

    @Override
    public byte[] transform(String name, String transformedName, byte[] bytes) {
        if (!transformedName.equals("software.bernie.geckolib3.renderers.geo.GeoBlockRenderer")) {
            return bytes;
        }
        ClassReader classReader = new ClassReader(bytes);
        ClassNode classNode = new ClassNode();
        classReader.accept(classNode,0);

        for(MethodNode node: classNode.methods){
            if(node.name.equals("render")){
                changeFindModDirMods(node);
            }
        }

        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES|ClassWriter.COMPUTE_MAXS);
        classNode.accept(classWriter);

        bytes = classWriter.toByteArray();
        return bytes;
    }

    public void changeFindModDirMods(MethodNode method){
        ListIterator<AbstractInsnNode> iterator = method.instructions.iterator();
        while (iterator.hasNext()) {
            AbstractInsnNode insn = iterator.next();
            if(insn.getOpcode()==SIPUSH){
                String lightmapTexUnit = HookLibPlugin.getObfuscated() ?  obfLightmapTexUnit : unobfLightmapTexUnit;
                iterator.set(new FieldInsnNode(GETSTATIC, "net/minecraft/client/renderer/OpenGlHelper", lightmapTexUnit, "I"));
            }
        }
    }
}
