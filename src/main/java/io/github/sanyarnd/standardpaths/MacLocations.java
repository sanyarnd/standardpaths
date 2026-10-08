package io.github.sanyarnd.standardpaths;

import java.nio.file.Path;

/// macOS locations.
///
/// Follows the [macOS Library Directory Details][guide].
///
/// [guide]:
/// https://developer.apple.com/library/archive/documentation/FileManagement/Conceptual/FileSystemProgrammingGuide/MacOSXDirectories/MacOSXDirectories.html
///
/// @author Alexander Biryukov
final class MacLocations extends PosixLocations {
    MacLocations(final Environment environment) {
        super(environment);
    }

    @Override
    public Path cache() {
        return library().resolve("Caches");
    }

    @Override
    public Path config() {
        return applicationSupport();
    }

    @Override
    public Path data() {
        return applicationSupport();
    }

    @Override
    public Path dataLocal() {
        return applicationSupport();
    }

    @Override
    public Path desktop() {
        return home().resolve("Desktop");
    }

    @Override
    public Path documents() {
        return home().resolve("Documents");
    }

    @Override
    public Path downloads() {
        return home().resolve("Downloads");
    }

    @Override
    public Path music() {
        return home().resolve("Music");
    }

    @Override
    public Path pictures() {
        return home().resolve("Pictures");
    }

    @Override
    public Path videos() {
        return home().resolve("Movies");
    }

    private Path library() {
        return home().resolve("Library");
    }

    private Path applicationSupport() {
        return library().resolve("Application Support");
    }
}
