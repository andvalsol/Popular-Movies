package com.example.luthiers.popularmovies.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatImageButton;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;

import com.example.luthiers.popularmovies.R;

public class CheckableImageButton extends AppCompatImageButton implements Checkable {

    private boolean mIsChecked = false;
    private static final int[] CHECKED_STATE_SET = {
            android.R.attr.state_checked
    };
    
    public CheckableImageButton(Context context) {
        super(context);
    }
    
    public CheckableImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setChecked(attrs);
    }
    
    public CheckableImageButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setChecked(attrs);
    }
    
    private void setChecked(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CheckableImageButton);
        setChecked(a.getBoolean(R.styleable.CheckableImageButton_android_checked, false));
        a.recycle();
    }
    
    @Override
    public boolean isChecked() {
        return mIsChecked;
    }
    
    @Override
    public void setChecked(boolean checked) {
        if (mIsChecked != checked) {
            mIsChecked = checked;
            
            refreshDrawableState();
        }
    }
    
    @Override
    public void toggle() {
        setChecked(!mIsChecked);
    }
    
    @Override
    public boolean performClick() {
        toggle();
        return super.performClick();
    }
    
    @Override
    public int[] onCreateDrawableState(int extraSpace) {
        int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            View.mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        
        return drawableState;
    }
}