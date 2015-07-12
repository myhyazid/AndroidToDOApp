package com.myhyazid.workstodo.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.myhyazid.workstodo.MainActivity;
import com.myhyazid.workstodo.db.TaskContract;

public class TaskDBHelper extends SQLiteOpenHelper {

    private static TaskDBHelper mInstance = null;
    private final Context context;

    public static TaskDBHelper getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (mInstance == null) {
            mInstance = new TaskDBHelper(context.getApplicationContext());
        }
        return mInstance;
    }

    public TaskDBHelper(Context context) {
        super(context, TaskContract.DB_NAME, null, TaskContract.DB_VERSION);
        this.context = context;
    }
    @Override
    public void onCreate(SQLiteDatabase sqlDB) {
        String sqlQuery =
                String.format("CREATE TABLE %s (" +
                                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                "%s TEXT)", TaskContract.TABLE,
                        TaskContract.Columns.TASK);

        Log.d("TaskDBHelper", "Query to form table: " + sqlQuery);
        sqlDB.execSQL(sqlQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqlDB, int i, int i2) {
        sqlDB.execSQL("DROP TABLE IF EXISTS "+TaskContract.TABLE);
        onCreate(sqlDB);
    }
    public int getProfilesCount() {
        String countQuery = "SELECT  * FROM "+TaskContract.TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

}