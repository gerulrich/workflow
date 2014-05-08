package org.simple.workflow.impl;

import org.simple.workflow.entity.Node;

/**
 * @see {@link Node}
 * @author German Ulrich
 *
 */
public class NodeImpl
    implements Node {

    private String name;
    private String description;
    private boolean joinNode;
    private boolean endNode;

    public NodeImpl(String name, String description, boolean joinNode, boolean endNode) {
        super();
        this.name = name;
        this.description = description;
        this.joinNode = joinNode;
        this.endNode = endNode;
    }

    protected NodeImpl() {
        super();
    }

    @Override
    public String getName() {
        return this.name;
    }

    protected void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    protected void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean isJoinNode() {
        return this.joinNode;
    }

    protected void setJoinNode(boolean joinNode) {
        this.joinNode = joinNode;
    }

    @Override
	public boolean isEndNode() {
		return endNode;
	}

	protected void setEndNode(boolean endNode) {
		this.endNode = endNode;
	}
    
    
}
