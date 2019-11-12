package com.arctos6135.robotlib.logging;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.BiConsumer;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import edu.wpi.first.wpilibj.DriverStation;

/**
 * A class with static methods for logging to files or other places on the
 * robot.
 * 
 * <p>
 * Note: The log files might not get written completely if the buffer is not
 * flushed. Although it is automatically flushed when the VM exits normally,
 * shutdown hooks will not be called in the event of a sudden power loss, such
 * as powering off the robot through a breaker. Therefore, it is recommended
 * that {@link #flush()} be called in the
 * {@link edu.wpi.first.wpilibj.IterativeRobotBase#disabledInit()
 * disabledInit()} method of the robot to flush the logs every time the robot is
 * disabled.
 * </p>
 * <p>
 * Note: Since the roboRIO has no external battery to power the RTC, its system
 * time will be reset every time power is lost. The system time is only
 * correctly updated after the Driver Station is connected. Therefore, you
 * should wait until {@link DriverStation#isDSAttached()} returns true before
 * calling {@link #init(Class)} or any of its overloads, since they depend on
 * the system time to be correct in order to create a log file with the correct
 * name.
 * </p>
 * <p>
 * Because log files can contain a lot of data and accumulates over time, it is
 * recommended that the {@link #cleanLogs(File, double)} method or one of its
 * overloads be used to automatically delete old log files.
 * </p>
 * 
 * @author Tyler Tian
 */
public class RobotLogger {
    private Handler fileHandler;
    private Formatter formatter;
    private Logger logger;

    private File logDir;
    private File logFile;

    private boolean isInitialized = false;

    private BiConsumer<Level, String> logHandler;

