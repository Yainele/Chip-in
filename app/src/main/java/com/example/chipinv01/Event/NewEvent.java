package com.example.chipinv01.Event;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.chipinv01.Homepage;
import com.example.chipinv01.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static android.text.InputType.TYPE_CLASS_NUMBER;

public class NewEvent extends AppCompatActivity {

    private RecyclerView choosenContactsList;
    public ArrayList<Data>ContactsArray = new ArrayList<>();
    public ArrayList<String> NumberOfPhones = new ArrayList<>();
    int Checker = 0;
    Double T_A_var;

    
    Long phone;
    String SENT_SMS = "SENT_SMS";
    String DELIVER_SMS = "DELIVER_SMS";
    Intent sent_intent = new Intent(SENT_SMS);
    Intent deliver_intent = new Intent(DELIVER_SMS);
    PendingIntent sent_pi, deliver_pi;

    FirebaseUser firebaseUserID = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();


    EventAdapter eventAdapter = new EventAdapter();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);
        getSupportActionBar().hide();



        ActivityCompat.requestPermissions(NewEvent.this, new String[]{Manifest.permission.SEND_SMS}, 1);
        sent_pi = PendingIntent.getBroadcast(NewEvent.this, 0, sent_intent, 0);
        deliver_pi = PendingIntent.getBroadcast(NewEvent.this, 0, deliver_intent, 0);
        choosenContactsList= findViewById(R.id.contactsRecycler);

        choosenContactsList.setLayoutManager(new LinearLayoutManager(this));
        choosenContactsList.addItemDecoration(new Cardview_item_decor.SpacesItemDecoration(10));
        choosenContactsList.setAdapter(eventAdapter);
        BottomNavigationView AfterContactMenu= findViewById(R.id.afterContact_menu);
        AfterContactMenu.setOnNavigationItemSelectedListener(navListener);

        Bundle choosenContacts = getIntent().getExtras();
        if (choosenContacts!=null){

            ContactsArray = (ArrayList<Data>) choosenContacts.getSerializable("ChoosenContacts");

        }
         else {
            Toast.makeText(NewEvent.this, "список контактов пуст", Toast.LENGTH_SHORT).show();
        }
        eventAdapter.setChoosenContacts(ContactsArray);
         eventAdapter.SetOnItemClickListener(new EventAdapter.OnItemClickListener() {
             @Override
             public void OnItemClick(int position) {

             }
         });
         /*
          MySwipeHelper swipeHelper = new MySwipeHelper(this, choosenContactsList,300) {
            @Override
            public void instantiateMyButton(RecyclerView.ViewHolder viewHolder, List<MyButton> buffer) {
                buffer.add(new MyButton(NewEvent.this,
                        "Удалить",
                        30,
                        0,
                        Color.parseColor("#8B0000"), new  MyButtonClickListener(){
                    @Override
                    public void onClick(int pos) {
                        ContactsArray.remove(pos);
                        if(T_A_var!=null && ContactsArray.size() !=0) {
                            double AmountForEveryUser = (T_A_var / ContactsArray.size());
                            eventAdapter.setAmountForEveryUserValue(AmountForEveryUser);
                        }
                        eventAdapter.notifyItemRemoved(pos);
                        Toast.makeText(NewEvent.this, "Удалено!", Toast.LENGTH_SHORT).show();
                    }
                }));
            }
        };
          */
        FloatingActionButton SaveData= (FloatingActionButton) findViewById(R.id.SaveDataBtn);
        SaveData.setOnClickListener(new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onClick(View view) {
        CreditNameDialogBuilder();
    }
});

    }
    //Навигационная панель
    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @SuppressLint("NonConstantResourceId")
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.Notify_item:
                    if(Checker == 0){
                        NotifyDialogBuilder();
                    }
                    else {
                        NotifyDialogBuilderExiter();
                    }
                    break;
                case R.id.totalAmount_item:
                    TotalAmountDialogBuilder();
                    break;

            }
            return true;
        }
    };

    //Диалоговое окно для общей суммы
    public void TotalAmountDialogBuilder(){
            final EditText totalAmount = new EditText(NewEvent.this);
            totalAmount.setHint("Введите общую сумму");
            totalAmount.setInputType(TYPE_CLASS_NUMBER);
            AlertDialog.Builder TotalAmountDialog = new AlertDialog.Builder(NewEvent.this);
            TotalAmountDialog.setTitle("Общая сумма.");
            TotalAmountDialog.setMessage("Введите общую сумму,которая разделится на всех.");
            TotalAmountDialog.setView(totalAmount);

            TotalAmountDialog.setPositiveButton("Разделить на всех", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
              String T_A =  totalAmount.getText().toString();
              T_A_var= Double.parseDouble(T_A);
              if(T_A_var!=null) {
                  double AmountForEveryUser = (T_A_var / ContactsArray.size());
                  Intent intentForAdapter = new Intent(NewEvent.this,EventAdapter.class);
                  intentForAdapter.putExtra("AmountForEveryUser",AmountForEveryUser);

                  eventAdapter.setAmountForEveryUserValue(AmountForEveryUser);
              }
              else{
                  Toast.makeText(NewEvent.this,"Введите сумму",Toast.LENGTH_LONG).show();
              }
                }
            });
            TotalAmountDialog.setNeutralButton("Скинуться по", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    String T_A =  totalAmount.getText().toString();
                    T_A_var= Double.parseDouble(T_A);
                    if(T_A_var!=null) {
                        double AmountForEveryUser = (T_A_var);
                        Intent intentForAdapter = new Intent(NewEvent.this,EventAdapter.class);
                        intentForAdapter.putExtra("AmountForEveryUser",AmountForEveryUser);
                        eventAdapter.setAmountForEveryUserValue(AmountForEveryUser);
                    }
                }
            });
            TotalAmountDialog.create().show();

    }
    //Диалоговое окно для того что бы уведомлять всех
    private void NotifyDialogBuilder(){
        
        AlertDialog.Builder NotifyAllDialog = new AlertDialog.Builder(NewEvent.this);
        NotifyAllDialog.setTitle("Уведомлять всех.");
        NotifyAllDialog.setMessage("Вы действительно хотите отправлять каждому участнику события смс с просьбой оплатить долг?\n" +
                "Содержание смс можно изменить в настройках.");
        NotifyAllDialog.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                 Checker=1;
                
            }
        });
        NotifyAllDialog.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        NotifyAllDialog.create().show();
    }
    private void NotifyDialogBuilderExiter(){
        AlertDialog.Builder NotifyAllDialogExiter = new AlertDialog.Builder(NewEvent.this);
        NotifyAllDialogExiter.setTitle("Уведомлять всех.");
        NotifyAllDialogExiter.setMessage("Вы уже выбрали функцию уведомления участников.\n" +
                "Хотите ее отменить?");
        NotifyAllDialogExiter.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Checker=0;

            }
        });
        NotifyAllDialogExiter.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        NotifyAllDialogExiter.create().show();
    }
    String DateOfDL;
    DatePickerDialog.OnDateSetListener listener;
    Calendar calendar = Calendar.getInstance();
    final int yearOF = calendar.get(Calendar.YEAR);
    final int monthOF = calendar.get(Calendar.MONTH);
    final int dayOF = calendar.get(Calendar.DAY_OF_MONTH);
    String CreditNameStr;
    private void CreditNameDialogBuilder(){
        AlertDialog.Builder CreditNameDialogBuilder = new AlertDialog.Builder(NewEvent.this);
        CreditNameDialogBuilder.setTitle("Название события.");
        CreditNameDialogBuilder.setMessage("Введите название события.");
        EditText CreditName = (EditText) new EditText(this);
        CreditNameDialogBuilder.setView(CreditName);
        CreditNameDialogBuilder.setPositiveButton("Продолжить", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                CreditNameStr = CreditName.getText().toString();
                DatePickerDialog datePickerDialog = new DatePickerDialog(NewEvent.this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,listener,yearOF,monthOF,dayOF);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.setTitle("Выберите дату окончания события");
                datePickerDialog.setCanceledOnTouchOutside(true);
                datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.DAY_OF_MONTH,day);
                        calendar.set(Calendar.MONTH,month);
                        calendar.set(Calendar.YEAR,year);
                        DateOfDL = DateFormat.getDateInstance(DateFormat.DEFAULT).format(calendar.getTime());
                        SmsSender();
                        dataSender();
                        Intent MainWindowIntent = new Intent(NewEvent.this, Homepage.class);
                        startActivity(MainWindowIntent);
                    }
                });
                datePickerDialog.show();
            }
        });
        CreditNameDialogBuilder.create().show();
    }

    @Override
    public void onResume() {

        super.onResume();

        NewEvent.this.registerReceiver(sentReceiver, new IntentFilter(SENT_SMS));

        NewEvent.this.registerReceiver(deliverReceiver, new IntentFilter(DELIVER_SMS));

    }


    @Override
    public void onStop() {

        super.onStop();

        NewEvent.this.unregisterReceiver(sentReceiver);

        NewEvent.this.unregisterReceiver(deliverReceiver);

    }

    BroadcastReceiver sentReceiver = new BroadcastReceiver() {

        @Override

        public void onReceive(Context context, Intent intent) {
            switch (getResultCode()) {
                case Activity.RESULT_OK:
                    Toast.makeText(context, "Sented", Toast.LENGTH_LONG).show();
                    break;
                default:
                    Toast.makeText(context, "Error S", Toast.LENGTH_LONG).show();
                    break;
            }
        }

    };


    BroadcastReceiver deliverReceiver = new BroadcastReceiver() {

        @Override

        public void onReceive(Context context, Intent intent) {
            switch (getResultCode()) {
                case Activity.RESULT_OK:
                    Toast.makeText(context, "Delivered", Toast.LENGTH_LONG).show();
                    break;
                default:
                    Toast.makeText(context, "Error D", Toast.LENGTH_LONG).show();
                    break;
            }
        }

    };
    public void SmsSender(){
        if (Checker!=0){
            for(int i=0;i<ContactsArray.size();i++) {
                NumberOfPhones.add(String.valueOf(ContactsArray.get(i).number));
            }
            for(int j=0;j<NumberOfPhones.size();j++){
                phone = Long.parseLong(NumberOfPhones.get(j));
                if (!phone.equals("")) {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(String.valueOf(phone),null,"Test Message", sent_pi, deliver_pi);
                }
            }
        }
    }
    public String getUsername() {
        AccountManager manager = AccountManager.get(this);
        Account[] accounts = manager.getAccountsByType("com.google");
        List<String> possibleEmails = new LinkedList<String>();

        for (Account account : accounts) {
            possibleEmails.add(account.name);
        }

        if (!possibleEmails.isEmpty() && possibleEmails.get(0) != null) {
            String email = possibleEmails.get(0);
            String[] parts = email.split("@");

            if (parts.length > 1)
                return parts[0];
        }
        return null;
    }

    Date date = new Date();
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM.dd.YY HH:mm");


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void dataSender(){
        //Доделать полную сумму
        Credit credit = new Credit();
        credit.setCreditName(CreditNameStr);
        credit.setCreditTime(dateFormat.format(date));
        credit.setDeadline(DateOfDL);
        credit.setCreditorName(getUsername());
        credit.setMemberAmount(String.valueOf(ContactsArray.size()));
        credit.setFullamount("No Value");
        ArrayList<Credit>Credits = new ArrayList<>();
        Credits.add(credit);
        firebaseFirestore.collection(firebaseUserID.getUid()).add(credit);
    }
}