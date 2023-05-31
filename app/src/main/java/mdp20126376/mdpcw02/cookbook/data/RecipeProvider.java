package mdp20126376.mdpcw02.cookbook.data;

import android.content.ContentProvider;

import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;


public class RecipeProvider extends ContentProvider {

    private static final int RECIPE = 1;
    private static final int RECIPE_ITEM = 0;
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private RecipeDbHelper mOpenHelper;

    private static final SQLiteQueryBuilder sRecipesQueryBuilder;

    static{
        sRecipesQueryBuilder = new SQLiteQueryBuilder();
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new RecipeDbHelper(getContext());
        return true;
    }




    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {

        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {

            case RECIPE: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                       RecipeContract.RecipeEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            case RECIPE_ITEM:{
                retCursor = mOpenHelper.getReadableDatabase().query(
                        RecipeContract.RecipeEntry.TABLE_NAME,
                        projection,
                        RecipeContract.RecipeEntry.RECIPE_TITLE +"= ?",
                        new String[]{uri.getPathSegments().get(1)},
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case RECIPE:

            case RECIPE_ITEM: {
                long _id = db.insert(RecipeContract.RecipeEntry.TABLE_NAME, null, values);
                System.out.println(_id);
                if ( _id > 0 ) {
                    returnUri = RecipeContract.RecipeEntry.buildRecipeUri(_id);
                    }
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    /*
     *
     *  update function
     *
     * */

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // Student: This is a lot like the delete function.  We return the number of rows impacted
        // by the update.
        //return 0;
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case RECIPE:
                rowsUpdated = db.update(RecipeContract.RecipeEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case RECIPE_ITEM:
                String id= uri.getPathSegments().get(1);
                rowsUpdated = db.update(
                        RecipeContract.RecipeEntry.TABLE_NAME,values,RecipeContract.RecipeEntry.RECIPE_TITLE +"= ?", new String[]{id});
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }



     @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

         final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
         final int match = sUriMatcher.match(uri);
         int rowsDeleted;
         switch (match) {
             case RECIPE:
                 rowsDeleted = db.delete(
                        RecipeContract.RecipeEntry.TABLE_NAME, selection, selectionArgs);
                 break;
             case RECIPE_ITEM:

                 String id= uri.getPathSegments().get(1);
                 System.out.println(id);
                 rowsDeleted = db.delete(
                         RecipeContract.RecipeEntry.TABLE_NAME,RecipeContract.RecipeEntry.RECIPE_TITLE +"= ?", new String[]{id});
                 break;
             default:
                 throw new UnsupportedOperationException("Unknown uri: " + uri);
         }

         if (selection == null || rowsDeleted != 0) {
             getContext().getContentResolver().notifyChange(uri, null);

         }
         return rowsDeleted;
    }



    static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = RecipeContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, "recipe", RECIPE);
        matcher.addURI(authority, "recipe/#", RECIPE_ITEM);

        return matcher;
    }

    @Override
    public String getType(Uri uri) {

        final int match = sUriMatcher.match(uri);

        switch (match) {

            case RECIPE:
                return RecipeContract.RecipeEntry.CONTENT_TYPE;

            case RECIPE_ITEM:
                return RecipeContract.RecipeEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    // insert



    @Override
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}