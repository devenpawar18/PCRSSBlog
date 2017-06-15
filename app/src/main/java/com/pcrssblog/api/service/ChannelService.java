package com.pcrssblog.api.service;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import com.pcrssblog.R;
import com.pcrssblog.api.model.Channel;
import com.pcrssblog.api.parser.ChannelParser;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * ChannelService - To make asynchronous call on background thread and fetch Channel (with article list) using AsyncTask.
 */

public class ChannelService extends AsyncTask<String, Void, ChannelService.Result> {
    private ChannelCallback mChannelCallback;
    private Context mContext;

    /**
     * Result containing Channel OR Error String
     */
    static class Result {
        public Channel mChannel;
        public String mMessage;

        /**
         * Success - Return Channel
         *
         * @param pChannel
         */
        Result(Channel pChannel) {
            this.mChannel = pChannel;
        }

        /**
         * Failure Return Error Message
         *
         * @param pMessage
         */
        Result(String pMessage) {
            this.mMessage = pMessage;
        }
    }

    /**
     * ChannelService callbacks
     */
    public interface ChannelCallback {
        void success(Channel pChannel);

        void failure(String pMessage);

        void downloadComplete();

        void downloadCancel();
    }

    public ChannelService(final ChannelCallback pChannelCallback, final Context pContext) {
        this.mChannelCallback = pChannelCallback;
        this.mContext = pContext;
    }

    @Override
    protected void onPreExecute() {
        if (this.mChannelCallback != null) {
            // Cancel AsyncTask in case of no connectivity
            final NetworkInfo networkInfo = this.getActiveNetworkInfo();
            if (networkInfo == null || !networkInfo.isConnected() || (networkInfo.getType() != ConnectivityManager.TYPE_WIFI && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
                // If no connectivity, cancel task and update Callback with null data.
                this.mChannelCallback.failure(this.mContext.getResources().getString(R.string.view_article_list_fetching_failure_no_network));
                this.mChannelCallback.downloadCancel();
                cancel(true);
            }
        }
    }

    /**
     * Fetch Articles on background thread
     *
     * @param urls
     * @return Channel (Success) OR Error String (Failure)
     */
    @Override
    protected ChannelService.Result doInBackground(String... urls) {
        try {
            return new Result(fetchChannel(urls[0]));
        } catch (IOException pIOException) {
            return new Result(this.mContext.getResources().getString(R.string.view_article_list_fetching_failure_refresh));
        } catch (XmlPullParserException pXmlPullParserException) {
            return new Result(this.mContext.getResources().getString(R.string.view_article_list_fetching_failure_parsing));
        }
    }

    /**
     * Submit success/failure callbacks to update UI
     *
     * @param pResult
     */
    @Override
    protected void onPostExecute(ChannelService.Result pResult) {
        if (pResult.mChannel != null) {
            this.mChannelCallback.success(pResult.mChannel);
        } else if (pResult.mMessage != null) {
            this.mChannelCallback.failure(pResult.mMessage);
        }
        this.mChannelCallback.downloadComplete();
    }

    /**
     * @param pURL
     * @return Channel
     * @throws XmlPullParserException
     * @throws IOException
     */
    private Channel fetchChannel(String pURL) throws XmlPullParserException, IOException {
        InputStream stream = null;
        ChannelParser channelParser = new ChannelParser();
        Channel channel;
        try {
            stream = downloadArticles(pURL);
            channel = channelParser.parse(stream);
        } finally {
            if (stream != null) {
                stream.close();
            }
        }

        return channel;
    }

    /**
     * Download Articles using HttpURLConnection
     *
     * @param pURL
     * @return Input Stream
     * @throws IOException
     */
    private InputStream downloadArticles(final String pURL) throws IOException {
        final URL url = new URL(pURL);
        final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout((int) TimeUnit.SECONDS.toMillis(10)); /*10 Seconds*/
        conn.setConnectTimeout((int) TimeUnit.SECONDS.toMillis(15)); /*15 Seconds*/
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.connect();
        return conn.getInputStream();
    }

    /**
     * @return NetworkInfo to check connectivity
     */
    public NetworkInfo getActiveNetworkInfo() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo;
    }
}
