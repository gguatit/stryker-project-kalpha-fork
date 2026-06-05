package com.zalexdev.stryker.utils;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import com.zalexdev.stryker.utils.StrykerTask;
import android.os.Environment;

public class DownloadFile extends StrykerTask<String, Boolean> {


    public Context context;
    public String urlDownload;
    public String filename;
    public DownloadManager manager;

    public DownloadFile(DownloadManager m, Context cont, String url, String name) {
        context = cont;
        urlDownload = url;
        filename = name;
        manager = m;
    }

    @SuppressLint({"WrongThread", "Range"})
    @Override
    protected Boolean doInBackground() {
        Logger logger = new Logger();
        logger.writeLine("Downloading file.."+urlDownload,1);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(urlDownload));
        request.setDescription("Please, wait...");
        request.setTitle("Downloading files...");
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename);
        boolean b = false;
        final long downloadId = manager.enqueue(request);
        boolean downloading = true;
        int timeout = 0;
        while (downloading && timeout < 120) {
            DownloadManager.Query q = new DownloadManager.Query();
            q.setFilterById(downloadId);
            Cursor cursor = manager.query(q);
            int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
            if (status == DownloadManager.STATUS_SUCCESSFUL) {
                downloading = false;
                b = true;
            } else if (status == DownloadManager.STATUS_FAILED) {
                downloading = false;
            }
            cursor.close();
            if (downloading) {
                try { Thread.sleep(500); } catch (InterruptedException e) { break; }
                timeout++;
            }
        }
        return b;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
    }

    @Override
    protected void onProgress(String value) {

    }


}
