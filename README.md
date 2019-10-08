# Standard Paths
[![Build Status](https://travis-ci.com/sanyarnd/standardpaths.svg?branch=master)](https://travis-ci.com/sanyarnd/standardpaths)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=io.github.sanyarnd%3Astandard-paths&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=io.github.sanyarnd%3Astandard-paths)

Standard Paths is a small library which provides cross platform access to the common directories such as `AppData`, `Desktop` or `tmp`.

Target JVM version is 8.

# Features
* All major distributions: `Windows`, `Linux`, `MacOS` (not yet implemented)
* Ease of use
* NIO-based 
* Lightweight (~10kb)

The package has [jna-platform](https://github.com/java-native-access/jna) as transitive dependency (~2.5mb).

# Quick Start
Access `StandardPaths` class and follow autocomplete suggestions:
```java
Path home = StandardPaths.home();
Path cache = StandardPaths.cache();
```

Be aware that all `StandardPaths` methods might throw unchecked `NoSuchPathException` if it's impossible to obtain required path.

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
```gradle
compile 'io.github.sanyarnd:standard-paths:1.0.2'
```
 
Standalone jars are available on [releases](https://github.com/sanyarnd/standardpaths/releases) page.

More download options available in [Bintray](https://bintray.com/sanya-rnd/maven-projects/standardpaths) repository.

# Available paths (examples)
Paths below are mere examples. 

Internal implementation will always do its best utilizing system API (`WinAPI`, `freedesktop` etc) and retrieve the real path.
 
More details can be found in [JavaDocs](https://sanyarnd.github.io/standardpaths/apidocs/index.html).

#### Cache
* `Windows`: `%USERPROFILE%/AppData/Local`
* `Linux`: `$XDG_CACHE_HOME` (default: `$HOME/.cache`)

#### Config
* `Windows`: `%USERPROFILE%/AppData/Local`
* `Linux`: `$XDG_CONFIG_HOME` (default: `$HOME/.config`)


#### Data
* `Windows`: `%USERPROFILE%/AppData/Roaming`
* `Linux`: `$HOME/.local/share`

#### Local data
* `Windows`: `%USERPROFILE%/AppData/Local`
* `Linux`: `$HOME/.local/share`

#### Temp directory
* `Windows`: `%USERPROFILE%/AppData/Local/Temp`
* `Linux`: `/tmp`

#### Home
* `Windows`: `%USERPROFILE%`
* `Linux`: `~`

#### User directories

desktop:
* `Windows`: `%USERPROFILE%/Desktop`
* `Linux`: `~/Desktop`

documents:
* `Windows`: `%USERPROFILE%/Documents`
* `Linux`: `~/Documents`

downloads:
* `Windows`: `%USERPROFILE%/Downloads`
* `Linux`: `~/Downloads`

music:
* `Windows`: `%USERPROFILE%/Music`
* `Linux`: `~/Music`

pictures:
* `Windows`: `%USERPROFILE%/Pictures`
* `Linux`: `~/Pictures`

videos:
* `Windows`: `%USERPROFILE%/Videos`
* `Linux`: `~/Videos`

# Changelog
See [CHANGELOG.md](CHANGELOG.md).
