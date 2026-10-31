
package com.example.smartpad;

import android.app.Activity;
import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class UpdateMachineActivity extends Activity {

    private DatabaseHelper db;
    private EditText edtMachineId;
    private Spinner spStock, spOperational;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new DatabaseHelper(this);

        LinearLayout container = ScreenHelper.createScreen(
                this, "Update Machine",
                "Update a machine's stock and working condition");

        ScreenHelper.setScrollableContent(this, container);

        ScreenHelper.addButton(this, container, "← Back", this::finish);

        ScreenHelper.addButton(this, container,
                "View Current Machines", this::showMachines);

        edtMachineId = ScreenHelper.addInput(
                this, container, "Machine ID (example: 1)");

        ScreenHelper.addText(this, container, "Stock level");

        spStock = ScreenHelper.addSpinner(this, container,
                new String[]{"Available", "Low Stock", "Out of Stock"});

        ScreenHelper.addText(this, container, "Machine condition");

        spOperational = ScreenHelper.addSpinner(this, container,
                new String[]{"Working", "Not Working", "Unknown"});

        ScreenHelper.addButton(this, container, "Save Changes", this::saveChanges);
    }

    private void showMachines() {
        ArrayList<String> machines = db.getMachines();

        StringBuilder message = new StringBuilder();

        for (String machine : machines) {
            message.append(machine).append("\n\n");
        }

        if (message.length() == 0) {
            message.append("No machines found.");
        }

        new AlertDialog.Builder(this)
                .setTitle("Current Machines")
                .setMessage(message.toString())
                .setPositiveButton("Close", null)
                .show();
    }

    private void saveChanges() {
        String idText = edtMachineId.getText().toString().trim();

        if (idText.isEmpty()) {
            ScreenHelper.toast(this, "Enter a machine ID");
            return;
        }

        int machineId;

        try {
            machineId = Integer.parseInt(idText);
        } catch (NumberFormatException e) {
            ScreenHelper.toast(this, "Machine ID must be a number");
            return;
        }

        boolean updated = db.updateMachine(
                machineId,
                spStock.getSelectedItem().toString(),
                spOperational.getSelectedItem().toString());

        if (updated) {
            Toast.makeText(this, "Machine updated successfully",
                    Toast.LENGTH_SHORT).show();
            edtMachineId.setText("");
        } else {
            ScreenHelper.toast(this,
                    "Machine not found. Check the ID and try again.");
        }
    }
}
