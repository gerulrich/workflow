package org.simple.workflow.entity;

import java.util.Date;
import java.util.List;

/**
 * Guarda toda la informaci&oacute;n de estado del workflow
 * para un "item".
 * @author German Ulrich
 *
 */
public interface WorkflowProcess {

    /**
     * Retorna la "key" del workflow process.
     * @return
     */
    String getKey();

    /**
     * Retorna la version de workflow utilizada para este workflow process
     * @return
     */
    String getVersion();

    /**
     * Obtiene el step actual para un {@link DistributionGroup}.
     * @param dg
     * @return
     */
    Step getCurrentStep(DistributionGroup dg);

    /**
     * Indica si un {@link DistributionGroup} participa del workflow process.
     * @param dg
     * @return
     */
    boolean isValidDG(DistributionGroup dg);

    /**
     * Agrega un step como resultado de la ejecuci&oacute;n de una transici&oacute;n.
     * @param name nombre del step
     * @param description descripci&oacute;n del step
     * @param agent agente que realiza la operaci&oacute;n
     * @param date fecha
     */
    void addStep(String name, String description, Agent agent, Date date);

    /**
     * Agrega un step como resultado de la ejecuci&oacute;n de una transici&oacute;n.
     * @param name nombre del step
     * @param description descripci&oacute;n del step
     * @param agent agente que realiza la operaci&oacute;n
     * @param dg grupo para el cual se agrega el step.
     * @param date fecha
     */
    void addStep(String name, String description, Agent agent, DistributionGroup dg, Date date);

    /**
     * Retorna una lista con todos los {@link DistributionGroup} involucrados en el workflow process.
     * @return
     */
    List<DistributionGroup> getDistributionGroups();
    
    /**
     * Retorna todos los step de un {@link DistributionGroup}.
     * @param dg
     * @return
     */
    List<Step> getSteps(DistributionGroup dg);
}
