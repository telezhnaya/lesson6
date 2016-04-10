package com.csc.telezhnaya.todo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.csc.telezhnaya.todo.MyContentProvider.ENTRIES_URI;
import static com.csc.telezhnaya.todo.TaskTable.*;

public class MainActivity extends AppCompatActivity {
    private final TaskManager manager = TaskManager.INSTANCE;

    @Bind(R.id.list)
    ListView listView;
    @Bind(R.id.new_task)
    EditText newTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);
        ButterKnife.bind(this);

        CursorAdapter adapter = new CursorAdapter(this, managedQuery(ENTRIES_URI, null, null, null, TaskManager.DB_ORDER), true) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                return LayoutInflater.from(parent.getContext()).inflate(R.layout.task, parent, false);
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                TextView textView = (TextView) view.findViewById(R.id.task);
                CheckBox checkBox = (CheckBox) view.findViewById(R.id.task_done);
                RatingBar star = (RatingBar) view.findViewById(R.id.star);

                textView.setText(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TEXT)));
                boolean done = cursor.getInt(cursor.getColumnIndex(COLUMN_STATUS)) == 1;
                checkBox.setChecked(done);
                star.setRating(cursor.getInt(cursor.getColumnIndex(COLUMN_STARRED)));
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(TaskTable._ID));
                view.setTag(id);

                if (done) {
                    textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                } else {
                    textView.setPaintFlags(textView.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
                }
            }
        };

        listView.setAdapter(adapter);

        manager.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        manager.unbind();
    }

    public void onAddClick(View view) {
        String text = newTask.getText().toString();
        if (!text.isEmpty()) {
            newTask.setText("");
            manager.addTask(text);
        }
    }

    public void onTaskChanged(View view) {
        View parent = (View) view.getParent();
        TextView textView = (TextView) parent.findViewById(R.id.task);
        CheckBox checkBox = (CheckBox) parent.findViewById(R.id.task_done);
        RatingBar star = (RatingBar) parent.findViewById(R.id.star);
        manager.updateTask((int) parent.getTag(), textView.getText().toString(), checkBox.isChecked(), star.getRating() > 0);
    }

    public void onTextClick(View view) {
        View parent = (View) view.getParent();
        final int position = (int) parent.getTag();
        TextView textView = (TextView) parent.findViewById(R.id.task);
        CheckBox checkBox = (CheckBox) parent.findViewById(R.id.task_done);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        final View dialogView = this.getLayoutInflater().inflate(R.layout.edit_task, null);
        dialogBuilder.setView(dialogView);
        EditText editText = (EditText) dialogView.findViewById(R.id.edit_task);
        editText.setText(textView.getText().toString());
        CheckBox done = (CheckBox) dialogView.findViewById(R.id.task_done);
        done.setChecked(checkBox.isChecked());

        dialogBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                EditText editText = (EditText) dialogView.findViewById(R.id.edit_task);
                CheckBox done = (CheckBox) dialogView.findViewById(R.id.task_done);
                RatingBar star = (RatingBar) dialogView.findViewById(R.id.star);

                String newText = editText.getText().toString();
                if (newText.isEmpty()) {
                    Toast.makeText(getApplicationContext(), R.string.write_smth, Toast.LENGTH_SHORT).show();
                } else {
                    manager.updateTask(position, newText, done.isChecked(), star.getRating() > 0);
                }
            }
        });

        dialogBuilder.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });

        dialogBuilder.create().show();
    }
}
