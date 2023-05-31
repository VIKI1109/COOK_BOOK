package mdp20126376.mdpcw02.cookbook.activity;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.res.AssetFileDescriptor;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.Surface;
import android.view.TextureView;
import android.widget.Toast;

import java.io.IOException;

import androidx.annotation.RequiresApi;

import mdp20126376.mdpcw02.cookbook.data.RecipeContract;
import mdp20126376.mdpcw02.cookbook.model.BaseActivity;
import mdp20126376.mdpcw02.cookbook.R;
import mdp20126376.mdpcw02.cookbook.model.Video;
import mdp20126376.mdpcw02.cookbook.service.VideoService;



public class RecipeVideoPlayActivity extends BaseActivity {


    public VideoService.MyBinder myBinder;

    SeekBar seekBar;
    private String recipe_title;
    private String video_path;
    Button play, stop;
    TextView tv_start_time, tv_end_time;
    TextureView textureView;
    Surface surface;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_video_play);

        Intent intentService = new Intent(RecipeVideoPlayActivity.this, VideoService.class);
        startService(intentService);//start the service
        bindService(intentService, serviceConnection, BIND_AUTO_CREATE);//bind the service


        final Intent intent = getIntent();
        recipe_title = intent.getStringExtra("title");
        video_path = intent.getStringExtra("videoPath");
//        seekBar = findViewById(R.id.tv_progess);
//
//        play = findViewById(R.id.iv_play);
//        stop = findViewById(R.id.iv_stop);
//        tv_start_time = findViewById(R.id.tv_start_time);
//        tv_end_time = findViewById(R.id.tv_end_time);
        VideoService.progressBar = seekBar;



        textureView = findViewById(R.id.textureView);

        textureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {

            @Override
            public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surfaceTexture, int i, int i1) {
                Log.e("RecipeVideoPlay", "onSurfaceTextureAvailable");

                surface = new Surface(surfaceTexture);


            }

            @Override
            public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surfaceTexture, int i, int i1) {
                Log.e("RecipeVideoPlay", "onSurfaceTextureSizeChanged");
                float textureWidth = textureView.getWidth();
                 float textureHeight =textureView.getHeight();
                 stretching(textureWidth, textureHeight);


            }

            @Override
            public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surfaceTexture) {
                Log.e("RecipeVideoPlay", "onSurfaceTextureDestroyed");

                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surfaceTexture) {
//                Log.e("RecipeVideoPlay", "onSurfaceTextureUpdated");

            }
        });


     //   play.setOnClickListener(view -> myBinder.play());

        /// pause.setOnClickListener(view -> myBinder.suspend());
