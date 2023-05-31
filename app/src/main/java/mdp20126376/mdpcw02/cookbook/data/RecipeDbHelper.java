package mdp20126376.mdpcw02.cookbook.data;



import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



/**
 * Manages a local database for recipes data.
 */
public class RecipeDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;

    static final String DATABASE_NAME = "recipes.db";

    public RecipeDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {


        // recipe table
        final String SQL_CREATE_RECIPE_TABLE = "CREATE TABLE " + RecipeContract.RecipeEntry.TABLE_NAME + " (" +
                RecipeContract.RecipeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                RecipeContract.RecipeEntry.RECIPE_TITLE + " TEXT NOT NULL, " +
                RecipeContract.RecipeEntry.RATING + " FLOAT NOT NULL, " +
                RecipeContract.RecipeEntry.RECIPE_INSTRUCTION + " TEXT NOT NULL, " +
                RecipeContract.RecipeEntry.RECIPE_INGREDIENT_LIST + " TEXT NOT NULL, " +
                RecipeContract.RecipeEntry.RECIPE_VIDEO_PATH + " TEXT " +
                " );";


        sqLiteDatabase.execSQL(SQL_CREATE_RECIPE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RecipeContract.RecipeEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}

