package com.QuantityMeasurementApp.entity;

public class QuantityMeasurementEntity {

    private final String operand1;
    private final String operand2;
    private final String operation;
    private final String result;

    public QuantityMeasurementEntity(String operand1,String operand2,String operation,String result){
        this.operand1=operand1;
        this.operand2=operand2;
        this.operation=operation;
        this.result=result;
    }

    public String getOperand1(){
        return operand1;
    }

    public String getOperand2(){
        return operand2;
    }

    public String getOperation(){
        return operation;
    }

    public String getResult(){
        return result;
    }

    @Override
    public String toString(){
        return "Operation: "+operation+
                ", Operand1: "+operand1+
                ", Operand2: "+operand2+
                ", Result: "+result;
    }
}