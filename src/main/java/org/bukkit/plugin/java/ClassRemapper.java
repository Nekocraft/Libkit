package org.bukkit.plugin.java;

import java.io.IOException;
import java.io.InputStream;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.commons.Remapper;
import org.objectweb.asm.commons.RemappingClassAdapter;

// Re-maps class references from non-versioned packages to the correct versioned package
public class ClassRemapper extends Remapper {
    private final String[] PACKAGE_ROOTS = new String[2];
    public static final ClassRemapper instance = new ClassRemapper();
    private final String version;

    // TODO: Lazy workaround against mavens shading.
    ClassRemapper() {
        PACKAGE_ROOTS[0] = "net/minecraft/server";
        PACKAGE_ROOTS[1] = "org/bukkit/craftbukkit";
        // TODO Always update this when CB updates it in the pom.xml:
        version = "v1_5_R1";
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
        String name = text.substring(startIndex + packagePath.length() + 1);
        if (name.startsWith("v") && !name.startsWith(version)) {
            int firstidx = name.indexOf('_');
            if (firstidx != -1) {
                // Check if the major version is a valid number
                String major = name.substring(1, firstidx);
                try {
                    Integer.parseInt(major);
                    // Major test success
                    int end = name.indexOf('/');
                    if (end != -1) {
                        // Give a correct version.
                        name = name.substring(end);
                    }
                } catch (NumberFormatException ex) {
                    // Major test fail
                }
            }
        }
        return text.substring(0, startIndex) + packagePath + name;
    }

    public byte[] remap(InputStream stream) throws IOException {
        ClassReader classReader = new ClassReader(stream);
        ClassWriter classWriter = new ClassWriter(classReader, 0);
        classReader.accept(new RemappingClassAdapter(classWriter, instance), ClassReader.EXPAND_FRAMES);
        return classWriter.toByteArray();
    }
}
