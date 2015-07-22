package com.udevel.popularmovies;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.udevel.popularmovies.data.local.entity.Movie;

public class PosterFullscreenActivity extends AppCompatActivity {
    public static final String ARG_KEY_POSTER_PATH = "ARG_KEY_POSTER_PATH";
    private Uri posterUri;
    private ImageView iv_poster;
    private ProgressWheel pw_main;
    private View fl_root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (getIntent() != null) {
            String posterPath = getIntent().getStringExtra(ARG_KEY_POSTER_PATH);
            if (posterPath != null) {
                posterUri = Uri.parse(Movie.BASE_URL_FOR_IMAGE).buildUpon().appendPath(Movie.FULLSIZE_IMAGE_WIDTH).appendEncodedPath(posterPath).build();
            }
        }
        setupViews();
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadPoster();
    }

    private void loadPoster() {
        pw_main.spin();
        Glide.with(this)
                .load(posterUri).asBitmap()
                .error(R.drawable.ic_image_error)
                .listener(new RequestListener<Uri, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, Uri model, Target<Bitmap> target, boolean isFirstResource) {
                        pw_main.stopSpinning();

                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Uri model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        pw_main.stopSpinning();
                        Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
                            public void onGenerated(Palette p) {
                                int darkVibrantColor = p.getVibrantColor(getResources().getColor(R.color.primary));
                                fl_root.setBackgroundColor(darkVibrantColor);
                            }
                        });
                        return false;
                    }
                })
                .fitCenter()
                .animate(R.anim.fade_in_rise)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(iv_poster);
    }

    private void setupViews() {
        setContentView(R.layout.activity_poster_fullscreen);
        fl_root = findViewById(R.id.fl_root);
        iv_poster = ((ImageView) findViewById(R.id.iv_poster));
        pw_main = ((ProgressWheel) findViewById(R.id.pw_main));
    }
}
