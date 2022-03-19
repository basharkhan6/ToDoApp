package com.proit.todo.view.task;

import com.proit.todo.model.Task;
import com.proit.todo.service.TaskService;
import com.proit.todo.view.MainView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.springframework.data.domain.Pageable;

@Route(value = "tasks", layout = MainView.class)
public class ListTaskView extends VerticalLayout {

    private final TaskService taskService;
    Grid<Task> grid;
    TextField filter;

    public ListTaskView(TaskService taskService) {
        this.taskService = taskService;
        this.grid = new Grid<>();
        this.filter = new TextField();

        grid.setClassName("w-1/2");
        grid.addColumn(Task::getId).setHeader("Id");
        grid.addColumn(Task::getName).setHeader("Name");
        grid.addColumn(Task::getDate).setHeader("Date");

        grid.addComponentColumn( task -> {
            Button editBtn =  new Button("Edit");
            editBtn.addClickListener(e-> UI.getCurrent()
                    .navigate(EditTaskView.class, task.getId().toString())
            );
            return editBtn;
        });

        filter.setPlaceholder("Filter by task name");
        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(e -> listAllTask(e.getValue()));

        add(filter, grid);
        listAllTask();
    }


    private void listAllTask() {
        grid.setItems(taskService.findAll(Pageable.unpaged()).getContent());
    }

    private void listAllTask(String filterTxt) {
        grid.setItems(taskService.findAllByName(filterTxt, Pageable.unpaged()).getContent());
    }


}
