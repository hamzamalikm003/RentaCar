package com.example.rentacarproject;

public class Owner_Form {
    private String name;
    private String location;
    private String phone;
    private String description;
    private String UID;


    public Owner_Form() {
        this.name = "";
        this.location = "";
        this.phone = "";
        this.description = "";
    }

    public Owner_Form(String Name, String Location, String Phone, String desc,String UID ) {
        this.name = Name;
        this.location = Location;
        this.phone = Phone;
        this.description = desc;
        this.UID = UID;

    }



    public Owner_Form(Owner_Form toCopy) {
        this.name = toCopy.getName();
        this.location = toCopy.getLocation();
        this.phone = toCopy.getPhone();
        this.description = toCopy.getDescription();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }
}
