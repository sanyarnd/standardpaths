package io.github.sanyarnd.standardpaths;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;

class AppPathsTest {
    private static final String APP = "my-app";

    @TempDir
    Path tempDir;

    private Path home;

    @BeforeEach
    void setUp() {
        home = tempDir.resolve("home");
    }

    @Test
    void unix() {
        final FakeEnvironment env =
                new FakeEnvironment().env("HOME", home).env("XDG_RUNTIME_DIR", tempDir.resolve("run"));
        final AppPaths app = new AppPaths(new UnixLocations(env), APP);

        assertThat(app.cache()).contains(home.resolve(".cache").resolve(APP));
        assertThat(app.config()).contains(home.resolve(".config").resolve(APP));
        assertThat(app.data()).contains(home.resolve(".local/share").resolve(APP));
        assertThat(app.dataLocal()).contains(home.resolve(".local/share").resolve(APP));
        assertThat(app.state()).contains(home.resolve(".local/state").resolve(APP));
        assertThat(app.runtime()).contains(tempDir.resolve("run").resolve(APP));
    }

    @Test
    void mac() {
        final AppPaths app = new AppPaths(new MacLocations(new FakeEnvironment().env("HOME", home)), APP);
        final Path support = home.resolve("Library/Application Support").resolve(APP);

        assertThat(app.cache()).contains(home.resolve("Library/Caches").resolve(APP));
        assertThat(app.config()).contains(support);
        assertThat(app.data()).contains(support);
        assertThat(app.dataLocal()).contains(support);
        assertThat(app.state()).contains(support);
        assertThat(app.runtime()).isEmpty();
    }

    @Test
    void windows() {
        final Path local = tempDir.resolve("Local");
        final Path roaming = tempDir.resolve("Roaming");
        final FakeEnvironment env = new FakeEnvironment()
                .property("user.home", home)
                .env("LOCALAPPDATA", local)
                .env("APPDATA", roaming);
        final AppPaths app = new AppPaths(new WindowsLocations(Mockito.mock(WindowsRegistry.class), env), APP);

        assertThat(app.cache()).contains(local.resolve(APP).resolve("cache"));
        assertThat(app.config()).contains(roaming.resolve(APP).resolve("config"));
        assertThat(app.data()).contains(roaming.resolve(APP).resolve("data"));
        assertThat(app.dataLocal()).contains(local.resolve(APP).resolve("data"));
        assertThat(app.state()).contains(local.resolve(APP).resolve("state"));
        assertThat(app.runtime()).isEmpty();
    }

    @ParameterizedTest
    @CsvSource({"unix", "windows"})
    void directoriesAreDistinct(final String os) {
        final FakeEnvironment env = new FakeEnvironment()
                .env("HOME", home)
                .property("user.home", home)
                .env("XDG_RUNTIME_DIR", tempDir.resolve("run"));
        final LocationDelegate delegate = os.equals("unix")
                ? new UnixLocations(env)
                : new WindowsLocations(Mockito.mock(WindowsRegistry.class), env);
        final AppPaths app = new AppPaths(delegate, APP);

        assertThat(app.cache()).isNotEqualTo(app.config()).isNotEqualTo(app.state());
        assertThat(app.config()).isNotEqualTo(app.data());
        assertThat(app.dataLocal()).isNotEqualTo(app.state());
    }

    @Test
    void emptyIfHomeIsUnknown() {
        final AppPaths app = new AppPaths(new UnixLocations(new FakeEnvironment()), APP);

        assertThat(app.cache()).isEmpty();
        assertThat(app.config()).isEmpty();
        assertThat(app.runtime()).isEmpty();
    }

    @Test
    void hasReadableToString() {
        assertThat(new AppPaths(new UnixLocations(new FakeEnvironment()), APP)).hasToString("AppPaths[my-app]");
    }
}
