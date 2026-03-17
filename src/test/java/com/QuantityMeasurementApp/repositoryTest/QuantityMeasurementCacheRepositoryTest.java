package com.QuantityMeasurementApp.repositoryTest;

import com.QuantityMeasurementApp.repository.QuantityMeasurementCacheRepository;
import com.QuantityMeasurementApp.entity.QuantityMeasurementEntity;

import org.junit.Test;

import static org.junit.Assert.*;

public class QuantityMeasurementCacheRepositoryTest {

    @Test
    public void testCacheRepository_Save(){

        QuantityMeasurementCacheRepository repo=new QuantityMeasurementCacheRepository();

        repo.save(new QuantityMeasurementEntity("2 FEET","24 INCHES","COMPARE","true"));

        assertEquals(1,repo.getAllMeasurements().size());
    }

    @Test
    public void testCacheRepository_DeleteAll(){

        QuantityMeasurementCacheRepository repo=new QuantityMeasurementCacheRepository();

        repo.save(new QuantityMeasurementEntity("2 FEET","24 INCHES","COMPARE","true"));

        repo.deleteAllMeasurements();

        assertEquals(0,repo.count());
    }
}
