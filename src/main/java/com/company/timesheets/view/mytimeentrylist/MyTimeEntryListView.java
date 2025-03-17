package com.company.timesheets.view.mytimeentrylist;


import com.company.timesheets.app.TimeEntrySupport;
import com.company.timesheets.entity.TimeEntry;
import com.company.timesheets.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "my-time-entries", layout = MainView.class)
@ViewController(id = "ts_TimeEntry.my")
@ViewDescriptor(path = "my-time-entry-list-view.xml")
public class MyTimeEntryListView extends StandardView {

    @ViewComponent
    private DataGrid<TimeEntry> timeEntriesDataGrid;
    @Autowired
    private TimeEntrySupport timeEntrySupport;

    @Subscribe("timeEntriesDataGrid.copy")
    public void onTimeEntriesDataGridCopy(final ActionPerformedEvent event) {
        TimeEntry selectedEntry = timeEntriesDataGrid.getSingleSelectedItem();
        if (selectedEntry == null) {
            return;
        }

        TimeEntry copiedEntry = timeEntrySupport.copy(selectedEntry);

        // todo open editor view
    }
}