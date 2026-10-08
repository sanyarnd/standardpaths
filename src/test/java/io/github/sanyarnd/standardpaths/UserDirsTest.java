package io.github.sanyarnd.standardpaths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThatCode;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.instancio.junit.Given;
import org.instancio.junit.InstancioExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@ExtendWith(InstancioExtension.class)
class UserDirsTest {
    @TempDir
    Path tempDir;

    private Path home;

    @BeforeEach
    void setUp() {
        home = tempDir.resolve("home");
    }

    private UserDirs parse(final String... lines) {
        return UserDirs.parse(List.of(lines), home);
    }

    // backslashes of Windows paths must be escaped in the file
    private static String escape(final Path path) {
        return path.toString().replace("\\", "\\\\");
    }

    @Test
    void parsesFileGeneratedByXdgUserDirsUpdate() {
        final UserDirs dirs = parse(
                "# This file is written by xdg-user-dirs-update",
                "# If you want to change or add directories, just edit the line you're",
                "# interested in. All local changes will be retained on the next run.",
                "# Format is XDG_xxx_DIR=\"$HOME/yyy\", where yyy is a shell-escaped",
                "# homedir-relative path, or XDG_xxx_DIR=\"/yyy\", where /yyy is an",
                "# absolute path. No other format is supported.",
                "# ",
                "XDG_DESKTOP_DIR=\"$HOME/Desktop\"",
                "XDG_DOWNLOAD_DIR=\"$HOME/Downloads\"",
                "XDG_TEMPLATES_DIR=\"$HOME/Templates\"",
                "XDG_PUBLICSHARE_DIR=\"$HOME/Public\"",
                "XDG_DOCUMENTS_DIR=\"$HOME/Documents\"",
                "XDG_MUSIC_DIR=\"$HOME/Music\"",
                "XDG_PICTURES_DIR=\"$HOME/Pictures\"",
                "XDG_VIDEOS_DIR=\"$HOME/Videos\"");

        assertThat(dirs.get("XDG_DESKTOP_DIR")).contains(home.resolve("Desktop"));
        assertThat(dirs.get("XDG_DOWNLOAD_DIR")).contains(home.resolve("Downloads"));
        assertThat(dirs.get("XDG_TEMPLATES_DIR")).contains(home.resolve("Templates"));
        assertThat(dirs.get("XDG_PUBLICSHARE_DIR")).contains(home.resolve("Public"));
        assertThat(dirs.get("XDG_DOCUMENTS_DIR")).contains(home.resolve("Documents"));
        assertThat(dirs.get("XDG_MUSIC_DIR")).contains(home.resolve("Music"));
        assertThat(dirs.get("XDG_PICTURES_DIR")).contains(home.resolve("Pictures"));
        assertThat(dirs.get("XDG_VIDEOS_DIR")).contains(home.resolve("Videos"));
    }

    // non-ASCII file names are not supported by the JVM with POSIX locale
    private static void assumeUnicodeFileNames() {
        assumeThatCode(() -> Paths.get("Музыка")).doesNotThrowAnyException();
    }

    @RepeatedTest(5)
    void parsesLocalizedNames(@Given final String name) {
        assumeUnicodeFileNames();
        final UserDirs dirs = parse("XDG_MUSIC_DIR=\"$HOME/Музыка/" + name + "\"");

        assertThat(dirs.get("XDG_MUSIC_DIR")).contains(home.resolve("Музыка").resolve(name));
    }

    @Test
    void parsesAbsolutePath() {
        final Path music = tempDir.resolve("music");

        final UserDirs dirs = parse("XDG_MUSIC_DIR=\"" + escape(music) + "\"");

        assertThat(dirs.get("XDG_MUSIC_DIR")).contains(music);
    }

    @ParameterizedTest
    @ValueSource(strings = {"$HOME", "$HOME/", "$HOME//"})
    void disabledDirectoryPointsToHome(final String value) {
        final UserDirs dirs = parse("XDG_DESKTOP_DIR=\"" + value + "\"");

        assertThat(dirs.get("XDG_DESKTOP_DIR")).contains(home);
    }

