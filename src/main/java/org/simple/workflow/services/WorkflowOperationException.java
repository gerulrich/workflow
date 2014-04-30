package org.simple.workflow.services;

public class WorkflowOperationException
    extends Exception {

    private static final long serialVersionUID = 1L;

    public WorkflowOperationException() {
        super();
    }

    public WorkflowOperationException(String message, Throwable cause) {
        super(message, cause);
    }

    public WorkflowOperationException(String message) {
        super(message);
    }

    public WorkflowOperationException(Throwable cause) {
        super(cause);
    }
}
