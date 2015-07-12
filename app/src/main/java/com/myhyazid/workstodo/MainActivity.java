package com.myhyazid.workstodo;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.myhyazid.workstodo.db.TaskContract;
import com.myhyazid.workstodo.db.TaskDBHelper;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SQLiteDatabase sqlDB = new TaskDBHelper(this).getWritableDatabase();
        Cursor cursor = sqlDB.query(TaskContract.TABLE,
                new String[]{TaskContract.Columns.TASK},
                null, null, null, null, null);

        cursor.moveToFirst();
        while (cursor.moveToNext()) {
            Log.d("MainActivity cursor",
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    TaskContract.Columns.TASK)));
        }
        updateUI();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    private TaskDBHelper helper;

    private void updateUI() {
        ListView listView = (ListView) findViewById(R.id.task_list);
        TaskDBHelper helper = new TaskDBHelper(MainActivity.this);
        SQLiteDatabase sqlDB = helper.getReadableDatabase();
        Cursor cursor = sqlDB.query(TaskContract.TABLE,
                new String[]{TaskContract.Columns._ID,
                        TaskContract.Columns.TASK},
                null, null, null, null, null);

        SimpleCursorAdapter listAdapter = new SimpleCursorAdapter(
                this,
                R.layout.task_view,
                cursor,
                new String[]{TaskContract.Columns.TASK},
                new int[]{R.id.taskTextView},
                0
        );
        listView.setAdapter(listAdapter);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_add_task:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Add a task");
                builder.setMessage("What do you want to do?");
                final EditText inputField = new EditText(this);
                inputField.setTextColor(Color.parseColor("#00C853"));
                builder.setView(inputField);
                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String task = inputField.getText().toString();
                        Log.d("MainActivity", task);

                        TaskDBHelper helper = new TaskDBHelper(MainActivity.this);
                        SQLiteDatabase db = helper.getWritableDatabase();
                        ContentValues values = new ContentValues();

                        values.clear();
                        values.put(TaskContract.Columns.TASK, task);

                        db.insertWithOnConflict(TaskContract.TABLE, null, values,
                                SQLiteDatabase.CONFLICT_IGNORE);

                        updateUI();
                    }
                });
                builder.setNegativeButton("Cancel", null);

                builder.create().show();
                return true;


            default:
                return false;
        }
    }

    public void onDoneButtonClick(View view) {
        View v = (View) view.getParent();
        TextView taskTextView = (TextView) v.findViewById(R.id.taskTextView);
        String task = taskTextView.getText().toString();

        String sql = String.format("DELETE FROM %s WHERE %s = '%s'",
                TaskContract.TABLE,
                TaskContract.Columns.TASK,
                task);


        TaskDBHelper helper = new TaskDBHelper(MainActivity.this);
        SQLiteDatabase sqlDB = helper.getWritableDatabase();
        sqlDB.execSQL(sql);
        updateUI();
    }
}


