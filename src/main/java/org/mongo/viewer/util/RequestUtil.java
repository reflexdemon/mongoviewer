package org.mongo.viewer.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;

public class RequestUtil {

    public static <T> T reflectToObject(HttpServletRequest request,
            Class<T> type) {
        T instance = null;
        try {
            instance = (T) Class.forName(type.getName()).newInstance();
            Field[] fields = type.getDeclaredFields();
            for (Field f : fields) {
                if (f.isAccessible()) {
                    String name = f.getName();
                    String value = request.getParameter(name);
                    BeanUtils.setProperty(instance, name, value);
                }
            }
        } catch (InstantiationException e) {
            throw new RuntimeException(
                    "Unable to populate values to the supplied Bean " + type, e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(
                    "Unable to populate values to the supplied Bean " + type, e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(
                    "Unable to populate values to the supplied Bean " + type, e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(
                    "Unable to populate values to the supplied Bean " + type, e);
        }
        return instance;
    }

}
