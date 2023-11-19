package carsharing.db.Service.Impl;

import carsharing.db.Model.Company;
import carsharing.db.Service.CompanyDaoService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CompanyDAO implements CompanyDaoService {

    private static final String SELECT_ALL = "SELECT * FROM COMPANY";
    private static final String insert = "INSERT INTO COMPANY (NAME) VALUES (?)";


    @Override
    public List<Company> findAll(Statement stmt) {
        List<Company> companyList = new ArrayList<>();
        try {
            ResultSet resultSet = stmt.executeQuery(SELECT_ALL);

            while(resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                companyList.add(new Company(name, id));
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return companyList;

    }

    @Override
    public Company findById(int id, Statement stmt) {
        List<Company> companyList = findAll(stmt);
        if(!companyList.isEmpty()){
            for(Company company: companyList){
                if(id == company.getId()){
                    return company;
                }
            }
        }
        return null;
    }

    @Override
    public void add(String company, Connection conn) {
        try(PreparedStatement preparedStatement = conn.prepareStatement(insert)){
            preparedStatement.setString(1, company);
            preparedStatement.executeUpdate();

            System.out.println("The company was created!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
