package io.github.sanyarnd.standardpaths;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Path;
import org.instancio.junit.Given;
import org.instancio.junit.InstancioExtension;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

@ExtendWith(InstancioExtension.class)
class EnvironmentTest {
    @TempDir
    Path tempDir;

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t", "relative", "relative/path", "./dot", "../parent", "~", "~/Desktop", "\u0000"})
    void rejectsMissingBlankRelativeAndInvalidPaths(final String value) {
        assertThat(Environment.absolutePath(value)).isEmpty();
    }

    @Test
    void acceptsAbsolutePath() {
        assertThat(Environment.absolutePath(tempDir.toString())).contains(tempDir);
    }

    @Test
    void normalizesAbsolutePath() {
        final String value = tempDir.resolve("a").resolve("..").resolve("b").toString();

        assertThat(Environment.absolutePath(value)).contains(tempDir.resolve("b"));
    }

    @RepeatedTest(5)
    void readsPathFromVariable(@Given final String name) {
        final Environment env = new FakeEnvironment().env(name, tempDir);

        assertThat(env.envPath(name)).contains(tempDir);
        assertThat(env.propertyPath(name)).isEmpty();
    }

    @RepeatedTest(5)
    void readsPathFromProperty(@Given final String name) {
        final Environment env = new FakeEnvironment().property(name, tempDir);

        assertThat(env.propertyPath(name)).contains(tempDir);
        assertThat(env.envPath(name)).isEmpty();
    }

    @RepeatedTest(5)
    void ignoresRelativeVariable(@Given final String name, @Given final String value) {
        final Environment env = new FakeEnvironment().env(name, value);

        assertThat(env.envPath(name)).isEmpty();
    }

    @Test
    void systemEnvironmentReadsSystemProperties() {
        assertThat(Environment.SYSTEM.getProperty("java.version")).isEqualTo(System.getProperty("java.version"));
    }

    @Test
    void systemEnvironmentReadsVariables() {
        assertThat(Environment.SYSTEM.getenv("PATH")).isEqualTo(System.getenv("PATH"));
    }
}
