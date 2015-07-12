package com.myhyazid.workstodo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class TaskDBHelperBC extends SQLiteOpenHelper {

    private static TaskDBHelperBC mInstance = null;
    //private final Context ctx;

    public static TaskDBHelperBC getInstance(Context ctx) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (mInstance == null) {
            mInstance = new TaskDBHelperBC(ctx.getApplicationContext());
        }
        return mInstance;
    }

    private TaskDBHelperBC(Context ctx) {
        super(ctx, TaskContract.DB_NAME, null, TaskContract.DB_VERSION);
        //this.context = context;
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

}