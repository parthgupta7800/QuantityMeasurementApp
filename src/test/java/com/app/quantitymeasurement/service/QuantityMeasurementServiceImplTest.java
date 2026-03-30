package com.app.quantitymeasurement.service;

import com.app.quantitymeasurement.DTO.QuantityDTO;
import com.app.quantitymeasurement.entity.QuantityMeasurementEntity;
import com.app.quantitymeasurement.exception.QuantityMeasurementException;
import com.app.quantitymeasurement.repository.QuantityMeasurementRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class QuantityMeasurementServiceImplTest {

    @Mock
    private QuantityMeasurementRepository repository;

    @InjectMocks
    private QuantityMeasurementServiceImpl service;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCompare_SameLengthUnits_ShouldReturnTrue(){

        QuantityDTO q1=new QuantityDTO(12.0,"INCH","LENGTH");
        QuantityDTO q2=new QuantityDTO(1.0,"FEET","LENGTH");

        boolean result=service.compare(q1,q2);

        assertTrue(result);
        verify(repository,times(1)).save(any(QuantityMeasurementEntity.class));
    }

    @Test
    void testCompare_DifferentMeasurementType_ShouldThrowException(){

        QuantityDTO q1=new QuantityDTO(1.0,"FEET","LENGTH");
        QuantityDTO q2=new QuantityDTO(1.0,"GRAM","WEIGHT");

        assertThrows(QuantityMeasurementException.class,
                ()->service.compare(q1,q2));
    }

    @Test
    void testAdd_LengthUnits(){

        QuantityDTO q1=new QuantityDTO(1.0,"FEET","LENGTH");
        QuantityDTO q2=new QuantityDTO(12.0,"INCH","LENGTH");

        QuantityDTO result=service.add(q1,q2);

        assertEquals(2.0,result.getValue(),0.0001);
        verify(repository,times(1)).save(any(QuantityMeasurementEntity.class));
    }

    @Test
    void testSubtract_LengthUnits(){

        QuantityDTO q1=new QuantityDTO(2.0,"FEET","LENGTH");
        QuantityDTO q2=new QuantityDTO(12.0,"INCH","LENGTH");

        QuantityDTO result=service.subtract(q1,q2);

        assertEquals(1.0,result.getValue(),0.0001);
        verify(repository,times(1)).save(any(QuantityMeasurementEntity.class));
    }

    @Test
    void testDivide(){

        QuantityDTO q1=new QuantityDTO(2.0,"FEET","LENGTH");
        QuantityDTO q2=new QuantityDTO(1.0,"FEET","LENGTH");

        double result=service.divide(q1,q2);

        assertEquals(2.0,result,0.0001);
        verify(repository,times(1)).save(any(QuantityMeasurementEntity.class));
    }

    @Test
    void testDivide_ByZero_ShouldThrowException(){

        QuantityDTO q1=new QuantityDTO(2.0,"FEET","LENGTH");
        QuantityDTO q2=new QuantityDTO(0.0,"FEET","LENGTH");

        assertThrows(Exception.class,
                ()->service.divide(q1,q2));
    }

    @Test
    void testConvert_Length(){

        QuantityDTO q=new QuantityDTO(1.0,"FEET","LENGTH");

        QuantityDTO result=service.convert(q,"INCH");

        assertEquals(12.0,result.getValue(),0.0001);
        verify(repository,times(1)).save(any(QuantityMeasurementEntity.class));
    }

    @Test
    void testConvert_InvalidUnit(){

        QuantityDTO q=new QuantityDTO(1.0,"FEET","LENGTH");

        assertThrows(Exception.class,
                ()->service.convert(q,"INVALID_UNIT"));
    }

    @Test
    void testGetHistory(){

        List<QuantityMeasurementEntity> mockList=new ArrayList<>();
        when(repository.findByOperation("ADD")).thenReturn(mockList);

        List<QuantityMeasurementEntity> result=service.getHistory("ADD");

        assertEquals(0,result.size());
        verify(repository,times(1)).findByOperation("ADD");
    }

    @Test
    void testGetCount(){

        when(repository.countByOperation("ADD")).thenReturn(5L);

        long count=service.getCount("ADD");

        assertEquals(5,count);
    }
}