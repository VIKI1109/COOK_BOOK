package mdp20126376.mdpcw02.cookbook.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import mdp20126376.mdpcw02.cookbook.model.ActivityCollector;
import mdp20126376.mdpcw02.cookbook.model.BaseActivity;
import mdp20126376.mdpcw02.cookbook.R;
import mdp20126376.mdpcw02.cookbook.data.RecipeContract;
import mdp20126376.mdpcw02.cookbook.model.Recipe;
import mdp20126376.mdpcw02.cookbook.service.VideoService;

public class RecipeDisplayActivity extends BaseActivity {

    private ContentResolver resolver;
    TableLayout tabActivity02;
    private List<Recipe> mData = null;
    Boolean refresh=false;
    private final int REQ_READ_EXTERNAL_STORAGE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_display);
        tabActivity02 = findViewById(R.id.tab_activity02);
        tabActivity02.setStretchAllColumns(true);
        Button btnAdd=findViewById(R.id.btnAddRecipe);
        resolver = getContentResolver();
        requestPermission();//dynamic request the permission

        btnAdd.setOnClickListener(view -> {
            Intent intent = new Intent(RecipeDisplayActivity.this,RecipeAddActivity.class);
            startActivity(intent);
        });


        initTable();


    }



    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        refresh=true;
        initTable();
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        refresh=false;
        ActivityCollector.finishAllActivities();//finish all the activities
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    //insert的时候会接着table加
    //table scrollable
    public void initTable(){


        mData = new ArrayList<>();
        Uri newUri = Uri.parse(RecipeContract.BASE_CONTENT_URI+"/" + RecipeContract.PATH_RECIPE);
        Uri newUriForItem = Uri.parse(RecipeContract.BASE_CONTENT_URI+"/" + RecipeContract.PATH_RECIPE_ITEM);
        String[] projection = new String[]{RecipeContract.RecipeEntry._ID,"title","rating","instruction","ingredient_list","video_path"};
        Cursor cursor=resolver.query(newUri,projection,null,null,RecipeContract.RecipeEntry.RATING + " DESC");

        while(cursor.moveToNext())
        {
            String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
            float rating=cursor.getFloat(cursor.getColumnIndexOrThrow("rating"));
            String instruction = cursor.getString(cursor.getColumnIndexOrThrow("instruction"));
            String ingredient_list=cursor.getString(cursor.getColumnIndexOrThrow("ingredient_list"));
            String video_path = cursor.getString(cursor.getColumnIndexOrThrow("video_path"));
            int recipe_id = cursor.getInt(cursor.getColumnIndexOrThrow(RecipeContract.RecipeEntry._ID));
            mData.add(new Recipe(title,recipe_id,rating,instruction,ingredient_list,video_path));
         }

        cursor.close();
        Recipe recipe;
        TableLayout tabActivity02 = findViewById(R.id.tab_activity02);
        tabActivity02.setStretchAllColumns(true);

        if(refresh){
            tabActivity02.removeViews(1, Math.max(0, tabActivity02.getChildCount() - 1));
        }
            for (int row = 0; row<mData.size(); row++){
                TableRow tableRow = new TableRow(RecipeDisplayActivity.this);
                recipe=mData.get(row);

                TextView textView0 = new TextView(RecipeDisplayActivity.this);
                textView0.setText(String.valueOf(recipe.getRating()));
                textView0.setTextSize(18);
                textView0.setBackgroundResource(R.drawable.textview_border);
                textView0.setGravity(Gravity.CENTER);
                tableRow.addView(textView0);


                TextView textView1 = new TextView(RecipeDisplayActivity.this);
                textView1.setText(recipe.getTitle());
                textView1.setTextSize(18);
                textView1.setBackgroundResource(R.drawable.textview_border);
                textView1.setGravity(Gravity.CENTER);
                tableRow.addView(textView1);



                String title =recipe.getTitle();
                float rating =recipe.getRating();
                int recipe_id=recipe.getRecipe_id();
                String list_of_ingredient =recipe.getIngredient_list();
                String recipe_instruction = recipe.getInstruction();

                Button btnForDelete = new Button(RecipeDisplayActivity.this);
                btnForDelete.setText("delete");
                tableRow.addView(btnForDelete);

                tabActivity02.addView(tableRow,new TableLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.MATCH_PARENT
                ));

                btnForDelete.setOnClickListener(view->{

                            AlertDialog.Builder builder = new AlertDialog.Builder(RecipeDisplayActivity.this);
                            builder.setTitle("Recipe Deletion Confirmation");
                            builder.setMessage("Are you sure to delete this recipe?");
                            builder.setPositiveButton("Yes", (dialogInterface, i) -> {
                            resolver.delete(newUriForItem,RecipeContract.RecipeEntry._ID+ "=?", new String[]{String.valueOf(recipe_id)});
                            Log.i("database","delete");
                            tableRow.removeAllViews();
                            });
                            builder.create();
                            builder.show();
                    });

                tableRow.setOnClickListener(v -> {

                    Intent intent = new Intent(RecipeDisplayActivity.this,RecipeViewActivity.class);
                    intent.putExtra("recipe_title",title);
                    intent.putExtra(RecipeContract.RecipeEntry._ID,recipe_id);
                    intent.putExtra("rating",rating);
                    intent.putExtra("list_of_ingredient",list_of_ingredient);
                    intent.putExtra("recipe_instruction",recipe_instruction);
                    startActivity(intent);

                });

            }

            }


    public void requestPermission() {

        if (Build.VERSION.SDK_INT >= 23) {
            int checkReadStoragePermission = ContextCompat.checkSelfPermission(
                    RecipeDisplayActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
            if (checkReadStoragePermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        RecipeDisplayActivity.this, new String[]{
                                Manifest.permission.READ_EXTERNAL_STORAGE
                        }, REQ_READ_EXTERNAL_STORAGE);
            }
        }
    }

    /**
     * A callback after requesting permission from a user
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull final String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(RecipeDisplayActivity.this, "Succeed in applying for read/write storage permission", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(RecipeDisplayActivity.this, "Failed to apply for read/write storage permission", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);



        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Toast.makeText(RecipeDisplayActivity.this, "Orientation Portrait", Toast.LENGTH_SHORT).show();
        }

        else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(RecipeDisplayActivity.this, "Orientation Landscape", Toast.LENGTH_SHORT).show();
        }



    }

    }

