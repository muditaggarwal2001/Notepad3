package com.example.mudit.notepad;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mudit.notepad.dummy.DBHelperClass;

public class NewNoteActivity extends AppCompatActivity {
    private long noteid;
    private DBHelperClass dbHelperClass;
    private EditText title;
    private EditText note;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
        title = (EditText)findViewById(R.id.note_title);
        note= (EditText)findViewById(R.id.note_desc);
        dbHelperClass = new DBHelperClass(this);
        dbHelperClass.open();
        noteid=-2;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.note_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save:
                if(noteid==-2) {
                    noteid = dbHelperClass.insertData(title.getText().toString(), note.getText().toString(), null);
                    //Add picpath and imageview for images
                    if(noteid!=-1)
                        Toast.makeText(NewNoteActivity.this, "Note Saved Successfully", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(NewNoteActivity.this, "Not enough memory", Toast.LENGTH_LONG).show();
                }
                break;
        }
        finish();
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelperClass.close();
    }
}
