package com.example.ak.rxproject.activity;

import android.database.Cursor;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.LinearLayout;

import com.example.ak.rxproject.R;
import com.example.ak.rxproject.adaptor.ContactAdaptor;
import com.example.ak.rxproject.contact.Contact;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

import static io.reactivex.Flowable.fromArray;

public class ContactActivity extends AppCompatActivity {
    @BindView(R.id.contactRv)
    RecyclerView recyclerView;

    ArrayList<Contact> contactArrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        ButterKnife.bind(this);
        Log.e("ContactActivity", "onCreate: ");

        Log.e("ContactActivity", "onNext: before" );



        final Snackbar snackbar = Snackbar.make(findViewById(R.id.contactLayout),"Contact saved to storage",Snackbar.LENGTH_LONG);

//
        Observable<ArrayList<Contact>> contactObservable = Observable.just(getAllContact())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        Log.e("ContactActivity", "onNext: after" );


//

        contactObservable.subscribe(new Observer<ArrayList<Contact>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(ArrayList<Contact> contacts) {

                ContactAdaptor contactAdaptor = new ContactAdaptor(getBaseContext(),contacts);
                recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
                recyclerView.setAdapter(contactAdaptor);

                try {
                    FileWriter fileWriter = new FileWriter(Environment.getExternalStorageDirectory().toString() + "/" + "contact.csv");
                    Log.e("ContactActivity", "onNext: " + Environment.getExternalStorageDirectory().toString() + "/" + "contact.csv");
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

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                snackbar.show();
            }
        });


    }


    public ArrayList<Contact> getAllContact(){
        ArrayList<Contact> contactLists = new ArrayList<>();
//
        String[] projection = new String[] {
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER,
                //plus any other properties you wish to query
        };

        Cursor cursor = null;
        try {
            cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, null, null, null);
        } catch (SecurityException e) {
            //SecurityException can be thrown if we don't have the right permissions
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
                        Log.e("ContactActivity", "onNext: "  + displayName );

                        String displayNumber = cursor.getString(indexOfDisplayNumber);
                        contactLists.add(new Contact(displayName,displayNumber));
                        //haven't seen this number yet: do something with this contact!
                    }  //don't do anything with this contact because we've already found this number

                }
            } finally {
                cursor.close();
            }
        }


        return contactLists;
    }
}
