package com.example.singh.android_contentprovider.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.singh.android_contentprovider.model.People;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Author: singh on: 02-Jan-18.
 */

public class LocalDataSource extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "people.db";
    private static final int DATABASE_VERSION = 1;
    private static LocalDataSource instance = null;
    public static final int ALL_PEOPLE = 0;
    public static final int DEFAULT_PEOPLE = 10;
    Context context;

    //    table ddl statements
    public static final String CREATE_TABLE =
            "CREATE TABLE " + LocalDataContract.People.TABLE_NAME + "(" +
                    LocalDataContract.People._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    LocalDataContract.People.NAME + " TEXT," +
                    LocalDataContract.People.GENDER + " TEXT," +
                    LocalDataContract.People.AGE + " TEXT" + ")";


    private LocalDataSource(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;

    }

    public static synchronized LocalDataSource getInstance(Context context) {
        if (instance == null) {
            return new LocalDataSource(context);
        } else {
            return instance;
        }
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //include update schema for the database
    }

    public long insert(People people) {

        SQLiteDatabase database = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(LocalDataContract.People.NAME, people.getName());
        contentValues.put(LocalDataContract.People.GENDER, people.getGender());
        contentValues.put(LocalDataContract.People.AGE, people.getAge());

        long rowId = database.insert(LocalDataContract.People.TABLE_NAME, null, contentValues);

        database.close();
        Log.d("MAINDB", "insert: " + rowId);
        return rowId;
    }

    public void update(People people) {

        //update logic

    }

    public List<People> retrieve(int id) {
        List<People> peopleList = new ArrayList<>();

        SQLiteDatabase database = getWritableDatabase();

        String rawQuery;
        Cursor cursor;

        if (id != 0) {
            rawQuery = "SELECT * FROM " +
                    LocalDataContract.People.TABLE_NAME + " WHERE "
                    + LocalDataContract.People._ID + "= ?";

            String[] selectionArgs = new String[]{String.valueOf(id)};
            cursor = database.rawQuery(rawQuery, selectionArgs);

        } else {
            rawQuery = "SELECT * FROM " + LocalDataContract.People.TABLE_NAME;
            cursor = database.rawQuery(rawQuery, null);

        }

        if (cursor.moveToFirst()) {
            do {
                People people = new People(
                        cursor.getInt(cursor.getColumnIndex(LocalDataContract.People._ID)),
                        cursor.getString(cursor.getColumnIndex(LocalDataContract.People.NAME)),
                        cursor.getString(cursor.getColumnIndex(LocalDataContract.People.GENDER)),
                        cursor.getString(cursor.getColumnIndex(LocalDataContract.People.AGE)));

                peopleList.add(people);

            } while (cursor.moveToNext());
        }

        database.close();

        return peopleList;
    }

    public void createDatabase(int peopleCount) {

        for (int i = 0; i < peopleCount; i++) {

            insert(getRandonPeople());
        }
    }

    private People getRandonPeople() {

        String[] FIRSTNAME_DICTIONARY = new String[]{"John", "Alex", "Matt", "Henry", "Jake", "Melinda", "Colleen", "Jennifer"};
        String[] LASTNAME_DICTIONARY = new String[]{"Smith", "Hanson", "Hancock", "Aniston", "Davidson", "Lee", "Singh"};
        String[] GENDER_DICTIONARY = new String[]{"Male", "Female"};
        String[] AGE_DICTIONARY = new String[]{"34", "54", "22", "25", "56", "44", "12"};

        String name = FIRSTNAME_DICTIONARY[new Random().nextInt(FIRSTNAME_DICTIONARY.length)] + " " +
                LASTNAME_DICTIONARY[new Random().nextInt(LASTNAME_DICTIONARY.length)];
        String age = AGE_DICTIONARY[new Random().nextInt(AGE_DICTIONARY.length)];
        String gender = GENDER_DICTIONARY[new Random().nextInt(GENDER_DICTIONARY.length)];

        return new People(name, gender, age);

    }

    public boolean checkDataExists() {

        File database = context.getDatabasePath(DATABASE_NAME);

        String query = "SELECT * FROM " + LocalDataContract.People.TABLE_NAME;
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        return cursor.getCount() > 0 && database.exists();
    }

}
