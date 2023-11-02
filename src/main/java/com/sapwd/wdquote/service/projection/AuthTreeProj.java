package com.sapwd.wdquote.service.projection;

public interface AuthTreeProj {
    Long getChildId();

    String getChildName();

    Long getParentId();

    String getParentName();

    Integer getParentType();
}
