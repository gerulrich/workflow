package org.simple.workflow.entity;

import java.util.List;

/**
 * Una transici&oacute;n representa un camino de un {@link Node} a otro.
 * @author German Ulrich
 *
 */
public interface Transition {

    /**
     * Retorna el nombre de la transici&oacute;n.
     * El nombre la transici&oacute;n debe ser &uacute;nico para todas
     * las transiciones con el mismo origen.
     * @return
     */
    String getName();

    /**
     * Retorna una descripci&oacute;n para la transici&oacute;n.
     * @return
     */
    String getDescription();

    /**
     * Retorna el nombre del nodo de origen.
     * @return
     */
    String getFrom();

    /**
     * Retorna el nombre del nodo destino.
     * @return
     */
    String getTo();

    /**
     * Retorna el nombre del nodo destino para una transici&oacute;n
     * de tipo fork.
     * @return
     */
    String getForkTo();

    /**
     * Indica si es una transici&oacute;n de tipo fork.
     * @return
     */
    boolean isForkTransition();

    /**
     * Retorna una lista con los permisos requeridos
     * para poder ejecutar esta transici&oacute;n.
     * @return
     */
    List<String> getRequiredPermissions();

}
