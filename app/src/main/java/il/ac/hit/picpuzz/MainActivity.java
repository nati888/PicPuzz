package il.ac.hit.picpuzz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    Button b_easy;
    Button b_normal;
    Button b_hard;
    Button b_extreme;
    //Button b_custom;
    Intent intent;

    String mCurrentPhotoPath;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE = 2;
    private static final int REQUEST_PERMISSION_READ_EXTERNAL_STORAGE = 3;
    private static final int REQUEST_IMAGE_GALLERY = 4;

    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b_easy = findViewById(R.id.b_easy);
        b_normal = findViewById(R.id.b_normal);
        b_hard = findViewById(R.id.b_hard);
        b_extreme = findViewById(R.id.b_extreme);
        //b_custom = findViewById(R.id.b_custom);

        prefs = getSharedPreferences("APPLICATION_PREFERENCE", Context.MODE_PRIVATE);
        showProgress();

        b_easy.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { showLevelImages(1); }});
        b_normal.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { showLevelImages(2); }});
        b_hard.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { showLevelImages(3); }});
        b_extreme.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { showLevelImages(4); }});
    }

    @Override
    protected void onResume() {
        super.onResume();
        showProgress();
    }

    public void showProgress() {
        int easy = 0;
        int normal = 0;
        int hard = 0;
        int extreme = 0;
        String currentFile;

        for(int i = 1; i < 101; i++) {
            if(i < 10)
                currentFile = "00" + i + ".jpg"; //"001.jpg"
            else
                currentFile = "0" + i + ".jpg";

            if(prefs.getBoolean(currentFile, false)) {
                if(i >= 1 && i <= 25)
                    easy++;
                else if(i >= 26 && i <= 50)
                    normal++;
                else if(i >= 51 && i <= 75)
                    hard++;
                else if(i >= 76 && i <= 100)
                    extreme++;
            }
        }

        b_easy.setText(getResources().getText(R.string.level_easy) + " (" + easy + "/25)");
        b_normal.setText(getResources().getText(R.string.level_normal) + " (" + normal + "/25)");
        b_hard.setText(getResources().getText(R.string.level_hard) + " ("+ hard + "/25)");
        b_extreme.setText(getResources().getText(R.string.level_extreme) + " ("+ extreme + "/25)");
        //b_custom.setText(getResources().getText(R.string.level_custom));
    }

    public void showLevelImages(int level) {
        intent = new Intent(getApplicationContext(), GridActivity.class);
        intent.putExtra("level", level);
        startActivity(intent);
    }

    public void onImageFromCameraClick(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;

            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }

            if (photoFile != null) {
                Uri photoUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".fileprovider", photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // permission not granted, initiate request
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE);
        } else {
            // Create an image file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";
            File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File image = File.createTempFile(imageFileName, ".jpg", storageDir);
            mCurrentPhotoPath = image.getAbsolutePath(); // save this to use in the intent
            return image;
        }

        return null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                onImageFromCameraClick(new View(this));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Intent intent = new Intent(this, PuzzleActivity.class);
            intent.putExtra("mCurrentPhotoPath", mCurrentPhotoPath);
            startActivity(intent);
        }

        if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == RESULT_OK) {
            Uri uri = data.getData();

            Intent intent = new Intent(this, PuzzleActivity.class);
            intent.putExtra("mCurrentPhotoUri", uri.toString());
            startActivity(intent);
        }
    }

    public void onImageFromGalleryClick(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION_READ_EXTERNAL_STORAGE);
        } else {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_IMAGE_GALLERY);
        }
    }
}