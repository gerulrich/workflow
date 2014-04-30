package org.simple.workflow.entity;

/**
 * Representa un estado en la definici&oacute;n del {@link Workflow}
 * @author German Ulrich
 *
 */
public interface Node {

    /**
     * Devuelve el nombre del nodo.
     * El nombre del nodo debe ser &uacute;nico  para cada nodo dentro del workflow
     * @return el nombre del nodo
     */
    String getName();

    /**
     * Devuelve una descripci&oacute;n para el nodo.
     * La descripci&oacute;n es un texto que describe el estado que representa el nodo.
     * @return una descripci&oacute;n para el nodo
     */
    String getDescription();


    /**
     * Indica si el nodo es de tipo Join.
     * Un nodo de tipo Join es un nodo en el cual todos los {@link DistributionGroup} deben esperar
     * a que el resto de los {@link DistributionGroup} lleguen a dicho nodo para poder avanzar.
     * Si un {@link DistributionGroup} llega a un nodo de tipo Join y otro {@link DistributionGroup}
     * todavia no alcanz&oacute; dicho estado, el primero quedar&aacute; en "standby" y no podr&aacute;
     * ejecutar ninguna de las transiciones definidas.
     * @return
     */
    boolean isJoinNode();

}
