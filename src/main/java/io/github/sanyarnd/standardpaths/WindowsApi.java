package io.github.sanyarnd.standardpaths;

import java.nio.file.Path;

/// Windows system calls, replaceable in tests.
///
/// @author Alexander Biryukov
interface WindowsApi {
    /// Path of the known folder, see `SHGetKnownFolderPath`.
    ///
    /// @param folder known folder
    /// @return absolute path
    /// @throws NoSuchPathException if the system call failed
    Path knownFolder(KnownFolder folder);

    /// Path of the temp directory, see `GetTempPath`.
    ///
    /// @return absolute path
    /// @throws NoSuchPathException if the system call failed
    Path tempDirectory();
}
