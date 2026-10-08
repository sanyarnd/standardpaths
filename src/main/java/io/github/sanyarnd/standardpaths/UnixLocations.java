package io.github.sanyarnd.standardpaths;

import java.nio.file.Path;
import java.util.Optional;

/// Linux and other Unix-like systems locations.
///
/// Follows the [XDG Base Directory Specification](https://specifications.freedesktop.org/basedir-spec/latest/)
/// and [xdg-user-dirs](https://www.freedesktop.org/wiki/Software/xdg-user-dirs/).
///
/// @author Alexander Biryukov
final class UnixLocations extends PosixLocations {
    UnixLocations(final Environment environment) {
        super(environment);
    }

    @Override
    public Optional<Path> cache() {
        return baseDir("XDG_CACHE_HOME", ".cache");
    }

    @Override
    public Optional<Path> config() {
        return baseDir("XDG_CONFIG_HOME", ".config");
    }

    @Override
    public Optional<Path> data() {
        return baseDir("XDG_DATA_HOME", ".local/share");
    }

    @Override
    public Optional<Path> dataLocal() {
        return data();
    }

    @Override
    public Optional<Path> state() {
        return baseDir("XDG_STATE_HOME", ".local/state");
    }

    // the specification has no default value
    @Override
    public Optional<Path> runtime() {
        return env.envPath("XDG_RUNTIME_DIR");
    }

    @Override
    public Optional<Path> desktop() {
        return userDir("XDG_DESKTOP_DIR", "Desktop");
    }

    @Override
    public Optional<Path> documents() {
        return userDir("XDG_DOCUMENTS_DIR", "Documents");
    }

    @Override
    public Optional<Path> downloads() {
        return userDir("XDG_DOWNLOAD_DIR", "Downloads");
    }

    @Override
    public Optional<Path> music() {
        return userDir("XDG_MUSIC_DIR", "Music");
    }

    @Override
    public Optional<Path> pictures() {
        return userDir("XDG_PICTURES_DIR", "Pictures");
    }

    @Override
    public Optional<Path> videos() {
        return userDir("XDG_VIDEOS_DIR", "Videos");
    }

    @Override
    public Optional<Path> templates() {
        return userDir("XDG_TEMPLATES_DIR", "Templates");
    }

    @Override
    public Optional<Path> publicShare() {
        return userDir("XDG_PUBLICSHARE_DIR", "Public");
    }

    // relative paths are invalid according to the specification and must be ignored
    private Optional<Path> baseDir(final String variable, final String fallback) {
        return env.envPath(variable).or(() -> inHome(fallback));
    }

    private Optional<Path> userDir(final String key, final String fallback) {
        return env.envPath(key).or(() -> userDirsFile(key)).or(() -> inHome(fallback));
    }

    private Optional<Path> userDirsFile(final String key) {
        final Optional<Path> home = home();
        final Optional<Path> config = config();
        if (home.isEmpty() || config.isEmpty()) {
            return Optional.empty();
        }
        return UserDirs.read(config.get().resolve(UserDirs.FILE_NAME), home.get())
                .get(key);
    }
}
