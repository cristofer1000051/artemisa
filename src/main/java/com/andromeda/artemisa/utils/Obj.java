package com.andromeda.artemisa.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Obj {

    private static final ObjectMapper mapper = new ObjectMapper();

    public String toJson(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public <T> T toObject(String json, Class<T> valueType) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, valueType);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

}
