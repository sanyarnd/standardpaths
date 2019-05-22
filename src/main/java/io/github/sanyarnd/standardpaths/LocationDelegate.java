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
 * Location delegate interface, mimics {@link StandardPaths}, helps to improve code readability.
 *
 * @author Alexander Biryukov
 */
interface LocationDelegate {
    @NonNull
    Path cache();

    @NonNull
    Path config();

    @NonNull
    Path data();

    @NonNull
    Path dataLocal();

    @NonNull
    Path temp();

    @NonNull
    Path home();

    @NonNull
    Path desktop();

    @NonNull
    Path documents();

    @NonNull
    Path downloads();

    @NonNull
    Path music();

    @NonNull
    Path pictures();

    @NonNull
    Path videos();
}
