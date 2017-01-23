package tools;

import modules.IModules;

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class ModulesTools {
    public static synchronized IModules LoadModule(java.net.URL url, String action_name) {
        IModules ret = null;
        Class<?> clazz = null;

        ClassLoader loader = URLClassLoader.newInstance(new URL[] { url }, IModules.class.getClassLoader());
        try {
            clazz = Class.forName(action_name, true, loader);
            ret = (IModules) clazz.getConstructor().newInstance();
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public static java.net.URL getURL(String file_link) {
        java.net.URL ret = null;

        java.io.File file = new java.io.File(file_link);
        try {
            ret = file.toURI().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return ret;
    }
}
