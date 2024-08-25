package com.goodbird.salamanderlib.asm.methods;

import com.goodbird.salamanderlib.particles.BedrockLibrary;
import net.minecraft.client.resources.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.FMLFolderResourcePack;
import software.bernie.geckolib3.GeckoLib;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class GeckolibCacheMethods {
    public static void reloadParticleLib(){
        BedrockLibrary.instance.reload();
    }

    public static void onRMReload(IResourcePack pack){
        for (ResourceLocation location : getLocations(pack, "particles", fileName -> fileName.endsWith(".json"))) {
            try {
                BedrockLibrary.instance.storeFactory(location);
            } catch (Exception e) {
                e.printStackTrace();
                GeckoLib.LOGGER.error("Error loading model file \"" + location + "\"!", e);
            }
        }
    }

    private static List<ResourceLocation> getLocations(IResourcePack pack, String folder, Predicate<String> predicate) {
        if (pack instanceof LegacyV2Adapter) {
            LegacyV2Adapter adapter = (LegacyV2Adapter) pack;
            Field packField = null;

            for (Field field : adapter.getClass().getDeclaredFields()) {
                if (field.getType() == IResourcePack.class) {
                    packField = field;

                    break;
                }
            }

            if (packField != null) {
                packField.setAccessible(true);

                try {
                    return getLocations((IResourcePack) packField.get(adapter), folder, predicate);
                } catch (Exception e) {
                }
            }
        }

        List<ResourceLocation> locations = new ArrayList<ResourceLocation>();

        if (pack instanceof FolderResourcePack) {
            handleFolderResourcePack((FolderResourcePack) pack, folder, predicate, locations);
        } else if (pack instanceof FileResourcePack) {
            handleZipResourcePack((FileResourcePack) pack, folder, predicate, locations);
        }

        return locations;
    }

    /* Folder handling */

    private static void handleFolderResourcePack(FolderResourcePack folderPack, String folder, Predicate<String> predicate,
                                                 List<ResourceLocation> locations) {
        Field fileField = null;

        for (Field field : AbstractResourcePack.class.getDeclaredFields()) {
            if (field.getType() == File.class) {
                fileField = field;

                break;
            }
        }

        if (fileField != null) {
            fileField.setAccessible(true);

            try {
                File file = (File) fileField.get(folderPack);
                Set<String> domains = folderPack.getResourceDomains();

                if (folderPack instanceof FMLFolderResourcePack) {
                    domains.add(((FMLFolderResourcePack) folderPack).getFMLContainer().getModId());
                }

                for (String domain : domains) {
                    String prefix = "assets/" + domain + "/" + folder;
                    File pathFile = new File(file, prefix);

                    enumerateFiles(folderPack, pathFile, predicate, locations, domain, folder);
                }
            } catch (IllegalAccessException e) {
                GeckoLib.LOGGER.error(e);
            }
        }
    }

    private static void enumerateFiles(FolderResourcePack folderPack, File parent, Predicate<String> predicate,
                                       List<ResourceLocation> locations, String domain, String prefix) {
        File[] files = parent.listFiles();

        if (files == null) {
            return;
        }

        for (File file : files) {
            if (file.isFile() && predicate.test(file.getName())) {
                locations.add(new ResourceLocation(domain, prefix + "/" + file.getName()));
            } else if (file.isDirectory()) {
                enumerateFiles(folderPack, file, predicate, locations, domain, prefix + "/" + file.getName());
            }
        }
    }

    /* Zip handling */

    private static void handleZipResourcePack(FileResourcePack filePack, String folder, Predicate<String> predicate,
                                              List<ResourceLocation> locations) {
        Field zipField = null;

        for (Field field : FileResourcePack.class.getDeclaredFields()) {
            if (field.getType() == ZipFile.class) {
                zipField = field;

                break;
            }
        }

        if (zipField != null) {
            zipField.setAccessible(true);

            try {
                enumerateZipFile(filePack, folder, (ZipFile) zipField.get(filePack), predicate, locations);
            } catch (IllegalAccessException e) {
                GeckoLib.LOGGER.error(e);
            }
        }
    }

    private static void enumerateZipFile(FileResourcePack filePack, String folder, ZipFile file, Predicate<String> predicate,
                                         List<ResourceLocation> locations) {
        Set<String> domains = filePack.getResourceDomains();
        Enumeration<? extends ZipEntry> it = file.entries();

        while (it.hasMoreElements()) {
            String name = it.nextElement().getName();

            for (String domain : domains) {
                String assets = "assets/" + domain + "/";
                String path = assets + folder + "/";

                if (name.startsWith(path) && predicate.test(name)) {
                    locations.add(new ResourceLocation(domain, name.substring(assets.length())));
                }
            }
        }
    }
}
