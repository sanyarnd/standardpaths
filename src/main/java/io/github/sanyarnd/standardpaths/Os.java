package io.github.sanyarnd.standardpaths;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

/**
 * Operation system enumeration.
 *
 * @author Alexander Biryukov
 */
enum Os {
    /**
     * Microsoft Windows operating system.
     */
    WINDOWS,
    /**
     * Linux-based operating system.
     */
    LINUX,
    /**
     * Apple Macintosh operating system.
     */
    MAC,
    /**
     * Other OSes, also if `os.name` is not defined.
     */
    UNKNOWN;

    private static final String OS_NAME = System.getProperty("os.name").toLowerCase(Locale.ENGLISH);

    /**
     * Current OS.
     *
     * @return current OS
     */
    public static @NotNull Os current() {
        final Os win = OS_NAME.contains("win") ? WINDOWS : UNKNOWN;
        final Os nonLinux = OS_NAME.contains("mac") ? MAC : win;

        return OS_NAME.contains("linux") ? LINUX : nonLinux;
    }

    public static boolean isWindows() {
        return current() == WINDOWS;
    }

    public static boolean isLinux() {
        return current() == LINUX;
    }

    public static boolean isMac() {
        return current() == MAC;
    }

    public static boolean isUnknown() {
        return current() == UNKNOWN;
    }
}
