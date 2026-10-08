package io.github.sanyarnd.standardpaths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Function;
import java.util.stream.Stream;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedClass;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

/// Behaviour shared by Linux and macOS.
@ParameterizedClass
@MethodSource("factories")
class PosixLocationsTest {
    @TempDir
    Path tempDir;

    private final Function<Environment, LocationDelegate> factory;

    PosixLocationsTest(final Function<Environment, LocationDelegate> locationsFactory) {
        factory = locationsFactory;
    }

    static Stream<Named<Function<Environment, LocationDelegate>>> factories() {
        return Stream.of(Named.of("unix", UnixLocations::new), Named.of("mac", MacLocations::new));
    }

    @Test
    void homeFromVariable() {
        final FakeEnvironment env =
                new FakeEnvironment().env("HOME", tempDir).property("user.home", tempDir.resolve("other"));

        assertThat(factory.apply(env).home()).isEqualTo(tempDir);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "relative"})
    void homeFromPropertyIfVariableIsInvalid(final String value) {
        final FakeEnvironment env = new FakeEnvironment().env("HOME", value).property("user.home", tempDir);

        assertThat(factory.apply(env).home()).isEqualTo(tempDir);
    }

    @Test
    void homeFromPropertyIfVariableIsMissing() {
        final FakeEnvironment env = new FakeEnvironment().property("user.home", tempDir);

        assertThat(factory.apply(env).home()).isEqualTo(tempDir);
    }

    @Test
    void homeIsUnavailable() {
        final LocationDelegate locations = factory.apply(new FakeEnvironment().property("user.home", "?"));

        assertThatThrownBy(locations::home)
                .isInstanceOf(NoSuchPathException.class)
                .hasMessageContaining("home");
    }

    @ParameterizedTest
    @EnumSource(value = Location.class, mode = EnumSource.Mode.EXCLUDE, names = "TEMP")
    void everyLocationExceptTempNeedsHome(final Location location) {
        final LocationDelegate locations = factory.apply(new FakeEnvironment());

        assertThatThrownBy(() -> location.of(locations)).isInstanceOf(NoSuchPathException.class);
    }

    @Test
    void tempFromVariable() {
        final FakeEnvironment env =
                new FakeEnvironment().env("TMPDIR", tempDir).property("java.io.tmpdir", tempDir.resolve("other"));

        assertThat(factory.apply(env).temp()).isEqualTo(tempDir);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "relative"})
    void tempFromPropertyIfVariableIsInvalid(final String value) {
        final FakeEnvironment env = new FakeEnvironment().env("TMPDIR", value).property("java.io.tmpdir", tempDir);

        assertThat(factory.apply(env).temp()).isEqualTo(tempDir);
    }

    @Test
    void tempFallsBackToTmp() {
        assertThat(factory.apply(new FakeEnvironment()).temp()).isEqualTo(Paths.get("/tmp"));
    }

    @ParameterizedTest
    @EnumSource(Location.class)
    void everyLocationIsAbsolute(final Location location) {
        final LocationDelegate locations =
                factory.apply(new FakeEnvironment().env("HOME", tempDir).env("TMPDIR", tempDir));

        assertThat(location.of(locations)).isAbsolute();
    }
}
