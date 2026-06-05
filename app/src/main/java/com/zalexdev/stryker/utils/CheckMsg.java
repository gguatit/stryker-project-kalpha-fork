package com.zalexdev.stryker.utils;

import android.annotation.SuppressLint;
import com.zalexdev.stryker.utils.StrykerTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * This class is used to check for messages
 */
public class CheckMsg extends StrykerTask<String, String> {
    public CheckMsg() {
    }

    @SuppressLint("WrongThread")
    @Override
    protected String doInBackground() {
        return "No messages";
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }

    @Override
    protected void onProgress(String value) {

    }


}
