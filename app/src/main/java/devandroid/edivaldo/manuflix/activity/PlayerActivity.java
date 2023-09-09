package devandroid.edivaldo.manuflix.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaActionSound;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import devandroid.edivaldo.manuflix.R;

public class PlayerActivity extends AppCompatActivity {

    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        MediaController mediaController = new MediaController(this);

        videoView = findViewById(R.id.videoView);

        videoView.setMediaController(new MediaController(this));
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);

        Uri uri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/manuzinha-9bc0d.appspot.com/o/manu.mp4?alt=media&token=0834affa-1e6f-4000-8b15-69512dc8171a");
        videoView.setVideoURI(uri);
        videoView.start();
    }
}