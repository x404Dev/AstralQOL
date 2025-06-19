package com.x404dev.astralQOL.config;

import com.x404dev.astralQOL.AstralQOL;
import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;

/**
 * A comprehensive configuration manager that handles YAML files with automatic
 * default value updating, path prefixes, and header management.
 */
public class AstralConfig extends YamlConfiguration {

    private final AstralQOL plugin;
    private final File configFile;
    private final YamlConfiguration defaultConfig;
    private String[] fileHeader;
    /**
     * -- GETTER --
     *  Gets the current path prefix.
     */
    @Getter
    private String pathPrefix;

    /**
     * Creates a new config manager with default file support.
     *
     * @param plugin   The plugin instance
     * @param fileName The config file name (e.g., "config.yml")
     */
    public AstralConfig(AstralQOL plugin, String fileName) {
        this(plugin, fileName, true);
    }

    /**
     * Creates a new config manager with optional default file support.
     *
     * @param plugin       The plugin instance
     * @param fileName     The config file name
     * @param useDefaults  Whether to use defaults from plugin resources
     */
    public AstralConfig(AstralQOL plugin, String fileName, boolean useDefaults) {
        this.plugin = Objects.requireNonNull(plugin, "Plugin cannot be null");

        // Load default configuration from plugin resources
        if (useDefaults) {
            try (InputStream defaultStream = plugin.getResource(fileName)) {
                if (defaultStream == null) {
                    throw new IllegalArgumentException("Default config file '" + fileName + "' not found in plugin resources");
                }
                this.defaultConfig = YamlConfiguration.loadConfiguration(
                        new InputStreamReader(defaultStream, StandardCharsets.UTF_8)
                );
            } catch (IOException e) {
                throw new RuntimeException("Failed to load default config: " + fileName, e);
            }
        } else {
            this.defaultConfig = null;
        }

        // Extract or create the config file
        this.configFile = extractConfigFile(fileName);

        // Load the configuration
        loadConfiguration();
    }

    /**
     * Sets the header that appears at the top of the config file when saved.
     *
     * @param header Array of header lines
     */
    public void setHeader(String... header) {
        this.fileHeader = header;
    }

    /**
     * Gets the current file header.
     *
     * @return The header lines or null if not set
     */
    public String[] getHeader() {
        return fileHeader;
    }

    /**
     * Sets a path prefix that gets prepended to all get/set operations.
     * Only works when default config is not used.
     *
     * @param prefix The path prefix (e.g., "players.uuid")
     */
    public void setPathPrefix(String prefix) {
        if (defaultConfig != null && prefix != null) {
            plugin.getLogger().warning("Path prefix ignored when using default config file");
            return;
        }
        this.pathPrefix = prefix;
    }

    /**
     * Writes a value to the config and saves it immediately.
     *
     * @param path  The configuration path
     * @param value The value to write
     */
    public void write(String path, Object value) {
        set(path, value);
        save();
        reload();
    }

    /**
     * Saves the configuration to disk.
     */
    public void save() {
        try {
            // Set header if available
            if (fileHeader != null && fileHeader.length > 0) {
                options().setHeader(Arrays.asList(fileHeader));
            }

            super.save(configFile);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to save config: " + configFile.getName(), e);
        }
    }

    /**
     * Reloads the configuration from disk.
     */
    public void reload() {
        save();
        loadConfiguration();
    }

    /**
     * Deletes the configuration file from disk.
     * The loaded config remains in memory.
     */
    public void delete() {
        try {
            if (configFile.exists() && !configFile.delete()) {
                plugin.getLogger().warning("Failed to delete config file: " + configFile.getName());
            }
        } catch (SecurityException e) {
            plugin.getLogger().log(Level.WARNING, "No permission to delete config file: " + configFile.getName(), e);
        }
    }

    /**
     * Gets the config file handle.
     *
     * @return The config file
     */
    public File getFile() {
        return configFile;
    }

    @Override
    public Object get(@NotNull String path, Object defaultValue) {
        // Handle default value updates
        if (defaultConfig != null) {
            validateDefaultValue(path, defaultValue);

            if (super.get(path, null) == null) {
                Object defaultFromFile = defaultConfig.get(path);
                if (defaultFromFile != null) {
                    logConfigUpdate(path, defaultFromFile);
                    write(path, defaultFromFile);
                } else {
                    plugin.getLogger().warning("Default value for '" + path + "' not found in default config");
                }
            }
        }

        // Apply path prefix if configured
        String finalPath = shouldApplyPrefix() ? pathPrefix + "." + path : path;
        return super.get(finalPath, null);
    }

    @Override
    public void set(@NotNull String path, Object value) {
        // Apply path prefix if configured
        String finalPath = shouldApplyPrefix() ? pathPrefix + "." + path : path;
        super.set(finalPath, value);
    }

    private void validateDefaultValue(String path, Object defaultValue) {
        if (defaultValue != null && !isPrimitiveOrWrapper(defaultValue.getClass())) {
            throw new IllegalArgumentException(
                    "Default value must be null when using default config file. Path: " + path +
                            ", provided default: " + defaultValue
            );
        }
    }

    private boolean shouldApplyPrefix() {
        return defaultConfig == null && pathPrefix != null && !isCalledFromConfigSection();
    }

    private boolean isCalledFromConfigSection() {
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        for (int i = 2; i < Math.min(stack.length, 5); i++) {
            String methodName = stack[i].getMethodName();
            if ("getConfigurationSection".equals(methodName)) {
                return true;
            }
        }
        return false;
    }

    private void loadConfiguration() {
        try {
            super.load(configFile);
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to load config: " + configFile.getName(), e);
        }
    }

    private File extractConfigFile(String fileName) {
        File file = new File(plugin.getDataFolder(), fileName);

        if (file.exists()) {
            return file;
        }

        // Create directories if needed
        createDirectories(fileName);

        // Copy default file if it exists
        if (defaultConfig != null) {
            try (InputStream resourceStream = plugin.getResource(fileName)) {
                if (resourceStream != null) {
                    Files.copy(resourceStream, Paths.get(file.toURI()), StandardCopyOption.REPLACE_EXISTING);
                }
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "Failed to extract default config: " + fileName, e);
            }
        } else {
            // Create empty file
            try {
                file.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "Failed to create config file: " + fileName, e);
            }
        }

        return file;
    }

    private void createDirectories(String fileName) {
        File dataFolder = plugin.getDataFolder();
        int lastSlash = fileName.lastIndexOf('/');

        if (lastSlash >= 0) {
            File directory = new File(dataFolder, fileName.substring(0, lastSlash));
            directory.mkdirs();
        } else {
            dataFolder.mkdirs();
        }
    }

    private void logConfigUpdate(String path, Object value) {
        plugin.getLogger().info(String.format(
                "Updating %s: Set '%s' to '%s'",
                configFile.getName(), path, value
        ));
    }

    private static boolean isPrimitiveOrWrapper(Class<?> clazz) {
        return clazz.isPrimitive() || WRAPPER_TYPES.contains(clazz) || clazz == String.class;
    }

    private static final Set<Class<?>> WRAPPER_TYPES = createWrapperTypes();

    private static Set<Class<?>> createWrapperTypes() {
        Set<Class<?>> wrappers = new HashSet<>();
        wrappers.add(Boolean.class);
        wrappers.add(Character.class);
        wrappers.add(Byte.class);
        wrappers.add(Short.class);
        wrappers.add(Integer.class);
        wrappers.add(Long.class);
        wrappers.add(Float.class);
        wrappers.add(Double.class);
        wrappers.add(Void.class);
        return wrappers;
    }
}