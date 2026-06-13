package org.firstinspires.ftc.teamcode.tankscript.yaml;

import org.snakeyaml.engine.v2.api.Dump;
import org.snakeyaml.engine.v2.api.DumpSettings;
import org.snakeyaml.engine.v2.api.Load;
import org.snakeyaml.engine.v2.api.LoadSettings;
import org.snakeyaml.engine.v2.api.StreamDataWriter;
import org.snakeyaml.engine.v2.common.FlowStyle;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A utility class to manage loading, reading, updating, and saving YAML configuration files
 * using the SnakeYAML Engine. Supports nested keys via dot notation (e.g., "database.host").
 */
public class Yaml {
    @NotNull private final InputStream stream;
    @NotNull private final Load yamlLoad;
    @NotNull private final Dump yamlDump;

    @NotNull public Map<String, Object> configData;
    @Nullable private String filePath = null;

    public Yaml(@NotNull InputStream stream) {
        this.stream = stream;

        LoadSettings loadSettings = LoadSettings.builder().build();
        this.yamlLoad = new Load(loadSettings);

        DumpSettings dumpSettings = DumpSettings.builder()
                .setDefaultFlowStyle(FlowStyle.BLOCK)
                .build();
        this.yamlDump = new Dump(dumpSettings);

        reload();
    }

    public Yaml(@NotNull String filePath) throws IOException {
        this(new File(filePath).toURI().toURL().openStream());
        this.filePath = filePath;
    }

    public Yaml(@NotNull File file) throws IOException {
        this(file.toURI().toURL().openStream());
        this.filePath = file.getAbsolutePath();
    }

    public synchronized void reload() {
        Object data = yamlLoad.loadFromInputStream(stream);
        if (data instanceof Map) {
            this.configData = (Map<String, Object>) data;
        } else if (data != null) {
            System.out.println("YAML loaded but root object is not a Map: " + filePath);
            this.configData = new LinkedHashMap<>();
        } else {
            this.configData = new LinkedHashMap<>();
        }
    }

