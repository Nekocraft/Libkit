package org.bukkit.plugin.java;

import java.io.IOException;
import java.io.InputStream;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.commons.Remapper;

/**
 * Re-maps class references from non-versioned packages to the correct versioned package
 */
public class ClassRemapper extends Remapper {
    private final String[] PACKAGE_ROOTS = { "net/minecraft/server/", "org/bukkit/craftbukkit/" };
    static final ClassRemapper instance = new ClassRemapper();
    private final String version;

    private ClassRemapper() {
        // This is to get the version from maven (with a ending slash)
        String remapped = "org/bukkit/craftbukkit/CraftServer";
        version = remapped.substring(23, 31);
    }

    @Override
    public String mapDesc(String desc) {
        return filter(desc);
    }

    @Override
    public String map(String typeName) {
        return filter(typeName);
    }

    private String filter(String text) {
        int idx;
        for (String packageRoot : PACKAGE_ROOTS) {
            if ((idx = text.indexOf(packageRoot)) != -1) {
                return convert(text, packageRoot, idx);
            }
        }
        return text;
    }

    private String convert(String text, String packagePath, int startIndex) {
        String name = text.substring(startIndex + packagePath.length());
        String header = text.substring(0, startIndex);
        if (name.startsWith("v")) {
            int firstidx = name.indexOf('_');
            if (firstidx != -1) {
                // Check if the major version is a valid number
                String major = name.substring(1, firstidx);
                try {
                    Integer.parseInt(major);
                    // Major test success
                    int end = name.indexOf('/');
                    if (end != -1) {
                        // Get rid of the version (removes 'v1_4_5.')
                        name = name.substring(end + 1);
                    }
                } catch (NumberFormatException ex) {
                    // Major test fail
                }
            }
        }
        if(name.endsWith("CraftServer")) {
            packagePath += version;
        }
        return header + packagePath + name;
    }

    static byte[] remap(InputStream stream) throws IOException {
        ClassReader classReader = new ClassReader(stream);
        ClassWriter classWriter = new ClassWriter(classReader, 0);
        classReader.accept(new LibigotRemappingClassAdapter(classWriter, instance), ClassReader.EXPAND_FRAMES);
        return classWriter.toByteArray();
    }

    public static Class<?> fakeForName(String name) throws ClassNotFoundException {
        return Class.forName(instance.filter(name.replace('.', '/')).replace('/', '.'));
    }
}
