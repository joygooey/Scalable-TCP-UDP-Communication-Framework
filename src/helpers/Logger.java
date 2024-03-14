package helpers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    public static void log(String message) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SS");
        String formattedDateTime = now.format(formatter);
        System.out.println("[" + formattedDateTime + "] " + message);
    }

    public static void main(String[] args) {
        Logger.log("yuhoo");
    }
}
