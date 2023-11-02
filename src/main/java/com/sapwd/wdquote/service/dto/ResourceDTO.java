package com.sapwd.wdquote.service.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ResourceDTO implements Serializable {

    private String name;
    private Map<String, ActionDTO> actions = new HashMap<>();

    public ResourceDTO() {}

    public ResourceDTO(String name, Map<String, ActionDTO> actions) {
        this.name = name;
        this.actions = actions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, ActionDTO> getActions() {
        return actions;
    }

    public void setActions(HashMap<String, ActionDTO> actions) {
        this.actions = actions;
    }

    @Override
    public String toString() {
        return "ResourceDTO{" + "name='" + name + '\'' + '}';
    }
}
