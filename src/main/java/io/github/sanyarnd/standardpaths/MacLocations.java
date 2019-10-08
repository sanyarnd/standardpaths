package io.github.sanyarnd.standardpaths;

import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

/**
 * MacOS locations.
 *
 * @author Alexander Biryukov
 */
final class MacLocations implements LocationDelegate {
    private static final String NOT_IMPLEMENTED_MESSAGE = "Not implemented";

    @Override
    public @NotNull Path cache() {
        throw new NoSuchPathException(NOT_IMPLEMENTED_MESSAGE);
    }

    @Override
    public @NotNull Path config() {
        throw new NoSuchPathException(NOT_IMPLEMENTED_MESSAGE);
    }

    @Override
    public @NotNull Path data() {
        throw new NoSuchPathException(NOT_IMPLEMENTED_MESSAGE);
    }

    @Override
    public @NotNull Path dataLocal() {
        throw new NoSuchPathException(NOT_IMPLEMENTED_MESSAGE);
    }

    @Override
    public @NotNull Path temp() {
        throw new NoSuchPathException(NOT_IMPLEMENTED_MESSAGE);
    }

    @Override
    public @NotNull Path home() {
        throw new NoSuchPathException(NOT_IMPLEMENTED_MESSAGE);
    }

    @Override
    public @NotNull Path desktop() {
        throw new NoSuchPathException(NOT_IMPLEMENTED_MESSAGE);
    }

    @Override
    public @NotNull Path documents() {
        throw new NoSuchPathException(NOT_IMPLEMENTED_MESSAGE);
    }

    @Override
    public @NotNull Path downloads() {
        throw new NoSuchPathException(NOT_IMPLEMENTED_MESSAGE);
    }

    @Override
    public @NotNull Path music() {
        throw new NoSuchPathException(NOT_IMPLEMENTED_MESSAGE);
    }

    @Override
    public @NotNull Path pictures() {
        throw new NoSuchPathException(NOT_IMPLEMENTED_MESSAGE);
    }

    @Override
    public @NotNull Path videos() {
        throw new NoSuchPathException(NOT_IMPLEMENTED_MESSAGE);
    }
}
