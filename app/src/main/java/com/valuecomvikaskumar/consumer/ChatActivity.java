package com.valuecomvikaskumar.consumer;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private String consumerName;
    private DatabaseReference mRef;
    private EditText mText;
    private Button send;
    private String id;
    private String listId;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        toolbar=findViewById(R.id.responseToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        consumerName=getIntent().getStringExtra("name");
        getSupportActionBar().setTitle(consumerName);

        listId=getIntent().getStringExtra("listId");
        id=getIntent().getStringExtra("id");
        recyclerView=findViewById(R.id.responseRecyclerView);
        linearLayoutManager=new LinearLayoutManager(ChatActivity.this);
        //linearLayoutManager.setReverseLayout(true);
        //linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        mText=findViewById(R.id.text);
        send=findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(mText.getText())){
                    String key= FirebaseDatabase.getInstance().getReference().push().getKey();
                    Map map=new HashMap();
                    map.put("message",mText.getText().toString());
                    map.put("userId", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    map.put("time",new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()));
                    map.put("timeStamp", ServerValue.TIMESTAMP);
                    map.put("key",key);

                    Map map2=new HashMap();
                    map2.put("message",mText.getText().toString());
                    map2.put("timeStamp",ServerValue.TIMESTAMP);

                    Map map1=new HashMap();
                    map1.put("Consumer/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/"+"response/"+id+"/"+key,map);
                    map1.put("Vendor/"+id+"/"+"response/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/"+key,map);
                    map1.put("Chat/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/"+listId+"/"+id+"/"+"lastMessage",map2);
                    map1.put("Chat/"+id+"/"+listId+"/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/"+"lastMessage",map2);
                    FirebaseDatabase.getInstance().getReference().updateChildren(map1, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if(databaseError==null){
                                mText.setText("");
                            }
                        }
                    });
                }
            }
        });

        mRef= FirebaseDatabase.getInstance().getReference().child("Consumer").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("response").child(id);
        showList();
    }


    private void showList(){
        Query mQuery=mRef.orderByChild("timeStamp");
        FirebaseRecyclerOptions<Response> options =
                new FirebaseRecyclerOptions.Builder<Response>()
                        .setQuery(mQuery, Response.class)
                        .build();

        final FirebaseRecyclerAdapter firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Response,ResponseViewHolder>(options) {


            @Override
            public ResponseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_view,parent,false);
                return new ResponseViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final ResponseViewHolder holder, int position, @NonNull final Response model) {
                final String ref=getRef(position).getKey();

                if(model.getUserId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                    holder.linearLayout.setVisibility(View.INVISIBLE);
                    holder.consumerMessage.setText(model.getMessage());
                    holder.consumerDate.setText(model.getTime());
                    holder.consumerName.setVisibility(View.INVISIBLE);
                }
                else {
                    holder.consumerLinearLayout.setVisibility(View.INVISIBLE);
                    holder.name.setVisibility(View.INVISIBLE);
                    holder.date.setText(model.getTime());
                    holder.message.setText(model.getMessage());


                }

            }
        };

        firebaseRecyclerAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = firebaseRecyclerAdapter.getItemCount();
                int lastVisiblePosition =
                        linearLayoutManager.findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the
                // user is at the bottom of the list, scroll to the bottom
                // of the list to show the newly added message.
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    recyclerView.scrollToPosition(positionStart);
                }
            }
        });
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
            name=itemView.findViewById(R.id.vendorName);
            message=itemView.findViewById(R.id.vendorText);
            date=itemView.findViewById(R.id.vendorTime);
            consumerName=itemView.findViewById(R.id.consumerName);
            consumerDate=itemView.findViewById(R.id.consumerTime);
            consumerMessage=itemView.findViewById(R.id.consumerText);
            linearLayout=itemView.findViewById(R.id.vendorLinearLayout);

        }
    }
}
