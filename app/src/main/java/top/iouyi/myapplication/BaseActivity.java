package top.iouyi.myapplication;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

/**
 * Created by fujiayi on 2017/9/13.
 * <p>
 * 此类 底层UI实现 无SDK相关逻辑
 */

public abstract class BaseActivity extends AppCompatActivity implements MainHandlerConstant,CompoundButton.OnCheckedChangeListener {
    protected Button mSpeak;
    protected Button mPase;
    protected Button mClear;
    protected Button[] buttons;
    protected EditText mInput;
    protected Handler mainHandler;
    private Switch onAutoCopy;

    /*
     * @param savedInstanceState
     */
    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_synth);
        onAutoCopy = findViewById(R.id.AutoCopy);
        onAutoCopy.setOnCheckedChangeListener(this);

        mainHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                handle(msg);
            }

        };
        initialView(); // 初始化UI
    }

    private void initialView() {
        mSpeak = this.findViewById(R.id.speak);
        mPase = this.findViewById(R.id.pase);
        mClear = this.findViewById(R.id.clear);
        mInput = this.findViewById(R.id.input);
        buttons = new Button[]{
                mSpeak, mClear, mPase
        };
    }

    public abstract void onCheckedChanged(CompoundButton buttonView, boolean isChecked);

    protected void handle(Message msg) {
        int what = msg.what;
        switch (what) {
            case PRINT:
                print(msg);
                break;

            case UI_CHANGE_INPUT_TEXT_SELECTION:
                if (msg.arg1 <= mInput.getText().length()) {
                    mInput.setSelection(0, msg.arg1);
                }
                break;

            case UI_CHANGE_SYNTHES_TEXT_SELECTION:
                SpannableString colorfulText = new SpannableString(mInput.getText().toString());
                if (msg.arg1 <= colorfulText.toString().length()) {
                    colorfulText.setSpan(new ForegroundColorSpan(Color.GRAY), 0, msg.arg1, Spannable
                            .SPAN_EXCLUSIVE_EXCLUSIVE);
                    mInput.setText(colorfulText);
                }
                break;

            default:
                break;
        }
    }

    private void print(Message msg) {
        String message = (String) msg.obj;
        if (message != null) {
            scrollLog(message);
        }
    }

    private void scrollLog(String message) {
        Spannable colorMessage = new SpannableString(message + "\n");
        colorMessage.setSpan(new ForegroundColorSpan(0xff0000ff), 0, message.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }
}
