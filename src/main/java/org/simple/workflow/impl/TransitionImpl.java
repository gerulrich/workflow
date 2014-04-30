package org.simple.workflow.impl;

import java.util.ArrayList;
import java.util.List;

import org.simple.workflow.entity.Transition;

public class TransitionImpl
    implements Transition {

    private String name;
    private String description;
    private String from;
    private String to;
    private String forkTo;
    private boolean forkTransition;
    private List<String> requiredTransitions;

    public TransitionImpl(String name, String description, String from, String to, String forkTo, boolean forkTransition,
        List<String> requiredTransitions) {
        super();
        this.name = name;
        this.description = description;
        this.from = from;
        this.to = to;
        this.forkTo = forkTo;
        this.forkTransition = forkTransition;
        this.requiredTransitions = requiredTransitions;
    }

    protected TransitionImpl() {
        super();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public String getFrom() {
        return this.from;
    }

    @Override
    public String getTo() {
        return this.to;
    }

    @Override
    public String getForkTo() {
        return this.forkTo;
    }

    @Override
    public boolean isForkTransition() {
        return this.forkTransition;
    }

    @Override
    public List<String> getRequiredPermissions() {
        return new ArrayList<String>(this.requiredTransitions);
    }

}
