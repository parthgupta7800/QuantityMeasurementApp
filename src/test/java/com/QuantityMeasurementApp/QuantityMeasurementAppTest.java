package com.QuantityMeasurementApp;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.QuantityMeasurementApp.DTO.QuantityDTO;
import com.QuantityMeasurementApp.controller.QuantityMeasurementController;
import com.QuantityMeasurementApp.entity.QuantityMeasurementEntity;
import com.QuantityMeasurementApp.exception.QuantityMeasurementException;
import com.QuantityMeasurementApp.repository.QuantityMeasurementCacheRepository;
import com.QuantityMeasurementApp.service.IQuantityMeasurementService;
import com.QuantityMeasurementApp.service.QuantityMeasurementServiceImpl;

public class QuantityMeasurementAppTest {

    private QuantityMeasurementController controller;
    private IQuantityMeasurementService service;

    @BeforeEach
    void setup() {
        QuantityMeasurementCacheRepository repository = new QuantityMeasurementCacheRepository();
        service = new QuantityMeasurementServiceImpl(repository);
        controller = new QuantityMeasurementController(service);
    }

    /* =========================================================
       ENTITY TESTS
    ========================================================= */

    @Test
    void testQuantityEntity_SingleOperandConstruction() {

        QuantityMeasurementEntity entity =
                new QuantityMeasurementEntity("2 FEET", "INCHES", "CONVERT", "24 INCHES");

        assertEquals("CONVERT", entity.getOperation());
    }

    @Test
    void testQuantityEntity_BinaryOperandConstruction() {

        QuantityMeasurementEntity entity =
                new QuantityMeasurementEntity("2 FEET", "24 INCHES", "ADD", "4 FEET");

        assertEquals("ADD", entity.getOperation());
    }

    @Test
    void testQuantityEntity_ToString_Success() {

        QuantityMeasurementEntity entity =
                new QuantityMeasurementEntity("2 FEET", "24 INCHES", "COMPARE", "true");

        assertNotNull(entity.toString());
    }

    /* =========================================================
       SERVICE TESTS
    ========================================================= */

    @Test
    void testService_CompareEquality_SameUnit_Success() {

        QuantityDTO q1 = new QuantityDTO(2,"FEET","Length");
        QuantityDTO q2 = new QuantityDTO(2,"FEET","Length");

        boolean result = service.compare(q1,q2);

        assertTrue(result);
    }

    @Test
    void testService_CompareEquality_DifferentUnit_Success() {

        QuantityDTO q1 = new QuantityDTO(2,"FEET","Length");
        QuantityDTO q2 = new QuantityDTO(24,"INCHES","Length");

        boolean result = service.compare(q1,q2);

        assertTrue(result);
    }

    @Test
    void testService_CompareEquality_CrossCategory_Error() {

        QuantityDTO q1 = new QuantityDTO(2,"FEET","Length");
        QuantityDTO q2 = new QuantityDTO(5,"KILOGRAM","Weight");

        assertThrows(QuantityMeasurementException.class, () ->
                service.compare(q1,q2)
        );
    }

    @Test
    void testService_Convert_Success() {

        QuantityDTO source = new QuantityDTO(2,"FEET","Length");

        QuantityDTO result = service.convert(source,"INCHES");

        assertEquals(24,result.getValue());
    }

    @Test
    void testService_Add_Success() {

        QuantityDTO q1 = new QuantityDTO(2,"FEET","Length");
        QuantityDTO q2 = new QuantityDTO(24,"INCHES","Length");

        QuantityDTO result = service.add(q1,q2);

        assertNotNull(result);
    }

    @Test
    void testService_Add_UnsupportedOperation_Error() {

        QuantityDTO q1 = new QuantityDTO(100,"CELSIUS","Temperature");
        QuantityDTO q2 = new QuantityDTO(50,"CELSIUS","Temperature");

        assertThrows(QuantityMeasurementException.class, () ->
                service.add(q1,q2)
        );
    }

    @Test
    void testService_Subtract_Success() {

        QuantityDTO q1 = new QuantityDTO(10,"FEET","Length");
        QuantityDTO q2 = new QuantityDTO(5,"FEET","Length");

        QuantityDTO result = service.subtract(q1,q2);

        assertNotNull(result);
    }

    @Test
    void testService_Divide_Success() {

        QuantityDTO q1 = new QuantityDTO(10,"FEET","Length");
        QuantityDTO q2 = new QuantityDTO(2,"FEET","Length");

        double result = service.divide(q1,q2);

        assertEquals(5,result);
    }

    @Test
    void testService_Divide_ByZero_Error() {

        QuantityDTO q1 = new QuantityDTO(10,"FEET","Length");
        QuantityDTO q2 = new QuantityDTO(0,"FEET","Length");

        assertThrows(QuantityMeasurementException.class, () ->
                service.divide(q1,q2)
        );
    }

    /* =========================================================
       CONTROLLER TESTS
    ========================================================= */

    @Test
    void testController_DemonstrateEquality_Success() {

        QuantityDTO q1 = new QuantityDTO(2,"FEET","Length");
        QuantityDTO q2 = new QuantityDTO(24,"INCHES","Length");

        boolean result = controller.performComparison(q1,q2);

        assertTrue(result);
    }

    @Test
    void testController_DemonstrateConversion_Success() {

        QuantityDTO source = new QuantityDTO(2,"FEET","Length");

        QuantityDTO result = controller.performConversion(source,"INCHES");

        assertEquals(24,result.getValue());
    }

    @Test
    void testController_DemonstrateAddition_Success() {

        QuantityDTO q1 = new QuantityDTO(2,"FEET","Length");
        QuantityDTO q2 = new QuantityDTO(24,"INCHES","Length");

        QuantityDTO result = controller.performAddition(q1,q2);

        assertNotNull(result);
    }

    @Test
    void testController_DemonstrateAddition_Error() {

        QuantityDTO q1 = new QuantityDTO(100,"CELSIUS","Temperature");
        QuantityDTO q2 = new QuantityDTO(50,"CELSIUS","Temperature");

        assertThrows(QuantityMeasurementException.class, () ->
                controller.performAddition(q1,q2)
        );
    }

    /* =========================================================
       INTEGRATION TESTS
    ========================================================= */

    @Test
    void testIntegration_EndToEnd_LengthAddition() {

        QuantityDTO q1 = new QuantityDTO(2,"FEET","Length");
        QuantityDTO q2 = new QuantityDTO(24,"INCHES","Length");

        QuantityDTO result = controller.performAddition(q1,q2);

        assertNotNull(result);
    }

    @Test
    void testIntegration_EndToEnd_TemperatureUnsupported() {

        QuantityDTO q1 = new QuantityDTO(100,"CELSIUS","Temperature");
        QuantityDTO q2 = new QuantityDTO(50,"CELSIUS","Temperature");

        assertThrows(QuantityMeasurementException.class, () ->
                controller.performAddition(q1,q2)
        );
    }

}