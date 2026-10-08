package io.github.sanyarnd.standardpaths;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

/// Real system calls, runs on Windows only.
@EnabledOnOs(OS.WINDOWS)
class JnaWindowsApiTest {
    private final JnaWindowsApi api = new JnaWindowsApi();

    @ParameterizedTest
    @EnumSource(KnownFolder.class)
    void knownFolderIsAbsolute(final KnownFolder folder) {
        assertThat(api.knownFolder(folder)).isAbsolute();
    }

    @Test
    void profileIsUserHome() {
        assertThat(api.knownFolder(KnownFolder.PROFILE)).isEqualTo(Paths.get(System.getProperty("user.home")));
    }

    @Test
    void appDataIsInsideProfile() {
        final Path profile = api.knownFolder(KnownFolder.PROFILE);

        assertThat(api.knownFolder(KnownFolder.LOCAL_APP_DATA)).startsWith(profile);
        assertThat(api.knownFolder(KnownFolder.ROAMING_APP_DATA)).startsWith(profile);
    }

    @Test
    void tempDirectoryExists() {
        final Path temp = api.tempDirectory();

        assertThat(temp).isAbsolute();
        assertThat(Files.isDirectory(temp)).isTrue();
    }
}
