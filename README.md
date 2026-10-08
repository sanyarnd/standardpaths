# Standard Paths
[![Build](https://github.com/sanyarnd/standardpaths/actions/workflows/build.yml/badge.svg)](https://github.com/sanyarnd/standardpaths/actions/workflows/build.yml)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.sanyarnd/standard-paths)](https://central.sonatype.com/artifact/io.github.sanyarnd/standard-paths)
[![Javadoc](https://javadoc.io/badge2/io.github.sanyarnd/standard-paths/javadoc.svg)](https://javadoc.io/doc/io.github.sanyarnd/standard-paths)

Standard Paths is a small library which provides cross platform access to the common directories such as `AppData`,
`Desktop` or `tmp`.

# Features
* Windows, macOS, Linux and other Unix-like systems
* Follows platform conventions: Known Folders on Windows, XDG Base Directory and `xdg-user-dirs` on Linux
* NIO-based
* JDK 11+ support

The library depends on [jna-platform](https://github.com/java-native-access/jna), which is used on Windows only.

# Quick Start
Access `StandardPaths` class and follow autocomplete suggestions:
```java
Path home = StandardPaths.home();
Path cache = StandardPaths.cache().resolve("my-app");
```

All returned paths are absolute, but directories are not guaranteed to exist.

`StandardPaths` methods throw unchecked `NoSuchPathException` if it's impossible to determine the path
(e.g. `$HOME` is not set).

More details can be found in [JavaDocs](https://javadoc.io/doc/io.github.sanyarnd/standard-paths).

# Download
Maven:
```xml
<dependency>
    <groupId>io.github.sanyarnd</groupId>
    <artifactId>standard-paths</artifactId>
    <version>1.0.2</version>
</dependency>
```

Gradle:
```kotlin
implementation("io.github.sanyarnd:standard-paths:1.0.2")
```

Jars are also available in [GitHub Packages](https://github.com/sanyarnd/standardpaths/packages)
and on the [releases](https://github.com/sanyarnd/standardpaths/releases) page.

# Available paths
| Method        | Windows                       | macOS                                 | Linux                                             |
|---------------|-------------------------------|---------------------------------------|---------------------------------------------------|
| `cache()`     | `%LOCALAPPDATA%`              | `$HOME/Library/Caches`                | `$XDG_CACHE_HOME` (default: `$HOME/.cache`)       |
| `config()`    | `%LOCALAPPDATA%`              | `$HOME/Library/Application Support`   | `$XDG_CONFIG_HOME` (default: `$HOME/.config`)     |
| `data()`      | `%APPDATA%`                   | `$HOME/Library/Application Support`   | `$XDG_DATA_HOME` (default: `$HOME/.local/share`)  |
| `dataLocal()` | `%LOCALAPPDATA%`              | `$HOME/Library/Application Support`   | `$XDG_DATA_HOME` (default: `$HOME/.local/share`)  |
| `temp()`      | `GetTempPath`                 | `$TMPDIR`                             | `$TMPDIR` (default: `/tmp`)                       |
| `home()`      | `%USERPROFILE%`               | `$HOME`                               | `$HOME`                                           |
| `desktop()`   | `%USERPROFILE%\Desktop`       | `$HOME/Desktop`                       | `XDG_DESKTOP_DIR` (default: `$HOME/Desktop`)      |
| `documents()` | `%USERPROFILE%\Documents`     | `$HOME/Documents`                     | `XDG_DOCUMENTS_DIR` (default: `$HOME/Documents`)  |
| `downloads()` | `%USERPROFILE%\Downloads`     | `$HOME/Downloads`                     | `XDG_DOWNLOAD_DIR` (default: `$HOME/Downloads`)   |
| `music()`     | `%USERPROFILE%\Music`         | `$HOME/Music`                         | `XDG_MUSIC_DIR` (default: `$HOME/Music`)          |
| `pictures()`  | `%USERPROFILE%\Pictures`      | `$HOME/Pictures`                      | `XDG_PICTURES_DIR` (default: `$HOME/Pictures`)    |
| `videos()`    | `%USERPROFILE%\Videos`        | `$HOME/Movies`                        | `XDG_VIDEOS_DIR` (default: `$HOME/Videos`)        |

Paths above are examples: Windows paths are retrieved via `SHGetKnownFolderPath`, Linux user directories are read
from the environment and `$XDG_CONFIG_HOME/user-dirs.dirs`.

# Building
Requires JDK 25:
```shell
./gradlew build
./gradlew spotlessApply
```

# Releasing
Pushing a tag like `2.0.0` publishes to Maven Central and GitHub Packages, creates a GitHub release
and updates the [site](https://sanyarnd.github.io/standardpaths/).

Secrets: `MAVEN_CENTRAL_USERNAME`, `MAVEN_CENTRAL_PASSWORD` ([Central Portal token](https://central.sonatype.com/account)),
`GPG_PRIVATE_KEY`, `GPG_KEY_PASSPHRASE`.

# Changelog
See [CHANGELOG.md](CHANGELOG.md).
