package mdp20126376.mdpcw02.cookbook.model;

import android.icu.text.CollationKey;
import android.icu.text.Collator;
import android.os.Build;

import androidx.annotation.RequiresApi;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;



/**
 * the list to store the songs
 */
public class VideoCollector {
    private static final ArrayList<Video> videoArrayList = new ArrayList<>();//歌曲数据

    public static void addVideo(Video video){

        videoArrayList.add(video);


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static ArrayList<Video> getVideosList(){

        Collections.sort(videoArrayList, new Comparator<Video>() {
            final Collator collator = Collator.getInstance();

            @Override
            public int compare(Video video1, Video video2) {
                String a = video1.getTitle();
                String b = video2.getTitle();
                CollationKey key1 = collator.getCollationKey(a);
                CollationKey key2 = collator.getCollationKey(b);

                return key1.compareTo(key2);
            }
        });
        return videoArrayList;
    }


}
