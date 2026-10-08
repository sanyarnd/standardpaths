package io.github.sanyarnd.standardpaths;

import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jspecify.annotations.Nullable;

/// Windows locations.
///
/// Application data directories are read from the environment, user directories (Desktop, Documents, etc.) are read
/// from the registry, which is queried once.
///
/// @author Alexander Biryukov
final class WindowsLocations implements LocationDelegate {
    private static final Pattern VARIABLE = Pattern.compile("%([^%]+)%");

    private final WindowsRegistry registry;
    private final Environment env;
    private volatile @Nullable Map<String, String> shellFolders;

    WindowsLocations(final WindowsRegistry windowsRegistry, final Environment environment) {
        registry = windowsRegistry;
        env = environment;
    }

    @Override
    public Optional<Path> home() {
        return env.propertyPath("user.home").or(() -> env.envPath("USERPROFILE"));
    }

    @Override
    public Optional<Path> temp() {
        return env.propertyPath("java.io.tmpdir").or(() -> env.envPath("TEMP")).or(() -> env.envPath("TMP"));
    }

    @Override
    public Optional<Path> cache() {
        return localAppData();
    }

    @Override
    public Optional<Path> config() {
        return roamingAppData();
    }

    @Override
    public Optional<Path> data() {
        return roamingAppData();
    }

    @Override
    public Optional<Path> dataLocal() {
        return localAppData();
    }

    @Override
    public Optional<Path> state() {
        return localAppData();
    }

    @Override
    public Optional<Path> runtime() {
        return Optional.empty();
    }

    @Override
    public Optional<Path> desktop() {
        return userDir("Desktop", "Desktop");
    }

    @Override
    public Optional<Path> documents() {
        return userDir("Personal", "Documents");
    }

    @Override
    public Optional<Path> downloads() {
        return userDir("{374DE290-123F-4565-9164-39C4925E467B}", "Downloads");
    }

    @Override
    public Optional<Path> music() {
        return userDir("My Music", "Music");
    }

    @Override
    public Optional<Path> pictures() {
        return userDir("My Pictures", "Pictures");
    }

    @Override
    public Optional<Path> videos() {
        return userDir("My Video", "Videos");
    }

    @Override
    public Optional<Path> templates() {
        return shellFolder("Templates")
                .or(() -> roamingAppData()
                        .map(path ->
                                path.resolve("Microsoft").resolve("Windows").resolve("Templates")));
    }

    @Override
    public Optional<Path> publicShare() {
        return env.envPath("PUBLIC");
    }

    // local and roaming directories are shared by different kinds of data
    @Override
    public Optional<Path> appDir(final Optional<Path> base, final String app, final String kind) {
        return base.map(path -> path.resolve(app).resolve(kind));
    }

    private Optional<Path> localAppData() {
        return env.envPath("LOCALAPPDATA")
                .or(() -> shellFolder("Local AppData"))
                .or(() -> inHome("AppData").map(path -> path.resolve("Local")));
    }

    private Optional<Path> roamingAppData() {
        return env.envPath("APPDATA")
                .or(() -> shellFolder("AppData"))
                .or(() -> inHome("AppData").map(path -> path.resolve("Roaming")));
    }

    private Optional<Path> userDir(final String valueName, final String fallback) {
        return shellFolder(valueName).or(() -> inHome(fallback));
    }

    private Optional<Path> inHome(final String relative) {
        return home().map(path -> path.resolve(relative));
    }

    private Optional<Path> shellFolder(final String valueName) {
        final String value = shellFolders().get(valueName);
        return value == null ? Optional.empty() : Environment.absolutePath(expand(value));
    }

    private Map<String, String> shellFolders() {
        Map<String, String> result = shellFolders;
        if (result == null) {
            result = registry.userShellFolders();
            shellFolders = result;
        }
        return result;
    }

    // expands %VARIABLE% references, unknown variables are kept as is
    private String expand(final String value) {
        final Matcher matcher = VARIABLE.matcher(value);
        final StringBuilder sb = new StringBuilder();
        int last = 0;
        while (matcher.find()) {
            final String replacement = env.getenv(matcher.group(1));
            sb.append(value, last, matcher.start()).append(replacement == null ? matcher.group() : replacement);
            last = matcher.end();
        }
        return sb.append(value.substring(last)).toString();
    }
}
