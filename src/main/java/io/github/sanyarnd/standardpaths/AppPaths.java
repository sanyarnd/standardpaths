package io.github.sanyarnd.standardpaths;

import java.nio.file.Path;
import java.util.Optional;

/// Directories of the particular application, created by [StandardPaths#forApp(String)].
///
/// Directories follow platform conventions:
/// - Windows: `%LOCALAPPDATA%\<app>\cache`, `%APPDATA%\<app>\config`, etc.;
/// - macOS: `$HOME/Library/Caches/<app>`, `$HOME/Library/Application Support/<app>`, etc.;
/// - Linux: `$XDG_CACHE_HOME/<app>`, `$XDG_CONFIG_HOME/<app>`, etc.
///
/// Note that on macOS config, data and state directories are the same.
///
/// @author Alexander Biryukov
public final class AppPaths {
    private final LocationDelegate delegate;
    private final String name;

    AppPaths(final LocationDelegate locationDelegate, final String appName) {
        delegate = locationDelegate;
        name = appName;
    }

    /// Application name.
    ///
    /// @return application name
    public String name() {
        return name;
    }

    /// Application cache directory, see [StandardPaths#cache()].
    ///
    /// @return path to the directory, empty if it can't be determined
    public Optional<Path> cache() {
        return delegate.appDir(delegate.cache(), name, "cache");
    }

    /// Application config directory, see [StandardPaths#config()].
    ///
    /// @return path to the directory, empty if it can't be determined
    public Optional<Path> config() {
        return delegate.appDir(delegate.config(), name, "config");
    }

    /// Application data directory, see [StandardPaths#data()].
    ///
    /// @return path to the directory, empty if it can't be determined
    public Optional<Path> data() {
        return delegate.appDir(delegate.data(), name, "data");
    }

    /// Application local data directory, see [StandardPaths#dataLocal()].
    ///
    /// @return path to the directory, empty if it can't be determined
    public Optional<Path> dataLocal() {
        return delegate.appDir(delegate.dataLocal(), name, "data");
    }

    /// Application state directory, see [StandardPaths#state()].
    ///
    /// @return path to the directory, empty if it can't be determined
    public Optional<Path> state() {
        return delegate.appDir(delegate.state(), name, "state");
    }

    /// Application runtime directory, see [StandardPaths#runtime()].
    ///
    /// @return path to the directory, empty if it can't be determined or the system has no runtime directory
    public Optional<Path> runtime() {
        return delegate.appDir(delegate.runtime(), name, "runtime");
    }

    @Override
    public String toString() {
        return "AppPaths[" + name + "]";
    }
}