    @Test
    void unescapesShellEscapedCharacters() {
        final UserDirs dirs = parse("XDG_MUSIC_DIR=\"$HOME/\\$My\\ Music\"");

        assertThat(dirs.get("XDG_MUSIC_DIR")).contains(home.resolve("$My Music"));
    }

    @Test
    void preservesSpaces() {
        final UserDirs dirs = parse("XDG_MUSIC_DIR=\"$HOME/My Music\"");

        assertThat(dirs.get("XDG_MUSIC_DIR")).contains(home.resolve("My Music"));
    }

    @Test
    void toleratesSurroundingWhitespace() {
        final UserDirs dirs = parse("  XDG_MUSIC_DIR = \"$HOME/Music\"  ");

        assertThat(dirs.get("XDG_MUSIC_DIR")).contains(home.resolve("Music"));
    }

    @Test
    void normalizesPath() {
        final UserDirs dirs = parse("XDG_MUSIC_DIR=\"$HOME/a/../Music\"");

        assertThat(dirs.get("XDG_MUSIC_DIR")).contains(home.resolve("Music"));
    }

    @Test
    void lastEntryWins() {
        final UserDirs dirs = parse("XDG_MUSIC_DIR=\"$HOME/Old\"", "XDG_MUSIC_DIR=\"$HOME/New\"");

        assertThat(dirs.get("XDG_MUSIC_DIR")).contains(home.resolve("New"));
    }

    @ParameterizedTest
    @ValueSource(
            strings = {
                "XDG_MUSIC_DIR=\"relative/Music\"",
                "XDG_MUSIC_DIR=\"$HOMEMusic\"",
                "XDG_MUSIC_DIR=\"~/Music\"",
                "XDG_MUSIC_DIR=\"$XDG_DATA_HOME/Music\"",
                "XDG_MUSIC_DIR=\"\"",
                "XDG_MUSIC_DIR=$HOME/Music",
                "XDG_MUSIC_DIR=\"$HOME/Music",
                "XDG_MUSIC_DIR=\"$HOME/\u0000\"",
                "# XDG_MUSIC_DIR=\"$HOME/Music\"",
                "export XDG_MUSIC_DIR=\"$HOME/Music\"",
                "xdg_music_dir=\"$HOME/Music\"",
                "MUSIC_DIR=\"$HOME/Music\"",
                ""
            })
    void ignoresUnsupportedLines(final String line) {
        assertThat(parse(line).get("XDG_MUSIC_DIR")).isEmpty();
    }

    @RepeatedTest(5)
    void missingKeyIsEmpty(@Given final String key) {
        assertThat(parse("XDG_MUSIC_DIR=\"$HOME/Music\"").get(key)).isEmpty();
    }

    @Test
    void readsFile() throws IOException {
        assumeUnicodeFileNames();
        final Path file = tempDir.resolve(UserDirs.FILE_NAME);
        Files.write(file, List.of("XDG_VIDEOS_DIR=\"$HOME/Видео\""), StandardCharsets.UTF_8);

        assertThat(UserDirs.read(file, home).get("XDG_VIDEOS_DIR")).contains(home.resolve("Видео"));
    }

    @Test
    void missingFileIsEmpty() {
        assertThat(UserDirs.read(tempDir.resolve("missing"), home).get("XDG_VIDEOS_DIR"))
                .isEmpty();
    }

    @Test
    void directoryInsteadOfFileIsEmpty() {
        assertThat(UserDirs.read(tempDir, home).get("XDG_VIDEOS_DIR")).isEmpty();
    }

    @Test
    void malformedEncodingIsEmpty() throws IOException {
        final Path file = tempDir.resolve(UserDirs.FILE_NAME);
        Files.write(file, new byte[] {'X', (byte) 0xC3, (byte) 0x28});

        assertThat(UserDirs.read(file, home).get("XDG_VIDEOS_DIR")).isEmpty();
    }
}
