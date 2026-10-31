package com.example.smartpad;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "smartpad.db";
    private static final int DB_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT, username TEXT UNIQUE, password TEXT, role TEXT)");

        db.execSQL("CREATE TABLE machines (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT, location TEXT, stock TEXT, operational TEXT)");

        db.execSQL("CREATE TABLE reports (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username TEXT, location TEXT, issue TEXT, status TEXT)");

        db.execSQL("CREATE TABLE tasks (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "location TEXT, description TEXT, type TEXT," +
                "priority TEXT, staff TEXT, status TEXT)");

        db.execSQL("CREATE TABLE announcements (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, message TEXT)");

        insertUser(db, "Demo Student", "student", "student123", "Student");
        insertUser(db, "Admin", "admin", "admin123", "Admin");
        insertUser(db, "Housekeeping Staff", "staff", "staff123", "Housekeeping");

        insertMachine(db, "Machine 01",
                "Main Academic Block - Ground Floor", "Available", "Working");
        insertMachine(db, "Machine 02",
                "Girls Hostel - Reception", "Low Stock", "Working");
        insertMachine(db, "Machine 03",
                "Library - First Floor", "Out of Stock", "Working");
        insertMachine(db, "Machine 04",
                "College Washroom - Second Floor", "Available", "Working");
    }

    private void insertUser(SQLiteDatabase db, String name,
                            String username, String password, String role) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("username", username);
        values.put("password", password);
        values.put("role", role);
        db.insert("users", null, values);
    }

    private void insertMachine(SQLiteDatabase db, String name,
                               String location, String stock, String operational) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("location", location);
        values.put("stock", stock);
        values.put("operational", operational);
        db.insert("machines", null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS machines");
        db.execSQL("DROP TABLE IF EXISTS reports");
        db.execSQL("DROP TABLE IF EXISTS tasks");
        db.execSQL("DROP TABLE IF EXISTS announcements");
        onCreate(db);
    }

    public Cursor login(String username, String password) {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery(
                "SELECT id, name, username, role FROM users " +
                        "WHERE username=? AND password=?",
                new String[]{username, password});
    }

    public ArrayList<String> getMachines() {
        ArrayList<String> list = new ArrayList<>();
        Cursor c = getReadableDatabase().rawQuery(
                "SELECT id, name, location, stock, operational FROM machines",
                null);

        try {
            while (c.moveToNext()) {
                list.add("ID: " + c.getInt(0) +
                        "\n" + c.getString(1) +
                        "\nLocation: " + c.getString(2) +
                        "\nStock: " + c.getString(3) +
                        "\nStatus: " + c.getString(4));
            }
        } finally {
            c.close();
        }
        return list;
    }

    public boolean addReport(String username, String location, String issue) {
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("location", location);
        values.put("issue", issue);
        values.put("status", "Submitted");
        return getWritableDatabase().insert("reports", null, values) != -1;
    }

    public ArrayList<String> getReports(String username) {
        ArrayList<String> list = new ArrayList<>();
        Cursor c = getReadableDatabase().rawQuery(
                "SELECT id, username, location, issue, status FROM reports " +
                        (username == null ? "" : "WHERE username=? ") + "ORDER BY id DESC",
                username == null ? null : new String[]{username});

        try {
            while (c.moveToNext()) {
                list.add("Report #" + c.getInt(0) +
                        "\nStudent: " + c.getString(1) +
                        "\nLocation: " + c.getString(2) +
                        "\nIssue: " + c.getString(3) +
                        "\nStatus: " + c.getString(4));
            }
        } finally {
            c.close();
        }
        return list;
    }

    public boolean updateReportStatus(int id, String status) {
        ContentValues values = new ContentValues();
        values.put("status", status);
        return getWritableDatabase().update("reports", values,
                "id=?", new String[]{String.valueOf(id)}) > 0;
    }

    public boolean addTask(String location, String description, String type,
                           String priority, String staff) {
        Cursor c = getReadableDatabase().rawQuery(
                "SELECT id FROM users WHERE username=? AND role='Housekeeping'",
                new String[]{staff});

        boolean staffExists = c.moveToFirst();
        c.close();

        if (!staffExists) return false;

        ContentValues values = new ContentValues();
        values.put("location", location);
        values.put("description", description);
        values.put("type", type);
        values.put("priority", priority);
        values.put("staff", staff);
        values.put("status", "Assigned");

        return getWritableDatabase().insert("tasks", null, values) != -1;
    }

    public ArrayList<String> getTasks(String staff) {
        ArrayList<String> list = new ArrayList<>();
        Cursor c = getReadableDatabase().rawQuery(
                "SELECT id, location, description, type, priority, staff, status " +
                        "FROM tasks " + (staff == null ? "" : "WHERE staff=? ") +
                        "ORDER BY id DESC",
                staff == null ? null : new String[]{staff});

        try {
            while (c.moveToNext()) {
                list.add("Task #" + c.getInt(0) +
                        "\nLocation: " + c.getString(1) +
                        "\nDescription: " + c.getString(2) +
                        "\nType: " + c.getString(3) +
                        "\nPriority: " + c.getString(4) +
                        "\nAssigned to: " + c.getString(5) +
                        "\nStatus: " + c.getString(6));
            }
        } finally {
            c.close();
        }
        return list;
    }

    public boolean updateTaskStatus(int id, String status) {
        ContentValues values = new ContentValues();
        values.put("status", status);
        return getWritableDatabase().update("tasks", values,
                "id=?", new String[]{String.valueOf(id)}) > 0;
    }

    public boolean updateMachine(int id, String stock, String operational) {
        ContentValues values = new ContentValues();
        values.put("stock", stock);
        values.put("operational", operational);
        return getWritableDatabase().update("machines", values,
                "id=?", new String[]{String.valueOf(id)}) > 0;
    }

    public boolean addAnnouncement(String message) {
        ContentValues values = new ContentValues();
        values.put("message", message);
        return getWritableDatabase().insert("announcements", null, values) != -1;
    }

    public ArrayList<String> getAnnouncements() {
        ArrayList<String> list = new ArrayList<>();
        Cursor c = getReadableDatabase().rawQuery(
                "SELECT id, message FROM announcements ORDER BY id DESC", null);

        try {
            while (c.moveToNext()) {
                list.add("Announcement #" + c.getInt(0) +
                        "\n" + c.getString(1));
            }
        } finally {
            c.close();
        }
        return list;
    }
}
