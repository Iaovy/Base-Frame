package cn.wl.android.lib.utils;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.blankj.utilcode.util.NetworkUtils;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechEvent;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;

import cn.wl.android.lib.config.WLConfig;

/**
 * Created by JustBlue on 2019-11-14.
 *
 * @email: bo.li@cdxzhi.com
 * @desc:
 */
public class TTSHelper implements InitListener {

    public static final String APP_ID = "5dcc4806";
    private static final String TAG = "TTSHelper";

    // 默认发音人
    private static String voicer = "xiaoyan";
    private final SpeechSynthesizer mTts;

    /**
     * 合成回调监听。
     */
    private SynthesizerListener mTtsListener = new SynthesizerListener() {

        @Override
        public void onSpeakBegin() {
        }

        @Override
        public void onSpeakPaused() {
        }

        @Override
        public void onSpeakResumed() {
        }

        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos,
                                     String info) {
            // 合成进度
//            Log.e("MscSpeechLog_", "percent =" + percent);
//            mPercentForBuffering = percent;
//            showTip(String.format(getString(R.string.tts_toast_format),
//                    mPercentForBuffering, mPercentForPlaying));
        }

        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
            // 播放进度
//            Log.e("MscSpeechLog_", "percent =" + percent);
//            mPercentForPlaying = percent;
//            showTip(String.format(getString(R.string.tts_toast_format),
//                    mPercentForBuffering, mPercentForPlaying));
//
//            SpannableStringBuilder style = new SpannableStringBuilder(texts);
//            Log.e(TAG, "beginPos = " + beginPos + "  endPos = " + endPos);
//            style.setSpan(new BackgroundColorSpan(Color.RED), beginPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            ((EditText) findViewById(R.id.tts_text)).setText(style);
        }

        @Override
        public void onCompleted(SpeechError error) {
//            System.out.println("oncompleted");
//            if (error == null) {
//                //	showTip("播放完成");
//                DebugLog.LogD("播放完成," + container.size());
//                try {
//                    for (int i = 0; i < container.size(); i++) {
//                        writeToFile(container.get(i));
//                    }
//                } catch (IOException e) {
//
//                }
//                FileUtil.saveFile(memFile, mTotalSize, Environment.getExternalStorageDirectory() + "/1.pcm");
//
//
//            } else if (error != null) {
//                showTip(error.getPlainDescription(true));
//            }
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            //	 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            //	 若使用本地能力，会话id为null
//            if (SpeechEvent.EVENT_SESSION_ID == eventType) {
//                Log.d(TAG, "session id =" + sid);
//            }
//
//            //当设置SpeechConstant.TTS_DATA_NOTIFY为1时，抛出buf数据
//            if (SpeechEvent.EVENT_TTS_BUFFER == eventType) {
//                byte[] buf = obj.getByteArray(SpeechEvent.KEY_EVENT_TTS_BUFFER);
//                Log.e("MscSpeechLog_", "bufis =" + buf.length);
//                container.add(buf);
//            }
        }
    };

    @Override
    public void onInit(int code) {
        Log.d(TAG, "InitListener init() code = " + code);
        if (code != ErrorCode.SUCCESS) {
            Log.d(TAG, "初始化失败,错误码：" + code + ",请点击网址https://www.xfyun.cn/document/error-code查询解决方案");
        } else {
            // 初始化成功，之后可以调用startSpeaking方法
            // 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
            // 正确的做法是将onCreate中的startSpeaking调用移至这里
        }
    }

    private static final class Holder {
        private static final TTSHelper mIns = new TTSHelper();
    }

    private TTSHelper() {

        // 初始化合成对象
        mTts = SpeechSynthesizer.createSynthesizer(WLConfig.getContext(), this);
    }

    public static TTSHelper get() {
        return Holder.mIns;
    }

    /**
     * 参数设置
     *
     * @return
     */
    private void setParam() {
        // 清空参数
        mTts.setParameter(SpeechConstant.PARAMS, null);
        // 根据合成引擎设置相应参数
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        //支持实时音频返回，仅在synthesizeToUri条件下支持
        mTts.setParameter(SpeechConstant.TTS_DATA_NOTIFY, "1");
        //	mTts.setParameter(SpeechConstant.TTS_BUFFER_TIME,"1");
        // 设置在线合成发音人
        mTts.setParameter(SpeechConstant.VOICE_NAME, voicer);
        //设置合成语速
        mTts.setParameter(SpeechConstant.SPEED, "40");
        //设置合成音调
        mTts.setParameter(SpeechConstant.PITCH, "50");
        //设置合成音量
        mTts.setParameter(SpeechConstant.VOLUME, "50");
        //设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE, "3");
        // 设置播放合成音频打断音乐播放，默认为true
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "false");
        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "pcm");
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/tts.pcm");
    }

    public void tryPlay(String text) {
        if (!WLConfig.isConnected()) {
            return;
        }
        if (mTts.isSpeaking()) {
            mTts.stopSpeaking();
        }

        setParam();
        mTts.startSpeaking(text, mTtsListener);
    }

}
