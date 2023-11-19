package carsharing.db.Util;

import carsharing.db.Model.Car;
import carsharing.db.Model.Company;
import carsharing.db.Model.Customer;
import carsharing.db.Service.Impl.CarDAO;
import carsharing.db.Service.Impl.CompanyDAO;
import carsharing.db.Service.Impl.CustomerDAO;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;
import java.util.Scanner;

public class MenuUtil {

    private static final CompanyDAO companyDao = new CompanyDAO();
    private static final CarDAO carDao = new CarDAO();
    private static final CustomerDAO customerDao = new CustomerDAO();
    private static final Scanner scanner = new Scanner(System.in);
    public static int getUserSelection()
    {
        String input = scanner.nextLine();
        return Integer.parseInt( input );
    }



    public static void companyListOptions(final List<Company> companyList)
    {
        System.out.println("Choose the company:");
        companyList.forEach( System.out::println );
        System.out.println("0. Back");
        System.out.println();
    }

    public static void printSelectedCompanyName(final int param,
                                                final String checkCompany)
    {
        if(param > 0){
            selectedCompanyName( checkCompany );
        }
    }

    public static Customer findCustomerInCustomer(final int userSelection,
                                                  final List<Customer> customerList)
    {
        for(Customer customer: customerList){
            if(userSelection == customer.getId()){
                return customer;
            }
        }
        return null;
    }


    public static void selectedCompanyName(String companyName){
        System.out.printf("'%s' company", companyName);
        System.out.println();
    }


    public static void carList(Connection conn, int id){
        List<Car> carList = carDao.findAvailableCars(conn,id);
        if (carList.isEmpty()) {
            System.out.println("The car list is empty!");
        } else {
            System.out.println("Car list:");
            for(int i = 0; i<carList.size();i++){
                Car car = carList.get(i);
                System.out.printf("%d. %s%n", i+1, car.getName() );
            }
            System.out.println();
        }
    }

    public static void getCustomerRentedCar(Customer customer, Statement stmt, Connection conn){
        boolean rentCheck = customerDao.checkCustomerRented(customer.getId(), stmt);
        if(!rentCheck){
            System.out.println("You didn't rent a car!");
        }
        else{

            System.out.println("Your rented car");
            Car car = carDao.findById(customer.getRentedCarId(), stmt,conn);
            System.out.println(car.getName());

            System.out.println("Company");
            Company foundCompany = companyDao.findById(car.getCompanyId(),stmt);
            System.out.println(foundCompany.getName());
        }
    }


    public static Customer returnCar( Customer customer, Statement stmt, Connection conn ){
        boolean hasRented = customerDao.checkCustomerRented(customer.getId(), stmt);
        if (hasRented){
            customerDao.updateReturnCustomer(customer, stmt, conn);
            System.out.println("You've returned a rented car!");
            customer = customerDao.findById(customer.getId(), stmt);
        } else {
            System.out.println("You didn't rent a car!");
        }
        return customer;
    }

    public static void addCompany(Connection conn) {
        System.out.println("Enter the company name:");
        String company = scanner.nextLine();
        companyDao.add(company, conn);
    }

    public static void createCar(int id, Connection conn){
        System.out.println("Enter the car name:");
        String car = scanner.nextLine();
        carDao.add(car, id, conn);
    }

    public static void createCustomer(Connection conn){
        System.out.println("Enter the customer name:");
        String customerName = scanner.nextLine();
        customerDao.add(customerName, conn);
    }
}
