package es.upm.etsiinf.pmd.pmdproject1920.bbdd;

public class BBDDVariables {
    public static final String BBDD_TABLE = "table";
    public static final String BBDD_ID_USER = "idUser";
    public static final String BBDD_TITLE = "title";
    public static final String BBDD_SUBTITLE = "subtitle";
    public static final String BBDD_CATEGORY = "category";
    public static final String BBDD_ABSTRACT = "astract";
    public static final String BBDD_BODY = "body";
    public static final String BBDD_THUMBNAIL = "thumbnail";
    public static final String BBDD_DATE = "date";


    public static final String BBDD_CREATE_TABLE_ARTICLES =
            "CREATE TABLE IF NOT EXIST "+ BBDD_TABLE +" ( " +
                    "    _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "    idUser INTEGER, " +
                    "    title TEXT, " +
                    "    subtitle TEXT " +
                    "    category TEXT " +
                    "    abstract TEXT " +
                    "    body TEXT " +
                    "    thumbnail TEXT " +
                    "    date INTEGER " +
                    ");";
}
