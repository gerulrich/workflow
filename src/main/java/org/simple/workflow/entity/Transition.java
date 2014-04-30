package org.simple.workflow.entity;

import java.util.List;

public interface Transition {

    String getName();

    String getDescription();

    String getFrom();

    String getTo();

    String getForkTo();

    boolean isForkTransition();

    List<String> getRequiredPermissions();

}
