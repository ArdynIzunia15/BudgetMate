package com.example.expensetracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "ExpenseTracker";
    // Table Name
    private static final String TABLE_USERS = "Users";
    private static final String TABLE_HISTORY = "History";
    // Column Table User
    private static final String KEY_ID_USER = "id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_BALANCE = "balance";
    // Column Table History
    private static final String KEY_ID_HISTORY = "id";
    private static final String KEY_ID_HISTORY_USER = "userId";
    private static final String KEY_TRANSACTION_TYPE = "transactionType";
    private static final String KEY_TITLE = "historyTitle";
    private static final String KEY_TOTAL_TRANSACTION = "totalTransaction";
    private static final String KEY_YEAR = "year";
    private static final String KEY_MONTH = "month";
    private static final String KEY_DAY = "day";
    private static final String KEY_HOUR = "hour";
    private static final String KEY_MINUTE = "minute";
    private static final String KEY_SECOND = "second";

    public DatabaseHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "(" +
                KEY_ID_USER + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                KEY_USERNAME + " TEXT," +
                KEY_PASSWORD + " TEXT," +
                KEY_BALANCE + " INTEGER)";
        String CREATE_HISTORY_TABLE = "CREATE TABLE " + TABLE_HISTORY + "(" +
                KEY_ID_HISTORY + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                KEY_ID_HISTORY_USER + " INTEGER NOT NULL," +
                KEY_TRANSACTION_TYPE + " INTEGER NOT NULL," +
                KEY_TITLE + " TEXT," +
                KEY_TOTAL_TRANSACTION + " INTEGER," +
                KEY_YEAR + " INTEGER," +
                KEY_MONTH + " INTEGER," +
                KEY_DAY + " INTEGER," +
                KEY_HOUR + " INTEGER," +
                KEY_MINUTE + " INTEGER," +
                KEY_SECOND + " INTEGER)";
        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_HISTORY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    public long addRecordUserAccount(User user){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USERNAME, user.getUsername());
        values.put(KEY_PASSWORD, user.getPassword());
        values.put(KEY_BALANCE, user.getBalance());

        if(db.insert(TABLE_USERS, null, values) == -1){
            db.close();
            return -1;
        }
        db.close();
        return 1;
    }

    public long addHistoryRecord(History history) {
        SQLiteDatabase db = null;

        try {
            db = getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(KEY_ID_HISTORY_USER, history.getUserId());
            values.put(KEY_TRANSACTION_TYPE, history.getTipeTransaksi());
            values.put(KEY_TITLE, history.getTransaksiJudul());
            values.put(KEY_TOTAL_TRANSACTION, history.getJumlahTransaksi());
            values.put(KEY_YEAR, history.getDateTime().getYear());
            values.put(KEY_MONTH, history.getDateTime().getMonthValue());
            values.put(KEY_DAY, history.getDateTime().getDayOfMonth());
            values.put(KEY_HOUR, history.getDateTime().getHour());
            values.put(KEY_MINUTE, history.getDateTime().getMinute());
            values.put(KEY_SECOND, history.getDateTime().getSecond());

            if (db.insert(TABLE_HISTORY, null, values) == -1) {
                return -1;
            }

            return 1;
        } catch (Exception e) {
            // Handle the exception appropriately or log the error
            e.printStackTrace();
            return -1;
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }


    public User getUser(String username){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + KEY_USERNAME + " ='" + username + "'";
        Cursor cursor = db.rawQuery(query, null);
        User user = null;
        if(cursor.moveToFirst()){
            user = new User();
            user.setId(Integer.parseInt(cursor.getString(0)));
            user.setUsername(cursor.getString(1));
            user.setPassword(cursor.getString(2));
            user.setBalance(cursor.getInt(3));
        }
        cursor.close();
        return user;
    }

    public int getIncome(int userId){
        int transactionType = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_HISTORY + " WHERE " + KEY_ID_HISTORY_USER + "=" + userId + " AND " + KEY_TRANSACTION_TYPE + "=" + transactionType;
        Cursor cursor = db.rawQuery(query, null);
        int totalIncome = 0;
        // Counting
        if(cursor.moveToFirst()){
            do{
                totalIncome += Integer.parseInt(String.valueOf(cursor.getInt(4)));
            }while(cursor.moveToNext());
        }
        return totalIncome;
    }

    public int getOutcome(int userId){
        int transactionType = 1;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_HISTORY + " WHERE " + KEY_ID_HISTORY_USER + "=" + userId + " AND " + KEY_TRANSACTION_TYPE + "=" + transactionType;
        Cursor cursor = db.rawQuery(query, null);
        int totalOutcome = 0;
        // Counting
        if(cursor.moveToFirst()){
            do{
                totalOutcome += Integer.parseInt(String.valueOf(cursor.getInt(4)));
            }while(cursor.moveToNext());
        }
        return totalOutcome;
    }

    public ArrayList<History> getAllData(int userId){
        ArrayList<History> arrData = new ArrayList<>();
        // Get readable database
        SQLiteDatabase db = this.getReadableDatabase();
        // SELECT * FROM table_name
        // String selectQuery = "SELECT * FROM " + TABLE_HISTORY + " WHERE " + KEY_ID_HISTORY_USER + "=" + userId;
        String selectQuery = "SELECT * FROM " + TABLE_HISTORY + " WHERE " + KEY_ID_HISTORY_USER + "=" + userId + " ORDER BY " + KEY_YEAR + " DESC, " + KEY_MONTH + " DESC, " + KEY_DAY + " DESC, " + KEY_HOUR + " DESC, " + KEY_MINUTE + " DESC, " + KEY_SECOND + " DESC";
        // Execute the query
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Loop through all rows and add the data to the ArrayList
        if (cursor.moveToFirst()) {
            do {
                // Make object
                History history = new History();
                history.setId(Integer.parseInt(String.valueOf(cursor.getInt(0))));
                history.setUserId(Integer.parseInt(String.valueOf(cursor.getInt(1))));
                history.setTipeTransaksi(Integer.parseInt(String.valueOf(cursor.getInt(2))));
                history.setTransaksiJudul(cursor.getString(3));
                history.setJumlahTransaksi(Integer.parseInt(String.valueOf(cursor.getInt(4))));
                LocalDateTime dateTime = LocalDateTime.of(
                        // Year
                        Integer.parseInt(String.valueOf(cursor.getInt(5))),
                        // Month
                        Integer.parseInt(String.valueOf(cursor.getInt(6))),
                        // Day
                        Integer.parseInt(String.valueOf(cursor.getInt(7))),
                        // Hour
                        Integer.parseInt(String.valueOf(cursor.getInt(8))),
                        // Minute
                        Integer.parseInt(String.valueOf(cursor.getInt(9))),
                        // Second
                        Integer.parseInt(String.valueOf(cursor.getInt(10)))
                );
                history.setDateTime(dateTime);

                // Add to ArrayList
                arrData.add(history);
            } while (cursor.moveToNext());
        }
        return arrData;
    }

    public int getBalance(int userId){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + KEY_ID_USER + "=" + userId;
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()){
            return Integer.parseInt(String.valueOf(cursor.getInt(3)));
        }
        else {
            return -1;
        }
    }

    public long updateBalance(int userId, int newBalance){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_BALANCE, newBalance);

        String whereClause = "id = ?";
        String[] whereArgs = {String.valueOf(userId)};

        if(db.update(TABLE_USERS, values, whereClause, whereArgs) != 0){
            return 1;
        }
        else{
            return -1;
        }
    }

    public String getUsername(int userId){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + KEY_ID_USER + "=" + userId;
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()){
            return cursor.getString(1);
        }
        return null;
    }
}
