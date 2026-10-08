package io.github.sanyarnd.standardpaths;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Optional;

/// Collection of methods, which return paths to the most common system locations.
///
/// Paths in the documentation are examples: the implementation asks the system (environment, registry, XDG
/// configuration) and returns the real path. Returned paths are absolute, but the directories are not guaranteed to
/// exist. A method returns an empty value if the system has no such directory (e.g. [#runtime()] on Windows) or the
/// path can't be determined (e.g. `$HOME` is not set).
///
/// Use [#forApp(String)] to get directories of the particular application:
/// ```java
/// AppPaths app = StandardPaths.forApp("my-app");
/// Path config = app.config().orElseThrow();
/// ```
///
/// @author Alexander Biryukov
public final class StandardPaths {
    private StandardPaths() {}

    private static LocationDelegate delegate() {
        return Holder.DELEGATE;
    }

    static LocationDelegate create(final Os os, final Environment environment) {
        switch (os) {
            case WINDOWS:
                return new WindowsLocations(new RegQueryRegistry(environment), environment);
            case MAC:
                return new MacLocations(environment);
            case UNIX:
                return new UnixLocations(environment);
        }
        throw new AssertionError("Unknown OS: " + os);
    }

    /// Directories of the particular application.
    ///
    /// @param name application name, used as a directory name
    /// @return application directories
    /// @throws IllegalArgumentException if the name is blank or is not a valid single directory name
    public static AppPaths forApp(final String name) {
        return new AppPaths(delegate(), validateAppName(name));
    }

    static String validateAppName(final String name) {
        Objects.requireNonNull(name, "name");
        if (name.trim().isEmpty()
                || !name.equals(name.trim())
                || name.equals(".")
                || name.equals("..")
                || name.indexOf('/') >= 0
                || name.indexOf('\\') >= 0) {
            throw new IllegalArgumentException("Invalid application name: '" + name + "'");
        }
        try {
            final Path path = Paths.get(name);
            if (path.isAbsolute()
                    || path.getNameCount() != 1
                    || !path.toString().equals(name)) {
                throw new IllegalArgumentException("Invalid application name: '" + name + "'");
            }
        } catch (InvalidPathException e) {
            throw new IllegalArgumentException("Invalid application name: '" + name + "'", e);
        }
        return name;
    }

    /// Current user home directory:
    /// - Windows: `user.home` (`%USERPROFILE%`);
    /// - macOS, Linux: `$HOME` (default: `user.home`).
    ///
    /// @return path to the home directory, empty if it can't be determined
    public static Optional<Path> home() {
        return delegate().home();
    }

    /// Directory for the temporary files:
    /// - Windows: `java.io.tmpdir` (`GetTempPath`, usually `%LOCALAPPDATA%\Temp`);
    /// - macOS, Linux: `$TMPDIR` (default: `java.io.tmpdir`, then `/tmp`).
    ///
    /// @return path to the temp directory, empty if it can't be determined
    public static Optional<Path> temp() {
        return delegate().temp();
    }

    /// Directory for the non-essential (cached) data:
    /// - Windows: `%LOCALAPPDATA%` (`%USERPROFILE%\AppData\Local`);
    /// - macOS: `$HOME/Library/Caches`;
    /// - Linux: `$XDG_CACHE_HOME` (default: `$HOME/.cache`).
    ///
    /// @return path to the cache directory, empty if it can't be determined
    public static Optional<Path> cache() {
        return delegate().cache();
    }

    /// Directory for the configuration files:
    /// - Windows: `%APPDATA%` (`%USERPROFILE%\AppData\Roaming`);
    /// - macOS: `$HOME/Library/Application Support`;
    /// - Linux: `$XDG_CONFIG_HOME` (default: `$HOME/.config`).
    ///
    /// @return path to the config directory, empty if it can't be determined
    public static Optional<Path> config() {
        return delegate().config();
    }

    /// Directory for the application data:
    /// - Windows: `%APPDATA%` (`%USERPROFILE%\AppData\Roaming`);
    /// - macOS: `$HOME/Library/Application Support`;
    /// - Linux: `$XDG_DATA_HOME` (default: `$HOME/.local/share`).
    ///
    /// Note that Windows synchronizes the roaming directory with the domain server. If it's not desired, consider
    /// using [#dataLocal()] instead.
    ///
    /// @return path to the data directory, empty if it can't be determined
    public static Optional<Path> data() {
        return delegate().data();
    }

