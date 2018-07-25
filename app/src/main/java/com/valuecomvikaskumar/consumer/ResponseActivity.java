package com.valuecomvikaskumar.consumer;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

public class ResponseActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private String consumerName;
    private DatabaseReference mRef;
    private EditText mText;
    private Button send;
    private String id;
    private String listId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_response);


        toolbar=findViewById(R.id.responseToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        consumerName=getIntent().getStringExtra("name");
        getSupportActionBar().setTitle(consumerName);

        recyclerView=findViewById(R.id.responseRecyclerView);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(ResponseActivity.this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        listId=getIntent().getStringExtra("listId");
        mRef= FirebaseDatabase.getInstance().getReference().child("Chat").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(listId);
        showList();
    }



    private void showList(){
        Query mQuery=mRef.orderByChild("timeStamp");
        FirebaseRecyclerOptions<Response> options =
                new FirebaseRecyclerOptions.Builder<Response>()
                        .setQuery(mQuery, Response.class)
                        .build();

        FirebaseRecyclerAdapter firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Response,ResponseViewHolder>(options) {


            @Override
            public ResponseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.response_view,parent,false);
                return new ResponseViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final ResponseViewHolder holder, int position, @NonNull final Response model) {
                final String ref=getRef(position).getKey();

                //Toast.makeText(ResponseActivity.this,"ref"+ref,Toast.LENGTH_SHORT).show();
               FirebaseDatabase.getInstance().getReference().child("Vendor").child(ref).addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(final DataSnapshot dataSnapshot) {
                       holder.consumerName.setText(dataSnapshot.child("name").getValue().toString());
                       holder.consumerMessage.setText(dataSnapshot.child("companyAddress").child("addressLine1").getValue().toString());
                       holder.itemView.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {
                               Intent intent=new Intent(ResponseActivity.this,ChatActivity.class);
                               intent.putExtra("listId",listId);
                               intent.putExtra("id",ref);
                               intent.putExtra("name",dataSnapshot.child("name").getValue().toString());
                               startActivity(intent);
                           }
                       });

                   }

                   @Override
                   public void onCancelled(DatabaseError databaseError) {

                   }
               });


            }
        };
        firebaseRecyclerAdapter.startListening();
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    public static class ResponseViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView consumerName;
        TextView message;
        TextView consumerMessage;
        TextView date;
        TextView consumerDate;
        LinearLayout linearLayout;
        LinearLayout consumerLinearLayout;
        View view;
        public ResponseViewHolder(View itemView) {
            super(itemView);
            this.view=itemView;
            consumerLinearLayout=itemView.findViewById(R.id.consumerLinearLayout);
            consumerName=itemView.findViewById(R.id.consumerName);
            consumerDate=itemView.findViewById(R.id.consumerTime);
            consumerMessage=itemView.findViewById(R.id.consumerText);


        }
    }
}
