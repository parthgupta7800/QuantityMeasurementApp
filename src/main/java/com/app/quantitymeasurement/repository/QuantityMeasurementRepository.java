package com.app.quantitymeasurement.repository;

import com.app.quantitymeasurement.entity.QuantityMeasurementEntity;
import com.app.quantitymeasurement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuantityMeasurementRepository
        extends JpaRepository<QuantityMeasurementEntity,Long> {

    // NEW: User-specific history
    List<QuantityMeasurementEntity> findByUserAndOperationIgnoreCase(User user,String operation);

    long countByUserAndOperationIgnoreCase(User user,String operation);
}