    /// Directory for the application data, which is never synchronized:
    /// - Windows: `%LOCALAPPDATA%` (`%USERPROFILE%\AppData\Local`);
    /// - macOS: `$HOME/Library/Application Support`;
    /// - Linux: `$XDG_DATA_HOME` (default: `$HOME/.local/share`).
    ///
    /// @return path to the local data directory, empty if it can't be determined
    public static Optional<Path> dataLocal() {
        return delegate().dataLocal();
    }

    /// Directory for the state data, which should persist between restarts, but is not important enough for
    /// [#data()] (logs, history, recently used files, etc.):
    /// - Windows: `%LOCALAPPDATA%` (`%USERPROFILE%\AppData\Local`);
    /// - macOS: `$HOME/Library/Application Support`;
    /// - Linux: `$XDG_STATE_HOME` (default: `$HOME/.local/state`).
    ///
    /// @return path to the state directory, empty if it can't be determined
    public static Optional<Path> state() {
        return delegate().state();
    }

    /// Directory for the runtime files (sockets, named pipes, etc.):
    /// - Windows: none;
    /// - macOS: none;
    /// - Linux: `$XDG_RUNTIME_DIR` (no default).
    ///
    /// @return path to the runtime directory, empty if it can't be determined
    public static Optional<Path> runtime() {
        return delegate().runtime();
    }

    /// Desktop directory:
    /// - Windows: `FOLDERID_Desktop` (`%USERPROFILE%\Desktop`);
    /// - macOS: `$HOME/Desktop`;
    /// - Linux: `XDG_DESKTOP_DIR` (default: `$HOME/Desktop`).
    ///
    /// @return path to the desktop directory, empty if it can't be determined
    public static Optional<Path> desktop() {
        return delegate().desktop();
    }

    /// Documents directory:
    /// - Windows: `FOLDERID_Documents` (`%USERPROFILE%\Documents`);
    /// - macOS: `$HOME/Documents`;
    /// - Linux: `XDG_DOCUMENTS_DIR` (default: `$HOME/Documents`).
    ///
    /// @return path to the documents directory, empty if it can't be determined
    public static Optional<Path> documents() {
        return delegate().documents();
    }

    /// Downloads directory:
    /// - Windows: `FOLDERID_Downloads` (`%USERPROFILE%\Downloads`);
    /// - macOS: `$HOME/Downloads`;
    /// - Linux: `XDG_DOWNLOAD_DIR` (default: `$HOME/Downloads`).
    ///
    /// @return path to the downloads directory, empty if it can't be determined
    public static Optional<Path> downloads() {
        return delegate().downloads();
    }

    /// Music directory:
    /// - Windows: `FOLDERID_Music` (`%USERPROFILE%\Music`);
    /// - macOS: `$HOME/Music`;
    /// - Linux: `XDG_MUSIC_DIR` (default: `$HOME/Music`).
    ///
    /// @return path to the music directory, empty if it can't be determined
    public static Optional<Path> music() {
        return delegate().music();
    }

    /// Pictures directory:
    /// - Windows: `FOLDERID_Pictures` (`%USERPROFILE%\Pictures`);
    /// - macOS: `$HOME/Pictures`;
    /// - Linux: `XDG_PICTURES_DIR` (default: `$HOME/Pictures`).
    ///
    /// @return path to the pictures directory, empty if it can't be determined
    public static Optional<Path> pictures() {
        return delegate().pictures();
    }

    /// Videos directory:
    /// - Windows: `FOLDERID_Videos` (`%USERPROFILE%\Videos`);
    /// - macOS: `$HOME/Movies`;
    /// - Linux: `XDG_VIDEOS_DIR` (default: `$HOME/Videos`).
    ///
    /// @return path to the videos directory, empty if it can't be determined
    public static Optional<Path> videos() {
        return delegate().videos();
    }

    /// Templates directory:
    /// - Windows: `FOLDERID_Templates` (`%APPDATA%\Microsoft\Windows\Templates`);
    /// - macOS: none;
    /// - Linux: `XDG_TEMPLATES_DIR` (default: `$HOME/Templates`).
    ///
    /// @return path to the templates directory, empty if it can't be determined
    public static Optional<Path> templates() {
        return delegate().templates();
    }

    /// Directory for the files shared with other users:
    /// - Windows: `%PUBLIC%` (`C:\Users\Public`);
    /// - macOS: `$HOME/Public`;
    /// - Linux: `XDG_PUBLICSHARE_DIR` (default: `$HOME/Public`).
    ///
    /// @return path to the public share directory, empty if it can't be determined
    public static Optional<Path> publicShare() {
        return delegate().publicShare();
    }

    // lazy initialization, the delegate is created on the first call
    private static final class Holder {
        static final LocationDelegate DELEGATE = create(Os.current(), Environment.SYSTEM);
    }
}
