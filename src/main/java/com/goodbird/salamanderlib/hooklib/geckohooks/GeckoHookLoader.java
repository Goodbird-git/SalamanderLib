package com.goodbird.salamanderlib.hooklib.geckohooks;

import com.goodbird.salamanderlib.hooklib.minecraft.HookLoader;
import com.goodbird.salamanderlib.hooklib.minecraft.PrimaryClassTransformer;

public class GeckoHookLoader extends HookLoader {

    @Override
    public String[] getASMTransformerClass() {
        return new String[]{PrimaryClassTransformer.class.getName(), GeoBlockRendererFix.class.getName()};
    }

    @Override
    public void registerHooks() {
        registerHookContainer("com.goodbird.salamanderlib.hooklib.geckohooks.AnnotationHooks");
    }
}
