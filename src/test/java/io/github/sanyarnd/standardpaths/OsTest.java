package io.github.sanyarnd.standardpaths;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class OsTest {
    @ParameterizedTest
    @CsvSource({
        "Windows 11, WINDOWS",
        "Windows Server 2022, WINDOWS",
        "windows 10, WINDOWS",
        "Mac OS X, MAC",
        "macOS, MAC",
        "Darwin, MAC",
        "Linux, UNIX",
        "FreeBSD, UNIX",
        "OpenBSD, UNIX",
        "SunOS, UNIX",
        "AIX, UNIX",
        "'', UNIX"
    })
    void detectsOsByName(final String osName, final Os expected) {
        assertThat(Os.of(osName)).isEqualTo(expected);
    }

    @Test
    void darwinIsNotWindows() {
        // "darwin" contains "win", the old implementation detected it as Windows
        assertThat(Os.of("darwin")).isEqualTo(Os.MAC);
    }

    @Test
    @EnabledOnOs(OS.WINDOWS)
    void currentIsWindows() {
        assertThat(Os.current()).isEqualTo(Os.WINDOWS);
    }

    @Test
    @EnabledOnOs(OS.MAC)
    void currentIsMac() {
        assertThat(Os.current()).isEqualTo(Os.MAC);
    }

    @Test
    @EnabledOnOs({OS.LINUX, OS.FREEBSD, OS.OPENBSD, OS.SOLARIS, OS.AIX})
    void currentIsUnix() {
        assertThat(Os.current()).isEqualTo(Os.UNIX);
    }
}
