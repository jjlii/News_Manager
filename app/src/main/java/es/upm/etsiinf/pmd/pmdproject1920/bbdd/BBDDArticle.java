package es.upm.etsiinf.pmd.pmdproject1920.bbdd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import es.upm.etsiinf.pmd.pmdproject1920.model.Article;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BBDDArticle {

    private static BBDDHelper helper;
    private static SQLiteDatabase db;

    public static void init(Context c){
        helper = new BBDDHelper(c);
    }

    public static List<Article> loadAllArticles(){
        ArrayList<Article> result = new ArrayList<>();

        db = helper.getReadableDatabase();
        Cursor cursor = db.query(BBDDVariables.BBDD_TABLE,
                null,null,null,null,
                null,null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            Integer id = cursor.getInt(1);
            String idUser= cursor.getString(2);
            String title= cursor.getString(3);
            String subtitle= cursor.getString(4);
            String category= cursor.getString(5);
            String abstact = cursor.getString(6);
            String body = cursor.getString(7);
            String thumbnail = cursor.getString(8);
            String datetime = cursor.getString(9);
            Date d = new Date(Long.parseLong(datetime));

            Article a = new Article(category, title, abstact, body, subtitle, idUser);
            a.setThumbnail(thumbnail);
            a.setLastUpdate(d);
            a.setId(id);
            result.add(a);

            cursor.moveToNext();
        }

        return result;
    }

    public static boolean deleteArticle(Integer id)
    {
        return db.delete(BBDDVariables.BBDD_TABLE, BBDDVariables.BBDD_ID_ARTICULO + "=" + id, null) > 0;
    }

    public static void insertArticle(@org.jetbrains.annotations.NotNull Article a){
        db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(BBDDVariables.BBDD_ID_ARTICULO, a.getId());
        values.put(BBDDVariables.BBDD_ID_USER, a.getIdUser());
        values.put(BBDDVariables.BBDD_TITLE, a.getTitleText());
        values.put(BBDDVariables.BBDD_SUBTITLE, a.getSubtitleText());
        values.put(BBDDVariables.BBDD_CATEGORY, a.getCategory());
        values.put(BBDDVariables.BBDD_ABSTRACT, a.getAbstractText());
        values.put(BBDDVariables.BBDD_BODY, a.getBodyText());
        values.put(BBDDVariables.BBDD_THUMBNAIL, a.getThumbnail());
        values.put(BBDDVariables.BBDD_DATE, Long.toString(a.getLastUpdate().getTime()));

        long insertId = db.insert(BBDDVariables.BBDD_TABLE, null, values);

        Log.i("DBMessage", "New article inserted with id: "+ insertId);
    }
}
