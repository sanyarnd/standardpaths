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

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;

/**
 * Interface to select calls from the Userenv.dll library on Windows.
 *
 * @author Alexander Biryukov
 */
interface Userenv extends StdCallLibrary {
    Userenv INSTANCE = Native.load("userenv", Userenv.class, W32APIOptions.DEFAULT_OPTIONS);

    /**
     * Retrieves the path to the root directory of the specified user's profile.
     *
     * @param phToken      A token for the user, which is returned by the LogonUser,
     *                     CreateRestrictedToken, DuplicateToken, OpenProcessToken, or
     *                     OpenThreadToken function. The token must have TOKEN_QUERY
     *                     access. For more information, see Access Rights for
     *                     Access-Token Objects.
     * @param lpProfileDir A pointer to a buffer that, when this function returns
     *                     successfully, receives the path to the specified user's
     *                     profile directory.
     * @param lpcchSize    Specifies the size of the lpProfileDir buffer, in TCHARs.
     *                     <p>
     *                     If the buffer specified by lpProfileDir is not large enough or
     *                     lpProfileDir is NULL, the function fails and this parameter
     *                     receives the necessary buffer size, including the terminating
     *                     null character.
     * @return TRUE if successful; otherwise, FALSE. To get extended error
     * information, call GetLastError.
     */
    boolean GetUserProfileDirectory(WinNT.@NonNull HANDLE phToken, @Nullable char[] lpProfileDir, @NonNull IntByReference lpcchSize);
}

