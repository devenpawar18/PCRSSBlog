package com.pcrssblog.api.service;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.pcrssblog.util.cache.ImageCache;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Download Images for each article using AsyncTask and put them into LRUCache for quick access
 */
public class ImageDownloaderService extends AsyncTask<String, Void, Bitmap> {
    private final WeakReference<ImageView> mImageViewReference;
    private ImageCache mCache;
    private String mURL;

    public ImageDownloaderService(final ImageView pImageView, final ImageCache pImageCache) {
        this.mImageViewReference = new WeakReference<ImageView>(pImageView);
        this.mCache = pImageCache;
    }

    /**
     * Download Article Bitmap on background thread
     *
     * @param params
     * @return
     */
    @Override
    protected Bitmap doInBackground(String... params) {
        return downloadBitmap(params[0]);
    }

    /**
     * Put Bitmap to cache and update UI
     *
     * @param bitmap
     */
    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (isCancelled()) {
            bitmap = null;
        }

        ImageView imageView = this.mImageViewReference.get();
        if (imageView != null && this.mURL != null) {
            if (bitmap != null) {
                this.mCache.put(this.mURL, bitmap);
                imageView.setImageBitmap(bitmap);
            }
        }
    }

    /**
     * @param pURL
     * @return Article Bitmap Image
     */
    private Bitmap downloadBitmap(String pURL) {
        this.mURL = pURL;
        HttpURLConnection urlConnection = null;
        try {
            URL uri = new URL(pURL);
            urlConnection = (HttpURLConnection) uri.openConnection();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != HttpURLConnection.HTTP_OK) {
                return null;
            }

            InputStream inputStream = urlConnection.getInputStream();
            if (inputStream != null) {
                return BitmapFactory.decodeStream(inputStream);
            }
        } catch (Exception e) {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return null;
    }
}