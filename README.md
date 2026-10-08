# Standard Paths
[![Build](https://github.com/sanyarnd/standardpaths/actions/workflows/build.yml/badge.svg)](https://github.com/sanyarnd/standardpaths/actions/workflows/build.yml)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.sanyarnd/standard-paths)](https://central.sonatype.com/artifact/io.github.sanyarnd/standard-paths)
[![Javadoc](https://javadoc.io/badge2/io.github.sanyarnd/standard-paths/javadoc.svg)](https://javadoc.io/doc/io.github.sanyarnd/standard-paths)

Standard Paths is a small library which provides cross platform access to the common directories such as `AppData`,
`Desktop` or `tmp`.

# Features
* Windows, macOS, Linux and other Unix-like systems
* Follows platform conventions: Known Folders on Windows, XDG Base Directory and `xdg-user-dirs` on Linux
* Application-specific directories
* No dependencies
* JDK 11+ support

# Quick Start
```java
// application directories
AppPaths app = StandardPaths.forApp("my-app");
Path config = app.config().orElseThrow();   // ~/.config/my-app, %APPDATA%\my-app\config, ...
Path cache = app.cache().orElseThrow();     // ~/.cache/my-app, %LOCALAPPDATA%\my-app\cache, ...

// common directories
Path downloads = StandardPaths.downloads().orElseThrow();
Optional<Path> runtime = StandardPaths.runtime(); // empty on Windows and macOS
```

All returned paths are absolute, but directories are not guaranteed to exist.

A method returns an empty `Optional` if the system has no such directory (e.g. `runtime()` on Windows) or the path
can't be determined (e.g. `$HOME` is not set).

More details can be found in [JavaDocs](https://javadoc.io/doc/io.github.sanyarnd/standard-paths).

# Download
Maven:
```xml
<dependency>
    <groupId>io.github.sanyarnd</groupId>
    <artifactId>standard-paths</artifactId>
    <version>2.0.0</version>
</dependency>
```

Gradle:
```kotlin
implementation("io.github.sanyarnd:standard-paths:2.0.0")
```

Jars are also available in [GitHub Packages](https://github.com/sanyarnd/standardpaths/packages)
and on the [releases](https://github.com/sanyarnd/standardpaths/releases) page.

# Available paths
| Method          | Windows                    | macOS                               | Linux                                            |
|-----------------|----------------------------|-------------------------------------|--------------------------------------------------|
| `home()`        | `%USERPROFILE%`            | `$HOME`                             | `$HOME`                                          |
| `temp()`        | `GetTempPath`              | `$TMPDIR`                           | `$TMPDIR` (default: `/tmp`)                      |
| `cache()`       | `%LOCALAPPDATA%`           | `$HOME/Library/Caches`              | `$XDG_CACHE_HOME` (default: `$HOME/.cache`)      |
| `config()`      | `%APPDATA%`                | `$HOME/Library/Application Support` | `$XDG_CONFIG_HOME` (default: `$HOME/.config`)    |
| `data()`        | `%APPDATA%`                | `$HOME/Library/Application Support` | `$XDG_DATA_HOME` (default: `$HOME/.local/share`) |
| `dataLocal()`   | `%LOCALAPPDATA%`           | `$HOME/Library/Application Support` | `$XDG_DATA_HOME` (default: `$HOME/.local/share`) |
| `state()`       | `%LOCALAPPDATA%`           | `$HOME/Library/Application Support` | `$XDG_STATE_HOME` (default: `$HOME/.local/state`)|
| `runtime()`     | none                       | none                                | `$XDG_RUNTIME_DIR`                               |
| `desktop()`     | `%USERPROFILE%\Desktop`    | `$HOME/Desktop`                     | `XDG_DESKTOP_DIR` (default: `$HOME/Desktop`)     |
| `documents()`   | `%USERPROFILE%\Documents`  | `$HOME/Documents`                   | `XDG_DOCUMENTS_DIR` (default: `$HOME/Documents`) |
| `downloads()`   | `%USERPROFILE%\Downloads`  | `$HOME/Downloads`                   | `XDG_DOWNLOAD_DIR` (default: `$HOME/Downloads`)  |
| `music()`       | `%USERPROFILE%\Music`      | `$HOME/Music`                       | `XDG_MUSIC_DIR` (default: `$HOME/Music`)         |
| `pictures()`    | `%USERPROFILE%\Pictures`   | `$HOME/Pictures`                    | `XDG_PICTURES_DIR` (default: `$HOME/Pictures`)   |
| `videos()`      | `%USERPROFILE%\Videos`     | `$HOME/Movies`                      | `XDG_VIDEOS_DIR` (default: `$HOME/Videos`)       |
| `templates()`   | `%APPDATA%\Microsoft\Windows\Templates` | none                 | `XDG_TEMPLATES_DIR` (default: `$HOME/Templates`) |
| `publicShare()` | `%PUBLIC%`                 | `$HOME/Public`                      | `XDG_PUBLICSHARE_DIR` (default: `$HOME/Public`)  |

`AppPaths` returned by `StandardPaths.forApp(name)` appends the application name; on Windows it also appends the kind
of the directory (`cache`, `config`, `data`, `state`), because local and roaming directories are shared.

Windows user directories are read from the registry (`User Shell Folders`, via `reg.exe`, once per JVM), so moved
folders are supported. Linux user directories are read from the environment and `$XDG_CONFIG_HOME/user-dirs.dirs`.

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
