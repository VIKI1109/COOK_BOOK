package mdp20126376.mdpcw02.cookbook.data;


import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;


/**
 * Defines table and column names for the recipes database.
 */
public class RecipeContract {
    // The "Content authority" is a name for the entire content provider, similar to the relationship
    // between a domain name and its website.  A convenient string to use for the content authority
    // is the package name for the app, which is guaranteed to be unique on the device.
    public static final String CONTENT_AUTHORITY = "mdp20126376.mdpcw02.cookbook";//.app

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://mdp20126376.mdpcw02.cookbook");



    // Possible paths (appended to base content URI for possible URI's) - tables name
    public static final String PATH_RECIPE = "recipe";

    public static final String PATH_RECIPE_ITEM = "recipe/#";
    /* Inner class that defines the table contents of the location table */



    /* Inner class that defines the table contents of the Recipe table */
    public static final class RecipeEntry implements BaseColumns {

        public static final String TABLE_NAME = "recipe";

        public static final String RECIPE_TITLE = "title";
        public static final String RATING = "rating";
        public static final String RECIPE_INSTRUCTION = "instruction";
        public static final String RECIPE_INGREDIENT_LIST = "ingredient_list";
        public static final String RECIPE_VIDEO_PATH = "video_path";

        // UriMatcher
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_RECIPE).build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RECIPE;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RECIPE +"/#";


        public static Uri buildRecipeUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static String getRecipeFromUri(Uri uri){
            return uri.getPathSegments().get(0);
        }

    }
}
