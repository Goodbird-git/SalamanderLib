package com.goodbird.salamanderlib.util;

import net.minecraftforge.fml.relauncher.CoreModManager;
import net.minecraftforge.fml.relauncher.FMLRelaunchLog;

import java.lang.reflect.Field;

public class ObfuscationUtils {
    private static boolean obf;
    private static boolean checked;
    public static boolean getObfuscated() {
        if (!checked) {
            try {
                Field deobfField = CoreModManager.class.getDeclaredField("deobfuscatedEnvironment");
                deobfField.setAccessible(true);
                obf = !deobfField.getBoolean(null);
                FMLRelaunchLog.info("[HOOKLIB] " + " Obfuscated: " + obf);
            } catch (Exception e) {
                e.printStackTrace();
            }
            checked = true;
        }
        return obf;
    }
}
