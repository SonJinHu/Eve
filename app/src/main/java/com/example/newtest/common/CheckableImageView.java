package com.example.newtest.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;
import android.widget.ImageView;

@SuppressLint("AppCompatCustomView")
public class CheckableImageView extends ImageView implements Checkable, View.OnClickListener {

    private static final int[] CHECKED_STATE_SET = {android.R.attr.state_checked};
    private boolean mChecked;
    private setOnCheckedChangeListener mListener;

    public CheckableImageView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        setOnClickListener(this);
    }

    @Override
    public int[] onCreateDrawableState(final int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked())
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        return drawableState;
    }

    @Override
    public void toggle() {
        setChecked(!mChecked);
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void setChecked(final boolean checked) {
        if (mChecked == checked) {
            if (mListener != null) {
                mListener.onCheckedChanged(mChecked);
            }
            return;
        }
        mChecked = checked;
        if (mListener != null) {
            mListener.onCheckedChanged(mChecked);
        }
        refreshDrawableState();
    }

    @Override
    public void onClick(View v) {
        toggle();
    }

    public void setOnCheckedChangeListener(setOnCheckedChangeListener listener) {
        mListener = listener;
    }

    public interface setOnCheckedChangeListener {
        void onCheckedChanged(boolean isChecked);
    }
}