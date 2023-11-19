package carsharing.db.Service;


import carsharing.db.Model.Car;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

public interface CarDaoService {
    List<Car> findAll(Connection conn, int id);
    List<Car> findAvailableCars(Connection conn, int companyId);
    void add(String carName, int companyId, Connection conn);
    Car findById(int id, Statement stmt, Connection conn);
}
