package com.QuantityMeasurementApp.entity;

import java.io.Serializable;

public class QuantityMeasurementEntity implements Serializable{

    private static final long serialVersionUID=1L;

    public String operation;
    public String operand1;
    public String operand2;
    public String result;
    public boolean isError;
    public String errorMessage;

    public QuantityMeasurementEntity(String operand1,String operand2,String operation,String result){
        this.operand1=operand1;
        this.operand2=operand2;
        this.operation=operation;
        this.result=result;
        this.isError=false;
    }

    public QuantityMeasurementEntity(String errorMessage){
        this.isError=true;
        this.errorMessage=errorMessage;
    }
}