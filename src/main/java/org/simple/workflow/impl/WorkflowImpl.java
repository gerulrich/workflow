package org.simple.workflow.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.simple.workflow.entity.Node;
import org.simple.workflow.entity.Transition;
import org.simple.workflow.entity.Workflow;

/**
 * @see {@link Workflow}
 * @author German Ulrich
 *
 */
public class WorkflowImpl
    implements Workflow {

    private String version;
    private List<Node> nodes;
    private List<Transition> transitions;

    private Map<String, Map<String, Transition>> indexedTransitions;
    private Map<String, Node> indexedNodes;

    public WorkflowImpl(List<Node> nodes, List<Transition> transitions, String version) {
        this();
        this.nodes.addAll(nodes);
        this.transitions.addAll(transitions);
        this.version = version;
        this.index();
    }

    protected WorkflowImpl() {
        this.nodes = new ArrayList<Node>();
        this.transitions = new ArrayList<Transition>();
        this.indexedTransitions = new LinkedHashMap<String, Map<String, Transition>>();
        this.indexedNodes = new LinkedHashMap<String, Node>();
    }

    @Override
    public Node getNode() {
        if (!this.nodes.isEmpty()) {
            return this.nodes.get(0);
        }
        return null;
    }

    @Override
    public Node getByName(String name) {
        return this.getIndexedNodes().get(name);
    }

    @Override
    public Transition getTransition(String nodeName, String transicionName) {
        Map<String, Transition> transitionByNode = this.getIndexedTransitions().get(nodeName);
        if (transitionByNode != null) {
            return transitionByNode.get(transicionName);
        }
        return null;
    }

    @Override
    public List<Transition> getTransitions(String nodeName) {
        Map<String, Transition> transitionByNode = this.getIndexedTransitions().get(nodeName);
        if (transitionByNode != null) {
            return new ArrayList<Transition>(transitionByNode.values());
        }
        return null;
    }

    @Override
    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    protected List<Node> getNodes() {
        return this.nodes;
    }

    protected void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    protected List<Transition> getTransitions() {
        return this.transitions;
    }

    protected void setTransitions(List<Transition> transitions) {
        this.transitions = transitions;
    }

    protected Map<String, Map<String, Transition>> getIndexedTransitions() {
        return this.indexedTransitions;
    }

    protected Map<String, Node> getIndexedNodes() {
        return this.indexedNodes;
    }

    protected void index() {
        for (Node node : this.nodes) {
            this.indexedNodes.put(node.getName(), node);
        }
        for (Transition transition : this.transitions) {
            if (!this.indexedTransitions.containsKey(transition.getFrom())) {
                this.indexedTransitions.put(transition.getFrom(), new LinkedHashMap<String, Transition>());
            }
            this.indexedTransitions.get(transition.getFrom()).put(transition.getName(), transition);
        }
    }
}
