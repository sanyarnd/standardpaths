package io.github.sanyarnd.standardpaths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.io.File;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
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
    WindowsRegistry registry;

    private Path home;
    private FakeEnvironment env;
    private WindowsLocations locations;

    @BeforeEach
    void setUp() {
        home = tempDir.resolve("home");
        env = new FakeEnvironment().property("user.home", home);
        locations = new WindowsLocations(registry, env);
    }

    private static String sep() {
        return File.separator;
    }

    @Test
    void homeFromProperty() {
        env.env("USERPROFILE", tempDir);

        assertThat(locations.home()).contains(home);
    }

    @Test
    void homeFallsBackToUserProfile() {
        final WindowsLocations noProperty =
                new WindowsLocations(registry, new FakeEnvironment().env("USERPROFILE", home));

        assertThat(noProperty.home()).contains(home);
    }

    @Test
    void homeIsUnavailable() {
        assertThat(new WindowsLocations(registry, new FakeEnvironment()).home()).isEmpty();
    }

    @Test
    void tempFromProperty() {
        env.property("java.io.tmpdir", tempDir).env("TEMP", home);

        assertThat(locations.temp()).contains(tempDir);
    }

    @Test
    void tempFallsBackToTempVariable() {
        env.env("TEMP", tempDir).env("TMP", home);

        assertThat(locations.temp()).contains(tempDir);
    }

    @Test
    void tempFallsBackToTmpVariable() {
        env.env("TEMP", "relative").env("TMP", tempDir);

        assertThat(locations.temp()).contains(tempDir);
    }

    @ParameterizedTest
    @CsvSource({
        "CACHE, LOCALAPPDATA",
        "DATA_LOCAL, LOCALAPPDATA",
        "STATE, LOCALAPPDATA",
        "CONFIG, APPDATA",
        "DATA, APPDATA"
    })
    void appDataFromVariable(final Location location, final String variable) {
        env.env(variable, tempDir.resolve(variable));

        assertThat(location.of(locations)).contains(tempDir.resolve(variable));
        verifyNoInteractions(registry);
    }

    @ParameterizedTest
    @CsvSource({
        "CACHE, Local AppData",
        "DATA_LOCAL, Local AppData",
        "STATE, Local AppData",
        "CONFIG, AppData",
        "DATA, AppData"
    })
    void appDataFromRegistry(final Location location, final String valueName) {
        env.env("USERPROFILE", home);
        when(registry.userShellFolders()).thenReturn(Map.of(valueName, "%USERPROFILE%" + sep() + "Custom"));

        assertThat(location.of(locations)).contains(home.resolve("Custom"));
    }

    @ParameterizedTest
    @CsvSource({"CACHE, Local", "DATA_LOCAL, Local", "STATE, Local", "CONFIG, Roaming", "DATA, Roaming"})
    void appDataDefaultsToProfile(final Location location, final String directory) {
        when(registry.userShellFolders()).thenReturn(Map.of());

        assertThat(location.of(locations)).contains(home.resolve("AppData").resolve(directory));
    }

    @ParameterizedTest
    @CsvSource({
        "DESKTOP, Desktop",
        "DOCUMENTS, Personal",
        "DOWNLOADS, {374DE290-123F-4565-9164-39C4925E467B}",
        "MUSIC, My Music",
        "PICTURES, My Pictures",
        "VIDEOS, My Video",
        "TEMPLATES, Templates"
    })
    void userDirFromRegistry(final Location location, final String valueName) {
        env.env("USERPROFILE", home);
        when(registry.userShellFolders()).thenReturn(Map.of(valueName, "%USERPROFILE%" + sep() + "Moved " + valueName));

        assertThat(location.of(locations)).contains(home.resolve("Moved " + valueName));
    }

    @ParameterizedTest
    @CsvSource({
        "DESKTOP, Desktop",
        "DOCUMENTS, Documents",
        "DOWNLOADS, Downloads",
        "MUSIC, Music",
        "PICTURES, Pictures",
        "VIDEOS, Videos"
    })
    void userDirDefaultsToProfile(final Location location, final String directory) {
        when(registry.userShellFolders()).thenReturn(Map.of());

        assertThat(location.of(locations)).contains(home.resolve(directory));
    }

    @Test
    void templatesDefaultsToRoamingAppData() {
        env.env("APPDATA", tempDir.resolve("roaming"));
        when(registry.userShellFolders()).thenReturn(Map.of());

        assertThat(locations.templates())
                .contains(tempDir.resolve("roaming")
                        .resolve("Microsoft")
                        .resolve("Windows")
                        .resolve("Templates"));
    }

    @Test
    void absoluteRegistryValueIsUsedAsIs() {
        when(registry.userShellFolders()).thenReturn(Map.of("Desktop", tempDir.toString()));

        assertThat(locations.desktop()).contains(tempDir);
    }

    @Test
    void expandsSeveralVariables() {
        final String full = tempDir.toString();
        final int split = full.indexOf(File.separatorChar, 1);
        env.env("HOMEDRIVE", full.substring(0, split)).env("HOMEPATH", full.substring(split));
        when(registry.userShellFolders()).thenReturn(Map.of("Desktop", "%HOMEDRIVE%%HOMEPATH%" + sep() + "Desktop"));

        assertThat(locations.desktop()).contains(tempDir.resolve("Desktop"));
    }

    @Test
    void unknownVariableMakesValueInvalid() {
        when(registry.userShellFolders()).thenReturn(Map.of("Desktop", "%UNKNOWN%" + sep() + "Desktop"));

        assertThat(locations.desktop()).contains(home.resolve("Desktop"));
    }

    @Test
    void relativeRegistryValueIsIgnored() {
        when(registry.userShellFolders()).thenReturn(Map.of("Desktop", "Desktop"));

        assertThat(locations.desktop()).contains(home.resolve("Desktop"));
    }

    @Test
    void registryIsQueriedOnce() {
        when(registry.userShellFolders()).thenReturn(Map.of());

        locations.desktop();
        locations.music();
        locations.videos();

        verify(registry, times(1)).userShellFolders();
    }

    @Test
    void runtimeIsAbsent() {
        assertThat(locations.runtime()).isEmpty();
        verifyNoInteractions(registry);
    }

    @Test
    void publicShareFromVariable() {
        env.env("PUBLIC", tempDir.resolve("Public"));

        assertThat(locations.publicShare()).contains(tempDir.resolve("Public"));
    }

    @Test
    void publicShareIsAbsentWithoutVariable() {
        assertThat(locations.publicShare()).isEmpty();
    }

    @RepeatedTest(3)
    void appDirIsSeparatedByKind(@Given final String app, @Given final String kind) {
        assertThat(locations.appDir(Optional.of(tempDir), app, kind))
                .contains(tempDir.resolve(app).resolve(kind));
    }
}
