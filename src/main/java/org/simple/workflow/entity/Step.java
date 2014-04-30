package org.simple.workflow.entity;

import java.util.Date;

public interface Step {

    String getName();

    String getDescription();

    Date getDate();

    String getAgentName();
}
