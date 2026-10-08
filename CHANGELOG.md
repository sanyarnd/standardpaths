# 2.0.0 (unreleased)
- Java 11+
- Build migrated from Maven + Travis CI to Gradle + GitHub Actions (Linux, Windows, macOS)
- Publishing to Maven Central (Central Portal) and GitHub Packages instead of Bintray
- JetBrains annotations replaced with JSpecify, the code is checked by Error Prone and NullAway
- No dependencies: `jna-platform` is removed, Windows directories are read from the environment and the registry
- All methods return `Optional<Path>` instead of throwing, `NoSuchPathException` is removed
- `StandardPaths.forApp(name)` returns application directories (`AppPaths`)
- New locations: `state()`, `runtime()`, `templates()`, `publicShare()`
- Windows: `config()` is the roaming directory (`%APPDATA%`)
- macOS support
- Linux implementation is used for other Unix-like systems (FreeBSD, Solaris, etc.)
- Linux: user directories are read from `user-dirs.dirs`, not only from environment variables
- Linux: relative `XDG_*` values are ignored as required by the specification
- Linux: `temp()` respects `$TMPDIR`
- Linux: `home()` falls back to `user.home` instead of the relative `~` path
- Windows: `home()` no longer returns `nullnull` when `HOMEDRIVE`/`HOMEPATH` are missing
- `Darwin` is no longer detected as Windows
- `StandardPaths` no longer fails during class initialization on an unsupported OS

# 1.0.2
- Optimized internal build
- Using Jetbrains annotations instead of checker-qual
- Updated codebase formatting

# 1.0.1
- Sonarcloud fixes

# 1.0
- Initial commit

