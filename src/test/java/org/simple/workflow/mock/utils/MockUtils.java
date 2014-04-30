package org.simple.workflow.mock.utils;

import java.util.ArrayList;
import java.util.List;

import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.simple.workflow.entity.Node;
import org.simple.workflow.entity.Transition;
import org.simple.workflow.entity.Workable;
import org.simple.workflow.entity.Workflow;
import org.simple.workflow.entity.WorkflowProcess;
import org.simple.workflow.impl.NodeImpl;
import org.simple.workflow.impl.TransitionImpl;
import org.simple.workflow.impl.WorkflowImpl;
import org.simple.workflow.impl.WorkflowProcessImpl;
import org.simple.workflow.services.WorkflowDatasource;

public class MockUtils {

    public static WorkflowDatasource createDatasource() {

        List<Node> nodes = new ArrayList<Node>();
        nodes.add(new NodeImpl("OPEN", "Abierto", false));
        nodes.add(new NodeImpl("DISPATCHED", "Despachado", false));
        nodes.add(new NodeImpl("IN_PROGRESS", "En progreso", false));
        nodes.add(new NodeImpl("WAIT_CLOSED", "Espera de cierre", true));
        nodes.add(new NodeImpl("CLOSED", "Cerrado", false));

        List<Transition> transitions = new ArrayList<Transition>();
        transitions.add(createTransition("DISPATCH", "Despachar", "OPEN", "DISPATCHED", "IN_PROGRESS"));
        transitions.add(createTransition("COMPLETE_TREATMENT", "Finalizar tratamiento", "DISPATCH", "WAIT_CLOSED", null));
        transitions.add(createTransition("COMPLETE_TREATMENT", "Finalizar tratamiento", "IN_PROGRESS", "WAIT_CLOSED", null));
        transitions.add(createTransition("CLOSE", "Cerrar", "WAIT_CLOSED", "CLOSED", null));
        transitions.add(createTransition("DIRECT_CLOSE", "Cerrar en forma directa", "OPEN", "CLOSED", null, "SUPERVISOR"));

        Workflow workflow = new WorkflowImpl(nodes, transitions, "1");

        WorkflowDatasource datasource = Mockito.mock(WorkflowDatasource.class);
        Mockito.when(datasource.resolveWorkflow(Mockito.anyString())).thenReturn(workflow);
        Mockito.when(datasource.getByVersion(Mockito.anyString())).thenReturn(workflow);
        Mockito.when(datasource.newInstance(Mockito.anyString(), Mockito.anyString())).thenAnswer(
            new Answer<WorkflowProcess>() {
                @Override
                public WorkflowProcess answer(InvocationOnMock invocation) throws Throwable {
                    return new WorkflowProcessImpl((String) invocation.getArguments()[0],
                        (String) invocation.getArguments()[1]);
                }
            });

        return datasource;
    }

    private static Transition createTransition(String name, String description, String from, String to, String forkTo,
        String... permissions) {
        List<String> requiredPermissionList = new ArrayList<String>();
        if (permissions != null && permissions.length > 0) {
            for (String permission : permissions) {
                requiredPermissionList.add(permission);
            }
        }
        return new TransitionImpl(name, description, from, to, forkTo, forkTo != null, requiredPermissionList);
    }

    public static Workable createWorkable(String key) {
        Workable workable = Mockito.mock(Workable.class);
        Mockito.when(workable.getKey()).thenReturn(key);
        return workable;
    }

}
