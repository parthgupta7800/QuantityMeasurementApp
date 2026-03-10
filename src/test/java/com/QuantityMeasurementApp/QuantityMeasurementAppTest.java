package com.QuantityMeasurementApp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class QuantityMeasurementAppTest{

    private static final double EPSILON=0.000001;

    @Test
    void testValidation_NullOperand_ConsistentAcrossOperations(){

        Quantity<LengthUnit>q=new Quantity<>(10.0,LengthUnit.FEET);

        assertThrows(IllegalArgumentException.class,()->q.add(null));
        assertThrows(IllegalArgumentException.class,()->q.subtract(null));
        assertThrows(IllegalArgumentException.class,()->q.divide(null));
    }

    @Test
    void testValidation_CrossCategory_ConsistentAcrossOperations(){

        Quantity<LengthUnit>length=new Quantity<>(10.0,LengthUnit.FEET);
        Quantity<WeightUnit>weight=new Quantity<>(5.0,WeightUnit.KILOGRAM);

        assertThrows(IllegalArgumentException.class,()->length.add((Quantity)weight));
        assertThrows(IllegalArgumentException.class,()->length.subtract((Quantity)weight));
        assertThrows(IllegalArgumentException.class,()->length.divide((Quantity)weight));
    }

    @Test
    void testValidation_NullTargetUnit_AddSubtractReject(){

        Quantity<LengthUnit>a=new Quantity<>(10.0,LengthUnit.FEET);
        Quantity<LengthUnit>b=new Quantity<>(5.0,LengthUnit.FEET);

        assertThrows(IllegalArgumentException.class,()->a.add(b,null));
        assertThrows(IllegalArgumentException.class,()->a.subtract(b,null));
    }

    @Test
    void testArithmeticOperation_Add_EnumComputation(){

        double result=Quantity.ArithmeticOperation.ADD.compute(10,5);
        assertEquals(15.0,result,EPSILON);
    }

    @Test
    void testArithmeticOperation_Subtract_EnumComputation(){

        double result=Quantity.ArithmeticOperation.SUBTRACT.compute(10,5);
        assertEquals(5.0,result,EPSILON);
    }

    @Test
    void testArithmeticOperation_Divide_EnumComputation(){

        double result=Quantity.ArithmeticOperation.DIVIDE.compute(10,5);
        assertEquals(2.0,result,EPSILON);
    }

    @Test
    void testArithmeticOperation_DivideByZero_EnumThrows(){

        assertThrows(ArithmeticException.class,
                ()->Quantity.ArithmeticOperation.DIVIDE.compute(10,0));
    }

    @Test
    void testAdd_UC12_BehaviorPreserved(){

        Quantity<LengthUnit>a=new Quantity<>(1.0,LengthUnit.FEET);
        Quantity<LengthUnit>b=new Quantity<>(12.0,LengthUnit.INCHES);

        Quantity<LengthUnit>result=a.add(b);

        assertEquals(2.0,result.getValue(),EPSILON);
    }

    @Test
    void testSubtract_UC12_BehaviorPreserved(){

        Quantity<LengthUnit>a=new Quantity<>(10.0,LengthUnit.FEET);
        Quantity<LengthUnit>b=new Quantity<>(6.0,LengthUnit.INCHES);

        Quantity<LengthUnit>result=a.subtract(b);

        assertEquals(9.5,result.getValue(),EPSILON);
    }

    @Test
    void testDivide_UC12_BehaviorPreserved(){

        Quantity<LengthUnit>a=new Quantity<>(10.0,LengthUnit.FEET);
        Quantity<LengthUnit>b=new Quantity<>(2.0,LengthUnit.FEET);

        double result=a.divide(b);

        assertEquals(5.0,result,EPSILON);
    }

    @Test
    void testImplicitTargetUnit_AddSubtract(){

        Quantity<VolumeUnit>a=new Quantity<>(5.0,VolumeUnit.LITRE);
        Quantity<VolumeUnit>b=new Quantity<>(500.0,VolumeUnit.MILLILITRE);

        Quantity<VolumeUnit>result=a.subtract(b);

        assertEquals(4.5,result.getValue(),EPSILON);
        assertEquals(VolumeUnit.LITRE,result.getUnit());
    }

    @Test
    void testExplicitTargetUnit_AddSubtract_Overrides(){

        Quantity<VolumeUnit>a=new Quantity<>(5.0,VolumeUnit.LITRE);
        Quantity<VolumeUnit>b=new Quantity<>(2.0,VolumeUnit.LITRE);

        Quantity<VolumeUnit>result=a.subtract(b,VolumeUnit.MILLILITRE);

        assertEquals(3000.0,result.getValue(),EPSILON);
    }

    @Test
    void testImmutability_AfterAdd_ViaCentralizedHelper(){

        Quantity<LengthUnit>a=new Quantity<>(5.0,LengthUnit.FEET);
        Quantity<LengthUnit>b=new Quantity<>(2.0,LengthUnit.FEET);

        Quantity<LengthUnit>result=a.add(b);

        assertEquals(5.0,a.getValue(),EPSILON);
        assertEquals(7.0,result.getValue(),EPSILON);
    }

    @Test
    void testImmutability_AfterSubtract_ViaCentralizedHelper(){

        Quantity<LengthUnit>a=new Quantity<>(10.0,LengthUnit.FEET);
        Quantity<LengthUnit>b=new Quantity<>(3.0,LengthUnit.FEET);

        Quantity<LengthUnit>result=a.subtract(b);

        assertEquals(10.0,a.getValue(),EPSILON);
        assertEquals(7.0,result.getValue(),EPSILON);
    }

    @Test
    void testImmutability_AfterDivide_ViaCentralizedHelper(){

        Quantity<LengthUnit>a=new Quantity<>(10.0,LengthUnit.FEET);
        Quantity<LengthUnit>b=new Quantity<>(2.0,LengthUnit.FEET);

        double result=a.divide(b);

        assertEquals(10.0,a.getValue(),EPSILON);
        assertEquals(5.0,result,EPSILON);
    }

    @Test
    void testAllOperations_AcrossAllCategories(){

        Quantity<LengthUnit>l1=new Quantity<>(10.0,LengthUnit.FEET);
        Quantity<LengthUnit>l2=new Quantity<>(2.0,LengthUnit.FEET);

        Quantity<WeightUnit>w1=new Quantity<>(10.0,WeightUnit.KILOGRAM);
        Quantity<WeightUnit>w2=new Quantity<>(5.0,WeightUnit.KILOGRAM);

        Quantity<VolumeUnit>v1=new Quantity<>(5.0,VolumeUnit.LITRE);
        Quantity<VolumeUnit>v2=new Quantity<>(2.0,VolumeUnit.LITRE);

        assertEquals(12.0,l1.add(l2).getValue(),EPSILON);
        assertEquals(5.0,w1.subtract(w2).getValue(),EPSILON);
        assertEquals(2.5,v1.divide(v2),EPSILON);
    }

    @Test
    void testArithmetic_Chain_Operations(){

        Quantity<LengthUnit>a=new Quantity<>(10.0,LengthUnit.FEET);
        Quantity<LengthUnit>b=new Quantity<>(2.0,LengthUnit.FEET);
        Quantity<LengthUnit>c=new Quantity<>(1.0,LengthUnit.FEET);

        double result=a.add(b).subtract(c).divide(new Quantity<>(1.0,LengthUnit.FEET));

        assertEquals(11.0,result,EPSILON);
    }

}