    public synchronized void save() {
        if (filePath == null) {
            throw new RuntimeException("YamlConfig is read-only without a file path specified.");
        }
        File file = new File(filePath);
        try (FileWriter writer = new FileWriter(file)) {
            yamlDump.dump(configData, new StreamDataWriter() {
                @Override
                public void flush() {
                    try {
                        writer.flush();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                @Override
                public void write(@NotNull String str, int off, int len) {
                    try {
                        writer.write(str, off, len);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                @Override
                public void write(@NotNull String str) {
                    try {
                        writer.write(str);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        } catch (IOException e) {
            throw new RuntimeException("Error saving YAML file: " + filePath, e);
        }
    }

    public synchronized void set(@NotNull String key, @Nullable Object value) {
        String[] keys = key.split("\\.");
        Map<String, Object> currentMap = this.configData;
        for (int i = 0; i < keys.length; i++) {
            if (i == keys.length - 1) {
                if (value == null) {
                    currentMap.remove(keys[i]);
                } else {
                    currentMap.put(keys[i], value);
                }
            } else {
                Object nextMap = currentMap.get(keys[i]);
                if (!(nextMap instanceof Map)) {
                    nextMap = new LinkedHashMap<String, Object>();
                    currentMap.put(keys[i], nextMap);
                }
                currentMap = (Map<String, Object>) nextMap;
            }
        }
    }

    @Nullable
    public synchronized Object getNestedValue(@NotNull String key) {
        String[] keys = key.split("\\.");
        Map<?, ?> currentMap = configData;
        Object value = null;

        for (int i = 0; i < keys.length; i++) {
            String segment = keys[i];
            value = currentMap.get(segment);

            if (value == null) {
                try {
                    int intKey = Integer.parseInt(segment);
                    value = currentMap.get(intKey);
                } catch (NumberFormatException ignored) {
                    // Ignore, it's not a number
                }
            }

            if (value == null) {
                return null;
            }

            if (i < keys.length - 1) {
                if (value instanceof Map) {
                    currentMap = (Map<?, ?>) value;
                } else {
                    return null;
                }
            }
        }
        return value;
    }

    // Utility/Type Check Methods

    public boolean isReadOnly() {
        return filePath == null;
    }

    public boolean exists(@NotNull String key) {
        return getNestedValue(key) != null;
    }

    @NotNull
    public Set<String> getKeys() {
        return configData.keySet();
    }

    public boolean isString(@NotNull String key) {
        return getNestedValue(key) instanceof String;
    }

    public boolean isBoolean(@NotNull String key) {
        return getNestedValue(key) instanceof Boolean;
    }

    public boolean isList(@NotNull String key) {
        return getNestedValue(key) instanceof List;
    }

    public boolean isMap(@NotNull String key) {
        return getNestedValue(key) instanceof Map;
    }

    public boolean isNumber(@NotNull String key) {
        return getNestedValue(key) instanceof Number;
    }

    public boolean isInt(@NotNull String key) {
        return getNestedValue(key) instanceof Integer;
    }

    public boolean isLong(@NotNull String key) {
        return getNestedValue(key) instanceof Long;
    }

    public boolean isDouble(@NotNull String key) {
        return getNestedValue(key) instanceof Double;
    }

    public boolean isShort(@NotNull String key) {
        return getNestedValue(key) instanceof Short;
    }

    public boolean isByte(@NotNull String key) {
        return getNestedValue(key) instanceof Byte;
    }

    @Nullable
    public Object geObject(@NotNull String key) {
        return getNestedValue(key);
    }

    @Nullable
    public String getString(@NotNull String key) {
        Object val = getNestedValue(key);
        return val instanceof String ? (String) val : null;
    }

    @Nullable
    public Boolean getBoolean(@NotNull String key) {
        Object val = getNestedValue(key);
        return val instanceof Boolean ? (Boolean) val : null;
    }

    @Nullable
    public Number getNumber(@NotNull String key) {
        Object val = getNestedValue(key);
        return val instanceof Number ? (Number) val : null;
    }

    @Nullable
    public Integer getInt(@NotNull String key) {
        Number num = getNumber(key);
        return num != null ? num.intValue() : null;
    }

    @Nullable
    public Long getLong(@NotNull String key) {
        Number num = getNumber(key);
        return num != null ? num.longValue() : null;
    }

    @Nullable
    public Short getShort(@NotNull String key) {
        Number num = getNumber(key);
        return num != null ? num.shortValue() : null;
    }

    @Nullable
    public Byte getByte(@NotNull String key) {
        Number num = getNumber(key);
        return num != null ? num.byteValue() : null;
    }

    @Nullable
    public Double getDouble(@NotNull String key) {
        Number num = getNumber(key);
        return num != null ? num.doubleValue() : null;
    }

    @Nullable
    public List<?> getList(@NotNull String key) {
        Object val = getNestedValue(key);
        return val instanceof List ? (List<?>) val : null;
    }

    @Nullable
    public Map<String, Object> getMap(@NotNull String key) {
        Object val = getNestedValue(key);
        if (!(val instanceof Map)) {
            return null;
        }
        Map<?, ?> raw = (Map<?, ?>) val;
        Map<String, Object> stringifiedMap = new LinkedHashMap<>();
        for (Map.Entry<?, ?> entry : raw.entrySet()) {
            stringifiedMap.put(entry.getKey().toString(), entry.getValue());
        }
        return stringifiedMap;
    }

    @Nullable
    public synchronized <T> List<T> getFilteredList(@NotNull String key, @NotNull Class<T> clazz) {
        List<?> list = getList(key);
        if (list == null) return null;

        List<T> filtered = new ArrayList<>();
        for (Object item : list) {
            if (clazz.isInstance(item)) {
                filtered.add(clazz.cast(item));
            }
        }
        return filtered;
    }

    @Nullable
    public List<String> getStringList(@NotNull String key) {
        return getFilteredList(key, String.class);
    }

    @Nullable
    public List<Integer> getIntList(@NotNull String key) {
        return getFilteredList(key, Integer.class);
    }

    @Nullable
    public List<Long> getLongList(@NotNull String key) {
        return getFilteredList(key, Long.class);
    }

    @Nullable
    public List<Boolean> getBooleanList(@NotNull String key) {
        return getFilteredList(key, Boolean.class);
    }

    @Nullable
    public List<Double> getDoubleList(@NotNull String key) {
        return getFilteredList(key, Double.class);
    }

    @Nullable
    public List<Short> getShortList(@NotNull String key) {
        return getFilteredList(key, Short.class);
    }

    @Nullable
    public List<Byte> getByteList(@NotNull String key) {
        return getFilteredList(key, Byte.class);
    }
}