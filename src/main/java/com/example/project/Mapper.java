package com.example.project;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Mapper {
    private static ObjectMapper objectMapper;
    private Mapper(){}

    public static ObjectMapper GetMapper(){
        if(objectMapper == null)
            objectMapper = new ObjectMapper();
        return objectMapper;
    }
}
