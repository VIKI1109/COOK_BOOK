package mdp20126376.mdpcw02.cookbook.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.Matrix;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;

import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.TextureView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import androidx.annotation.RequiresApi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


//// 暂停后是继续播！！！ 改
public class VideoService extends Service {


    //直接点stop会有bug！//  还要改textview的样式 区分度大些// refactory 重构一下。 //
    public interface VideoServiceChangeListenerInterface {
        void mediaPlayerProgressChange(long played, long duration);

        void mediaPlayerSeekToChange();

        void mediaPlayerIsPlaying();

        void mediaPlayerIsPaused();

        void mediaPlayerIsResumed();

        void mediaPlayerStop();

    }


    public VideoService() {
    }

    public static MediaPlayer mediaPlayer;
    public MyBinder myBinder;
    public static SeekBar progressBar;
    public static TextureView texture;
    public static VideoPlayerState state;
    public static int position;
    public static boolean isRotating = false;



    Thread thread;

    public enum VideoPlayerState {
        PLAYING,
        PAUSED,
        STOPPED
    }

    private List<VideoServiceChangeListenerInterface> listenerInterfaceList;


    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }


    // 创建
    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("VideoService", "onCreate");
        this.state = VideoPlayerState.STOPPED;
        mediaPlayer = new MediaPlayer();
        myBinder = new MyBinder();
        listenerInterfaceList = new ArrayList<>();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("VideoService", "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public boolean onUnbind(Intent intent) {
        Log.e("MusicService", "onUnbind");
        return false;
    }

    /**
     * After Unbind the service, when the user come back to the activities, the service will be rebound.
     *
     * @param intent
     */
    @Override
    public void onRebind(Intent intent) {
        Log.e("VideoService", "onRebind");
        super.onRebind(intent);
    }


    @Override
    public void onDestroy() {
        Log.e("VideoService", "onDestroy");

        listenerInterfaceList.clear();
        this.state = VideoPlayerState.STOPPED;

        super.onDestroy();

    }


    public class MyBinder extends Binder {
        // 播放
        public void play_first_time(Surface surface, String path) {
            System.out.println("play");
            VideoService.this.play_first_time(surface, path);
            for (VideoServiceChangeListenerInterface listener : listenerInterfaceList) {
                listener.mediaPlayerIsPlaying(); //the state of the media player is playing
            }
        }

        public void play() {
            System.out.println("play");
            VideoService.this.play();
        }


        @RequiresApi(api = Build.VERSION_CODES.O)
        public void resume() {
            System.out.println("resume");
            VideoService.this.resumeVideo();
            for (VideoServiceChangeListenerInterface listener : listenerInterfaceList) {
                listener.mediaPlayerIsResumed(); //the state of the media player is playing
            }
        }



        // 停止
        public void cease() {
            System.out.println("stop");
            VideoService.this.cease();
            for (VideoServiceChangeListenerInterface listener : listenerInterfaceList) {
                listener.mediaPlayerStop();
            }
        }

        // 暂停
        public void suspend() {
            System.out.println("pause");
            VideoService.this.suspend();
            for (VideoServiceChangeListenerInterface listener : listenerInterfaceList) {
                listener.mediaPlayerIsPaused();
            }
        }

        public void registerListenerForPlayerChange(VideoServiceChangeListenerInterface listener) {
            listenerInterfaceList.add(listener);
        }

        public void unregisterListenerForPlayerChange(VideoServiceChangeListenerInterface listener) {
            listenerInterfaceList.remove(listener);
        }



    }


    // 停止
    public void cease() {
// 停止

        if(mediaPlayer!=null&&thread==null&&state==VideoPlayerState.PAUSED){

            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
        }
        //直接点pause 再点stop 报错 没有thread
        else if(mediaPlayer!=null&&thread!=null&&state!=VideoPlayerState.STOPPED){
        mediaPlayer.stop();  //
        mediaPlayer.reset();
        mediaPlayer.release();
        thread.interrupt();
       }

        state = VideoPlayerState.STOPPED;

    }



    public void suspend() {


        if (state==VideoPlayerState.PLAYING) {
            System.out.println("pause");
            mediaPlayer.pause();
            thread.interrupt();
        }
        state = VideoPlayerState.PAUSED;

    }




        @RequiresApi(api = Build.VERSION_CODES.O)
        public void resumeVideo(){


            mediaPlayer.seekTo(position, MediaPlayer.SEEK_CLOSEST);

            mediaPlayer.setOnSeekCompleteListener(m -> {
                mediaPlayer.start();
                state=VideoPlayerState.PLAYING;

                thread = new Thread(run);
                thread.start();
            });

            Log.e("续播时间：", String.valueOf(position));
        }



        // 播放
        public void play_first_time(Surface surface,String path) {


                    if (state == VideoPlayerState.STOPPED) {
                    try {
                        mediaPlayer.reset();
                        mediaPlayer.setDataSource(path);
                        mediaPlayer.setSurface(surface);
                        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        System.out.println("开始播放");

                        mediaPlayer.prepare();
                       // progressBar.setOnSeekBarChangeListener(new Progress());
                        mediaPlayer.start();
                        state = VideoPlayerState.PLAYING;
                        thread = new Thread(run);
                        thread.start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }


      public void play(){

        if (state == VideoPlayerState.PLAYING) {

            suspend();
            for (VideoServiceChangeListenerInterface listener : listenerInterfaceList) {

                listener.mediaPlayerIsPaused();
            }
          }

        else if (state == VideoPlayerState.PAUSED) {
            mediaPlayer.start();
            state=VideoPlayerState.PLAYING;
            new Thread(run).start();
            for (VideoServiceChangeListenerInterface listener : listenerInterfaceList) {

                listener.mediaPlayerIsPlaying(); //the state of the media player is playing
            }
        }

    }




        // 设置进度
        public class Progress implements OnSeekBarChangeListener {


            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {

                if (fromUser) {
                    System.out.println(progress + "---" + true);

                    mediaPlayer.seekTo(progress,MediaPlayer.SEEK_CLOSEST);

                }

                if(!fromUser){
                    if(mediaPlayer.isPlaying()) {

                        for (VideoServiceChangeListenerInterface listener : listenerInterfaceList) {
                            listener.mediaPlayerProgressChange(mediaPlayer.getCurrentPosition(), mediaPlayer.getDuration());
                        }
                    }

                }




            }


            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }


            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }


        }



        Handler progressHandler = new Handler();

        Runnable run = new Runnable() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            public void run() {

                if (!thread.isInterrupted()) {
                    if (state==VideoPlayerState.PLAYING) {

//                        if (mediaPlayer != null) {
//
//                            int max = mediaPlayer.getDuration();
//                            progressBar.setMax(max);
//
//                            int current= mediaPlayer.getCurrentPosition();
//                            position=current;
//
//                            progressBar.setProgress(current);
//
//
//
//                            if (mediaPlayer.isPlaying()) {
//                                progressHandler.postDelayed(run, 50);
//                            }


//                        }
                    }

                }
            }
        };






}

