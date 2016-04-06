package com.truongkhanhduy.ipmathdemo;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.truongkhanhduy.apiservice.OCRServiceAPI;
import com.truongkhanhduy.model.IP;

import java.io.ByteArrayOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CameraActivity extends AppCompatActivity{


    private final int RESPONSE_OK = 200;
    private final int IMAGE_PICKER_REQUEST = 1;
    private final int REQUEST_ID_IMAGE_CAPTURE = 100;

    EditText txtResult;
    Button btnGalery,btnConvert,btnCamera;
    ImageView imgPic;

    IP ipResult;
    Intent intent;
    private String apiKey;
    private String langCode;
    private String fileName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Camera");
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.color.camera_theme));
        setContentView(R.layout.activity_camera);
        addControls();
        addEvents();

    }

    private void addEvents() {
        btnGalery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI), IMAGE_PICKER_REQUEST);
            }
        });
        btnConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processConvert();
            }
        });
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });
        ImageView btnReset= (ImageView) findViewById(R.id.btnReset);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                finish();
                overridePendingTransition(0, 0);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
    }

    private void takePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        this.startActivityForResult(intent, REQUEST_ID_IMAGE_CAPTURE);
    }


    private void processConvert() {
        apiKey = "QdgJmSnWjK";
        langCode = "en";

        // Checking are all fields set
        if (fileName != null && !apiKey.equals("") && !langCode.equals("")) {
            final ProgressDialog dialog = ProgressDialog.show( CameraActivity.this, "Loading ...", "Converting to text.", true, false);
            final Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    final OCRServiceAPI apiClient = new OCRServiceAPI(apiKey);
                    apiClient.convertToText(langCode, fileName);

                    // Doing UI related code in UI thread
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();

                            // Showing response dialog
                            final AlertDialog.Builder alert = new AlertDialog.Builder(CameraActivity.this);
                            alert.setMessage("Convert success!");
                            txtResult.setText(apiClient.getResponseText());
                            alert.setPositiveButton(
                                    "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                        }
                                    });

                            // Setting dialog title related from response code
                            if (apiClient.getResponseCode() == RESPONSE_OK) {
                                alert.setTitle("Success");
                            } else {
                                alert.setTitle("Faild");
                            }

                            alert.show();
                        }
                    });
                }
            });
            thread.start();
        } else {
            Toast.makeText(CameraActivity.this, "All data are required.", Toast.LENGTH_SHORT).show();
        }
    }
    private boolean ValidateInsert(String str)
    {
        Pattern pattern = Pattern.compile("^\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}/\\d{1,3}");
        Matcher matcher=pattern.matcher(str);
        Pattern pattern2 = Pattern.compile("^\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}");
        Matcher matcher2=pattern2.matcher(str);

        return matcher.matches()||matcher2.matches();
    }

    private void addControls() {
        txtResult= (EditText) findViewById(R.id.txtResult);
        btnGalery = (Button) findViewById(R.id.btnGalery);
        btnConvert = (Button) findViewById(R.id.btnConvert);
        btnCamera= (Button) findViewById(R.id.btnCamera);
        imgPic= (ImageView) findViewById(R.id.imgPic);
        registerForContextMenu(txtResult);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_context_result, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        String strIP=txtResult.getText().toString().trim().substring(1);
        switch(item.getItemId()){
            case R.id.mnu_edit:
                break;
            case R.id.mnu_info:
                intent=new Intent(
                        CameraActivity.this,
                        IPInfoActivity.class
                );
                intent.putExtra("INFO",strIP);
                startActivity(intent);
                break;
            case R.id.mnu_subnet:
                intent=new Intent(
                        CameraActivity.this,
                        SubnetActivity.class
                );
                intent.putExtra("SUBNET",strIP);
                startActivity(intent);
                break;
            case R.id.mnu_ipv6:
                intent=new Intent(
                        CameraActivity.this,
                        ConvertIPv6Activity.class
                );
                intent.putExtra("IPV6",strIP);
                startActivity(intent);
                break;
        }
        return super.onContextItemSelected(item);
    }


    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_PICKER_REQUEST && resultCode == RESULT_OK) {
            fileName = getRealPathFromURI(data.getData());
            //Bitmap photo = (Bitmap) data.getExtras().get("data");
            imgPic.setImageBitmap(BitmapFactory.decodeFile(fileName));
            //Toast.makeText(CameraActivity.this, "OK!", Toast.LENGTH_SHORT).show();
        }
        if(requestCode==REQUEST_ID_IMAGE_CAPTURE && resultCode==RESULT_OK){
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imgPic.setImageBitmap(photo);
            Uri tempUri = getImageUri(getApplicationContext(), photo);

            fileName = getRealPathFromURI(tempUri);
        }
    }

    /*
     * Returns image real path.
     */
    private String getRealPathFromURI(final Uri contentUri) {
        final String[] proj = { MediaStore.Images.Media.DATA };
        final Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    /*
     * Cuts selected file name from real path to show in screen.
     */
    private String getStringNameFromRealPath(final String bucketName) {
        return bucketName.lastIndexOf('/') > 0 ? bucketName.substring(bucketName.lastIndexOf('/') + 1) : bucketName;
    }
}
