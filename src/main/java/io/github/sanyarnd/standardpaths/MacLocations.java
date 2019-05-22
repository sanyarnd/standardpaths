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

import java.nio.file.Path;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * MacOS locations.
 *
 * @author Alexander Biryukov
 */
final class MacLocations implements LocationDelegate {
    @NonNull
    @Override
    public Path cache() {
        throw new IllegalArgumentException("Not implemented");
    }

    @NonNull
    @Override
    public Path config() {
        throw new IllegalArgumentException("Not implemented");
    }

    @NonNull
    @Override
    public Path data() {
        throw new IllegalArgumentException("Not implemented");
    }

    @NonNull
    @Override
    public Path dataLocal() {
        throw new IllegalArgumentException("Not implemented");
    }

    @NonNull
    @Override
    public Path temp() {
        throw new IllegalArgumentException("Not implemented");
    }

    @NonNull
    @Override
    public Path home() {
        throw new IllegalArgumentException("Not implemented");
    }

    @NonNull
    @Override
    public Path desktop() {
        throw new IllegalArgumentException("Not implemented");
    }

    @NonNull
    @Override
    public Path documents() {
        throw new IllegalArgumentException("Not implemented");
    }

    @NonNull
    @Override
    public Path downloads() {
        throw new IllegalArgumentException("Not implemented");
    }

    @NonNull
    @Override
    public Path music() {
        throw new IllegalArgumentException("Not implemented");
    }

    @NonNull
    @Override
    public Path pictures() {
        throw new IllegalArgumentException("Not implemented");
    }

    @NonNull
    @Override
    public Path videos() {
        throw new IllegalArgumentException("Not implemented");
    }
}
