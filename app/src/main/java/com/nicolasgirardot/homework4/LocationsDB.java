package com.nicolasgirardot.homework4;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by nicolasgirardot on 4/20/17.
 */

public class LocationsDB extends SQLiteOpenHelper {

    private static LocationsDB sInstance;

    // Database Info
    private static final String DATABASE_NAME = "LocationDB";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String TABLE_LOCATIONS = "locations";

    // Location Table Columns
    private static final String KEY_LOCATION_ID = "id";
    private static final String KEY_LOCATION_LONGITUDE = "longitude";
    private static final String KEY_LOCATION_LATITUDE = "latitude";
    private static final String KEY_LOCATION_ZOOM = "zoom";
    private final Context context;


    public static synchronized LocationsDB getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new LocationsDB(context.getApplicationContext());
        }
        return sInstance;
    }

    public static synchronized LocationsDB getInstance() {
        return sInstance;
    }

    private LocationsDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOCATIONS_TABLE = "CREATE TABLE " + TABLE_LOCATIONS +
                "(" +
                KEY_LOCATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + // Define a primary key
                KEY_LOCATION_LONGITUDE + " DOUBLE," +
                KEY_LOCATION_LATITUDE + " DOUBLE," +
                KEY_LOCATION_ZOOM + " DOUBLE" +
                ")";
        db.execSQL(CREATE_LOCATIONS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addLocation(Location location) {
        SQLiteDatabase db = getWritableDatabase();


        db.beginTransaction();
        try {

            ContentValues values = new ContentValues();
            Log.d("AHrhUFHRFU", location.getPosition().longitude  + " ::::: " + location.getPosition().latitude);
            values.put(KEY_LOCATION_LONGITUDE, location.getPosition().longitude);
            values.put(KEY_LOCATION_LATITUDE, location.getPosition().latitude);
            values.put(KEY_LOCATION_ZOOM, location.getZoom());

            db.insertOrThrow(TABLE_LOCATIONS, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add post to database");
        } finally {
            db.endTransaction();
        }
    }

    public void deleteAllLocations() {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(TABLE_LOCATIONS, null, null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to delete all posts and users");
        } finally {
            db.endTransaction();
        }
    }

    public List<Location> getAllPosts() {
        List<Location> locations = new ArrayList<>();

        String LOCATIONS_SELECT_QUERY =
                String.format("SELECT * FROM %s",
                        TABLE_LOCATIONS);

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(LOCATIONS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Location newLocation = new Location();
                    newLocation.setPosition(new LatLng(cursor.getDouble(cursor.getColumnIndex(KEY_LOCATION_LATITUDE)), cursor.getDouble(cursor.getColumnIndex(KEY_LOCATION_LONGITUDE))));
                    locations.add(newLocation);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return locations;
    }
}
