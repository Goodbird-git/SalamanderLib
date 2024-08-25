package com.goodbird.salamanderlib.util;

import java.io.File;
import java.io.FileOutputStream;

public class AsmUtils {
    public static void writeClazzToFile(byte[] bytes, String name) {
        try {
            File file = new File(".\\" , name + ".class");
            file.createNewFile();
            file.setWritable(true);
            FileOutputStream stream = new FileOutputStream(file);
            stream.write(bytes);
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
