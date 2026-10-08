package io.github.sanyarnd.standardpaths;

import java.nio.file.Path;

/// Collection of methods, which return paths to the most common system locations.
///
/// Paths in the documentation are examples: the implementation asks the system (WinAPI, XDG configuration, etc.) and
/// returns the real path. Returned paths are absolute, but the directories are not guaranteed to exist.
///
/// Usually you'd like to invoke `resolve("<appname>")` on the returned path to get an application subdirectory.
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
                return new WindowsLocations(new JnaWindowsApi(), environment);
            case MAC:
                return new MacLocations(environment);
            case UNIX:
                return new UnixLocations(environment);
        }
        throw new AssertionError("Unknown OS: " + os);
    }

    /// Directory for the non-essential (cached) data:
    /// - Windows: `%LOCALAPPDATA%` (`%USERPROFILE%\AppData\Local`);
    /// - macOS: `$HOME/Library/Caches`;
    /// - Linux: `$XDG_CACHE_HOME` (default: `$HOME/.cache`).
    ///
    /// @return path to the cache directory
    /// @throws NoSuchPathException if it's impossible to determine the path
    public static Path cache() {
        return delegate().cache();
    }

    /// Directory for the configuration files:
    /// - Windows: `%LOCALAPPDATA%` (`%USERPROFILE%\AppData\Local`);
    /// - macOS: `$HOME/Library/Application Support`;
    /// - Linux: `$XDG_CONFIG_HOME` (default: `$HOME/.config`).
    ///
    /// @return path to the config directory
    /// @throws NoSuchPathException if it's impossible to determine the path
    public static Path config() {
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
    /// @return path to the data directory
    /// @throws NoSuchPathException if it's impossible to determine the path
    public static Path data() {
        return delegate().data();
    }

    /// Directory for the application data, which is never synchronized:
    /// - Windows: `%LOCALAPPDATA%` (`%USERPROFILE%\AppData\Local`);
    /// - macOS: `$HOME/Library/Application Support`;
    /// - Linux: `$XDG_DATA_HOME` (default: `$HOME/.local/share`).
    ///
    /// @return path to the local data directory
    /// @throws NoSuchPathException if it's impossible to determine the path
    public static Path dataLocal() {
        return delegate().dataLocal();
    }

    /// Directory for the temporary files:
    /// - Windows: `GetTempPath` (usually `%USERPROFILE%\AppData\Local\Temp`);
    /// - macOS, Linux: `$TMPDIR` (default: `java.io.tmpdir`, then `/tmp`).
    ///
    /// @return path to the temp directory
    /// @throws NoSuchPathException if it's impossible to determine the path
    public static Path temp() {
        return delegate().temp();
    }

    /// Current user home directory:
    /// - Windows: `%USERPROFILE%`;
    /// - macOS, Linux: `$HOME` (default: `user.home`).
    ///
    /// @return path to the home directory
    /// @throws NoSuchPathException if it's impossible to determine the path
    public static Path home() {
        return delegate().home();
    }

    /// Desktop directory:
    /// - Windows: `%USERPROFILE%\Desktop`;
    /// - macOS: `$HOME/Desktop`;
    /// - Linux: `$XDG_DESKTOP_DIR` (default: `$HOME/Desktop`).
    ///
    /// @return path to the desktop directory
    /// @throws NoSuchPathException if it's impossible to determine the path
    public static Path desktop() {
        return delegate().desktop();
    }

    /// Documents directory:
    /// - Windows: `%USERPROFILE%\Documents`;
    /// - macOS: `$HOME/Documents`;
    /// - Linux: `$XDG_DOCUMENTS_DIR` (default: `$HOME/Documents`).
    ///
    /// @return path to the documents directory
    /// @throws NoSuchPathException if it's impossible to determine the path
    public static Path documents() {
        return delegate().documents();
    }

    /// Downloads directory:
    /// - Windows: `%USERPROFILE%\Downloads`;
    /// - macOS: `$HOME/Downloads`;
    /// - Linux: `$XDG_DOWNLOAD_DIR` (default: `$HOME/Downloads`).
    ///
    /// @return path to the downloads directory
    /// @throws NoSuchPathException if it's impossible to determine the path
    public static Path downloads() {
        return delegate().downloads();
    }

    /// Music directory:
    /// - Windows: `%USERPROFILE%\Music`;
    /// - macOS: `$HOME/Music`;
    /// - Linux: `$XDG_MUSIC_DIR` (default: `$HOME/Music`).
    ///
    /// @return path to the music directory
    /// @throws NoSuchPathException if it's impossible to determine the path
    public static Path music() {
        return delegate().music();
    }

    /// Pictures directory:
    /// - Windows: `%USERPROFILE%\Pictures`;
    /// - macOS: `$HOME/Pictures`;
    /// - Linux: `$XDG_PICTURES_DIR` (default: `$HOME/Pictures`).
    ///
    /// @return path to the pictures directory
    /// @throws NoSuchPathException if it's impossible to determine the path
    public static Path pictures() {
        return delegate().pictures();
    }

    /// Videos directory:
    /// - Windows: `%USERPROFILE%\Videos`;
    /// - macOS: `$HOME/Movies`;
    /// - Linux: `$XDG_VIDEOS_DIR` (default: `$HOME/Videos`).
    ///
    /// @return path to the videos directory
    /// @throws NoSuchPathException if it's impossible to determine the path
    public static Path videos() {
        return delegate().videos();
    }

    // lazy initialization, the delegate is created on the first call
    private static final class Holder {
        static final LocationDelegate DELEGATE = create(Os.current(), Environment.SYSTEM);
    }
}
