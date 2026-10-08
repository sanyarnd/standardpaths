package io.github.sanyarnd.standardpaths;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import org.jspecify.annotations.Nullable;

final class FakeEnvironment implements Environment {
    private final Map<String, String> variables = new HashMap<>();
    private final Map<String, String> properties = new HashMap<>();

    FakeEnvironment env(final String name, final String value) {
        variables.put(name, value);
        return this;
    }

    FakeEnvironment env(final String name, final Path value) {
        return env(name, value.toString());
    }

    FakeEnvironment property(final String name, final String value) {
        properties.put(name, value);
        return this;
    }

    FakeEnvironment property(final String name, final Path value) {
        return property(name, value.toString());
    }

    @Override
    public @Nullable String getenv(final String name) {
        return variables.get(name);
    }

    @Override
    public @Nullable String getProperty(final String name) {
        return properties.get(name);
    }
}
