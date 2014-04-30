package org.simple.workflow.entity;

import java.util.List;

public interface Workflow {

    /**
     * Devuelve el nodo inicial del workflow.
     * @return 
     */
    Node getNode();

    /**
     * Devuelve un nodo a partir de su nombre
     * @param name
     * @return
     */
    Node getByName(String name);

    /**
     * Obtiene una transicion
     * @param name nombre del nodo
     * @param transicion nombre de la transicion
     * @param agent
     * @return
     */
    Transition getTransition(String nodeName, String transicionName);

    /**
     * Obtiene una lista con todas las transiciones de un nodo.
     * @param nodeName nombre del nodo.
     * @return
     */
    List<Transition> getTransitions(String nodeName);

    /**
     * Devuelve la version del workflow.
     * @return
     */
    String getVersion();


}
