package com.andframe.listener;

import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * 安全监听器
 * Created by SCWANG on 2016/11/26.
 */

@SuppressWarnings("unused")
public class SafeListener implements
        View.OnClickListener,
        View.OnLongClickListener,
        View.OnTouchListener,
        View.OnFocusChangeListener,
        TextView.OnEditorActionListener,
        AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener,
        AdapterView.OnItemSelectedListener,
        CompoundButton.OnCheckedChangeListener,
        ViewPager.OnPageChangeListener,
        android.text.TextWatcher,
        SeekBar.OnSeekBarChangeListener {

    /**
     * 防抖动
     */
    private static boolean enabled = true;
    private static final Runnable ENABLE_AGAIN = new Runnable() {
        @Override public void run() {
            enabled = true;
        }
    };

    private View.OnClickListener clickListener;
    private View.OnLongClickListener longClockListener;
    private View.OnTouchListener touthListener;

    private View.OnFocusChangeListener focusChangeListener;
    private TextView.OnEditorActionListener editorActionListener;
    private AdapterView.OnItemClickListener itemClickListener;
    private AdapterView.OnItemLongClickListener itemLongClickListener;
    private AdapterView.OnItemSelectedListener itemSelectedListener;
    private CompoundButton.OnCheckedChangeListener checkedChangeListener;
    private ViewPager.OnPageChangeListener pageChangeListener;
    private android.text.TextWatcher textWatcher;
    private SeekBar.OnSeekBarChangeListener seekBarChangeListener;

    public SafeListener(View.OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public SafeListener(View.OnLongClickListener longClockListener) {
        this.longClockListener = longClockListener;
    }

    public SafeListener(View.OnTouchListener touthListener) {
        this.touthListener = touthListener;
    }

    public SafeListener(SeekBar.OnSeekBarChangeListener seekBarChangeListener) {
        this.seekBarChangeListener = seekBarChangeListener;
    }

    public SafeListener(TextWatcher textWatcher) {
        this.textWatcher = textWatcher;
    }

    public SafeListener(TextView.OnEditorActionListener editorActionListener) {
        this.editorActionListener = editorActionListener;
    }

    public SafeListener(AdapterView.OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public SafeListener(AdapterView.OnItemLongClickListener itemLongClickListener) {
        this.itemLongClickListener = itemLongClickListener;
    }

    public SafeListener(AdapterView.OnItemSelectedListener itemSelectedListener) {
        this.itemSelectedListener = itemSelectedListener;
    }

    public SafeListener(CompoundButton.OnCheckedChangeListener checkedChangeListener) {
        this.checkedChangeListener = checkedChangeListener;
    }

    @Override
    public void onClick(View v) {
        if (clickListener != null && enabled) {
            try {
                enabled = false;
                v.post(ENABLE_AGAIN);
                clickListener.onClick(v);
            } catch (Throwable e) {
                //AfExceptionHandler.handle(e, "SafeListener.clickListener.onClick");
            }
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (longClockListener != null) {
            try {
                return longClockListener.onLongClick(v);
            } catch (Throwable e) {
                //AfExceptionHandler.handle(e, "SafeListener.longClockListener.onLongClick");
            }
        }
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (touthListener != null) {
            try {
                return touthListener.onTouch(v, event);
            } catch (Throwable e) {
                //AfExceptionHandler.handle(e, "SafeListener.touthListener.onTouch");
            }
        }
        return false;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (seekBarChangeListener != null) {
            try {
                seekBarChangeListener.onProgressChanged(seekBar, progress, fromUser);
            } catch (Throwable e) {
                //AfExceptionHandler.handle(e, "SafeListener.seekBarChangeListener.onProgressChanged");
            }
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        if (seekBarChangeListener != null) {
            try {
                seekBarChangeListener.onStartTrackingTouch(seekBar);
            } catch (Throwable e) {
                //AfExceptionHandler.handle(e, "SafeListener.seekBarChangeListener.onStartTrackingTouch");
            }
        }
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (seekBarChangeListener != null) {
            try {
                seekBarChangeListener.onStopTrackingTouch(seekBar);
            } catch (Throwable e) {
                //AfExceptionHandler.handle(e, "SafeListener.seekBarChangeListener.onStopTrackingTouch");
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (checkedChangeListener != null) {
            try {
                checkedChangeListener.onCheckedChanged(buttonView, isChecked);
            } catch (Throwable e) {
                //AfExceptionHandler.handle(e, "SafeListener.checkedChangeListener.onChecked");
            }
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (editorActionListener != null) {
            try {
                return editorActionListener.onEditorAction(v, actionId, event);
            } catch (Throwable e) {
                //AfExceptionHandler.handle(e, "SafeListener.editorActionListener.onEditorAction");
            }
        }
        return false;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (focusChangeListener != null) {
            try {
                focusChangeListener.onFocusChange(v, hasFocus);
            } catch (Throwable e) {
                //AfExceptionHandler.handle(e, "SafeListener.focusChangeListener.onFocusChange");
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        View postHost = parent == null ? view : parent;
        if (itemClickListener != null && (enabled || (postHost == null))) {
            try {
                if (postHost != null) {
                    enabled = false;
                    postHost.post(ENABLE_AGAIN);
                }
                itemClickListener.onItemClick(parent, view, position, id);
            } catch (Throwable e) {
                //AfExceptionHandler.handle(e, "SafeListener.itemClickListener.onItemClick");
            }
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if (itemLongClickListener != null) {
            try {
                return itemLongClickListener.onItemLongClick(parent, view, position, id);
            } catch (Throwable e) {
                //AfExceptionHandler.handle(e, "SafeListener.itemLongClick.onItemLongClick");
            }
        }
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (itemSelectedListener != null) {
            try {
                itemSelectedListener.onItemSelected(parent, view, position, id);
            } catch (Throwable e) {
                //AfExceptionHandler.handle(e, "SafeListener.itemSelectedListener.onItemSelected");
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        if (itemSelectedListener != null) {
            try {
                itemSelectedListener.onNothingSelected(parent);
            } catch (Throwable e) {
                //AfExceptionHandler.handle(e, "SafeListener.itemSelectedListener.onNothingSelected");
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (pageChangeListener != null) {
            try {
                pageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            } catch (Throwable e) {
                //AfExceptionHandler.handle(e, "SafeListener.pageChangeListener.onPageScrolled");
            }
        }
    }

    @Override
    public void onPageSelected(int position) {
        if (pageChangeListener != null) {
            try {
                pageChangeListener.onPageSelected(position);
            } catch (Throwable e) {
                //AfExceptionHandler.handle(e, "SafeListener.pageChangeListener.onPageSelected");
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (pageChangeListener != null) {
            try {
                pageChangeListener.onPageScrollStateChanged(state);
            } catch (Throwable e) {
                //AfExceptionHandler.handle(e, "SafeListener.pageChangeListener.onPageScrollStateChanged");
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if (textWatcher != null) {
            try {
                textWatcher.beforeTextChanged(s, start, count, after);
            } catch (Throwable e) {
                //AfExceptionHandler.handle(e, "SafeListener.textWatcher.beforeTextChanged");
            }
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (textWatcher != null) {
            try {
                textWatcher.onTextChanged(s, start, before, count);
            } catch (Throwable e) {
                //AfExceptionHandler.handle(e, "SafeListener.textWatcher.onTextChanged");
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (textWatcher != null) {
            try {
                textWatcher.afterTextChanged(s);
            } catch (Throwable e) {
                //AfExceptionHandler.handle(e, "SafeListener.textWatcher.afterTextChanged");
            }
        }
    }
}
