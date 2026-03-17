package com.QuantityMeasurementApp.entityTest;
import com.QuantityMeasurementApp.entity.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class QuantityMeasurementEntityTest {

    @Test
    public void testQuantityEntity_BinaryOperandConstruction(){

        QuantityMeasurementEntity entity=
                new QuantityMeasurementEntity("2 FEET","24 INCHES","COMPARE","true");

        assertEquals("COMPARE",entity.getOperation());
        assertEquals("true",entity.getResult());
    }

    @Test
    public void testQuantityEntity_ToString(){

        QuantityMeasurementEntity entity=
                new QuantityMeasurementEntity("2 FEET","24 INCHES","COMPARE","true");

        assertTrue(entity.toString().contains("COMPARE"));
    }
}