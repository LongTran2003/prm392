package prm392.orderfood.data.network;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateTypeAdapterFlexible extends TypeAdapter<Date> {
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS", Locale.US);

    @Override
    public void write(JsonWriter out, Date value) throws IOException {
        if (value != null) {
            out.value(sdf.format(value));
        } else {
            out.nullValue();
        }
    }

    @Override
    public Date read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull(); // bỏ qua giá trị null
            return null;
        }

        String dateStr = in.nextString();
        try {
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            throw new IOException("Failed to parse date: " + dateStr, e);
        }
    }
}
