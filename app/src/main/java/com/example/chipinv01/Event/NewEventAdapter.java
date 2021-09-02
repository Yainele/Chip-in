package com.example.chipinv01.Event;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.util.Log;
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

import java.util.ArrayList;

public class NewEventAdapter extends RecyclerView.Adapter<NewEventAdapter.choosenContactsViewHolder> {
    ArrayList<Member> ChoosenContacts = new ArrayList<>();
    static int AmountForEveryUserValue = -1;
    static int newAmount=0;
    public void setChoosenContacts(ArrayList<Member> ChoosenContacts) {
        this.ChoosenContacts = ChoosenContacts;
        notifyDataSetChanged();
    }
    public ArrayList<Member> getChoosenContacts() {
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
        NewEventAdapter.AmountForEveryUserValue = (int) AmountForEveryUserValue;
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

    }

    @Override
    public int getItemCount() {
        try {
            return ChoosenContacts.size();
        }
        catch (Exception e){

        }
        return 0;
    }

   public class choosenContactsViewHolder extends RecyclerView.ViewHolder{
        TextView ChoosenContactPhone;
        TextView ChoosenContactName;
        TextView AmountForEveryUser;
        ImageView ChoosenContactsImage;
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


            //добавление суммы
            itemView.setOnClickListener(v -> {
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
                        ContinueBtn.setOnClickListener(v1 -> {
                            if(!AmountInput.getText().toString().isEmpty()){
                                try {
                                    AmountForEveryUserValue = Integer.parseInt(AmountInput.getText().toString());
                                    AmountForEveryUser.setText(AmountInput.getText().toString());
                                    bar.setProgress(AmountForEveryUserValue);
                                    ChoosenContacts.get(position).amount = Double.parseDouble(AmountInput.getText().toString());

                                    AmountDialog.cancel();

                                }
                                catch (Exception e){
                                    Log.v("Error in onclick is :  ", "", e);
                                }
                            }
                            else{
                                AmountDialog.cancel();
                            }


                        });
                    }
                }
            });
            //отнимание суммы
            payment.setOnClickListener(view -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.OnItemClick(position);

                        final AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext());
                        @SuppressLint("ResourceType") View dialogView = LayoutInflater.from(view.getRootView().getContext()).inflate(R.layout.custom_dialog_for_add_amount, null);
                        de.hdodenhof.circleimageview.CircleImageView dialogImageView = dialogView.findViewById(R.id.dialog_boxImage);
                        builder.setView(dialogView);
                        builder.setCancelable(true);
                        ///////////оптимизировать элементы алертдиалога
                        TextView dialog_boxUsername = dialogView.findViewById(R.id.dialog_boxUserName);
                        TextView dialog_boxPhone = dialogView.findViewById(R.id.dialog_boxUserPhone);
                        Button ContinueBtn = dialogView.findViewById(R.id.ContinueBtn);
                        Uri baseUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, ChoosenContacts.get(position).id);
                        Uri imageUri = Uri.withAppendedPath(baseUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
                        dialog_boxUsername.setText(ChoosenContacts.get(position).name);
                        dialog_boxPhone.setText(ChoosenContacts.get(position).number);
                        dialogImageView.setImageURI(imageUri);
                        ///////////
                        final AlertDialog AmountDialog = builder.show();
                        final EditText AmountInput = dialogView.findViewById(R.id.dialog_UserAmountText);

                        Toast.makeText(view.getContext(), "Click!", Toast.LENGTH_SHORT).show();
                        ContinueBtn.setOnClickListener(v -> {
                            if(!AmountInput.getText().toString().isEmpty()) {
                                try {

                                    String dif = AmountInput.getText().toString();
                                    difVar = Integer.parseInt(dif);
                                    newAmount = (int) (ChoosenContacts.get(position).amount - difVar);
                                    String amI = String.valueOf(newAmount);
                                    bar.setProgress(newAmount);
                                    AmountForEveryUser.setText(amI);
                                    ChoosenContacts.get(position).amount = Double.parseDouble(amI);

                                    AmountDialog.cancel();

                                }
                                catch (Exception e){
                                    Log.v("Second on click :   ", "", e);
                                }
                            }
                            else{
                                AmountDialog.cancel();
                            }


                        });
                    }
                }
            });

        }

       public void bindContact(Member contact) {

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