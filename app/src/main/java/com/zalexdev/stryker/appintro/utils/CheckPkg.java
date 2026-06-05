package com.zalexdev.stryker.appintro.utils;

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
 * CheckPkg is a class that checks if a package is installed on the device
 */
public class CheckPkg extends StrykerTask<String, Boolean> {


    public String pkg;

    public CheckPkg(String p) {
        pkg = p;
    }

    @SuppressLint("WrongThread")
    @Override
    protected Boolean doInBackground() {
        String line;
        boolean result = false;


        try {

            Process process = Runtime.getRuntime().exec("su -mm");
            OutputStream stdin = process.getOutputStream();
            InputStream stderr = process.getErrorStream();
            InputStream stdout = process.getInputStream();
            stdin.write(("pm list packages" + '\n').getBytes());
            stdin.write(("exit\n").getBytes());
            stdin.flush();
            stdin.close();
            BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
            while ((line = br.readLine()) != null) {
                if (line.contains(pkg)) {
                    result = true;
                }
            }
            br.close();
            br = new BufferedReader(new InputStreamReader(stderr));
            while ((line = br.readLine()) != null) {
                publish(line);
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
    }

    @Override
    protected void onProgress(String value) {
    }


}
