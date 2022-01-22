package com.example.rentacarproject;

import java.util.ArrayList;

public class Menu_Form {
    private ArrayList<Car_Add> starters_list;
    private ArrayList<Car_Add> car_list;
    private ArrayList<Car_Add> truck_list;
    private ArrayList<Car_Add> bus_list;




    public Menu_Form() {

        car_list = new ArrayList<Car_Add>();
        truck_list = new ArrayList<Car_Add>();
        bus_list = new ArrayList<Car_Add>();

    }



    public void setCar_list(ArrayList<Car_Add> car_list) {
        this.car_list.addAll(car_list);
    }

    public void setTruck_list(ArrayList<Car_Add> truck_list) {
        this.truck_list.addAll(truck_list);
    }

    public void setbus_list(ArrayList<Car_Add> bus_list) {
        this.bus_list.addAll(bus_list);
    }





    public ArrayList<Car_Add> getCar_list() {
        return car_list;
    }

    public ArrayList<Car_Add> getTruck_list() {
        return truck_list;
    }

    public ArrayList<Car_Add> getBus_list() {
        return bus_list;
    }


    public boolean exist(Car_Add car){
        for (int i = 0; i < this.bus_list.size(); i++){
            if (bus_list.get(i).check_equal(car)){
                return true;
            }
        }
        for (int i = 0; i < car_list.size(); i++){
            if(car_list.get(i).check_equal(car)){
                return true;
            }
        }
        for (int i = 0; i < truck_list.size(); i++){
            if (truck_list.get(i).check_equal(car)){
                return true;
            }
        }


        return false;

    }

    public boolean add_car(Car_Add car){
        return car_list.add(car);

    }
    public boolean add_truck(Car_Add car){
        return truck_list.add(car);
    }
    public boolean add_bus(Car_Add car){
        return bus_list.add(car);
    }
    public int get_numof_cars(){
        int sum =  truck_list.size() + car_list.size() + bus_list.size();
        return sum;
    }
    public boolean remove_car(String name, String type){
        if (type == "Car"){
            for (int i = 0; i < car_list.size(); i++){
                String str = car_list.get(i).getCar_name();
                if (str.equals(name)){
                    Car_Add temp = car_list.get(i);
                    car_list.remove(temp);
                    return true;
                }
            }
            return false;
        }
        else if (type == "Truck"){
            for (int i = 0; i < truck_list.size(); i++){
                String str = truck_list.get(i).getCar_name();
                if (str.equals(name)){
                    Car_Add temp = truck_list.get(i);
                    truck_list.remove(temp);
                    return true;
                }
            }
            return false;
        }
        else if (type == "Bus"){
            for (int i = 0; i < bus_list.size(); i++){
                String str = bus_list.get(i).getCar_name();
                if (str.equals(name)){
                    Car_Add temp = bus_list.get(i);
                    bus_list.remove(temp);
                    return true;
                }
            }
            return false;
        }
        else{
            return false;
        }
    }

    public void clear_cars(){
        car_list.clear();
    }
    public void clear_trucks(){
        truck_list.clear();
    }
    public void clear_buses(){
        bus_list.clear();
    }
    public void replace_car(String name ,Car_Add car){
        if (car_list.contains(car)){
            for (int i = 0; i < car_list.size(); i++){
                if (car_list.get(i).getCar_name().equals(name)){
                    car_list.set(i, car);
                }
            }
        }
        else if (truck_list.contains(car)){
            for (int i = 0; i < truck_list.size(); i++){
                if (truck_list.get(i).getCar_name().equals(name)){
                    truck_list.set(i, car);
                }
            }
        }
        else if (bus_list.contains(car)){
            for (int i = 0; i < bus_list.size(); i++){
                if (bus_list.get(i).getCar_name().equals(name)){
                    bus_list.set(i, car);
                }
            }
        }
    }
}
