package com.example.smartpad;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class AnnouncementsActivity extends Activity {

    private DatabaseHelper db;
    private LinearLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseHelper(this);

        container = ScreenHelper.createScreen(
                this, "Campus Announcements",
                "Updates about sanitary pad vending machines");

        setContentView(container);

        ScreenHelper.addButton(this, container, "Back", this::finish);
        showAnnouncements();
    }

    private void showAnnouncements() {
        ArrayList<String> announcements = db.getAnnouncements();

        if (announcements.isEmpty()) {
            ScreenHelper.addText(this, container,
                    "No announcements available.");
            return;
        }

        for (String announcement : announcements) {
            ScreenHelper.addCard(this, container, announcement);
        }
    }
}
