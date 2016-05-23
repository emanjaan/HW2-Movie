package com.monkey.tasteful.movie;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;
import com.monkey.tasteful.movie.data.MovieDbHelper;
import com.monkey.tasteful.movie.data.Contract;


import java.util.Map;
import java.util.Set;

/**
 * Created by emanjaan on 5/22/16.
 */
public class DataBaseTest extends AndroidTestCase
{
    Context context;

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        context = getContext();
    }


    public void testInsertMovie()
    {
        MovieDbHelper helper = new MovieDbHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        //create a contentvalues with information

        ContentValues cv = movieValues();
        Cursor cursor = null;

        try{
            long locId = db.insert(Contract.movieEntry.TABLE_NAME, null, cv);

            assertTrue(locId != -1);

            cursor = db.query(Contract.movieEntry.TABLE_NAME, null, null, null, null, null, null);

            assertTrue("No records", cursor.moveToFirst());

            assertTrue(validateCurrentRecord("Records do not match", cursor, cv));

            Cursor movieCursor = db.query(
                    Contract.movieEntry.TABLE_NAME,  // Table to Query
                    null, // leaving "columns" null just returns all the columns.
                    null, // cols for "where" clause
                    null, // values for "where" clause
                    null, // columns to group by
                    null, // columns to filter by row groups
                    null  // sort order
            );

            assertTrue(validateCurrentRecord("testInsertReadDb weatherEntry failed to validate",
                    movieCursor, movieValues()));


        }

        catch(Exception e)
        {
            Log.e("test", e.getMessage());
        }

        finally
        {
            db.delete(Contract.movieEntry.TABLE_NAME, null, null);
            if(cursor != null) cursor.close();
            if(db != null)db.close();
        }


    }



    static ContentValues movieValues()
    {
        ContentValues cv = new ContentValues();
        cv.put(Contract.movieEntry.COLUMN_TITLE, "Santa hourse");
        cv.put(Contract.movieEntry.COLUMN_SUMMARY, "About Santa, it was a nice movie! ");
        cv.put(Contract.movieEntry.COLUMN_DATE, " 1419033600L");
        return cv;
    }



    static boolean validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues)
    {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet)
        {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            if (idx == -1) return false;
            String expectedValue = entry.getValue().toString();
            String retrievedValue = valueCursor.getString(idx);
            if (!retrievedValue.equals(expectedValue)) return false;

        }
        return true;
    }


}
