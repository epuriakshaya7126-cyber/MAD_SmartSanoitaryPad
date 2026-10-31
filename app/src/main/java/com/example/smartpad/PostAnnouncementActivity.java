
package com.example.smartpad;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class PostAnnouncementActivity extends Activity {

    private DatabaseHelper db;
    private EditText edtAnnouncement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new DatabaseHelper(this);

        LinearLayout container = ScreenHelper.createScreen(
                this, "Post Announcement",
                "Share updates with students and housekeeping staff");

        setContentView(container);

        ScreenHelper.addButton(this, container, "← Back", this::finish);

        edtAnnouncement = ScreenHelper.addInput(
                this, container, "Write announcement");

        ScreenHelper.addButton(this, container, "Publish", () -> {
            String message = edtAnnouncement.getText()
                    .toString().trim();

            if (message.isEmpty()) {
                ScreenHelper.toast(this, "Enter an announcement");
                return;
            }

            boolean saved = db.addAnnouncement(message);

            if (saved) {
                Toast.makeText(this, "Announcement published",
                        Toast.LENGTH_SHORT).show();
                finish();
            } else {
                ScreenHelper.toast(this, "Could not publish announcement");
            }
        });
    }
}
