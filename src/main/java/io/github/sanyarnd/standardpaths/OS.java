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

import java.util.Locale;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Operation system enumeration.
 *
 * @author Alexander Biryukov
 */
enum OS {
    /**
     * Microsoft Windows operating system.
     */
    WINDOWS,
    /**
     * Linux-based operating system.
     */
    LINUX,
    /**
     * Apple Macintosh operating system.
     */
    MAC,
    /**
     * Other OSes, also if `os.name` is not defined.
     */
    UNKNOWN;

    private static String name = System.getProperty("os.name").toLowerCase(Locale.ENGLISH);

    /**
     * Current OS.
     *
     * @return current OS
     */
    @NonNull
    public static OS current() {
        OS win = name.contains("win") ? WINDOWS : UNKNOWN;
        OS nonLinux = name.contains("mac") ? MAC : win;

        return name.contains("linux") ? LINUX : nonLinux;
    }

    public static boolean isWindows() { return OS.current() == OS.WINDOWS; }

    public static boolean isLinux() { return OS.current() == OS.LINUX; }

    public static boolean isMac() { return OS.current() == OS.MAC; }

    public static boolean isUnknown() { return OS.current() == OS.UNKNOWN; }
}
