package com.QuantityMeasurementApp.integrationTest;

import com.QuantityMeasurementApp.DTO.QuantityDTO;
import com.QuantityMeasurementApp.repository.*;
import com.QuantityMeasurementApp.service.*;

import org.junit.Test;

import static org.junit.Assert.*;

public class QuantityMeasurementIntegrationTest {

    @Test
    public void testIntegration_EndToEnd_LengthAddition(){

        IQuantityMeasurementRepository repo=new QuantityMeasurementDatabaseRepository();
        IQuantityMeasurementService service=new QuantityMeasurementServiceImpl(repo);

        QuantityDTO q1=new QuantityDTO(2,"FEET","Length");
        QuantityDTO q2=new QuantityDTO(12,"INCHES","Length");

        QuantityDTO result=service.add(q1,q2);

        assertNotNull(result);
    }

    @Test
    public void testIntegration_EndToEnd_TemperatureUnsupported(){

        IQuantityMeasurementRepository repo=new QuantityMeasurementDatabaseRepository();
        IQuantityMeasurementService service=new QuantityMeasurementServiceImpl(repo);

        QuantityDTO q1=new QuantityDTO(100,"CELSIUS","Temperature");
        QuantityDTO q2=new QuantityDTO(50,"CELSIUS","Temperature");

        try{
            service.add(q1,q2);
            fail();
        }catch(Exception e){
            assertTrue(e.getMessage().contains("not support"));
        }
    }
}