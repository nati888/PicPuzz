package il.ac.hit.picpuzz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity2 extends AppCompatActivity {

    Button b_easy;
    Button b_normal;
    Button b_hard;
    Button b_extreme;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        b_easy = findViewById(R.id.b_easy);
        b_normal = findViewById(R.id.b_normal);
        b_hard = findViewById(R.id.b_hard);
        b_extreme = findViewById(R.id.b_extreme);

        b_easy.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { showLevelImages(1); }});
        b_normal.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { showLevelImages(2); }});
        b_hard.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { showLevelImages(3); }});
        b_extreme.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { showLevelImages(4); }});
    }

    public void showLevelImages(int level) {
        intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("level", level);
        startActivity(intent);
    }
}