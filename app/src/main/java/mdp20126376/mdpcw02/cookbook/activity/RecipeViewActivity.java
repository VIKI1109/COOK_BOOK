package mdp20126376.mdpcw02.cookbook.activity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.io.File;

import mdp20126376.mdpcw02.cookbook.model.BaseActivity;
import mdp20126376.mdpcw02.cookbook.R;
import mdp20126376.mdpcw02.cookbook.data.RecipeContract;

public class RecipeViewActivity extends BaseActivity {

    private ContentResolver resolver;
    private Context context;
    float rating;
    private EditText etRecipeRating;
    String recipe_title, list_of_ingredient, recipe_instruction;
    String videoPath;
    Float changedRating;
    int recipe_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_view);
        resolver = getContentResolver();

        final Intent intent = getIntent();
        recipe_id= intent.getIntExtra(RecipeContract.RecipeEntry._ID,0);
        rating = intent.getFloatExtra("rating", 0);
        recipe_title = intent.getStringExtra("recipe_title");
        list_of_ingredient = intent.getStringExtra("list_of_ingredient");
        recipe_instruction = intent.getStringExtra("recipe_instruction");

        Button submitButton = findViewById(R.id.submitButton);
        Button playButton = findViewById(R.id.playButton);
        TextView tvRecipeInstruction = findViewById(R.id.tvRecipeInstruction);
        TextView tvRecipeTitle = findViewById(R.id.tvRecipeTitle);
        TextView tvRecipeListOfIngredient = findViewById(R.id.tvRecipeListOfIngredient);
        etRecipeRating = findViewById(R.id.etRecipeRating);
        TextView tvRacipeRating = findViewById(R.id.tvRecipeRating);


        tvRecipeTitle.setText(recipe_title);
        tvRecipeInstruction.setText(recipe_instruction);
        tvRecipeListOfIngredient.setText(list_of_ingredient);
        tvRacipeRating.setText(String.valueOf(rating));


        etRecipeRating.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 3);
                        etRecipeRating.setText(s);
                        etRecipeRating.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    etRecipeRating.setText(s);
                    etRecipeRating.setSelection(2);
                }

                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (s.toString().charAt(1) != '.') {
                        etRecipeRating.setText(s.subSequence(0, 1));
                        etRecipeRating.setSelection(1);
                    }
                }


            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!s.toString().equals("")) {
                    float num = Float.parseFloat(s.toString());
                    if (num > 5) {
                        String string1 = String.valueOf(5);
                        etRecipeRating.setText(string1);
                        Toast.makeText(RecipeViewActivity.this, "The rating should be lower than or equal to 5.", Toast.LENGTH_LONG).show();

                    } else if (num < 0) {
                        String string2 = String.valueOf(0);
                        etRecipeRating.setText(string2);
                        Toast.makeText(RecipeViewActivity.this, "The rating should be higher than or equal to 0.", Toast.LENGTH_LONG).show();
                    }
                }
            }


        });


        submitButton.setOnClickListener(v -> {


            if (!"".equals(etRecipeRating.getText().toString())) {

                if(Float.parseFloat(etRecipeRating.getText().toString())>5||Float.parseFloat(etRecipeRating.getText().toString())<0){
                    Toast.makeText(RecipeViewActivity.this, "The rating should be in the range of 0 to 5.", Toast.LENGTH_SHORT).show();
                }else{
                changedRating = Float.parseFloat(etRecipeRating.getText().toString());
                update(changedRating);
                    Intent intent1 = new Intent(RecipeViewActivity.this, RecipeDisplayActivity.class);
                    startActivity(intent1);
                }
            }
            else {
                Toast.makeText(RecipeViewActivity.this, "You have not updated the rate", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(RecipeViewActivity.this, RecipeDisplayActivity.class);
                startActivity(intent1);
            }


        });

        playButton.setOnClickListener(v -> {


            if(query(recipe_title)) {
                File file=new File(videoPath);
                if(file.exists()) {
                    //moveTaskToBack(true);
                    Intent intent1 = new Intent(RecipeViewActivity.this, RecipeVideoPlayActivity.class);
                    intent1.putExtra("videoPath", videoPath);
                    intent1.putExtra("title", recipe_title);
                    startActivity(intent1);
                }
                else{
                    Toast.makeText(RecipeViewActivity.this, "The video has been deleted or location changed", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(RecipeViewActivity.this, "No video with the \n" +
                        "recipe", Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void update(Float rate) {

        ContentValues updateValues = new ContentValues();
        updateValues.put(RecipeContract.RecipeEntry.RATING, rate);
        Uri newUri = Uri.parse(RecipeContract.BASE_CONTENT_URI + "/" + RecipeContract.PATH_RECIPE_ITEM);
        System.out.println(recipe_title);
        resolver.update(newUri, updateValues, RecipeContract.RecipeEntry._ID + "=?", new String[]{String.valueOf(recipe_id)});
        Log.i("database", "update");

    }

    public boolean query(String title) {

        Uri newUri = Uri.parse(RecipeContract.BASE_CONTENT_URI + "/" + RecipeContract.PATH_RECIPE_ITEM);
        Cursor recipeCurse = resolver.query(newUri, null, RecipeContract.RecipeEntry._ID+ "=?", new String[]{String.valueOf(recipe_id)}, null);
        if (recipeCurse != null) {
            while (recipeCurse.moveToNext()) {
                videoPath = recipeCurse.getString(recipeCurse.getColumnIndexOrThrow(RecipeContract.RecipeEntry.RECIPE_VIDEO_PATH));

                Log.d("Database", title + " " + videoPath + " ");
            }

            recipeCurse.close();
            if(videoPath==null){
                return false;
            }
        }
        Log.i("database", "query");
        return true;

    }


    @Override
    protected void onNewIntent(Intent intent) {

        super.onNewIntent(intent);



    }



    @Override
    public void onRestart() {
        super.onRestart();
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

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Toast.makeText(RecipeViewActivity.this, "Orientation Portrait", Toast.LENGTH_SHORT).show();
        }

        else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(RecipeViewActivity.this, "Orientation Landscape", Toast.LENGTH_SHORT).show();
        }



    }
}