package com.example.smartpad;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class MyReportsActivity extends Activity {

    private DatabaseHelper db;
    private LinearLayout container;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new DatabaseHelper(this);
        username = getIntent().getStringExtra("USERNAME");

        if (username == null) {
            username = "";
        }

        container = ScreenHelper.createScreen(
                this, "My Reports",
                "Check the progress of your submitted issues");

        setContentView(container);

        ScreenHelper.addButton(this, container, "← Back", this::finish);
        showMyReports();
    }

    private void showMyReports() {
        ArrayList<String> reports = db.getReports(username);

        if (reports.isEmpty()) {
            ScreenHelper.addText(this, container,
                    "You have not submitted any reports yet.");
            return;
        }

        for (String report : reports) {
            ScreenHelper.addCard(this, container, report);
        }
    }
}
