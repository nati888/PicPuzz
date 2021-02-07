package il.ac.hit.picpuzz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Arrays;

public class GridActivity extends AppCompatActivity {
    GridView grid;
    int piecesNum = 9;
    int rows = 3;
    int columns = 3;
    String[] files;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);

        AssetManager am = getAssets();
        try {
            grid = findViewById(R.id.grid);
            files = am.list("img");

            Intent intent = getIntent();
            int level = intent.getIntExtra("level", 1);
            switch (level) {
                case 1:
                    grid.setAdapter(new ImageAdapter(this, 1, 26));
                    files = Arrays.copyOfRange(am.list("img"),0,25);
                    break;
                case 2:
                    grid.setAdapter(new ImageAdapter(this, 26, 51));
                    files = Arrays.copyOfRange(am.list("img"),25,52);
                    piecesNum = 12;
                    rows = 4;
                    columns = 3;
                    break;
                case 3:
                    grid.setAdapter(new ImageAdapter(this, 51, 76));
                    files = Arrays.copyOfRange(am.list("img"),50,77);
                    piecesNum = 16;
                    rows = 4;
                    columns = 4;
                    break;
                case 4:
                    grid.setAdapter(new ImageAdapter(this, 76, 101));
                    files = Arrays.copyOfRange(am.list("img"),75,102);
                    piecesNum = 20;
                    rows = 5;
                    columns = 4;
                    break;

                default:
                    grid.setAdapter(new ImageAdapter(this, 1, 2));
                    files = Arrays.copyOfRange(am.list("img"),0,1);
                    break;
            }

            grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getApplicationContext(), PuzzleActivity.class);
                    intent.putExtra("assetName", files[position % files.length]);
                    intent.putExtra("pieces", piecesNum);
                    intent.putExtra("rows", rows);
                    intent.putExtra("columns", columns);
                    startActivity(intent);
                }
            });
        } catch (IOException e) {
            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT);
        }
    }

}