package org.bukkit.plugin.java;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import org.apache.commons.lang.Validate;
import org.bukkit.plugin.AuthorNagException;
// Libigot start
import java.io.InputStream;

import java.security.CodeSigner;
import java.security.CodeSource;
// Libigot end
/**
 * A ClassLoader for plugins, to allow shared classes across multiple plugins
 */
public class PluginClassLoader extends URLClassLoader {
    private final JavaPluginLoader loader;
    private final Map<String, Class<?>> classes = new HashMap<String, Class<?>>();
    final boolean extended = this.getClass() != PluginClassLoader.class;
    // Libigot start
    private final CodeSigner[] signers = new CodeSigner[0];
    private byte[] data;
    // Libigot end

    /**
     * Internal class not intended to be exposed
     */
    @Deprecated
    public PluginClassLoader(final JavaPluginLoader loader, final URL[] urls, final ClassLoader parent) {
        this(loader, urls, parent, null);

        if (loader.warn) {
            if (extended) {
                loader.server.getLogger().log(Level.WARNING, "PluginClassLoader not intended to be extended by " + getClass() + ", and may be final in a future version of Bukkit");
            } else {
                loader.server.getLogger().log(Level.WARNING, "Constructor \"public PluginClassLoader(JavaPluginLoader, URL[], ClassLoader)\" is Deprecated, and may be removed in a future version of Bukkit", new AuthorNagException(""));
            }
            loader.warn = false;
        }
    }


    PluginClassLoader(final JavaPluginLoader loader, final URL[] urls, final ClassLoader parent, final Object methodSignature) {
        super(urls, parent);
        Validate.notNull(loader, "Loader cannot be null");

        this.loader = loader;
    }

    @Override
    public void addURL(URL url) { // Override for access level!
        super.addURL(url);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        return extended ? findClass(name, true) : findClass0(name, true); // Don't warn on deprecation, but maintain overridability
    }

    // Libigot start
    private void ensurePackageLoaded(String name) {
        int i = name.lastIndexOf('.');
        if (i != -1) {
            String pkgname = name.substring(0, i);
            // If not yet defined, define it
            if (getPackage(pkgname) == null) {
                definePackage(pkgname, null, null, null, null, null, null, null);
            }
        }
    }
    // Libigot end

    /**
     * @deprecated Internal method that wasn't intended to be exposed
     */
    @Deprecated
    protected Class<?> findClass(String name, boolean checkGlobal) throws ClassNotFoundException {
        if (loader.warn) {
            loader.server.getLogger().log(Level.WARNING, "Method \"protected Class<?> findClass(String, boolean)\" is Deprecated, and may be removed in a future version of Bukkit", new AuthorNagException(""));
            loader.warn = false;
        }
        return findClass0(name, checkGlobal);
    }

    Class<?> findClass0(String name, boolean checkGlobal) throws ClassNotFoundException {
        if (name.startsWith("org.bukkit.") || name.startsWith("net.minecraft.") || name.startsWith("org.Libigotdev.")) { // Libigot
            throw new ClassNotFoundException(name);
        }
        Class<?> result = classes.get(name);

        if (result == null) {
            if (checkGlobal) {
                result = loader.extended ? loader.getClassByName(name) : loader.getClassByName0(name); // Don't warn on deprecation, but maintain overridability
            }

            if (result == null) {
                // Libigot start
                try {
                    // Ensure that the package is loaded
                    ensurePackageLoaded(name);

                    // Load the resource to the name
                    String path = name.replace('.', '/').concat(".class");
                    URL res = this.findResource(path);
                    if (res != null) {
                        InputStream stream = res.openStream();
                        if (stream != null) {
                            // Remap the classes
                            data = ClassRemapper.instance.remap(stream);
                            // Define (create) the class using the modified byte code
                            // The top-child class loader is used for this to prevent access violations
                            // Also we fix the URL so it matches the one from URLClassLoader
                            String newString = res.toString();
                            if(newString.startsWith("jar:") && newString.contains("!")) {
                                newString = newString.substring(4, newString.indexOf("!"));
                                res = new URL(newString);
                            }
                            CodeSource cs = new CodeSource(res, signers);
                            result = this.defineClass(name, data, 0, data.length, cs);
                            if (result != null) {
                                // Resolve it - sets the class loader of the class
                                this.resolveClass(result);
                            }
                        }
                    }
                } catch (Throwable t) {
                    throw new ClassNotFoundException("Internal Libigot error", t);
                }
                // Libigot end
            }

            classes.put(name, result);
        }

        return result;
    }

    /**
     * @deprecated Internal method that wasn't intended to be exposed
     */
    @Deprecated
    public Set<String> getClasses() {
        if (loader.warn) {
            loader.server.getLogger().log(Level.WARNING, "Method \"public Set<String> getClasses()\" is Deprecated, and may be removed in a future version of Bukkit", new AuthorNagException(""));
            loader.warn = false;
        }
        return getClasses0();
    }

    Set<String> getClasses0() {
        return classes.keySet();
    }
}
