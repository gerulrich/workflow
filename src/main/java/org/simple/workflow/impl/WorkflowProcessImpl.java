package org.simple.workflow.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.simple.workflow.entity.Agent;
import org.simple.workflow.entity.DistributionGroup;
import org.simple.workflow.entity.Step;
import org.simple.workflow.entity.WorkflowProcess;

public class WorkflowProcessImpl
    implements WorkflowProcess {

    private String version;
    private String key;
    private List<Step> steps;
    private Map<String, Step> currentStep;
    private Set<DistributionGroup> distributionGroup;

    public WorkflowProcessImpl(String key, String version) {
        this();
        this.key = key;
        this.version = version;
    }

    protected WorkflowProcessImpl() {
        this.steps = new ArrayList<Step>();
        this.currentStep = new HashMap<String, Step>();
        this.distributionGroup = new LinkedHashSet<DistributionGroup>();
    }

    @Override
    public String getKey() {
        return this.key;
    }

    @Override
    public String getVersion() {
        return this.version;
    }

    @Override
    public Step getCurrentStep(DistributionGroup dg) {
        return this.currentStep.get(dg.getOID());
    }

    @Override
    public void addStep(String name, String description, Agent agent, Date date) {
        this.addStep(this.createStep(name, description, agent.getName(), date), agent.getDG());
    }

    @Override
    public void addStep(String name, String description, Agent agent, DistributionGroup dg, Date date) {
        this.addStep(this.createStep(name, description, agent.getName(), date), dg);
    }

    @Override
    public List<DistributionGroup> getDistributionGroups() {
        return new ArrayList<DistributionGroup>(this.distributionGroup);
    }

    @Override
    public boolean isValidDG(DistributionGroup dg) {
        return this.currentStep != null && this.currentStep.containsKey(dg.getOID());
    }

    private void addStep(Step step, DistributionGroup dg) {
        this.steps.add(step);
        if (!this.currentStep.containsKey(dg.getOID())) {
            this.distributionGroup.add(dg);
        }
        this.currentStep.put(dg.getOID(), step);
    }

    protected Step createStep(String name, String description, String agentName, Date date) {
        return new StepImpl(name, description, agentName, date);
    }

}
