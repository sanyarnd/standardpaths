package io.github.sanyarnd.standardpaths;

import java.nio.file.Path;
import java.util.Optional;

/// All locations, binds [LocationDelegate] methods to [StandardPaths] methods.
enum Location {
    HOME,
    TEMP,
    CACHE,
    CONFIG,
    DATA,
    DATA_LOCAL,
    STATE,
    RUNTIME,
    DESKTOP,
    DOCUMENTS,
    DOWNLOADS,
    MUSIC,
    PICTURES,
    VIDEOS,
    TEMPLATES,
    PUBLIC_SHARE;

    Optional<Path> of(final LocationDelegate delegate) {
        return switch (this) {
            case HOME -> delegate.home();
            case TEMP -> delegate.temp();
            case CACHE -> delegate.cache();
            case CONFIG -> delegate.config();
            case DATA -> delegate.data();
            case DATA_LOCAL -> delegate.dataLocal();
            case STATE -> delegate.state();
            case RUNTIME -> delegate.runtime();
            case DESKTOP -> delegate.desktop();
            case DOCUMENTS -> delegate.documents();
            case DOWNLOADS -> delegate.downloads();
            case MUSIC -> delegate.music();
            case PICTURES -> delegate.pictures();
            case VIDEOS -> delegate.videos();
            case TEMPLATES -> delegate.templates();
            case PUBLIC_SHARE -> delegate.publicShare();
        };
    }

    Optional<Path> ofStandardPaths() {
        return switch (this) {
            case HOME -> StandardPaths.home();
            case TEMP -> StandardPaths.temp();
            case CACHE -> StandardPaths.cache();
            case CONFIG -> StandardPaths.config();
            case DATA -> StandardPaths.data();
            case DATA_LOCAL -> StandardPaths.dataLocal();
            case STATE -> StandardPaths.state();
            case RUNTIME -> StandardPaths.runtime();
            case DESKTOP -> StandardPaths.desktop();
            case DOCUMENTS -> StandardPaths.documents();
            case DOWNLOADS -> StandardPaths.downloads();
            case MUSIC -> StandardPaths.music();
            case PICTURES -> StandardPaths.pictures();
            case VIDEOS -> StandardPaths.videos();
            case TEMPLATES -> StandardPaths.templates();
            case PUBLIC_SHARE -> StandardPaths.publicShare();
        };
    }
}
