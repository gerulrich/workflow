package org.simple.workflow.services.impl;

import java.util.Map;

import org.simple.workflow.entity.Workflow;
import org.simple.workflow.services.WorkflowDatasource;
import org.simple.workflow.util.LRUMap;

public abstract class AbstractWorkflowDatasource
    implements WorkflowDatasource {

    private static final int CACHE_SIZE = 5;
    private Map<String, Workflow> cachedWorkflows = new LRUMap<String, Workflow>(CACHE_SIZE, CACHE_SIZE);
    private Workflow defaultWorkflow;

    @Override
    public Workflow resolveWorkflow(String version) {
        if (version == null) {
            if (this.defaultWorkflow == null) {
                this.defaultWorkflow = this.getDefaultWorkflow();
            }
            return this.defaultWorkflow;
        } else {
            return this.getByVersion(version);
        }
    }

    @Override
    public Workflow getByVersion(String version) {
        if (this.defaultWorkflow != null && this.defaultWorkflow.getVersion().equals(version)) {
            return this.defaultWorkflow;
        } else if (this.cachedWorkflows.containsKey(version)) {
            return this.cachedWorkflows.get(version);
        }
        return this.getFromRepository(version);
    }

    protected abstract Workflow getDefaultWorkflow();

    protected abstract Workflow getFromRepository(String version);

}
