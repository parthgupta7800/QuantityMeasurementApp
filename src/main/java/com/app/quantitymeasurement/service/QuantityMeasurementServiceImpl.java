package com.app.quantitymeasurement.service;

import com.app.quantitymeasurement.DTO.QuantityDTO;
import com.app.quantitymeasurement.Quantity;
import com.app.quantitymeasurement.entity.QuantityMeasurementEntity;
import com.app.quantitymeasurement.entity.User;
import com.app.quantitymeasurement.exception.QuantityMeasurementException;
import com.app.quantitymeasurement.repository.QuantityMeasurementRepository;
import com.app.quantitymeasurement.repository.UserRepository;
import com.app.quantitymeasurement.units.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuantityMeasurementServiceImpl implements IQuantityMeasurementService {

    @Autowired
    private QuantityMeasurementRepository repository;

    @Autowired
    private UserRepository userRepository;

    // 🔥 Get logged-in user
    private User getCurrentUser(){
        var auth=SecurityContextHolder.getContext().getAuthentication();

        if(auth==null||!auth.isAuthenticated())
            return null;

        String email=auth.getName();

        return userRepository.findByEmail(email).orElse(null);
    }

    // Save only if logged-in
    private void saveHistory(String op1,String op2,String operation,String result){

        User user=getCurrentUser();

        if(user==null) return; // guest → skip saving

        QuantityMeasurementEntity entity=new QuantityMeasurementEntity(op1,op2,operation,result);
        entity.setUser(user);

        repository.save(entity);
    }

    // Convert DTO → Quantity
    private Quantity convertDTOToQuantity(QuantityDTO dto){

        String unitName=dto.getUnit().toUpperCase();
        double value=dto.getValue();

        switch(dto.getMeasurementType().toUpperCase()){

            case "LENGTH":
                return new Quantity(value,LengthUnit.valueOf(unitName));

            case "WEIGHT":
                return new Quantity(value,WeightUnit.valueOf(unitName));

            case "VOLUME":
                return new Quantity(value,VolumeUnit.valueOf(unitName));

            case "TEMPERATURE":
                return new Quantity(value,TemperatureUnit.valueOf(unitName));

            default:
                throw new QuantityMeasurementException("Invalid measurement type");
        }
    }

    private QuantityDTO convertQuantityToDTO(Quantity quantity){

        String measurementType=quantity.getUnit().getClass()
                .getSimpleName()
                .replace("Unit","")
                .toUpperCase();

        return new QuantityDTO(
                quantity.getValue(),
                quantity.getUnit().toString(),
                measurementType
        );
    }

    // ========================= OPERATIONS =========================

    @Override
    public boolean compare(QuantityDTO q1,QuantityDTO q2){

        Quantity quantity1=convertDTOToQuantity(q1);
        Quantity quantity2=convertDTOToQuantity(q2);

        if(!q1.getMeasurementType().equalsIgnoreCase(q2.getMeasurementType())){
            throw new QuantityMeasurementException("Different measurement types");
        }

        boolean result=quantity1.equals(quantity2);

        saveHistory(quantity1.toString(),quantity2.toString(),"COMPARE",String.valueOf(result));

        return result;
    }

    @Override
    public QuantityDTO convert(QuantityDTO source,String targetUnit){

        Quantity quantity=convertDTOToQuantity(source);

        IMeasurable unit=(IMeasurable)Enum.valueOf(
                (Class<? extends Enum>)quantity.getUnit().getClass(),
                targetUnit.toUpperCase()
        );

        Quantity result=quantity.convertTo(unit);

        saveHistory(quantity.toString(),targetUnit,"CONVERT",result.toString());

        return convertQuantityToDTO(result);
    }

    @Override
    public QuantityDTO add(QuantityDTO q1,QuantityDTO q2){

        Quantity quantity1=convertDTOToQuantity(q1);
        Quantity quantity2=convertDTOToQuantity(q2);

        Quantity result=quantity1.add(quantity2);

        saveHistory(quantity1.toString(),quantity2.toString(),"ADD",result.toString());

        return convertQuantityToDTO(result);
    }

    @Override
    public QuantityDTO subtract(QuantityDTO q1,QuantityDTO q2){

        Quantity quantity1=convertDTOToQuantity(q1);
        Quantity quantity2=convertDTOToQuantity(q2);

        Quantity result=quantity1.subtract(quantity2);

        saveHistory(quantity1.toString(),quantity2.toString(),"SUBTRACT",result.toString());

        return convertQuantityToDTO(result);
    }

    @Override
    public double divide(QuantityDTO q1,QuantityDTO q2){

        Quantity quantity1=convertDTOToQuantity(q1);
        Quantity quantity2=convertDTOToQuantity(q2);

        double result=quantity1.divide(quantity2);

        saveHistory(quantity1.toString(),quantity2.toString(),"DIVIDE",String.valueOf(result));

        return result;
    }

    // ========================= HISTORY =========================

    @Override
    public List<QuantityMeasurementEntity> getHistory(String operation){

        User user=getCurrentUser();

        if(user==null){
            throw new QuantityMeasurementException("Login required to view history");
        }

        return repository.findByUserAndOperationIgnoreCase(user,operation);
    }

    @Override
    public long getCount(String operation){

        User user=getCurrentUser();

        if(user==null){
            throw new QuantityMeasurementException("Login required to view count");
        }

        return repository.countByUserAndOperationIgnoreCase(user,operation);
    }
}