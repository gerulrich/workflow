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
    private String agentOID;

    public StepImpl(String name, String description, String agentOID, String agentName, Date date) {
        super();
        this.name = name;
        this.description = description;
        this.agentOID = agentOID;
        this.agentName = agentName;
        this.date = date;
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

    @Override
	public String getAgentOID() {
		return agentOID;
	}

	protected void setAgentOID(String agentOID) {
		this.agentOID = agentOID;
	}

}
