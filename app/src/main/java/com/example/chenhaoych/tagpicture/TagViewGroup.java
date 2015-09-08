package com.example.chenhaoych.tagpicture;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by chenhao.ych on 2015/7/1.
 */
public class TagViewGroup extends ViewGroup implements View.OnTouchListener {
    private int lastX = -1, lastY = -1, dragged = -1;
    private ArrayList<Point> tagPositions = new ArrayList<>();

    public TagViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnTouchListener(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < getChildCount(); i++) {
            Point position = tagPositions.get(i);
            View tagview = getChildAt(i);
            int measureHeight = tagview.getMeasuredHeight();
            int measuredWidth = tagview.getMeasuredWidth();
            tagview.layout(position.x, position.y - measureHeight / 2, position.x + measuredWidth, position.y + measureHeight / 2);
        }
    }


    public boolean onTouch(View view, MotionEvent event) {
        int action = event.getAction();
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                lastX = (int) event.getX();
                lastY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                dragged = getIndexFromXY(lastX, lastY);
                if (dragged != -1) {
                    int x = (int) event.getX(), y = (int) event.getY();
                    int measureHeight = getChildAt(dragged).getMeasuredHeight();
                    int measureWidth = getChildAt(dragged).getMeasuredWidth();
                    int l = tagPositions.get(dragged).x, t = tagPositions.get(dragged).y - measureHeight / 2;
                    l += (x - lastX);
                    t += (y - lastY);
                    getChildAt(dragged).layout(l, t, l + measureWidth, t + measureHeight);
                }
                break;
            case MotionEvent.ACTION_UP:
                int x = (int) event.getX(), y = (int) event.getY();
                if (dragged == -1 && Math.abs(x - lastX) <= 10
                        && Math.abs(x - lastX) <= 10) {
                    View tagview = LayoutInflater.from(getContext()).inflate(R.layout.tag_view, null);
                    tagPositions.add(new Point(lastX, lastY));
                    addView(tagview);
                } else if(dragged != -1){
                    int oldx = tagPositions.get(dragged).x, oldy = tagPositions.get(dragged).y;
                    tagPositions.set(dragged, new Point(oldx + (x - lastX), oldy + (y - lastY)));
                }
                break;
        }
        return true;
    }

    private int getIndexFromXY(int x, int y) {
        for (int i = 0; i < getChildCount(); i++) {
            View tagview = getChildAt(i);
            Point leftmiddle = tagPositions.get(i);
            int measureHeight = tagview.getMeasuredHeight();
            int measureWidth = tagview.getMeasuredWidth();
            if (x <= leftmiddle.x + measureWidth && x >= leftmiddle.x && y <= leftmiddle.y + measureHeight / 2 && y >= leftmiddle.y - measureHeight / 2) {
                return i;
            }
        }
        return -1;
    }
}
