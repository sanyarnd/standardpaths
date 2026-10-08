package io.github.sanyarnd.standardpaths;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/// Parser of the `user-dirs.dirs` file created by `xdg-user-dirs-update`.
///
/// Every entry has the form `XDG_xxx_DIR="$HOME/yyy"` or `XDG_xxx_DIR="/yyy"`.
///
/// @author Alexander Biryukov
final class UserDirs {
    static final String FILE_NAME = "user-dirs.dirs";

    private static final Pattern ENTRY = Pattern.compile("^\\s*(XDG_[A-Z_]+_DIR)\\s*=\\s*\"(.*)\"\\s*$");
    private static final String HOME_PREFIX = "$HOME";

    private final Map<String, Path> dirs;

    private UserDirs(final Map<String, Path> userDirs) {
        dirs = userDirs;
    }

    /// Reads the file, missing or unreadable file is treated as empty.
    ///
    /// @param file path to `user-dirs.dirs`
    /// @param home user home directory, used to expand `$HOME`
    /// @return parsed directories
    static UserDirs read(final Path file, final Path home) {
        try {
            return parse(Files.readAllLines(file, StandardCharsets.UTF_8), home);
        } catch (IOException e) {
            return new UserDirs(Map.of());
        }
    }

    /// Parses the file content, malformed lines are ignored.
    ///
    /// @param lines file content
    /// @param home user home directory, used to expand `$HOME`
    /// @return parsed directories
    static UserDirs parse(final List<String> lines, final Path home) {
        final Map<String, Path> result = new HashMap<>();
        for (final String line : lines) {
            final Matcher matcher = ENTRY.matcher(line);
            if (matcher.matches()) {
                toPath(unescape(matcher.group(2)), home).ifPresent(path -> result.put(matcher.group(1), path));
            }
        }
        return new UserDirs(result);
    }

    /// Directory for the key.
    ///
    /// @param key variable name, e.g. `XDG_DESKTOP_DIR`
    /// @return directory or empty if not defined
    Optional<Path> get(final String key) {
        return Optional.ofNullable(dirs.get(key));
    }

    private static Optional<Path> toPath(final String value, final Path home) {
        if (value.equals(HOME_PREFIX) || value.startsWith(HOME_PREFIX + "/")) {
            final String relative = value.substring(HOME_PREFIX.length()).replaceFirst("^/+", "");
            try {
                return Optional.of(
                        relative.isEmpty() ? home : home.resolve(relative).normalize());
            } catch (InvalidPathException e) {
                return Optional.empty();
            }
        }
        return Environment.absolutePath(value);
    }

    private static String unescape(final String value) {
        final StringBuilder sb = new StringBuilder(value.length());
        for (int i = 0; i < value.length(); ++i) {
            final char c = value.charAt(i);
            if (c == '\\' && i + 1 < value.length()) {
                sb.append(value.charAt(++i));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
