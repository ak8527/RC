package com.example.ak.rxproject.activity;

import android.database.Cursor;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.ak.rxproject.R;
import com.example.ak.rxproject.adaptor.ContactAdaptor;
import com.example.ak.rxproject.contact.Contact;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ContactActivity extends AppCompatActivity {
    @BindView(R.id.contactRv)
    RecyclerView recyclerView;

    private final ArrayList<Contact> contactArrayList = new ArrayList<>();
    ContactAdaptor contactAdaptor;


    private Disposable disposable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        ButterKnife.bind(this);
        contactAdaptor = new ContactAdaptor(getBaseContext(),contactArrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        recyclerView.setAdapter(contactAdaptor);

        contactBackgroundTask();

//
    }


    public ArrayList<Contact> getAllContact(){

        ArrayList<Contact> contactLists = new ArrayList<>();

        String[] projection = new String[] {
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER,
        };

        Cursor cursor = null;
        try {
            cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, null, null, null);
        } catch (SecurityException e) {
            Toast.makeText(this,"Sorry, Contacts not found!!!",Toast.LENGTH_SHORT).show();
        }

        if (cursor != null) {
            try {
                HashSet<String> normalizedNumbersAlreadyFound = new HashSet<>();
                int indexOfNormalizedNumber = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER);
                int indexOfDisplayName = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                int indexOfDisplayNumber = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

                while (cursor.moveToNext()) {
                    String normalizedNumber = cursor.getString(indexOfNormalizedNumber);
                    if (normalizedNumbersAlreadyFound.add(normalizedNumber)) {
                        String displayName = cursor.getString(indexOfDisplayName);
                        String displayNumber = cursor.getString(indexOfDisplayNumber);
                        contactLists.add(new Contact(displayName,displayNumber));
                    }

                }
            } finally {
                cursor.close();
            }
        }

        return contactLists;
    }






    public void makeCsvFile(ArrayList<Contact> contacts){
        try {
            FileWriter fileWriter = new FileWriter(Environment.getExternalStorageDirectory().toString() + "/" + "Contacts.csv");
            fileWriter.append("Name");
            fileWriter.append(',');
            fileWriter.append("Contact Number");
            fileWriter.append(',');
            fileWriter.append('\n');

            for (Contact contact : contacts) {
                fileWriter.append(contact.getContactName());
                fileWriter.append(',');
                fileWriter.append(contact.getContactNumber());
                fileWriter.append(',');
                fileWriter.append('\n');
            }

            fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private void contactBackgroundTask(){

        Observable<ArrayList<Contact>> contactObservable = Observable.just(getAllContact())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                ;


        contactObservable.subscribe(new Observer<ArrayList<Contact>>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;

            }

            @Override
            public void onNext(ArrayList<Contact> contacts) {

                contactArrayList.clear();
                contactArrayList.addAll(contacts);
                contactAdaptor.notifyDataSetChanged();

                makeCsvFile(contacts);

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
               Snackbar snackbar = Snackbar.make(findViewById(R.id.contactLayout),"Contact saved to storage",Snackbar.LENGTH_SHORT);

                snackbar.show();
            }
        });


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
