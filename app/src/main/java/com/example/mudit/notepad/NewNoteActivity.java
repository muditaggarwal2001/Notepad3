package com.example.mudit.notepad;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import com.example.mudit.notepad.dummy.DBHelperClass;
import com.example.mudit.notepad.dummy.DummyContent;

public class NewNoteActivity extends AppCompatActivity {
    private long noteid;
    private DBHelperClass dbHelperClass;
    private EditText title;
    private EditText note;
    private String prevTitle, prevnote;
    private DummyContent.Note temp;
    public static final String ARG_ITEM_ID = "item_id";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
        title = (EditText)findViewById(R.id.note_title);
        note= (EditText)findViewById(R.id.note_desc);
        noteid=-2;
        prevnote="";
        prevTitle="";
        if(getIntent().hasExtra(ARG_ITEM_ID))
        {
            noteid = Integer.parseInt(getIntent().getStringExtra(ARG_ITEM_ID));
            temp = DummyContent.ITEM_MAP.get(Long.toString(noteid));
            prevnote=temp.getDetails();
            prevTitle=temp.toString();
            setToPrevText();
        }

        dbHelperClass = new DBHelperClass(this);
        dbHelperClass.open();

    }

    public void setToPrevText()
    {
        title.setText(prevTitle);
        note.setText(prevnote);
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
                else
                {
                    if(dbHelperClass.updateData(Integer.parseInt(Long.toString(noteid)),title.getText().toString(), note.getText().toString(), null))
                    {
                        Toast.makeText(NewNoteActivity.this, "Note Saved Successfully", Toast.LENGTH_LONG).show();
                    }
                    else
                        Toast.makeText(NewNoteActivity.this, "Note update failed", Toast.LENGTH_LONG).show();

                }
                break;

            case R.id.undo: setToPrevText();
                break;

        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelperClass.close();
    }
}
