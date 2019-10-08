package io.github.sanyarnd.standardpaths;

import org.jetbrains.annotations.NotNull;

/**
 * Indicates that underlying OS system is either damaged or in malformed state.
 *
 * <p>Exception is thrown only in very non-standard situations,
 * e.g. system is missing the basic environment variables or user directories are owned by others
 *
 * @author Alexander Biryukov
 */
public class NoSuchPathException extends RuntimeException {
    /**
     * Constructs a new exception with the specified detail message. The cause is not initialized, and may subsequently
     * be initialized by a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for later retrieval by the {@link #getMessage()}
     *                method.
     */
    public NoSuchPathException(final @NotNull String message) {
        super(message);
    }
}
