
package com.satton.activity;

import com.satton.R;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Contacts;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

public class ContactActivity extends Activity {

    private TextView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.contact);

        //        get3();
        //        get2();
        get1();

    }

    private void get3() {
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = new String[] {
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER
        };

        Cursor people = getContentResolver().query(uri, projection, null, null, null);

        int indexName = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        int indexNumber = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

        people.moveToFirst();
        do {
            String name = people.getString(indexName);
            String number = people.getString(indexNumber);
            Log.d("contacts", String.format("%s %s ", name, number));
        } while (people.moveToNext());

    }

    private void get1() {
        ContentResolver resolver = getContentResolver();
        Cursor cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        while (cursor.moveToNext()) {

            String contactsId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            String contactsName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            String contactsPhone = "";
            if (Integer.parseInt(cursor.getString(cursor
                    .getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                Cursor cPhone = resolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? ",
                        new String[] {
                            contactsId
                        }, null);
                while (cPhone.moveToNext()) {
                    contactsPhone = cPhone.getString(cPhone.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.DATA1));
                }
                cPhone.close();
            }

            String contactsMail = "";
            Cursor cMail = resolver.query(
                    ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ? ",
                    new String[] {
                        contactsId
                    }, null);
            while (cMail.moveToNext()) {
                contactsMail = cMail.getString(cMail.getColumnIndex(
                        ContactsContract.CommonDataKinds.Email.DATA1));
            }
            cMail.close();

            Log.d("COntacts", String.format("%s %s  %s", contactsName, contactsMail, contactsPhone));
        }
    }

    private void get2() {
        Cursor c = getContentResolver().query(Contacts.CONTENT_URI, new String[] {
                Contacts._ID, Contacts.LOOKUP_KEY, Contacts.DISPLAY_NAME,
        }, null, null, null);
        c.moveToPosition(-1);
        while (c.moveToNext()) {
            // カーソルからデータ取り出し
            int contactsId = c.getInt(0);
            String displayName = c.getString(c.getColumnIndex(StructuredName.DISPLAY_NAME));
            Log.d("contacts", String.format("%s / %s ", contactsId, displayName));
        }
    }

}
