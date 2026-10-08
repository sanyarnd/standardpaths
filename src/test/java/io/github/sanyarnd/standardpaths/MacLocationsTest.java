package io.github.sanyarnd.standardpaths;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Path;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;

class MacLocationsTest {
    private static final String[] XDG_VARIABLES = {
        "XDG_CACHE_HOME",
        "XDG_CONFIG_HOME",
        "XDG_DATA_HOME",
        "XDG_STATE_HOME",
        "XDG_RUNTIME_DIR",
        "XDG_DESKTOP_DIR",
        "XDG_DOCUMENTS_DIR",
        "XDG_DOWNLOAD_DIR",
        "XDG_MUSIC_DIR",
        "XDG_PICTURES_DIR",
        "XDG_VIDEOS_DIR",
        "XDG_TEMPLATES_DIR",
        "XDG_PUBLICSHARE_DIR"
    };

    @TempDir
    Path tempDir;

    private Path home;
    private FakeEnvironment env;
    private MacLocations locations;

    @BeforeEach
    void setUp() {
        home = tempDir.resolve("home");
        env = new FakeEnvironment().env("HOME", home);
        locations = new MacLocations(env);
    }

    @ParameterizedTest
    @CsvSource({
        "CACHE, Library/Caches",
        "CONFIG, Library/Application Support",
        "DATA, Library/Application Support",
        "DATA_LOCAL, Library/Application Support",
        "STATE, Library/Application Support",
        "DESKTOP, Desktop",
        "DOCUMENTS, Documents",
        "DOWNLOADS, Downloads",
        "MUSIC, Music",
        "PICTURES, Pictures",
        "VIDEOS, Movies",
        "PUBLIC_SHARE, Public"
    })
    void resolvesAgainstHome(final Location location, final String relative) {
        assertThat(location.of(locations)).contains(home.resolve(relative));
    }

    @ParameterizedTest
    @EnumSource(
            value = Location.class,
            names = {"RUNTIME", "TEMPLATES"})
    void hasNoDirectory(final Location location) {
        assertThat(location.of(locations)).isEmpty();
    }

    @ParameterizedTest
    @EnumSource(value = Location.class, mode = EnumSource.Mode.EXCLUDE, names = "TEMP")
    void ignoresXdgVariables(final Location location) {
        final Optional<Path> before = location.of(locations);
        for (final String variable : XDG_VARIABLES) {
            env.env(variable, tempDir.resolve(variable));
        }

        assertThat(location.of(locations)).isEqualTo(before);
    }
}
