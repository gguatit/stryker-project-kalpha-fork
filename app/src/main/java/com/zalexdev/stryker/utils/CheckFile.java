package com.zalexdev.stryker.utils;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import com.zalexdev.stryker.utils.StrykerTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

/**
 * Checks if a file exists on the device
 */
public class CheckFile extends StrykerTask<String, Boolean> {


    public String path;

    public CheckFile(String path1) {
        path = path1;
    }

    @SuppressLint("WrongThread")
    @Override
    protected Boolean doInBackground() {
        String line;
        boolean result = false;
        Logger logger = new Logger();

        try {
            logger.writeLine("Checking file.. "+path,1);

            Process process = Runtime.getRuntime().exec("su -mm");
            OutputStream stdin = process.getOutputStream();
            InputStream stderr = process.getErrorStream();
            InputStream stdout = process.getInputStream();
            stdin.write(("[ -f " + path + " ] && echo true || echo false" + '\n').getBytes());
            stdin.write(("exit\n").getBytes());
            stdin.flush();
            stdin.close();
            BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
            while ((line = br.readLine()) != null) {
                logger.writeLine(line,2);
                if (line.contains("true")) {
                    result = true;
                }
            }
            br.close();
            br = new BufferedReader(new InputStreamReader(stderr));
            while ((line = br.readLine()) != null) {
                publish(line);
                logger.writeLine(line,3);
            }
            br.close();
            process.waitFor(60, TimeUnit.SECONDS);
            process.destroy();
        } catch (IOException e) {
            Log.d("Debug: ", "An IOException was caught: " + e.getMessage());
        } catch (InterruptedException ex) {
            Log.d("Debug: ", "An InterruptedException was caught: " + ex.getMessage());
        }

        return result;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
    }

    @Override
    protected void onProgress(String value) {

    }


}
