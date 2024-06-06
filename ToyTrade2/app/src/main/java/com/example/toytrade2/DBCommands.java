package com.example.toytrade2;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DBCommands {
    private DBHelper helper;

    public DBCommands(Context ctx) {
        helper = new DBHelper(ctx);
    }

    public long addUser(User user) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", user.getName());
        values.put("email", user.getEmail());
        values.put("phone", user.getPhone());
        values.put("password", user.getPassword());
        values.put("photo", user.getPhoto());
        return db.insert("users", null, values);
    }

    public long addToy(Toy toy, List<byte[]> photos) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", toy.getUserId());
        values.put("name", toy.getName());
        values.put("condition", toy.getCondition());
        values.put("category", toy.getCategory());
        values.put("age_category", toy.getAgeCategory());
        values.put("description", toy.getDescription());
        values.put("exchange_preferences", toy.getExchangePreferences());
        values.put("address_id", toy.getAddressId());
        values.put("is_active", toy.getIsActive() ? 1 : 0);
        long toyId =  db.insert("toys", null, values);
        if (!photos.isEmpty()) {
            for (byte[] photo : photos) {
                ContentValues photoValues = new ContentValues();
                photoValues.put("toy_id", toyId);
                photoValues.put("photo", photo);
                db.insert("toy_photos", null, photoValues);
            }
        }
        return toyId;
    }

    public long addAddress(Address address) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", address.getUserId());
        values.put("address", address.getAddress());
        return db.insert("addresses", null, values);
    }

    public long addTrade(Trade trade) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user1_id", trade.getUser1Id());
        values.put("user2_id", trade.getUser2Id());
        values.put("toy1_id", trade.getToy1Id());
        values.put("toy2_id", trade.getToy2Id());
        values.put("status", trade.getStatus() ? 1 : 0);
        return db.insert("trades", null, values);
    }

    @SuppressLint("Range")
    public User getUserById(int userId) {
        SQLiteDatabase db = helper.getReadableDatabase();
        User user = null;
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE id = ?", new String[]{String.valueOf(userId)});
        if (cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String email = cursor.getString(cursor.getColumnIndex("email"));
            String phone = cursor.getString(cursor.getColumnIndex("phone"));
            String password = cursor.getString(cursor.getColumnIndex("password"));
            byte[] photo = cursor.getBlob(cursor.getColumnIndex("photo"));
            user = new User(name, email, phone, password, photo);
        }
        cursor.close();
        return user;
    }

    @SuppressLint("Range")
    public Toy getToyById(int toyId) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Toy toy = null;
        Cursor cursor = db.rawQuery("SELECT * FROM toys WHERE id = ?", new String[]{String.valueOf(toyId)});
        if (cursor.moveToFirst()) {
            int userId = cursor.getInt(cursor.getColumnIndex("user_id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String condition = cursor.getString(cursor.getColumnIndex("condition"));
            String category = cursor.getString(cursor.getColumnIndex("category"));
            String ageCategory = cursor.getString(cursor.getColumnIndex("age_category"));
            String description = cursor.getString(cursor.getColumnIndex("description"));
            String exchangePreferences = cursor.getString(cursor.getColumnIndex("exchange_preferences"));
            int addressId = cursor.getInt(cursor.getColumnIndex("address_id"));
            boolean isActive = cursor.getInt(cursor.getColumnIndex("is_active")) == 1;
            toy = new Toy(userId, name, condition, category, ageCategory, description, exchangePreferences, addressId, isActive);
        }
        cursor.close();
        return toy;
    }

    public List<byte[]> getToyPhotos(int toyId) {
        SQLiteDatabase db = helper.getReadableDatabase();
        List<byte[]> photos = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT photo FROM toy_photos WHERE toy_id = ?", new String[]{String.valueOf(toyId)});
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") byte[] photo = cursor.getBlob(cursor.getColumnIndex("photo"));
                photos.add(photo);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return photos;
    }

    @SuppressLint("Range")
    public Address getAddressById(int addressId) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Address address = null;
        Cursor cursor = db.rawQuery("SELECT * FROM addresses WHERE id = ?", new String[]{String.valueOf(addressId)});
        if (cursor.moveToFirst()) {
            int userId = cursor.getInt(cursor.getColumnIndex("user_id"));
            String addressText = cursor.getString(cursor.getColumnIndex("address"));
            address = new Address(userId, addressText);
        }
        cursor.close();
        return address;
    }

    @SuppressLint("Range")
    public Trade getTradeById(int tradeId) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Trade trade = null;
        Cursor cursor = db.rawQuery("SELECT * FROM trades WHERE id = ?", new String[]{String.valueOf(tradeId)});
        if (cursor.moveToFirst()) {
            int user1Id = cursor.getInt(cursor.getColumnIndex("user1_id"));
            int user2Id = cursor.getInt(cursor.getColumnIndex("user2_id"));
            int toy1Id = cursor.getInt(cursor.getColumnIndex("toy1_id"));
            int toy2Id = cursor.getInt(cursor.getColumnIndex("toy2_id"));
            boolean status = cursor.getInt(cursor.getColumnIndex("status")) == 1;
            trade = new Trade(user1Id, user2Id, toy1Id, toy2Id, status);
        }
        cursor.close();
        return trade;
    }

    public int updateUser(User user, int userId) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", user.getName());
        values.put("email", user.getEmail());
        values.put("phone", user.getPhone());
        values.put("password", user.getPassword());
        values.put("photo", user.getPhoto());
        return db.update("users", values, "id = ?", new String[]{String.valueOf(userId)});
    }

    public long updateToy(Toy toy, int toyId, List<byte[]> newPhotos, List<byte[]> deletedPhotos) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", toy.getUserId());
        values.put("name", toy.getName());
        values.put("condition", toy.getCondition());
        values.put("category", toy.getCategory());
        values.put("age_category", toy.getAgeCategory());
        values.put("description", toy.getDescription());
        values.put("exchange_preferences", toy.getExchangePreferences());
        values.put("address_id", toy.getAddressId());
        values.put("is_active", toy.getIsActive() ? 1 : 0);
        long rowsAffected = db.update("toys", values, "id = ?",
                new String[]{String.valueOf(toyId)});
        for (byte[] photo : newPhotos) {
            ContentValues photoValues = new ContentValues();
            photoValues.put("toy_id", toyId);
            photoValues.put("photo", photo);
            db.insert("toy_photos", null, photoValues);
        }

        for (byte[] photo : deletedPhotos) {
            db.delete("toy_photos", "toy_id = ? AND photo = ?",
                    new String[]{String.valueOf(toyId), String.valueOf(photo)});
        }
        return rowsAffected;
    }

    public int updateAddress(Address address, int addressId) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", address.getUserId());
        values.put("address", address.getAddress());
        return db.update("addresses", values, "id = ?", new String[]{String.valueOf(addressId)});
    }

    public void deleteAddress(int addressId) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete("addresses", "id=?", new String[]{String.valueOf(addressId)});
        db.close();
    }

    public void deleteToy(int toyId) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete("toys", "id=?", new String[]{String.valueOf(toyId)});
        db.close();
    }

    public void updateToyActivity(int toyId, boolean toyActivityStatus) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("is_active", (toyActivityStatus ? 1 : 0));
        db.update("toys", values, "id = ?", new String[]{String.valueOf(toyId)});
    }

    public void updateTradeStatus(int tradeId, boolean tradeStatus) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("status", (tradeStatus ? 1 : 0));
        db.update("trades", values, "id = ?", new String[]{String.valueOf(tradeId)});
    }

    public List<Integer> getAllUserAddresses(int userId) {
        SQLiteDatabase db = helper.getReadableDatabase();
        List<Integer> userAddressesIds = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT id FROM addresses WHERE user_id = ?",
                new String[]{String.valueOf(userId)});
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int addressId = cursor.getInt(cursor.getColumnIndex("id"));
                userAddressesIds.add(addressId);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return userAddressesIds;
    }

    public List<Integer> getAllUserActiveToyIds(int userId, boolean activeness) {
        SQLiteDatabase db = helper.getReadableDatabase();
        List<Integer> activeToyIds = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT id FROM toys WHERE user_id = ? AND is_active = ?",
                new String[]{String.valueOf(userId), String.valueOf(activeness ? 1 : 0)});
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int toyId = cursor.getInt(cursor.getColumnIndex("id"));
                activeToyIds.add(toyId);
                Log.d("toydbid", String.valueOf(toyId));
            } while (cursor.moveToNext());
        }
        cursor.close();

        return activeToyIds;
    }

    public List<Integer> getAllActiveToyIds(int userId) {
        SQLiteDatabase db = helper.getReadableDatabase();
        List<Integer> activeToyIds = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT id FROM toys WHERE user_id != ? AND is_active = ?",
                new String[]{String.valueOf(userId), String.valueOf(1)});
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int toyId = cursor.getInt(cursor.getColumnIndex("id"));
                activeToyIds.add(toyId);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return activeToyIds;
    }

    public List<Integer> getAllUserTradeIds(int userId, boolean status) {
        SQLiteDatabase db = helper.getReadableDatabase();
        List<Integer> tradeIds = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT id FROM trades WHERE user1_id = ? AND status = ?",
                new String[]{String.valueOf(userId), String.valueOf(status ? 1 : 0)});
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int tradeId = cursor.getInt(cursor.getColumnIndex("id"));
                tradeIds.add(tradeId);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return tradeIds;
    }

    public List<Integer> getAllToUserTradeIds(int userId, boolean status) {
        SQLiteDatabase db = helper.getReadableDatabase();
        List<Integer> tradeIds = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT id FROM trades WHERE user2_id = ? AND status = ?",
                new String[]{String.valueOf(userId), String.valueOf(status ? 1 : 0)});
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int tradeId = cursor.getInt(cursor.getColumnIndex("id"));
                tradeIds.add(tradeId);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return tradeIds;
    }

    @SuppressLint("Range")
    public int findUserByPhoneNumber(String phoneNumber) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE phone = ?",
                new String[]{String.valueOf(phoneNumber)});
        int id = -1;
        if (cursor.moveToFirst()) {
            id = cursor.getInt(cursor.getColumnIndex("id"));
        }
        cursor.close();
        return id;
    }

    @SuppressLint("Range")
    public int getToyAddressId(String address, int userId) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM addresses WHERE address = ? AND user_id = ?",
                new String[]{String.valueOf(address), String.valueOf(userId)});
        int id = -1;
        if (cursor.moveToFirst()) {
            id = cursor.getInt(cursor.getColumnIndex("id"));
        }
        cursor.close();
        return id;
    }

    public List<Integer> getSearchToys(String search, int userId) {
        SQLiteDatabase db = helper.getReadableDatabase();
        List<Integer> searchToyIds = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT id FROM toys WHERE user_id != ? AND is_active = ? " +
                        "AND name LIKE '%'?'%' OR category LIKE '%'?'%' OR description LIKE '%'?'%'",
                new String[]{String.valueOf(userId), String.valueOf(1), String.valueOf(search),
                        String.valueOf(search), String.valueOf(search)});
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int toyId = cursor.getInt(cursor.getColumnIndex("id"));
                searchToyIds.add(toyId);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return searchToyIds;
    }

}
