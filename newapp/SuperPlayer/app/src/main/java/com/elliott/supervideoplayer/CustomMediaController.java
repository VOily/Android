package com.elliott.supervideoplayer;
//改动 ：  删除弹幕功能  增加media controller构造方法
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.elliott.supervideoplayer.utils.BitmapUtil;
import com.elliott.supervideoplayer.utils.LogUtils;
import com.elliott.supervideoplayer.utils.RandomUtil;
import com.elliott.supervideoplayer.utils.T;

import java.io.File;

import io.vov.vitamio.utils.Log;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;
import master.flame.danmaku.controller.IDanmakuView;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;

public class CustomMediaController extends MediaController {
    private Context mContext;
    private View mVolumeBrightnessLayout;
    private ImageView mOperationBg;
    private ImageView mOperationPercent;
    private AudioManager mAudioManager;
    private int mMaxVolume;
    private int mVolume = -1;
    private float mBrightness = -1f;
    private GestureDetector mGestureDetector;
    private VideoView mVideoView;

    VideoViewActivity activity;
    private ImageView mediacontroller_snapshot;
    private ImageView mediacontroller_previous;
    private ImageView mediacontroller_next;
    private ImageView mediacontroller_screen_fit;
    /**
     * public static final int VIDEO_LAYOUT_ORIGIN
     缩放参数，原始画面大小。
     常量值：0

     public static final int VIDEO_LAYOUT_SCALE
     缩放参数，画面全屏。
     常量值：1

     public static final int VIDEO_LAYOUT_STRETCH
     缩放参数，画面拉伸。
     常量值：2

     public static final int VIDEO_LAYOUT_ZOOM
     缩放参数，画面裁剪。
     常量值：3
     */
    private String[] strDialogs=new String[]{"100%","全屏","拉伸","裁剪"};
    private int[] imgs=new int[]{R.drawable.mediacontroller_sreen_size_100,R.drawable.mediacontroller_screen_fit,R.drawable.mediacontroller_screen_size,R.drawable.mediacontroller_sreen_size_crop};
    private int mCurrentPageSize=2;

    private TextView currenttime_tv;

    public CustomMediaController(Context context,VideoView mView) {
        super(context);
        this.mVideoView = mView;
        this.mContext = context;
        activity=(VideoViewActivity)context;
        mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        mMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        mGestureDetector = new GestureDetector(mContext, new MyGestureListener());

    }
    /**
     * 弹幕相关
     */
    private IDanmakuView mDanmakuView;
    private Switch mtanMuSwitch;
    private DanmakuContext mDanmakuContext;
    private BaseDanmakuParser mParser;

