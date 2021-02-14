package il.ac.hit.picpuzz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import java.io.IOException;
import java.util.Arrays;

public class GridActivity extends AppCompatActivity {

    private GridView grid;
    private int piecesNum = 9;
    private int rows = 3;
    private int minPicIndex = 1;
    private int maxPicIndex = 26;
    private String[] files;
    private AssetManager am;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);

        grid = findViewById(R.id.grid);
        prefs = getSharedPreferences("APPLICATION_PREFERENCE", Context.MODE_PRIVATE);
        am = getAssets();
        refreshMusic(false);

        try {
            files = am.list("img");
        } catch (IOException e) {
            e.printStackTrace();
        }

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), PuzzleActivity.class);
                intent.putExtra("assetName", files[position % files.length]);
                intent.putExtra("pieces", piecesNum);
                intent.putExtra("rows", rows);
                intent.putExtra("columns", piecesNum/rows);
                startActivity(intent);
            }
        });
    }

    void prepareImages() throws IOException {
        Intent intent = getIntent();
        int level = intent.getIntExtra("level", 1);
        switch (level) {
            case 1:
                minPicIndex = 1;
                maxPicIndex = 26;
                break;
            case 2:
                minPicIndex = 26;
                maxPicIndex = 51;
                piecesNum = 12;
                rows = 4;
                break;
            case 3:
                minPicIndex = 51;
                maxPicIndex = 76;
                piecesNum = 16;
                rows = 4;
                break;
            case 4:
                minPicIndex = 76;
                maxPicIndex = 101;
                piecesNum = 20;
                rows = 5;
                break;

            default:
                minPicIndex = 1;
                maxPicIndex = 2;
                break;
        }

        grid.setAdapter(new ImageAdapter(this, minPicIndex, maxPicIndex));
        files = Arrays.copyOfRange(am.list("img"),minPicIndex - 1,maxPicIndex - 1);
    }

    @Override
    protected void onPause() {
        super.onPause();
        refreshMusic(true);
    }

    @Override
    protected void onResume() {
        super.onResume();

        try {
            prepareImages();
        } catch (IOException e) {
            e.printStackTrace();
        }

        refreshMusic(false);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        refreshMusic(false);
    }

    public void refreshMusic(boolean forceShutdown) {
        if (prefs.getBoolean("musicOff", false) || forceShutdown)
            MusicManager.pause();
        else
            MusicManager.start(this, MusicManager.MUSIC_MENU);
    }
}