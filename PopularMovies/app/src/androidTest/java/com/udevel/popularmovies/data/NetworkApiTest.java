package com.udevel.popularmovies.data;

import android.test.AndroidTestCase;
import android.util.Log;

import com.udevel.popularmovies.data.network.NetworkApi;
import com.udevel.popularmovies.data.network.api.DiscoverMovieResult;
import com.udevel.popularmovies.data.network.api.MovieDetailInfo;

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

        NetworkApi.getMovieById(targetMovieId, new Callback<MovieDetailInfo>() {
            @Override
            public void success(MovieDetailInfo movieDetailInfo, Response response) {
                assertNotNull(response);
                assertNotNull(movieDetailInfo);
                assertEquals("Response has wrong movie returned!", targetMovieId, movieDetailInfo.getId().intValue());
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