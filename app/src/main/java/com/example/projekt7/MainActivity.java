package com.example.projekt7;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private LoaderManager.LoaderCallbacks<Cursor> mCallbacks;
    private SimpleCursorAdapter mAdapterKursora;
    private ListView mLista;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        for(int i =0 ;i<10;i++){
            Rzecz rzecz =new Rzecz();
            rzecz.setKolumna1("Telefon"+i);
            rzecz.setKolumna2("Model"+i);
            rzecz.setKolumna3("1."+i);
            PomocnikBD bd = new PomocnikBD(this);
            rzecz.setKolumna4("http://telefon/model/"+i);
            bd.dodaj(rzecz);
        }

        mLista = (ListView)findViewById(R.id.list);

        uruchomLoader();

        mLista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                obslugaEdycji(id);
            }
        });
        mLista.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        mLista.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }
            @Override
            public void onDestroyActionMode(ActionMode mode) { }
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater pompka = mode.getMenuInflater();
                pompka.inflate(R.menu.menu_kontekstowe, menu);
                return true;
            }
            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.akcyjka:
                        kasujZaznaczone();
                        return true;
                    case R.id.akcyjkas:
                        //kasujZaznaczone();
                        return true;
                    case R.id.akcyjkass:
                        //kasujZaznaczone();
                        return true;
                }
                return false;
            }
            @Override
            public void onItemCheckedStateChanged(ActionMode mode,int position, long id,
                                                  boolean checked) { }

        });

     /*  PomocnikBD baza=new PomocnikBD(getApplicationContext());
        Rzecz rzecz = new Rzecz();
        rzecz.setKolumna1("bardzo lubię");
        rzecz.setKolumna2("policję");
        baza.dodaj(rzecz);
        String[] mapujZ = new String[]{PomocnikBD.ID,PomocnikBD.KOLUMNA1,PomocnikBD.KOLUMNA2};
        int[] mapujDo = new int[]{R.id.etykieta4,R.id.etykieta1,R.id.etykieta2};

        SimpleCursorAdapter adapter = new SimpleCursorAdapter( this, R.layout.wiersz_listy,baza.wyswietl(),mapujZ ,mapujDo,0);

        mLista=(ListView)findViewById(R.id.list);                  //dla zmiennej odpowiadającej komponentowi  ListView
        mLista.setAdapter(adapter);

*/

        /*Rzecz rzecz = new Rzecz();
        rzecz.setKolumna1("policyjne ścierwo");
        rzecz.setKolumna2("to wróg największy");
        baza.dodaj(rzecz);

        for(Rzecz rz: baza.wyswietlAll()){
            tw.setText(tw.getText()+"\n"+rz.getId()+" "+rz.getKolumna1()+ " "+ rz.getKolumna2());
        }*/
        //baza.dodaj("lalal","policja");
        // baza.dodaj("lalal","karetka");



        // for(int i=3; i<=20; i++){
        //      baza.kasuj(i);
        // }
        //baza.aktualizuj(2,"nie policja","nie karetka");

      /*  while(kursor.moveToNext()){
            int nrK=kursor.getInt(0);
            String kolumna1=kursor.getString(1);
            String kolumna2=kursor.getString(2);
            tw.setText(tw.getText()+"\n"+nrK+" "+kolumna1+" "+kolumna2);
        }*/

    }



    private void kasujZaznaczone() {
        long zaznaczone[] = mLista.getCheckedItemIds();
        for (int i = 0; i < zaznaczone.length; ++i) {
            getContentResolver().delete(
                    ContentUris.withAppendedId(MojDiler.URI_ZAWARTOSCI,
                            zaznaczone[i]), null, null);
        }
    }
    private void uruchomLoader() {
        // getSupportLoaderManager().initLoader(0, //identyfikator loadera
        //       null, //argumenty (Bundle)
        //     this); //klasa implementująca LoaderCallbacks

        android.support.v4.app.LoaderManager.getInstance(this).initLoader(0, null, this).forceLoad();
        // utworzenie mapowania między kolumnami tabeli a kolumnami wyświetlanej listy
        String[] mapujZ = new String[]{ PomocnikBD.ID, PomocnikBD.KOLUMNA2}; //nazwy „kolumn” dostawcy
        int[] mapujDo = new int[]{ R.id.etykieta1, R.id.etykieta2}; //R.id... - identyfikatory komponentów
        // adapter wymaga aby wyniku zapytania znajdowała się kolumna _id
        mAdapterKursora = new SimpleCursorAdapter(this,
                R.layout.wiersz_listy, null, mapujZ, mapujDo, 0);
        mLista.setAdapter(mAdapterKursora);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // adapter wymaga aby wyniku zapytania znajdowała się kolumna _id
        String[] projekcja = { PomocnikBD.KOLUMNA1,PomocnikBD.KOLUMNA2}; // inne „kolumny” do wyświetlenia
        CursorLoader loaderKursora = new CursorLoader(MainActivity.this,
                MojDiler.URI_ZAWARTOSCI, projekcja, null,null, null);
        return loaderKursora;
    }
    @Override
    public void onLoadFinished(Loader<Cursor> arg0, Cursor dane) {
        switch(arg0.getId()){
            case 0:
                mAdapterKursora.swapCursor(dane);
                break;
        }

    }
    @Override
    public void onLoaderReset(Loader<Cursor> arg0) {
        mAdapterKursora.swapCursor(null);
    }
    /*private void dodajWartosc() {
        ContentValues wartosci = new ContentValues();
        EditText wartoscEdycja = (EditText)
                findViewById(R.id.wartosc_edycja);
        wartosci.put(PomocnikBD.WARTOSC,
                wartoscEdycja.getText().toString());
        Uri uriNowego = getContentResolver().insert(
                WartosciProvider.URI_ZAWARTOSCI, wartosci);
    }
*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.akcja_plus) {    //pierwsza akcja
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void obslugaEdycji(long id){
        Intent intencja = new Intent(this, EditActivity.class);             //stworzenie nowej aktywności Wybor.class
        intencja.putExtra("id",id);                           //tu przekazuje ilosc ocen już jako int do aktywności Wybor
        startActivity(intencja);
    }

}
