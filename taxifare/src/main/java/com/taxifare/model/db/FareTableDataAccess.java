package com.taxifare.model.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.taxifare.model.pojos.FareTable;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FareTableDataAccess {
    private static final String COL_ID = "_id";
    private static final String COL_CITY = "city";
    private static final String COL_COUNTRY = "country";
    private static final String COL_BASE_FARE = "min_fare";
    private static final String COL_FARE_PER_KM = "fare_per_km";
    private static final String COL_WAIT_TIME_FEE = "wait_fees";
    private static final String COL_CURRENCY = "currency";
    private static final String FARE_TABLE_TITLE = "fare";

    private SQLiteDatabase sqlDatabase;
    private TaxiFareDatabaseAdapter myDatabaseAdapter;

    public FareTableDataAccess(Context context) {
        myDatabaseAdapter = new TaxiFareDatabaseAdapter(context);
    }

    public void OpenDatabase() {
        sqlDatabase = myDatabaseAdapter.getReadableDatabase();
    }

    public void CloseDatabase() {
        myDatabaseAdapter.close();
    }

    public FareTable retrieveFareInfoById(long rowId) throws SQLException {

        FareTable dataFare = new FareTable();

        Cursor mCursor = sqlDatabase.query(true, FARE_TABLE_TITLE, new String[]{
                        COL_ID, COL_CITY, COL_COUNTRY,
                        COL_BASE_FARE, COL_FARE_PER_KM,
                        COL_WAIT_TIME_FEE, COL_CURRENCY}, COL_ID + "=" + rowId, null, null, null,
                null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }

        dataFare = cursorToFare(mCursor);

        return dataFare;
    }

    public FareTable retrieveFareInfoByCity(String cityName, String country) throws SQLException {

        FareTable dataFare = new FareTable();

        Cursor mCursor = sqlDatabase.query(true, FARE_TABLE_TITLE, new String[]{
                        COL_ID, COL_CITY, COL_COUNTRY,
                        COL_BASE_FARE, COL_FARE_PER_KM,
                        COL_WAIT_TIME_FEE, COL_CURRENCY},
                COL_CITY + "=" + "'" + cityName + "' " +
                        "AND " + COL_COUNTRY + "=" + "'" + country + "'",
                null, null, null,
                null, null);

        if (mCursor.getCount() > 0) {
            mCursor.moveToFirst();
            dataFare = cursorToFare(mCursor);
        }

        mCursor.close();

        return dataFare;
    }

    public List<FareTable> retrieveCitiesByCountry(String country) throws SQLException {

        List<FareTable> dataFareList = new ArrayList<FareTable>();

        Cursor mCursor = sqlDatabase.query(true, FARE_TABLE_TITLE, new String[]{
                        COL_ID, COL_CITY, COL_COUNTRY,
                        COL_BASE_FARE, COL_FARE_PER_KM,
                        COL_WAIT_TIME_FEE, COL_CURRENCY},
                COL_COUNTRY + "=" + "'" + country + "'",
                null, null, null,
                COL_CITY, null);

        if (mCursor != null) {
            mCursor.moveToFirst();

            while (!mCursor.isAfterLast()) {
                FareTable dataFare = cursorToFare(mCursor);
                dataFareList.add(dataFare);
                mCursor.moveToNext();
            }

            mCursor.close();
        }

        return dataFareList;
    }

    public List<String> retrieveAllCountries() throws SQLException {

        List<String> dataFareList = new ArrayList<String>();

        Cursor mCursor = sqlDatabase.query(true, FARE_TABLE_TITLE, new String[]
                {COL_COUNTRY}, null, null, null, null, COL_COUNTRY, null);

        if (mCursor != null) {
            mCursor.moveToFirst();

            while (!mCursor.isAfterLast()) {
                String country = mCursor.getString(0);
                dataFareList.add(country);
                mCursor.moveToNext();
            }

            mCursor.close();
        }
        return dataFareList;
    }

    public List<FareTable> retrieveAllCities() throws SQLException {
        List<FareTable> allCities = new ArrayList<FareTable>();

        Cursor cursor = sqlDatabase.query(FARE_TABLE_TITLE, new String[]
                {COL_ID, COL_CITY, COL_COUNTRY, COL_BASE_FARE, COL_FARE_PER_KM, COL_WAIT_TIME_FEE,
                        COL_CURRENCY}, null, null, null, null, COL_COUNTRY);

        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                FareTable character = cursorToFare(cursor);
                allCities.add(character);
                cursor.moveToNext();
            }
        }

        cursor.close();

        return allCities;
    }

    private FareTable cursorToFare(Cursor cursor) {
        if (cursor == null)
            return null;

        FareTable dataFare = new FareTable();

        dataFare.setId(cursor.getLong(0));
        dataFare.setCity(cursor.getString(1));
        dataFare.setCountry(cursor.getString(2));
        dataFare.setMinimumFare(cursor.getDouble(3));
        dataFare.setFarePerKilometers(cursor.getDouble(4));
        dataFare.setWaitingFees(cursor.getDouble(5));
        dataFare.setCurrency(cursor.getString(6));

        return dataFare;
    }
}
