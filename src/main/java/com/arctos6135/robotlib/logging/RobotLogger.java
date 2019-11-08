package com.arctos6135.robotlib.logging;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
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
 * Because log files can contain a lot of data and accumulates over time, it is
 * recommended that the {@link #cleanLogs(File, DateFormat, long)} method or one
 * of its overloads be used to automatically delete old log files.
 * </p>
 * 
 * @author Tyler Tian
 */
public final class RobotLogger {
    private static Handler fileHandler;
    private static Formatter formatter;
    private static Logger logger;

    private static DateFormat dateFormat;
    private static File logDir;

    private static boolean isInitialized = false;

    private static BiConsumer<Level, String> logHandler;

    static {
        // Add a shutdown hook on the VM to always write logs before exiting
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                flush();
            }
        });
    }

    private RobotLogger() {
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
     * The log files are stored in "/home/lvuser/frc-robot-logs". The dates are
     * formatted with the string "yyyy_MM_dd-HH_mm_ss".
     * </p>
     * 
     * @param robotClass The robot's class
     * @throws IOException              If an error occurs with the log file
     * @throws IllegalArgumentException If {@code logDir} is not a directory
     */
    public static void init(Class<?> robotClass) throws IOException {
        init(robotClass, new File("/home/lvuser/frc-robot-logs"));
    }

    /**
     * Initializes the robot logger.
     * 
     * <p>
     * If the logger is already initialized, this method will have no effect.
     * Attempting to log without first initializing the logger will have no effect.
     * </p>
     * <p>
     * The dates are formatted with the string "yyyy_MM_dd-HH_mm_ss".
     * </p>
     * 
     * @param robotClass The robot's class
     * @param logDir     The directory to store the logs in
     * @throws IOException              If an error occurs with the log file
     * @throws IllegalArgumentException If {@code logDir} is not a directory
     */
    public static void init(Class<?> robotClass, File logDir) throws IOException {
        init(robotClass, logDir, new SimpleDateFormat("yyyy_MM_dd-HH_mm_ss"));
    }

    /**
     * Initializes the robot logger.
     * 
     * <p>
     * If the logger is already initialized, this method will have no effect.
     * Attempting to log without first initializing the logger will have no effect.
     * </p>
     * 
     * @param robotClass The robot's class
     * @param logDir     The directory to store the logs in
     * @param dateFormat A date formatter (for the log file name)
     * @throws IOException              If an error occurs with the log file
     * @throws IllegalArgumentException If {@code logDir} is not a directory
     */
    public static void init(Class<?> robotClass, File logDir, DateFormat dateFormat) throws IOException {
        if (isInitialized) {
            return;
        }
        if (!logDir.isDirectory()) {
            throw new IllegalArgumentException("logDir must be a directory!");
        }

        // Get logger for robot class
        logger = Logger.getLogger(robotClass.getName());
        logger.setUseParentHandlers(false);

        RobotLogger.dateFormat = dateFormat;

        // Get a date string (for the log file)
        Date date = new Date();

        // Create the log directory if it does not exist
        logDir.mkdirs();

        // Create handler and formatter
        fileHandler = new FileHandler(logDir.getAbsolutePath() + File.separator + dateFormat.format(date) + ".log");
        formatter = new RobotLoggerFormatter();
        fileHandler.setFormatter(formatter);
        logger.addHandler(fileHandler);

        isInitialized = true;
    }

    /**
     * Sets the logging level.
     * 
     * @param level The logging level
     */
    public static void setLevel(Level level) {
        logger.setLevel(level);
    }

    /**
     * Sets a log handler that will be called every time info is logged.
     * 
     * @param handler A {@code BiConsumer<T, U>} accepting a log level and log
     *                message string.
     */
    public static void setLogHandler(BiConsumer<Level, String> handler) {
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
    public static void logError(String error) {
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
    public static void logWarning(String warning) {
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
    public static void logInfo(String info) {
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
    public static void logInfoFine(String infoFine) {
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
    public static void logInfoFiner(String infoFiner) {
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
    public static void logInfoFinest(String infoFinest) {
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
    public static void flush() {
        if (isInitialized) {
            fileHandler.flush();
        }
    }

    /**
     * Deletes all the logs that are more than a certain number of hours old.
     * 
     * <p>
     * Note that unlike {@link #cleanLogs(File, DateFormat, long)}, this method will
     * have no effect if the logger is not initialized, since it depends on the log
     * directory and date format to be set.
     * </p>
     * 
     * @param maxAgeHours The max age, in hours, of a log before it gets deleted
     */
    public static void cleanLogs(long maxAgeHours) {
        if (!isInitialized) {
            return;
        }
        cleanLogs(logDir, dateFormat, maxAgeHours);
    }

    /**
     * Deletes all the logs that are more than a certain number of hours old.
     * 
     * <p>
     * Unlike {@link #cleanLogs(long)}, this method still works even if the logger
     * is not initialized.
     * </p>
     * 
     * @param logDir        The log directory
     * @param logDateFormat The date format the log file names were formatted with
     * @param maxAgeHours   The max age, in hours, of a log file before it gets
     *                      deleted
     */
    public static void cleanLogs(File logDir, DateFormat logDateFormat, long maxAgeHours) {
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
            // Check only files
            if (f.isFile()) {
                try {
                    // Try to parse the date
                    // Parse it from the filename instead of getting the last modified time
                    // This way we don't delete anything that's not a log file
                    Date d = logDateFormat.parse(f.getName());
                    // Convert the difference between the two times into hours and delete the file
                    // if needed
                    long diffHours = TimeUnit.HOURS.convert(now.getTime() - d.getTime(), TimeUnit.MILLISECONDS);
                    if (diffHours > maxAgeHours) {
                        f.delete();
                    }
                }
                // If the name cannot be parsed skip it
                catch (ParseException e) {
                    continue;
                }
            }
        }
    }
}
