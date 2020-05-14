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

    public static void init(Context c){
        helper = new BBDDHelper(c);
    }

    public static List<Article> loadAllArticles(){
        ArrayList<Article> result = new ArrayList<>();

        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(BBDDVariables.BBDD_TITLE,
                null,null,null,null,
                null,null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            long id = cursor.getLong(0);
            long idUser= cursor.getLong(1);
            String title= cursor.getString(2);
            String subtitle= cursor.getString(3);
            String abstact= cursor.getString(4);
            String category = cursor.getString(5);
            String body = cursor.getString(7);
            String thumbnail = cursor.getString(8);
            long datetime = cursor.getLong(9);
            Date d = new Date(datetime);

            Article a = new Article(category, title, abstact, body, subtitle, (int) idUser);
            a.setThumbnail(thumbnail);
            a.setLastUpdate(d);
            a.setId((int) id);
            result.add(a);

            cursor.moveToNext();
        }

        return result;
    }


    public static void newArticle(Article a){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(BBDDVariables.BBDD_ID_USER, Long.valueOf(a.getIdUser()));
        values.put(BBDDVariables.BBDD_TITLE, a.getTitleText());
        values.put(BBDDVariables.BBDD_SUBTITLE, a.getSubtitleText());
        values.put(BBDDVariables.BBDD_CATEGORY, a.getCategory());
        values.put(BBDDVariables.BBDD_ABSTRACT, a.getAbstractText());
        values.put(BBDDVariables.BBDD_BODY, a.getBodyText());
        values.put(BBDDVariables.BBDD_THUMBNAIL, a.getThumbnail());
        values.put(BBDDVariables.BBDD_DATE, a.getLastUpdate().getTime());

        long insertId = db.insert(BBDDVariables.BBDD_TABLE, null, values);

        Log.i("DBMessage", "New article inserted with id: "+ insertId);
    }
}
