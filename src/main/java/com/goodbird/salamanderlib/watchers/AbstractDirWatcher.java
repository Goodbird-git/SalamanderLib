package com.goodbird.salamanderlib.watchers;

import java.io.File;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;

import static java.nio.file.StandardWatchEventKinds.*;

public abstract class AbstractDirWatcher extends Thread {
    private WatchService watcher;
    private final Map<WatchKey, Path> keys = new HashMap<WatchKey, Path>();
    private boolean recursive;

    private void register(Path dir) {
        try {
            WatchKey key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
            keys.put(key, dir);
        } catch (Exception e) {

        }
    }

    private void registerAll(final Path start) {
        try {
            Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                    register(dir);
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (Exception e) {

        }
    }

    public AbstractDirWatcher(Path dir, boolean recursive) {
        try {
            this.watcher = FileSystems.getDefault().newWatchService();
            this.recursive = recursive;
            if (recursive) {
                registerAll(dir);
            } else {
                register(dir);
            }
        } catch (Exception ignored) {
        }
    }

    @Override
    public void run() {
        try {
            WatchKey key;
            while ((key = watcher.take()) != null) {
                processKey(key);
                key.reset();

                boolean valid = key.reset();
                if (!valid) {
                    keys.remove(key);
                    if (keys.isEmpty()) {
                        break;
                    }
                }
            }
        } catch (Exception ignored) {
            System.out.println("COLLAPSED");
        }
    }

    public void processKey(WatchKey key) {
        for (WatchEvent event : key.pollEvents()) {
            String name = ((Path) event.context()).getFileName().toString();
            Path dir = keys.get(key);
            Path child = dir.resolve(name);
            if (recursive && (event.kind() == ENTRY_CREATE)) {
                if (Files.isDirectory(child, LinkOption.NOFOLLOW_LINKS)) {
                    registerAll(child);
                }
            }
            if (event.kind() == ENTRY_CREATE) {
                processCreate(child.toFile());
            }
            if (event.kind() == ENTRY_DELETE) {
                processDelete(child.toFile());
            }
            if (event.kind() == ENTRY_MODIFY) {
                processModify(child.toFile());
            }
        }
    }

    public abstract void processCreate(File path);

    public abstract void processDelete(File path);

    public abstract void processModify(File path);

}
