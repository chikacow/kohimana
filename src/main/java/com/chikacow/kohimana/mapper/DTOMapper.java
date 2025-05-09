package com.chikacow.kohimana.mapper;

import java.lang.reflect.Field;

public class DTOMapper {

    public static void copyNonNullFields(Object sourceDto, Object targetEntity) {
        Field[] fields = sourceDto.getClass().getDeclaredFields();

        for (Field dtoField : fields) {
            try {
                dtoField.setAccessible(true);
                Object value = dtoField.get(sourceDto);
                if (value != null) {
                    try {
                        Field entityField = targetEntity.getClass().getDeclaredField(dtoField.getName());
                        entityField.setAccessible(true);
                        entityField.set(targetEntity, value);
                    } catch (NoSuchFieldException e) {
                        // Ignore fields that don't exist in the target
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
