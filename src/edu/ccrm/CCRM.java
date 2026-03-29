package edu.ccrm;

import edu.ccrm.cli.MainMenu;
import edu.ccrm.config.AppConfig;

/**
 * Campus Course & Records Manager (CCRM)
 * Main application entry point demonstrating Java SE concepts
 */
public class CCRM {
    public static void main(String[] args) {
        // Initialize application configuration (Singleton pattern)
        AppConfig config = AppConfig.getInstance();
        System.out.println("Campus Course & Records Manager (CCRM) by 24BCE10528");
        System.out.println("========================================");
        System.out.println("Java Platform: " + config.getPlatformInfo());
        System.out.println();
        
        // Start the CLI menu
        MainMenu menu = new MainMenu();
        menu.run();
    }
}