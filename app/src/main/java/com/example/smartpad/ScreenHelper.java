package com.example.smartpad;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.content.res.ColorStateList;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ScreenHelper {

    private static final int PINK = Color.rgb(194, 24, 91);
    private static final int LIGHT_PINK = Color.rgb(255, 247, 250);
    private static final int DARK = Color.rgb(65, 35, 50);

    public static LinearLayout createScreen(
            Activity activity, String title, String subtitle) {

        ScrollView scroll = new ScrollView(activity);
        scroll.setFillViewport(true);
        scroll.setBackgroundColor(LIGHT_PINK);

        LinearLayout page = new LinearLayout(activity);
        page.setOrientation(LinearLayout.VERTICAL);
        page.setPadding(
                dp(activity, 20),
                dp(activity, 20),
                dp(activity, 20),
                dp(activity, 24)
        );

        // Attach the content layout to the ScrollView once.
        scroll.addView(page);

        TextView heading = new TextView(activity);
        heading.setText(title);
        heading.setTextSize(26);
        heading.setTextColor(PINK);
        heading.setTypeface(null, Typeface.BOLD);
        page.addView(heading);

        TextView intro = new TextView(activity);
        intro.setText(subtitle);
        intro.setTextSize(14);
        intro.setTextColor(DARK);
        intro.setPadding(0, dp(activity, 6), 0, dp(activity, 16));
        page.addView(intro);

        // Store the ScrollView so setScrollableContent() can attach it.
        page.setTag(scroll);

        return page;
    }

    public static void setScrollableContent(
            Activity activity, LinearLayout page) {

        Object tag = page.getTag();

        if (tag instanceof ScrollView) {
            ScrollView scroll = (ScrollView) tag;

            // Prevent an already-attached view from being attached again.
            if (scroll.getParent() instanceof android.view.ViewGroup) {
                ((android.view.ViewGroup) scroll.getParent())
                        .removeView(scroll);
            }

            activity.setContentView(scroll);
        } else {
            activity.setContentView(page);
        }
    }

    public static Button addButton(
            Activity activity,
            LinearLayout page,
            String label,
            Runnable action) {

        Button button = new Button(activity);
        button.setText(label);
        button.setTextColor(Color.WHITE);
        button.setAllCaps(false);
        button.setBackgroundTintList(ColorStateList.valueOf(PINK));

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        params.setMargins(
                0, dp(activity, 5), 0, dp(activity, 5)
        );

        page.addView(button, params);

        button.setOnClickListener(v -> {
            if (action != null) {
                action.run();
            }
        });

        return button;
    }

    public static TextView addText(
            Activity activity,
            LinearLayout page,
            String text) {

        TextView view = new TextView(activity);
        view.setText(text);
        view.setTextColor(DARK);
        view.setTextSize(15);
        view.setPadding(
                0, dp(activity, 8), 0, dp(activity, 8)
        );

        page.addView(view);
        return view;
    }

    public static TextView addCard(
            Activity activity,
            LinearLayout page,
            String text) {

        TextView card = new TextView(activity);
        card.setText(text);
        card.setTextColor(DARK);
        card.setTextSize(15);
        card.setPadding(
                dp(activity, 14),
                dp(activity, 12),
                dp(activity, 14),
                dp(activity, 12)
        );

        GradientDrawable background = new GradientDrawable();
        background.setColor(Color.WHITE);
        background.setCornerRadius(dp(activity, 12));
        background.setStroke(
                dp(activity, 1),
                Color.rgb(245, 210, 225)
        );

        card.setBackground(background);

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        params.setMargins(
                0, dp(activity, 5), 0, dp(activity, 7)
        );

        page.addView(card, params);
        return card;
    }

    public static EditText addInput(
            Activity activity,
            LinearLayout page,
            String hint) {

        EditText input = new EditText(activity);
        input.setHint(hint);
        input.setTextColor(DARK);
        input.setHintTextColor(Color.GRAY);
        input.setSingleLine(false);

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        params.setMargins(
                0, dp(activity, 4), 0, dp(activity, 8)
        );

        page.addView(input, params);
        return input;
    }

    public static Spinner addSpinner(
            Activity activity,
            LinearLayout page,
            String[] options) {

        Spinner spinner = new Spinner(activity);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                activity,
                android.R.layout.simple_spinner_item,
                options
        );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinner.setAdapter(adapter);
        page.addView(spinner);
        return spinner;
    }

    public static void toast(Activity activity, String message) {
        Toast.makeText(
                activity,
                message,
                Toast.LENGTH_SHORT
        ).show();
    }

    private static int dp(Activity activity, int value) {
        return (int) (
                value * activity.getResources()
                        .getDisplayMetrics().density
        );
    }
}
