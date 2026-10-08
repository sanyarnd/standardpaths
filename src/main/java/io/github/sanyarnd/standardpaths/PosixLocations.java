package io.github.sanyarnd.standardpaths;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

/// Locations shared by POSIX-like systems.
///
/// @author Alexander Biryukov
abstract class PosixLocations implements LocationDelegate {
    protected final Environment env;

    PosixLocations(final Environment environment) {
        env = environment;
    }

    @Override
    public Optional<Path> home() {
        return env.envPath("HOME").or(() -> env.propertyPath("user.home"));
    }

    @Override
    public Optional<Path> temp() {
        return env.envPath("TMPDIR")
                .or(() -> env.propertyPath("java.io.tmpdir"))
                .or(() -> Optional.of(Paths.get("/tmp")));
    }

    /// Resolves the path against the home directory.
    ///
    /// @param relative relative path
    /// @return resolved path or empty if home directory is unknown
    protected Optional<Path> inHome(final String relative) {
        return home().map(path -> path.resolve(relative));
    }
}
