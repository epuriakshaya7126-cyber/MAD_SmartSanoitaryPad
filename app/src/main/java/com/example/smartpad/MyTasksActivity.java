
package com.example.smartpad;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class MyTasksActivity extends Activity {

    private DatabaseHelper db;
    private LinearLayout container;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new DatabaseHelper(this);
        username = getIntent().getStringExtra("USERNAME");

        if (username == null) username = "staff";

        container = ScreenHelper.createScreen(
                this, "My Assigned Tasks",
                "View and update your housekeeping tasks");

        setContentView(container);
        ScreenHelper.addButton(this, container, "← Back", this::finish);

        showTasks();
    }

    private void showTasks() {
        ArrayList<String> tasks = db.getTasks(username);

        if (tasks.isEmpty()) {
            ScreenHelper.addText(this, container,
                    "No tasks assigned to you yet.");
            return;
        }

        for (String task : tasks) {
            ScreenHelper.addCard(this, container, task);

            int taskId = getTaskId(task);

            if (taskId > 0) {
                ScreenHelper.addButton(this, container,
                        "Update Task", () -> updateTask(taskId));
            }
        }
    }

    private int getTaskId(String task) {
        try {
            return Integer.parseInt(
                    task.split("\n")[0].replace("Task #", "").trim());
        } catch (Exception e) {
            return -1;
        }
    }

    private void updateTask(int taskId) {
        String[] statuses = {
                "In Progress", "Completed", "Needs Technical Support"
        };

        new AlertDialog.Builder(this)
                .setTitle("Update task status")
                .setItems(statuses, (dialog, which) -> {
                    boolean updated = db.updateTaskStatus(
                            taskId, statuses[which]);

                    Toast.makeText(this,
                            updated ? "Task status updated"
                                    : "Could not update task",
                            Toast.LENGTH_SHORT).show();

                    if (updated) refreshTasks();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void refreshTasks() {
        container.removeAllViews();
        ScreenHelper.addText(this, container,
                "View and update your housekeeping tasks");
        ScreenHelper.addButton(this, container, "← Back", this::finish);
        showTasks();
    }
}
