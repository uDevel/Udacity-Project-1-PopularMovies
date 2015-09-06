package com.udevel.popularmovies.activity;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.udevel.popularmovies.R;
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
    protected void onStop() {
        super.onStop();
        Glide.clear(iv_poster);
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadPoster();
    }

    private void loadPoster() {
        iv_poster.setVisibility(View.INVISIBLE);

        pw_main.spin();
        Glide.with(this)
                .load(posterUri)
                .asBitmap()
                .error(R.drawable.ic_image_error)
                .listener(new RequestListener<Uri, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, Uri model, Target<Bitmap> target, boolean isFirstResource) {
                        pw_main.stopSpinning();
                        onBackPressed();
                        Toast.makeText(pw_main.getContext(), R.string.msg_error_data_connection_error, Toast.LENGTH_LONG).show();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Uri model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        pw_main.stopSpinning();
                        Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
                            public void onGenerated(Palette p) {
                                int backgroundColor = p.getMutedColor(getResources().getColor(R.color.dark_gray));
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    reveal();
                                }
                                fl_root.setBackgroundColor(backgroundColor);
                                iv_poster.setVisibility(View.VISIBLE);
                            }
                        });
                        return false;
                    }
                })
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(iv_poster);
    }

    private void setupViews() {
        setContentView(R.layout.activity_poster_fullscreen);
        fl_root = findViewById(R.id.fl_root);
        iv_poster = ((ImageView) findViewById(R.id.iv_poster));
        pw_main = ((ProgressWheel) findViewById(R.id.pw_main));
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void reveal() {
        View targetView = fl_root;
        int cx = (targetView.getLeft() + targetView.getRight()) / 2;
        int cy = (targetView.getTop() + targetView.getBottom()) / 2;

        int finalRadius = Math.max(targetView.getWidth(), targetView.getHeight());
        Animator anim = ViewAnimationUtils.createCircularReveal(targetView, cx, cy, 0, finalRadius);
        anim.start();
    }
}
