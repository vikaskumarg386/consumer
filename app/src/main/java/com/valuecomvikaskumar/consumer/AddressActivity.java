package com.valuecomvikaskumar.consumer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AddressActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Button continueButton;
    private EditText pincode;
    private EditText addressLine1;
    private EditText addressLine2;
    private EditText city;
    private EditText state;
    private EditText country;
    private DatabaseReference mRef;
    private String userId;

    private String Epincode;
    private String Eaddress1;
    private String Eaddress2;
    private String Ecity;
    private String Estate;
    private String Ecountry;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        toolbar=findViewById(R.id.bankAccountDetailsToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setTitle("Company Address");

        pincode=findViewById(R.id.pincode);
        addressLine1=findViewById(R.id.addressLine1);
        addressLine2=findViewById(R.id.addressLine2);
        city=findViewById(R.id.city);
        state=findViewById(R.id.state);
        country=findViewById(R.id.country);

        userId= FirebaseAuth.getInstance().getCurrentUser().getUid();
        mRef= FirebaseDatabase.getInstance().getReference();

        Epincode=getIntent().getStringExtra("pincode");
        Eaddress1=getIntent().getStringExtra("address1");
        Eaddress2=getIntent().getStringExtra("address2");
        Ecity=getIntent().getStringExtra("city");
        Estate=getIntent().getStringExtra("state");
        Ecountry=getIntent().getStringExtra("country");
        if(!TextUtils.isEmpty(Epincode)&&!TextUtils.isEmpty(Eaddress1)&&!TextUtils.isEmpty(Eaddress2)&&!TextUtils.isEmpty(Ecity)&&!TextUtils.isEmpty(Estate)&&!TextUtils.isEmpty(Ecountry)){
            pincode.setText(Epincode);
            addressLine1.setText(Eaddress1);
            addressLine2.setText(Eaddress2);
            city.setText(Ecity);
            state.setText(Estate);
            country.setText(Ecountry);
        }

        pincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                if (!TextUtils.isEmpty(pincode.getText().toString())&&pincode.getText().toString().length()==6)
                    sss(pincode.getText().toString());
                else {
                    city.setText("");
                    state.setText("");
                    country.setText("");
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!TextUtils.isEmpty(pincode.getText().toString())&&pincode.getText().toString().length()==6)
                    sss(pincode.getText().toString());
                else {
                    city.setText("");
                    state.setText("");
                    country.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(pincode.getText().toString())&&pincode.getText().toString().length()==6)
                    sss(pincode.getText().toString());
                else {
                    city.setText("");
                    state.setText("");
                    country.setText("");
                }
            }
        });

        continueButton=findViewById(R.id.companyAddressContinue);
        //continue button
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(pincode.getText())&&!TextUtils.isEmpty(addressLine1.getText())&&!TextUtils.isEmpty(addressLine2.getText())&&!TextUtils.isEmpty(city.getText())&&!TextUtils.isEmpty(state.getText())&&!TextUtils.isEmpty(country.getText())) {
                    final ProgressDialog progressDialog=new ProgressDialog(AddressActivity.this);
                    progressDialog.setMessage("please wait");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    Map map = new HashMap();
                    map.put("pincode", pincode.getText().toString());
                    map.put("addressLine1", addressLine1.getText().toString());
                    map.put("addressLine2", addressLine2.getText().toString());
                    map.put("city", city.getText().toString());
                    map.put("state", state.getText().toString());
                    map.put("country", country.getText().toString());

                    mRef.child("Consumer").child(userId).child("address").setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                if(TextUtils.isEmpty(Epincode)){
                                    Intent intent = new Intent(AddressActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();}
                                else {
                                    Intent intent = new Intent(AddressActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                            else {
                                progressDialog.dismiss();
                                Toast.makeText(AddressActivity.this,"something went wrong",Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }
                else {
                    Toast.makeText(AddressActivity.this,"Please fill all fields",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void sss(String pin){
        new GetCoordinates().execute(pin.replace(" ","+"));

    }

    private  class GetCoordinates extends AsyncTask<String,Void,String> {

        ProgressDialog dialog=new ProgressDialog(AddressActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Please wait");
            dialog.setCanceledOnTouchOutside(false);
            //dialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            JSONObject jsonObject= null;
            try {
                jsonObject = new JSONObject(s);
                String lat=((JSONArray)jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry").getJSONObject("location").get("lat").toString();
                String lng=((JSONArray)jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry").getJSONObject("location").get("lng").toString();
                Log.i("lat",lat);
                Log.i("lng",lng);
                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(AddressActivity.this, Locale.getDefault());

                try {
                    addresses = geocoder.getFromLocation(Double.parseDouble(lat), Double.parseDouble(lng), 1);
                    String city1 = addresses.get(0).getLocality();
                    String state1 = addresses.get(0).getAdminArea();
                    String country1 = addresses.get(0).getCountryName();



                    city.setText(city1);
                    state.setText(state1);
                    country.setText(country1);


                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        protected String doInBackground(String... strings) {

            String response;
            try {
                String address=strings[0];
                HttpDataHandler http=new HttpDataHandler();
                String url=String.format("https://maps.googleapis.com/maps/api/geocode/json?address=%s",address);
                response=http.getHttpData(url);
                return response;
            }catch (Exception e){

            }
            return null;
        }
    }

}
