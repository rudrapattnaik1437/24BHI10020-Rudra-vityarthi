package edu.ccrm.config;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Singleton pattern implementation for application configuration
 * Thread-safe lazy initialization
 */
public class AppConfig {
    private static volatile AppConfig instance;
    private final String dataDirectory;
    private final String backupDirectory;
    private final DateTimeFormatter timestampFormat;
    private final String version;
    
    // Private constructor to prevent instantiation
    private AppConfig() {
        this.dataDirectory = "data";
        this.backupDirectory = "backups";
        this.timestampFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        this.version = "1.0.0";
    }
    
    // Thread-safe singleton getInstance method
    public static AppConfig getInstance() {
        if (instance == null) {
            synchronized (AppConfig.class) {
                if (instance == null) {
                    instance = new AppConfig();
                }
            }
        }
        return instance;
    }
    
    public String getDataDirectory() { return dataDirectory; }
    public String getBackupDirectory() { return backupDirectory; }
    public DateTimeFormatter getTimestampFormat() { return timestampFormat; }
    public String getVersion() { return version; }
    
    public Path getDataPath() {
        return Paths.get(dataDirectory);
    }
    
    public Path getBackupPath() {
        return Paths.get(backupDirectory);
    }
    
    public String getCurrentTimestamp() {
        return LocalDateTime.now().format(timestampFormat);
    }
    
    public String getPlatformInfo() {
        return String.format("Java SE %s - Platform independent application running on %s", 
                System.getProperty("java.version"),
                System.getProperty("os.name"));
    }
    
    // Prevent cloning
    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Singleton instance cannot be cloned");
    }
}