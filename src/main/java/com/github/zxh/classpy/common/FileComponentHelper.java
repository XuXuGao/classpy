package com.github.zxh.classpy.common;

import com.github.zxh.classpy.classfile.ClassComponent;
import java.lang.reflect.Array;
import java.lang.reflect.Field;

/**
 *
 * @author zxh
 */
public class FileComponentHelper {
    
    // todo
    public static void setNameForClassComponentFields(ClassComponent ccObj)
            throws ReflectiveOperationException {
        
        for (Class<?> ccClass = ccObj.getClass(); ccClass != null; ccClass = ccClass.getSuperclass()) {
            for (Field field : ccClass.getDeclaredFields()) {
                field.setAccessible(true);
                if (isClassComponentType(field)) {
                    // field is ClassComponent
                    ClassComponent ccFieldVal = (ClassComponent) field.get(ccObj);
                    if (ccFieldVal != null) {
                        ccFieldVal.setName(field.getName());
                        setNameForClassComponentFields(ccFieldVal);
                    }
                } else if (isClassComponentArrayType(field)) {
                    // field is ClassComponent[]
                    Object arrFieldVal = field.get(ccObj);
                    if (arrFieldVal != null) {
                        int length = Array.getLength(arrFieldVal);
                        for (int i = 0; i < length; i++) {
                            ClassComponent arrItem = (ClassComponent) Array.get(arrFieldVal, i);
                            if (arrItem != null) {
                                setNameForClassComponentFields(arrItem);
                            }
                        }
                    }
                }
            }
        }
    }
    
    private static boolean isClassComponentType(Field field) {
        return ClassComponent.class.isAssignableFrom(field.getType());
    }
    
    private static boolean isClassComponentArrayType(Field field) {
        if (!field.getType().isArray()) {
            return false;
        }
        
        return ClassComponent.class.isAssignableFrom(
                field.getType().getComponentType());
    }
    
}