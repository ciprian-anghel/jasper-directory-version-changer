package main.java.jasperversionchanger;

import java.util.logging.ConsoleHandler;
import java.util.logging.Logger;

import main.java.jasperversionchanger.formatter.SimpleLoggingFormatter;


public class JasperVersionChangerMain {	
	
	private static void configureLogger() {
        Logger rootLogger = Logger.getLogger("");
        ConsoleHandler consoleHandler = null;

        for (var handler : rootLogger.getHandlers()) {
            if (handler instanceof ConsoleHandler) {
                consoleHandler = (ConsoleHandler) handler;
                break;
            }
        }

        if (consoleHandler != null) {
            consoleHandler.setFormatter(new SimpleLoggingFormatter());
        }
    }	
	
	public static void main(String[] args) {
		configureLogger();
		
		JasperVersionChanger versionChanger = new JasperVersionChanger(args);
		versionChanger.start();		
	}
}