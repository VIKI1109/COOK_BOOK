package mdp20126376.mdpcw02.cookbook.activity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import mdp20126376.mdpcw02.cookbook.model.BaseActivity;
import mdp20126376.mdpcw02.cookbook.R;
import mdp20126376.mdpcw02.cookbook.data.RecipeContract;

//descending
public class RecipeAddActivity extends BaseActivity {

    private EditText etTitle, etInstruction,etListOfIngredient,etRating;
    private TextView tvVideoName;
    private Button btnSave,btnUploadVideo;
//    private RatingBar ratingBar;
    float rate=0;

    private Context context;
    private int REQUEST_VIDEO_CODE=0;
    private Uri videoUri=null;
    public static String videoPath=null;
    public static String videoTitle=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_add);

        final ContentResolver resolver = this.getContentResolver();
        etTitle = findViewById(R.id.etTitle);
        etInstruction = findViewById(R.id.etInstruction);
        etListOfIngredient = findViewById(R.id.etListOfIngredient);
        tvVideoName=findViewById(R.id.tvVideoName);
        etRating=findViewById(R.id.etRating);
        btnSave = findViewById(R.id.btnSave);
        btnUploadVideo = findViewById(R.id.btnUploadVideo);
        context = RecipeAddActivity.this;

        etRating.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 3);
                        etRating.setText(s);
                        etRating.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    etRating.setText(s);
                    etRating.setSelection(2);
                }

                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (s.toString().charAt(1) != '.') {
                        etRating.setText(s.subSequence(0, 1));
                        etRating.setSelection(1);
                    }
                }


            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                if (!s.toString().equals("")) {
                    float num = Float.parseFloat(s.toString());
                    if (num > 5) {
                        String string1 = String.valueOf(5);
                        etRating.setText(string1);
                        Toast.makeText(RecipeAddActivity.this, "The rating should be lower than or equal to 5.", Toast.LENGTH_LONG).show();

                    } else if (num < 0) {
                        String string2 = String.valueOf(0);
                        etRating.setText(string2);
                        Toast.makeText(RecipeAddActivity.this, "The rating should be higher than or equal to 0.", Toast.LENGTH_LONG).show();
                    }
                }
            }


        });


        btnSave.setOnClickListener(v -> {

                String title = etTitle.getText().toString();
                String instruction = etInstruction.getText().toString();
                String listOfIngredient = etListOfIngredient.getText().toString();
                String rating= etRating.getText().toString();


            if("".equals(title.trim())||"".equals(instruction.trim())||"".equals(listOfIngredient.trim())||"".equals(rating.trim())){
                Toast.makeText(RecipeAddActivity.this, "Input all the information", Toast.LENGTH_LONG).show();
            }else {

                if (!"".equals(rating)) {
                    rate = Float.parseFloat(rating);

                    if(rate>5||rate<0){
                        Toast.makeText(RecipeAddActivity.this, "The rating should be in the range of 0 to 5.", Toast.LENGTH_LONG).show();
                    }
                    else{
                        ContentValues values = new ContentValues();
                        values.put("title", title);
                        values.put("rating", rate);
                        values.put("instruction", instruction);
                        values.put("ingredient_list", listOfIngredient);
                        values.put("video_path", videoPath);

                        Uri uri = Uri.parse(RecipeContract.BASE_CONTENT_URI + "/" + RecipeContract.PATH_RECIPE);
                        resolver.insert(uri, values);

                        Intent intent = new Intent(this, RecipeDisplayActivity.class);
                        startActivity(intent);
                    }
                }

            }

        });

        btnUploadVideo.setOnClickListener(view -> {

           // moveTaskToBack(true);
            Intent intent = new Intent(RecipeAddActivity.this,RecipeVideoSeletionActivity.class);
            startActivity(intent);
        });





    }

    /**
     * If Activity is already in the task stack,
     * start Activity again, then the onNewIntent() method will be called at this time.
     * Jump to MainActivity—> onNewIntent—> onRestart—> onStart—>onResume
     * @param intent the intent
     */
    @Override
    protected  void  onNewIntent(Intent intent) {

        super.onNewIntent(intent);


    }

    private  void  processExtraData(){



    }

    @Override
    public void onRestart() {
        super.onRestart();

        tvVideoName.setText(videoTitle);


    }

    @Override
    protected void onStart() {
        super.onStart();
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

    /**
     * Destroy the activity.
     * if the onDestroy() is called after the OnBackPressed(), then the activities will be destroy and the service won't be stopped.
     * else the activity and service will be destroyed both.
     */
    @Override
    protected void onDestroy() {

        super.onDestroy();

    }

    /**
     * User return to the activity
     * the activity is ready for being used by users.
     */
    @Override
    protected void onResume() {

        super.onResume();

    }

    /**
     * The method used to change the orientation of the interface
     * @param newConfig the configuration of the phone
     */
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {

            Toast.makeText(RecipeAddActivity.this, "Orientation Portrait", Toast.LENGTH_SHORT).show();
        }

        else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(RecipeAddActivity.this, "Orientation Landscape", Toast.LENGTH_SHORT).show();
        }


    }

}