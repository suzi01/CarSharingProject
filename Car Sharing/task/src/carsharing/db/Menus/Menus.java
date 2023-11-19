package carsharing.db.Menus;

import carsharing.db.Model.Car;
import carsharing.db.Model.Company;
import carsharing.db.Model.Customer;
import carsharing.db.Service.Impl.CarDAO;
import carsharing.db.Service.Impl.CompanyDAO;
import carsharing.db.Service.Impl.CustomerDAO;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import static carsharing.db.Util.MenuUtil.*;

public class Menus {

    private static final CompanyDAO companyDao = new CompanyDAO();
    private static final CarDAO carDao = new CarDAO();
    private static final CustomerDAO customerDao = new CustomerDAO();

    public static void mainMenuInput(Statement stmt, Connection conn) {
        int userResponse;
        do{
            System.out.println("1. Log in as a manager");
            System.out.println("2. Log in as a customer");
            System.out.println("3. Create a customer");
            System.out.println("0. Exit");
            userResponse =  getUserSelection( );

            switch ( userResponse ){
                case 1 -> managerMenuInput(stmt, conn);
                case 2 -> customerList(stmt, conn);
                case 3 -> createCustomer(conn);
                case 0 -> System.exit(0);
                default ->  System.out.println("Invalid choice. Please try again.");
            }
        } while(userResponse != 0);
    }



    private static void managerMenuInput(Statement stmt, Connection conn) {
        int userResponse;
        do{
            System.out.println("1. Company list");
            System.out.println("2. Create a company");
            System.out.println("0. Back");
            userResponse =  getUserSelection( );

            switch ( userResponse ){
                case 1 -> companyList(stmt, conn);
                case 2 -> addCompany(conn);
                case 0 -> mainMenuInput( stmt,conn );
                default ->  System.out.println("Invalid choice. Please try again.");
            }
        } while(userResponse != 0);

    }

    public static void selectedCompanyMenu(Statement stmt, Connection conn, int userSelection){
        int userResponse;
        do{
            System.out.println("1. Car list");
            System.out.println("2. Create a car");
            System.out.println("0. Back");
            userResponse =  getUserSelection( );

            switch ( userResponse ){
                case 1 -> carList(conn,userSelection);
                case 2 -> createCar(userSelection, conn);
                case 0 -> managerMenuInput( stmt,conn );
                default ->  System.out.println("Invalid choice. Please try again.");
            }

        }while(userResponse != 0);
    }

    public static void customerMenu(Statement stmt, Customer customer, Connection conn ){
        int userResponse;
        do{
            System.out.println("1. Rent a car");
            System.out.println("2. Return a rented car");
            System.out.println("3. My rented car");
            System.out.println("0. Back");
            userResponse = getUserSelection();
            switch (userResponse){
                case 1 -> customer = rentCar(customer, stmt, conn);
                case 2 -> customer = returnCar(customer, stmt, conn);
                case 3 -> getCustomerRentedCar(customer, stmt, conn);
                case 0 -> mainMenuInput( stmt, conn );
            }
        }while ( userResponse != 0);

    }

    public static void companyList(Statement stmt, Connection conn){
        int userSelection;
        List<Company> companyList = companyDao.findAll(stmt);
        if (companyList.isEmpty()) {
            System.out.println("The company list is empty!");
        } else {
            do{
                companyListOptions( companyList );
                userSelection = getUserSelection( );

                int param = userSelection > companyList.size() ? -1 : userSelection == 0 ? 0 : 1;
                switch ( param ){
                    case 1 -> {
                        Company checkCompany = companyDao.findById(userSelection, stmt);
                        printSelectedCompanyName(param, checkCompany.getName());

                        selectedCompanyMenu(stmt, conn,userSelection);}
                    case 0 -> managerMenuInput( stmt,conn );
                    default -> System.out.println( "Invalid option. Please try again");
                }
            } while(userSelection != 0);
        }
    }

    public static void customerList(Statement stmt, Connection conn){
        int userSelection;
        List<Customer> customerList = customerDao.findAll(stmt);
        if (customerList.isEmpty()) {
            System.out.println("The customer list is empty!");
            System.out.println();
        } else {
            System.out.println("Choose a customer:");
            customerList.forEach(System.out::println);
            System.out.println("0. Back");
            System.out.println();
            userSelection = getUserSelection();
            Customer selectedCustomer = findCustomerInCustomer(userSelection, customerList);


            int param = userSelection > customerList.size() ? -1 : userSelection == 0 ? 0 : 1;
            switch ( param ){
                case 1 -> customerMenu(stmt, selectedCustomer, conn); // extract customer with id userSelection from customerList
                case 0 -> mainMenuInput( stmt,conn );
                default -> System.out.println( "Invalid option. Please try again");
            }
        }
    }

    private static Company rentCompanyList(Statement stmt) {
        List<Company> companyList = companyDao.findAll(stmt);
        if (companyList.isEmpty()) {
            System.out.println("The company list is empty!");
        }
        else {
            companyListOptions( companyList );
            int userSelected = getUserSelection( );

            int param = userSelected > companyList.size() ? -1 : userSelected == 0 ? 0 : 1;
            Company checkCompany = companyDao.findById(userSelected, stmt);
            switch(param){
                case -1, 0 -> {
                    return null;
                }
                case 1 -> {
                    return checkCompany;
                }
            }
        }
        return null;
    }
    public static Customer rentCar(Customer customer, Statement stmt, Connection conn){
        boolean rentCheck = customerDao.checkCustomerRented(customer.getId(), stmt);
        if(rentCheck){
            System.out.println("You've already rented a car!");
        }
        else{
            Company chosenCompany = rentCompanyList(stmt);
            if(chosenCompany != null){
                carList(conn, chosenCompany.getId());
                int chosenCar = getUserSelection();
                if(chosenCar!= 0){

                    customerDao.updateCustomer(customer, stmt, conn,chosenCar);
                    Car car = carDao.findById(chosenCar,stmt, conn);
                    System.out.printf("You rented '%s'", car.getName());

                    customer = customerDao.findById(chosenCompany.getId(), stmt);
                }
            }
        } return customer;
    }
}
