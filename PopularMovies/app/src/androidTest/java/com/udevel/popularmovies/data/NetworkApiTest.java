package com.udevel.popularmovies.data;

import android.test.AndroidTestCase;
import android.util.Log;

import com.udevel.popularmovies.data.network.NetworkApi;
import com.udevel.popularmovies.data.network.api.DiscoverMovieResult;
import com.udevel.popularmovies.data.network.api.MovieDetailInfoResult;
import com.udevel.popularmovies.data.network.api.ReviewsResult;
import com.udevel.popularmovies.data.network.api.TrailersResult;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by benny on 7/12/2015.
 */
public class NetworkApiTest extends AndroidTestCase {
    private static final String TAG = NetworkApiTest.class.getSimpleName();

    public void setUp() throws Exception {
        super.setUp();

    }

    public void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGetMoviesByPopularity() throws Exception {
        final CountDownLatch signal = new CountDownLatch(1);

        NetworkApi.getMoviesByPopularity(1, new Callback<DiscoverMovieResult>() {
            @Override
            public void success(DiscoverMovieResult discoverMovieResult, Response response) {
                assertNotNull(response);
                assertNotNull(discoverMovieResult);
                assertTrue("Response has no movie info!", discoverMovieResult.getResults().size() > 0);
                Log.d("NetworkApiTest", "movieResult:" + discoverMovieResult.getResults().size());
                signal.countDown();
            }

            @Override
            public void failure(RetrofitError error) {
                assertFalse(true);
                signal.countDown();
            }
        });
        signal.await(30, TimeUnit.SECONDS);
        assertTrue(true);
    }

    public void testGetMoviesByRating() throws Exception {
        final CountDownLatch signal = new CountDownLatch(1);

        NetworkApi.getMoviesByRating(1, 200, new Callback<DiscoverMovieResult>() {
            @Override
            public void success(DiscoverMovieResult discoverMovieResult, Response response) {
                assertNotNull(response);
                assertNotNull(discoverMovieResult);
                assertTrue("Response has no movie info!", discoverMovieResult.getResults().size() > 0);
                Log.d("NetworkApiTest", "movieResult:" + discoverMovieResult.getResults().size());
                signal.countDown();
            }

            @Override
            public void failure(RetrofitError error) {
                assertFalse(true);
                signal.countDown();
            }
        });
        signal.await(30, TimeUnit.SECONDS);
        assertTrue(true);
    }

    public void testGetMovieById() throws Exception {
        final CountDownLatch signal = new CountDownLatch(1);
        final int targetMovieId = 87101;

        NetworkApi.getMovieById(targetMovieId, new Callback<MovieDetailInfoResult>() {
            @Override
            public void success(MovieDetailInfoResult movieDetailInfoResult, Response response) {
                assertNotNull(response);
                assertNotNull(movieDetailInfoResult);
                MovieDetailInfoResult.Trailers trailers = movieDetailInfoResult.getTrailers();
                assertNotNull(trailers);
                List<MovieDetailInfoResult.Youtube> youtubes = trailers.getYoutube();
                assertNotNull(youtubes);
                assertTrue(youtubes.size() > 0);
                assertEquals("Response has wrong movie returned!", targetMovieId, movieDetailInfoResult.getId().intValue());
                signal.countDown();
            }

            @Override
            public void failure(RetrofitError error) {
                assertFalse(true);
                signal.countDown();
            }
        });
        signal.await(30, TimeUnit.SECONDS);
        assertTrue(true);
    }

    public void testGetMovieTrailers() throws Exception {
        final CountDownLatch signal = new CountDownLatch(1);
        final int targetMovieId = 87101;

        NetworkApi.getMovieTrailers(targetMovieId, new Callback<TrailersResult>() {

            @Override
            public void success(TrailersResult trailersResult, Response response) {
                assertNotNull(trailersResult);
                assertNotNull(trailersResult.getResults());
                assertTrue(trailersResult.getResults().size() > 1);
                signal.countDown();
            }

            @Override
            public void failure(RetrofitError error) {
                assertFalse(true);
                signal.countDown();
            }
        });
        signal.await(30, TimeUnit.SECONDS);
        assertTrue(true);
    }

    public void testGetMovieReviews() throws Exception {
        final CountDownLatch signal = new CountDownLatch(1);
        final int targetMovieId = 119450;

        NetworkApi.getMovieReviews(targetMovieId, new Callback<ReviewsResult>() {

            @Override
            public void success(ReviewsResult reviewsResult, Response response) {
                assertNotNull(reviewsResult);
                assertNotNull(reviewsResult.getResults());
                assertTrue(reviewsResult.getResults().size() > 1);
                signal.countDown();
            }

            @Override
            public void failure(RetrofitError error) {
                assertFalse(true);
                signal.countDown();
            }
        });
        signal.await(30, TimeUnit.SECONDS);
        assertTrue(true);
    }
}