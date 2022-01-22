package com.example.rentacarproject;

public class Car_Add {
    private String car_name;
    private String car_description;
    private double price;

    public Car_Add(double price,String car_name, String car_description) {
        this.car_name = car_name;
        this.car_description = car_description;
        this.price = price;
    }

    public String getCar_name() {
        return car_name;
    }

    public String getCar_description() {
        return car_description;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        String str =   car_name +" , price: " + price +" " + " , description: " + car_description;
        return  str;
    }

    public boolean check_equal(Car_Add car){
        if (this.getCar_name().equals(car.getCar_name())){
            return true;
        }
        else {
            return false;
        }
    }


    public void set_Car( Car_Add temp_car) {
        this.car_name = temp_car.getCar_name();
        this.price = temp_car.getPrice();
        this.car_description = temp_car.car_description;
    }

}
