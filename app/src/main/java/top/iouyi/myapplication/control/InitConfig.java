package top.iouyi.myapplication.control;

import com.baidu.tts.client.TtsMode;

public class InitConfig {

    private String appId;

    private String appKey;

    private String secretKey;

    private TtsMode ttsMode;

    public InitConfig(String appId, String appKey, String secretKey, TtsMode ttsMode) {
        this.appId = appId;
        this.appKey = appKey;
        this.secretKey = secretKey;
        this.ttsMode = ttsMode;
    }

    public String getAppId() {
        return appId;
    }

    public String getAppKey() {
        return appKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public TtsMode getTtsMode() {
        return ttsMode;
    }
}
