package com.example.smartpad;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class ManageReportsActivity extends Activity {

    private DatabaseHelper db;
    private LinearLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new DatabaseHelper(this);
        container = ScreenHelper.createScreen(
                this, "Manage Reports",
                "Review and update student-reported issues");

        setContentView(container);
        ScreenHelper.addButton(this, container, "← Back", this::finish);
        showReports();
    }

    private void showReports() {
        ArrayList<String> reports = db.getReports(null);

        if (reports.isEmpty()) {
            ScreenHelper.addText(this, container,
                    "No reports have been submitted.");
            return;
        }

        for (String report : reports) {
            ScreenHelper.addCard(this, container, report);

            int reportId = getReportId(report);

            if (reportId > 0) {
                ScreenHelper.addButton(this, container,
                        "Update Report Status", () -> chooseStatus(reportId));
            }
        }
    }

    private int getReportId(String report) {
        try {
            String firstLine = report.split("\n")[0];
            return Integer.parseInt(firstLine.replace("Report #", "").trim());
        } catch (Exception e) {
            return -1;
        }
    }

    private void chooseStatus(int reportId) {
        String[] statuses = {
                "Under Review", "Assigned", "In Progress",
                "Resolved", "Rejected"
        };

        new AlertDialog.Builder(this)
                .setTitle("Choose report status")
                .setItems(statuses, (dialog, which) -> {
                    boolean updated = db.updateReportStatus(
                            reportId, statuses[which]);

                    Toast.makeText(this,
                            updated ? "Report status updated"
                                    : "Could not update report",
                            Toast.LENGTH_SHORT).show();

                    if (updated) {
                        refreshReports();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void refreshReports() {
        container.removeAllViews();
        ScreenHelper.addText(this, container,
                "Review and update student-reported issues");
        ScreenHelper.addButton(this, container, "← Back", this::finish);
        showReports();
    }
}
