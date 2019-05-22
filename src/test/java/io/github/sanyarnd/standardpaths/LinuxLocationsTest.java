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
import org.junit.jupiter.api.condition.OS;

/**
 * @author Alexander Biryukov
 */
public class LinuxLocationsTest {
    private LinuxLocations locations = new LinuxLocations();

    @Test
    @EnabledOnOs(org.junit.jupiter.api.condition.OS.LINUX)
    public void cache_test() { Assertions.assertNotNull(locations.cache()); }

    @Test
    @EnabledOnOs(org.junit.jupiter.api.condition.OS.LINUX)
    public void config_test() { Assertions.assertNotNull(locations.config()); }

    @Test
    @EnabledOnOs(org.junit.jupiter.api.condition.OS.LINUX)
    public void data_test() { Assertions.assertNotNull(locations.data()); }

    @Test
    @EnabledOnOs(org.junit.jupiter.api.condition.OS.LINUX)
    public void dataLocal_test() { Assertions.assertNotNull(locations.dataLocal()); }

    @Test
    @EnabledOnOs(org.junit.jupiter.api.condition.OS.LINUX)
    public void temp_test() { Assertions.assertNotNull(locations.temp()); }

    @Test
    @EnabledOnOs(org.junit.jupiter.api.condition.OS.LINUX)
    public void home_test() { Assertions.assertNotNull(locations.home()); }

    @Test
    @EnabledOnOs(org.junit.jupiter.api.condition.OS.LINUX)
    public void desktop_test() { Assertions.assertNotNull(locations.desktop()); }

    @Test
    @EnabledOnOs(org.junit.jupiter.api.condition.OS.LINUX)
    public void documents_test() { Assertions.assertNotNull(locations.documents()); }

    @Test
    @EnabledOnOs(org.junit.jupiter.api.condition.OS.LINUX)
    public void downloads_test() { Assertions.assertNotNull(locations.downloads()); }

    @Test
    @EnabledOnOs(org.junit.jupiter.api.condition.OS.LINUX)
    public void music_test() { Assertions.assertNotNull(locations.music()); }

    @Test
    @EnabledOnOs(org.junit.jupiter.api.condition.OS.LINUX)
    public void pictures_test() { Assertions.assertNotNull(locations.pictures()); }

    @Test
    @EnabledOnOs(OS.LINUX)
    public void videos_test() { Assertions.assertNotNull(locations.videos()); }
}

