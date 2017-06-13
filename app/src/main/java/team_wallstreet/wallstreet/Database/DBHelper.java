package team_wallstreet.wallstreet.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by TJ on 6/13/2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "stocks.db";
    SQLiteDatabase db;

    /**
     * Create a helper object to create, open, and/or manage a database.
     * This method always returns very quickly.  The database is not actually
     * created or opened until one of {@link #getWritableDatabase} or
     * {@link #getReadableDatabase} is called.
     *
     * @param context to use to open or create the database
     * @param name    of the database file, or null for an in-memory database
     * @param factory to use for creating cursor objects, or null for the default
     * @param version number of the database (starting at 1); if the database is older,
     *                {@link #onUpgrade} will be used to upgrade the database; if the database is
     *                newer, {@link #onDowngrade} will be used to downgrade the database
     */
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        db = this.getWritableDatabase();
    }

    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE StockInfo (Date DATE PRIMARY KEY, Open DOUBLE," +
                "High DOUBLE, Low DOUBLE, Close Double, AdjClose Double, Volume INTEGER)");
    }

    /**
     * Called when the database needs to be upgraded. The implementation
     * should use this method to drop tables, add tables, or do anything else it
     * needs to upgrade to the new schema version.
     * <p>
     * <p>
     * The SQLite ALTER TABLE documentation can be found
     * <a href="http://sqlite.org/lang_altertable.html">here</a>. If you add new columns
     * you can use ALTER TABLE to insert them into a live table. If you rename or remove columns
     * you can use ALTER TABLE to rename the old table, then create the new table and then
     * populate the new table with the contents of the old table.
     * </p><p>
     * This method executes within a transaction.  If an exception is thrown, all changes
     * will automatically be rolled back.
     * </p>
     *
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS StockInfo");
        onCreate(db);
    }

    public void clearStocks() {
        db = this.getWritableDatabase();
        db.execSQL("delete from StockInfo");
        db.close();
    }

    public boolean insertStock(Stock stock) {
        db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Date", String.valueOf(stock.getDate()));
        contentValues.put("Open", stock.getOpen());
        contentValues.put("High", stock.getHigh());
        contentValues.put("Low", stock.getLow());
        contentValues.put("Close", stock.getClose());
        contentValues.put("AdjClose", stock.getAdjClose());
        contentValues.put("Volume", stock.getVolume());
        db.insert("StockInfo", null, contentValues);
        db.close();
        return true;
    }

    public ArrayList<Stock> getAllStocks() {
        db = this.getWritableDatabase();
        ArrayList<Stock> arrayList = new ArrayList();
        Cursor res;
        res = db.rawQuery("Select * from StockInfo", null);

        res.moveToFirst();
        while (!res.isAfterLast()) {
            Stock stock = new Stock();
            stock.setDate(res.getString(res.getColumnIndex("Date")));
            stock.setOpen(res.getDouble(res.getColumnIndex("Open")));
            stock.setHigh(res.getDouble(res.getColumnIndex("High")));
            stock.setLow(res.getDouble(res.getColumnIndex("Low")));
            stock.setClose(res.getDouble(res.getColumnIndex("Close")));
            stock.setAdjClose(res.getDouble(res.getColumnIndex("AdjClose")));
            stock.setVolume(res.getInt(res.getColumnIndex("Volume")));
            arrayList.add(stock);
            res.moveToNext();
        }
        db.close();
        return arrayList;
    }
}
