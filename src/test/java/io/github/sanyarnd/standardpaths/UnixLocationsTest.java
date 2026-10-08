package io.github.sanyarnd.standardpaths;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;
import org.instancio.junit.Given;
import org.instancio.junit.InstancioExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

@ExtendWith(InstancioExtension.class)
class UnixLocationsTest {
    @TempDir
    Path tempDir;

    private Path home;
    private FakeEnvironment env;
    private UnixLocations locations;

    @BeforeEach
    void setUp() {
        home = tempDir.resolve("home");
        env = new FakeEnvironment().env("HOME", home);
        locations = new UnixLocations(env);
    }

    static Stream<Arguments> baseDirs() {
        return Stream.of(
                Arguments.of(Location.CACHE, "XDG_CACHE_HOME", ".cache"),
                Arguments.of(Location.CONFIG, "XDG_CONFIG_HOME", ".config"),
                Arguments.of(Location.DATA, "XDG_DATA_HOME", ".local/share"),
                Arguments.of(Location.DATA_LOCAL, "XDG_DATA_HOME", ".local/share"));
    }

    static Stream<Arguments> userDirs() {
        return Stream.of(
                Arguments.of(Location.DESKTOP, "XDG_DESKTOP_DIR", "Desktop"),
                Arguments.of(Location.DOCUMENTS, "XDG_DOCUMENTS_DIR", "Documents"),
                Arguments.of(Location.DOWNLOADS, "XDG_DOWNLOAD_DIR", "Downloads"),
                Arguments.of(Location.MUSIC, "XDG_MUSIC_DIR", "Music"),
                Arguments.of(Location.PICTURES, "XDG_PICTURES_DIR", "Pictures"),
                Arguments.of(Location.VIDEOS, "XDG_VIDEOS_DIR", "Videos"));
    }

    static Stream<Arguments> allDirs() {
        return Stream.concat(baseDirs(), userDirs());
    }

    private void writeUserDirs(final Path configDir, final String... lines) throws IOException {
        Files.createDirectories(configDir);
        Files.write(configDir.resolve(UserDirs.FILE_NAME), List.of(lines), StandardCharsets.UTF_8);
    }

    @ParameterizedTest
    @MethodSource("allDirs")
    void defaultsToHomeSubdirectory(final Location location, final String variable, final String fallback) {
        assertThat(location.of(locations)).isEqualTo(home.resolve(fallback));
    }

    @ParameterizedTest
    @MethodSource("allDirs")
    void absoluteVariableWins(final Location location, final String variable, final String fallback) {
        final Path custom = tempDir.resolve("custom");
        env.env(variable, custom);

        assertThat(location.of(locations)).isEqualTo(custom);
    }

    @ParameterizedTest
    @MethodSource("allDirs")
    void relativeVariableIsIgnored(final Location location, final String variable, final String fallback) {
        env.env(variable, "relative/dir");

        assertThat(location.of(locations)).isEqualTo(home.resolve(fallback));
    }

    @ParameterizedTest
    @MethodSource("allDirs")
    void blankVariableIsIgnored(final Location location, final String variable, final String fallback) {
        env.env(variable, "  ");

        assertThat(location.of(locations)).isEqualTo(home.resolve(fallback));
    }

    @Test
    void dataLocalIsData() {
        env.env("XDG_DATA_HOME", tempDir.resolve("data"));

        assertThat(locations.dataLocal()).isEqualTo(locations.data());
    }

    @Test
    void homeIsNotTilde() {
        // the old implementation returned relative "~" path
        final UnixLocations noHome = new UnixLocations(new FakeEnvironment().property("user.home", home));

        assertThat(noHome.home()).isEqualTo(home).isAbsolute();
    }

    @ParameterizedTest
    @MethodSource("userDirs")
    void userDirFromDefaultConfig(final Location location, final String key, final String fallback) throws IOException {
        writeUserDirs(home.resolve(".config"), key + "=\"$HOME/Custom " + fallback + "\"");

        assertThat(location.of(locations)).isEqualTo(home.resolve("Custom " + fallback));
    }

    @ParameterizedTest
    @MethodSource("userDirs")
    void userDirFromXdgConfigHome(final Location location, final String key, final String fallback) throws IOException {
        final Path config = tempDir.resolve("config");
        env.env("XDG_CONFIG_HOME", config);
        writeUserDirs(config, key + "=\"$HOME/Custom\"");
        writeUserDirs(home.resolve(".config"), key + "=\"$HOME/Ignored\"");

        assertThat(location.of(locations)).isEqualTo(home.resolve("Custom"));
    }

    @ParameterizedTest
    @MethodSource("userDirs")
    void variableWinsOverConfig(final Location location, final String key, final String fallback) throws IOException {
        final Path custom = tempDir.resolve("custom");
        env.env(key, custom);
        writeUserDirs(home.resolve(".config"), key + "=\"$HOME/Ignored\"");

        assertThat(location.of(locations)).isEqualTo(custom);
    }

    @ParameterizedTest
    @MethodSource("userDirs")
    void missingConfigEntryFallsBackToDefault(final Location location, final String key, final String fallback)
            throws IOException {
        writeUserDirs(home.resolve(".config"), "XDG_TEMPLATES_DIR=\"$HOME/Templates\"");

        assertThat(location.of(locations)).isEqualTo(home.resolve(fallback));
    }

    @ParameterizedTest
    @CsvSource({"DESKTOP, XDG_DESKTOP_DIR", "DOWNLOADS, XDG_DOWNLOAD_DIR"})
    void disabledUserDirPointsToHome(final Location location, final String key) throws IOException {
        writeUserDirs(home.resolve(".config"), key + "=\"$HOME/\"");

        assertThat(location.of(locations)).isEqualTo(home);
    }

    @RepeatedTest(5)
    void readsWholeConfig(
            @Given final String desktop,
            @Given final String documents,
            @Given final String downloads,
            @Given final String music,
            @Given final String pictures,
            @Given final String videos)
            throws IOException {
        writeUserDirs(
                home.resolve(".config"),
                "XDG_DESKTOP_DIR=\"$HOME/" + desktop + "\"",
                "XDG_DOCUMENTS_DIR=\"$HOME/" + documents + "\"",
                "XDG_DOWNLOAD_DIR=\"$HOME/" + downloads + "\"",
                "XDG_MUSIC_DIR=\"$HOME/" + music + "\"",
                "XDG_PICTURES_DIR=\"$HOME/" + pictures + "\"",
                "XDG_VIDEOS_DIR=\"$HOME/" + videos + "\"");

        assertThat(locations.desktop()).isEqualTo(home.resolve(desktop));
        assertThat(locations.documents()).isEqualTo(home.resolve(documents));
        assertThat(locations.downloads()).isEqualTo(home.resolve(downloads));
        assertThat(locations.music()).isEqualTo(home.resolve(music));
        assertThat(locations.pictures()).isEqualTo(home.resolve(pictures));
        assertThat(locations.videos()).isEqualTo(home.resolve(videos));
    }
}
