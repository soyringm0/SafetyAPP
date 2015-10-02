package com.stevenkovach.chemapp;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;


public class chemSearch extends Activity {

    String minput;
    public String chemName;
    public double IDLH;
    public double PEL;
    public double Density;
    public double MW;
    public double BPc;
    public double BPf;
    public double LFL;
    public double UFL;
    public double FlashPoint;

    public double lengthValueNum;
    public double widthValueNum;
    public double heightValueNum;
    public double amountSpilledValueNum;
    public double roomVentRateValueNum;
    public double volume;

    public int workSpaceChecked;
    public int hoodChecked;
    public int labChecked;
    public int customChecked;


    private LVCursorAdapter customAdapter;
    private static final String TAG = chemSearch.class.getSimpleName();

    public boolean introTextRan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chem_search);

        minput = "xxxx";


        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            getAllExtras(extras);
        }

        final DataAdapter mDbHelper = new DataAdapter(getApplicationContext());
        mDbHelper.createDatabase();
        mDbHelper.open();

        Cursor testdata = mDbHelper.getTestData(minput);


        SearchView msearchView = (SearchView) findViewById(R.id.searchView);
        ListView listView = (ListView) findViewById(R.id.listView);



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "clicked on item: " + position);
                mDbHelper.createDatabase();
                mDbHelper.open();
                Cursor mtempcurm = mDbHelper.getTestDataID(Long.toString(id));
                //   mDbHelper.close();

                if(mtempcurm.getCount()==0)
                {
                    //Toast.makeText(chemSearch.this,"oups nothing got",Toast.LENGTH_LONG).show();
                    return;
                }
                StringBuffer buffer=new StringBuffer();
                while(mtempcurm.moveToNext())
                {
                    chemName = mtempcurm.getString(1);
                    IDLH = mtempcurm.getDouble(2);
                    PEL = mtempcurm.getDouble(3);
                    Density = mtempcurm.getDouble(4);
                    MW = mtempcurm.getDouble(5);
                    BPc = mtempcurm.getDouble(6);
                    BPf = mtempcurm.getDouble(7);
                    LFL = mtempcurm.getDouble(8);
                    UFL = mtempcurm.getDouble(9);
                    FlashPoint = mtempcurm.getDouble(10);

                    buffer.append(""+mtempcurm.getString(1)+"");
                    //Toast.makeText(chemSearch.this,buffer.toString(),Toast.LENGTH_LONG).show();

                }

                Intent chemFound = new Intent(chemSearch.this, chemProperties.class);
                putAllExtras(chemFound);
                startActivity(chemFound);

                //Toast.makeText(MainActivity.this,Integer.toString(position) + Long.toString(id),Toast.LENGTH_LONG).show();


            }
        });


        final LVCursorAdapter todoAdapter = new LVCursorAdapter(this, testdata);
        listView.setAdapter(todoAdapter);


        // customAdapter.changeCursor(databaseHelper.getAllData());


        //String[] from = new String[] {Contacts.People.NAME};
        //int[]  to = new int[] { R.id.itemTextView };
        //SimpleCursorAdapter adapter = new SimpleCursorAdapter(getApplicationContext(),
        //        R.layout.fuellayout,
        //        testdata, from, to);

        // Setup cursor adapter using cursor from last step
        // LVCursorAdapter todoAdapter = new LVCursorAdapter(MainActivity.this, testdata,0);
        // Attach cursor adapter to the ListView
        //lvItems.setAdapter(todoAdapter);

        //lvItems.setAdapter(adapter);

        msearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                 if (newText.length() != 0) {
                    System.out.println("--->" + newText);
                    // handle search here
                    // mDbHelper.createDatabase();
                    mDbHelper.createDatabase();
                    mDbHelper.open();
                    Cursor newCursor = mDbHelper.getTestData(newText);
                    todoAdapter.changeCursor(newCursor);
                    // mDbHelper.close();

                    return true;
                }
                return false;

            }


            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
        });




        // mDbHelper.close();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chem_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public static int safeLongToInt(long l) {
        if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
            throw new IllegalArgumentException
                    (l + " cannot be cast to int without changing its value.");
        }
        return (int) l;
    }

    protected void putAllExtras(Intent intent)
    {
        intent.putExtra("chemName", chemName);
        intent.putExtra("IDLH", IDLH);
        intent.putExtra("PEL", PEL);
        intent.putExtra("Density", Density);
        intent.putExtra("MW", MW);
        intent.putExtra("BPc", BPc);
        intent.putExtra("BPf", BPf);
        intent.putExtra("LFL", LFL);
        intent.putExtra("UFL", UFL);
        intent.putExtra("FlashPoint", FlashPoint);
        intent.putExtra("lengthValue",lengthValueNum);
        intent.putExtra("widthValue",widthValueNum);
        intent.putExtra("heightValue",heightValueNum);
        intent.putExtra("roomVentRateValue",roomVentRateValueNum);
        intent.putExtra("amountSpilledValue",amountSpilledValueNum);

        intent.putExtra("hoodIsChecked", hoodChecked);
        intent.putExtra("labIsChecked", labChecked);
        intent.putExtra("customIsChecked", customChecked);
        intent.putExtra("workSpaceIsChecked", workSpaceChecked);
        intent.putExtra("volume", volume);

        intent.putExtra("introTextRan",introTextRan);
    }

    protected void getAllExtras(Bundle extras)
    {
        chemName = extras.getString("chemName");
        IDLH = extras.getDouble("IDLH");
        PEL = extras.getDouble("PEL");
        Density = extras.getDouble("Density");
        MW = extras.getDouble("MW");
        BPc = extras.getDouble("BPc");
        BPf = extras.getDouble("BPf");
        LFL = extras.getDouble("LFL");
        UFL = extras.getDouble("UFL");
        FlashPoint = extras.getDouble("FlashPoint");
        lengthValueNum = extras.getDouble("lengthValue");
        widthValueNum = extras.getDouble("widthValue");
        heightValueNum = extras.getDouble("heightValue");
        amountSpilledValueNum = extras.getDouble("amountSpilledValue");
        roomVentRateValueNum = extras.getDouble("roomVentRateValue");
        workSpaceChecked = extras.getInt("workSpaceIsChecked");
        hoodChecked = extras.getInt("hoodIsChecked");
        labChecked = extras.getInt("labIsChecked");
        customChecked = extras.getInt("customIsChecked");
        volume = extras.getDouble("volume");

        introTextRan = extras.getBoolean("introTextRan");
    }
}
