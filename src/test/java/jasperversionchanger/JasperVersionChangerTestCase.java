package test.java.jasperversionchanger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import main.java.jasperversionchanger.JasperVersionChanger;

class JasperVersionChangerTestCase {
	
	private final Path testZipPath = Paths.get("./SomeFile.zip");
		
	@AfterEach
	void after() {
		try {
			Files.delete(testZipPath);
		} catch (IOException e) {}
	}
	
	@Test
	void testArgumentsProvidedCorrectly() throws IOException {
	    Files.createFile(testZipPath);
		new JasperVersionChanger("./SomeFile.zip", "Value1", "Value2");
	}
	
	@Test
	void testNoArgumentsProvided() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			new JasperVersionChanger(null, null, null);
        });
		assertEquals("Missing arguments", exception.getMessage());
	}
	
	@Test
	void testEmptyArguments() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			new JasperVersionChanger("  ", "  ", "   ");
        });
		assertEquals("Missing arguments", exception.getMessage());
	}
	
	@Test
	void testNotZip() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			new JasperVersionChanger("SomeFile.txt", "Value1", "Value2");
        });
		assertEquals("Unexpected file extension. Zip file expected.", exception.getMessage());
	}
	
	@Test
	void testFileNotFound() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new JasperVersionChanger("SomeFile.zip", "Value1", "Value2");
        });
		assertEquals("File not found.", exception.getMessage());
	}
	
	@Test
	void testWrongNumberOfArgs() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new JasperVersionChanger();
        });
		assertEquals("Wrong number of arguments", exception.getMessage());
		
		exception = assertThrows(IllegalArgumentException.class, () -> {
            new JasperVersionChanger("one");
        });
		assertEquals("Wrong number of arguments", exception.getMessage());
		
		exception = assertThrows(IllegalArgumentException.class, () -> {
            new JasperVersionChanger("one", "two");
        });
		assertEquals("Wrong number of arguments", exception.getMessage());
		
		exception = assertThrows(IllegalArgumentException.class, () -> {
            new JasperVersionChanger("one", "two", "three", "four");
        });
		assertEquals("Wrong number of arguments", exception.getMessage());
	}
}