    private Handler mDismissHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                mVolumeBrightnessLayout.setVisibility(View.GONE);
            }
        }
    };

    @Override
    protected View makeControllerView() {
        LayoutInflater inflate = LayoutInflater.from(getContext());
        View temp = inflate.inflate(R.layout.mmediacontroller, null);
        mediacontroller_snapshot= (ImageView) temp.findViewById(R.id.mediacontroller_snapshot);
        mediacontroller_previous= (ImageView) temp.findViewById(R.id.mediacontroller_previous);
        mediacontroller_next= (ImageView) temp.findViewById(R.id.mediacontroller_next);
        mediacontroller_screen_fit= (ImageView) temp.findViewById(R.id.mediacontroller_screen_fit);
        mediacontroller_snapshot.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap currentFrame = activity.getCurrentFrame();
                //保存到本地
                String picturnPath= activity.getExternalCacheDir()+File.separator+RandomUtil.getRandomLetters(6)+".jpg";
                boolean saveSuccess = BitmapUtil.saveBitmap(currentFrame, new File(picturnPath));
                if(saveSuccess){
                    T.showLong(activity,"截屏保存到"+picturnPath);
                }else{
                    T.showLong(activity,"截屏失败");
                }
            }
        });
        mediacontroller_previous.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.reverseVideo();
            }
        });
        mediacontroller_next.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.speedVideo();
            }
        });
        mediacontroller_screen_fit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentPageSize++;
                if(mCurrentPageSize>3){
                    mCurrentPageSize=0;
                }
                T.showToastMsgShort(activity,strDialogs[mCurrentPageSize]);
                mediacontroller_screen_fit.setBackground(getResources().getDrawable(imgs[mCurrentPageSize]));
                activity.setVideoPageSize(mCurrentPageSize);
            }
        });
        currenttime_tv = (TextView) temp.findViewById(R.id.currenttime_tv);
        mVolumeBrightnessLayout = temp.findViewById(R.id.operation_volume_brightness);
        mOperationBg = (ImageView) temp.findViewById(R.id.operation_bg);
        mOperationPercent = (ImageView) temp.findViewById(R.id.operation_percent);
        /*temp.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("down");
                if (mGestureDetector.onTouchEvent(event)) {
                    Log.d("success");
                    return true;
                }
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_UP:
                        Log.d("up");
                        endGesture();
                        break;
                }

                return false;
            }
        });*/
        temp.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mGestureDetector.onTouchEvent(event);
                return true;

            }
        });
        return temp;
    }

    private void endGesture() {
        mVolume = -1;
        mBrightness = -1f;
        // 隐藏
        mDismissHandler.removeMessages(0);
        mDismissHandler.sendEmptyMessageDelayed(0, 500);
    }

    public void setTanMuView(IDanmakuView tanMuView,DanmakuContext mDanmakuContext,BaseDanmakuParser mParser ) {
        this.mDanmakuView = tanMuView;
        this.mDanmakuContext=mDanmakuContext;
        this.mParser=mParser;
    }

    /**
     * 声音高低
     *
     * @param percent
     */
    private void onVolumeSlide(float percent) {
        if (mVolume == -1) {
            mVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            if (mVolume < 0)
                mVolume = 0;

            mOperationBg.setImageResource(R.drawable.video_volumn_bg);
            mVolumeBrightnessLayout.setVisibility(View.VISIBLE);
        }

        int index = (int) (percent * mMaxVolume) + mVolume;
        if (index > mMaxVolume)
            index = mMaxVolume;
        else if (index < 0)
            index = 0;
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0);
        //findViewById返回为空，尝试mContex view activity还是不行
        //ViewGroup.LayoutParams lp = mOperationPercent.getLayoutParams();
        //lp.width = findViewById(R.id.operation_full).getLayoutParams().width * index / mMaxVolume;
        //mOperationPercent.setLayoutParams(lp);
    }

    /**
     * 处理屏幕亮暗
     *
     * @param percent
     */
    private void onBrightnessSlide(float percent) {
        if (mBrightness < 0) {
            mBrightness = ((Activity) mContext).getWindow().getAttributes().screenBrightness;
            if (mBrightness <= 0.00f)
                mBrightness = 0.50f;
            if (mBrightness < 0.01f)
                mBrightness = 0.01f;

            mOperationBg.setImageResource(R.drawable.video_brightness_bg);
            mVolumeBrightnessLayout.setVisibility(View.VISIBLE);
        }
        WindowManager.LayoutParams lpa = ((Activity) getContext()).getWindow().getAttributes();
        lpa.screenBrightness = mBrightness + percent;
        if (lpa.screenBrightness > 1.0f)
            lpa.screenBrightness = 1.0f;
        else if (lpa.screenBrightness < 0.01f)
            lpa.screenBrightness = 0.01f;
        ((Activity) mContext).getWindow().setAttributes(lpa);

        //ViewGroup.LayoutParams lp = mOperationPercent.getLayoutParams();
        //lp.width = (int) (findViewById(R.id.operation_full).getLayoutParams().width * lpa.screenBrightness);
        //mOperationPercent.setLayoutParams(lp);
    }
    //播放与暂停
    private void playOrPause(){
        if (mVideoView != null)
            if (mVideoView.isPlaying()) {
                mVideoView.pause();
            } else {
                mVideoView.start();
            }
    }

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        //滑动监听
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            float mOldX = e1.getX(), mOldY = e1.getY();
            int y = (int) e2.getRawY();
            int x = (int) e2.getRawX();
            Display disp = activity.getWindowManager().getDefaultDisplay();
            int windowWidth = disp.getWidth();
            int windowHeight = disp.getHeight();
            if (mOldX > windowWidth * 3.0 / 4.0) {// 右边滑动 屏幕 3/4
                onVolumeSlide((mOldY - y) / windowHeight);
            } else if (mOldX < windowWidth * 1.0 / 4.0) {// 左边滑动 屏幕 1/4
                onBrightnessSlide((mOldY - y) / windowHeight);
            }
            return super.onScroll(e1, e2, distanceX, distanceY);
        }
        //双击暂停或开始
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            playOrPause();
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return super.onFling(e1, e2, velocityX, velocityY);
        }

    }
}
