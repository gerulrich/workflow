package org.simple.workflow.services;

import org.simple.workflow.entity.Workflow;
import org.simple.workflow.entity.WorkflowProcess;

/**
 * Datasource para las operaciones b&aacute;sicas del {@link WorkflowEngine}.
 * Se puede usar cualquier mecanismo de persistencia de datos que se desee.
 * @author German Ulrich
 *
 */
public interface WorkflowDatasource {

    /**
     * Obtiene un workflow por su versi&oacute;n.
     * Si no se especifica la versi&oacute;n debe retornar el
     * workflow por defecto.
     * @param version
     * @return
     */
    Workflow resolveWorkflow(String version);

    /**
     * Obtiene un workflow por su versi&oacute;n.
     * @param version
     * @return
     */
    Workflow getByVersion(String version);

    /**
     * Retorna una nueva instancia de un {@link WorkflowProcess}.
     * @param key
     * @param version
     * @return
     */
    WorkflowProcess newInstance(String key, String version);

    /**
     * Retorna un {@link WorkflowProcess} ya existente por su "key".
     * @param key
     * @return
     */
    WorkflowProcess getByKey(String key);

    /**
     * Guarda un {@link WorkflowProcess}.
     * @param entity
     */
    void save(WorkflowProcess entity);

}
