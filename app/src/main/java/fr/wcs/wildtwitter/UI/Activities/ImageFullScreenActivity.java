package fr.wcs.wildtwitter.UI.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.ImageView;

import fr.wcs.wildtwitter.GlideApp;
import fr.wcs.wildtwitter.R;

public class ImageFullScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_full_screen);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Intent intent = getIntent();
        String imageUrl = intent.getStringExtra("ImageUrl");

        ImageView imageViewFullScreen = findViewById(R.id.imageViewFullScreen);
        GlideApp.with(this)
                .load(imageUrl)
                .into(imageViewFullScreen);
    }
}
