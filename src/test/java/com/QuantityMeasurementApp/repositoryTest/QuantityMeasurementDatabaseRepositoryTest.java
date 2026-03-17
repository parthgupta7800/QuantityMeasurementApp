package com.QuantityMeasurementApp.repositoryTest;


import com.QuantityMeasurementApp.repository.IQuantityMeasurementRepository;
import com.QuantityMeasurementApp.repository.QuantityMeasurementDatabaseRepository;
import com.QuantityMeasurementApp.entity.QuantityMeasurementEntity;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class QuantityMeasurementDatabaseRepositoryTest {

    @Test
    public void testDatabaseRepository_SaveEntity(){

        IQuantityMeasurementRepository repo=new QuantityMeasurementDatabaseRepository();

        repo.save(new QuantityMeasurementEntity("2 FEET","24 INCHES","COMPARE","true"));

        List<?> list=repo.getAllMeasurements();

        assertTrue(list.size()>0);
    }

    @Test
    public void testDatabaseRepository_Count(){

        IQuantityMeasurementRepository repo=new QuantityMeasurementDatabaseRepository();

        long count=repo.count();

        assertTrue(count>=0);
    }
}
