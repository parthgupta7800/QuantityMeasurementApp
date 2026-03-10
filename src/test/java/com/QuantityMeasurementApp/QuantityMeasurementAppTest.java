package com.QuantityMeasurementApp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class QuantityMeasurementAppTest {

    private static final double EPS=0.0001;

    @Test
    void testTemperatureEquality_CelsiusToCelsius_SameValue(){
        Quantity<TemperatureUnit>a=new Quantity<>(0.0,TemperatureUnit.CELSIUS);
        Quantity<TemperatureUnit>b=new Quantity<>(0.0,TemperatureUnit.CELSIUS);
        assertTrue(a.equals(b));
    }

    @Test
    void testTemperatureEquality_FahrenheitToFahrenheit_SameValue(){
        Quantity<TemperatureUnit>a=new Quantity<>(32.0,TemperatureUnit.FAHRENHEIT);
        Quantity<TemperatureUnit>b=new Quantity<>(32.0,TemperatureUnit.FAHRENHEIT);
        assertTrue(a.equals(b));
    }

    @Test
    void testTemperatureEquality_CelsiusToFahrenheit_0Celsius32Fahrenheit(){
        Quantity<TemperatureUnit>a=new Quantity<>(0.0,TemperatureUnit.CELSIUS);
        Quantity<TemperatureUnit>b=new Quantity<>(32.0,TemperatureUnit.FAHRENHEIT);
        assertTrue(a.equals(b));
    }

    @Test
    void testTemperatureEquality_CelsiusToFahrenheit_100Celsius212Fahrenheit(){
        Quantity<TemperatureUnit>a=new Quantity<>(100.0,TemperatureUnit.CELSIUS);
        Quantity<TemperatureUnit>b=new Quantity<>(212.0,TemperatureUnit.FAHRENHEIT);
        assertTrue(a.equals(b));
    }

    @Test
    void testTemperatureEquality_CelsiusToFahrenheit_Negative40Equal(){
        Quantity<TemperatureUnit>a=new Quantity<>(-40.0,TemperatureUnit.CELSIUS);
        Quantity<TemperatureUnit>b=new Quantity<>(-40.0,TemperatureUnit.FAHRENHEIT);
        assertTrue(a.equals(b));
    }

    @Test
    void testTemperatureEquality_SymmetricProperty(){
        Quantity<TemperatureUnit>a=new Quantity<>(0.0,TemperatureUnit.CELSIUS);
        Quantity<TemperatureUnit>b=new Quantity<>(32.0,TemperatureUnit.FAHRENHEIT);
        assertTrue(a.equals(b)&&b.equals(a));
    }

    @Test
    void testTemperatureEquality_ReflexiveProperty(){
        Quantity<TemperatureUnit>a=new Quantity<>(25.0,TemperatureUnit.CELSIUS);
        assertTrue(a.equals(a));
    }

    @Test
    void testTemperatureConversion_CelsiusToFahrenheit_VariousValues(){
        Quantity<TemperatureUnit>a=new Quantity<>(50.0,TemperatureUnit.CELSIUS);
        Quantity<TemperatureUnit>result=a.convertTo(TemperatureUnit.FAHRENHEIT);
        assertEquals(122.0,result.getValue(),EPS);
    }

    @Test
    void testTemperatureConversion_FahrenheitToCelsius_VariousValues(){
        Quantity<TemperatureUnit>a=new Quantity<>(122.0,TemperatureUnit.FAHRENHEIT);
        Quantity<TemperatureUnit>result=a.convertTo(TemperatureUnit.CELSIUS);
        assertEquals(50.0,result.getValue(),EPS);
    }

    @Test
    void testTemperatureConversion_RoundTrip_PreservesValue(){
        Quantity<TemperatureUnit>a=new Quantity<>(37.0,TemperatureUnit.CELSIUS);
        Quantity<TemperatureUnit>b=a.convertTo(TemperatureUnit.FAHRENHEIT);
        Quantity<TemperatureUnit>c=b.convertTo(TemperatureUnit.CELSIUS);
        assertEquals(a.getValue(),c.getValue(),EPS);
    }

    @Test
    void testTemperatureConversion_SameUnit(){
        Quantity<TemperatureUnit>a=new Quantity<>(25.0,TemperatureUnit.CELSIUS);
        Quantity<TemperatureUnit>b=a.convertTo(TemperatureUnit.CELSIUS);
        assertEquals(25.0,b.getValue(),EPS);
    }

    @Test
    void testTemperatureConversion_ZeroValue(){
        Quantity<TemperatureUnit>a=new Quantity<>(0.0,TemperatureUnit.CELSIUS);
        Quantity<TemperatureUnit>b=a.convertTo(TemperatureUnit.FAHRENHEIT);
        assertEquals(32.0,b.getValue(),EPS);
    }

    @Test
    void testTemperatureConversion_NegativeValues(){
        Quantity<TemperatureUnit>a=new Quantity<>(-20.0,TemperatureUnit.CELSIUS);
        Quantity<TemperatureUnit>b=a.convertTo(TemperatureUnit.FAHRENHEIT);
        assertEquals(-4.0,b.getValue(),EPS);
    }

    @Test
    void testTemperatureConversion_LargeValues(){
        Quantity<TemperatureUnit>a=new Quantity<>(1000.0,TemperatureUnit.CELSIUS);
        Quantity<TemperatureUnit>b=a.convertTo(TemperatureUnit.FAHRENHEIT);
        assertEquals(1832.0,b.getValue(),EPS);
    }

    @Test
    void testTemperatureUnsupportedOperation_Add(){
        Quantity<TemperatureUnit>a=new Quantity<>(100.0,TemperatureUnit.CELSIUS);
        Quantity<TemperatureUnit>b=new Quantity<>(50.0,TemperatureUnit.CELSIUS);
        assertThrows(UnsupportedOperationException.class,()->a.add(b));
    }

    @Test
    void testTemperatureUnsupportedOperation_Subtract(){
        Quantity<TemperatureUnit>a=new Quantity<>(100.0,TemperatureUnit.CELSIUS);
        Quantity<TemperatureUnit>b=new Quantity<>(50.0,TemperatureUnit.CELSIUS);
        assertThrows(UnsupportedOperationException.class,()->a.subtract(b));
    }

    @Test
    void testTemperatureUnsupportedOperation_Divide(){
        Quantity<TemperatureUnit>a=new Quantity<>(100.0,TemperatureUnit.CELSIUS);
        Quantity<TemperatureUnit>b=new Quantity<>(50.0,TemperatureUnit.CELSIUS);
        assertThrows(UnsupportedOperationException.class,()->a.divide(b));
    }

    @Test
    void testTemperatureUnsupportedOperation_ErrorMessage(){
        Quantity<TemperatureUnit>a=new Quantity<>(100.0,TemperatureUnit.CELSIUS);
        Exception ex=assertThrows(UnsupportedOperationException.class,
                ()->a.add(new Quantity<>(50.0,TemperatureUnit.CELSIUS)));
        assertTrue(ex.getMessage().contains("Temperature"));
    }

    @Test
    void testTemperatureDifferentValuesInequality(){
        Quantity<TemperatureUnit>a=new Quantity<>(50.0,TemperatureUnit.CELSIUS);
        Quantity<TemperatureUnit>b=new Quantity<>(100.0,TemperatureUnit.CELSIUS);
        assertFalse(a.equals(b));
    }

    @Test
    void testTemperatureNullOperandValidation_InComparison(){
        Quantity<TemperatureUnit>a=new Quantity<>(10.0,TemperatureUnit.CELSIUS);
        assertFalse(a.equals(null));
    }

    @Test
    void testTemperatureEnumImplementsIMeasurable(){
        assertTrue(IMeasurable.class.isAssignableFrom(TemperatureUnit.class));
    }

    @Test
    void testTemperatureUnit_NameMethod(){
        assertEquals("CELSIUS",TemperatureUnit.CELSIUS.getUnitName());
    }

    @Test
    void testTemperatureUnit_ConversionFactor(){
        assertEquals(1.0,TemperatureUnit.CELSIUS.getConversionFactor(),EPS);
    }

    @Test
    void testTemperatureValidateOperationSupport_MethodBehavior(){
        assertThrows(UnsupportedOperationException.class,
                ()->TemperatureUnit.CELSIUS.validateOperationSupport("ADD"));
    }

    @Test
    void testTemperatureIntegrationWithGenericQuantity(){
        Quantity<TemperatureUnit>a=new Quantity<>(30.0,TemperatureUnit.CELSIUS);
        Quantity<TemperatureUnit>b=a.convertTo(TemperatureUnit.FAHRENHEIT);
        assertEquals(86.0,b.getValue(),EPS);
    }
}