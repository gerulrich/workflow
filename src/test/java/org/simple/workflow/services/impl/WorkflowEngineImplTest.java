package org.simple.workflow.services.impl;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;
import org.simple.workflow.entity.Agent;
import org.simple.workflow.entity.Step;
import org.simple.workflow.entity.Transition;
import org.simple.workflow.entity.Workable;
import org.simple.workflow.entity.WorkflowProcess;
import org.simple.workflow.impl.WorkflowProcessImpl;
import org.simple.workflow.mock.utils.AgentTestImpl;
import org.simple.workflow.mock.utils.MockUtils;
import org.simple.workflow.services.WorkflowDatasource;
import org.simple.workflow.services.WorkflowOperationException;

public class WorkflowEngineImplTest {

    private static final String WORKFLOW_VERSION = "1";
    private static int WORKFLOW_KEY = 0;

    private WorkflowEngineImpl engine = new WorkflowEngineImpl();
    private WorkflowDatasource datasource;
    private Agent agentA = new AgentTestImpl("userA", "User A", "dg0A", "DG A");
    private Agent agentB = new AgentTestImpl("userB", "User B", "dg0B", "DG B");
    private Agent agentSUP = new AgentTestImpl("userSUP", "User SUP", "dg0A", "DG A", "SUPERVISOR");
    private WorkflowProcess process;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() {
        this.datasource = MockUtils.createDatasource();
        this.engine.setDatasource(this.datasource);
        this.process = new WorkflowProcessImpl(createWorkflowKey(), WORKFLOW_VERSION);
        Mockito.when(this.datasource.getByKey(Mockito.eq(this.process.getKey()))).thenReturn(this.process);
    }

    @Test
    public void test_startProcess_shouldReturnProcessWithStartStep() throws WorkflowOperationException {
        Workable workable = MockUtils.createWorkable("workable#01");
        WorkflowProcess processFromEngine = this.engine.start(workable, this.agentA);

        Assert.assertEquals("workable#01", processFromEngine.getKey());
        Assert.assertEquals(WORKFLOW_VERSION, processFromEngine.getVersion());

        Step currentStep = processFromEngine.getCurrentStep(this.agentA.getDG());
        Assert.assertNotNull(currentStep);
        Assert.assertEquals("OPEN", currentStep.getName());
        Assert.assertEquals("Abierto", currentStep.getDescription());
        Assert.assertEquals(this.agentA.getName(), currentStep.getAgentName());
        Assert.assertEquals(this.agentA.getOID(), currentStep.getAgentOID());

        verify(workable, times(1)).getKey();
        verify(workable, times(1)).addStep(eq(this.agentA), eq(this.agentA.getDG()), eq("OPEN"), eq("Abierto"),
            any(Date.class));
        verifyNoMoreInteractions(workable);

        verify(this.datasource, times(1)).resolveWorkflow(null);
        verify(this.datasource, times(1)).newInstance("workable#01", WORKFLOW_VERSION);
        verify(this.datasource, times(1)).save(processFromEngine);
        verifyNoMoreInteractions(this.datasource);
    }

    @Test
    public void test_getByKey_ValidAgent_shouldReturnWorkflowProcess() throws WorkflowOperationException {
        this.process.addStep("OPEN", "OPEN", this.agentA, new Date());

        WorkflowProcess processFromEngine = this.engine.getByKey(this.process.getKey(), this.agentA);
        Assert.assertNotNull(processFromEngine);

        verify(this.datasource, times(1)).getByKey(this.process.getKey());
        verifyNoMoreInteractions(this.datasource);
    }

    @Test
    public void test_getByKey_InvalidAgent_shouldThrowException() throws WorkflowOperationException {
        this.thrown.expect(WorkflowOperationException.class);
        this.thrown.expectMessage(String.format("Invalid workflow process: key[%s], DG[DG B]", this.process.getKey()));
        this.process.addStep("OPEN", "OPEN", this.agentA, new Date());

        WorkflowProcess processFromEngine = this.engine.getByKey(this.process.getKey(), this.agentB);
        Assert.assertNull(processFromEngine);
    }

