package prm392.orderfood.androidapp.utils;

import android.content.Context;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;

public class FileUtils {

    public static File getFileFromUri(Context context, Uri uri) {
        String extension = getExtension(context, uri);
        if (extension == null || extension.trim().isEmpty()) {
            extension = "jpg";
        }

        String fileName = "temp_" + System.currentTimeMillis() + "." + extension;
        File file = new File(context.getCacheDir(), fileName);

        try (InputStream input = context.getContentResolver().openInputStream(uri);
             OutputStream output = new FileOutputStream(file)) {

            if (input == null) return null;

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

    private static String getExtension(Context context, Uri uri) {
        String mimeType = context.getContentResolver().getType(uri);
        if (mimeType != null) {
            String ext = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType);
            if (ext != null && !ext.trim().isEmpty()) {
                return ext.toLowerCase(Locale.US);
            }
        }

        String path = uri.getPath();
        if (path != null) {
            int dot = path.lastIndexOf('.');
            if (dot != -1 && dot < path.length() - 1) {
                return path.substring(dot + 1).toLowerCase(Locale.US);
            }
        }

        return null;
    }
}
