package io.github.sanyarnd.standardpaths;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;

@Tag("integration")
public class OsTest {
    @Test
    @EnabledOnOs(org.junit.jupiter.api.condition.OS.WINDOWS)
    public void windows_test() {
        Assertions.assertTrue(OS.isWindows());
    }

    @Test
    @EnabledOnOs(org.junit.jupiter.api.condition.OS.LINUX)
    public void linux_test() {
        Assertions.assertTrue(OS.isLinux());
    }

    @Test
    @EnabledOnOs(org.junit.jupiter.api.condition.OS.MAC)
    public void mac_test() {
        Assertions.assertTrue(OS.isMac());
    }

    @Test
    @EnabledOnOs(org.junit.jupiter.api.condition.OS.OTHER)
    public void unknown_test() {
        Assertions.assertTrue(OS.isUnknown());
    }
}
