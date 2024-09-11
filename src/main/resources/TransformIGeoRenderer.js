function initializeCoreMod() {
    return {
        "AddParticlesToRenderer": {
	        "target": {
		        "type": "METHOD",
		        "class": "software.bernie.geckolib3.renderers.geo.IGeoRenderer",
		        "methodName": "render",
		        "methodDesc": "(Lsoftware/bernie/geckolib3/geo/render/built/GeoModel;Ljava/lang/Object;FLnet/minecraft/client/renderer/RenderType;Lcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;Lcom/mojang/blaze3d/vertex/IVertexBuilder;IIFFFF)V"
	        },
	        "transformer": function(methodNode) {
	            var Opcodes = Java.type("org.objectweb.asm.Opcodes");
	            var ASMAPI = Java.type('net.minecraftforge.coremod.api.ASMAPI');
	            var VarInsnNode = Java.type("org.objectweb.asm.tree.VarInsnNode");
	            var nodes = methodNode.instructions.toArray();
                for(var i=0;i<nodes.length;i++){
                    var curNode = nodes[i];
                    if(curNode.getOpcode()==Opcodes.INVOKEINTERFACE && curNode.name.equals("setCurrentModelRenderCycle")){
                        var list =  ASMAPI.listOf(
                            new VarInsnNode(Opcodes.ALOAD, 0),
                            new VarInsnNode(Opcodes.ALOAD, 1),
                            new VarInsnNode(Opcodes.ALOAD, 5),
                            new VarInsnNode(Opcodes.ALOAD, 2),
                            new VarInsnNode(Opcodes.FLOAD, 3),
                            ASMAPI.buildMethodCall("com/goodbird/salamanderlib/helper/ParticleDrawingHelper", "drawParticles", "(Lsoftware/bernie/geckolib3/renderers/geo/IGeoRenderer;Lsoftware/bernie/geckolib3/geo/render/built/GeoModel;Lcom/mojang/blaze3d/matrix/MatrixStack;Ljava/lang/Object;F)V", ASMAPI.MethodType.STATIC)
                        )
                        methodNode.instructions.insert(curNode, list);
                        break;
                    }
                }
		        print("GeoRendererTransformer successfully transformed!");
		        return methodNode;
	        }
        }
    }
}