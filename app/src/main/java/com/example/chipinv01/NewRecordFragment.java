package com.example.chipinv01;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.chipinv01.Event.Cardview_item_decor;
import com.example.chipinv01.Event.Data;
import com.example.chipinv01.Event.ContactsAdapter;
import com.example.chipinv01.Event.NewEvent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class NewRecordFragment extends Fragment {
    RecyclerView ContactView;
    com.example.chipinv01.Event.ContactsAdapter ContactsAdapter;
    ContentResolver contentResolver;
    String phone = "";
    ArrayList<Data> ChooseContact = new ArrayList<>();
    ArrayList<ImageView>Avatars = new ArrayList<>();
    ArrayList<Data>contacts = new ArrayList<>();
    FloatingActionButton choosenContactButton;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @SuppressLint("Range")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View RootView = inflater.inflate(R.layout.activity_contacts_list, container, false);
        ContactView = (RecyclerView) RootView.findViewById(R.id.ContactView);
        choosenContactButton = RootView.findViewById(R.id.contactsChoosenButt);
        ContactView.setLayoutManager(new LinearLayoutManager(getContext()));
        ContactsAdapter = new ContactsAdapter();
        ContactView.setAdapter(ContactsAdapter);
        ContactView.addItemDecoration(new Cardview_item_decor.SpacesItemDecoration(10));
        contentResolver = getActivity().getContentResolver();
        Intent createEvent = new Intent(getActivity(),NewEvent.class);

        @SuppressLint("UseCompatLoadingForDrawables") final Drawable checkCircle = this.getResources().getDrawable(R.drawable.ic_baseline_check_circle_24);
        @SuppressLint("UseCompatLoadingForDrawables") final Drawable oneXML = this.getResources().getDrawable(R.drawable.empty);
        //@SuppressLint("UseCompatLoadingForDrawables") final Drawable defaultIMG = this.getResources().getDrawable(R.drawable.ic_baseline_account_circle_24);
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, new String[]{
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.HAS_PHONE_NUMBER,
                ContactsContract.Contacts.PHOTO_ID
        },null,null,ContactsContract.Contacts._ID);



        if (cursor != null && cursor.moveToFirst()){
            do{
                long id = cursor.getLong(0);
                String name = cursor.getString(1);
                String hasNumber = cursor.getString(2);
                if (Integer.parseInt(hasNumber) != 0) {
                    Cursor pCur;
                    pCur = this.getActivity().getContentResolver().query(
                            ContactsContract.CommonDataKinds
                                    .Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds
                                    .Phone.CONTACT_ID + " = ?",
                            new String[]{String.valueOf(id)},
                            null);
                    while (pCur.moveToNext()) {
                        phone = pCur.getString(
                                pCur.getColumnIndex(
                                        ContactsContract.
                                                CommonDataKinds.
                                                Phone.NUMBER));
                    }
                }
                else {
                    phone = "No number";
                }
                long imageId = cursor.getLong(3);
                Data contact = new Data(name, phone,imageId,id);
                contacts.add(contact);
            }while (cursor.moveToNext());
        }

        if (cursor != null) {cursor.close();}
        ContactsAdapter.setOnItemListenerListener(new ContactsAdapter.OnItemListener() {
            ImageView checked= null;
            @Override
            public void OnItemClickListener(View view, int position) {

                ImageView imageView = (ImageView)view.findViewById(R.id.checkImage);
                if (imageView.getTag()!=checkCircle) {
                    imageView.setTag(checkCircle);
                    imageView.setImageDrawable(checkCircle);
                    ChooseContact.add(contacts.get(view.getId()));
                    Avatars.add(imageView);
                }
                else
                {
                    imageView.setImageDrawable(oneXML);
                    imageView.setTag(oneXML);
                    ChooseContact.remove(contacts.get(position));

                    Avatars.remove(imageView);
                }

            }

            @Override
            public void OnItemLongClickListener(View view, int position) {

            }
        });
        ContactsAdapter.setContacts(contacts);
        choosenContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToAttract(v);
            }
            public void goToAttract(View v)
            {
                createEvent.putExtra("ChoosenContacts",ChooseContact );
                startActivity(createEvent);
            }
        });
        return RootView;
    }
}