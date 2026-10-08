package io.github.sanyarnd.standardpaths;

import java.nio.file.Path;
import java.util.Optional;

/// Platform-specific locations, mirrors [StandardPaths].
///
/// @author Alexander Biryukov
interface LocationDelegate {
    Optional<Path> home();

    Optional<Path> temp();

    Optional<Path> cache();

    Optional<Path> config();

    Optional<Path> data();

    Optional<Path> dataLocal();

    Optional<Path> state();

    Optional<Path> runtime();

    Optional<Path> desktop();

    Optional<Path> documents();

    Optional<Path> downloads();

    Optional<Path> music();

    Optional<Path> pictures();

    Optional<Path> videos();

    Optional<Path> templates();

    Optional<Path> publicShare();

    /// Application directory inside the base directory.
    ///
    /// @param base base directory, e.g. [#cache()]
    /// @param app application name
    /// @param kind directory kind (`cache`, `config`, etc.), used where base directories are shared
    /// @return application directory
    default Optional<Path> appDir(final Optional<Path> base, final String app, final String kind) {
        return base.map(path -> path.resolve(app));
    }
}
