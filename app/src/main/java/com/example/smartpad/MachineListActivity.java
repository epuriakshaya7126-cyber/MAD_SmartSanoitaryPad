package com.example.smartpad;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class MachineListActivity extends Activity {

    private DatabaseHelper db;
    private LinearLayout container;
    private String role;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new DatabaseHelper(this);
        role = getIntent().getStringExtra("USER_ROLE");
        username = getIntent().getStringExtra("USERNAME");

        LinearLayout container = ScreenHelper.createScreen(
                this, "Machine Locations", "Find sanitary pad vending machines");

        ScreenHelper.setScrollableContent(this, container);

        ScreenHelper.addButton(this, container, "← Back", this::finish);
        showMachines();
    }

    private void showMachines() {
        ArrayList<String> machines = db.getMachines();

        if (machines.isEmpty()) {
            ScreenHelper.addText(this, container,
                    "No machines have been added yet.");
            return;
        }

        for (String machine : machines) {
            ScreenHelper.addCard(this, container, machine);

            if ("Student".equalsIgnoreCase(role)) {
                final String selectedMachine = machine;

                ScreenHelper.addButton(this, container,
                        "Report a Problem", () -> {
                            Intent intent = new Intent(
                                    this, ReportIssueActivity.class);
                            intent.putExtra("USER_ROLE", role);
                            intent.putExtra("USERNAME", username);
                            intent.putExtra("MACHINE_INFO", selectedMachine);
                            startActivity(intent);
                        });
            }
        }
    }
}
