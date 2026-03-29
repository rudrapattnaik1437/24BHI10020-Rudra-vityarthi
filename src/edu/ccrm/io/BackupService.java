package edu.ccrm.io;

import edu.ccrm.config.AppConfig;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.stream.Stream;

/**
 * Backup service using NIO.2 file operations
 * Demonstrates recursive file operations and Path manipulation
 */
public class BackupService {
    private final AppConfig config;
    
    public BackupService() {
        this.config = AppConfig.getInstance();
    }
    
    // Create backup with timestamp
    public Path createBackup() throws IOException {
        String timestamp = config.getCurrentTimestamp();
        Path backupDir = config.getBackupPath().resolve("backup_" + timestamp);
        
        // Create backup directory
        Files.createDirectories(backupDir);
        
        // Copy data directory to backup
        Path dataDir = config.getDataPath();
        if (Files.exists(dataDir)) {
            copyDirectory(dataDir, backupDir.resolve("data"));
        }
        
        System.out.println("Backup created at: " + backupDir.toAbsolutePath());
        return backupDir;
    }
    
    // Recursive directory copy using NIO.2
    private void copyDirectory(Path source, Path target) throws IOException {
        try (Stream<Path> paths = Files.walk(source)) {
            paths.forEach(sourcePath -> {
                try {
                    Path targetPath = target.resolve(source.relativize(sourcePath));
                    if (Files.isDirectory(sourcePath)) {
                        Files.createDirectories(targetPath);
                    } else {
                        Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                    }
                } catch (IOException e) {
                    throw new RuntimeException("Failed to copy: " + sourcePath, e);
                }
            });
        }
    }
    
    // Recursive method to calculate total size of backup directory
    public long calculateBackupSize(Path backupPath) throws IOException {
        if (!Files.exists(backupPath)) {
            return 0;
        }
        
        return calculateDirectorySizeRecursive(backupPath);
    }
    
    // Recursive method demonstration
    private long calculateDirectorySizeRecursive(Path path) throws IOException {
        if (Files.isRegularFile(path)) {
            return Files.size(path);
        }
        
        if (Files.isDirectory(path)) {
            long totalSize = 0;
            try (Stream<Path> children = Files.list(path)) {
                for (Path child : children.toList()) {
                    totalSize += calculateDirectorySizeRecursive(child); // Recursive call
                }
            }
            return totalSize;
        }
        
        return 0;
    }
    
    // List all backup directories
    public void listBackups() throws IOException {
        Path backupDir = config.getBackupPath();
        
        if (!Files.exists(backupDir)) {
            System.out.println("No backups found.");
            return;
        }
        
        System.out.println("Available Backups:");
        System.out.println("-".repeat(50));
        
        try (Stream<Path> backups = Files.list(backupDir)) {
            backups.filter(Files::isDirectory)
                    .sorted((p1, p2) -> p2.getFileName().toString().compareTo(p1.getFileName().toString()))
                    .forEach(backup -> {
                        try {
                            BasicFileAttributes attrs = Files.readAttributes(backup, BasicFileAttributes.class);
                            long size = calculateBackupSize(backup);
                            
                            System.out.printf("%-30s %10s %10d bytes%n",
                                    backup.getFileName().toString(),
                                    attrs.creationTime().toString().substring(0, 19),
                                    size);
                        } catch (IOException e) {
                            System.err.println("Error reading backup info: " + e.getMessage());
                        }
                    });
        }
    }
    
    // Clean old backups (keep only last N)
    public void cleanOldBackups(int keepCount) throws IOException {
        Path backupDir = config.getBackupPath();
        
        if (!Files.exists(backupDir)) {
            return;
        }
        
        try (Stream<Path> backups = Files.list(backupDir)) {
            backups.filter(Files::isDirectory)
                    .sorted((p1, p2) -> p2.getFileName().toString().compareTo(p1.getFileName().toString()))
                    .skip(keepCount)
                    .forEach(backup -> {
                        try {
                            deleteDirectoryRecursively(backup);
                            System.out.println("Deleted old backup: " + backup.getFileName());
                        } catch (IOException e) {
                            System.err.println("Failed to delete backup: " + backup.getFileName());
                        }
                    });
        }
    }
    
    // Recursive directory deletion
    private void deleteDirectoryRecursively(Path path) throws IOException {
        if (Files.isDirectory(path)) {
            try (Stream<Path> children = Files.list(path)) {
                for (Path child : children.toList()) {
                    deleteDirectoryRecursively(child); // Recursive call
                }
            }
        }
        Files.delete(path);
    }
    
    // Restore from backup
    public void restoreFromBackup(Path backupPath) throws IOException {
        if (!Files.exists(backupPath)) {
            throw new IOException("Backup not found: " + backupPath);
        }
        
        Path dataBackup = backupPath.resolve("data");
        if (Files.exists(dataBackup)) {
            Path currentData = config.getDataPath();
            
            // Remove current data directory
            if (Files.exists(currentData)) {
                deleteDirectoryRecursively(currentData);
            }
            
            // Copy backup to current data directory
            copyDirectory(dataBackup, currentData);
            System.out.println("Data restored from: " + backupPath.getFileName());
        }
    }
}