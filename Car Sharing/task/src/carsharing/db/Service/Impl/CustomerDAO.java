package carsharing.db.Service.Impl;


import carsharing.db.Model.Customer;
import carsharing.db.Service.CustomerDaoService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO implements CustomerDaoService {

    private static final String insert = "INSERT INTO CUSTOMER (NAME, RENTED_CAR_ID) VALUES (?,?)";
    private static final String update ="UPDATE CUSTOMER SET RENTED_CAR_ID=? WHERE ID=?" ;
    private static final String returnCar = "UPDATE CUSTOMER SET RENTED_CAR_ID=NULL WHERE ID=?";

    @Override
    public List<Customer> findAll(Statement stmt) {
        List<Customer> customerList = new ArrayList<>();
        try {
            String sql =  "SELECT * FROM CUSTOMER";
            ResultSet resultSet = stmt.executeQuery(sql);

            while(resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int rentedCarId = resultSet.getInt("rented_car_id");
                customerList.add(new Customer(id, rentedCarId,name));
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return customerList;
    }



    @Override
    public void add(String customerName, Connection conn) {
        try(PreparedStatement preparedStatement = conn.prepareStatement(insert)){
            preparedStatement.setString(1, customerName);
            preparedStatement.setNull(2, java.sql.Types.INTEGER);
            preparedStatement.executeUpdate();

            System.out.println("The customer was added!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean checkCustomerRented(int id, Statement stmt){
        List<Customer> customerList = findAll(stmt);
        if(!customerList.isEmpty()){
            for(Customer customer: customerList){
                if(id == customer.getId()){
                    if(customer.getRentedCarId() != 0){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public Customer findById(int id, Statement stmt) {
        List<Customer> customerList = findAll(stmt);
        if(!customerList.isEmpty()){
            for(Customer customer: customerList){
                if(id == customer.getId()){
                    return customer;
                }
            }
        }
        return null;
    }

@Override
public void updateCustomer(Customer customer, Statement stmt, Connection conn, int rentedCarId) {
    try(PreparedStatement preparedStatement = conn.prepareStatement(update)){
        preparedStatement.setInt(2, customer.getId());
        preparedStatement.setInt(1, rentedCarId );
        preparedStatement.executeUpdate();


    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
}


    @Override
    public void updateReturnCustomer(Customer customer, Statement stmt, Connection conn) {
        try(PreparedStatement preparedStatement = conn.prepareStatement(returnCar)){
            preparedStatement.setInt(1, customer.getId());
            preparedStatement.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}