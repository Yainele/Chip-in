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
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import static android.text.InputType.TYPE_CLASS_NUMBER;

import recycleViewAdapters.ExtendedEventAdapter;
import recycleViewAdapters.homePageAdapter;

@RequiresApi(api = Build.VERSION_CODES.N)
public class NewEvent extends AppCompatActivity {

    private RecyclerView choosenContactsList;
    public ArrayList<Member>ContactsArray = new ArrayList<>();
    public ArrayList<Event>EventArray = new ArrayList<>();
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



    NewEventAdapter newEventAdapter = new NewEventAdapter();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);
        Objects.requireNonNull(getSupportActionBar()).hide();



        ActivityCompat.requestPermissions(NewEvent.this, new String[]{Manifest.permission.SEND_SMS}, 1);
        sent_pi = PendingIntent.getBroadcast(NewEvent.this, 0, sent_intent, 0);
        deliver_pi = PendingIntent.getBroadcast(NewEvent.this, 0, deliver_intent, 0);
        choosenContactsList= findViewById(R.id.contactsRecycler);

        choosenContactsList.setLayoutManager(new LinearLayoutManager(this));
        choosenContactsList.addItemDecoration(new Cardview_item_decor.SpacesItemDecoration(10));
        choosenContactsList.setAdapter(newEventAdapter);
        BottomNavigationView AfterContactMenu= findViewById(R.id.afterContact_menu);
        AfterContactMenu.setOnNavigationItemSelectedListener(navListener);

        FloatingActionButton SaveData= (FloatingActionButton) findViewById(R.id.SaveDataBtn);

        Bundle choosenContacts = getIntent().getExtras();
        Bundle choosenEvent = getIntent().getExtras();
        //приём данных при создания евента
        if (choosenContacts.size()!=0){

            ContactsArray = (ArrayList<Member>) choosenContacts.getSerializable("ChoosenContacts");
            newEventAdapter.setChoosenContacts(ContactsArray);

        }
         else {
            Toast.makeText(NewEvent.this, "список контактов пуст", Toast.LENGTH_SHORT).show();
        }
         //приём данных если нажимаем по евенту в хоумпедж
        try {
            if (choosenEvent != null) {
                Event event;
                event = (Event) choosenEvent.getSerializable("ExtendedEvent");
                if (event.Members != null) {
                    SaveData.hide();
                    newEventAdapter.setChoosenContacts(event.Members);
                }
            }
        }
        catch (Exception e){

        }

         newEventAdapter.SetOnItemClickListener(new NewEventAdapter.OnItemClickListener() {
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
                  Intent intentForAdapter = new Intent(NewEvent.this, NewEventAdapter.class);
                  intentForAdapter.putExtra("AmountForEveryUser",AmountForEveryUser);

                  newEventAdapter.setAmountForEveryUserValue(AmountForEveryUser);
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
                        Intent intentForAdapter = new Intent(NewEvent.this, NewEventAdapter.class);
                        intentForAdapter.putExtra("AmountForEveryUser",AmountForEveryUser);
                        newEventAdapter.setAmountForEveryUserValue(AmountForEveryUser);
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
                    Toast.makeText(context, "Send", Toast.LENGTH_LONG).show();
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
    @SuppressLint({"NewApi", "SimpleDateFormat"})
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM.dd.y.HH:mm");


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void dataSender(){
        //Доделать полную сумму

        CollectionReference collectionReference = firebaseFirestore.collection(firebaseUserID.getUid());
        Event event = new Event();
        event.setCreditName(CreditNameStr);
        event.setCreditTime(dateFormat.format(date));
        event.setDeadline(DateOfDL);
        event.setCreditorName(getUsername());
        event.setMemberAmount(String.valueOf(ContactsArray.size()));
        event.setFullamount("No Value");
        event.setUniqueId(generatePushId());
        event.setMembers(ContactsArray);
        //firebaseFirestore.collection(firebaseUserID.getUid()).add(event);
        firebaseFirestore.collection(firebaseUserID.getUid()).document(event.getUniqueId()).set(event);


    }
        // Modeled after base64 web-safe chars, but ordered by ASCII.
        private final static String PUSH_CHARS = "-0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ_abcdefghijklmnopqrstuvwxyz";

        public String generatePushId() {
            // Timestamp of last push, used to prevent local collisions if you push twice in one ms.
            long lastPushTime = 0L;

            // We generate 72-bits of randomness which get turned into 12 characters and
            // appended to the timestamp to prevent collisions with other clients. We store the last
            // characters we generated because in the event of a collision, we'll use those same
            // characters except "incremented" by one.
            char[] lastRandChars = new char[72];

            long now = new Date().getTime();

            boolean duplicateTime = (now == lastPushTime);

            char[] timeStampChars = new char[8];
            for (int i = 7; i >= 0; i--) {
                final long module = now % 64;
                timeStampChars[i] = PUSH_CHARS.charAt(Long.valueOf(module).intValue());
                now = (long) Math.floor(now / 64);
            }
            if (now != 0)
                throw new AssertionError("We should have converted the entire timestamp.");

            String id = new String(timeStampChars);
            if (!duplicateTime) {
                for (int i = 0; i < 12; i++) {
                    final double times = Math.random() * 64;
                    lastRandChars[i] = (char) Math.floor(Double.valueOf(times).intValue());

                }
            } else {
                // If the timestamp hasn't changed since last push, use the same random number,
                //except incremented by 1.
                int lastValueOfInt=0;
                for (int i = 11; i >= 0 && lastRandChars[i] == 63; i--) {
                    lastValueOfInt = i;
                    lastRandChars[i] = 0;
                }
                lastRandChars[lastValueOfInt]++;
            }
            for (int i = 0; i < 12; i++) {
                id += PUSH_CHARS.charAt(lastRandChars[i]);
            }
            if (id.length() != 20)
                throw new AssertionError("Length should be 20.");

            return id;
        };




}