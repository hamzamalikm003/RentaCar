package com.example.rentacarproject;

import java.util.ArrayList;

public class OrderForm {


    private String order_num;
    private String owner_id;
    private String client_id;
    private String status;
    private double total_price = 0;

    private ArrayList<Car_Add> cars_orderd = new ArrayList<Car_Add>();

    public OrderForm(String order_num, String owner_id, String client_id, String status) {
        this.client_id = client_id;
        this.order_num = order_num;
        this.owner_id = owner_id;
        this.status = status;

    }

    public OrderForm(OrderForm ord) {
        this.client_id = ord.client_id;
        this.order_num = ord.order_num;
        this.owner_id = ord.owner_id;
        this.status = ord.status;

    }

    public void addCar(Car_Add car1) {
        this.cars_orderd.add(car1);
        total_price += car1.getPrice();
    }


    @Override
    public String toString() {
        String strToRet = "Status: " + status.toUpperCase() + ", " + "\n" +
                "Order Number: " + order_num.replaceAll("[^0-9]", "") + ", " + "\n" ;

        strToRet += "Vehicles: ";
        for (Car_Add car1 : cars_orderd) {
            strToRet += car1.getCar_name();
        }
        strToRet += "\n";
        strToRet += "Total Price: " + total_price + "\n";
        return strToRet;
    }

    public void removeCar(Car_Add car1) {
        this.cars_orderd.remove(car1);
        total_price -= car1.getPrice();
    }

    public String getClient_id() {
        return client_id;
    }

    public String getOrder_num() {
        return order_num;
    }

    public String getOwner_id() {
        return owner_id;
    }

    public String getStatus() {
        return status;
    }

    public ArrayList<Car_Add> getCars_orderd() {
        return cars_orderd;
    }

    public double getTotal_price() {
        return total_price;
    }


    public void setStatus(String status) {
        this.status = status;
    }
}




