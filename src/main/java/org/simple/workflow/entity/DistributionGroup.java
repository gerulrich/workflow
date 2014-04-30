package org.simple.workflow.entity;

/**
 * Representa un grupo de agentes.
 * Cada grupo de agentes comparte el estado de un mismo {@link WorkflowProcess} para el mismo {@link Workable}.
 * @author German Ulrich
 *
 */
public interface DistributionGroup {

    /**
     * Retorna el identificador del grupo.
     * @return
     */
    String getOID();

    /**
     * Retorna un nombre descriptivo del grupo.
     * @return
     */
    String getName();
}
