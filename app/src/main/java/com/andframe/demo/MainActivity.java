package com.andframe.demo;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.andframe.annotation.BindLayout;
import com.andframe.annotation.listener.BindOnCheckedChanged;
import com.andframe.annotation.listener.BindOnClick;
import com.andframe.annotation.listener.BindOnEditorAction;
import com.andframe.annotation.listener.BindOnItemClick;
import com.andframe.annotation.listener.BindOnTextChanged;
import com.andframe.annotation.listener.BindOnTouch;
import com.andframe.annotation.resource.BindColor;
import com.andframe.annotation.resource.BindDrawable;
import com.andframe.annotation.resource.BindString;
import com.andframe.annotation.view.BindView;
import com.andframe.annotation.view.BindViews;

import java.util.List;

import butterknife.OnPageChange;

@BindLayout(R.layout.activity_main)
public abstract class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.message)
    TextView mTextMessage;

    @BindView(R.id.navigation)
    BottomNavigationView mNavigationView;

    @BindViews(R.id.message)
    TextView[] mTextMessages;

    @BindViews({R.id.message,R.id.navigation})
    List<View> mTextMessageList;

    @BindString(R.string.app_name)
    String appname;

    @BindDrawable(R.mipmap.ic_launcher)
    Drawable drawable;

    @BindColor(android.R.color.holo_orange_light)
    int primaryColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTextMessage.setBackgroundDrawable(drawable);
        mNavigationView.setOnNavigationItemSelectedListener(this);
        mNavigationView.setBackgroundColor(primaryColor);
        Toast.makeText(this, "appname = " + appname, Toast.LENGTH_SHORT).show();
    }

    @BindOnClick(R.id.message)
    public void onMessageClick() {
        Toast.makeText(this, "onMessageClick", Toast.LENGTH_SHORT).show();
    }

    @BindOnTouch(R.id.message)
    public boolean onTouch(MotionEvent event) {
        if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
            Toast.makeText(this, "ACTION_DOWN", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @BindOnItemClick(R.id.message)
    public void onItemClick(int index) {

    }

    @BindOnEditorAction(R.id.message)
    public boolean onEditorAction(int index) {
        return false;
    }

    @BindOnCheckedChanged(R.id.content)
    public void onCheckedChanged(boolean value) {

    }

    @OnPageChange(R.id.content)
    public void onPageChange(int index) {

    }

    @BindOnTextChanged(R.id.content)
    public void onTextChange(CharSequence s) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                mTextMessage.setText(R.string.title_home);
                return true;
            case R.id.navigation_dashboard:
                mTextMessage.setText(R.string.title_dashboard);
                return true;
            case R.id.navigation_notifications:
                mTextMessage.setText(R.string.title_notifications);
                return true;
        }
        return false;
    }
}
