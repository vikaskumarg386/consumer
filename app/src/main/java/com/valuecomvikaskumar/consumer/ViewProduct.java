package com.valuecomvikaskumar.consumer;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ViewProduct extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView productName;
    private TextView sizeOrWeight;
    private TextView price;
    private ImageButton forward;
    private ImageButton backward;
    private String vendorUserId;
    private TextView fullAddress;
    private String address;
    private String city;
    private String state;
    private String country;
    private Button buyNow;
    private Button location;
    private String quantity;


    private String productPrice;
    private String name;
    private String barcode;
    private String size;
    private String url[]=new String[6];
    private Spinner spinner;
    private int k=0,m=1;
    private String[] quantities={"click here","1","2","3","4","5"};
    private ImageView[] imageViews=new ImageView[6];
    private String lat;
    private String lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product);


        toolbar=findViewById(R.id.viewProductToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setTitle("Add Product");

        imageViews[1]=findViewById(R.id.imageView);
        imageViews[2]=findViewById(R.id.imageView8);
        imageViews[3]=findViewById(R.id.imageView10);
        imageViews[4]=findViewById(R.id.imageView11);
        imageViews[5]=findViewById(R.id.imageView12);

        productName=findViewById(R.id.pName);
        sizeOrWeight=findViewById(R.id.pSize);
        forward=findViewById(R.id.forward);
        backward=findViewById(R.id.backward);
        price=findViewById(R.id.pPrice);
        spinner=findViewById(R.id.spinner);
        fullAddress=findViewById(R.id.address);
        location=findViewById(R.id.location);
        ArrayAdapter adapter=new ArrayAdapter(ViewProduct.this,android.R.layout.simple_list_item_1,quantities);
        spinner.setAdapter(adapter);


        name=getIntent().getStringExtra("name");
        productPrice=getIntent().getStringExtra("price");
        barcode=getIntent().getStringExtra("barcode");
        size=getIntent().getStringExtra("size");
        url[1]=getIntent().getStringExtra("imageUrl1");
        url[2]=getIntent().getStringExtra("imageUrl2");
        url[3]=getIntent().getStringExtra("imageUrl3");
        url[4]=getIntent().getStringExtra("imageUrl4");
        url[5]=getIntent().getStringExtra("imageUrl5");
        vendorUserId=getIntent().getStringExtra("vendorUserId");

        FirebaseDatabase.getInstance().getReference().child("Vendor").child(vendorUserId).child("companyAddress").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                address=dataSnapshot.child("addressLine1").getValue().toString();
                city=dataSnapshot.child("city").getValue().toString();
                state=dataSnapshot.child("state").getValue().toString();
                country=dataSnapshot.child("country").getValue().toString();
                fullAddress.setText(address+" "+city+" "+state+" "+country);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        buyNow=findViewById(R.id.buyNow);


        if(!TextUtils.isEmpty(name)&&!TextUtils.isEmpty(size)&&!TextUtils.isEmpty(barcode)&&!TextUtils.isEmpty(productPrice)){
            productName.setText(name);
            sizeOrWeight.setText(size);
            price.setText(productPrice);

        }
        if(!TextUtils.isEmpty(url[1])){
            Picasso.with(ViewProduct.this).load(url[1]).placeholder(R.drawable.download).networkPolicy(NetworkPolicy.OFFLINE).into(imageViews[1], new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(ViewProduct.this).load(url[1]).placeholder(R.drawable.download).into(imageViews[1]);
                }
            });
            k++;
        }
        if(!TextUtils.isEmpty(url[2])){

            Picasso.with(ViewProduct.this).load(url[2]).placeholder(R.drawable.download).networkPolicy(NetworkPolicy.OFFLINE).into(imageViews[2], new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(ViewProduct.this).load(url[2]).placeholder(R.drawable.download).into(imageViews[2]);
                }
            });
            k++;
        }
        if(!TextUtils.isEmpty(url[3])){

            Picasso.with(ViewProduct.this).load(url[3]).placeholder(R.drawable.download).networkPolicy(NetworkPolicy.OFFLINE).into(imageViews[3], new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(ViewProduct.this).load(url[3]).placeholder(R.drawable.download).into(imageViews[3]);
                }
            });
            k++;
        }
        if(!TextUtils.isEmpty(url[4])){

            Picasso.with(ViewProduct.this).load(url[4]).placeholder(R.drawable.download).networkPolicy(NetworkPolicy.OFFLINE).into(imageViews[4], new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(ViewProduct.this).load(url[4]).placeholder(R.drawable.download).into(imageViews[4]);
                }
            });
            k++;
        }
        if(!TextUtils.isEmpty(url[5])){

            Picasso.with(ViewProduct.this).load(url[5]).placeholder(R.drawable.download).networkPolicy(NetworkPolicy.OFFLINE).into(imageViews[5], new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(ViewProduct.this).load(url[5]).placeholder(R.drawable.download).into(imageViews[5]);
                }
            });
            k++;
        }


        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(m+1<=k){
                    m++;
                    imageViews[m-1].setVisibility(View.INVISIBLE);
                    imageViews[m].setVisibility(View.VISIBLE);

                }
                Log.i("m=",String.valueOf(m));
            }
        });
        backward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(m-1>=1){
                    m--;
                    if(m<5){
                    imageViews[m+1].setVisibility(View.INVISIBLE);
                    imageViews[m].setVisibility(View.VISIBLE);}
                    else if(m>=5){
                        imageViews[5].setVisibility(View.INVISIBLE);
                        imageViews[1].setVisibility(View.VISIBLE);
                    }
                    Log.i("m=",String.valueOf(m));
                }
            }
        });


        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sss(fullAddress.getText().toString().replace(","," "));
                if(!TextUtils.isEmpty(lat)&&!TextUtils.isEmpty(lng)){
                Intent intent=new Intent(ViewProduct.this,MapsActivity.class);
                intent.putExtra("lat",lat);
                intent.putExtra("lng",lng);
                startActivity(intent);}
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                quantity=quantities[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!quantity.equals("click here")) {
                    final ProgressDialog dialog=new ProgressDialog(ViewProduct.this);
                    dialog.setMessage("wait");
                    dialog.show();
                    final Map map = new HashMap();
                    final String pushKey = FirebaseDatabase.getInstance().getReference().push().getKey();
                    map.put("consumerId", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    map.put("date", new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()));
                    map.put("timeStamp", ServerValue.TIMESTAMP);
                    map.put("orderKey", pushKey);
                    map.put("list", " NAME    QUANTITY\n"+" "+productName.getText().toString() + "    " + quantity);

                    final Map map1 = new HashMap();
                    map1.put("Consumer/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/" + "placedOrder/" + pushKey, map);
                    map1.put("Vendor/" + vendorUserId + "/" + "orders/" + pushKey, map);
                    FirebaseDatabase.getInstance().getReference().updateChildren(map1, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if(databaseError==null) {
                                dialog.dismiss();
                                AlertDialog.Builder builder = new AlertDialog.Builder(ViewProduct.this);
                                builder.setMessage("Your order has been placed. The shopper will try to connect you");
                                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                builder.show();
                            }

                        }
                    });
                }
                else {
                    Toast.makeText(ViewProduct.this,"please select quantity",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    void sss(String pin){
        new GetCoordinates().execute(pin.replace(" ","+"));

    }

    private  class GetCoordinates extends AsyncTask<String,Void,String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            JSONObject jsonObject= null;
            try {
                jsonObject = new JSONObject(s);
                lat=((JSONArray)jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry").getJSONObject("location").get("lat").toString();
                lng=((JSONArray)jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry").getJSONObject("location").get("lng").toString();


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
