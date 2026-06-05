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
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * This class is used to fix the inet group permissions
 */
public class FixInet extends StrykerTask<String, Boolean> {


    public Core core;

    public FixInet( Core c) {
        core = c;
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
            stdin.write((Core.EXECUTE+" ash" + '\n').getBytes());
            stdin.write((" > /etc/resolv.conf" + '\n').getBytes());
            stdin.write((" echo \"nameserver 1.1.1.1\" >> /etc/resolv.conf" + '\n').getBytes());
            stdin.write((" echo \"nameserver 8.8.8.8\" >> /etc/resolv.conf" + '\n').getBytes());
            stdin.write((" echo \"nameserver 9.9.9.9\" >> /etc/resolv.conf" + '\n').getBytes());
            stdin.write(("exit\n").getBytes());
            stdin.flush();
            stdin.close();
            ArrayList<String> out = new ArrayList<>();
            ArrayList<String> outerror = new ArrayList<>();
            BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
            while ((line = br.readLine()) != null) {
                out.add(line);
            }
            br.close();
            br = new BufferedReader(new InputStreamReader(stderr));
            while ((line = br.readLine()) != null) {
                outerror.add(line);
                publish(line);
            }
            br.close();
            core.writetolog(out, false);
            core.writetolog(outerror, true);
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
