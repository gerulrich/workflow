package org.simple.workflow.entity;

import java.util.Date;

public interface Workable {

    String getKey();

    void addStep(Agent agent, DistributionGroup dg, String name, String description, Date date);

}
