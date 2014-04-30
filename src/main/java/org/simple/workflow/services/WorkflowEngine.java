package org.simple.workflow.services;

import java.util.List;

import org.simple.workflow.entity.Agent;
import org.simple.workflow.entity.DistributionGroup;
import org.simple.workflow.entity.Transition;
import org.simple.workflow.entity.Workable;
import org.simple.workflow.entity.Workflow;
import org.simple.workflow.entity.WorkflowProcess;

public interface WorkflowEngine {

    /**
     * Inicia un nuevo proceso de workflow o "instancia".
     * @param workable objeto sobre el cual se inicia el {@link Workflow}
     * @param agent agente que realiza la acci&oacute;n.
     * @return un nuevo proceso de workflow.
     */
    WorkflowProcess start(Workable workable, Agent agent) throws WorkflowOperationException;

    /**
     * Obtiene un proceso de workflow a partir de su key.
     * @param key key del proceso.
     * @param agent agente que realiza la acci&oacute;n. Debe estar asociado al workflow.
     * @return
     */
    WorkflowProcess getByKey(String key, Agent agent) throws WorkflowOperationException;

    /**
     * Ejecuta una transicion sobre el workflow. 
     * @param workable 
     * @param transicion nombre de la transicion
     * @param agent agente que realiza la acci&oacute;n
     */
    void exec(Workable workable, String transicion, Agent agent) throws WorkflowOperationException;

    /**
     * Ejecuta una transicion de fork sobre el workflow. 
     * @param workable
     * @param transicion
     * @param agent
     * @param destinationDG
     */
    void fork(Workable workable, String transicion, Agent agent, DistributionGroup... destinationDG)
        throws WorkflowOperationException;


    /**
     * Obtiene una lista con las transiciones posibles seg&uacute;n el estado
     * del proceso y el agente que est&aacute; ejecutando la acci&oacute;n.
     * @param workable
     * @param agent
     * @return
     */
    List<Transition> getTransitions(Workable workable, Agent agent) throws WorkflowOperationException;


}
