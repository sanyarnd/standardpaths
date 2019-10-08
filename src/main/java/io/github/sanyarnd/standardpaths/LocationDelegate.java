package io.github.sanyarnd.standardpaths;

import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

/**
 * Location delegate interface, mimics {@link StandardPaths}, helps to improve code readability.
 *
 * @author Alexander Biryukov
 */
interface LocationDelegate {
    @NotNull
    Path cache();

    @NotNull
    Path config();

    @NotNull
    Path data();

    @NotNull
    Path dataLocal();

    @NotNull
    Path temp();

    @NotNull
    Path home();

    @NotNull
    Path desktop();

    @NotNull
    Path documents();

    @NotNull
    Path downloads();

    @NotNull
    Path music();

    @NotNull
    Path pictures();

    @NotNull
    Path videos();
}
