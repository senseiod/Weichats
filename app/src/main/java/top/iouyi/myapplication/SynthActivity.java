package top.iouyi.myapplication;

import com.baidu.tts.client.TtsMode;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;

import top.iouyi.myapplication.Service.CopyService;
import top.iouyi.myapplication.control.InitConfig;
import top.iouyi.myapplication.control.MySyntherizer;
import top.iouyi.myapplication.control.NonBlockSyntherizer;

public class SynthActivity extends BaseActivity implements View.OnClickListener {

    protected String appId = "11353503";
    protected String appKey = "2QNdxNwvIxtaf8A8npXG8eVG";
    protected String secretKey = "MzldBSjHqPxDMGL53tkSwEBKA4L12ZxU";
    protected TtsMode ttsMode = TtsMode.ONLINE;
    protected MySyntherizer synthesizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialButtons();
        initialTts();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.speak:
                speak();
                break;

            case R.id.pase:
                pasteText();
                break;

            case R.id.clear:
                clear();
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            StartService();
        } else {
            StopService();
        }
    }

    protected void initialTts() {
        InitConfig initConfig = new InitConfig(appId, appKey, secretKey, ttsMode);
        synthesizer = new NonBlockSyntherizer(this, initConfig, mainHandler);
    }

    private void speak() {
        String text = mInput.getText().toString();
        if (TextUtils.isEmpty(mInput.getText())) {
            text = "请输入你要合成的语音";
            mInput.setText(text);
        }
        synthesizer.speak(text);
    }

    private void clear() {
        String text;
        text = "";
        mInput.setText(text);
    }

    private void pasteText() {
        Toast.makeText(SynthActivity.this, "已粘贴", Toast.LENGTH_SHORT).show();
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = clipboard.getPrimaryClip();
        if (clipData != null && clipData.getItemCount() > 0) {
            CharSequence pasteText = clipData.getItemAt(0).getText();
            mInput.setText(pasteText);
        }
    }

    @Override
    protected void onDestroy() {
        synthesizer.release();
        super.onDestroy();
    }

    protected void handle(Message msg) {
        for (Button b : buttons) {
            b.setEnabled(true);
        }
    }

    private void initialButtons() {
        for (Button b : buttons) {
            b.setOnClickListener(this);
            b.setEnabled(false);
        }
    }

    private void StartService() {
        Intent intentFive = new Intent(this, CopyService.class);
        startService(intentFive);
        startNotification();
    }

    private void StopService() {
        Intent intentFive = new Intent(this, CopyService.class);
        stopService(intentFive);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancelAll();
    }

    // 后台运行实现
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        PackageManager pm = getPackageManager();
        ResolveInfo homeInfo =
                pm.resolveActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME), 0);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ActivityInfo ai = homeInfo.activityInfo;
            Intent startIntent = new Intent(Intent.ACTION_MAIN);
            startIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            startIntent.setComponent(new ComponentName(ai.packageName, ai.name));
            startActivitySafely(startIntent);
            return true;
        } else
            return super.onKeyDown(keyCode, event);
    }

    private void startActivitySafely(Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "null",
                    Toast.LENGTH_SHORT).show();
        } catch (SecurityException e) {
            Toast.makeText(this, "null",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void startNotification(){
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("服务已开启")
                .setContentText("现在你可以直接复制了")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .build();
        notification.flags |= Notification.FLAG_ONGOING_EVENT;
        manager.notify(1, notification);
    }
}
