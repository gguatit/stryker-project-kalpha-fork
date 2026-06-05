package com.zalexdev.stryker.utils;

import static android.content.ContentValues.TAG;
import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

import android.annotation.SuppressLint;
import android.content.Context;
import com.zalexdev.stryker.utils.StrykerTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 * This class checks for updates and returns the result
 */
public class CheckUpdates extends StrykerTask<String, String> {

    public Context context;
    public Core core;
    public CheckUpdates(Context cont) {
        context = cont;
        core = new Core(context);
    }
    @SuppressLint("WrongThread")
    @Override
    protected String doInBackground() {
        return "No updates";
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }

    @Override
    protected void onProgress(String value) {

    }


}
