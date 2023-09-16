package com.bkav.aiotcloud.screen.setting.face.historyObject;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.bkav.aiotcloud.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.Collection;
import java.util.HashSet;

public class EventDecorator implements DayViewDecorator {
    private Context context;
    private final int color;
    private final HashSet<CalendarDay> dates;

    public EventDecorator(int color, Collection<CalendarDay> dates, Context context) {
        this.color = color;
        this.dates = new HashSet<>(dates);
        this.context = context;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new DotSpan(5, color));
    }
}
