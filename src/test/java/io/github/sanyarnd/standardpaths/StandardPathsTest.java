package io.github.sanyarnd.standardpaths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.instancio.junit.Given;
import org.instancio.junit.InstancioExtension;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

@ExtendWith(InstancioExtension.class)
class StandardPathsTest {
    @Test
    void createsWindowsLocations() {
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
    @EnumSource(
            value = Location.class,
            names = {"HOME", "TEMP", "CACHE", "CONFIG", "DATA", "DATA_LOCAL", "STATE"})
    void currentSystemHasCommonDirectories(final Location location) {
        assertThat(location.ofStandardPaths())
                .hasValueSatisfying(path -> assertThat(path).isAbsolute());
    }

    @ParameterizedTest
    @EnumSource(Location.class)
    void currentSystemPathIsAbsolute(final Location location) {
        location.ofStandardPaths().ifPresent(path -> assertThat(path).isAbsolute());
    }

    @ParameterizedTest
    @EnumSource(Location.class)
    void currentSystemPathMatchesDelegate(final Location location) {
        final LocationDelegate delegate = StandardPaths.create(Os.current(), Environment.SYSTEM);

        assertThat(location.ofStandardPaths()).isEqualTo(location.of(delegate));
    }

    @Test
    void forAppReturnsApplicationDirectories() {
        final AppPaths app = StandardPaths.forApp("my-app");

        assertThat(app.name()).isEqualTo("my-app");
        assertThat(app.cache())
                .hasValueSatisfying(path ->
                        assertThat(path).startsWithRaw(StandardPaths.cache().orElseThrow()));
        assertThat(app.config())
                .hasValueSatisfying(path ->
                        assertThat(path).startsWithRaw(StandardPaths.config().orElseThrow()));
    }

    @RepeatedTest(5)
    void acceptsValidNames(@Given final String name) {
        assertThat(StandardPaths.validateAppName(name)).isEqualTo(name);
    }

    @ParameterizedTest
    @ValueSource(strings = {"my-app", "My App", "org.example.app", "app_1.0"})
    void acceptsTypicalNames(final String name) {
        assertThat(StandardPaths.forApp(name).name()).isEqualTo(name);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "\t", " app", "app ", ".", "..", "a/b", "a\\b", "/app", "\\app", "app\u0000"})
    void rejectsInvalidNames(final String name) {
        assertThatThrownBy(() -> StandardPaths.forApp(name))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid application name");
    }

    @Test
    @SuppressWarnings("NullAway")
    void rejectsNullName() {
        assertThatNullPointerException().isThrownBy(() -> StandardPaths.forApp(null));
    }
}
