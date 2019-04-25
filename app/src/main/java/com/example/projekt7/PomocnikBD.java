package com.example.projekt7;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.SimpleCursorAdapter;

import java.util.LinkedList;
import java.util.List;

public class PomocnikBD extends SQLiteOpenHelper {

    private Context mKontekst;
    public final static int WERSJA_BAZY = 1;
    public final static String ID = "_id";
    public final static String NAZWA_BAZY = "projekt7";
    public final static String NAZWA_TABELI = "komorki";
    public final static String KOLUMNA1 = "producent";
    public final static String KOLUMNA2 = "model";
    public final static String KOLUMNA4 = "link";
    public final static String KOLUMNA3 = "wersja";
    public final static String TW_BAZY = "CREATE TABLE " + NAZWA_TABELI +
            "("+ID+" integer primary key autoincrement, " +
            KOLUMNA1+" text not null,"+
            KOLUMNA2+" text not null,"+
            KOLUMNA3+" text not null, "+
            KOLUMNA4+" text not null "+
            ");";
    private static final String KAS_BAZY = "DROP TABLE IF EXISTS "+NAZWA_TABELI;

    public PomocnikBD(Context context){
        super(context,NAZWA_BAZY,null, WERSJA_BAZY);
        mKontekst=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TW_BAZY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(KAS_BAZY);
        onCreate(db);

    }
    public void drop(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(TW_BAZY);
        db.close();
    }
    public void dodaj(Rzecz rzecz){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues zbior = new ContentValues();
        zbior.put(KOLUMNA1,rzecz.getKolumna1());
        zbior.put(KOLUMNA2,rzecz.getKolumna2());
        zbior.put(KOLUMNA3,rzecz.getKolumna3());
        zbior.put(KOLUMNA4,rzecz.getKolumna4());
        //db.insertOrThrow(NAZWA_TABELI,null,zbior);
        db.insert(NAZWA_TABELI,null,zbior);
        db.close();
    }

    public List<Rzecz> wyswietlAll(){
        List<Rzecz> rzeczy = new LinkedList<Rzecz>();
        String[] kolumny = {ID, KOLUMNA1, KOLUMNA2,KOLUMNA3,KOLUMNA4};
        SQLiteDatabase db = getReadableDatabase();

        Cursor kursor = db.query(NAZWA_TABELI,kolumny,null,null,null,null,null,null);
        while(kursor.moveToNext()){
            Rzecz rzecz = new Rzecz();
            rzecz.setId(kursor.getLong(0));
            rzecz.setKolumna1(kursor.getString(1));
            rzecz.setKolumna2(kursor.getString(2));
            rzecz.setKolumna3(kursor.getString(3));
            rzecz.setKolumna4(kursor.getString(4));
            rzeczy.add(rzecz);
        }
        db.close();
        return rzeczy;
    }

    public Cursor wyswietl(){

        SQLiteDatabase db = getReadableDatabase();
        String[] kolumny = {ID, KOLUMNA1, KOLUMNA2,KOLUMNA3,KOLUMNA4};
        Cursor kursor = db.query(NAZWA_TABELI,kolumny,null,null,null,null,null,null);
        return kursor;
    }

    public void kasuj(int id){
        SQLiteDatabase db = getWritableDatabase();
        String[] argumenty={""+id};
        db.delete(NAZWA_TABELI,"_id=?",argumenty);
        db.close();
    }

    public void aktualizuj(Rzecz rzecz){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues zbior = new ContentValues();
        zbior.put(KOLUMNA1,rzecz.getKolumna1());
        zbior.put(KOLUMNA2,rzecz.getKolumna2());
        zbior.put(KOLUMNA3,rzecz.getKolumna3());
        zbior.put(KOLUMNA4,rzecz.getKolumna4());
        String args[] = {rzecz.getId()+""};
        db.update(NAZWA_TABELI,zbior,"_id=?",args);
        db.close();
    }

    public Rzecz dajRzecz(int id){
        Rzecz rzecz = new Rzecz();
        SQLiteDatabase db = getReadableDatabase();

        String[] kolumny = { ID, KOLUMNA1, KOLUMNA2,KOLUMNA3,KOLUMNA4};
        String args[] = {id + ""};

        Cursor kursor = db.query(NAZWA_TABELI, kolumny , "_id=?" , args, null, null , null ,null );

        if(kursor!=null){
            kursor.moveToFirst();
            rzecz.setId(kursor.getLong(0));
            rzecz.setKolumna1(kursor.getString(1));
            rzecz.setKolumna2(kursor.getString(2));
            rzecz.setKolumna3(kursor.getString(3));
            rzecz.setKolumna4(kursor.getString(4));
        }
        db.close();
        return rzecz;
    }
}