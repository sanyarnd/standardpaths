package io.github.sanyarnd.standardpaths;

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
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

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
    private static @NotNull Path knownFolder(final Guid.@NotNull GUID folderId) {
        final PointerByReference ppStr = new PointerByReference();
        final WinNT.HRESULT ret = Shell32.INSTANCE.SHGetKnownFolderPath(folderId,
                ShlObj.KNOWN_FOLDER_FLAG.DONT_VERIFY.getFlag(), null, ppStr);

        if (WinError.S_OK.equals(ret)) {
            final Pointer pStr = ppStr.getValue();
            final StringBuilder sb = new StringBuilder();
            // we'll never reach 4096 anyway
            final long maxLengthInBytes = 4096;
            for (long i = 0; i < maxLengthInBytes; ++i) {
                // SHGetKnownFolderPath returns wchar_t string, and offset is calculated in bytes
                // so we actually need i*2 for correct indexing
                final char c = pStr.getChar(i * 2);
                // function returns null-terminated string
                if (c == '\u0000') {
                    break;
                }
                sb.append(c);
            }
            return Paths.get(sb.toString()).toAbsolutePath();
        } else {
            final String message = getLastErrorString();
            throw new NoSuchPathException(message);
        }
    }

    private static @NotNull String getLastErrorString() {
        final int errorCode = Kernel32.INSTANCE.GetLastError();
        return Kernel32Util.formatMessage(errorCode);
    }

    private static @NotNull Path localAppData() {
        return knownFolder(KnownFolders.FOLDERID_LocalAppData);
    }

    private static @NotNull Path roamingAppData() {
        return knownFolder(KnownFolders.FOLDERID_RoamingAppData);
    }

    @Override
    public @NotNull Path cache() {
        return localAppData();
    }

    @Override
    public @NotNull Path config() {
        return localAppData();
    }

    @Override
    public @NotNull Path data() {
        return roamingAppData();
    }

    @Override
    public @NotNull Path dataLocal() {
        return localAppData();
    }

    @Override
    public @NotNull Path temp() {
        return Paths.get(Kernel32Util.getTempPath()).toAbsolutePath();
    }

    @Override
    public @NotNull Path home() {
        return userenvProfileDirectory().orElse(environmentProfileDirectory());
    }

    private @NotNull Optional<Path> userenvProfileDirectory() {
        // get process handle and create associated query token (required by GetUserProfileDirectory)
        final WinNT.HANDLE processHandle = Kernel32.INSTANCE.GetCurrentProcess();
        final WinNT.HANDLEByReference pToken = new WinNT.HANDLEByReference();
        final boolean isOpened = Advapi32.INSTANCE.OpenProcessToken(processHandle, WinNT.TOKEN_QUERY, pToken);

        if (isOpened) {
            final IntByReference pSize = new IntByReference(0);
            // by documentation, such call must return false with pSize containing required buffer size
            final boolean failedCall = Userenv.INSTANCE.GetUserProfileDirectory(pToken.getValue(), null, pSize);
            if (!failedCall && pSize.getValue() > 0) {
                // now it's the real call
                final char[] buf = new char[pSize.getValue()];
                final boolean userDirRetrieved =
                        Userenv.INSTANCE.GetUserProfileDirectory(pToken.getValue(), buf, pSize);

                // close token once we don't need it, ignore ret value
                Kernel32.INSTANCE.CloseHandle(pToken.getValue());

                if (userDirRetrieved) {
                    final String userDir = Native.toString(buf);
                    final Path path = Paths.get(userDir).toAbsolutePath();
                    return Optional.of(path);
                }
            } else {
                // close token once we don't need it, ignore ret value
                Kernel32.INSTANCE.CloseHandle(pToken.getValue());
            }
        }
        return Optional.empty();
    }

    private static @NotNull Path environmentProfileDirectory() {
        final String userProfile = System.getenv("USERPROFILE");
        final String homeVariables = System.getenv("HOMEDRIVE") + System.getenv("HOMEPATH");
        final String homeVariable = System.getenv("HOME");

        return Stream.of(userProfile, homeVariables, homeVariable)
                .filter(Objects::nonNull).findFirst()
                .map(Paths::get)
                .orElseThrow(() -> new NoSuchPathException("Unable to retrieve path for directory"));
    }

    @Override
    public @NotNull Path desktop() {
        return knownFolder(KnownFolders.FOLDERID_Desktop);
    }

    @Override
    public @NotNull Path documents() {
        return knownFolder(KnownFolders.FOLDERID_Documents);
    }

    @Override
    public @NotNull Path downloads() {
        return knownFolder(KnownFolders.FOLDERID_Downloads);
    }

    @Override
    public @NotNull Path music() {
        return knownFolder(KnownFolders.FOLDERID_Music);
    }

    @Override
    public @NotNull Path pictures() {
        return knownFolder(KnownFolders.FOLDERID_Pictures);
    }

    @Override
    public @NotNull Path videos() {
        return knownFolder(KnownFolders.FOLDERID_Videos);
    }
}
