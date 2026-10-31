package com.example.smartpad;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Toast;

public class AssignTaskActivity extends Activity {

    private DatabaseHelper db;
    private EditText edtLocation, edtDescription, edtStaff;
    private Spinner spTaskType, spPriority;
    private LinearLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new DatabaseHelper(this);
        container = ScreenHelper.createScreen(
                this, "Assign Housekeeping Task",
                "Create a refill or maintenance task");

        setContentView(container);
        ScreenHelper.addButton(this, container, "Back", this::finish);

        edtLocation = ScreenHelper.addInput(
                this, container, "Machine location");
        edtDescription = ScreenHelper.addInput(
                this, container, "Task description");
        edtStaff = ScreenHelper.addInput(
                this, container, "Housekeeping username");

        spTaskType = ScreenHelper.addSpinner(this, container,
                new String[]{"Refill Stock", "Machine Maintenance"});

        spPriority = ScreenHelper.addSpinner(this, container,
                new String[]{"Normal", "High", "Urgent"});

        ScreenHelper.addButton(this, container, "Assign Task", () -> {
            String location = edtLocation.getText().toString().trim();
            String description = edtDescription.getText().toString().trim();
            String staff = edtStaff.getText().toString().trim();

            if (location.isEmpty() || description.isEmpty() || staff.isEmpty()) {
                ScreenHelper.toast(this, "Fill in all fields");
                return;
            }

            boolean saved = db.addTask(
                    location, description,
                    spTaskType.getSelectedItem().toString(),
                    spPriority.getSelectedItem().toString(), staff);

            ScreenHelper.toast(this, saved
                    ? "Task assigned successfully"
                    : "Could not assign task. Check the staff username.");
        });
    }
}
