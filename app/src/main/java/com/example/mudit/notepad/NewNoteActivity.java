package com.example.mudit.notepad;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.mudit.notepad.dummy.DBHelperClass;
import com.example.mudit.notepad.dummy.DummyContent;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class NewNoteActivity extends AppCompatActivity {
    private long noteid;
    private DBHelperClass dbHelperClass;
    private EditText title;
    private EditText note;
    private String prevTitle, prevnote, previmage;
    private DummyContent.Note temp;
    public static final String ARG_ITEM_ID = "item_id";
    private static final int PICK_IMAGE=1;
    private static final int CAMERA_PIC_REQUEST =100;
    private Bitmap bitmap;
    private Uri fileUri;
    public static final int MEDIA_TYPE_IMAGE = 1;
    ImageView imageview2;
    Button photo;
    Uri imageuri;

    private static final String IMAGE_DIRECTORY_NAME = "Hello Camera";
    String imgDecodableString;
    File file;
    String encodedString;
    String fileName;
    String fileLength;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
        imageview2 = (ImageView)findViewById(R.id.imageView2);
        photo= (Button)findViewById(R.id.photo);
        title = (EditText)findViewById(R.id.note_title);
        note= (EditText)findViewById(R.id.note_desc);
        noteid=-2;
        prevnote="";
        prevTitle="";
        previmage="";
        if(getIntent().hasExtra(ARG_ITEM_ID))
        {
            noteid = Integer.parseInt(getIntent().getStringExtra(ARG_ITEM_ID));
            temp = DummyContent.ITEM_MAP.get(Long.toString(noteid));
            prevnote=temp.getDetails();
            prevTitle=temp.toString();
            previmage = temp.getPicpath();
            setToPrevText();
        }

        dbHelperClass = new DBHelperClass(this);
        dbHelperClass.open();

    }

    public void setToPrevText()
    {
        title.setText(prevTitle);
        note.setText(prevnote);
        if (!previmage.equals("")) {

            byte[] imgbytes = Base64.decode(previmage,Base64.NO_WRAP);
            Bitmap bmp = BitmapFactory.decodeByteArray(imgbytes,0,imgbytes.length);
            imageview2.setImageBitmap(bmp);
        }
        else
        {
            Toast.makeText(this.getBaseContext(), "In toast", Toast.LENGTH_SHORT).show();
        }
            //imageview2.setVisibility(View.INVISIBLE);
            //bitmap=null;
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
                    if(bitmap != null ) {
                        String picpath = getStringImage(bitmap);
                        noteid = dbHelperClass.insertData(title.getText().toString(), note.getText().toString(), picpath);
                    }
                    else
                    {
                        noteid = dbHelperClass.insertData(title.getText().toString(), note.getText().toString(), "");
                    }
                    //Add picpath and imageview for images
                    if(noteid!=-1)
                        Toast.makeText(NewNoteActivity.this, "Note Saved Successfully", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(NewNoteActivity.this, "Not enough memory", Toast.LENGTH_LONG).show();
                }
                else
                {
                    if(dbHelperClass.updateData(Integer.parseInt(Long.toString(noteid)),title.getText().toString(), note.getText().toString(), ""))
                    {
                        Toast.makeText(NewNoteActivity.this, "Note Saved Successfully", Toast.LENGTH_LONG).show();
                    }
                    else
                        Toast.makeText(NewNoteActivity.this, "Note update failed", Toast.LENGTH_LONG).show();

                }
                break;

            case R.id.undo: setToPrevText();
                break;
            case R.id.photo: openGallery();

                /**photo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openGallery();
                    }
                });*/


        }
        return true;
    }
    public void openGallery(){
        Intent gallery=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }
    /*protected void onActivityResult(int requestCode, int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode== RESULT_OK && requestCode== PICK_IMAGE){
            imageuri= data.getData();
            imageview2.setImageURI(imageuri);
        }
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            // Get the cursor

            Cursor cursor = getContentResolver().query(filePath,
                    filePathColumn, null, null, null);
            // Move to first row
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            imgDecodableString = cursor.getString(columnIndex);
            cursor.close();


            file = new File(imgDecodableString);
            try {
                BitmapFactory.Options options = new BitmapFactory.Options();
                // downsizing image as it throws OutOfMemory Exception for larger
                // images

                options.inSampleSize = 8;
                bitmap = BitmapFactory.decodeFile(imgDecodableString,options);
                //Getting the Bitmap from Gallery
                //bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                imageview2.setImageBitmap(bitmap);

            }
            catch(NullPointerException e)
            {
                e.printStackTrace();
            }
            /* catch (IOException e) {
                e.printStackTrace();
            }*/
        }
        else if(requestCode == CAMERA_PIC_REQUEST)
        {
            //Uri filePath = data.getData();
            if (resultCode == RESULT_OK) {
                try {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    // downsizing image as it throws OutOfMemory Exception for larger
                    // images

                    options.inSampleSize = 8;

                    bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
                            options);
                    //bitmap = (Bitmap)data.getExtras().get("data");
                    imageview2.setImageBitmap(bitmap);

                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
            else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    /**
     * Here we store the file url as it will be null after returning from camera
     * app
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on scren orientation
        // changes
        outState.putParcelable("file_uri", fileUri);
    }

    /*
     * Here we restore the fileUri again
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.NO_WRAP);
        return encodedImage;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelperClass.close();
    }
}


/*package com.example.mudit.notepad;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.mudit.notepad.dummy.DBHelperClass;
import com.example.mudit.notepad.dummy.DummyContent;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class NewNoteActivity extends AppCompatActivity {
    private long noteid;
    private DBHelperClass dbHelperClass;
    private EditText title;
    private EditText note;
    private String prevTitle, prevnote;
    private DummyContent.Note temp;
    public static final String ARG_ITEM_ID = "item_id";
    private static final int PICK_IMAGE=100;
    private static final int CAMERA_PIC_REQUEST =1;
    private Bitmap bitmap;
    private Uri fileUri;
    public static final int MEDIA_TYPE_IMAGE = 1;
    ImageView imageview2;
    Button photo;
    Uri imageuri;

    private static final String IMAGE_DIRECTORY_NAME = "Hello Camera";
    String imgDecodableString;
    File file;
    String encodedString;
    String fileName;
    String fileLength;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
        imageview2 = (ImageView)findViewById(R.id.photo);
        photo= (Button)findViewById(R.id.photo);
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
            case R.id.photo: openGallery();

                photo.setOnClickListener(new View.OnClickListener() {
                    @Override
                   public void onClick(View view) {
                        openGallery();
                    }
               });


        }
        return true;
    }
    public void openGallery(){
        Intent gallery=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }
    /*protected void onActivityResult(int requestCode, int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode== RESULT_OK && requestCode== PICK_IMAGE){
            imageuri= data.getData();
            imageview2.setImageURI(imageuri);
        }
    }*/

   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            // Get the cursor

            Cursor cursor = getContentResolver().query(filePath,
                    filePathColumn, null, null, null);
            // Move to first row
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            imgDecodableString = cursor.getString(columnIndex);
            cursor.close();


            file = new File(imgDecodableString);
            try {
                BitmapFactory.Options options = new BitmapFactory.Options();
                // downsizing image as it throws OutOfMemory Exception for larger
                // images

                options.inSampleSize = 8;
                bitmap = BitmapFactory.decodeFile(imgDecodableString,options);
                //Getting the Bitmap from Gallery
                //bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                imageview2.setImageBitmap(bitmap);

            }
            catch(NullPointerException e)
            {
                e.printStackTrace();
            }
            /* catch (IOException e) {
                e.printStackTrace();
            }*/
        /*}
      /*  else if(requestCode == CAMERA_PIC_REQUEST)
        {
            //Uri filePath = data.getData();
            if (resultCode == RESULT_OK) {
                try {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    // downsizing image as it throws OutOfMemory Exception for larger
                    // images

                    options.inSampleSize = 8;

                                        bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
                            options);
                    //bitmap = (Bitmap)data.getExtras().get("data");
                    imageview2.setImageBitmap(bitmap);

                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
            else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelperClass.close();
    }
}*/
