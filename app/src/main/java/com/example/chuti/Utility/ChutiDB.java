package com.example.chuti.Utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.chuti.Model.Announcement;

import java.util.ArrayList;
import java.util.List;


public class ChutiDB extends SQLiteOpenHelper {
    private static final String DB_NAME = "CHUTIDB";
    private static final int DB_VERSION = 1;
    private static final String TABLE_Announcement = "Announcement";
    private static final String AnnouncementID = "AnnouncementID";
    private static final String AnnouncementPeriod = "AnnouncementPeriod";
    private static final String AnnouncementTitle = "AnnouncementTitle";
    private static final String AnnouncementText = "AnnouncementText";
    private static final String IsPublished = "IsPublished";
    private static final String PublishedDate = "publishedDate";
    private static final String IsForAll = "IsForAll";
    private static final String IsRead = "IsRead";
    private static final String ModifiedDate = "ModifiedDate";

    public ChutiDB(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query1 = "CREATE TABLE " + TABLE_Announcement + " (" +
                AnnouncementID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + // Set as the primary key
                AnnouncementPeriod + " INTEGER, " +
                AnnouncementTitle + " TEXT, " + // TEXT is preferred over NVARCHAR in SQLite
                AnnouncementText + " TEXT, " +
                IsPublished + " INTEGER, " + // 0 for false, 1 for true
                PublishedDate + " TEXT, " + // ISO 8601 format: yyyy-MM-dd HH:mm:ss
                IsForAll + " INTEGER, " + // 0 for false, 1 for true
                IsRead + " INTEGER DEFAULT 0, " + // 0 for false, 1 for true
                ModifiedDate + " TEXT" + // ISO 8601 format
                ");";
        db.execSQL(query1);
    }

    public boolean saveAnnouncement(Integer announcementID,
                                    Integer announcementPeriod,
                                    String announcementTitle,
                                    String announcementText,
                                    String publishedDate,
                                    String modifiedDate) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(AnnouncementID, announcementID);
        values.put(AnnouncementPeriod, announcementPeriod);
        values.put(AnnouncementTitle, announcementTitle);
        values.put(AnnouncementText, announcementText);
        values.put(PublishedDate, publishedDate);
        values.put(ModifiedDate, modifiedDate);

        // Return true if insert is successful, false otherwise
        return db.insert(TABLE_Announcement, null, values) != -1;
    }

    public boolean doesAnnouncementExist(int announcementID) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT 1 FROM " + TABLE_Announcement + " WHERE " + AnnouncementID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(announcementID)});

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public List<Announcement> readAllAnnouncements() {
        List<Announcement> announcements = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_Announcement;

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Announcement announcement = new Announcement();
                announcement.setAnnouncementID(cursor.getInt(cursor.getColumnIndexOrThrow(AnnouncementID)));
                announcement.setAnnouncementPeriod(cursor.getInt(cursor.getColumnIndexOrThrow(AnnouncementPeriod)));
                announcement.setAnnouncementTitle(cursor.getString(cursor.getColumnIndexOrThrow(AnnouncementTitle)));
                announcement.setAnnouncementText(cursor.getString(cursor.getColumnIndexOrThrow(AnnouncementText)));
                announcement.setPublishedDate(cursor.getString(cursor.getColumnIndexOrThrow(PublishedDate)));
                announcement.setIsRead(cursor.getInt(cursor.getColumnIndexOrThrow(IsRead)));
                announcement.setModifiedDate(cursor.getString(cursor.getColumnIndexOrThrow(ModifiedDate)));

                announcements.add(announcement);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return announcements;
    }

    public boolean updateAnnouncement(int announcementID, int isRead) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Create a ContentValues object to hold the values to update
        ContentValues values = new ContentValues();
        values.put(IsRead, isRead);  // Set the new value for IsRead

        // Update the record where the AnnouncementID matches
        int rowsAffected = db.update(TABLE_Announcement, values, AnnouncementID + " = ?", new String[]{String.valueOf(announcementID)});

        // Close the database
        db.close();

        // Return whether the update was successful
        return rowsAffected > 0;
    }

    public int countAnnouncements() {
        SQLiteDatabase db = this.getReadableDatabase();  // Get the database in read mode
        String query = "SELECT COUNT(*) FROM " + TABLE_Announcement + " WHERE " + IsRead + "=0";  // Correctly formatted query
        Cursor cursor = db.rawQuery(query, null);  // Execute the query

        int count = 0;
        if (cursor != null) {
            if (cursor.moveToFirst()) {  // Move to the first row of the result
                count = cursor.getInt(0);  // Get the count from the first column (COUNT(*) returns a single value)
            }
            cursor.close();  // Don't forget to close the cursor
        }

        return count;  // Return the count of rows
    }

    public Announcement getLastAnnouncement() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_Announcement + " ORDER BY " + PublishedDate + " DESC LIMIT 1";
        Cursor cursor = db.rawQuery(query, null);

        Announcement announcement = null;
        if (cursor != null && cursor.moveToFirst()) {
            announcement = new Announcement();
            announcement.setAnnouncementID(cursor.getInt(cursor.getColumnIndexOrThrow(AnnouncementID)));
            announcement.setAnnouncementPeriod(cursor.getInt(cursor.getColumnIndexOrThrow(AnnouncementPeriod)));
            announcement.setAnnouncementTitle(cursor.getString(cursor.getColumnIndexOrThrow(AnnouncementTitle)));
            announcement.setAnnouncementText(cursor.getString(cursor.getColumnIndexOrThrow(AnnouncementText)));
            announcement.setPublishedDate(cursor.getString(cursor.getColumnIndexOrThrow(PublishedDate)));
            announcement.setIsRead(cursor.getInt(cursor.getColumnIndexOrThrow(IsRead)));
            announcement.setModifiedDate(cursor.getString(cursor.getColumnIndexOrThrow(ModifiedDate)));

            cursor.close();
        }

        return announcement;
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Announcement);
    }
}