    /**
     * Constructs a new logger instance.
     * 
     * <p>
     * Note that this does not initialize it. Call {@link #init(Class)} or one of
     * its overloads to initialize the logger.
     * </p>
     */
    public RobotLogger() {
        // Add a shutdown hook on the VM to always write logs before exiting
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                flush();
            }
        });
    }

    /**
     * Formatter for the logger.
     * 
     * <p>
     * Messages are formatted in the format: (yyyy/MM/dd HH:mm:ss) [LEVEL] Message
     * </p>
     */
    private static class RobotLoggerFormatter extends Formatter {

        private static DateFormat format = new SimpleDateFormat("(yyyy/MM/dd HH:mm:ss)");

        @Override
        public String format(LogRecord record) {
            StringBuilder builder = new StringBuilder(format.format(new Date()));
            builder.append(" [").append(record.getLevel().toString()).append("]: ").append(record.getMessage())
                    .append("\n");
            return builder.toString();
        }
    }

    /**
     * Initializes the robot logger.
     * 
     * <p>
     * If the logger is already initialized, this method will have no effect.
     * Attempting to log without first initializing the logger will have no effect.
     * </p>
     * <p>
     * The log files are stored in "/home/lvuser/frc-robot-logs". If this directory
     * does not exist, it will be created. The dates are formatted with the string
     * "yyyy_MM_dd-HH_mm_ss".
     * </p>
     * <p>
     * Note: Since the roboRIO has no external battery to power the RTC, its system
     * time will be reset every time power is lost. The system time is only
     * correctly updated after the Driver Station is connected. Therefore, you
     * should wait until {@link DriverStation#isDSAttached()} returns true before
     * calling this method or any of its overloads, since they depend on the system
     * time to be correct in order to create a log file with the correct name.
     * </p>
     * 
     * @param robotClass The robot's class
     * @throws IOException              If an error occurs with creating the log
     *                                  file or directories
     * @throws IllegalArgumentException If the log directory exists, but is not a
     *                                  directory
     */
    public void init(Class<?> robotClass) throws IOException {
        init(robotClass, new File("/home/lvuser/frc-robot-logs"));
    }

    /**
     * Initializes the robot logger.
     * 
     * <p>
     * If {@code logDir} or any of its parent directories do not exist, they will be
     * created.
     * </p>
     * <p>
     * If the logger is already initialized, this method will have no effect.
     * Attempting to log without first initializing the logger will have no effect.
     * </p>
     * <p>
     * The dates are formatted with the string "yyyy_MM_dd-HH_mm_ss".
     * </p>
     * <p>
     * Note: Since the roboRIO has no external battery to power the RTC, its system
     * time will be reset every time power is lost. The system time is only
     * correctly updated after the Driver Station is connected. Therefore, you
     * should wait until {@link DriverStation#isDSAttached()} returns true before
     * calling this method or any of its overloads, since they depend on the system
     * time to be correct in order to create a log file with the correct name.
     * </p>
     * 
     * @param robotClass The robot's class
     * @param logDir     The directory to store the logs in
     * @throws IOException              If an error occurs with creating the log
     *                                  file or directories
     * @throws IllegalArgumentException If {@code logDir} exists, but is not a
     *                                  directory
     */
    public void init(Class<?> robotClass, File logDir) throws IOException {
        init(robotClass, logDir, new SimpleDateFormat("yyyy_MM_dd-HH_mm_ss"));
    }

    /**
     * Initializes the robot logger.
     * 
     * <p>
     * If {@code logDir} or any of its parent directories do not exist, they will be
     * created.
     * </p>
     * <p>
     * If the logger is already initialized, this method will have no effect.
     * Attempting to log without first initializing the logger will have no effect.
     * </p>
     * <p>
     * Note: Since the roboRIO has no external battery to power the RTC, its system
     * time will be reset every time power is lost. The system time is only
     * correctly updated after the Driver Station is connected. Therefore, you
     * should wait until {@link DriverStation#isDSAttached()} returns true before
     * calling this method or any of its overloads, since they depend on the system
     * time to be correct in order to create a log file with the correct name.
     * </p>
     * 
     * @param robotClass The robot's class
     * @param logDir     The directory to store the logs in
     * @param dateFormat A date formatter (for the log file name)
     * @throws IOException              If an error occurs with creating the log
     *                                  file or directories
     * @throws IllegalArgumentException If {@code logDir} exists, but is not a
     *                                  directory
     */
    public void init(Class<?> robotClass, File logDir, DateFormat dateFormat) throws IOException {
        if (isInitialized) {
            return;
        }
        if (logDir.exists() && !logDir.isDirectory()) {
            throw new IllegalArgumentException("logDir must be a directory!");
        }

        // Get logger for robot class
        logger = Logger.getLogger(robotClass.getName());
        logger.setUseParentHandlers(false);

        this.logDir = logDir;

        // Get a date string (for the log file)
        Date date = new Date();

        // Create the log directory if it does not exist
        if (!logDir.exists()) {
            try {
                if (!logDir.mkdirs()) {
                    throw new IOException("Failed to create log directory!");
                }
            } catch (SecurityException e) {
                throw new IOException("Failed to create log directory!", e);
            }
        }

        // Create handler and formatter
        String logFilePath = logDir.getAbsolutePath() + File.separator + dateFormat.format(date) + ".log";
        this.fileHandler = new FileHandler(logFilePath);
        this.logFile = new File(logFilePath);
        this.formatter = new RobotLoggerFormatter();
        fileHandler.setFormatter(formatter);
        logger.addHandler(fileHandler);

        isInitialized = true;
    }

    /**
     * Sets the logging level.
     * 
     * <p>
     * Note that this only affects the logs that are written to file; the errors and
     * warnings reported to the Driver Station and log handler calls are not
     * affected.
     * </p>
     * 
     * @param level The logging level
     */
    public void setLevel(Level level) {
        logger.setLevel(level);
    }

    /**
     * Sets a log handler that will be called every time info is logged.
     * 
     * @param handler A {@code BiConsumer<T, U>} accepting a log level and log
     *                message string.
     */
    public void setLogHandler(BiConsumer<Level, String> handler) {
        logHandler = handler;
    }

    /**
     * Logs an error and reports it to the Driver Station.
     * 
     * <p>
     * The error will be logged with the {@code SEVERE} level and will also be
     * reported to the DS. If the logger is not initialized, this will have no
     * effect.
     * </p>
     * 
     * @param error The error message
     */
    public void logError(String error) {
        if (isInitialized) {
            DriverStation.reportError(error, false);
            logger.severe(error);

            if (logHandler != null) {
                logHandler.accept(Level.SEVERE, error);
            }
        }
    }

    /**
     * Logs a warning and reports it to the Driver Station.
     * 
     * <p>
     * The warning will be logged with the {@code WARNING} level and will also be
     * reported to the DS. If the logger is not initialized, this will have no
     * effect.
     * </p>
     * 
     * @param warning The warning message
     */
    public void logWarning(String warning) {
        if (isInitialized) {
            DriverStation.reportWarning(warning, false);
            logger.warning(warning);

            if (logHandler != null) {
                logHandler.accept(Level.WARNING, warning);
            }
        }
    }

    /**
     * Logs regular info.
     * 
     * <p>
     * The info will be logged with the {@code INFO} level. If the logger is not
     * initialized, this will have no effect.
     * </p>
     * 
     * @param info The info message
     */
    public void logInfo(String info) {
        if (isInitialized) {
            logger.info(info);

            if (logHandler != null) {
                logHandler.accept(Level.INFO, info);
            }
        }
    }

    /**
     * Logs fine info.
     * 
     * <p>
     * The info will be logged with the {@code FINE} level. If the logger is not
     * initialized, this will have no effect.
     * </p>
     * 
     * @param infoFine The info message
     */
    public void logInfoFine(String infoFine) {
        if (isInitialized) {
            logger.fine(infoFine);

            if (logHandler != null) {
                logHandler.accept(Level.FINE, infoFine);
            }
        }
    }

    /**
     * Logs finer info.
     * 
     * <p>
     * The info will be logged with the {@code FINER} level. If the logger is not
     * initialized, this will have no effect.
     * </p>
     * 
     * @param infoFiner The info message
     */
    public void logInfoFiner(String infoFiner) {
        if (isInitialized) {
            logger.finer(infoFiner);

            if (logHandler != null) {
                logHandler.accept(Level.FINER, infoFiner);
            }
        }
    }

    /**
     * Logs finest info.
     * 
     * <p>
     * The info will be logged with the {@code FINEST} level. If the logger is not
     * initialized, this will have no effect.
     * </p>
     * 
     * @param infoFinest The info message
     */
    public void logInfoFinest(String infoFinest) {
        if (isInitialized) {
            logger.finest(infoFinest);

            if (logHandler != null) {
                logHandler.accept(Level.FINEST, infoFinest);
            }
        }
    }

    /**
     * Writes the logs out to a file.
     * 
     * <p>
     * This is automatically done via a shutdown hook when the VM exits. However, as
     * shutdown hooks will not be called in the event of a sudden power loss such as
     * powering off the robot through a breaker, it is recommended this method be
     * called in the {@link edu.wpi.first.wpilibj.IterativeRobotBase#disabledInit()
     * disabledInit()} method to flush the logs every time the robot is disabled.
     * </p>
     */
    public void flush() {
        if (isInitialized) {
            fileHandler.flush();
        }
    }

    /**
     * Deletes all the logs that are more than a certain number of hours old.
     * 
     * <p>
     * This method will only delete files that have an extension of .log, or
     * .log.lck.
     * </p>
     * <p>
     * Note that unlike {@link #cleanLogs(File, double)}, this method will have no
     * effect if the logger is not initialized, since it depends on the log
     * directory to be set.
     * </p>
     * 
     * @param maxAgeHours The max age, in hours, of a log before it gets deleted
     */
    public void cleanLogs(double maxAgeHours) {
        if (!isInitialized) {
            return;
        }
        cleanLogs(logDir, maxAgeHours);
    }

    /**
     * Deletes all the logs that are more than a certain number of hours old.
     * 
     * <p>
     * This method will only delete files that have an extension of .log, or
     * .log.lck.
     * </p>
     * <p>
     * Unlike {@link #cleanLogs(double)}, this method still works even if the logger
     * is not initialized.
     * </p>
     * 
     * @param logDir      The log directory
     * @param maxAgeHours The max age, in hours, of a log file before it gets
     *                    deleted
     */
    public void cleanLogs(File logDir, double maxAgeHours) {
        if (!logDir.isDirectory()) {
            if (logDir.exists()) {
                throw new IllegalArgumentException("logDir must be a directory");
            } else {
                logWarning("The log directory passed to cleanLogs does not exist!");
                return;
            }
        }
        // Cache the current date and time
        Date now = new Date();
        // Go through all files in the dir
        for (File f : logDir.listFiles()) {
            // Check only files that end in .log
            // Also make sure that it's not the file currently used
            try {
                if (f.isFile()
                        && (f.getName().endsWith(".log") && !f.getCanonicalPath().equals(logFile.getCanonicalPath()))
                        || (f.getName().endsWith(".log.lck")
                                && !f.getCanonicalPath().equals(logFile.getCanonicalPath() + ".lck"))) {
                    // Calculate time after last modified
                    double diffHours = (now.getTime() - f.lastModified()) / 3600000.0;
                    if (diffHours >= maxAgeHours) {
                        f.delete();
                    }
                }
            } catch (IOException e) {
                // WTF
                e.printStackTrace();
                throw new UncheckedIOException("Something bad just happened that never should", e);
            }
        }
    }
}
