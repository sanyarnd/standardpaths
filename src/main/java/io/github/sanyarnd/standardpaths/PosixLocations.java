package io.github.sanyarnd.standardpaths;

import java.nio.file.Path;
import java.nio.file.Paths;

/// Locations shared by POSIX-like systems.
///
/// @author Alexander Biryukov
abstract class PosixLocations implements LocationDelegate {
    protected final Environment env;

    PosixLocations(final Environment environment) {
        env = environment;
    }

    @Override
    public Path home() {
        return env.envPath("HOME")
                .or(() -> env.propertyPath("user.home"))
                .orElseThrow(() -> new NoSuchPathException("Unable to determine home directory: $HOME is not set"));
    }

    @Override
    public Path temp() {
        return env.envPath("TMPDIR")
                .or(() -> env.propertyPath("java.io.tmpdir"))
                .orElseGet(() -> Paths.get("/tmp"));
    }
}
