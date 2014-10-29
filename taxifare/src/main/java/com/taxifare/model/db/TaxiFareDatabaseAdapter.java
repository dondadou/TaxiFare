package com.taxifare.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.*;

public class TaxiFareDatabaseAdapter extends SQLiteOpenHelper {
    private final Context context;

    public static final String DATABASE_NAME = "taxifare.sqlite";

    public static final String DATABASE_PATH = "/data/data/com.taxifare/databases/";

    public static final int DATABASE_VERSION = 1;

    public TaxiFareDatabaseAdapter(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;

        try {
            this.createDataBase();
        } catch (IOException e) {
            throw new Error("Error copying database");
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            CopyDataBase();
        } catch (IOException e) {
            throw new Error("Error copying database");
        }
    }

    private boolean CheckDataBase() {

        File dbFile = new File(DATABASE_PATH + DATABASE_NAME);
        return dbFile.exists();
    }

    private void CopyDataBase() throws IOException {

        InputStream myInput = context.getAssets().open(DATABASE_NAME);
        String outFileName = DATABASE_PATH + DATABASE_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);

        // transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        // Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public void createDataBase() throws IOException {
        boolean dbExist = CheckDataBase();

        if (!dbExist) {
            this.getReadableDatabase();
            try {
                CopyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }
}
