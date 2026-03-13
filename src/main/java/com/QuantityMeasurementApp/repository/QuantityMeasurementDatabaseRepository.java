package com.QuantityMeasurementApp.repository;

import com.QuantityMeasurementApp.entity.QuantityMeasurementEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuantityMeasurementDatabaseRepository implements IQuantityMeasurementRepository{

    private static final String URL="jdbc:mysql://localhost:3306/quantity_measurement";
    private static final String USER="root";
    private static final String PASSWORD="root";

    private Connection getConnection() throws SQLException{
        return DriverManager.getConnection(URL,USER,PASSWORD);
    }

    @Override
    public void save(QuantityMeasurementEntity entity){

        String sql="INSERT INTO quantity_measurement_entity(operand1,operand2,operation,result) VALUES(?,?,?,?)";

        try(Connection conn=getConnection();
            PreparedStatement ps=conn.prepareStatement(sql)){

            ps.setString(1,entity.getOperand1());
            ps.setString(2,entity.getOperand2());
            ps.setString(3,entity.getOperation());
            ps.setString(4,entity.getResult());

            ps.executeUpdate();

        }catch(SQLException e){
            throw new RuntimeException("Database save failed",e);
        }
    }

    @Override
    public List<QuantityMeasurementEntity> getAllMeasurements(){

        List<QuantityMeasurementEntity> list=new ArrayList<>();

        String sql="SELECT operand1,operand2,operation,result FROM quantity_measurement_entity";

        try(Connection conn=getConnection();
            PreparedStatement ps=conn.prepareStatement(sql);
            ResultSet rs=ps.executeQuery()){

            while(rs.next()){

                list.add(new QuantityMeasurementEntity(
                        rs.getString("operand1"),
                        rs.getString("operand2"),
                        rs.getString("operation"),
                        rs.getString("result")
                ));
            }

        }catch(SQLException e){
            throw new RuntimeException("Database read failed",e);
        }

        return list;
    }

    @Override
    public long count(){

        String sql="SELECT COUNT(*) FROM quantity_measurement_entity";

        try(Connection conn=getConnection();
            PreparedStatement ps=conn.prepareStatement(sql);
            ResultSet rs=ps.executeQuery()){

            if(rs.next()){
                return rs.getLong(1);
            }

        }catch(SQLException e){
            throw new RuntimeException(e);
        }

        return 0;
    }

    @Override
    public void deleteAllMeasurements(){

        String sql="DELETE FROM quantity_measurement_entity";

        try(Connection conn=getConnection();
            PreparedStatement ps=conn.prepareStatement(sql)){

            ps.executeUpdate();

        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }
}