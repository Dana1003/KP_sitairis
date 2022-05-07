package com.example.project;

import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

public class SingletonTest {
    @Test
    void getMapper() {
        var mapper = Mapper.GetMapper();
        /*Assert.isNull(mapper, "Данный объект пустой!");*/
        Assert.notNull(mapper, "Данный объект не пустой!");
    }
}
