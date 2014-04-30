package org.simple.workflow.entity;

import java.util.Date;
import java.util.List;

public interface WorkflowProcess {

    String getKey();

    String getVersion();

    Step getCurrentStep(DistributionGroup dg);

    boolean isValidDG(DistributionGroup dg);

    void addStep(String name, String description, Agent agent, Date date);

    void addStep(String name, String description, Agent agent, DistributionGroup dg, Date date);

    List<DistributionGroup> getDistributionGroups();
}
