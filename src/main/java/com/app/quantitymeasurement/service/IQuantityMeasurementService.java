package com.app.quantitymeasurement.service;

import com.app.quantitymeasurement.DTO.QuantityDTO;

public interface IQuantityMeasurementService {

    boolean compare(QuantityDTO q1,QuantityDTO q2);

    QuantityDTO convert(QuantityDTO source,String targetUnit);

    QuantityDTO add(QuantityDTO q1,QuantityDTO q2);

    QuantityDTO subtract(QuantityDTO q1,QuantityDTO q2);

    double divide(QuantityDTO q1,QuantityDTO q2);
}