package com.QuantityMeasurementApp.service;

import com.QuantityMeasurementApp.DTO.QuantityDTO;
import com.QuantityMeasurementApp.Quantity;
import com.QuantityMeasurementApp.units.LengthUnit;
import com.QuantityMeasurementApp.units.WeightUnit;
import com.QuantityMeasurementApp.units.VolumeUnit;
import com.QuantityMeasurementApp.units.TemperatureUnit;
import com.QuantityMeasurementApp.units.IMeasurable;
import com.QuantityMeasurementApp.entity.QuantityMeasurementEntity;
import com.QuantityMeasurementApp.exception.QuantityMeasurementException;
import com.QuantityMeasurementApp.repository.IQuantityMeasurementRepository;

public class QuantityMeasurementServiceImpl implements IQuantityMeasurementService{

    private final IQuantityMeasurementRepository repository;

    public QuantityMeasurementServiceImpl(IQuantityMeasurementRepository repository){
        this.repository=repository;
    }

    private Quantity convertDTOToQuantity(QuantityDTO dto){

        String unitName=dto.getUnit();
        double value=dto.getValue();

        if(dto.getMeasurementType().equalsIgnoreCase("Length")){
            LengthUnit unit=LengthUnit.valueOf(unitName);
            return new Quantity(value,unit);
        }

        if(dto.getMeasurementType().equalsIgnoreCase("Weight")){
            WeightUnit unit=WeightUnit.valueOf(unitName);
            return new Quantity(value,unit);
        }

        if(dto.getMeasurementType().equalsIgnoreCase("Volume")){
            VolumeUnit unit=VolumeUnit.valueOf(unitName);
            return new Quantity(value,unit);
        }

        if(dto.getMeasurementType().equalsIgnoreCase("Temperature")){
            TemperatureUnit unit=TemperatureUnit.valueOf(unitName);
            return new Quantity(value,unit);
        }

        throw new QuantityMeasurementException("Invalid measurement type");
    }

    private QuantityDTO convertQuantityToDTO(Quantity quantity){

        return new QuantityDTO(
                quantity.getValue(),
                quantity.getUnit().toString(),
                quantity.getUnit().getClass().getSimpleName()
        );
    }

    @Override
    public boolean compare(QuantityDTO q1,QuantityDTO q2){

        try{

            Quantity quantity1 = convertDTOToQuantity(q1);
            Quantity quantity2 = convertDTOToQuantity(q2);

            // Cross-category validation
            if(!q1.getMeasurementType().equalsIgnoreCase(q2.getMeasurementType())){
                throw new QuantityMeasurementException(
                        "Cannot compare different measurement categories"
                );
            }

            boolean result = quantity1.equals(quantity2);

            repository.save(new QuantityMeasurementEntity(
                    quantity1.toString(),
                    quantity2.toString(),
                    "COMPARE",
                    String.valueOf(result)
            ));

            return result;

        }catch(Exception e){
            throw new QuantityMeasurementException(e.getMessage());
        }
    }

    @Override
    public QuantityDTO convert(QuantityDTO source,String targetUnit){

        try{

            Quantity quantity=convertDTOToQuantity(source);

            IMeasurable unit=(IMeasurable)Enum.valueOf(
                    (Class<? extends Enum>)quantity.getUnit().getClass(),
                    targetUnit
            );

            Quantity result=quantity.convertTo(unit);

            repository.save(new QuantityMeasurementEntity(
                    quantity.toString(),
                    targetUnit,
                    "CONVERT",
                    result.toString()
            ));

            return convertQuantityToDTO(result);

        }catch(Exception e){
            throw new QuantityMeasurementException(e.getMessage());
        }
    }

    @Override
    public QuantityDTO add(QuantityDTO q1,QuantityDTO q2){

        try{

            Quantity quantity1=convertDTOToQuantity(q1);
            Quantity quantity2=convertDTOToQuantity(q2);

            Quantity result=quantity1.add(quantity2);

            repository.save(new QuantityMeasurementEntity(
                    quantity1.toString(),
                    quantity2.toString(),
                    "ADD",
                    result.toString()
            ));

            return convertQuantityToDTO(result);

        }catch(Exception e){
            throw new QuantityMeasurementException(e.getMessage());
        }
    }

    @Override
    public QuantityDTO subtract(QuantityDTO q1,QuantityDTO q2){

        try{

            Quantity quantity1=convertDTOToQuantity(q1);
            Quantity quantity2=convertDTOToQuantity(q2);

            Quantity result=quantity1.subtract(quantity2);

            repository.save(new QuantityMeasurementEntity(
                    quantity1.toString(),
                    quantity2.toString(),
                    "SUBTRACT",
                    result.toString()
            ));

            return convertQuantityToDTO(result);

        }catch(Exception e){
            throw new QuantityMeasurementException(e.getMessage());
        }
    }

    @Override
    public double divide(QuantityDTO q1,QuantityDTO q2){

        try{

            Quantity quantity1=convertDTOToQuantity(q1);
            Quantity quantity2=convertDTOToQuantity(q2);

            double result=quantity1.divide(quantity2);

            repository.save(new QuantityMeasurementEntity(
                    quantity1.toString(),
                    quantity2.toString(),
                    "DIVIDE",
                    String.valueOf(result)
            ));

            return result;

        }catch(Exception e){
            throw new QuantityMeasurementException(e.getMessage());
        }
    }
}