package org.simple.workflow.mock.utils;

import java.util.ArrayList;
import java.util.List;

import org.simple.workflow.entity.Agent;
import org.simple.workflow.entity.DistributionGroup;

public class AgentTestImpl
    implements Agent {

    private String id;
    private String name;
    private DistributionGroup dg;
    private List<String> permissions = new ArrayList<String>();

    public AgentTestImpl(String agentId, String agentName, String dgId, String dgName, String... permissions) {
        this.id = agentId;
        this.name = agentName;
        this.dg = new DGTestImpl(dgId, dgName);
        for (String permission : permissions) {
            this.permissions.add(permission);
        }
    }

    @Override
    public String getOID() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public List<String> getPermissions() {
        return this.permissions;
    }

    @Override
    public DistributionGroup getDG() {
        return this.dg;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        AgentTestImpl other = (AgentTestImpl) obj;
        if (this.id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!this.id.equals(other.id)) {
            return false;
        }
        return true;
    }

}
