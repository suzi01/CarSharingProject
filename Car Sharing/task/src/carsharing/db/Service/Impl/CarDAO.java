package carsharing.db.Service.Impl;

import carsharing.db.Model.Car;
import carsharing.db.Service.CarDaoService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarDAO implements CarDaoService {

    private static final String insert = "INSERT INTO CAR (NAME, COMPANY_ID) VALUES (?, ?)";
    private static final String findSQL =  "SELECT * FROM CAR WHERE COMPANY_ID=?" ;
    private static final String findAvailable = "SELECT ID,NAME,COMPANY_ID FROM CAR WHERE COMPANY_ID =? AND ID NOT IN (SELECT RENTED_CAR_ID FROM CUSTOMER WHERE RENTED_CAR_ID IS NOT NULL)";
    private static final String findByName  =  "SELECT * FROM CAR WHERE NAME=?" ;


//    private static Connection conn;

    @Override
    public List<Car> findAll(Connection conn, int companyId) {
        List<Car> carList = new ArrayList<>();
        try (PreparedStatement preparedStatement = conn.prepareStatement(findSQL)){
            preparedStatement.setInt(1, companyId );
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                carList.add(new Car(id,companyId,name));
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return carList;
    }

    @Override
    public List<Car> findAvailableCars(Connection conn, int companyId) {
        List<Car> carList = new ArrayList<>();
        try (PreparedStatement preparedStatement = conn.prepareStatement(findAvailable)){
            preparedStatement.setInt(1, companyId );
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                carList.add(new Car(id,companyId,name));
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return carList;
    }



    @Override
    public void add(String carName, int id, Connection conn) {

        try(PreparedStatement preparedStatement = conn.prepareStatement(insert)){
            preparedStatement.setString(1, carName);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();

            System.out.println("The car was added!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    public Car findById(int companyId, Statement stmt, Connection conn) {
        List<Car> carList = findAll(conn, companyId);
        System.out.println(carList);
        if(!carList.isEmpty()){
            for(Car car: carList){
                if(companyId == car.getId()){
                    return car;
                }
            }
        }
        return null;
    }




}
