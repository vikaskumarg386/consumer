package com.valuecomvikaskumar.consumer;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    private Toolbar toolbar;
    private DatabaseReference mRef;
    private String userId;
    private ViewPager mViewPager;
    private SectionPagerAdapter mSectionPagerAdapter;
    private TabLayout mTabLayout;
    private String pincode;
    private String address1;
    private String address2;
    private String city;
    private String state;
    private String country;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar=findViewById(R.id.mainActivityToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Consumer");



        if (FirebaseAuth.getInstance().getCurrentUser()==null)
        {
            Intent intent=new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent);
        }
        else {

            onStart();
            userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            mRef = FirebaseDatabase.getInstance().getReference().child("Vendor").child(userId).child("product");

            mViewPager =(ViewPager)findViewById(R.id.viewPager);

            mSectionPagerAdapter =new SectionPagerAdapter(getSupportFragmentManager());
            mViewPager.setAdapter(mSectionPagerAdapter);

            mTabLayout=(TabLayout)findViewById(R.id.tabLayout);
            mTabLayout.setupWithViewPager(mViewPager);


            FirebaseDatabase.getInstance().getReference().child("Consumer").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("address").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.hasChild("pincode")) {
                        pincode = dataSnapshot.child("pincode").getValue().toString();
                        address1 = dataSnapshot.child("addressLine1").getValue().toString();
                        address2 = dataSnapshot.child("addressLine2").getValue().toString();
                        city = dataSnapshot.child("city").getValue().toString();
                        country = dataSnapshot.child("country").getValue().toString();
                        state = dataSnapshot.child("state").getValue().toString();


                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (FirebaseAuth.getInstance().getCurrentUser()==null)
        {
            Intent intent=new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch(item.getItemId()){
            case R.id.signOut:{
                AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Do you want to logout");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        onStart();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();

                break;
            }
            case R.id.changeAddress:{

                Intent intent=new Intent(MainActivity.this,AddressActivity.class);
                intent.putExtra("pincode",pincode);
                intent.putExtra("address1",address1);
                intent.putExtra("address2",address2);
                intent.putExtra("city",city);
                intent.putExtra("state",state);
                intent.putExtra("country",country);
                startActivity(intent);


            }
        }

        return true;
    }
}
