package io.github.sanyarnd.standardpaths;

import java.util.Map;

/// Read access to the Windows registry, replaceable in tests.
///
/// @author Alexander Biryukov
interface WindowsRegistry {
    /// Values of `HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Explorer\User Shell Folders`.
    ///
    /// Values are not expanded, e.g. `%USERPROFILE%\Desktop`.
    ///
    /// @return value name to value data, empty if the key can't be read
    Map<String, String> userShellFolders();
}