//
//
       // stop.setOnClickListener(view -> myBinder.cease());
    }


    @Override //为什么finish不了
    protected void onDestroy() {
// TODO Auto-generated method stub

        super.onDestroy();
        if (serviceConnection != null) {
            unbindService(serviceConnection);
        }
        stopService(new Intent(this, VideoService.class));
    }

    @Override
    protected void onStart() {

        super.onStart();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onRestart() {


        myBinder.resume();
        int position = VideoService.position;
        System.out.println(position);
        super.onRestart();

    }

    @Override
    protected void onPause() {

        super.onPause();
    }

    @Override
    protected void onResume() {

        super.onResume();

    }

    @Override
    protected void onStop() {
        if (VideoService.state == VideoService.VideoPlayerState.PLAYING) {
            myBinder.suspend();
            int position = VideoService.position;
            System.out.println(position);
        } else if (VideoService.state == VideoService.VideoPlayerState.PAUSED) {
            int position = VideoService.position;
            System.out.println(position);
        }
        super.onStop();
    }


    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e("RecipeVideoPlay", "onServiceConnected");
            myBinder = (VideoService.MyBinder) service;
            myBinder.registerListenerForPlayerChange(videoServiceChangeListenerInterface);
            myBinder.play_first_time(surface, video_path);

            float textureWidth = textureView.getWidth();
            float textureHeight =textureView.getHeight();
            stretching(textureWidth, textureHeight);

            //myBinder.changeOrientation();


        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            myBinder.unregisterListenerForPlayerChange(videoServiceChangeListenerInterface);


        }
    };


    private final VideoService.VideoServiceChangeListenerInterface videoServiceChangeListenerInterface = new VideoService.VideoServiceChangeListenerInterface() {


        /**
         * When the progress of MP3player changes, the text of duration and progress will change.
         * @param played the current progress
         * @param duration  the current song duration
         */
        @Override
        public void mediaPlayerProgressChange(long played, long duration) {

//
//            tv_end_time.setText(durationTimeToTextString((int) duration));
//
//            tv_start_time.setText(durationTimeToTextString((int) played));


        }


        @Override
        public void mediaPlayerSeekToChange() {
        }


        /**
         * When MP3player is playing, change the pause image button to play image button
         * And if the song is from other application, set the song name to "Song from Another Application"
         * Otherwise, set the text of the song name to the title of this song in the song list.
         */

        @SuppressLint({"ResourceType", "UseCompatLoadingForDrawables"})
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void mediaPlayerIsPlaying() {

            Log.w("DisplayActivity", "STATUS_PLAYING");
          //  play.setText("PAUSE");

        }


        /**
         * When the player is paused, set the play button to pause button
         */

        @SuppressLint("UseCompatLoadingForDrawables")
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void mediaPlayerIsPaused() {

//            play.setText("PLAY");

            Log.w("DisplayActivity", "STATUS_PAUSED");

        }

        /**
         *When the player resume to play, set the pause button to play button
         */

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @SuppressLint("UseCompatLoadingForDrawables")
        @Override
        public void mediaPlayerIsResumed() {
           // play.setText("pause");
            Log.w("DisplayActivity", "STATUS_PLAYING");
        }


        /**
         * When the player is stopped, reset the duration text and progress text to 0.
         *
         */
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @SuppressLint("UseCompatLoadingForDrawables")
        @Override
        public void mediaPlayerStop() {

            finish();


        }



    };


    /**
     * Change the time to string type 0:00
     */

    public String durationTimeToTextString(int duration) {
        int durationTosecond = duration / 1000;
        int durationTominute = durationTosecond / 60;
        int second = durationTosecond % 60;
        StringBuilder stringBuilder = new StringBuilder();
        if (durationTominute < 10) {
            stringBuilder.append(0);
        }
        stringBuilder.append(durationTominute);
        stringBuilder.append(':');
        if (second < 10) {
            stringBuilder.append(0);
        }
        stringBuilder.append(second);
        return stringBuilder.toString();
    }


    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //VideoService.isRotating=true;

        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {

            Toast.makeText(RecipeVideoPlayActivity.this, "Orientation Portrait", Toast.LENGTH_SHORT).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

            Toast.makeText(RecipeVideoPlayActivity.this, "Orientation Landscape", Toast.LENGTH_SHORT).show();
        }

    }


    public  void stretching(float mtextureViewWidth, float mtextureViewHeight) {


        Matrix matrix = new Matrix();

        int mVideoWidth = VideoService.mediaPlayer.getVideoWidth();

        int mVideoHeight = VideoService.mediaPlayer.getVideoHeight();

        float sx = mtextureViewWidth / mVideoWidth;
        float sy = mtextureViewHeight / mVideoHeight;

        float sx1 = mVideoWidth / mtextureViewWidth;
        float sy1 = mVideoHeight / mtextureViewHeight;
        matrix.preScale(sx1, sy1);
        Log.d("mat", matrix.toString());

        if (sx >= sy) {
            matrix.preScale(sy, sy);
            float leftX = (mtextureViewWidth - mVideoWidth * sy) / 2;
            matrix.postTranslate(leftX, 0);
        } else {
            matrix.preScale(sx, sx);
            float leftY = (mtextureViewHeight - mVideoHeight * sx) / 2;
            matrix.postTranslate(0, leftY);
        }

        textureView.setTransform(matrix);
        textureView.postInvalidate();



    }


}





