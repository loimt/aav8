package com.bkav.aiotcloud.screen.setting.face.customer;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.ConfigurationCompat;

import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.language.LanguageManager;
import com.bkav.aiotcloud.screen.setting.face.StepThreeFragment;
import com.bkav.aiotcloud.screen.setting.face.edit.InfoAIScreen;
import com.bkav.aiotcloud.screen.user.EditUserProfile;
import com.bkav.aiotcloud.util.LanguageConfig;
import com.bkav.aiotcloud.util.SharedPrefs;

import java.lang.reflect.Field;
import java.util.Locale;
import java.util.Objects;

import javax.xml.transform.Result;

public class SetTimeDialog extends AppCompatActivity {
    private Button save;
    private DatePicker datePicker;
    public static String TIME = "time";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.settime_dialog);
        setLayout();
        init();
        setData();
        action();
    }

    private void init() {
        this.save = findViewById(R.id.save);
        this.datePicker = findViewById(R.id.datePicker);

    }

    private void setData() {
        this.save.setText(LanguageManager.getInstance().getValue("save"));
        LanguageConfig.changeLanguage(getApplicationContext(), LanguageManager.getInstance().getValue("datetime_picker"));
//        Intent intent = getIntent();
        String time = getIntent().getStringExtra(TIME); // dd/mm/yyyy
        String[] times = time.split("/");
        String date = times[0];
        String month = times[1];
        String year = times[2];
        if (!date.isEmpty() && !month.isEmpty() && !year.isEmpty()) {
            this.datePicker.updateDate(Integer.parseInt(year), Integer.parseInt(month) - 1, Integer.parseInt(date));
        }
        colorizeDatePicker(datePicker);
    }

    private void action() {
        this.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                @SuppressLint("DefaultLocale") String data = String.format("%02d-%02d-%02d", datePicker.getDayOfMonth(), datePicker.getMonth() + 1, datePicker.getYear());
                Log.e("time", data);
                Intent dataBack = new Intent();
                dataBack.putExtra(AddCustomerActivity.TIME, data);
                dataBack.putExtra(EditUserProfile.TIME, data);
                setResult(RESULT_OK, dataBack);
                finish();
            }
        });
    }

    private void setLayout() {
        Window window = this.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        setFinishOnTouchOutside(false);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        Context context = LanguageConfig.changeLanguage(newBase, LanguageManager.getInstance().getValue("datetime_picker"));
        super.attachBaseContext(context);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public static void colorizeDatePicker(DatePicker datePicker) {
        Resources system = Resources.getSystem();
        int dayId = system.getIdentifier("day", "id", "android");
        int monthId = system.getIdentifier("month", "id", "android");
        int yearId = system.getIdentifier("year", "id", "android");

        NumberPicker dayPicker = (NumberPicker) datePicker.findViewById(dayId);
        NumberPicker monthPicker = (NumberPicker) datePicker.findViewById(monthId);
        NumberPicker yearPicker = (NumberPicker) datePicker.findViewById(yearId);

        setDividerColor(dayPicker);
        setDividerColor(monthPicker);
        setDividerColor(yearPicker);
    }

    private static void setDividerColor(NumberPicker picker) {
        if (picker == null)
            return;

        final int count = picker.getChildCount();
        for (int i = 0; i < count; i++) {
            try {
                @SuppressLint("SoonBlockedPrivateApi") Field dividerField = picker.getClass().getDeclaredField("mSelectionDivider");
                dividerField.setAccessible(true);
                ColorDrawable colorDrawable = new ColorDrawable(picker.getResources().getColor(R.color.colorAccent, null));
                dividerField.set(picker, colorDrawable);
                picker.invalidate();
            } catch (Exception e) {
                Log.w("setDividerColor", e);
            }
        }

        try {
            @SuppressLint("SoonBlockedPrivateApi") Field selectorWheelPaintField = picker.getClass()
                    .getDeclaredField("mSelectorWheelPaint");
            selectorWheelPaintField.setAccessible(true);
            ((Paint) Objects.requireNonNull(selectorWheelPaintField.get(picker))).setColor(picker.getResources().getColor(R.color.white, null));
        } catch (NoSuchFieldException | IllegalAccessException | IllegalArgumentException e) {
            Log.w("setNumberPickerTextColor", e);
        }

        final int countIndex = picker.getChildCount();
        for (int i = 0; i < countIndex; i++) {
            View child = picker.getChildAt(i);
            if (child instanceof EditText)
                ((EditText) child).setTextColor(picker.getResources().getColor(R.color.white, null));
        }
        picker.invalidate();
    }
}
