package com.valuecomvikaskumar.consumer;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class OrderFragment extends Fragment {



    private DatabaseReference mRef;
    private String userId;
    private RecyclerView recyclerView;
    private View mView;
    public OrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView= inflater.inflate(R.layout.fragment_order, container, false);
        recyclerView=mView.findViewById(R.id.orderRecyclerView);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        mRef= FirebaseDatabase.getInstance().getReference().child("Consumer").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("placedOrder");

        showList();

        return mView;
    }


    private void showList(){
        Query mQuery=mRef.orderByChild("timeStamp");
        FirebaseRecyclerOptions<Order> options =
                new FirebaseRecyclerOptions.Builder<Order>()
                        .setQuery(mQuery, Order.class)
                        .build();

        FirebaseRecyclerAdapter firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Order,OrderViewHolder>(options) {


            @Override
            public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.order_view,parent,false);
                return new OrderViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final OrderViewHolder holder, int position, @NonNull final Order model) {
                final String ref=getRef(position).getKey();

                holder.list.setText(model.getList());
                holder.date.setText(model.getDate());
                FirebaseDatabase.getInstance().getReference().child("Consumer").child(model.getConsumerId()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshot) {
                        holder.name.setText("  "+dataSnapshot.child("name").getValue().toString());
                        holder.address.setText("  "+dataSnapshot.child("address").child("addressLine1").getValue().toString());
                        holder.button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent=new Intent(getContext(),ResponseActivity.class);
                                intent.putExtra("listId",ref);
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

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView list;
        TextView date;
        TextView address;
        View view;
        Button button;

        public OrderViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            name = itemView.findViewById(R.id.consumerName);
            list = itemView.findViewById(R.id.consumerItemList);
            date = itemView.findViewById(R.id.consumerTime);
            address = itemView.findViewById(R.id.consumerAddress);
            button=itemView.findViewById(R.id.response);
        }
    }

}
