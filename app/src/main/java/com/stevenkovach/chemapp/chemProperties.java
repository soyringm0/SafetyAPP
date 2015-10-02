package com.stevenkovach.chemapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class chemProperties extends Activity {


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

    private TextView mChemName;
    private TextView mChemIDLH;
    private TextView mChemPEL;
    private TextView mChemDensity;
    private TextView mChemMW;
    private TextView mChemBPf;
    private TextView mChemLFL;
    private TextView mChemULF;
    private TextView mChemFlashPoint;

    private Button mOkayButton;

    public boolean introTextRan;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chem_properties);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            getAllExtras(extras);
        }

        mChemName = (TextView) findViewById(R.id.chemName);
        mChemIDLH = (TextView) findViewById(R.id.chemIDLH);
        mChemPEL = (TextView) findViewById(R.id.chemPEL);
        mChemDensity = (TextView) findViewById(R.id.chemDensity);
        mChemMW = (TextView) findViewById(R.id.chemMW);
        mChemBPf = (TextView) findViewById(R.id.chemBPf);
        mChemLFL = (TextView) findViewById(R.id.chemLFL);
        mChemULF = (TextView) findViewById(R.id.chemUFL);
        mChemFlashPoint = (TextView) findViewById(R.id.chemFlashPoint);

        mOkayButton = (Button) findViewById(R.id.okayButton);

        mChemName.setText(chemName);
        mChemIDLH.setText(IDLH+" ppm");
        mChemPEL.setText(PEL+" ppm");
        mChemDensity.setText(Density+" g/cm^3");
        mChemMW.setText(MW+" g/mol");
        mChemBPf.setText(BPf+"\u00b0F  ("+BPc+"\u00b0C)");
        mChemLFL.setText(LFL+"%");
        mChemULF.setText(UFL+"%");
        mChemFlashPoint.setText(FlashPoint+"\u00b0F");

        mOkayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent chemFound = new Intent(chemProperties.this, MainPage.class);
                putAllExtras(chemFound);
                startActivity(chemFound);
            }
        });

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
