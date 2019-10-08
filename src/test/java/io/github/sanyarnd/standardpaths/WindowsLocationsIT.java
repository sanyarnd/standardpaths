package io.github.sanyarnd.standardpaths;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

@Tag("integration")
public class WindowsLocationsIT {
    private final WindowsLocations locations = new WindowsLocations();

    @Test
    @EnabledOnOs(OS.WINDOWS)
    public void cache_test() {
        Assertions.assertNotNull(locations.cache());
    }

    @Test
    @EnabledOnOs(OS.WINDOWS)
    public void config_test() {
        Assertions.assertNotNull(locations.config());
    }

    @Test
    @EnabledOnOs(OS.WINDOWS)
    public void data_test() {
        Assertions.assertNotNull(locations.data());
    }

    @Test
    @EnabledOnOs(OS.WINDOWS)
    public void dataLocal_test() {
        Assertions.assertNotNull(locations.dataLocal());
    }

    @Test
    @EnabledOnOs(OS.WINDOWS)
    public void temp_test() {
        Assertions.assertNotNull(locations.temp());
    }

    @Test
    @EnabledOnOs(OS.WINDOWS)
    public void home_test() {
        Assertions.assertNotNull(locations.home());
    }

    @Test
    @EnabledOnOs(OS.WINDOWS)
    public void desktop_test() {
        Assertions.assertNotNull(locations.desktop());
    }

    @Test
    @EnabledOnOs(OS.WINDOWS)
    public void documents_test() {
        Assertions.assertNotNull(locations.documents());
    }

    @Test
    @EnabledOnOs(OS.WINDOWS)
    public void downloads_test() {
        Assertions.assertNotNull(locations.downloads());
    }

    @Test
    @EnabledOnOs(OS.WINDOWS)
    public void music_test() {
        Assertions.assertNotNull(locations.music());
    }

    @Test
    @EnabledOnOs(OS.WINDOWS)
    public void pictures_test() {
        Assertions.assertNotNull(locations.pictures());
    }

    @Test
    @EnabledOnOs(OS.WINDOWS)
    public void videos_test() {
        Assertions.assertNotNull(locations.videos());
    }
}
