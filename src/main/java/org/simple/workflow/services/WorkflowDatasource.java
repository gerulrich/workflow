package org.simple.workflow.services;

import org.simple.workflow.entity.Workflow;
import org.simple.workflow.entity.WorkflowProcess;

public interface WorkflowDatasource {

    Workflow resolveWorkflow(String version);

    Workflow getByVersion(String version);

    WorkflowProcess newInstance(String key, String version);

    WorkflowProcess getByKey(String key);

    void save(WorkflowProcess entity);

}
