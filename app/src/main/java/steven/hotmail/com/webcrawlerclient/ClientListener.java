package steven.hotmail.com.webcrawlerclient;

import android.content.Context;
import android.content.Intent;
import android.graphics.Path;
import android.util.Log;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Steven on 10/1/2017.
 */

public class ClientListener {

    // Holds the connected status of the socket
    private boolean mConnected = false;
    private Socket mSocket;
    // Hold the client for this listener
    private Socket mClient;
    // Holds the console output
    private List<String> messageLog;
    // The string ip:port
    private String mAddress;
    private boolean isRunning = false;
    private int queueSize = 0;

    private static ClientListener instance;

    public ClientListener(final Context context, Socket socket, String address)
    {
        instance = this;
        mClient = socket;
        messageLog = new ArrayList<String>();
        mAddress = address;

        socket.on("crawl", new Emitter.Listener()
        {

            @Override
            public void call(Object... args) {
                // Add the ip crawled to the message log
                JSONObject obj = (JSONObject)args[0];
                try {
                    messageLog.add("Crawled " + obj.getString("url"));
                } catch(JSONException e)
                {

                }
            }
        });

        socket.on("disconnect", new Emitter.Listener()
        {

            @Override
            public void call(Object... args) {
                //mClient.close();
            }
        });

        socket.on("start", new Emitter.Listener()
        {

            @Override
            public void call(Object... args) {
                isRunning = true;
            }
        });

        socket.on("stop", new Emitter.Listener()
        {

            @Override
            public void call(Object... args) {
                isRunning = false;
            }
        });

        socket.on("queueSize", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                queueSize = Integer.parseInt(args[0].toString());
            }
        });
        /**
         * Called when the server wishes to update the client, holds
         * information such as crawler state, time sync etc
          */
        socket.on("update", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                // Add the ip crawled to the message log
                JSONObject obj = (JSONObject)args[0];
                try {
                    messageLog.add("Crawled " + obj.getString("url"));
                } catch(JSONException e)
                {

                }
            }
        });
        {

        }

        mClient.connect();
    }

    public void queueWebsite(String website)
    {
        mClient.emit("queue", website);
        if(!mClient.connected()) mClient.connect();
    }

    /**
     * Starts the indexer using the specified website as
     * the root
     */
    public void startIndexing()
    {
        mClient.emit("start");
        if(!mClient.connected())
            mClient.connect();
    }

    /**
     * Stops the indexer, essentially same as the pause function
     */
    public void stopIndexing()
    {
        mClient.emit("stop");
        if(!mClient.connected())
            mClient.connect();
    }

    /**
     * Close the connection to the server
     */
    public void closeConnection()
    {
        if(mClient.connected())
        {
            mClient.disconnect();
        }
    }

    public boolean isRunning()
    {
        return isRunning;
    }

    /**
     * Gets the currently queued urls in the crawler
     * @return
     */
    public int getQueueSize()
    {
        return queueSize;
    }

    /**
     * Get the socket this client listener is
     * listening on
     * @return
     */
    public Socket getListeningSocket()
    {
        return mSocket;
    }

    /**
     * Returns true or false if this client is
     * actively listening on a socket
     * @return
     */
    public boolean isListening()
    {
        return mSocket != null;
    }

    public static ClientListener instance()
    {
        return instance;
    }

    public String getAddress()
    {
        return mAddress;
    }

    public List<String> getMessageLog()
    {
        return messageLog;
    }

}
