package com.t3h.service.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.t3h.service.db.UserDao;
import com.t3h.service.db.UserDatabase;
import com.t3h.service.model.User;

public class UserProvider extends ContentProvider {
    private static final String AUTHORITY = "com.t3h.service.provider";
    public static final Uri CONTENT_USER_URI =
            Uri.parse("content://" + AUTHORITY + "/user");
    private UserDao userDao;

    private static final UriMatcher uriMatcher;

    private static final int URI_USER = 1000;

    private static final int URI_USER_ID = 2000;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "user", URI_USER);
        uriMatcher.addURI(AUTHORITY, "user/#", URI_USER_ID);
    }

    @Override
    public boolean onCreate() {
        userDao = UserDatabase.getUserDatabase(getContext()).getUserDao();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri,
                        @Nullable String[] projection,
                        @Nullable String selection,
                        @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {
        switch (uriMatcher.match(uri)) {
            case URI_USER:
                return userDao.findAllUsersAndReturnCursor();
            case URI_USER_ID:
                int id = Integer.parseInt(uri.getPathSegments().get(1));
                return userDao.findOneUserAndReturnCursor(id);
            default:
                throw new IllegalArgumentException("Unknown uri");
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        if (uriMatcher.match(uri) != URI_USER) {
            throw new IllegalArgumentException("Unknown uri to insert");
        }
        if (!contentValues.containsKey("_id")
                || !contentValues.containsKey("_username")
                || !contentValues.containsKey("_password")) {
            throw new IllegalArgumentException("Content values does not valid");
        } else {
            int id = contentValues.getAsInteger("_id");
            String username = contentValues.getAsString("_username");
            String password = contentValues.getAsString("_password");
            User user = new User(id, username, password);
            userDao.insertUser(user);
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
