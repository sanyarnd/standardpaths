package io.github.sanyarnd.standardpaths;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Linux locations.
 *
 * @author Alexander Biryukov
 */
final class LinuxLocations implements LocationDelegate {
    private static final String HOME_ENV = "HOME";

    private static final String XDG_CACHE_HOME = "XDG_CACHE_HOME";
    private static final String XDG_CONFIG_HOME = "XDG_CONFIG_HOME";
    private static final String XDG_DATA_HOME = "XDG_DATA_HOME";
    private static final String XDG_DESKTOP_DIR = "XDG_DESKTOP_DIR";
    private static final String XDG_DOCUMENTS_DIR = "XDG_DOCUMENTS_DIR";
    private static final String XDG_DOWNLOAD_DIR = "XDG_DOWNLOAD_DIR";
    private static final String XDG_MUSIC_DIR = "XDG_MUSIC_DIR";
    private static final String XDG_PICTURES_DIR = "XDG_PICTURES_DIR";
    private static final String XDG_VIDEOS_DIR = "XDG_VIDEOS_DIR";

    private static @NotNull Optional<String> getVariable(final @NotNull String variable) {
        final String result = System.getenv(variable);
        return result == null || result.trim().isEmpty() ? Optional.empty() : Optional.of(result);
    }

    @SafeVarargs
    private static @NotNull Path takeFirstPath(final @NotNull Optional<String>... candidates) {
        return Stream.of(candidates)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst()
                .map(Paths::get)
                .orElseThrow(() -> new NoSuchPathException("Unable to retrieve path for directory"));
    }

    @Override
    public @NotNull Path cache() {
        final Optional<String> xdgCache = getVariable(XDG_CACHE_HOME);
        final Optional<String> home = getVariable(HOME_ENV).map(s -> s + File.separator + ".cache");
        return takeFirstPath(xdgCache, home);
    }

    @Override
    public @NotNull Path config() {
        final Optional<String> xdgConfig = getVariable(XDG_CONFIG_HOME);
        final Optional<String> home = getVariable(HOME_ENV).map(s -> s + File.separator + ".config");

        return takeFirstPath(xdgConfig, home);
    }

    @Override
    public @NotNull Path data() {
        final Optional<String> xdgData = getVariable(XDG_DATA_HOME);
        final Optional<String> home = getVariable(HOME_ENV)
                .map(s -> s + File.separator + ".local" + File.separator + "share");

        return takeFirstPath(xdgData, home);
    }

    @Override
    public @NotNull Path dataLocal() {
        return data();
    }

    @Override
    public @NotNull Path temp() {
        return Paths.get("/tmp");
    }

    @Override
    public @NotNull Path home() {
        final Optional<String> home = getVariable(HOME_ENV);
        final Optional<String> tilde = Optional.of("~");

        return takeFirstPath(home, tilde);
    }

    @Override
    public @NotNull Path desktop() {
        final Optional<String> xdgDesktop = getVariable(XDG_DESKTOP_DIR);
        final Optional<String> home = getVariable(HOME_ENV).map(s -> s + File.separator + "Desktop");

        return takeFirstPath(xdgDesktop, home);
    }

    @Override
    public @NotNull Path documents() {
        final Optional<String> xdgDocuments = getVariable(XDG_DOCUMENTS_DIR);
        final Optional<String> home = getVariable(HOME_ENV).map(s -> s + File.separator + "Documents");

        return takeFirstPath(xdgDocuments, home);
    }

    @Override
    public @NotNull Path downloads() {
        final Optional<String> xdgDownloads = getVariable(XDG_DOWNLOAD_DIR);
        final Optional<String> home = getVariable(HOME_ENV).map(s -> s + File.separator + "Downloads");

        return takeFirstPath(xdgDownloads, home);
    }

    @Override
    public @NotNull Path music() {
        final Optional<String> xdgMusic = getVariable(XDG_MUSIC_DIR);
        final Optional<String> home = getVariable(HOME_ENV).map(s -> s + File.separator + "Music");

        return takeFirstPath(xdgMusic, home);
    }

    @Override
    public @NotNull Path pictures() {
        final Optional<String> xdgPictures = getVariable(XDG_PICTURES_DIR);
        final Optional<String> home = getVariable(HOME_ENV).map(s -> s + File.separator + "Pictures");

        return takeFirstPath(xdgPictures, home);
    }

    @Override
    public @NotNull Path videos() {
        final Optional<String> xdgVideos = getVariable(XDG_VIDEOS_DIR);
        final Optional<String> home = getVariable(HOME_ENV).map(s -> s + File.separator + "Videos");

        return takeFirstPath(xdgVideos, home);
    }
}
