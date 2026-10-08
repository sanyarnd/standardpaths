package io.github.sanyarnd.standardpaths;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class StandardPathsTest {
    @Test
    void createsWindowsLocations() {
        // native libraries are loaded lazily, creation must work on any OS
        assertThat(StandardPaths.create(Os.WINDOWS, new FakeEnvironment())).isInstanceOf(WindowsLocations.class);
    }

    @Test
    void createsMacLocations() {
        assertThat(StandardPaths.create(Os.MAC, new FakeEnvironment())).isInstanceOf(MacLocations.class);
    }

    @Test
    void createsUnixLocations() {
        assertThat(StandardPaths.create(Os.UNIX, new FakeEnvironment())).isInstanceOf(UnixLocations.class);
    }

    @ParameterizedTest
    @EnumSource(Location.class)
    void currentSystemPathIsAbsolute(final Location location) {
        assertThat(location.ofStandardPaths()).isAbsolute();
    }

    @ParameterizedTest
    @EnumSource(Location.class)
    void currentSystemPathMatchesDelegate(final Location location) {
        final LocationDelegate delegate = StandardPaths.create(Os.current(), Environment.SYSTEM);

        assertThat(location.ofStandardPaths()).isEqualTo(location.of(delegate));
    }
}
