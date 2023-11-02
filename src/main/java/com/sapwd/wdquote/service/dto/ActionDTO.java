package com.sapwd.wdquote.service.dto;

import java.util.HashSet;
import java.util.Set;

public class ActionDTO {

    private String action;
    private Set<String> roles = new HashSet<>();

    public ActionDTO() {}

    public ActionDTO(String action, Set<String> roles) {
        this.action = action;
        this.roles = roles;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "ActionDTO{" + "action='" + action + '\'' + '}';
    }
}
