package org.simple.workflow.services.impl;

import static java.lang.String.format;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.simple.workflow.entity.Agent;
import org.simple.workflow.entity.DistributionGroup;
import org.simple.workflow.entity.Node;
import org.simple.workflow.entity.Step;
import org.simple.workflow.entity.Transition;
import org.simple.workflow.entity.Workable;
import org.simple.workflow.entity.Workflow;
import org.simple.workflow.entity.WorkflowProcess;
import org.simple.workflow.services.WorkflowDatasource;
import org.simple.workflow.services.WorkflowEngine;
import org.simple.workflow.services.WorkflowOperationException;

public class WorkflowEngineImpl
    implements WorkflowEngine {

    private WorkflowDatasource datasource;

    public WorkflowDatasource getDatasource() {
        return this.datasource;
    }

    public void setDatasource(WorkflowDatasource datasource) {
        this.datasource = datasource;
    }

    @Override
    public WorkflowProcess start(Workable workable, Agent agent) throws WorkflowOperationException {
        Workflow workflow = this.getWorkflowByVersionOrDefault(null);
        WorkflowProcess process = this.datasource.newInstance(workable.getKey(), workflow.getVersion());
        Node firstNode = workflow.getNode();
        process.addStep(firstNode.getName(), firstNode.getDescription(), agent, new Date());
        workable.addStep(agent, agent.getDG(), firstNode.getName(), firstNode.getDescription(), new Date());
        this.datasource.save(process);
        return process;
    }

    @Override
    public WorkflowProcess getByKey(String key, Agent agent) throws WorkflowOperationException {
        WorkflowProcess process = this.datasource.getByKey(key);
        if (process != null && process.isValidDG(agent.getDG())) {
            return process;
        }
        String message = String.format("Invalid workflow process: key[%s], DG[%s]", key, agent.getDG().getName());
        throw new WorkflowOperationException(message);
    }

    @Override
    public void exec(Workable workable, String transicionName, Agent agent) throws WorkflowOperationException {
        WorkflowProcess process = this.getByKey(workable.getKey(), agent);
        Workflow workflow = this.getWorkflowByVersionOrDefault(process.getVersion());
        Transition transition = this.getTransitionAndValidate(process, workflow, transicionName, agent);
        this.executeTransition(workflow, process, workable, transition, agent);
    }

    @Override
    public void fork(Workable workable, String transitionName, Agent agent, DistributionGroup... destinationDG)
        throws WorkflowOperationException {
        WorkflowProcess process = this.getByKey(workable.getKey(), agent);
        Workflow workflow = this.getWorkflowByVersionOrDefault(process.getVersion());
        Transition transition = this.getTransitionAndValidate(process, workflow, transitionName, agent);
        this.executeTransition(workflow, process, workable, transition, agent, destinationDG);
    }

    @Override
    public List<Transition> getTransitions(Workable workable, Agent agent) throws WorkflowOperationException {
        List<Transition> allowedTransitions = new ArrayList<Transition>();
        WorkflowProcess process = this.getByKey(workable.getKey(), agent);
        Workflow workflow = this.getWorkflowByVersionOrDefault(process.getVersion());
        Step currentStep = process.getCurrentStep(agent.getDG());

        if (!this.isStandBy(workflow, process, agent)) {
            for (Transition transition : workflow.getTransitions(currentStep.getName())) {
                if (this.hasPermission(agent, transition)) {
                    allowedTransitions.add(transition);
                }
            }
        }

        return allowedTransitions;
    }

    /**
     * Evalua si el agente tiene permiso para ejecutar la transici&oacute;n.
     * @param agent
     * @param transition
     * @return
     */
    private boolean hasPermission(Agent agent, Transition transition) {
        List<String> requiredPermissions = transition.getRequiredPermissions();
        if (requiredPermissions == null || requiredPermissions.isEmpty()) {
            return true;
        }

        List<String> agentPermissions = agent.getPermissions();
        if (agentPermissions == null || agentPermissions.isEmpty()) {
            return false;
        }

        boolean hasPermission = false;
        for (String permission : agentPermissions) {
            if (requiredPermissions.contains(permission)) {
                hasPermission = true;
                break;
            }
        }

        return hasPermission;
    }

    private Workflow getWorkflowByVersionOrDefault(String version) throws WorkflowOperationException {
        Workflow workflow = this.datasource.resolveWorkflow(version);
        if (workflow != null) {
            return workflow;
        }
        throw new WorkflowOperationException("Workflow not found");
    }

    private Transition getTransitionAndValidate(WorkflowProcess process, Workflow workflow, String name, Agent agent)
        throws WorkflowOperationException {
        Step currentStep = process.getCurrentStep(agent.getDG());

        if (this.isStandBy(workflow, process, agent)) {
            String message = format("The process is in standby for step '%s' and DG '%s'", currentStep.getName(), agent
                .getDG().getName());
            throw new WorkflowOperationException(message);
        }

        Transition transition = workflow.getTransition(currentStep.getName(), name);
        if (transition == null || !currentStep.getName().equals(transition.getFrom())) {
            String message = format("Invalid transition '%s' for step '%s'", name, currentStep.getName());
            throw new WorkflowOperationException(message);
        }

        if (!this.hasPermission(agent, transition)) {
            String message = format("The agent '%s' has not permission for '%s' transition. Process[key=%s,DG=%s]",
                agent.getName(), transition.getName(), process.getKey(), agent.getDG().getName());
            throw new WorkflowOperationException(message);
        }

        // TODO validar transition.getTo() y transition.getForkTo() sean un nodo valido.

        return transition;
    }

    private void executeTransition(Workflow workflow, WorkflowProcess process, Workable workable, Transition transition,
        Agent agent, DistributionGroup... dg) {
        if (transition.isForkTransition() && ((dg == null || dg.length < 1))) {
            throw new IllegalArgumentException("Invalid transition arguments with fork transition");
        } else if (!transition.isForkTransition() && dg != null && dg.length > 0) {
            throw new IllegalArgumentException("Too many argument for transition");
        }

        Date currentDate = new Date();
        Node nextNode = workflow.getByName(transition.getTo());
        process.addStep(nextNode.getName(), nextNode.getDescription(), agent, currentDate);
        workable.addStep(agent, agent.getDG(), nextNode.getName(), nextNode.getDescription(), currentDate);

        if (transition.isForkTransition()) {
            Node forkNode = workflow.getByName(transition.getForkTo());
            for (DistributionGroup forkDg : dg) {
                process.addStep(forkNode.getName(), forkNode.getDescription(), agent, forkDg, currentDate);
                workable.addStep(agent, forkDg, forkNode.getName(), forkNode.getDescription(), currentDate);
            }
        }

        // if (nextNode.isEnd()) {
        // verificar si todos llegaron al final
        // si es asi, se da por terminado el proceso.
        // }

        this.datasource.save(process);

    }

    /**
     * Determina si el {@link WorkflowProcess} est&aacute; en "standby" (espera) para 
     * el Agente (o DG) especificado. El {@link WorkflowProcess} est&aacute; en espera 
     * si el nodo actual es un nodo de join, hay m&aacute;s de un {@link DistributionGroup} y alguno de ellos
     * no alcanz&oacute; el nodo en cuesti&oacute;n.
     * @param workflow
     * @param process
     * @param agent
     * @return
     */
    private boolean isStandBy(Workflow workflow, WorkflowProcess process, Agent agent) {
        boolean standby = false;
        Step currentStep = process.getCurrentStep(agent.getDG());
        Node currentNode = workflow.getByName(currentStep.getName());
        if (currentNode.isJoinNode()) {
            for (DistributionGroup dg : process.getDistributionGroups()) {
                List<Step> stepsForDG = process.getSteps(dg);
                boolean stepFound = false;
                for(Step step : stepsForDG) {
                	if (step.getName().equals(currentNode.getName())) {
                		stepFound = true;
                		break;
                    }
                }
                if (!stepFound) {
                	standby = true;
                }
            }
        }
        return standby;
    }

}
