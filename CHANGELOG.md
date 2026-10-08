# 2.0.0 (unreleased)
- Java 11+
- Build migrated from Maven + Travis CI to Gradle + GitHub Actions (Linux, Windows, macOS)
- Publishing to Maven Central (Central Portal) and GitHub Packages instead of Bintray
- JetBrains annotations replaced with JSpecify, the code is checked by Error Prone and NullAway
- `jna-platform` updated to 5.19.1 and is a runtime dependency now
- macOS support
- Linux implementation is used for other Unix-like systems (FreeBSD, Solaris, etc.)
- Linux: user directories are read from `user-dirs.dirs`, not only from environment variables
- Linux: relative `XDG_*` values are ignored as required by the specification
- Linux: `temp()` respects `$TMPDIR`
- Linux: `home()` falls back to `user.home` instead of the relative `~` path
- Windows: `home()` uses `FOLDERID_Profile`, the custom `userenv.dll` binding is removed
- Windows: known folder paths are no longer leaked, failures report the real error
- Windows: `home()` fallback no longer returns `nullnull` when `HOMEDRIVE`/`HOMEPATH` are missing
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

