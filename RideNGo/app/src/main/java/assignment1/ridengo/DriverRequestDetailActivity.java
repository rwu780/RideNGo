package assignment1.ridengo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;

import static android.provider.CalendarContract.CalendarCache.URI;

/**
 * The type Driver request detail activity.
 */
public class DriverRequestDetailActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1;
    private String username;
    private RideRequest rideRequest;
    private ArrayList<String> info = new ArrayList<String>();
    private RideRequest offlineAcceptedRequest;
    private static final String AR_FILE = "offlineAcceptedRequest";
    private static final String T = ".sav";
    private LatLng startPoint;
    private LatLng endPoint;
    private String[] phoneNum;
    private String[] emailAddress;
    private Intent phoneIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_request_detail);

        username = getIntent().getStringExtra("username");
        final int id = getIntent().getIntExtra("id", 0);
        RideRequestController.notifyUser(username, this);
        UserController.loadUserListFromServer("{\"from\":0,\"size\":10000,\"query\": { \"match\": { \"username\": \"" + username + "\"}}}");
        final User user = UserController.getUserList().getUserByUsername(username);

        if(isConnected()){
            UserController.loadUserListFromServer("{\"from\":0,\"size\":10000,\"query\": { \"match\": { \"username\": \"" + username + "\"}}}");
            RideRequestController.loadRequestListFromServer("{\"from\":0,\"size\":10000,\"query\": { \"match\": { \"id\": " + String.valueOf(id) + "}}}");
            checkOfflineAcceptedRequest(username);
            if(offlineAcceptedRequest != null){
                int offlineRequestId = offlineAcceptedRequest.getId();
                RideRequest refreshedOfflineRequest = RideRequestController.getRequestList().getRequestById(offlineRequestId);
                if(refreshedOfflineRequest.getStatus().equals("Waiting for Driver") || refreshedOfflineRequest.getStatus().equals("Waiting for Confirmation") ) {
                    user.acceptRequest(refreshedOfflineRequest);
                    offlineAcceptedRequest = null;
                    Toast.makeText(this, "The request you accepted while offline is now accepted. You can check it in VIEW ACCEPTED.", Toast.LENGTH_SHORT).show();
                }
                else{
                    offlineAcceptedRequest = null;
                    Toast.makeText(this, "Sorry, the request you accepted while offline is no longer available.", Toast.LENGTH_SHORT).show();
                }
            }
        }


        rideRequest = RideRequestController.getRequestList().getRequestById(id);
        getInfo();

        ListView requestDetailListView = (ListView)findViewById(R.id.RequestDetailListView);
        Button showOnMapButton = (Button)findViewById(R.id.mapShowButton);
        Button acceptButton = (Button)findViewById(R.id.AcceptButton);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, info);
        requestDetailListView.setAdapter(adapter);

        if(rideRequest.isAccepted(username)){
            acceptButton.setText("You've Accepted");
            acceptButton.setEnabled(false);
        }
        else if(rideRequest.getStatus().equals("Driver Confirmed")||rideRequest.getStatus().equals("Trip Completed")){
            acceptButton.setText("Not available");
            acceptButton.setEnabled(false);
        }
        else {
            acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isConnected()) {
                        user.acceptRequest(rideRequest);
                    }
                    else{
                        offlineAcceptedRequest = rideRequest;
                        saveOffLineAcceptedRequest(username);
                        offlineAcceptedRequest = null;
                        Toast.makeText(DriverRequestDetailActivity.this, "You are offline now, you will accept the request once you are online if the request is still available.", Toast.LENGTH_SHORT).show();
                    }
                    finish();
                }
            });
        }
        requestDetailListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                if(id == 3){
                    Toast.makeText(DriverRequestDetailActivity.this,"Email rider",Toast.LENGTH_SHORT).show();
                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    //Intent emailIntent = new Intent(Intent.ACTION_SEND, Uri.parse("mailto:" + rideRequests.get(position).getRider().getEmail()));
                    //emailIntent.putExtra(Intent.EXTRA_EMAIL, rideRequests.get(position).getRider().getEmail());
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, emailAddress);
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "RideNGo");
                    emailIntent.setType("message/rfc822");
                    //Toast.makeText(DriverRequestDetailActivity.this, "Email rider TEST" + rideRequests.get(position).getRider().getEmail(), Toast.LENGTH_LONG).show();
                    startActivity(Intent.createChooser(emailIntent, "Send Email"));
                }
                else if(id == 4) {
                    phoneIntent = new Intent(Intent.ACTION_CALL, URI.parse("tel: " + phoneNum[0]));
                    // Checking for phone permissions
                    if(ActivityCompat.checkSelfPermission(DriverRequestDetailActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(DriverRequestDetailActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CODE);
                    } else {
                        startActivity(phoneIntent);
                    }
                }
            }
        });

        showOnMapButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                ArrayList<LatLng> listOfPoints = new ArrayList<LatLng>();
                Bundle extras = new Bundle();
                startPoint = rideRequest.getStartCoord();
                endPoint = rideRequest.getEndCoord();
                listOfPoints.add(startPoint);
                listOfPoints.add(endPoint);
                Intent intent = new Intent(DriverRequestDetailActivity.this, ShowPointsOnMapActivity.class);
                extras.putParcelableArrayList("SHOW_POINTS",listOfPoints);
                intent.putExtras(extras);
                startActivity(intent);

            }
        });
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this,DriverMainActivity.class);
        intent.putExtra("username",username);
        startActivity(intent);
        finish();
    }

    /**
     * Get info.
     */
    private void getInfo(){
        info.clear();
        User rider = rideRequest.getRider();
        info.add("Start: " + rideRequest.getStartPoint().toString());
        info.add("End: " + rideRequest.getEndPoint().toString());
        info.add("Name: " + rider.getUsername());
        info.add("Email: " + rider.getEmail());
        emailAddress = new String[]{rider.getEmail()};
        info.add("Phone: " + rider.getPhoneNum());
        phoneNum = new String[]{ rider.getPhoneNum()};
        info.add("Status: " + rideRequest.getStatus().toString());
        info.add("Description: " + rideRequest.getDescription());
    }

    /**
     * Is connected boolean.
     *
     * @return the boolean
     */
    public boolean isConnected(){
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return ((activeNetwork != null) && activeNetwork.isConnectedOrConnecting());
    }

    private void saveOffLineAcceptedRequest(String username){
        String FILENAME = AR_FILE+username+T;
        try {
            FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);

            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));

            Gson gson = new Gson();
            gson.toJson(offlineAcceptedRequest, out);
            out.flush();

            fos.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        }
    }

    /**
     * Check offline accepted request.
     *
     * @param username the username
     */
    public void checkOfflineAcceptedRequest(String username){
        String FILENAME = AR_FILE+username+T;
        try {
            FileInputStream fis = openFileInput(FILENAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));

            Gson gson = new Gson();
            Type rideRequestType = new TypeToken<RideRequest>(){}.getType();

            offlineAcceptedRequest = gson.fromJson(in, rideRequestType);
            deleteFile(FILENAME);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            offlineAcceptedRequest = null;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        }
    }
}
