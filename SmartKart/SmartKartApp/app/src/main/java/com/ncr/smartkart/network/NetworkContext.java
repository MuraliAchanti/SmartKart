package com.ncr.smartkart.network;

import android.util.Log;

import com.ncr.smartkart.SmartKart;
import com.ncr.smartkart.utils.PersistentDeviceStorage;
import com.ncr.smartkart.utils.Utils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkContext {

    private static final String TAG = "NetworkContext";
    private static final Interceptor REWRITE_RESPONSE_INTERCEPTOR = chain -> {
        Response originalResponse = chain.proceed(chain.request());
        String cacheControl = originalResponse.header("Cache-Control");

        if (cacheControl == null || cacheControl.contains("no-store") || cacheControl.contains("no-cache") ||
                cacheControl.contains("must-revalidate") || cacheControl.contains("max-age=0")) {
            return originalResponse.newBuilder()
                    .header("Cache-Control", "public, max-age=" + 10)
                    .build();
        } else {
            return originalResponse;
        }
    };
    private static final Interceptor OFFLINE_INTERCEPTOR = chain -> {
        Request request = chain.request();

        if (!Utils.hasActiveInternetConnection(SmartKart.getInstance().getApplicationContext())) {
            Log.d(TAG, "rewriting request");

            int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
            request = request.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                    .build();
        }

        return chain.proceed(request);
    };
    public static APISource apiSource;
    private Map<String, Object> services;
    private Retrofit retrofit;
    private File httpCacheDirectory = new File(SmartKart.getInstance().getApplicationContext().getCacheDir(), "responses");
    private int cacheSize = 10 * 1024 * 1024; // 10 MiB
    private Cache cache = new Cache(httpCacheDirectory, cacheSize);

    private static String getStoreUrl() {
        PersistentDeviceStorage persistentDeviceStorage = PersistentDeviceStorage.getInstance(SmartKart.getInstance().getApplicationContext());
        return persistentDeviceStorage.getStoreUrl();
    }

    /**
     * Private Constructor to make it singleton
     */
    public void initialise() {
        Log.d(TAG, "initialise() called");
        services = new HashMap<>();

        String BASE_URL = getStoreUrl();

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .addNetworkInterceptor(REWRITE_RESPONSE_INTERCEPTOR)
                .readTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(OFFLINE_INTERCEPTOR)
                .addInterceptor(httpLoggingInterceptor)
                .cache(cache);

        OkHttpClient client = builder.build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        apiSource = getService(APISource.class);
    }

    /**
     * This method will be used for getting the same instance everytime
     * To avoid creating new instance every time
     *
     * @param clazz will be added if it doesn't have it in hash map
     * @param <T>   Object
     * @return <T>
     */
    private synchronized <T> T getService(Class<T> clazz) {
        String key = clazz.getName();
        if (services.containsKey(key))
            return (T) services.get(key);
        else {
            Log.i("aa", "getService: ");
            T newClass = retrofit.create(clazz);
            services.put(key, newClass);
            return newClass;
        }
    }

}
