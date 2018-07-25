package com.valuecomvikaskumar.consumer;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class ProductFragment extends Fragment {

    private AutoCompleteTextView editText;
    private Button button;
    private Toolbar toolbar;
    private String productCategory;
    private String pincode;
    private ImageButton add;
    private LinearLayout linearLayout;
    private Button placeOrder;

    TextView textView[]=new TextView[10];
    AutoCompleteTextView name[]=new AutoCompleteTextView[10];
    TextView textView1[]=new TextView[10];
    EditText quantity[]=new EditText[10];
    int n=1;
    private ArrayList<String> list=new ArrayList<String>();
    private View view;

    public ProductFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view=inflater.inflate(R.layout.fragment_product, container, false);
        editText=view.findViewById(R.id.productName);
        button=view.findViewById(R.id.search);
        add=view.findViewById(R.id.add);
        name[0]=view.findViewById(R.id.productName0);
        quantity[0]=view.findViewById(R.id.quantity0);


        linearLayout=view.findViewById(R.id.linearLayout);

        FirebaseDatabase.getInstance().getReference().child("product").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot d:dataSnapshot.getChildren()){
                    list.add(d.child("productName").getValue().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        final ArrayAdapter adapter=new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1,list);
        editText.setAdapter(adapter);
        name[0].setAdapter(adapter);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(n<10) {
                    int k = n++;
                    final LinearLayout linearLayout1 = new LinearLayout(getContext());
                    linearLayout1.setOrientation(LinearLayout.VERTICAL);
                    LinearLayout linearLayout2 = new LinearLayout(getContext());
                    linearLayout2.setOrientation(LinearLayout.VERTICAL);
                    LinearLayout linearLayout3 = new LinearLayout(getContext());
                    linearLayout3.setOrientation(LinearLayout.VERTICAL);

                    textView[k] = new TextView(getContext());
                    textView[k].setText("Product name:");
                    textView[k].setTextSize(17);
                    textView[k].setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    ));
                    name[k] = new AutoCompleteTextView(getContext());
                    name[k].setAdapter(adapter);
                    name[k].setBackgroundResource(R.drawable.shape);
                    name[k].setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT
                    ));
                    name[k].setId(k);
                    name[k].setPadding(12, 12, 12, 12);
                    linearLayout2.addView(textView[k]);
                    linearLayout2.addView(name[k]);

                    textView1[k] = new TextView(getContext());
                    textView1[k].setText("Quantity:");
                    textView1[k].setTextSize(17);
                    textView1[k].setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    ));
                    quantity[k] = new EditText(getContext());
                    quantity[k].setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT
                    ));
                    quantity[k].setId(k);

                    quantity[k].setPadding(12, 12, 12, 12);
                    quantity[k].setText("");
                    quantity[k].setBackgroundResource(R.drawable.shape);
                    View view = new View(getContext());
                    view.setBackgroundColor(getResources().getColor(android.R.color.black));
                    view.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT

                    ));
                    linearLayout3.addView(textView1[k]);
                    linearLayout3.addView(quantity[k]);
                    linearLayout1.addView(linearLayout2);
                    linearLayout1.addView(linearLayout3);
                    linearLayout1.addView(view);

                    linearLayout1.setLayoutParams(new LinearLayout.LayoutParams(
                            RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT
                    ));
                    linearLayout.addView(linearLayout1);
                }
                else {
                    Toast.makeText(getContext(),"Maximum product exceed",Toast.LENGTH_SHORT).show();
                }
            }
        });

        placeOrder=view.findViewById(R.id.placeOrder);
        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogeBulider =new AlertDialog.Builder(getContext());
                TextView textView=new TextView(getContext());
                final StringBuilder stringBuilder=new StringBuilder("  NAME    QUANTITY\n");
                for(int j=0;j<10;j++){
                    if(name[j]!=null){
                        stringBuilder.append("  "+name[j].getText()+"     "+quantity[j].getText()+"\n");
                    }
                }
                textView.setText(stringBuilder.toString());
                dialogeBulider.setView(textView);
                dialogeBulider.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        final ProgressDialog dialog1=new ProgressDialog(getContext());
                        dialog1.setMessage("wait");
                        dialog1.show();
                        final Map map=new HashMap();
                        final String pushKey=FirebaseDatabase.getInstance().getReference().push().getKey();
                        map.put("consumerId", FirebaseAuth.getInstance().getCurrentUser().getUid());
                        map.put("date",new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()));
                        map.put("timeStamp", ServerValue.TIMESTAMP);
                        map.put("orderKey",pushKey);
                        map.put("list",stringBuilder.toString());

                        final Map map1=new HashMap();
                        map1.put("Consumer/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/"+"placedOrder/"+pushKey,map);

                        FirebaseDatabase.getInstance().getReference().child("Consumer").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("placedOrder").child(pushKey).setValue(map);

                        FirebaseDatabase.getInstance().getReference().child("productCategory").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for(final DataSnapshot d:dataSnapshot.getChildren())
                                    if(d.hasChild(name[0].getText().toString())){
                                        productCategory=d.getKey();
                                        FirebaseDatabase.getInstance().getReference().child("Vendor").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                for(final DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                                                    FirebaseDatabase.getInstance().getReference().child("Vendor").child(dataSnapshot1.getKey()).child("category").addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            for(DataSnapshot dataSnapshot2:dataSnapshot.getChildren())
                                                                if(dataSnapshot2.getValue().toString().equals(productCategory)){
                                                                    map1.put("Vendor/"+dataSnapshot1.getKey()+"/"+"orders/"+pushKey,map);
                                                                    Log.i("key",dataSnapshot1.getKey());
                                                                }


                                                            FirebaseDatabase.getInstance().getReference().updateChildren(map1, new DatabaseReference.CompletionListener() {
                                                                @Override
                                                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                                    if(databaseError==null){
                                                                        dialog1.dismiss();
                                                                        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                                                                        builder.setMessage("This order has been sent to all shoppers who are near by your location and they will place a bid for your list and you can choose one out of them to buy product");
                                                                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(DialogInterface dialog, int which) {
                                                                                for(int i=0;i<10;i++){
                                                                                    if(name[i]!=null){
                                                                                        name[i].setText("");
                                                                                        quantity[i].setText("");
                                                                                    }
                                                                                }
                                                                            }
                                                                        });
                                                                        builder.show();
                                                                    }
                                                                }
                                                            });
                                                        }



                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    });
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

                                    }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                });
                dialogeBulider.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialogeBulider.show();


            }
        });






        FirebaseDatabase.getInstance().getReference().child("Consumer").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("address").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("pincode","yes i am in");
                if(dataSnapshot.hasChild("pincode")) {
                    pincode=dataSnapshot.child("pincode").getValue().toString();

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                FirebaseDatabase.getInstance().getReference().child("productCategory").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot d:dataSnapshot.getChildren())
                            if(d.hasChild(editText.getText().toString())){
                                //
                                productCategory=d.getKey();
                                Intent intent=new Intent(getContext(),ProductsActivity.class);
                                intent.putExtra("productName",editText.getText().toString());
                                intent.putExtra("productCategory",d.getKey());
                                intent.putExtra("pincode",pincode);
                                startActivity(intent);

                            }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });
        return view;
    }

}
