package carsharing.db.Service;

import carsharing.db.Model.Customer;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

public interface CustomerDaoService {
    List<Customer> findAll(Statement stmt);
    void add(String customerName, Connection conn);
    boolean checkCustomerRented(int id, Statement stmt);
    void updateCustomer(Customer customer, Statement stmt, Connection conn, int rentedCarId);
    Customer findById(int id, Statement stmt);
    void updateReturnCustomer(Customer customer, Statement stmt, Connection conn);

}