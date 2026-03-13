package com.QuantityMeasurementApp;

import com.QuantityMeasurementApp.controller.QuantityMeasurementController;
import com.QuantityMeasurementApp.repository.QuantityMeasurementCacheRepository;
import com.QuantityMeasurementApp.service.IQuantityMeasurementService;
import com.QuantityMeasurementApp.service.QuantityMeasurementServiceImpl;
import com.QuantityMeasurementApp.DTO.QuantityDTO;

public class QuantityMeasurementApp {

    public static void main(String[] args){

        // Repository
        QuantityMeasurementCacheRepository repository =
                new QuantityMeasurementCacheRepository();

        // Service
        IQuantityMeasurementService service =
                new QuantityMeasurementServiceImpl(repository);

        // Controller
        QuantityMeasurementController controller =
                new QuantityMeasurementController(service);
        
        

        /* ===============================
           Example 1: Length Equality
        ================================= */

        QuantityDTO quantity1 = new QuantityDTO(
                2,
                "FEET",
                "Length"
        );

        QuantityDTO quantity2 = new QuantityDTO(
                24,
                "INCHES",
                "Length"
        );

        boolean comparisonResult =
                controller.performComparison(quantity1,quantity2);

        System.out.println("\n--- Equality Demonstration ---");
        System.out.println("Operation: COMPARISON");
        System.out.println("This Quantity: "+quantity1);
        System.out.println("That Quantity: "+quantity2);
        System.out.println("Comparison Result: "+comparisonResult);


        /* ===============================
           Example 2: Temperature Conversion
        ================================= */

        QuantityDTO temp1 = new QuantityDTO(
                0,
                "CELSIUS",
                "Temperature"
        );

        QuantityDTO temp2 = new QuantityDTO(
                0,
                "FAHRENHEIT",
                "Temperature"
        );

        QuantityDTO conversionResult =
                controller.performConversion(temp1,"FAHRENHEIT");

        System.out.println("\n--- Temperature Conversion ---");
        System.out.println("Conversion Result: "+conversionResult);


        /* ===============================
           Example 3: Temperature Addition Attempt
        ================================= */

        try{

            controller.performAddition(temp1,temp2);

        }catch(Exception ex){

            System.out.println("\n--- Temperature Addition Attempt ---");
            System.out.println("Temperature addition not supported: "
                    +ex.getMessage());
        }
    }
}