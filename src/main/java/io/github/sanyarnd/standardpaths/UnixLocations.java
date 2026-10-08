package io.github.sanyarnd.standardpaths;

import java.nio.file.Path;

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
    public Path cache() {
        return baseDir("XDG_CACHE_HOME", ".cache");
    }

    @Override
    public Path config() {
        return baseDir("XDG_CONFIG_HOME", ".config");
    }

    @Override
    public Path data() {
        return baseDir("XDG_DATA_HOME", ".local/share");
    }

    @Override
    public Path dataLocal() {
        return data();
    }

    @Override
    public Path desktop() {
        return userDir("XDG_DESKTOP_DIR", "Desktop");
    }

    @Override
    public Path documents() {
        return userDir("XDG_DOCUMENTS_DIR", "Documents");
    }

    @Override
    public Path downloads() {
        return userDir("XDG_DOWNLOAD_DIR", "Downloads");
    }

    @Override
    public Path music() {
        return userDir("XDG_MUSIC_DIR", "Music");
    }

    @Override
    public Path pictures() {
        return userDir("XDG_PICTURES_DIR", "Pictures");
    }

    @Override
    public Path videos() {
        return userDir("XDG_VIDEOS_DIR", "Videos");
    }

    // relative paths are invalid according to the specification and must be ignored
    private Path baseDir(final String variable, final String fallback) {
        return env.envPath(variable).orElseGet(() -> home().resolve(fallback));
    }

    private Path userDir(final String key, final String fallback) {
        return env.envPath(key)
                .or(() -> UserDirs.read(config().resolve(UserDirs.FILE_NAME), home())
                        .get(key))
                .orElseGet(() -> home().resolve(fallback));
    }
}
