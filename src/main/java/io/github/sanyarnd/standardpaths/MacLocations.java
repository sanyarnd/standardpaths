package io.github.sanyarnd.standardpaths;

import java.nio.file.Path;
import java.util.Optional;

/// macOS locations.
///
/// Follows the [macOS Library Directory Details][guide].
///
/// [guide]:
/// https://developer.apple.com/library/archive/documentation/FileManagement/Conceptual/FileSystemProgrammingGuide/MacOSXDirectories/MacOSXDirectories.html
///
/// @author Alexander Biryukov
final class MacLocations extends PosixLocations {
    private static final String APPLICATION_SUPPORT = "Library/Application Support";

    MacLocations(final Environment environment) {
        super(environment);
    }

    @Override
    public Optional<Path> cache() {
        return inHome("Library/Caches");
    }

    @Override
    public Optional<Path> config() {
        return inHome(APPLICATION_SUPPORT);
    }

    @Override
    public Optional<Path> data() {
        return inHome(APPLICATION_SUPPORT);
    }

    @Override
    public Optional<Path> dataLocal() {
        return inHome(APPLICATION_SUPPORT);
    }

    @Override
    public Optional<Path> state() {
        return inHome(APPLICATION_SUPPORT);
    }

    @Override
    public Optional<Path> runtime() {
        return Optional.empty();
    }

    @Override
    public Optional<Path> desktop() {
        return inHome("Desktop");
    }

    @Override
    public Optional<Path> documents() {
        return inHome("Documents");
    }

    @Override
    public Optional<Path> downloads() {
        return inHome("Downloads");
    }

    @Override
    public Optional<Path> music() {
        return inHome("Music");
    }

    @Override
    public Optional<Path> pictures() {
        return inHome("Pictures");
    }

    @Override
    public Optional<Path> videos() {
        return inHome("Movies");
    }

    @Override
    public Optional<Path> templates() {
        return Optional.empty();
    }

    @Override
    public Optional<Path> publicShare() {
        return inHome("Public");
    }
}
