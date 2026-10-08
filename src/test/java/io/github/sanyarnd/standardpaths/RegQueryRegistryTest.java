package io.github.sanyarnd.standardpaths;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import org.instancio.junit.Given;
import org.instancio.junit.InstancioExtension;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@ExtendWith(InstancioExtension.class)
class RegQueryRegistryTest {
    @Test
    void parsesRegQueryOutput() {
        final List<String> output = List.of(
                "",
                "HKEY_CURRENT_USER\\Software\\Microsoft\\Windows\\CurrentVersion\\Explorer\\User Shell Folders",
                "    AppData    REG_EXPAND_SZ    %USERPROFILE%\\AppData\\Roaming",
                "    Cache    REG_EXPAND_SZ    %USERPROFILE%\\AppData\\Local\\Microsoft\\Windows\\INetCache",
                "    Desktop    REG_EXPAND_SZ    D:\\Desktop",
                "    Local AppData    REG_EXPAND_SZ    %USERPROFILE%\\AppData\\Local",
                "    My Music    REG_EXPAND_SZ    %USERPROFILE%\\Music",
                "    Personal    REG_EXPAND_SZ    %USERPROFILE%\\Documents",
                "    {374DE290-123F-4565-9164-39C4925E467B}    REG_EXPAND_SZ    %USERPROFILE%\\Downloads",
                "    Templates    REG_SZ    C:\\Templates",
                "");

        assertThat(RegQueryRegistry.parse(output))
                .containsOnly(
                        Map.entry("AppData", "%USERPROFILE%\\AppData\\Roaming"),
                        Map.entry("Cache", "%USERPROFILE%\\AppData\\Local\\Microsoft\\Windows\\INetCache"),
                        Map.entry("Desktop", "D:\\Desktop"),
                        Map.entry("Local AppData", "%USERPROFILE%\\AppData\\Local"),
                        Map.entry("My Music", "%USERPROFILE%\\Music"),
                        Map.entry("Personal", "%USERPROFILE%\\Documents"),
                        Map.entry("{374DE290-123F-4565-9164-39C4925E467B}", "%USERPROFILE%\\Downloads"),
                        Map.entry("Templates", "C:\\Templates"));
    }

    @Test
    void preservesSpacesInsideData() {
        assertThat(RegQueryRegistry.parse(List.of("    My Video    REG_EXPAND_SZ    D:\\My  Videos  ")))
                .containsOnly(Map.entry("My Video", "D:\\My  Videos"));
    }

    @Test
    void parsesEmptyData() {
        assertThat(RegQueryRegistry.parse(List.of("    Desktop    REG_SZ    ", "    Music    REG_SZ")))
                .containsOnly(Map.entry("Desktop", ""), Map.entry("Music", ""));
    }

    @ParameterizedTest
    @ValueSource(
            strings = {
                "HKEY_CURRENT_USER\\Software",
                "    Flags    REG_DWORD    0x1",
                "    Binary    REG_BINARY    0102",
                "    Multi    REG_MULTI_SZ    a\\0b",
                "Desktop    REG_SZ    C:\\Desktop",
                "ERROR: The system was unable to find the specified registry key or value.",
                ""
            })
    void ignoresUnsupportedLines(final String line) {
        assertThat(RegQueryRegistry.parse(List.of(line))).isEmpty();
    }

    @RepeatedTest(5)
    void parsesArbitraryNames(@Given final String name, @Given final String data) {
        assertThat(RegQueryRegistry.parse(List.of("    " + name + "    REG_SZ    " + data)))
                .containsOnly(Map.entry(name, data));
    }

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void missingRegExecutableGivesEmptyResult() {
        assertThat(new RegQueryRegistry(new FakeEnvironment()).userShellFolders())
                .isEmpty();
    }

    @Test
    @EnabledOnOs(OS.WINDOWS)
    void readsRealRegistry() {
        final Map<String, String> folders = new RegQueryRegistry(Environment.SYSTEM).userShellFolders();

        assertThat(folders).containsKeys("AppData", "Local AppData", "Desktop", "Personal");
    }

    @Test
    @EnabledOnOs(OS.WINDOWS)
    void realLocationsMatchEnvironment() {
        final WindowsLocations locations =
                new WindowsLocations(new RegQueryRegistry(Environment.SYSTEM), Environment.SYSTEM);
        final Path home = Path.of(System.getProperty("user.home"));

        assertThat(locations.home()).contains(home);
        assertThat(locations.data()).contains(Path.of(System.getenv("APPDATA")));
        assertThat(locations.dataLocal()).contains(Path.of(System.getenv("LOCALAPPDATA")));
        assertThat(locations.desktop())
                .hasValueSatisfying(path -> assertThat(path).startsWithRaw(home));
        assertThat(locations.documents())
                .hasValueSatisfying(path -> assertThat(path).startsWithRaw(home));
        assertThat(locations.downloads()).contains(home.resolve("Downloads"));
        assertThat(locations.templates())
                .hasValueSatisfying(path -> assertThat(path).startsWithRaw(Path.of(System.getenv("APPDATA"))));
        assertThat(locations.publicShare()).contains(Path.of(System.getenv("PUBLIC")));
    }
}
