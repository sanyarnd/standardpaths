package io.github.sanyarnd.standardpaths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.io.File;
import java.nio.file.Path;
import org.instancio.junit.Given;
import org.instancio.junit.InstancioExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({MockitoExtension.class, InstancioExtension.class})
class WindowsLocationsTest {
    @TempDir
    Path tempDir;

    @Mock
    WindowsApi api;

    private FakeEnvironment env;
    private WindowsLocations locations;

    @BeforeEach
    void setUp() {
        env = spy(new FakeEnvironment());
        locations = new WindowsLocations(api, env);
    }

    @ParameterizedTest
    @CsvSource({
        "CACHE, LOCAL_APP_DATA",
        "CONFIG, LOCAL_APP_DATA",
        "DATA, ROAMING_APP_DATA",
        "DATA_LOCAL, LOCAL_APP_DATA",
        "HOME, PROFILE",
        "DESKTOP, DESKTOP",
        "DOCUMENTS, DOCUMENTS",
        "DOWNLOADS, DOWNLOADS",
        "MUSIC, MUSIC",
        "PICTURES, PICTURES",
        "VIDEOS, VIDEOS"
    })
    void usesKnownFolder(final Location location, final KnownFolder folder) {
        final Path path = tempDir.resolve(folder.name());
        when(api.knownFolder(folder)).thenReturn(path);

        assertThat(location.of(locations)).isEqualTo(path);
        verifyNoInteractions(env);
    }

    @ParameterizedTest
    @CsvSource({
        "CACHE, LOCAL_APP_DATA",
        "CONFIG, LOCAL_APP_DATA",
        "DATA, ROAMING_APP_DATA",
        "DATA_LOCAL, LOCAL_APP_DATA",
        "DESKTOP, DESKTOP",
        "DOCUMENTS, DOCUMENTS",
        "DOWNLOADS, DOWNLOADS",
        "MUSIC, MUSIC",
        "PICTURES, PICTURES",
        "VIDEOS, VIDEOS"
    })
    void propagatesKnownFolderFailure(final Location location, final KnownFolder folder) {
        final NoSuchPathException failure = new NoSuchPathException("failure");
        when(api.knownFolder(folder)).thenThrow(failure);

        assertThatThrownBy(() -> location.of(locations)).isSameAs(failure);
    }

    @Test
    void tempUsesSystemCall() {
        when(api.tempDirectory()).thenReturn(tempDir);

        assertThat(locations.temp()).isEqualTo(tempDir);
        verifyNoInteractions(env);
    }

    @Test
    void tempFallsBackToTempVariable() {
        when(api.tempDirectory()).thenThrow(new NoSuchPathException("failure"));
        env.env("TEMP", tempDir).env("TMP", tempDir.resolve("tmp"));

        assertThat(locations.temp()).isEqualTo(tempDir);
    }

    @Test
    void tempFallsBackToTmpVariable() {
        when(api.tempDirectory()).thenThrow(new NoSuchPathException("failure"));
        env.env("TEMP", "relative").env("TMP", tempDir);

        assertThat(locations.temp()).isEqualTo(tempDir);
    }

    @Test
    void tempFallsBackToProperty() {
        when(api.tempDirectory()).thenThrow(new NoSuchPathException("failure"));
        env.property("java.io.tmpdir", tempDir);

        assertThat(locations.temp()).isEqualTo(tempDir);
    }

    @Test
    void tempRethrowsSystemCallFailure() {
        final NoSuchPathException failure = new NoSuchPathException("failure");
        when(api.tempDirectory()).thenThrow(failure);

        assertThatThrownBy(locations::temp).isSameAs(failure);
    }

    @Test
    void homeFallsBackToUserProfile() {
        when(api.knownFolder(KnownFolder.PROFILE)).thenThrow(new NoSuchPathException("failure"));
        env.env("USERPROFILE", tempDir).env("HOMEPATH", tempDir.resolve("other"));

        assertThat(locations.home()).isEqualTo(tempDir);
    }

    @Test
    void homeFallsBackToHomeDriveAndPath() {
        when(api.knownFolder(KnownFolder.PROFILE)).thenThrow(new NoSuchPathException("failure"));
        final String full = tempDir.toString();
        final int split = full.indexOf(File.separatorChar, 1);
        env.env("HOMEDRIVE", full.substring(0, split)).env("HOMEPATH", full.substring(split));

        assertThat(locations.home()).isEqualTo(tempDir);
    }

    @Test
    void homeIgnoresHomePathWithoutDrive() {
        // the old implementation concatenated "null" strings
        when(api.knownFolder(KnownFolder.PROFILE)).thenThrow(new NoSuchPathException("failure"));
        env.env("HOMEPATH", tempDir).property("user.home", tempDir.resolve("home"));

        assertThat(locations.home()).isEqualTo(tempDir.resolve("home"));
    }

    @Test
    void homeFallsBackToProperty() {
        when(api.knownFolder(KnownFolder.PROFILE)).thenThrow(new NoSuchPathException("failure"));
        env.property("user.home", tempDir);

        assertThat(locations.home()).isEqualTo(tempDir);
    }

    @RepeatedTest(3)
    void homeRethrowsSystemCallFailure(@Given final String message) {
        final NoSuchPathException failure = new NoSuchPathException(message);
        when(api.knownFolder(KnownFolder.PROFILE)).thenThrow(failure);

        assertThatThrownBy(locations::home).isSameAs(failure).hasMessage(message);
    }

    @Test
    void knownFoldersAreQueriedOnEveryCall() {
        when(api.knownFolder(KnownFolder.DESKTOP)).thenReturn(tempDir, tempDir.resolve("moved"));

        assertThat(locations.desktop()).isEqualTo(tempDir);
        assertThat(locations.desktop()).isEqualTo(tempDir.resolve("moved"));
        verify(env, never()).getenv(anyString());
    }
}
