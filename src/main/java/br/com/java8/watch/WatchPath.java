package br.com.java8.watch;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 *
 * @author Renato
 */
public class WatchPath {

    public static void main(String[] args) {
        final Path path = Paths.get("watch");
        watchFile(path);
    }

    private static void watchFile(final Path path) throws RuntimeException {
        try (final WatchService watchService = path.getFileSystem().newWatchService()) {
            path.register(watchService,
                    StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_MODIFY,
                    StandardWatchEventKinds.ENTRY_DELETE);

            while (true) {
                final WatchKey watchKey = watchService.poll(1, TimeUnit.SECONDS);

                if (watchKey != null) {
                    watchKey.pollEvents()
                            .stream()
                            .forEach(e -> System.out.println(e.context()));
                }
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
