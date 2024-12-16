package com.example.fms_market.model;

import javafx.scene.image.Image;
import java.util.List;

public class Team_members {
    private String name;
    private Image personImage;
    private List<String> tasks;

    public Team_members(String name,List<String> tasks, Image personImage) {
        this.name = name;
        this.tasks = tasks;
        this.personImage = personImage;
    }

    public String getName() {
        return name;
    }
    public Image getPersonImage() {
        return personImage;
    }

    public List<String> getTasks() {
        return tasks;
    }

    @Override
    public String toString() {
        return "Team_member{name='" + name + "', tasks=" + tasks + "}";
    }


}
