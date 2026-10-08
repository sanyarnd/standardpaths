package io.github.sanyarnd.standardpaths;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;

class MacLocationsTest {
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
        "DESKTOP, Desktop",
        "DOCUMENTS, Documents",
        "DOWNLOADS, Downloads",
        "MUSIC, Music",
        "PICTURES, Pictures",
        "VIDEOS, Movies"
    })
    void resolvesAgainstHome(final Location location, final String relative) {
        assertThat(location.of(locations)).isEqualTo(home.resolve(relative));
    }

    @ParameterizedTest
    @EnumSource(
            value = Location.class,
            names = {"CACHE", "CONFIG", "DATA", "DESKTOP", "DOCUMENTS", "DOWNLOADS", "MUSIC", "PICTURES", "VIDEOS"})
    void ignoresXdgVariables(final Location location) {
        final Path before = location.of(locations);
        for (final String variable : new String[] {
            "XDG_CACHE_HOME",
            "XDG_CONFIG_HOME",
            "XDG_DATA_HOME",
            "XDG_DESKTOP_DIR",
            "XDG_DOCUMENTS_DIR",
            "XDG_DOWNLOAD_DIR",
            "XDG_MUSIC_DIR",
            "XDG_PICTURES_DIR",
            "XDG_VIDEOS_DIR"
        }) {
            env.env(variable, tempDir.resolve(variable));
        }

        assertThat(location.of(locations)).isEqualTo(before);
    }
}
