package org.simple.workflow.entity;

import java.util.Date;

/**
 * Representa cada uno de los estados de un {@link WorkflowProcess}.
 * @author German Ulrich
 *
 */
public interface Step {

    /**
     * Retorna el nombre del step.
     * El nombre del Step se corresponde al nombre del {@link Node} del workflow.
     * @return
     */
    String getName();

    /**
     * Retorna una descripci&oacute;n para el Step.
     * @return
     */
    String getDescription();

    /**
     * Retorna la fecha en que se cre&oacute; el Step.
     * @return
     */
    Date getDate();

    /**
     * Retorna el nombre del agente que cre&oacute; el Step.
     * @return
     */
    String getAgentName();
}
