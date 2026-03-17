package com.QuantityMeasurementApp.service;

import com.QuantityMeasurementApp.DTO.QuantityDTO;
import com.QuantityMeasurementApp.repository.IQuantityMeasurementRepository;
import com.QuantityMeasurementApp.service.QuantityMeasurementServiceImpl;

import org.junit.Test;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class QuantityMeasurementServiceImplTest {

    @Test
    public void testService_CompareEquality_Success(){

        IQuantityMeasurementRepository repo=mock(IQuantityMeasurementRepository.class);
        QuantityMeasurementServiceImpl service=new QuantityMeasurementServiceImpl(repo);

        QuantityDTO q1=new QuantityDTO(2,"FEET","Length");
        QuantityDTO q2=new QuantityDTO(24,"INCHES","Length");

        boolean result=service.compare(q1,q2);

        assertTrue(result);
        verify(repo).save(any());
    }

    @Test
    public void testService_Add_UnsupportedOperation_Error(){

        IQuantityMeasurementRepository repo=mock(IQuantityMeasurementRepository.class);
        QuantityMeasurementServiceImpl service=new QuantityMeasurementServiceImpl(repo);

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