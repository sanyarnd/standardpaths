package io.github.sanyarnd.standardpaths;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/// [WindowsRegistry] implementation based on the `reg.exe` tool, which is available on every Windows installation.
///
/// @author Alexander Biryukov
final class RegQueryRegistry implements WindowsRegistry {
    static final String USER_SHELL_FOLDERS =
            "HKEY_CURRENT_USER\\Software\\Microsoft\\Windows\\CurrentVersion\\Explorer\\User Shell Folders";

    private static final Pattern VALUE = Pattern.compile("^ {4}(.+?) {4}(REG_SZ|REG_EXPAND_SZ)(?: {4}(.*))?$");
    private static final long TIMEOUT_SECONDS = 10;

    private final Environment env;

    RegQueryRegistry(final Environment environment) {
        env = environment;
    }

    @Override
    public Map<String, String> userShellFolders() {
        try {
            return parse(query(USER_SHELL_FOLDERS));
        } catch (IOException e) {
            return Map.of();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return Map.of();
        }
    }

    /// Parses `reg query` output, only string values are returned.
    ///
    /// @param lines command output
    /// @return value name to value data
    static Map<String, String> parse(final List<String> lines) {
        final Map<String, String> result = new HashMap<>();
        for (final String line : lines) {
            final Matcher matcher = VALUE.matcher(line);
            if (matcher.matches()) {
                final String data = matcher.group(3);
                result.put(matcher.group(1), data == null ? "" : data.trim());
            }
        }
        return result;
    }

    // output is redirected to the file: reading from the pipe may block forever if the process hangs
    private List<String> query(final String key) throws IOException, InterruptedException {
        final Path output = Files.createTempFile("standard-paths-reg", ".txt");
        try {
            final Process process = new ProcessBuilder(regExecutable(), "query", key)
                    .redirectErrorStream(true)
                    .redirectOutput(output.toFile())
                    .redirectInput(ProcessBuilder.Redirect.PIPE)
                    .start();
            process.getOutputStream().close();
            if (!process.waitFor(TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
                process.destroyForcibly();
                return List.of();
            }
            if (process.exitValue() != 0) {
                return List.of();
            }
            return Files.readAllLines(output, outputCharset());
        } finally {
            Files.deleteIfExists(output);
        }
    }

    // absolute path protects from PATH hijacking
    private String regExecutable() {
        return env.envPath("SystemRoot")
                .map(root -> root.resolve("System32").resolve("reg.exe").toString())
                .orElse("reg.exe");
    }

    // console tools write in the ANSI code page, `native.encoding` is available since Java 17
    private Charset outputCharset() {
        final String encoding = env.getProperty("native.encoding");
        if (encoding == null) {
            return Charset.defaultCharset();
        }
        try {
            return Charset.forName(encoding);
        } catch (IllegalArgumentException e) {
            return StandardCharsets.UTF_8;
        }
    }
}
