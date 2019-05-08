package com.example.smarthome;

public class Controller {
    private int id;
    private String displayable_name;
    private String room_name;
    private String equipment_name;
    private boolean status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDisplayable_name() {
        return displayable_name;
    }

    public void setDisplayable_name(String displayable_name) {
        this.displayable_name = displayable_name;
    }

    public String getRoom_name() {
        return room_name;
    }

    public void setRoom_name(String room_name) {
        this.room_name = room_name;
    }

    public String getEquipment_name() {
        return equipment_name;
    }

    public void setEquipment_name(String equipment_name) {
        this.equipment_name = equipment_name;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
