// This is an open source non-commercial project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com
//
//                     Copyright 2019 Alexander Biryukov
//
//     Licensed under the Apache License, Version 2.0 (the "License");
//     you may not use this file except in compliance with the License.
//     You may obtain a copy of the License at
//
//               http://www.apache.org/licenses/LICENSE-2.0
//
//     Unless required by applicable law or agreed to in writing, software
//     distributed under the License is distributed on an "AS IS" BASIS,
//     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//     See the License for the specific language governing permissions and
//     limitations under the License.

package io.github.sanyarnd.standardpaths;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Stream;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Linux locations.
 *
 * @author Alexander Biryukov
 */
final class LinuxLocations implements LocationDelegate {
    private static final String XDG_CACHE_HOME = "XDG_CACHE_HOME";
    private static final String XDG_CONFIG_HOME = "XDG_CONFIG_HOME";
    private static final String XDG_DATA_HOME = "XDG_DATA_HOME";
    private static final String XDG_DESKTOP_DIR = "XDG_DESKTOP_DIR";
    private static final String XDG_DOCUMENTS_DIR = "XDG_DOCUMENTS_DIR";
    private static final String XDG_DOWNLOAD_DIR = "XDG_DOWNLOAD_DIR";
    private static final String XDG_MUSIC_DIR = "XDG_MUSIC_DIR";
    private static final String XDG_PICTURES_DIR = "XDG_PICTURES_DIR";
    private static final String XDG_VIDEOS_DIR = "XDG_VIDEOS_DIR";

    @NonNull
    private static Optional<String> getVariable(@NonNull final String variable) {
        String result = System.getenv(variable);
        return result == null || result.trim().length() == 0 ? Optional.empty() : Optional.of(result);
    }

    @NonNull
    @SafeVarargs
    private static Path takeFirstPath(@NonNull final Optional<String>... candidates) {
        return Stream.of(candidates)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .findFirst()
            .map(Paths::get)
            .orElseThrow(() -> new NoSuchPathException("Unable to retrieve path for directory"));
    }

    @NonNull
    @Override
    public Path cache() {
        Optional<String> xdgCache = getVariable(XDG_CACHE_HOME);
        Optional<String> home = getVariable("HOME").map(s -> s + File.separator + ".cache");
        return takeFirstPath(xdgCache, home);
    }

    @NonNull
    @Override
    public Path config() {
        Optional<String> xdgConfig = getVariable(XDG_CONFIG_HOME);
        Optional<String> home = getVariable("HOME").map(s -> s + File.separator + ".config");

        return takeFirstPath(xdgConfig, home);
    }

    @NonNull
    @Override
    public Path data() {
        Optional<String> xdgData = getVariable(XDG_DATA_HOME);
        Optional<String> home = getVariable("HOME").map(s -> s + File.separator + ".local" + File.separator + "share");

        return takeFirstPath(xdgData, home);
    }

    @NonNull
    @Override
    public Path dataLocal() {
        return data();
    }

    @NonNull
    @Override
    public Path temp() {
        return Paths.get("/tmp");
    }

    @NonNull
    @Override
    public Path home() {
        Optional<String> home = getVariable("HOME");
        Optional<String> tilde = Optional.of("~");

        return takeFirstPath(home, tilde);
    }

    @NonNull
    @Override
    public Path desktop() {
        Optional<String> xdgDesktop = getVariable(XDG_DESKTOP_DIR);
        Optional<String> home = getVariable("HOME").map(s -> s + File.separator + "Desktop");

        return takeFirstPath(xdgDesktop, home);
    }

    @NonNull
    @Override
    public Path documents() {
        Optional<String> xdgDocuments = getVariable(XDG_DOCUMENTS_DIR);
        Optional<String> home = getVariable("HOME").map(s -> s + File.separator + "Documents");

        return takeFirstPath(xdgDocuments, home);
    }

    @NonNull
    @Override
    public Path downloads() {
        Optional<String> xdgDownloads = getVariable(XDG_DOWNLOAD_DIR);
        Optional<String> home = getVariable("HOME").map(s -> s + File.separator + "Downloads");

        return takeFirstPath(xdgDownloads, home);
    }

    @NonNull
    @Override
    public Path music() {
        Optional<String> xdgMusic = getVariable(XDG_MUSIC_DIR);
        Optional<String> home = getVariable("HOME").map(s -> s + File.separator + "Music");

        return takeFirstPath(xdgMusic, home);
    }

    @NonNull
    @Override
    public Path pictures() {
        Optional<String> xdgPictures = getVariable(XDG_PICTURES_DIR);
        Optional<String> home = getVariable("HOME").map(s -> s + File.separator + "Pictures");

        return takeFirstPath(xdgPictures, home);
    }

    @NonNull
    @Override
    public Path videos() {
        Optional<String> xdgVideos = getVariable(XDG_VIDEOS_DIR);
        Optional<String> home = getVariable("HOME").map(s -> s + File.separator + "Videos");

        return takeFirstPath(xdgVideos, home);
    }
}
