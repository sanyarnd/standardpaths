package io.github.sanyarnd.standardpaths;

import java.nio.file.Path;
import java.util.Optional;

/// Windows locations.
///
/// @author Alexander Biryukov
final class WindowsLocations implements LocationDelegate {
    private final WindowsApi api;
    private final Environment env;

    WindowsLocations(final WindowsApi windowsApi, final Environment environment) {
        api = windowsApi;
        env = environment;
    }

    @Override
    public Path cache() {
        return api.knownFolder(KnownFolder.LOCAL_APP_DATA);
    }

    @Override
    public Path config() {
        return api.knownFolder(KnownFolder.LOCAL_APP_DATA);
    }

    @Override
    public Path data() {
        return api.knownFolder(KnownFolder.ROAMING_APP_DATA);
    }

    @Override
    public Path dataLocal() {
        return api.knownFolder(KnownFolder.LOCAL_APP_DATA);
    }

    @Override
    public Path temp() {
        try {
            return api.tempDirectory();
        } catch (NoSuchPathException e) {
            return env.envPath("TEMP")
                    .or(() -> env.envPath("TMP"))
                    .or(() -> env.propertyPath("java.io.tmpdir"))
                    .orElseThrow(() -> e);
        }
    }

    @Override
    public Path home() {
        try {
            return api.knownFolder(KnownFolder.PROFILE);
        } catch (NoSuchPathException e) {
            return env.envPath("USERPROFILE")
                    .or(this::homeDrivePath)
                    .or(() -> env.propertyPath("user.home"))
                    .orElseThrow(() -> e);
        }
    }

    @Override
    public Path desktop() {
        return api.knownFolder(KnownFolder.DESKTOP);
    }

    @Override
    public Path documents() {
        return api.knownFolder(KnownFolder.DOCUMENTS);
    }

    @Override
    public Path downloads() {
        return api.knownFolder(KnownFolder.DOWNLOADS);
    }

    @Override
    public Path music() {
        return api.knownFolder(KnownFolder.MUSIC);
    }

    @Override
    public Path pictures() {
        return api.knownFolder(KnownFolder.PICTURES);
    }

    @Override
    public Path videos() {
        return api.knownFolder(KnownFolder.VIDEOS);
    }

    private Optional<Path> homeDrivePath() {
        final String drive = env.getenv("HOMEDRIVE");
        final String path = env.getenv("HOMEPATH");
        if (drive == null || path == null) {
            return Optional.empty();
        }
        return Environment.absolutePath(drive + path);
    }
}
