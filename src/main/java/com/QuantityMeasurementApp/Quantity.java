package com.QuantityMeasurementApp;

import java.util.function.DoubleBinaryOperator;

public class Quantity<U extends IMeasurable>{

    private final double value;
    private final U unit;

    private static final double EPSILON=0.000001;

    public Quantity(double value,U unit){

        if(unit==null)
            throw new IllegalArgumentException("Unit cannot be null");

        if(!Double.isFinite(value))
            throw new IllegalArgumentException("Invalid value");

        this.value=value;
        this.unit=unit;
    }

    public double getValue(){
        return value;
    }

    public U getUnit(){
        return unit;
    }

    private double toBaseUnit(){
        return unit.convertToBaseUnit(value);
    }

    public Quantity<U> convertTo(U targetUnit){

        if(targetUnit==null)
            throw new IllegalArgumentException("Target unit cannot be null");

        double baseValue=unit.convertToBaseUnit(value);
        double converted=targetUnit.convertFromBaseUnit(baseValue);

        return new Quantity<>(converted,targetUnit);
    }

    public Quantity<U> add(Quantity<U>other){

        validateArithmeticOperands(other,this.unit,false);

        double baseResult=performArithmetic(other,this.unit,ArithmeticOperation.ADD);

        double result=unit.convertFromBaseUnit(baseResult);

        return new Quantity<>(result,unit);
    }

    public Quantity<U> add(Quantity<U>other,U targetUnit){

        validateArithmeticOperands(other,targetUnit,true);

        double baseResult=performArithmetic(other,targetUnit,ArithmeticOperation.ADD);

        double result=targetUnit.convertFromBaseUnit(baseResult);

        return new Quantity<>(result,targetUnit);
    }

    public Quantity<U> subtract(Quantity<U>other){

        validateArithmeticOperands(other,this.unit,false);

        double baseResult=performArithmetic(other,this.unit,ArithmeticOperation.SUBTRACT);

        double result=unit.convertFromBaseUnit(baseResult);

        return new Quantity<>(result,unit);
    }

    public Quantity<U> subtract(Quantity<U>other,U targetUnit){

        validateArithmeticOperands(other,targetUnit,true);

        double baseResult=performArithmetic(other,targetUnit,ArithmeticOperation.SUBTRACT);

        double result=targetUnit.convertFromBaseUnit(baseResult);

        return new Quantity<>(result,targetUnit);
    }

    public double divide(Quantity<U>other){

        validateArithmeticOperands(other,null,false);

        return performArithmetic(other,null,ArithmeticOperation.DIVIDE);
    }

    private void validateArithmeticOperands(Quantity<U>other,U targetUnit,boolean targetUnitRequired){

        if(other==null)
            throw new IllegalArgumentException("Quantity cannot be null");

        if(this.unit.getClass()!=other.unit.getClass())
            throw new IllegalArgumentException("Incompatible measurement categories");

        if(!Double.isFinite(other.value))
            throw new IllegalArgumentException("Invalid value");

        if(targetUnitRequired&&targetUnit==null)
            throw new IllegalArgumentException("Target unit cannot be null");
    }

    private double performArithmetic(Quantity<U>other,U targetUnit,ArithmeticOperation operation){

        double thisBase=this.unit.convertToBaseUnit(this.value);
        double otherBase=other.unit.convertToBaseUnit(other.value);

        return operation.compute(thisBase,otherBase);
    }

    enum ArithmeticOperation{

        ADD((a,b)->a+b),

        SUBTRACT((a,b)->a-b),

        DIVIDE((a,b)->{
            if(Math.abs(b)<EPSILON)
                throw new ArithmeticException("Division by zero");
            return a/b;
        });

        private final DoubleBinaryOperator operation;

        ArithmeticOperation(DoubleBinaryOperator operation){
            this.operation=operation;
        }

        public double compute(double a,double b){
            return operation.applyAsDouble(a,b);
        }
    }

    @Override
    public boolean equals(Object obj){

        if(this==obj)
            return true;

        if(obj==null||getClass()!=obj.getClass())
            return false;

        Quantity<?>other=(Quantity<?>)obj;

        if(this.unit.getClass()!=other.unit.getClass())
            return false;

        return Math.abs(this.toBaseUnit()-other.toBaseUnit())<EPSILON;
    }

    @Override
    public int hashCode(){
        return Double.hashCode(toBaseUnit());
    }

    @Override
    public String toString(){
        return value+" "+unit;
    }
}