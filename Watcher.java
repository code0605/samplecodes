import java.io.IOException;
import java.nio.file.*;

public class FolderWatcher {
    public static void main(String[] args) {
        try {
            String folderPath = "/path/to/your/shared/folder";
            WatchService watchService = FileSystems.getDefault().newWatchService();

            Path path = Paths.get(folderPath);
            path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);

            System.out.println("Watching folder: " + folderPath);

            while (true) {
                WatchKey key;
                try {
                    key = watchService.take();
                } catch (InterruptedException e) {
                    System.err.println("Error while waiting for events: " + e.getMessage());
                    return;
                }

                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();

                    if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                        Path filePath = (Path) event.context();
                        System.out.println("New file detected: " + filePath);

                        // Call your database job here
                        callDatabaseJob(filePath);
                    }
                }

                boolean valid = key.reset();
                if (!valid) {
                    System.err.println("WatchKey no longer valid, exiting.");
                    return;
                }
            }
        } catch (IOException e) {
            System.err.println("Error initializing folder watcher: " + e.getMessage());
        }
    }

    private static void callDatabaseJob(Path filePath) {
        // Implement the logic to call your database job here
        System.out.println("Calling database job for file: " + filePath);
    }
}
