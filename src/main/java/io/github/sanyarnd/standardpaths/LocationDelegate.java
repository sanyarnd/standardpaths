package io.github.sanyarnd.standardpaths;

import java.nio.file.Path;

/// Platform-specific locations, mirrors [StandardPaths].
///
/// @author Alexander Biryukov
interface LocationDelegate {
    Path cache();

    Path config();

    Path data();

    Path dataLocal();

    Path temp();

    Path home();

    Path desktop();

    Path documents();

    Path downloads();

    Path music();

    Path pictures();

    Path videos();
}
