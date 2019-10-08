package io.github.sanyarnd.standardpaths;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

@Tag("integration")
public class LinuxLocationsIT {
    private final LocationDelegate locations = new LinuxLocations();

    @Test
    @EnabledOnOs(OS.LINUX)
    public void cache_test() {
        Assertions.assertNotNull(locations.cache());
    }

    @Test
    @EnabledOnOs(OS.LINUX)
    public void config_test() {
        Assertions.assertNotNull(locations.config());
    }

    @Test
    @EnabledOnOs(OS.LINUX)
    public void data_test() {
        Assertions.assertNotNull(locations.data());
    }

    @Test
    @EnabledOnOs(OS.LINUX)
    public void dataLocal_test() {
        Assertions.assertNotNull(locations.dataLocal());
    }

    @Test
    @EnabledOnOs(OS.LINUX)
    public void temp_test() {
        Assertions.assertNotNull(locations.temp());
    }

    @Test
    @EnabledOnOs(OS.LINUX)
    public void home_test() {
        Assertions.assertNotNull(locations.home());
    }

    @Test
    @EnabledOnOs(OS.LINUX)
    public void desktop_test() {
        Assertions.assertNotNull(locations.desktop());
    }

    @Test
    @EnabledOnOs(OS.LINUX)
    public void documents_test() {
        Assertions.assertNotNull(locations.documents());
    }

    @Test
    @EnabledOnOs(OS.LINUX)
    public void downloads_test() {
        Assertions.assertNotNull(locations.downloads());
    }

    @Test
    @EnabledOnOs(OS.LINUX)
    public void music_test() {
        Assertions.assertNotNull(locations.music());
    }

    @Test
    @EnabledOnOs(OS.LINUX)
    public void pictures_test() {
        Assertions.assertNotNull(locations.pictures());
    }

    @Test
    @EnabledOnOs(OS.LINUX)
    public void videos_test() {
        Assertions.assertNotNull(locations.videos());
    }
}

