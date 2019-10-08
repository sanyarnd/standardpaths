package io.github.sanyarnd.standardpaths;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

@Tag("integration")
@EnabledOnOs(OS.WINDOWS)
class WindowsLocationsIT {
    private final LocationDelegate locations = new WindowsLocations();

    @Test
    void cache_test() {
        Assertions.assertNotNull(locations.cache());
    }

    @Test
    void config_test() {
        Assertions.assertNotNull(locations.config());
    }

    @Test
    void data_test() {
        Assertions.assertNotNull(locations.data());
    }

    @Test
    void dataLocal_test() {
        Assertions.assertNotNull(locations.dataLocal());
    }

    @Test
    void temp_test() {
        Assertions.assertNotNull(locations.temp());
    }

    @Test
    void home_test() {
        Assertions.assertNotNull(locations.home());
    }

    @Test
    void desktop_test() {
        Assertions.assertNotNull(locations.desktop());
    }

    @Test
    void documents_test() {
        Assertions.assertNotNull(locations.documents());
    }

    @Test
    void downloads_test() {
        Assertions.assertNotNull(locations.downloads());
    }

    @Test
    void music_test() {
        Assertions.assertNotNull(locations.music());
    }

    @Test
    void pictures_test() {
        Assertions.assertNotNull(locations.pictures());
    }

    @Test
    void videos_test() {
        Assertions.assertNotNull(locations.videos());
    }
}
