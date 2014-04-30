package org.simple.workflow.entity;

import java.util.List;

/**
 * Representa a una entidad capaz de efectuar operaciones sobre un {@link WorkflowProcess}.
 * Por ejemplo un usuario.
 * @author German Ulrich
 *
 */
public interface Agent {

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

    /**
     * Retorna la lista de permisos del agente.
     * @return
     */
    List<String> getPermissions();

    /**
     * Retorna el {@link DistributionGroup} del agente
     * @return
     */
    DistributionGroup getDG();
}
