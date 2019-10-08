package io.github.sanyarnd.standardpaths;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;

@Tag("integration")
class OsIT {
    @Test
    @EnabledOnOs(org.junit.jupiter.api.condition.OS.WINDOWS)
    void windows_test() {
        Assertions.assertTrue(Os.isWindows());
    }

    @Test
    @EnabledOnOs(org.junit.jupiter.api.condition.OS.LINUX)
    void linux_test() {
        Assertions.assertTrue(Os.isLinux());
    }

    @Test
    @EnabledOnOs(org.junit.jupiter.api.condition.OS.MAC)
    void mac_test() {
        Assertions.assertTrue(Os.isMac());
    }

    @Test
    @EnabledOnOs(org.junit.jupiter.api.condition.OS.OTHER)
    void unknown_test() {
        Assertions.assertTrue(Os.isUnknown());
    }
}
