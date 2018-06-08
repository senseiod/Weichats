package top.iouyi.myapplication.Service;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.TtsMode;

public class CopyService extends Service {

    protected String appId = "11353503";
    protected String appKey = "2QNdxNwvIxtaf8A8npXG8eVG";
    protected String secretKey = "MzldBSjHqPxDMGL53tkSwEBKA4L12ZxU";
    private TtsMode ttsMode = TtsMode.ONLINE;
    protected SpeechSynthesizer mSpeechSynthesizer;

    @Override
    public void onCreate() {
        super.onCreate();
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        assert clipboardManager != null;
        clipboardManager.addPrimaryClipChangedListener(mClipListener);
        initTTs();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(getApplicationContext(), "服务已开启", Toast.LENGTH_SHORT).show();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    ClipboardManager.OnPrimaryClipChangedListener mClipListener = new
            ClipboardManager.OnPrimaryClipChangedListener() {

        public String p2ps;

        public void onPrimaryClipChanged() {
            final ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = clipboard.getPrimaryClip();

            if (clipData != null && clipData.getItemCount() > 0) {
                String CopyText = String.valueOf(clipData.getItemAt(0).getText());
                p2ps = CopyText;
                Toast.makeText(getApplicationContext(), p2ps, Toast.LENGTH_SHORT).show();
                mSpeechSynthesizer.speak(p2ps);
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.removePrimaryClipChangedListener(mClipListener);
        if (mSpeechSynthesizer != null) {
            mSpeechSynthesizer.release();
            mSpeechSynthesizer = null;
        }
        super.onDestroy();
        Toast.makeText(getApplicationContext(), "服务已被关闭", Toast.LENGTH_SHORT).show();
    }

    private void initTTs() {
        mSpeechSynthesizer = SpeechSynthesizer.getInstance();
        mSpeechSynthesizer.setContext(this);
        mSpeechSynthesizer.setAppId(appId);
        mSpeechSynthesizer.setApiKey(appKey, secretKey);
        mSpeechSynthesizer.initTts(ttsMode);
    }
}
