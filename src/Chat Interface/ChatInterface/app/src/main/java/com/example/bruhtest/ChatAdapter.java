package com.example.bruhtest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends ArrayAdapter<com.example.bruhtest.Message> {
//    private static final Tag
    private final Context myContext;
    int myResource;

    public ChatAdapter(@NonNull Context context, int resource, @NonNull ArrayList<com.example.bruhtest.Message> objects) {
        super(context, resource, objects);
//        this.myContext = myContext;
        myContext = context;
        myResource = resource;
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //get message information
        String author = getItem(position).getAuthor();
        String messageText = getItem(position).getText();
        int profilePicSource = getItem(position).getProfPicSource();

        //Creating message with the information
//        com.example.bruhtest.Message message = new com.example.bruhtest.Message(author, messageText, profilePicSource);

        LayoutInflater inflater = LayoutInflater.from(myContext);
        convertView = inflater.inflate(myResource, parent, false);

        TextView tvAuthor = (TextView) convertView.findViewById(R.id.messageAuthor);
        TextView tvMessage = (TextView) convertView.findViewById(R.id.messageText);
        CircleImageView tvImage = (CircleImageView) convertView.findViewById(R.id.profilePicture);

        tvAuthor.setText(author);
        tvMessage.setText(messageText);
        tvImage.setImageResource(profilePicSource);

        return convertView;
    }
}
