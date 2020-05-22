package es.upm.etsiinf.pmd.pmdproject1920.bbdd;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import es.upm.etsiinf.pmd.pmdproject1920.model.Article;

public class BBDDHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "BBDDArticulos";
    private static final int DATABASE_VERSION = 1;

    public BBDDHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {// crear la estructura y datos initicales de la base de datos
        sqLiteDatabase.execSQL(BBDDVariables.BBDD_CREATE_TABLE_ARTICLES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL( BBDDVariables.SQL_DELETE_ENTRIES );
        onCreate(db);
    }


}
