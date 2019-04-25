package com.example.projekt7;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class MojDiler extends ContentProvider {
    private PomocnikBD mPomocnik;
    private static final String IDENTYFIKATOR = "com.example.projekt7.MojDiler";
    public static final Uri URI_ZAWARTOSCI = Uri.parse("content://"+IDENTYFIKATOR+"/"+PomocnikBD.NAZWA_TABELI);
    private static final int CALA_TABELA = 1;
    private static final int WYBRANY_WIERSZ = 2;
    private static final UriMatcher sDopasowanieUri = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sDopasowanieUri.addURI(IDENTYFIKATOR,PomocnikBD.NAZWA_TABELI,CALA_TABELA);
        sDopasowanieUri.addURI(IDENTYFIKATOR,PomocnikBD.NAZWA_TABELI+"/#",WYBRANY_WIERSZ);
    }
    @Override
    public String getType(Uri uri) { return null;}
    @Override
    public boolean onCreate(){
        mPomocnik = new PomocnikBD(getContext()) ;
        return true;
    }
    @Override
    public Uri insert(Uri uri, ContentValues values){
        int typUri = sDopasowanieUri.match(uri);

        SQLiteDatabase db = mPomocnik.getWritableDatabase();

        long idDodanego = 0;
        switch(typUri){
            case CALA_TABELA:
                //db.insertOrThrow(NAZWA_TABELI,null,zbior);
                db.insert(PomocnikBD.NAZWA_TABELI,null,values);
                break;
            default:
                throw new IllegalArgumentException("Nieznane URI: "+ uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return Uri.parse(PomocnikBD.NAZWA_TABELI+"/"+ idDodanego);
    }
    public Cursor query(Uri uri, String[] projection,
                        String selection, String[] selectionArgs,
                        String sortOrder) {
        int typUri = sDopasowanieUri.match(uri);
        //otwieranie magazynu – np. bazy danych...
        SQLiteDatabase db = mPomocnik.getWritableDatabase();
        Cursor kursor = null;
        switch (typUri) {
            case CALA_TABELA:
                kursor = db.query(false,PomocnikBD.NAZWA_TABELI,projection,selection,selectionArgs,null,null,sortOrder,null,null);
                break;
            case WYBRANY_WIERSZ:
                kursor = db.query(false, PomocnikBD.NAZWA_TABELI,
                        projection,dodajIdDoSelekcji(selection, uri),
                        selectionArgs,null,null,sortOrder,null,null);
                break;
            default:
                throw new IllegalArgumentException("Nieznane URI: " + uri);
        }
        kursor.setNotificationUri(getContext().
                getContentResolver(), uri);
        return kursor;
    }
    @Override
    public int delete(Uri uri, String selection,
                      String[] selectionArgs) {
        int typUri = sDopasowanieUri.match(uri);
        SQLiteDatabase db = mPomocnik.getWritableDatabase();
        int liczbaUsunietych = 0;
        switch (typUri) {
            case CALA_TABELA:
                liczbaUsunietych = db.delete(PomocnikBD.NAZWA_TABELI,
                        selection, //WHERE
                        selectionArgs); //usuwanie rekordów

                break;
            case WYBRANY_WIERSZ:
                liczbaUsunietych = db.delete(PomocnikBD.NAZWA_TABELI,
                        dodajIdDoSelekcji(selection,uri), //WHERE
                        selectionArgs); //usuwanie rekordów
                break;
            default:
                throw new IllegalArgumentException("Nieznane URI: " +
                        uri);
        }
        //powiadomienie o zmianie danych
        getContext().getContentResolver().notifyChange(uri, null);
        return liczbaUsunietych;
    }
    @Override
    public int update(Uri uri, ContentValues values,
                      String selection, String[] selectionArgs) {
        int typUri = sDopasowanieUri.match(uri);
        SQLiteDatabase db = mPomocnik.getWritableDatabase();
        int liczbaZaktualizowanych = 0;
        switch (typUri) {
            case CALA_TABELA:
                liczbaZaktualizowanych = db.update(PomocnikBD.NAZWA_TABELI,values,selection,selectionArgs);
                break;
            case WYBRANY_WIERSZ:
                liczbaZaktualizowanych = db.update(PomocnikBD.NAZWA_TABELI,values,dodajIdDoSelekcji(selection,uri),selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Nieznane URI: " +
                        uri);
        } //powiadomienie o zmianie danych
        getContext().getContentResolver().notifyChange(uri, null);
        return liczbaZaktualizowanych;
    }
    private String dodajIdDoSelekcji(String selekcja, Uri uri) {
        //jeżeli już jest to dodajemy tylko dodatkowy warunek
        if (selekcja != null && !selekcja.equals(""))
            selekcja = selekcja + " and " + PomocnikBD.ID + "="
                    + uri.getLastPathSegment();
            //jeżeli nie ma WHERE tworzymy je od początku
        else
            selekcja = PomocnikBD.ID + "=" + uri.getLastPathSegment();
        return selekcja;
    }


}