package com.company.timesheets.view.task;


import com.company.timesheets.entity.Task;
import com.company.timesheets.entity.TimeEntry;
import com.company.timesheets.entity.User;
import com.company.timesheets.view.main.MainView;
import com.company.timesheets.view.timeentry.TimeEntryDetailView;
import com.vaadin.flow.router.Route;
import io.jmix.core.AccessManager;
import io.jmix.core.DataManager;
import io.jmix.core.Metadata;
import io.jmix.core.usersubstitution.CurrentUserSubstitution;
import io.jmix.flowui.DialogWindows;
import io.jmix.flowui.accesscontext.UiEntityContext;
import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "my-tasks", layout = MainView.class)
@ViewController("ts_Task.my")
@ViewDescriptor("my-task-list-view.xml")
@LookupComponent("tasksDataGrid")
@DialogMode(width = "64em")
public class MyTaskListView extends StandardListView<Task> {

    @ViewComponent
    private DataGrid<Task> tasksDataGrid;
    @Autowired
    private DataManager dataManager;
    @Autowired
    private CurrentUserSubstitution currentUserSubstitution;
    @Autowired
    private DialogWindows dialogWindows;
    @Autowired
    private Metadata metadata;
    @Autowired
    private AccessManager accessManager;

    @Subscribe("tasksDataGrid.createTimeEntry")
    public void onTasksDataGridCreateTimeEntry(final ActionPerformedEvent event) {
        Task selectedItem = tasksDataGrid.getSingleSelectedItem();
        if (selectedItem == null) {
            return;
        }

        TimeEntry timeEntry = dataManager.create(TimeEntry.class);
        timeEntry.setUser((User) currentUserSubstitution.getEffectiveUser());
        timeEntry.setTask(selectedItem);

        DialogWindow<TimeEntryDetailView> dialogWindow = dialogWindows.detail(this, TimeEntry.class)
                .withViewClass(TimeEntryDetailView.class)
                .newEntity(timeEntry)
                .build();
        dialogWindow.getView().setOwnTimeEntry(true);
        dialogWindow.open();
    }

    @Install(to = "tasksDataGrid.createTimeEntry", subject = "enabledRule")
    private boolean tasksDataGridCreateTimeEntryEnabledRule() {
        UiEntityContext entityContext = new UiEntityContext(metadata.getClass(TimeEntry.class));
        accessManager.applyRegisteredConstraints(entityContext);
        return entityContext.isCreatePermitted();
    }

}