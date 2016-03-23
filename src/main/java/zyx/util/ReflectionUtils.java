package zyx.util;

import java.lang.reflect.Method;

/**
 * Created by stark.zhang on 2015/11/2.
 */
public class ReflectionUtils {

    /**
     * 获取object所对应的method的返回值
     * @param object
     * @param methodName
     * @return
     * @throws Exception
     */
    public static Object executeMethod(Object object, String methodName)
            throws Exception {
        Class clazz = object.getClass();
        Method method = clazz.getMethod(methodName, new Class[0]);
        return method.invoke(object, new Object[0]);
    }


    /**
     * 根据property返回getXXX method name
     * @param property
     * @return
     */
    public static String returnGetMethodName(String property) {
        return "get" + Character.toUpperCase(property.charAt(0)) + property.substring(1);
    }


}
