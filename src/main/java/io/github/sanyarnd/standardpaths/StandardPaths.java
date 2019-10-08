package io.github.sanyarnd.standardpaths;

import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

/**
 * Collection of methods, which return paths to the most common system locations.
 *
 * <p>Paths in methods' documentation are examples.<br>
 * Internal implementation will always do its best utilizing system API and retrieve the real path.<br>
 *
 * @author Alexander Biryukov
 */
public final class StandardPaths {
    private static final LocationDelegate DELEGATE = getDelegate();

    private StandardPaths() { /* utility class */ }

    private static @NotNull LocationDelegate getDelegate() {
        switch (Os.current()) {
            case WINDOWS:
                return new WindowsLocations();
            case LINUX:
                return new LinuxLocations();
            case MAC:
                throw new IllegalArgumentException("MacOS is currently unsupported, please consider creating PR");
            default:
                throw new IllegalArgumentException("Unsupported OS: " + Os.current());
        }
    }

    /**
     * Directory which can use used to store runtime/cache data:
     * <ul>
     * <li>Win:  {@code %USERPROFILE%/AppData/Local};</li>
     * <li>Unix: {@code $XDG_CACHE_HOME} (default: {@code $HOME/.cache}).</li>
     * </ul>
     * Usually you'd like to invoke {@code resolve("<appname>")} on return value to create subdirectory.
     *
     * @return path to runtime/cache directory
     * @throws NoSuchPathException if it's impossible to acquire path to directory
     */
    public static @NotNull Path cache() {
        return DELEGATE.cache();
    }

    /**
     * Directory which can use used to store configuration data:
     * <ul>
     * <li>Win:  {@code %USERPROFILE%/AppData/Local};</li>
     * <li>Unix: {@code $XDG_CONFIG_HOME} (default: {@code $HOME/.config}).</li>
     * </ul>
     * Usually you'd like to invoke {@code resolve("<appname>")} on return value to create subdirectory.
     *
     * @return path to config directory
     * @throws NoSuchPathException if it's impossible to acquire path to directory
     */
    public static @NotNull Path config() {
        return DELEGATE.config();
    }

    /**
     * Directory which can use used to store data:
     * <ul>
     * <li>Win:  {@code %USERPROFILE%/AppData/Roaming};</li>
     * <li>Unix: {@code $HOME/.local/share}.</li>
     * </ul>
     * Note that Windows will sync changes with domain server.
     * If not desired, Consider using {@link #dataLocal()} instead.
     *
     * <p>Usually you'd like to invoke {@code resolve("<appname>")} on return value to create subdirectory.
     *
     * @return path to config directory
     * @throws NoSuchPathException if it's impossible to acquire path to directory
     */
    public static @NotNull Path data() {
        return DELEGATE.data();
    }

    /**
     * Directory which can use used to store data:
     * <ul>
     * <li>Win:  {@code %USERPROFILE%/AppData/Local};</li>
     * <li>Unix: {@code $HOME/.local/share}.</li>
     * </ul>
     * Usually you'd like to invoke {@code resolve("<appname>")} on return value to create subdirectory.
     *
     * @return path to config directory
     * @throws NoSuchPathException if it's impossible to acquire path to directory
     */
    public static @NotNull Path dataLocal() {
        return DELEGATE.dataLocal();
    }

    /**
     * Temp files directory:
     * <ul>
     * <li>Win:  {@code %USERPROFILE%/AppData/Local/Temp};</li>
     * <li>Unix: {@code /tmp}.</li>
     * </ul>
     *
     * @return path to home directory
     * @throws NoSuchPathException if it's impossible to acquire path to directory
     */
    public static @NotNull Path temp() {
        return DELEGATE.temp();
    }

    /**
     * Current user Home directory:
     * <ul>
     * <li>Win:  {@code %USERPROFILE%};</li>
     * <li>Unix: {@code ~}.</li>
     * </ul>
     *
     * @return path to home directory
     * @throws NoSuchPathException if it's impossible to acquire path to directory
     */
    public static @NotNull Path home() {
        return DELEGATE.home();
    }

    /**
     * Desktop directory:
     * <ul>
     * <li>Win:  {@code %USERPROFILE%/Desktop};</li>
     * <li>Unix: {@code ~/Desktop}.</li>
     * </ul>
     *
     * @return path to desktop directory
     * @throws NoSuchPathException if it's impossible to acquire path to directory
     */
    public static @NotNull Path desktop() {
        return DELEGATE.desktop();
    }

    /**
     * Documents directory:
     * <ul>
     * <li>Win:  {@code %USERPROFILE%/Documents};</li>
     * <li>Unix: {@code ~/Documents}.</li>
     * </ul>
     *
     * @return path to documents directory
     * @throws NoSuchPathException if it's impossible to acquire path to directory
     */
    public static @NotNull Path documents() {
        return DELEGATE.documents();
    }

    /**
     * Downloads directory:
     * <ul>
     * <li>Win:  {@code %USERPROFILE%/Downloads};</li>
     * <li>Unix: {@code ~/Downloads}.</li>
     * </ul>
     *
     * @return path to downloads directory
     * @throws NoSuchPathException if it's impossible to acquire path to directory
     */
    public static @NotNull Path downloads() {
        return DELEGATE.downloads();
    }

    /**
     * Music directory:
     * <ul>
     * <li>Win:  {@code %USERPROFILE%/Music};</li>
     * <li>Unix: {@code ~/Music}.</li>
     * </ul>
     *
     * @return path to music directory
     * @throws NoSuchPathException if it's impossible to acquire path to directory
     */
    public static @NotNull Path music() {
        return DELEGATE.music();
    }

    /**
     * Pictures directory:
     * <ul>
     * <li>Win:  {@code %USERPROFILE%/Pictures};</li>
     * <li>Unix: {@code ~/Pictures}.</li>
     * </ul>
     *
     * @return path to pictures directory
     * @throws NoSuchPathException if it's impossible to acquire path to directory
     */
    public static @NotNull Path pictures() {
        return DELEGATE.pictures();
    }

    /**
     * Videos directory:
     * <ul>
     * <li>Win:  {@code %USERPROFILE%/Videos};</li>
     * <li>Unix: {@code ~/Videos}.</li>
     * </ul>
     *
     * @return path to videos directory
     * @throws NoSuchPathException if it's impossible to acquire path to directory
     */
    public static @NotNull Path videos() {
        return DELEGATE.videos();
    }
}
