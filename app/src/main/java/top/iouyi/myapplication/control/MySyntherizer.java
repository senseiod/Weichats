package top.iouyi.myapplication.control;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.baidu.tts.client.SpeechSynthesizer;

import top.iouyi.myapplication.MainHandlerConstant;


public class MySyntherizer implements MainHandlerConstant {

    protected SpeechSynthesizer mSpeechSynthesizer;
    protected Context context;
    protected Handler mainHandler;

    private static final String TAG = "NonBlockSyntherizer";

    private static boolean isInitied = false;

    protected MySyntherizer(Context context, Handler mainHandler) {
        if (isInitied) {
            throw new RuntimeException("MySynthesizer 类里面 SpeechSynthesizer还未释放，请勿新建一个新类");
        }
        this.context = context;
        this.mainHandler = mainHandler;
        isInitied = true;
    }

    /**
     * 注意该方法需要在新线程中调用。且该线程不能结束。详细请参见NonBlockSyntherizer的实现
     */
    protected boolean init(InitConfig config) {

        sendToUiThread("");
        mSpeechSynthesizer = SpeechSynthesizer.getInstance();
        mSpeechSynthesizer.setContext(context);
        mSpeechSynthesizer.setAppId(config.getAppId());
        mSpeechSynthesizer.setApiKey(config.getAppKey(), config.getSecretKey());

        int result = mSpeechSynthesizer.initTts(config.getTtsMode());
        if (result != 0) {
            return false;
        }
        sendToUiThread(INIT_SUCCESS, "");
        return true;
    }

    /**
     * 合成并播放
     *
     */
    public int speak(String text) {
        Log.i(TAG, "speak text:" + text);
        return mSpeechSynthesizer.speak(text);
    }

    public void release() {
        mSpeechSynthesizer.stop();
        mSpeechSynthesizer.release();
        mSpeechSynthesizer = null;
        isInitied = false;
    }

    protected void sendToUiThread(String message) {
        sendToUiThread(PRINT, message);
    }

    protected void sendToUiThread(int action, String message) {
        Log.i(TAG, message);
        if (mainHandler == null) {
            return;
        }
        Message msg = Message.obtain();
        msg.what = action;
        msg.obj = message + "\n";
        mainHandler.sendMessage(msg);
    }

}
