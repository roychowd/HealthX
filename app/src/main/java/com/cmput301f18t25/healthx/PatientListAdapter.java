/*
 * Class Name: PatientListAdapter
 *
 * Version: Version 1.0
 *
 * Date : December 3, 2018
 *
 * Copyright (c) Team 25, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behavior at University of Alberta
 */
package com.cmput301f18t25.healthx;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * This is the adapter for the patient recycler view.
 *
 * @author Dhrub
 * @author Ivan
 * @author Cecilia
 * @version 1.0
 *
 */


public class PatientListAdapter extends RecyclerView.Adapter<PatientListAdapter.ViewHolder> {


    public Context ctx;
    private List<User> users;
    private Intent Mintent;

    // NEED TO DISTINGUISH EACH USER WHETHER HE IS A PATIENT
    public PatientListAdapter(List<User> users, Intent intent){

        this.users = users;
        this.Mintent = intent;

    }
    @NonNull
    @Override
    public PatientListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // CHANGE LAYOUT
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.patientlist_cardview, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;

    }

    @Override
    public void onBindViewHolder(PatientListAdapter.ViewHolder holder, final int position) {

        holder.pName.setText(users.get(position).getName());
        holder.pUserId.setText(users.get(position).getUsername());
        holder.pUserEmail.setText(users.get(position).getEmail());
        holder.pUserPhone.setText(users.get(position).getPhoneNumber());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                User toView = users.get(position);
                Bundle bundle = Mintent.getExtras();
                bundle.putString("PatientEmail",toView.getEmail());
                bundle.putString("PatientId",toView.getUsername());

                Intent intent = new Intent(v.getContext(), ActivityViewPatientProblem.class);
                intent.putExtras(bundle);
                v.getContext().startActivity(intent);
                Toast.makeText(v.getContext(), "View " + toView.getName(), Toast.LENGTH_SHORT).show();

            }

        });

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView pName;
        public TextView pUserId;
        public TextView pUserEmail;
        public TextView pUserPhone;
        public Context context;


        public ViewHolder(final View itemView) {
            super(itemView);
            this.context = ctx;

            pName = itemView.findViewById(R.id.patientName);
            pUserId = itemView.findViewById(R.id.patientID);
            pUserEmail = itemView.findViewById(R.id.patientEmail);
            pUserPhone = itemView.findViewById(R.id.patientPhone);

            itemView.setClickable(true);

        }
    }

}
