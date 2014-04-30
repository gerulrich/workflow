package org.simple.workflow.impl;

import java.util.Date;

import org.simple.workflow.entity.Step;

/**
 * @see {@link Step}
 * @author German Ulrich
 *
 */
public class StepImpl
    implements Step {

    private String name;
    private String description;
    private Date date;
    private String agentName;

    public StepImpl(String name, String description, String agentName, Date date) {
        super();
        this.name = name;
        this.description = description;
        this.date = date;
        this.agentName = agentName;
    }

    protected StepImpl() {
        super();
    }

    @Override
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String getAgentName() {
        return this.agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

}
