package prm392.orderfood.androidapp.ui.fragment.shopOwner;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import prm392.orderfood.androidapp.R;

public class MapPickerActivity extends AppCompatActivity {

    private MapView mapView;
    private Marker currentMarker;
    private GeoPoint selectedPoint;
    private EditText edtAddress;
    private ImageButton btnSearch;
    private Button btnConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuration.getInstance().setUserAgentValue(getPackageName());

        // Sử dụng layout XML bạn đã viết
        setContentView(R.layout.activity_map_picker);

        mapView = findViewById(R.id.map);
        edtAddress = findViewById(R.id.edtAddress);
        btnSearch = findViewById(R.id.btnSearch);
        btnConfirm = findViewById(R.id.btnConfirm);

        mapView.setMultiTouchControls(true);
        mapView.getController().setZoom(15.0);

        // Vị trí mặc định
        GeoPoint defaultPoint = new GeoPoint(10.762622, 106.660172); // HCM
        mapView.getController().setCenter(defaultPoint);
        setMarker(defaultPoint);

        setupMapTapListener();
        setupSearchFunction();
        setupConfirmButton();
    }

    private void setupMapTapListener() {
        MapEventsReceiver receiver = new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                setMarker(p);
                return true;
            }

            @Override
            public boolean longPressHelper(GeoPoint p) {
                return false;
            }
        };
        MapEventsOverlay overlay = new MapEventsOverlay(receiver);
        mapView.getOverlays().add(overlay);
    }

    private void setupSearchFunction() {
        btnSearch.setOnClickListener(v -> {
            String location = edtAddress.getText().toString();
            if (!location.isEmpty()) {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocationName(location, 1);
                    if (addresses != null && !addresses.isEmpty()) {
                        Address address = addresses.get(0);
                        GeoPoint point = new GeoPoint(address.getLatitude(), address.getLongitude());
                        mapView.getController().setCenter(point);
                        setMarker(point);
                    } else {
                        Toast.makeText(this, "Không tìm thấy địa chỉ", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Lỗi tìm địa chỉ", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Vui lòng nhập địa chỉ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupConfirmButton() {
        btnConfirm.setOnClickListener(v -> {
            if (selectedPoint != null) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("latitude", selectedPoint.getLatitude());
                resultIntent.putExtra("longitude", selectedPoint.getLongitude());
                setResult(RESULT_OK, resultIntent);
                finish();
            } else {
                Toast.makeText(this, "Vui lòng chọn vị trí", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setMarker(GeoPoint point) {
        selectedPoint = point;

        if (currentMarker != null) {
            mapView.getOverlays().remove(currentMarker);
        }

        currentMarker = new Marker(mapView);
        currentMarker.setPosition(point);
        currentMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        currentMarker.setTitle("Vị trí đã chọn");
        mapView.getOverlays().add(currentMarker);
        mapView.invalidate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }
}
