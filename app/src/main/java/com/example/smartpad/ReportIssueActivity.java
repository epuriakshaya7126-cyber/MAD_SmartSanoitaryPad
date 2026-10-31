package com.example.smartpad;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

public class ReportIssueActivity extends Activity {

    private DatabaseHelper db;
    private EditText edtLocation, edtDetails;
    private Spinner spIssueType;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new DatabaseHelper(this);
        username = getIntent().getStringExtra("USERNAME");

        if (username == null) username = "student";

        String machineInfo = getIntent().getStringExtra("MACHINE_INFO");

        LinearLayout container = ScreenHelper.createScreen(
                this, "Report a Problem",
                "Report an issue with a sanitary pad machine");

        setContentView(container);
        ScreenHelper.addButton(this, container, "← Back", this::finish);

        edtLocation = ScreenHelper.addInput(
                this, container, "Machine location");

        if (machineInfo != null && !machineInfo.isEmpty()) {
            String location = extractLocation(machineInfo);
            edtLocation.setText(location);
        }

        ScreenHelper.addText(this, container, "Choose the problem:");

        spIssueType = ScreenHelper.addSpinner(this, container,
                new String[]{
                        "Out of Stock",
                        "Low Stock",
                        "Machine Not Working",
                        "Not Dispensing",
                        "Other"
                });

        edtDetails = ScreenHelper.addInput(
                this, container, "Additional details (optional)");

        ScreenHelper.addButton(this, container, "Submit Report", () -> {
            String location = edtLocation.getText()
                    .toString().trim();
            String issue = spIssueType.getSelectedItem().toString();
            String details = edtDetails.getText()
                    .toString().trim();

            if (location.isEmpty()) {
                ScreenHelper.toast(this, "Enter the machine location");
                return;
            }

            if (!details.isEmpty()) {
                issue += " - " + details;
            }

            boolean saved = db.addReport(username, location, issue);

            if (saved) {
                Toast.makeText(this, "Report submitted successfully",
                        Toast.LENGTH_LONG).show();
                finish();
            } else {
                ScreenHelper.toast(this, "Could not submit report");
            }
        });
    }

    private String extractLocation(String machineInfo) {
        for (String line : machineInfo.split("\n")) {
            if (line.startsWith("Location:")) {
                return line.substring("Location:".length()).trim();
            }
        }
        return "";
    }
}
