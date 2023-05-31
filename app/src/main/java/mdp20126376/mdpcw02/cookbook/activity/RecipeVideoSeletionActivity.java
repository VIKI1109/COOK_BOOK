package mdp20126376.mdpcw02.cookbook.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentResolver;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ListView;
import android.widget.Toast;

import mdp20126376.mdpcw02.cookbook.model.BaseActivity;
import mdp20126376.mdpcw02.cookbook.R;
import mdp20126376.mdpcw02.cookbook.adapter.VideoListAdapter;
import mdp20126376.mdpcw02.cookbook.model.Video;
import mdp20126376.mdpcw02.cookbook.model.VideoCollector;

public class RecipeVideoSeletionActivity extends BaseActivity {


    int video_total_number=0;
    private Toolbar toolbar = null;
    private ListView videoListVIew = null;
    private VideoListAdapter listViewAdapter;//the song list view adapter
    private String dataPath;
    private String title;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_video_seletion);
        loadWidget();
        load_Video_data();
        videoListViewAdapter(); //set the song view list adapter

        videoListVIew.setOnItemClickListener((parent, view, position, id) -> {

            Video video = VideoCollector.getVideosList().get(position);
            dataPath =  video.getDataPath();
            title= video.getTitle();
            RecipeAddActivity.videoPath=dataPath;
            RecipeAddActivity.videoTitle=title;
            finish();

        });
    }
    @Override
    protected void onStart() {
        super.onStart();

    }
    @Override
    public void onRestart() {
        super.onRestart();

    }

    @Override
    protected void onResume() {

        super.onResume();

    }

    /**
     * Activity is stopping
     */
    @Override
    protected void onPause() {

        super.onPause();
    }

    /**
     * when the activity is ready to back to the background, the OnStop() will be called.
     */
    @Override
    protected void onStop() {

        super.onStop();
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onDestroy() {
        VideoCollector.getVideosList().clear();
        super.onDestroy();

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void videoListViewAdapter() {
        listViewAdapter = new VideoListAdapter(RecipeVideoSeletionActivity.this, R.layout.video_list, VideoCollector.getVideosList());
        videoListVIew.setAdapter(listViewAdapter);
    }

    public void loadWidget() {
        toolbar = findViewById(R.id.toolbar_activity_display);
        videoListVIew = findViewById(R.id.songList);

    }

    private void load_Video_data() {
        ContentResolver contentResolver = getContentResolver();
        try (Cursor cursor = contentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                null, null, null, null)) {
            if (cursor != null) {
                while (cursor.moveToNext()) {

                        String dataPath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                        String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
                        long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));

                        String name= getVideoName(title);

                        Video video = new Video(
                                name,
                                dataPath
                        );

                        if(size<=10*1024*1024){
                        VideoCollector.addVideo(video);
                        video_total_number++;
                        }
                    }
                }
            }
         catch (Exception e) {
            e.printStackTrace();
        }
        try (Cursor cursor = contentResolver.query(MediaStore.Video.Media.INTERNAL_CONTENT_URI,
                null, null, null, null)) {
            if (cursor != null) {
                while (cursor.moveToNext()) {

                    String dataPath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                    String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
                    long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));

                    String name= getVideoName(title);

                    Video video = new Video(
                            name,
                            dataPath
                    );

                    if(size<=10*1024*1024){
                        VideoCollector.addVideo(video);
                        video_total_number++;
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        if (video_total_number == 0) {
            Toast.makeText(RecipeVideoSeletionActivity.this, "No video in this phone", Toast.LENGTH_SHORT).show();
        }

    }

    private String getVideoName(String videoPath) {

        String[] path= videoPath.split("/");

        return path[path.length-1];
    }


    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Toast.makeText(RecipeVideoSeletionActivity.this, "Orientation Portrait", Toast.LENGTH_SHORT).show();
        }

        else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(RecipeVideoSeletionActivity.this, "Orientation Landscape", Toast.LENGTH_SHORT).show();
        }



    }


}