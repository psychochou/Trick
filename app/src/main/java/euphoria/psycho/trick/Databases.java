package euphoria.psycho.trick;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Databases extends SQLiteOpenHelper {
    private static Databases sDatabases;

    public static Databases getInstance() {
        return sDatabases;
    }

    public static Databases newInstance(Context context, String databaseFileName) {
        if (sDatabases == null) {

            sDatabases = new Databases(context, databaseFileName);
        }
        return sDatabases;
    }


    public void addTab(String tabName) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", tabName);
        getWritableDatabase().insertWithOnConflict("tab", null, contentValues, SQLiteDatabase.CONFLICT_ABORT);
    }


    public List<TrickModel> getTricks(String tag) {
        List<TrickModel> collection = new ArrayList<>();

        Cursor cursor = getReadableDatabase().rawQuery("select _id,title from trick where tag = ?  order by updateAt desc ", new String[]{tag});
        while (cursor.moveToNext()) {

            TrickModel trickModel = new TrickModel();
            trickModel.Id = cursor.getInt(0);

            trickModel.Title = cursor.getString(1);

            collection.add(trickModel);
        }
        return collection;
    }

    public String getTrick(String tag, String title) {
        Cursor cursor = getReadableDatabase().rawQuery("select content from trick where tag =? and title=?", new String[]{tag
                , title});

        if (cursor.moveToNext()) {
            String v = cursor.getString(0);
            cursor.close();
            return v;
        }
        return null;
    }

    public String getTrick(int id) {
        Cursor cursor = getReadableDatabase().rawQuery("select content from trick where _id=?", new String[]{Integer.toString(id)});

        if (cursor.moveToNext()) {
            String v = cursor.getString(0);
            cursor.close();
            return v;
        }
        return null;
    }

    public void addTrick(TrickModel trickModel) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", trickModel.Title);
        contentValues.put("content", trickModel.Content);
        contentValues.put("tag", trickModel.Tag);
        contentValues.put("createAt", System.currentTimeMillis() / 1000L);
        contentValues.put("updateAt", System.currentTimeMillis() / 1000L);
        getWritableDatabase().insert("trick", null, contentValues);
    }

    public void updateTrick(TrickModel trickModel) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", trickModel.Title);
        contentValues.put("content", trickModel.Content);
        contentValues.put("updateAt", System.currentTimeMillis() / 1000L);
        getWritableDatabase().update("trick", contentValues, "_id=?", new String[]{Integer.toString(trickModel.Id)});
    }

    public void moveTrick(int id, String tag) {
        ContentValues contentValues = new ContentValues();

        contentValues.put("tag", tag);

        getWritableDatabase().update("trick", contentValues, "_id=?", new String[]{Integer.toString(id)});
    }

    public List<String> getTabList() {


        List<String> collection = new ArrayList<>();
        collection.add("Others");
        Cursor cursor = getReadableDatabase().rawQuery("select name from tab order by name ", null);

        while (cursor.moveToNext()) {
            String tabName = cursor.getString(0);
            if (tabName.equals("Others")) continue;
            ;
            collection.add(tabName);
        }
        cursor.close();


        return collection;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS `tab` ( `_id` INTEGER, `name` TEXT, PRIMARY KEY(`_id`) )");
        sqLiteDatabase.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `name_u` ON `tab` (`name` )");

        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS `trick` ( `_id` INTEGER, `title` TEXT, `content` TEXT, `tag` TEXT, `createAt` INTEGER, `updateAt` INTEGER, PRIMARY KEY(`_id`) )");

        sqLiteDatabase.setLocale(Locale.CHINA);
    }
    // CREATE UNIQUE INDEX `title_tag_u` ON `trick` (`title` ,`tag` )


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public Databases(Context context, String name) {
        super(context, name, null, 1);
    }


}