    @Test
    public void test_getByKey_InvalidWorkflow_shouldThrowException() throws WorkflowOperationException {
        this.thrown.expect(WorkflowOperationException.class);
        this.thrown.expectMessage("Invalid workflow process: key[workable#01], DG[DG A]");

        WorkflowProcess processFromEngine = this.engine.getByKey("workable#01", this.agentA);
        Assert.assertNull(processFromEngine);
    }

    @Test
    public void test_exec_ValidTransition_WithoutPermission_shouldThrowException() throws WorkflowOperationException {
        this.thrown.expect(WorkflowOperationException.class);
        String message = String.format(
            "The agent 'User A' has not permission for 'DIRECT_CLOSE' transition. Process[key=%s,DG=DG A]",
            this.process.getKey());
        this.thrown.expectMessage(message);

        this.process.addStep("OPEN", "OPEN", this.agentA, new Date());

        Workable workable = MockUtils.createWorkable(this.process.getKey());
        this.engine.exec(workable, "DIRECT_CLOSE", this.agentA);
    }

    @Test
    public void test_exec_ValidTransition_WithPermission_shouldExecuteStep() throws WorkflowOperationException {
        this.process.addStep("OPEN", "OPEN", this.agentA, new Date());

        Workable workable = MockUtils.createWorkable(this.process.getKey());
        this.engine.exec(workable, "DIRECT_CLOSE", this.agentSUP);

        Step currentStep = this.process.getCurrentStep(this.agentSUP.getDG());
        Assert.assertNotNull(currentStep);
        Assert.assertEquals("CLOSED", currentStep.getName());
        Assert.assertEquals(this.agentSUP.getName(), currentStep.getAgentName());
        Assert.assertEquals(this.agentSUP.getOID(), currentStep.getAgentOID());

        verify(workable, times(1)).getKey();
        verify(workable, times(1)).addStep(eq(this.agentSUP), eq(this.agentSUP.getDG()), eq("CLOSED"), anyString(),
            any(Date.class));
        verifyNoMoreInteractions(workable);

        verify(this.datasource, times(1)).getByKey(this.process.getKey());
        verify(this.datasource, times(1)).resolveWorkflow(WORKFLOW_VERSION);
        verify(this.datasource, times(1)).save(this.process);
        verifyNoMoreInteractions(this.datasource);

    }


    @Test
    public void test_fork_ValidTransition_shouldExecuteForkStep() throws WorkflowOperationException {
        this.process.addStep("OPEN", "OPEN", this.agentA, new Date());

        Workable workable = MockUtils.createWorkable(this.process.getKey());
        this.engine.fork(workable, "DISPATCH", this.agentA, this.agentB.getDG());

        Step currentStepA = this.process.getCurrentStep(this.agentA.getDG());
        Assert.assertNotNull(currentStepA);
        Assert.assertEquals("DISPATCHED", currentStepA.getName());
        Assert.assertEquals(this.agentA.getName(), currentStepA.getAgentName());
        Assert.assertEquals(this.agentA.getOID(), currentStepA.getAgentOID());

        Step currentStepB = this.process.getCurrentStep(this.agentB.getDG());
        Assert.assertNotNull(currentStepB);
        Assert.assertEquals("IN_PROGRESS", currentStepB.getName());
        Assert.assertEquals(this.agentA.getName(), currentStepB.getAgentName());
        Assert.assertEquals(this.agentA.getOID(), currentStepB.getAgentOID());        

        verify(workable, times(1)).getKey();
        verify(workable, times(1)).addStep(eq(this.agentA), eq(this.agentA.getDG()), eq("DISPATCHED"), anyString(),
            any(Date.class));
        verify(workable, times(1)).addStep(eq(this.agentA), eq(this.agentB.getDG()), eq("IN_PROGRESS"), anyString(),
            any(Date.class));
        verifyNoMoreInteractions(workable);

        verify(this.datasource, times(1)).getByKey(this.process.getKey());
        verify(this.datasource, times(1)).resolveWorkflow(WORKFLOW_VERSION);
        verify(this.datasource, times(1)).save(this.process);
        verifyNoMoreInteractions(this.datasource);

    }

