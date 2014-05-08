package org.simple.workflow.entity;

import java.util.Date;

/**
 * Representa un "item" que tiene sociado un {@link WorkflowProcess}.
 * @author German Ulrich
 *
 */
public interface Workable {

    /**
     * Retorna la key del "item"
     * @return
     */
    String getKey();

    /**
     * Este m&eacute;todo es invocado cada vez que se crea un Step dentro del {@link WorkflowProcess}.
     * @param agent agente que ejecuta la acci&oacute;
     * @param dg grupo para el cual se agrega el step.
     * @param name nombre del step
     * @param description descripci&oacute;n del step.
     * @param date fecha
     */
    void addStep(Agent agent, DistributionGroup dg, String name, String description, Date date);
    
    /**
     * Este m&eacute;todo es invocado cuando el proceso de workflow a llegado a su fin.
     */
    void completed();

}
