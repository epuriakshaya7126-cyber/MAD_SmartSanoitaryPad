package com.example.smartpad;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class ManageTasksActivity extends Activity {

    private DatabaseHelper db;
    private LinearLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new DatabaseHelper(this);
        container = ScreenHelper.createScreen(
                this, "Manage Tasks",
                "Track housekeeping refill and maintenance tasks");

        setContentView(container);
        ScreenHelper.addButton(this, container, "← Back", this::finish);
        showTasks();
    }

    private void showTasks() {
        ArrayList<String> tasks = db.getTasks(null);

        if (tasks.isEmpty()) {
            ScreenHelper.addText(this, container, "No tasks available.");
            return;
        }

        for (String task : tasks) {
            ScreenHelper.addCard(this, container, task);

            int taskId = getTaskId(task);

            if (taskId > 0) {
                ScreenHelper.addButton(this, container,
                        "Update Task Status", () -> chooseStatus(taskId));
            }
        }
    }

    private int getTaskId(String task) {
        try {
            String firstLine = task.split("\n")[0];
            return Integer.parseInt(firstLine.replace("Task #", "").trim());
        } catch (Exception e) {
            return -1;
        }
    }

    private void chooseStatus(int taskId) {
        String[] statuses = {
                "Assigned", "In Progress", "Completed",
                "Needs Technical Support"
        };

        new AlertDialog.Builder(this)
                .setTitle("Update task")
                .setItems(statuses, (dialog, which) -> {
                    boolean updated = db.updateTaskStatus(
                            taskId, statuses[which]);

                    Toast.makeText(this,
                            updated ? "Task updated" : "Update failed",
                            Toast.LENGTH_SHORT).show();

                    if (updated) refreshTasks();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void refreshTasks() {
        container.removeAllViews();
        ScreenHelper.addText(this, container,
                "Track housekeeping refill and maintenance tasks");
        ScreenHelper.addButton(this, container, "← Back", this::finish);
        showTasks();
    }
}
