package com.example.chipinv01.Event;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chipinv01.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.choosenContactsViewHolder> {
    ArrayList<Data> ChoosenContacts = new ArrayList<>();
    static int AmountForEveryUserValue = -1;
    static int newAmount=0;
    public void setChoosenContacts(ArrayList<Data> ChoosenContacts) {
        this.ChoosenContacts = ChoosenContacts;
        notifyDataSetChanged();
    }
    public ArrayList<Data> getChoosenContacts() {
        return ChoosenContacts;
    }

    public interface OnItemClickListener {
        void OnItemClick(int position);
    }
    public void SetOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }
    private OnItemClickListener mListener;
    public void setAmountForEveryUserValue(double AmountForEveryUserValue){
        EventAdapter.AmountForEveryUserValue = (int) AmountForEveryUserValue;
        notifyDataSetChanged();
        if (ChoosenContacts.size()!=0) {
            for (int i = 0; i < ChoosenContacts.size(); i++) {
                ChoosenContacts.get(i).amount = AmountForEveryUserValue;
            }
        }

    }
    @NonNull
    @Override
    public choosenContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_event_list_item, parent, false);
        return new choosenContactsViewHolder(view,mListener);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull final choosenContactsViewHolder holder, final int position) {
        holder.bindContact(ChoosenContacts.get(position));
        holder.itemView.setId(position);
        holder.bar.setMin(0);
        holder.bar.setMax(AmountForEveryUserValue);
        holder.bar.setProgress((int) AmountForEveryUserValue);

    }

    @Override
    public int getItemCount() {
        return ChoosenContacts.size();
    }

   public class choosenContactsViewHolder extends RecyclerView.ViewHolder{
        TextView ChoosenContactPhone;
        TextView ChoosenContactName;
        TextView AmountForEveryUser;
        ImageView ChoosenContactsImage;
        TextView AmountForPersonalityUser;
        ImageView payment;
        ProgressBar bar;
        int difVar=0;



        public choosenContactsViewHolder(@NonNull View itemView,OnItemClickListener listener) {
            super(itemView);
            ChoosenContactPhone = itemView.findViewById(R.id.ChoosenContactPhone);
            ChoosenContactName = itemView.findViewById(R.id.ChoosenContactName);
            ChoosenContactsImage= itemView.findViewById(R.id.circleImageView);
            AmountForEveryUser=itemView.findViewById(R.id.AmountForEveryUser_item);
            payment = itemView.findViewById(R.id.payment);
            bar = itemView.findViewById(R.id.Bar);
            bar.getProgressDrawable().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION ){
                            listener.OnItemClick(position);
                            final AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
                            @SuppressLint("ResourceType")View dialogView = LayoutInflater.from(v.getRootView().getContext()).inflate(R.layout.custom_dialog_for_add_amount,null);
                            de.hdodenhof.circleimageview.CircleImageView dialogImageView = dialogView.findViewById(R.id.dialog_boxImage);
                            builder.setView(dialogView);
                            builder.setCancelable(true);
                            TextView dialog_boxUsername = dialogView.findViewById(R.id.dialog_boxUserName);
                            TextView dialog_boxPhone = dialogView.findViewById(R.id.dialog_boxUserPhone);
                            Button ContinueBtn = dialogView.findViewById(R.id.ContinueBtn);
                            Uri baseUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, ChoosenContacts.get(position).id);
                            Uri imageUri = Uri.withAppendedPath(baseUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
                            dialog_boxUsername.setText(ChoosenContacts.get(position).name);
                            dialog_boxPhone.setText(ChoosenContacts.get(position).number);
                            dialogImageView.setImageURI(imageUri);
                            final AlertDialog AmountDialog = builder.show();
                            final EditText AmountInput = dialogView.findViewById(R.id.dialog_UserAmountText);
                            ContinueBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(!AmountInput.getText().toString().equals(null)) {
                                        AmountDialog.cancel();

                                        //AmountForPersonalityUser.setText(AmountInput.getText().toString());
                                        //AmountForEveryUser.setText("   ");
                                        AmountForEveryUserValue = Integer.parseInt(AmountInput.getText().toString());
                                        String afeu = AmountForEveryUser.getText().toString();
                                        AmountForEveryUser.setText(AmountInput.getText().toString());
                                        bar.setProgress(AmountForEveryUserValue);
                                        ChoosenContacts.get(position).amount = Double.parseDouble(AmountInput.getText().toString());
                                        AmountDialog.cancel();
                                    }
                                    else{
                                        AmountDialog.cancel();
                                    }


                                }
                            });
                        }
                    }
                }

            });
            payment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.OnItemClick(position);
                            final AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext());
                            @SuppressLint("ResourceType") View dialogView = LayoutInflater.from(view.getRootView().getContext()).inflate(R.layout.custom_dialog_for_add_amount, null);
                            de.hdodenhof.circleimageview.CircleImageView dialogImageView = dialogView.findViewById(R.id.dialog_boxImage);
                            builder.setView(dialogView);
                            builder.setCancelable(true);
                            TextView dialog_boxUsername = dialogView.findViewById(R.id.dialog_boxUserName);
                            TextView dialog_boxPhone = dialogView.findViewById(R.id.dialog_boxUserPhone);
                            Button ContinueBtn = dialogView.findViewById(R.id.ContinueBtn);
                            Uri baseUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, ChoosenContacts.get(position).id);
                            Uri imageUri = Uri.withAppendedPath(baseUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
                            dialog_boxUsername.setText(ChoosenContacts.get(position).name);
                            dialog_boxPhone.setText(ChoosenContacts.get(position).number);
                            dialogImageView.setImageURI(imageUri);
                            final AlertDialog AmountDialog = builder.show();
                            final EditText AmountInput = dialogView.findViewById(R.id.dialog_UserAmountText);
                            Toast.makeText(view.getContext(), "Click!", Toast.LENGTH_SHORT).show();
                            ContinueBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(!AmountInput.getText().toString().equals(null)) {
                                        AmountDialog.cancel();

                                        //AmountForPersonalityUser.setText(AmountInput.getText().toString());
                                        //AmountForEveryUser.setText("   ");

                                        String dif = AmountInput.getText().toString();
                                        String dif2 = AmountForEveryUser.getText().toString();
                                        int difVar2 = Integer.parseInt(dif2);
                                        difVar = Integer.parseInt(dif);
                                        newAmount = (difVar2 - difVar);
                                        String amI = String.valueOf(newAmount);
                                        //AmountForEveryUserValue = Integer.parseInt(amI);
                                        bar.setProgress(Integer.parseInt(amI));
                                        AmountForEveryUser.setText(amI);
                                        ChoosenContacts.get(position).amount = Double.parseDouble(amI);
                                        AmountDialog.cancel();
                                    }
                                    else{
                                        AmountDialog.cancel();
                                    }


                                }
                            });
                        }
                    }
                }
            });

        }

       public void bindContact(Data contact) {

            if(AmountForEveryUserValue != -1){
                AmountForEveryUser.setText(String.valueOf(AmountForEveryUserValue));
                //AmountForPersonalityUser.setText(" ");
            }


           ChoosenContactName.setText(contact.name);
           ChoosenContactPhone.setText(contact.number);
           Uri baseUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contact.id);
           Uri imageUri = Uri.withAppendedPath(baseUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
           ChoosenContactsImage.setImageURI(imageUri);

       }


    }
}