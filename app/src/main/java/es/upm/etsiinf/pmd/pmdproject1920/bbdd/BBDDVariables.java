package es.upm.etsiinf.pmd.pmdproject1920.bbdd;

public class BBDDVariables {
    public static final String BBDD_TABLE = "articulos";
    public static final String BBDD_ID_ARTICULO = "idArticulo";
    public static final String BBDD_ID_USER = "idUser";
    public static final String BBDD_TITLE = "sdfdsfdsfdsf";
    public static final String BBDD_SUBTITLE = "subtitle";
    public static final String BBDD_CATEGORY = "category";
    public static final String BBDD_ABSTRACT = "astract";
    public static final String BBDD_BODY = "body";
    public static final String BBDD_THUMBNAIL = "thumbnail";
    public static final String BBDD_DATE = "date";


    public static final String BBDD_CREATE_TABLE_ARTICLES =
            "CREATE TABLE IF NOT EXISTS "+ BBDD_TABLE + " ( " +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    BBDD_ID_ARTICULO + " INTEGER, " +
                    BBDD_ID_USER + " TEXT, " +
                    BBDD_TITLE + " TEXT, " +
                    BBDD_SUBTITLE + " TEXT, " +
                    BBDD_CATEGORY + " TEXT, " +
                    BBDD_ABSTRACT + " TEXT, " +
                    BBDD_BODY + " TEXT, " +
                    BBDD_THUMBNAIL + " TEXT, " +
                    BBDD_DATE + " TEXT);";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS "+ BBDD_TABLE +";";
}