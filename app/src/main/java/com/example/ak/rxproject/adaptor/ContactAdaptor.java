package com.example.ak.rxproject.adaptor;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ak.rxproject.R;
import com.example.ak.rxproject.contact.Contact;
import com.github.ivbaranov.mli.MaterialLetterIcon;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContactAdaptor extends RecyclerView.Adapter<ContactAdaptor.ContactHolder> {


    private Context context;
    private ArrayList<Contact> contactList;

    public ContactAdaptor(Context context, ArrayList<Contact> contactList) {
        this.context = context;
        this.contactList = contactList;
    }

    @NonNull
    @Override
    public ContactHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.contact_list_item,viewGroup,false);
        return (new ContactHolder(view));
    }

    @Override
    public void onBindViewHolder(@NonNull ContactHolder contactHolder, int i) {
        Contact contact = contactList.get(i);
        contactHolder.contactName.setText(contact.getContactName());
        contactHolder.contactNumber.setText(contact.getContactNumber());
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));

        contactHolder.contactIv.setInitials(true);
        contactHolder.contactIv.setShapeColor(color);
        contactHolder.contactIv.setLetter(String.valueOf(contact.getContactName().charAt(0)));
        contactHolder.contactIv.setLetterSize(24);
        contactHolder.contactIv.setInitialsNumber(2);








    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    class ContactHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.contactNameTv)
        TextView contactName;
        @BindView(R.id.contactNumberTv)
        TextView contactNumber;
        @BindView(R.id.contactIv)
        MaterialLetterIcon contactIv;

        ContactHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