    @Test
    public void test_exec_InvalidTransition_shouldThrowException() throws WorkflowOperationException {
        this.thrown.expect(WorkflowOperationException.class);
        this.thrown.expectMessage("Invalid transition 'INVALID_TRANS' for step 'OPEN'");
        this.process.addStep("OPEN", "OPEN", this.agentA, new Date());

        Workable workable = MockUtils.createWorkable(this.process.getKey());
        Mockito.when(this.datasource.getByKey(Mockito.anyString())).thenReturn(this.process);
        this.engine.exec(workable, "INVALID_TRANS", this.agentA);

    }

    @Test
    public void test_fork_InvalidTransition_shouldThrowException() throws WorkflowOperationException {
        this.thrown.expect(WorkflowOperationException.class);
        this.thrown.expectMessage("Invalid transition 'INVALID_TRANS' for step 'OPEN'");
        this.process.addStep("OPEN", "OPEN", this.agentA, new Date());

        Workable workable = MockUtils.createWorkable(this.process.getKey());
        this.engine.fork(workable, "INVALID_TRANS", this.agentA, this.agentB.getDG());
    }

    @Test
    public void test_getTransitions_NormalNode_SomePermissions_shouldReturnValidTransitions()
        throws WorkflowOperationException {
        this.process.addStep("OPEN", "OPEN", this.agentA, new Date());

        Workable workable = MockUtils.createWorkable(this.process.getKey());
        List<Transition> transitions = this.engine.getTransitions(workable, this.agentA);
        Assert.assertEquals(1, transitions.size());
        Assert.assertEquals("DISPATCH", transitions.get(0).getName());

        verify(workable, times(1)).getKey();
        verifyNoMoreInteractions(workable);

        verify(this.datasource, times(1)).getByKey(this.process.getKey());
        verify(this.datasource, times(1)).resolveWorkflow(WORKFLOW_VERSION);
        verifyNoMoreInteractions(this.datasource);
    }

    @Test
    public void test_getTransitions_NormalNode_AllPermissions_shouldReturnValidTransitions()
        throws WorkflowOperationException {
        this.process.addStep("OPEN", "OPEN", this.agentA, new Date());

        Workable workable = MockUtils.createWorkable(this.process.getKey());
        List<Transition> transitions = this.engine.getTransitions(workable, this.agentSUP);
        Assert.assertEquals(2, transitions.size());
        Assert.assertEquals("DISPATCH", transitions.get(0).getName());
        Assert.assertEquals("DIRECT_CLOSE", transitions.get(1).getName());
    }

    @Test
    public void test_getTransitions_InJoinNode_standby_shouldReturnEmptyList() throws WorkflowOperationException {
        this.process.addStep("WAIT_CLOSED", "WAIT_CLOSED", this.agentA, new Date());
        this.process.addStep("IN_PROGRESS", "IN_PROGRESS", this.agentB, new Date());

        Workable workable = MockUtils.createWorkable(this.process.getKey());
        List<Transition> transitions = this.engine.getTransitions(workable, this.agentA);
        Assert.assertTrue(transitions.isEmpty());

        verify(workable, times(1)).getKey();
        verifyNoMoreInteractions(workable);

        verify(this.datasource, times(1)).getByKey(this.process.getKey());
        verify(this.datasource, times(1)).resolveWorkflow(WORKFLOW_VERSION);
        verifyNoMoreInteractions(this.datasource);

    }

    @Test
    public void test_getTransitions_InJoinNode_canProceed_shouldValidTransition() throws WorkflowOperationException {
        this.process.addStep("WAIT_CLOSED", "WAIT_CLOSED", this.agentA, new Date());

        Workable workable = MockUtils.createWorkable(this.process.getKey());
        List<Transition> transitions = this.engine.getTransitions(workable, this.agentA);
        Assert.assertEquals(1, transitions.size());
        Assert.assertEquals("CLOSE", transitions.get(0).getName());

        verify(workable, times(1)).getKey();
        verifyNoMoreInteractions(workable);

        verify(this.datasource, times(1)).getByKey(this.process.getKey());
        verify(this.datasource, times(1)).resolveWorkflow(WORKFLOW_VERSION);
        verifyNoMoreInteractions(this.datasource);

    }

    private static String createWorkflowKey() {
        WorkflowEngineImplTest.WORKFLOW_KEY++;
        return "workable" + WorkflowEngineImplTest.WORKFLOW_KEY;
    }

}
