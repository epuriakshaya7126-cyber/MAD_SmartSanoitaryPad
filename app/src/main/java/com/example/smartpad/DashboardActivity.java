package com.example.smartpad;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

public class DashboardActivity extends Activity {

    private String role, username;
    private LinearLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        role = getIntent().getStringExtra("USER_ROLE");
        username = getIntent().getStringExtra("USERNAME");

        if (role == null) role = "Student";
        if (username == null) username = "student";

        container = ScreenHelper.createScreen(
                this, "SmartPad ♡",
                "Welcome, " + username + "\n" + role + " Dashboard");

        ScreenHelper.setScrollableContent(this, container);

        if (role.equalsIgnoreCase("Student")) {
            addPage("View Machines", MachineListActivity.class);
            addPage("Report a Problem", ReportIssueActivity.class);
            addPage("My Reports", MyReportsActivity.class);
            addPage("Announcements", AnnouncementsActivity.class);

        } else if (role.equalsIgnoreCase("Admin")) {
            addPage("View Machines", MachineListActivity.class);
            addPage("Update Machines", UpdateMachineActivity.class);
            addPage("Manage Reports", ManageReportsActivity.class);
            addPage("Assign Task", AssignTaskActivity.class);
            addPage("Manage Tasks", ManageTasksActivity.class);
            addPage("Post Announcement", PostAnnouncementActivity.class);
            addPage("View Announcements", AnnouncementsActivity.class);

        } else if (role.equalsIgnoreCase("Housekeeping")) {
            addPage("View Machines", MachineListActivity.class);
            addPage("My Assigned Tasks", MyTasksActivity.class);
            addPage("Announcements", AnnouncementsActivity.class);
        }

        ScreenHelper.addButton(this, container, "Logout", () -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void addPage(String label, Class<?> destination) {
        ScreenHelper.addButton(this, container, label, () -> {
            Intent intent = new Intent(this, destination);
            intent.putExtra("USER_ROLE", role);
            intent.putExtra("USERNAME", username);
            startActivity(intent);
        });
    }
}
