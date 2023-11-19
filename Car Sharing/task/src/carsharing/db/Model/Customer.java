package carsharing.db.Model;

public class Customer {
    private int id;
    private int rentedCarId;
    private String name;

    public Customer(int id, int rentedCarId, String name) {
        this.id = id;
        this.rentedCarId = rentedCarId;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getRentedCarId() {
        return rentedCarId;
    }

    public void setRentedCarId(int companyId) {
        this.rentedCarId = companyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return id + ". "+ name;
    }
}

