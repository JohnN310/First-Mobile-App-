package com.example.spotifytest;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.Nullable;

public class AccountsDatabaseHandler extends SQLiteOpenHelper {

    private static final String databaseName = "accountsDatabase";
    private static final int databaseVersion = 1;
    private static final String tableName = "accounts";
    private static final String username = "username";
    private static final String password = "password";
    private static final String name = "name";
    private static final String code = "code";
    private static final String friends = "friends";
    private static final String invites = "invites";

    private static final String topTracks = "topTracks";

    public AccountsDatabaseHandler(@Nullable Context context) {
        super(context, databaseName, null, databaseVersion);
    }

    public void onCreate(SQLiteDatabase db) {
        // an sqlite query and we are
        // setting our column names
        // along with their data types.
        String query = "CREATE TABLE " + tableName + " ("
                + username + " TEXT,"
                + password + " TEXT,"
                + name + " TEXT,"
                + code + " TEXT,"
                + friends + " TEXT,"
                + invites + " TEXT,"
                + topTracks +" TEXT)";


        // at last we are calling a exec sql
        // method to execute above sql query
        db.execSQL(query);
    }


    public void newUser(String userUsername, String userPassword, String userName, String userCode) {

       // a variable for
        // our sqlite database and calling writable method
        // as we are writing data in our database.
        SQLiteDatabase db = this.getWritableDatabase();

        // creating a
        // variable for content values.
        ContentValues values = new ContentValues();

        // passing all values
        // along with its key and value pair.
        values.put(username, userUsername);
        values.put(password, userPassword);
        values.put(name, userName);
        values.put(code, userCode);
        values.put(friends, "friends,");
        values.put(invites, "invites,");
        values.put(topTracks, "topTracks,");
        // after adding all values we are passing
        // content values to our table.
        db.insert(tableName, null, values);
        // at last we are closing our
        // database after adding database.
        db.close();
    }

    public void changePassword(String userUsername, String newPassword) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(password, newPassword);

        Cursor cursor = db.rawQuery("Select * from accounts where username = ?", new String[]{userUsername});

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            if (userUsername.equals(cursor.getString(0))) {
                db.update(tableName, values, "username=?", new String[]{userUsername});
                break;
            }
            cursor.moveToNext();

        }
        db.close();
    }

    public void addInvite(String userUsername, String newFriend) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        Cursor cursor = db.rawQuery("Select * from accounts where username = ?", new String[]{userUsername});


        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            if (userUsername.equals(cursor.getString(0))) {
                String prevInvites = cursor.getString(5);
                values.put(invites, prevInvites + newFriend + ",");
                db.update(tableName, values, "username=?", new String[]{userUsername});
                break;
            }
            cursor.moveToNext();

        }
        cursor.close();
        db.close();
    }

    public void updateInvites(String userUsername, String updatedInvites) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("invites", updatedInvites);
        db.update(tableName, values, "username=?", new String[]{userUsername});
        db.close();
    }

    public void addFriend(String userUsername, String newFriend) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        Cursor cursor = db.rawQuery("Select * from accounts where username = ?", new String[]{userUsername});


        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            if (userUsername.equals(cursor.getString(0))) {
                String prevFriends = cursor.getString(4);
                values.put(friends, prevFriends + newFriend + ",");
                db.update(tableName, values, "username=?", new String[]{userUsername});
                break;
            }
            cursor.moveToNext();

        }

        cursor.close();
        db.close();
    }

    public boolean contains(String username) {

        SQLiteDatabase db = this.getWritableDatabase();

        @SuppressLint("Recycle") Cursor cursor = db.rawQuery("Select * from accounts where username = ?", new String[]{username});
        if (cursor.getCount() > 0) {
            db.close();
            return true;

        }
        db.close();
        return false;

    }

    @SuppressLint("Recycle")
    public int authenticate(String username, String password) {

        SQLiteDatabase db = this.getWritableDatabase();

        @SuppressLint("Recycle") Cursor cursor = db.rawQuery("Select * from accounts where username = ? and password = ?", new String[]{username, password});
        if (cursor.getCount() > 0) {
            db.close();
            return 3;
        } else if (db.rawQuery("Select * from accounts where username = ?", new String[]{username}).getCount() > 0) {
            db.close();
            return 2;
        }
        db.close();
        return 1;

    }

    public YourProfile getAccount(String userName) {

        YourProfile thisProfile = new YourProfile();
        if (userName != null) {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM accounts WHERE username = ? LIMIT 1", new String[]{userName});

            if (cursor.moveToFirst()) {
                int usernameInd = cursor.getColumnIndex("username");
                thisProfile.setUsername(cursor.getString(usernameInd));
                int passwordInd = cursor.getColumnIndex("password");
                thisProfile.setPassword(cursor.getString(passwordInd));
                int nameInd = cursor.getColumnIndex("name");
                thisProfile.setName(cursor.getString(nameInd));
                int friendsInd = cursor.getColumnIndex("friends");
                thisProfile.setFriends(cursor.getString(friendsInd));
                int topTracksInd = cursor.getColumnIndex("topTracks");
                thisProfile.setTop5SongList(cursor.getString(topTracksInd));
                int inviteInt = cursor.getColumnIndex(("invites"));
                thisProfile.setInvites(cursor.getString(inviteInt));
                // we need these 2 lines
            }

            cursor.close();
        }
        return thisProfile;
    }

