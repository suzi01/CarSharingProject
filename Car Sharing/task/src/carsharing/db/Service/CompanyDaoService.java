package carsharing.db.Service;

import carsharing.db.Model.Company;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

public interface CompanyDaoService {
    List<Company> findAll(Statement stmt);
    Company findById(int id, Statement stmt);
    void add(String company, Connection conn);
}
