package io.github.sanyarnd.standardpaths;

import java.util.Locale;

/// Operating system family.
///
/// @author Alexander Biryukov
enum Os {
    /// Microsoft Windows.
    WINDOWS,
    /// Apple macOS.
    MAC,
    /// Linux, BSD and other Unix-like systems, which follow the XDG Base Directory Specification.
    UNIX;

    /// Current operating system.
    ///
    /// @return current operating system
    static Os current() {
        return of(System.getProperty("os.name", ""));
    }

    /// Detects operating system by the `os.name` system property value.
    ///
    /// @param osName value of `os.name`
    /// @return operating system family, [#UNIX] if unknown
    static Os of(final String osName) {
        final String name = osName.toLowerCase(Locale.ROOT);
        if (name.startsWith("windows")) {
            return WINDOWS;
        }
        if (name.startsWith("mac") || name.startsWith("darwin")) {
            return MAC;
        }
        return UNIX;
    }
}
