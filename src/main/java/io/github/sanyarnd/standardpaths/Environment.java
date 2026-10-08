package io.github.sanyarnd.standardpaths;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import org.jspecify.annotations.Nullable;

/// Access to environment variables and system properties, replaceable in tests.
///
/// @author Alexander Biryukov
interface Environment {
    /// Environment of the current process.
    Environment SYSTEM = new Environment() {
        @Override
        public @Nullable String getenv(final String name) {
            return System.getenv(name);
        }

        @Override
        public @Nullable String getProperty(final String name) {
            return System.getProperty(name);
        }
    };

    /// Value of the environment variable.
    ///
    /// @param name variable name
    /// @return variable value or `null` if not defined
    @Nullable
    String getenv(String name);

    /// Value of the system property.
    ///
    /// @param name property name
    /// @return property value or `null` if not defined
    @Nullable
    String getProperty(String name);

    /// Absolute path stored in the environment variable.
    ///
    /// @param name variable name
    /// @return path or empty if variable is not defined, blank or doesn't contain an absolute path
    default Optional<Path> envPath(final String name) {
        return absolutePath(getenv(name));
    }

    /// Absolute path stored in the system property.
    ///
    /// @param name property name
    /// @return path or empty if property is not defined, blank or doesn't contain an absolute path
    default Optional<Path> propertyPath(final String name) {
        return absolutePath(getProperty(name));
    }

    /// Converts the value to an absolute path.
    ///
    /// @param value path string
    /// @return path or empty if value is `null`, blank or not an absolute path
    static Optional<Path> absolutePath(final @Nullable String value) {
        if (value == null || value.trim().isEmpty()) {
            return Optional.empty();
        }
        try {
            final Path path = Paths.get(value);
            return path.isAbsolute() ? Optional.of(path.normalize()) : Optional.empty();
        } catch (InvalidPathException e) {
            return Optional.empty();
        }
    }
}
