package com.valuecomvikaskumar.consumer;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class ProductsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private DatabaseReference mRef;
    private String userId;
    private RecyclerView recyclerView;
    private RecyclerView recyclerView1;
    private String pincode;
    private String productName;
    private String productCategory;
    private DatabaseReference ref;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);


        toolbar=findViewById(R.id.mainActivityToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setTitle("Product");

        recyclerView=findViewById(R.id.recyclerView);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(ProductsActivity.this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        recyclerView1=findViewById(R.id.recyclerView1);

        LinearLayoutManager linearLayoutManager1=new LinearLayoutManager(ProductsActivity.this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView1.setLayoutManager(linearLayoutManager1);
        recyclerView1.setHasFixedSize(true);

        productName=getIntent().getStringExtra("productName");
        pincode=getIntent().getStringExtra("pincode");
        productCategory=getIntent().getStringExtra("productCategory");
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mRef = FirebaseDatabase.getInstance().getReference();
        FirebaseAuth.getInstance().getCurrentUser();
        Log.i("userId",userId);
        progressDialog=new ProgressDialog(ProductsActivity.this);
        progressDialog.setMessage("wait");
        progressDialog.show();

        if(!TextUtils.isEmpty(pincode)&&!TextUtils.isEmpty(productCategory)) {

            ref = mRef.child("productAreaWise").child(pincode).child(productCategory);
            showList1();
            showList();
        }

    }



    private void showList1(){
        Query mQuery=ref.orderByChild("productName").startAt(productName).endAt(productName+"\uf8ff");
        FirebaseRecyclerOptions<Product> options =
                new FirebaseRecyclerOptions.Builder<Product>()
                        .setQuery(mQuery, Product.class)
                        .build();

        FirebaseRecyclerAdapter firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Product,ProductViewHolder>(options) {


            @Override
            public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.searched_product,parent,false);
                return new ProductViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final ProductViewHolder holder, int position, @NonNull final Product model) {
                final String ref=getRef(position).getKey();
                holder.name.setText(model.getProductName());
                holder.price.setText("price:"+model.getPrice());

                Picasso.with(getApplicationContext()).load(model.getImageUrl1()).placeholder(R.drawable.download).networkPolicy(NetworkPolicy.OFFLINE).into(holder.imageView1, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(getApplicationContext()).load(model.getImageUrl1()).placeholder(R.drawable.download).into(holder.imageView1);

                    }
                });


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(ProductsActivity.this,ViewProduct.class);
                        intent.putExtra("barcode",model.getBarcode());
                        intent.putExtra("name",model.getProductName());
                        intent.putExtra("price",model.getPrice());
                        intent.putExtra("size",model.getSizeOrWeight());
                        intent.putExtra("imageUrl1",model.getImageUrl1());
                        intent.putExtra("imageUrl2",model.getImageUrl2());
                        intent.putExtra("imageUrl3",model.getImageUrl3());
                        intent.putExtra("imageUrl4",model.getImageUrl4());
                        intent.putExtra("imageUrl5",model.getImageUrl5());
                        intent.putExtra("vendorUserId",model.getVendorUserId());
                        startActivity(intent);
                    }
                });




            }
        };
        firebaseRecyclerAdapter.startListening();
        recyclerView1.setAdapter(firebaseRecyclerAdapter);
    }

    private void showList(){
        Query mQuery=ref;
        FirebaseRecyclerOptions<Product> options =
                new FirebaseRecyclerOptions.Builder<Product>()
                        .setQuery(mQuery, Product.class)
                        .build();

        FirebaseRecyclerAdapter firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Product,ProductViewHolder>(options) {


            @Override
            public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.searched_product,parent,false);
                return new ProductViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final ProductViewHolder holder, int position, @NonNull final Product model) {
                final String ref=getRef(position).getKey();
                holder.name.setText(model.getProductName());
                holder.price.setText("price:"+model.getPrice());
                //holder.size.setText(model.getSizeOrWeight());
                //holder.barcode.setText(model.getBarcode());
                //holder.time.setText(model.getDate());
                Picasso.with(getApplicationContext()).load(model.getImageUrl1()).placeholder(R.drawable.download).networkPolicy(NetworkPolicy.OFFLINE).into(holder.imageView1, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(getApplicationContext()).load(model.getImageUrl1()).placeholder(R.drawable.download).into(holder.imageView1);

                    }
                });



                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(ProductsActivity.this,ViewProduct.class);
                        intent.putExtra("barcode",model.getBarcode());
                        intent.putExtra("name",model.getProductName());
                        intent.putExtra("price",model.getPrice());
                        intent.putExtra("size",model.getSizeOrWeight());
                        intent.putExtra("imageUrl1",model.getImageUrl1());
                        intent.putExtra("imageUrl2",model.getImageUrl2());
                        intent.putExtra("imageUrl3",model.getImageUrl3());
                        intent.putExtra("imageUrl4",model.getImageUrl4());
                        intent.putExtra("imageUrl5",model.getImageUrl5());
                        intent.putExtra("vendorUserId",model.getVendorUserId());
                        startActivity(intent);
                    }
                });



            }
        };
        firebaseRecyclerAdapter.startListening();
        recyclerView.setAdapter(firebaseRecyclerAdapter);
        progressDialog.dismiss();

    }



    @Override
    protected void onStart() {
        super.onStart();

        if (FirebaseAuth.getInstance().getCurrentUser()==null)
        {
            Intent intent=new Intent(ProductsActivity.this,LoginActivity.class);
            startActivity(intent);
        }
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder{

        TextView name,price;
        ImageView imageView1;
        View view;
        public ProductViewHolder(View itemView) {
            super(itemView);
            this.view=itemView;
            imageView1=itemView.findViewById(R.id.imageView2);
            name=itemView.findViewById(R.id.productName);
            price=itemView.findViewById(R.id.price);

        }
    }
}
