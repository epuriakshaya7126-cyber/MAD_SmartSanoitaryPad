
package com.example.smartpad;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

    private static final String TAG = "SmartPadLogin";

    private EditText edtUsername, edtPassword;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            db = new DatabaseHelper(this);

            edtUsername = findViewById(R.id.edtUsername);
            edtPassword = findViewById(R.id.edtPassword);
            Button btnLogin = findViewById(R.id.btnLogin);

            if (edtUsername == null || edtPassword == null || btnLogin == null) {
                Toast.makeText(this,
                        "Login XML IDs do not match the Java code",
                        Toast.LENGTH_LONG).show();
                return;
            }

            btnLogin.setOnClickListener(v -> loginUser());

        } catch (Exception e) {
            Log.e(TAG, "Login screen initialization failed", e);
            showError("Could not initialize the login screen: " + e.getMessage());
        }
    }

    private void loginUser() {
        String username = edtUsername.getText().toString().trim();
        String password = edtPassword.getText().toString();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this,
                    "Enter username and password",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        try (Cursor cursor = db.login(username, password)) {
            if (cursor != null && cursor.moveToFirst()) {

                String name = cursor.getString(1);
                String loggedInUsername = cursor.getString(2);
                String role = cursor.getString(3);

                Intent intent = new Intent(
                        MainActivity.this, DashboardActivity.class);

                intent.putExtra("USERNAME", loggedInUsername);
                intent.putExtra("USER_NAME", name);
                intent.putExtra("USER_ROLE", role);

                startActivity(intent);

            } else {
                Toast.makeText(this,
                        "Incorrect username or password",
                        Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            Log.e(TAG, "Login failed", e);
            showError("Login failed: " + e.getMessage());
        }
    }

    private void showError(String message) {
        new AlertDialog.Builder(this)
                .setTitle("SmartPad Error")
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }
}
