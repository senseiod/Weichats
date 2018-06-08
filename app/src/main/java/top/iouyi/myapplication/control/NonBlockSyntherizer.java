package top.iouyi.myapplication.control;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;


public class NonBlockSyntherizer extends MySyntherizer {

    private static final int INIT = 1;
    private HandlerThread hThread;
    private Handler tHandler;

    public NonBlockSyntherizer(Context context, InitConfig initConfig, Handler mainHandler) {
        super(context, mainHandler);
        initThread();
        runInHandlerThread(INIT, initConfig);
    }

    protected void initThread() {
        hThread = new HandlerThread("");
        hThread.start();
        tHandler = new Handler(hThread.getLooper()) {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
               InitConfig config = (InitConfig) msg.obj;
               init(config);
            }
        };
    }
    private void runInHandlerThread(int action, Object obj) {
        Message msg = Message.obtain();
        msg.what = action;
        msg.obj = obj;
        tHandler.sendMessage(msg);
    }
}
