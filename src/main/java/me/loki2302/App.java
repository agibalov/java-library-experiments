package me.loki2302;

import org.activiti.engine.*;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.identity.User;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import java.util.List;

public class App {
    public static void main(String[] args) {
        ProcessEngine processEngine = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration()
                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_CREATE_DROP)
                .setJdbcUrl("jdbc:h2:mem:my-own-db;DB_CLOSE_DELAY=1000")
                .setAsyncExecutorEnabled(true)
                .setAsyncExecutorActivate(false)
                .setJobExecutorActivate(false)
                .buildProcessEngine();

        IdentityService identityService = processEngine.getIdentityService();
        identityService.saveGroup(identityService.newGroup("management"));
        identityService.saveGroup(identityService.newGroup("accountancy"));

        User accountantUser = identityService.newUser("accountant");
        accountantUser.setPassword("accountant");
        identityService.saveUser(accountantUser);
        identityService.createMembership("accountant", "accountancy");

        User managerUser = identityService.newUser("manager");
        managerUser.setPassword("manager");
        identityService.saveUser(managerUser);
        identityService.createMembership("manager", "management");

        RepositoryService repositoryService = processEngine.getRepositoryService();
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("FinancialReportProcess.bpmn20.xml")
                .deploy();
        System.out.println(deployment);

        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("financialReport");
        System.out.println(processInstance);

        processEngine.getRuntimeService().addEventListener(new MyEventListener());

        TaskService taskService = processEngine.getTaskService();
        if(true) {
            List<Task> tasksAvailableToAccountant = taskService.createTaskQuery().taskCandidateUser("accountant").list();
            System.out.printf("tasksAvailableToAccountant: %d\n", tasksAvailableToAccountant.size());
            for (Task task : tasksAvailableToAccountant) {
                System.out.println(task);
            }

            Task t = tasksAvailableToAccountant.get(0);
            taskService.claim(t.getId(), "accountant");

            taskService.complete(t.getId());
        }

        System.out.printf("Process instance end time: %s\n", processEngine.getHistoryService().createHistoricProcessInstanceQuery().processInstanceId(processInstance.getProcessInstanceId()).singleResult().getEndTime());

        if(true) {
            List<Task> tasksAvailableToManager = taskService.createTaskQuery().taskCandidateUser("manager").list();
            System.out.printf("tasksAvailableToManager: %d\n", tasksAvailableToManager.size());
            for (Task task : tasksAvailableToManager) {
                System.out.println(task);
            }

            Task t = tasksAvailableToManager.get(0);
            taskService.claim(t.getId(), "manager");

            taskService.complete(t.getId());
        }

        System.out.printf("Process instance end time: %s\n", processEngine.getHistoryService().createHistoricProcessInstanceQuery().processInstanceId(processInstance.getProcessInstanceId()).singleResult().getEndTime());

    }

    public static class MyEventListener implements ActivitiEventListener {

        @Override
        public void onEvent(ActivitiEvent event) {

            System.out.printf("got event: %s [%s]\n", event.getType(), event.getClass());
        }

        @Override
        public boolean isFailOnException() {
            return false;
        }
    }
}
