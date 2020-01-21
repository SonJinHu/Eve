package com.example.newtest.test;

import android.view.MotionEvent;
import android.view.View;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

public class BottomSheet extends BottomSheetBehavior {
    @Override
    public boolean onTouchEvent(CoordinatorLayout parent, View child, MotionEvent event) {
        return super.onTouchEvent(parent, child, event);
    }


}
