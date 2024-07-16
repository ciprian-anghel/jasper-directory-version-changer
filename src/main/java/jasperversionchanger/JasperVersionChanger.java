package main.java.jasperversionchanger;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class JasperVersionChanger {

	private static Logger logger = Logger.getLogger(JasperVersionChanger.class.getName());

	private final String pathToZip;
	private final String fromFolderName;
	private final String toFolderName;

	private static final String DESTINATION_DIR_NAME = "out"; 

	public JasperVersionChanger(String... args) {
		validateArguments(args);
		
		this.pathToZip = args[0];
		this.fromFolderName = args[1];
		this.toFolderName = args[2];		
	}

	public void start() {
		process();
	}

	private void process() {
		logger.info("Process started...");

		try (ZipInputStream zipIn = new ZipInputStream(new FileInputStream(pathToZip))) {

			File destDirectory = new File("." + File.separator + DESTINATION_DIR_NAME);
			if (!destDirectory.exists()) {
				destDirectory.mkdirs();
				logger.info("Dir created: " + destDirectory);
			}

			ZipEntry entry = zipIn.getNextEntry();
			while (entry != null) {
				String newFilePath = "." + File.separator + DESTINATION_DIR_NAME + File.separator + entry.getName();
				newFilePath = newFilePath.replace(fromFolderName, toFolderName);

				if (!entry.isDirectory()) {
					// If the entry is a file, extract it
					extractFile(zipIn, newFilePath);
				} else {
					// If the entry is a directory, create it
					File dir = new File(newFilePath);
					dir.mkdirs();
					logger.info("Dir created: " + dir);
				}
				zipIn.closeEntry();
				entry = zipIn.getNextEntry();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		logger.info("Process complete...");
	}

	private void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
		try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath))) {
			byte[] bytesIn = new byte[4096];
			int read;
			while ((read = zipIn.read(bytesIn)) != -1) {
				bos.write(bytesIn, 0, read);
			}
		}
		Path newPath = Paths.get(filePath);
		String content = new String(Files.readAllBytes(newPath));
		if (content.contains(fromFolderName)) {
			content = content.replace(fromFolderName, toFolderName);
			logger.info("file edited: " + newPath);
		}
		Files.write(newPath, content.getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
	}

	private void validateArguments(String... args) {
		if (args.length != 3) {
			howToCallInfoMessage();
			throw new IllegalArgumentException("Wrong number of arguments");
		}
		
		final String pathToZip = args[0];
		final String fromFolderName = args[1];
		final String toFolderName = args[2];
		
		if (pathToZip == null || fromFolderName == null || toFolderName == null ||
				pathToZip.isBlank() || fromFolderName.isBlank() || toFolderName.isBlank()) {
			howToCallInfoMessage();
			throw new IllegalArgumentException("Missing arguments");
		}

		if (!pathToZip.endsWith(".zip")) {
			logger.info("First argument should end with .zip extension.");
			throw new IllegalArgumentException("Unexpected file extension. Zip file expected.");
		}

		boolean zipFileExists = Files.exists(Paths.get(pathToZip));
		if (!zipFileExists) {
			logger.info("File provided to first argument was not found.");
			throw new IllegalArgumentException("File not found.");
		}
	}
	
	private void howToCallInfoMessage() {
		logger.info("The following arguments are required: <path to zip file> <from folder name> <to folder name>");
		logger.info("For example:");
		logger.info("java -jar JasperVersionChanger.jar ./Version243.zip Version243 Version244");
	}
}
