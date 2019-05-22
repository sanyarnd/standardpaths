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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;

/**
 * @author Alexander Biryukov
 */
public class OsTest {
    @Test
    @EnabledOnOs(org.junit.jupiter.api.condition.OS.WINDOWS)
    public void windows_test() {
        Assertions.assertTrue(OS.isWindows());
    }

    @Test
    @EnabledOnOs(org.junit.jupiter.api.condition.OS.LINUX)
    public void linux_test() {
        Assertions.assertTrue(OS.isLinux());
    }

    @Test
    @EnabledOnOs(org.junit.jupiter.api.condition.OS.MAC)
    public void mac_test() {
        Assertions.assertTrue(OS.isMac());
    }

    @Test
    @EnabledOnOs(org.junit.jupiter.api.condition.OS.OTHER)
    public void unknown_test() {
        Assertions.assertTrue(OS.isUnknown());
    }
}
