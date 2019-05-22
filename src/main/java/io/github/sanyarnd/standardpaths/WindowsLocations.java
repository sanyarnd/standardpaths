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
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import org.checkerframework.checker.nullness.qual.NonNull;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Advapi32;
import com.sun.jna.platform.win32.Guid;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.Kernel32Util;
import com.sun.jna.platform.win32.KnownFolders;
import com.sun.jna.platform.win32.Shell32;
import com.sun.jna.platform.win32.ShlObj;
import com.sun.jna.platform.win32.WinError;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;

/**
 * Windows locations.
 *
 * @author Alexander Biryukov
 */
final class WindowsLocations implements LocationDelegate {
    /**
     * Get the known folder path.
     *
     * @param folderId known folder ID
     * @return path to known folder
     * @throws NoSuchPathException if unable to retrieve path
     */
    @NonNull
    private static Path knownFolder(final Guid.@NonNull GUID folderId) {
        PointerByReference ppStr = new PointerByReference();
        WinNT.HRESULT ret = Shell32.INSTANCE.SHGetKnownFolderPath(folderId,
            ShlObj.KNOWN_FOLDER_FLAG.DONT_VERIFY.getFlag(), null, ppStr);

        if (WinError.S_OK.equals(ret)) {
            Pointer pStr = ppStr.getValue();
            StringBuilder sb = new StringBuilder();
            // we'll never reach 4096 anyway
            final int maxLengthInBytes = 4096;
            for (int i = 0; i < maxLengthInBytes; ++i) {
                // SHGetKnownFolderPath returns wchar_t string, and offset is calculated in bytes
                // so we actually need i*2 for correct indexation
                char c = pStr.getChar(i * 2);
                if (c == '\u0000') {
                    break;
                }
                sb.append(c);
            }
            return Paths.get(sb.toString()).toAbsolutePath();
        } else {
            String message = getLastErrorString();
            throw new NoSuchPathException(message);
        }
    }

    @NonNull
    private static String getLastErrorString() {
        int errorCode = Kernel32.INSTANCE.GetLastError();
        return Kernel32Util.formatMessage(errorCode);
    }

    @NonNull
    private static Path localAppData() {
        return knownFolder(KnownFolders.FOLDERID_LocalAppData);
    }

    @NonNull
    private static Path roamingAppData() {
        return knownFolder(KnownFolders.FOLDERID_RoamingAppData);
    }

    @NonNull
    @Override
    public Path cache() {
        return localAppData();
    }

    @NonNull
    @Override
    public Path config() {
        return localAppData();
    }

    @NonNull
    @Override
    public Path data() {
        return roamingAppData();
    }

    @NonNull
    @Override
    public Path dataLocal() {
        return localAppData();
    }

    @NonNull
    @Override
    public Path temp() {
        return Paths.get(Kernel32Util.getTempPath()).toAbsolutePath();
    }

    @NonNull
    @Override
    public Path home() {
        return userenvProfileDirectory().orElse(environmentProfileDirectory());
    }

    @NonNull
    private Optional<Path> userenvProfileDirectory() {
        // get process handle and create associated query token (required by GetUserProfileDirectory)
        WinNT.HANDLE processHandle = Kernel32.INSTANCE.GetCurrentProcess();
        WinNT.HANDLEByReference pToken = new WinNT.HANDLEByReference();
        boolean isOpened = Advapi32.INSTANCE.OpenProcessToken(processHandle, WinNT.TOKEN_QUERY, pToken);

        if (isOpened) {
            IntByReference pSize = new IntByReference(0);
            // this call must return false, and pSize must contain the required buffer size
            boolean failedCall = Userenv.INSTANCE.GetUserProfileDirectory(pToken.getValue(), null, pSize);
            if (!failedCall && pSize.getValue() > 0) {
                // now it's the real call
                char[] buf = new char[pSize.getValue()];
                boolean userDirRetrieved = Userenv.INSTANCE.GetUserProfileDirectory(pToken.getValue(), buf, pSize);

                // close token once we don't need it, ignore ret value
                Kernel32.INSTANCE.CloseHandle(pToken.getValue());

                if (userDirRetrieved) {
                    String userDir = Native.toString(buf);
                    Path path = Paths.get(userDir).toAbsolutePath();
                    return Optional.of(path);
                }
            } else {
                // close token once we don't need it, ignore ret value
                Kernel32.INSTANCE.CloseHandle(pToken.getValue());
            }
        }
        return Optional.empty();
    }

    @NonNull
    private Path environmentProfileDirectory() {
        String userProfile = System.getenv("USERPROFILE");
        String homeVariables = System.getenv("HOMEDRIVE") + System.getenv("HOMEPATH");
        String homeVariable = System.getenv("HOME");

        return Stream.of(userProfile, homeVariables, homeVariable)
            .filter(Objects::nonNull).findFirst()
            .map(Paths::get)
            .orElseThrow(() -> new NoSuchPathException("Unable to retrieve path for directory"));
    }

    @NonNull
    @Override
    public Path desktop() {
        return knownFolder(KnownFolders.FOLDERID_Desktop);
    }

    @NonNull
    @Override
    public Path documents() {
        return knownFolder(KnownFolders.FOLDERID_Documents);
    }

    @NonNull
    @Override
    public Path downloads() {
        return knownFolder(KnownFolders.FOLDERID_Downloads);
    }

    @NonNull
    @Override
    public Path music() {
        return knownFolder(KnownFolders.FOLDERID_Music);
    }

    @NonNull
    @Override
    public Path pictures() {
        return knownFolder(KnownFolders.FOLDERID_Pictures);
    }

    @NonNull
    @Override
    public Path videos() {
        return knownFolder(KnownFolders.FOLDERID_Videos);
    }
}
