package com.andframe.demo;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

/**
 * A placeholder fragment containing a simple view.
 */
@BindLayout(R.layout.fragment_fragment)
public class FragmentActivityFragment extends Fragment {

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fragment, container, false);
    }


    @BindOnClick(R.id.message)
    public void onMessageClick() {
        Toast.makeText(getContext(), "onMessageClick", Toast.LENGTH_SHORT).show();
    }

    @BindOnTouch(R.id.message)
    public boolean onTouch(MotionEvent event) {
        if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
            Toast.makeText(getContext(), "ACTION_DOWN", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @BindOnItemClick(R.id.message)
    public void onItemClick(AdapterView adapterView, View view, int index) {

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

}
