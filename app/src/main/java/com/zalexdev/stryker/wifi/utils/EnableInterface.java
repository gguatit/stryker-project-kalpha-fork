package com.zalexdev.stryker.wifi.utils;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import com.zalexdev.stryker.utils.StrykerTask;
import android.util.Log;

import com.zalexdev.stryker.utils.Core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


public class EnableInterface extends StrykerTask<String, Boolean> {
    public String exec = Core.EXECUTE;
    public String wlan;
    public Core core;


    public EnableInterface(String wlan1, Core c) {
        wlan = wlan1;
        core = c;
    }

    @SuppressLint("WrongThread")
    @Override
    protected Boolean doInBackground() {
        String line;
        boolean result = false;


        try {

            // The above code is creating a process that will run the command "su -mm" and then run the
            // command "ip link set wlan0 up" and then exit. It ups adapter interface for scan
            Process process = Runtime.getRuntime().exec("su -mm");
            OutputStream stdin = process.getOutputStream();
            InputStream stderr = process.getErrorStream();
            InputStream stdout = process.getInputStream();
            stdin.write(("ip link set " + wlan + " up" + '\n').getBytes());
            stdin.write(("exit\n").getBytes());
            stdin.flush();
            stdin.close();
            ArrayList<String> outerror = new ArrayList<>();
            BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
            br.close();
            br = new BufferedReader(new InputStreamReader(stderr));
            while ((line = br.readLine()) != null) {
                publish(line);
                outerror.add(line);
            }
            core.writetolog(outerror, true);
            br.close();
            process.waitFor(60, TimeUnit.SECONDS);
            process.destroy();
            if (process.exitValue() == 0) {
                result = true;
            }
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
