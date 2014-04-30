package org.simple.workflow.entity;

import java.util.List;

public interface Agent {

    String getOID();

    String getName();

    List<String> getPermissions();

    DistributionGroup getDG();
}
