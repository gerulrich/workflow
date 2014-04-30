package org.simple.workflow.mock.utils;

import org.simple.workflow.entity.DistributionGroup;

public class DGTestImpl
    implements DistributionGroup {

    private String id;
    private String name;

    public DGTestImpl(String id, String name) {
        super();
        this.id = id;
        this.name = name;
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
        DGTestImpl other = (DGTestImpl) obj;
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
