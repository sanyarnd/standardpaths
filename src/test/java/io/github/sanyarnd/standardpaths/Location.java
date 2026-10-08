package io.github.sanyarnd.standardpaths;

import java.nio.file.Path;

/// All locations, binds [LocationDelegate] methods to [StandardPaths] methods.
enum Location {
    CACHE,
    CONFIG,
    DATA,
    DATA_LOCAL,
    TEMP,
    HOME,
    DESKTOP,
    DOCUMENTS,
    DOWNLOADS,
    MUSIC,
    PICTURES,
    VIDEOS;

    Path of(final LocationDelegate delegate) {
        return switch (this) {
            case CACHE -> delegate.cache();
            case CONFIG -> delegate.config();
            case DATA -> delegate.data();
            case DATA_LOCAL -> delegate.dataLocal();
            case TEMP -> delegate.temp();
            case HOME -> delegate.home();
            case DESKTOP -> delegate.desktop();
            case DOCUMENTS -> delegate.documents();
            case DOWNLOADS -> delegate.downloads();
            case MUSIC -> delegate.music();
            case PICTURES -> delegate.pictures();
            case VIDEOS -> delegate.videos();
        };
    }

    Path ofStandardPaths() {
        return switch (this) {
            case CACHE -> StandardPaths.cache();
            case CONFIG -> StandardPaths.config();
            case DATA -> StandardPaths.data();
            case DATA_LOCAL -> StandardPaths.dataLocal();
            case TEMP -> StandardPaths.temp();
            case HOME -> StandardPaths.home();
            case DESKTOP -> StandardPaths.desktop();
            case DOCUMENTS -> StandardPaths.documents();
            case DOWNLOADS -> StandardPaths.downloads();
            case MUSIC -> StandardPaths.music();
            case PICTURES -> StandardPaths.pictures();
            case VIDEOS -> StandardPaths.videos();
        };
    }
}
