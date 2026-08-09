package ServerPackage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogWriter {
    private static final String LOG_FILE = "chat_history.txt";

    public static void logMessage(String userId, String msg, String godzina) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            writer.write("[" + timestamp + "] " + userId + " (" + godzina + "): " + msg);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Błąd zapisu do logu: " + e.getMessage());
        }
    }
}
