package io.github.sanyarnd.standardpaths;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
     * @param phToken      A token for the user, which is returned by the LogonUser, CreateRestrictedToken,
     *                     DuplicateToken, OpenProcessToken, or OpenThreadToken function. The token must have
     *                     TOKEN_QUERY access. For more information, see Access Rights for Access-Token Objects.
     * @param lpProfileDir A pointer to a buffer that, when this function returns successfully, receives the path to the
     *                     specified user's profile directory.
     * @param lpcchSize    Specifies the size of the lpProfileDir buffer, in TCHARs. If the buffer specified by
     *                     lpProfileDir is not large enough or lpProfileDir is NULL, the function fails and this
     *                     parameter receives the necessary buffer size, including the terminating null character.
     * @return TRUE if successful; otherwise, FALSE. To get extended error information, call GetLastError.
     */
    @SuppressWarnings("MethodName")
    boolean GetUserProfileDirectory(
            WinNT.@NotNull HANDLE phToken,
            @Nullable char[] lpProfileDir,
            @NotNull IntByReference lpcchSize
    );
}

