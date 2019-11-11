package com.arctos6135.robotlib.logging;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;

import com.arctos6135.robotlib.TestUtils;

import org.junit.Test;

/**
 * Tests {@link RobotLogger}.
 * 
 * @author Tyler Tian
 */
public class RobotLoggerTest {

    /**
     * Tests {@link logger#init(Class, File)} and {@link logger#flush()}. This test
     * makes sure a log file is created.
     * 
     * @throws IOException if something goes wrong
     */
    @Test
    public void testRobotLoggerInit() throws IOException {
        // Create a temp dir for the logs
        // Otherwise there will be an exception since /home is owned by root
        Path tempDir = Files.createTempDirectory("robotlib-test-");
        File f = tempDir.toFile();
        TestUtils.deleteDirectoryOnExit(f);

        RobotLogger logger = new RobotLogger();

        logger.init(getClass(), f);
        logger.flush();

        assertThat(f.listFiles().length, greaterThan(1));
    }

    /**
     * Tests logging to file, including {@link RobotLogger#logError(String)},
     * {@link RobotLogger#logWarning(String)}, {@link RobotLogger#logInfo(String)},
     * {@link RobotLogger#logInfoFine(String)},
     * {@link RobotLogger#logInfoFiner(String)} and
     * {@link RobotLogger#logInfoFinest(String)}.
     * 
     * @throws IOException if something goes wrong
     */
    @Test
    public void testRobotLoggerLogging() throws IOException {
        // Create a temp dir for the logs
        // Otherwise there will be an exception since /home is owned by root
        Path tempDir = Files.createTempDirectory("robotlib-test-");
        File f = tempDir.toFile();
        TestUtils.deleteDirectoryOnExit(f);

        RobotLogger logger = new RobotLogger();

        logger.init(getClass(), f);
        logger.setLevel(Level.FINEST);
        // Flush to make sure the file exists
        logger.flush();

        // Open a reader to the file
        File logFile = null;
        for (File f1 : f.listFiles()) {
            // Set the log file to a file with the .log extension
            // Since a .lck file might also be created
            if (f1.getName().endsWith(".log")) {
                logFile = f1;
                break;
            }
        }
        BufferedReader r = new BufferedReader(new FileReader(logFile));

        // Log an error and flush
        logger.logError("Arctos");
        logger.flush();
        // Verify that the line was written
        String line = r.readLine();
        assertThat(line.contains("Arctos") && line.contains("SEVERE"), is(true));

        logger.logWarning("6135");
        logger.flush();
        line = r.readLine();
        assertThat(line.contains("6135") && line.contains("WARNING"), is(true));

        logger.logInfo("FIRST");
        logger.flush();
        line = r.readLine();
        assertThat(line.contains("FIRST") && line.contains("INFO"), is(true));

        logger.logInfoFine("Robotics");
        logger.flush();
        line = r.readLine();
        assertThat(line.contains("Robotics") && line.contains("FINE"), is(true));

        logger.logInfoFiner("Competition");
        logger.flush();
        line = r.readLine();
        assertThat(line.contains("Competition") && line.contains("FINER"), is(true));

        logger.logInfoFinest("FRC");
        logger.flush();
        line = r.readLine();
        assertThat(line.contains("FRC") && line.contains("FINEST"), is(true));

        r.close();
    }

    Level expectedLevel;
    String expectedMessage;
    boolean handlerCalled = false;

    /**
     * Tests the log handler, including
     * {@link RobotLogger#setLogHandler(java.util.function.BiConsumer)},
     * {@link RobotLogger#logError(String)}, {@link RobotLogger#logWarning(String)},
     * {@link RobotLogger#logInfo(String)}, {@link RobotLogger#logInfoFine(String)},
     * {@link RobotLogger#logInfoFiner(String)} and
     * {@link RobotLogger#logInfoFinest(String)}.
     * 
     * @throws IOException if something goes wrong
     */
    @Test
    public void testRobotLoggerHandler() throws IOException {
        // Create a temp dir for the logs
        // Otherwise there will be an exception since /home is owned by root
        Path tempDir = Files.createTempDirectory("robotlib-test-");
        File f = tempDir.toFile();
        TestUtils.deleteDirectoryOnExit(f);

        RobotLogger logger = new RobotLogger();

        logger.init(getClass(), f);

        // Set the log handler
        logger.setLogHandler((level, message) -> {
            // Assert level and message are as expected
            assertThat(expectedLevel, is(equalTo(level)));
            assertThat(expectedMessage, is(equalTo(message)));
            // Indicate handler has been called
            handlerCalled = true;
        });

        expectedLevel = Level.SEVERE;
        expectedMessage = "Arctos";
        handlerCalled = false;
        logger.logError("Arctos");
        assertThat(handlerCalled, is(true));

        expectedLevel = Level.WARNING;
        expectedMessage = "6135";
        handlerCalled = false;
        logger.logWarning("6135");
        assertThat(handlerCalled, is(true));

        expectedLevel = Level.INFO;
        expectedMessage = "FIRST";
        handlerCalled = false;
        logger.logInfo("FIRST");
        assertThat(handlerCalled, is(true));

        expectedLevel = Level.FINE;
        expectedMessage = "Robotics";
        handlerCalled = false;
        logger.logInfoFine("Robotics");
        assertThat(handlerCalled, is(true));

        expectedLevel = Level.FINER;
        expectedMessage = "Competition";
        handlerCalled = false;
        logger.logInfoFiner("Competition");
        assertThat(handlerCalled, is(true));

        expectedLevel = Level.FINEST;
        expectedMessage = "FRC";
        handlerCalled = false;
        logger.logInfoFinest("FRC");
        assertThat(handlerCalled, is(true));
    }
}
