package com.example.fms_market.model;

import javafx.scene.image.Image;
import java.util.List;

public record Team_members(String name, List<String> tasks, Image personImage) {

    @Override
    public String toString() {
        return STR."Team_member{name='\{name}', tasks=\{tasks}}";
    }

}
