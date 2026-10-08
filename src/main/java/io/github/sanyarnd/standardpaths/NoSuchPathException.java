package io.github.sanyarnd.standardpaths;

import org.jspecify.annotations.Nullable;

/// Indicates that the path can't be determined.
///
/// The exception is thrown only in non-standard situations, e.g. the system is missing the basic environment
/// variables or a system call failed.
///
/// @author Alexander Biryukov
public class NoSuchPathException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /// Constructs a new exception with the specified detail message.
    ///
    /// @param message the detail message
    public NoSuchPathException(final @Nullable String message) {
        super(message);
    }

    NoSuchPathException(final @Nullable String message, final @Nullable Throwable cause) {
        super(message, cause);
    }
}
