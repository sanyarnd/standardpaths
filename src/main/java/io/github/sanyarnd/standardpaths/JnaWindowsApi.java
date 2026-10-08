package io.github.sanyarnd.standardpaths;

import com.sun.jna.LastErrorException;
import com.sun.jna.platform.win32.Guid;
import com.sun.jna.platform.win32.Kernel32Util;
import com.sun.jna.platform.win32.KnownFolders;
import com.sun.jna.platform.win32.Shell32Util;
import java.nio.file.Path;
import java.nio.file.Paths;

/// [WindowsApi] implementation based on JNA, failures are reported as `Win32Exception` (a `LastErrorException`).
///
/// Native libraries are loaded lazily on the first call.
///
/// @author Alexander Biryukov
final class JnaWindowsApi implements WindowsApi {
    @Override
    public Path knownFolder(final KnownFolder folder) {
        try {
            return Paths.get(Shell32Util.getKnownFolderPath(guid(folder))).toAbsolutePath();
        } catch (LastErrorException e) {
            throw new NoSuchPathException("Unable to retrieve known folder " + folder, e);
        }
    }

    @Override
    public Path tempDirectory() {
        try {
            return Paths.get(Kernel32Util.getTempPath()).toAbsolutePath();
        } catch (LastErrorException e) {
            throw new NoSuchPathException("Unable to retrieve temp directory", e);
        }
    }

    private static Guid.GUID guid(final KnownFolder folder) {
        switch (folder) {
            case PROFILE:
                return KnownFolders.FOLDERID_Profile;
            case LOCAL_APP_DATA:
                return KnownFolders.FOLDERID_LocalAppData;
            case ROAMING_APP_DATA:
                return KnownFolders.FOLDERID_RoamingAppData;
            case DESKTOP:
                return KnownFolders.FOLDERID_Desktop;
            case DOCUMENTS:
                return KnownFolders.FOLDERID_Documents;
            case DOWNLOADS:
                return KnownFolders.FOLDERID_Downloads;
            case MUSIC:
                return KnownFolders.FOLDERID_Music;
            case PICTURES:
                return KnownFolders.FOLDERID_Pictures;
            case VIDEOS:
                return KnownFolders.FOLDERID_Videos;
        }
        throw new AssertionError("Unknown folder: " + folder);
    }
}