//    public void deleteAccount(String userUsername) {
//
//        // on below line we are creating a variable for
//        // our sqlite database and calling writable method
//        // as we are writing data in our database.
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        db.delete(tableName, "username=?", new String[]{userUsername});
//        db.close();
//    }


    public void deleteAccount(String userUsername) {
        SQLiteDatabase db = this.getWritableDatabase();

        // go through all accounts
        Cursor cursor = db.rawQuery("SELECT username, friends, invites FROM " + tableName, null);

        if (cursor.moveToFirst()) {
            do {
                String currentUser = cursor.getString(0);
                String friendsList = cursor.getString(1);
                String invitesList = cursor.getString(2);

                ContentValues values = new ContentValues();
                boolean needsUpdate = false;

                // clean up friends
                if (friendsList != null && friendsList.contains(userUsername)) {
                    String updatedFriends = friendsList.replace(userUsername + ",", "");
                    values.put("friends", updatedFriends);
                    needsUpdate = true;
                }

                // clean up invites
                if (invitesList != null && invitesList.contains(userUsername)) {
                    String updatedInvites = invitesList.replace(userUsername + ",", "");
                    values.put("invites", updatedInvites);
                    needsUpdate = true;
                }

                // save back if changed
                if (needsUpdate) {
                    db.update(tableName, values, "username=?", new String[]{currentUser});
                }

            } while (cursor.moveToNext());
        }

        cursor.close();

        // delete the account
        db.delete(tableName, "username=?", new String[]{userUsername});

        db.close();
    }


    public void saveTopTracks(List<String> topTrackNames) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // convert the list of track names to a single string
        String topTracksString = String.join(",", topTrackNames); // Using comma as delimiter

        // add the top tracks string to ContentValues
        values.put("topTracks", topTracksString);

        // insert the ContentValues into the database
        db.insert(tableName, null, values);

        db.close();
    }


    public void addSavedWrapped(String userUsername, List<String> top5names) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        Cursor cursor = db.rawQuery("Select * from accounts where username = ?", new String[]{userUsername});

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            if (userUsername.equals(cursor.getString(0))) {
                String prevWrappeds = cursor.getString(6);
                String newWrapped = "";
                List<String> currentList = new ArrayList<>(Arrays.asList(prevWrappeds.split(",")));
                for (int i = 0; i < top5names.size(); i++) {
                    String currString = top5names.get(i);
                    if (!currentList.contains(currString)) {
                        currentList.add(currString);
                        if (i + 1 != top5names.size()) {
                            newWrapped += currString + ",";
                        } else {
                            newWrapped += currString;
                        }
                    }
                }
                values.put("topTracks", prevWrappeds + newWrapped + ",");
                db.update(tableName, values, "username=?", new String[]{userUsername});
                break;
            }
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
    }

    public List<String> getSavedWrapped(String userUsername) {

        List<String> savedWrappedList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT topTracks FROM " + tableName + " WHERE username = ?", new String[]{userUsername});

        if (cursor.moveToFirst()) {
            String savedWrappeds = cursor.getString(0);
            String[] savedWrappedsArray = savedWrappeds.split(",");

            for (String savedWrapped : savedWrappedsArray) {
                String[] wrappedStrings = savedWrapped.split(",");
                savedWrappedList.addAll(Arrays.asList(wrappedStrings));
            }
        }

        cursor.close();
        db.close();
        return savedWrappedList;
    }


    public ArrayList<YourProfile> readProfiles() {
        // database for reading our database.
        SQLiteDatabase database = this.getReadableDatabase();

        // a cursor with query to
        // read data from database.
        Cursor profileCursor = database.rawQuery("SELECT * FROM " + tableName, null);

        // a new array list.
        ArrayList<YourProfile> profilesArrayList = new ArrayList<>();

        // moving our cursor to first position.
        if (profileCursor.moveToFirst()) {
            do {
                // add the data from
                // cursor to our array list.
                profilesArrayList.add(new YourProfile(profileCursor.getString(0), profileCursor.getString(1), profileCursor.getString(2), profileCursor.getString(3), profileCursor.getString(4), profileCursor.getString(5), profileCursor.getString(6)));
            } while (profileCursor.moveToNext());
            // moving our cursor to next.
        }
        // at last closing our cursor
        // and returning our array list.
        profileCursor.close();
        return profilesArrayList;
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // this method is called to check if the table exists already.
        db.execSQL("DROP TABLE IF EXISTS " + tableName);
        onCreate(db);
    }




}
