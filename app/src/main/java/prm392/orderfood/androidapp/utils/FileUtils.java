package prm392.orderfood.androidapp.utils;

import android.content.Context;
import android.net.Uri;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtils {

    public static File getFileFromUri(Context context, Uri uri) {
        String fileName = "temp_" + System.currentTimeMillis();
        File file = new File(context.getCacheDir(), fileName);

        try (InputStream input = context.getContentResolver().openInputStream(uri);
             OutputStream output = new FileOutputStream(file)) {

            byte[] buffer = new byte[4 * 1024];
            int read;
            while ((read = input.read(buffer)) != -1) {
                output.write(buffer, 0, read);
            }

            output.flush();
            return file;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
