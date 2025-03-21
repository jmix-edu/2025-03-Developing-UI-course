package com.company.timesheets.view.asynctasks;


import com.company.timesheets.view.main.MainView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.asynctask.UiAsyncTasks;
import io.jmix.flowui.component.textfield.TypedTextField;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static java.lang.Thread.sleep;

@Route(value = "async-tasks-view", layout = MainView.class)
@ViewController(id = "ts_AsyncTasksView")
@ViewDescriptor(path = "async-tasks-view.xml")
public class AsyncTasksView extends StandardView {
    @Autowired
    private UiAsyncTasks uiAsyncTasks;
    @Autowired
    private Notifications notifications;
    @ViewComponent
    private TypedTextField<Object> inputField;

    @Subscribe(id = "performWithoutResultBtn", subject = "clickListener")
    public void onPerformWithoutResultBtnClick(final ClickEvent<JmixButton> event) {
        uiAsyncTasks.runnableConfigurer(() -> voidMethod("param"))
                .withResultHandler(() -> {
                    notifications.show("Void method performed!");
                })
                .runAsync();
    }

    private void voidMethod(String param) {
        try {
            sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted", e);
        }
    }

    @Subscribe(id = "performChangesBtn", subject = "clickListener")
    public void onPerformChangesBtnClick(final ClickEvent<JmixButton> event) {
        String entered = inputField.getValue();

        uiAsyncTasks.supplierConfigurer(() -> changeString(entered))
                .withResultHandler(changingResult -> {
                    notifications.show(changingResult);
                })
                .withTimeout(3, TimeUnit.SECONDS)
                .withExceptionHandler(ex -> {
                    if (ex instanceof TimeoutException) {
                        notifications.create("Timeout exception")
                                .withType(Notifications.Type.WARNING)
                                .withCloseable(false)
                                .withDuration(2000)
                                .show();
                    }
                })
                .supplyAsync();
    }

    private String changeString(String entered) {
        try {
            sleep(4000);
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted", e);
        }

        return (entered + " changed").toUpperCase();
    }
}