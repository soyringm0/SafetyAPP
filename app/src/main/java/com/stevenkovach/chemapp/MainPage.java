package com.stevenkovach.chemapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DecimalFormat;


public class MainPage extends Activity {

    private static final String hoodIsChecked = "hoodIsChecked";
    private static final String labIsChecked = "labIsChecked";
    private static final String customIsChecked = "customIsChecked";
    private static final String workSpaceIsChecked = "workSpaceIsChecked";


    private String[] standards;
    private String[] roomUnits;
    private String[] ventUnits;
    private String[] amountUnits;

    private Spinner stdSizeSpinner;
    private Spinner roomUnitSpinner;
    private Spinner ventUnitSpinner;
    private Spinner amountSpilledSpinner;

    private LinearLayout mRoomUnitsLayout;
    private LinearLayout mWorkSpaceLayout;
    private LinearLayout mChemicalInfoLayout;

    private CheckBox mCustomCheck;
    private CheckBox mHoodCheck;
    private CheckBox mLabCheck;

    private Button mWorkSpaceButton;
    private Button mStartTimerButton;
    private Button mFindChemButton;
    private Button mChemStorageButton;

    private TextView mHours;
    private TextView mMinutes;
    private TextView mSeconds;


    private EditText mLengthValue;
    private EditText mWidthValue;
    private EditText mHeightValue;
    private EditText mAmountSpilledValue;
    private EditText mRoomVentRateValue;

    private int hoodChecked = 0;
    private int labChecked = 0;
    private int customChecked = 0;
    private int workSpaceChecked = 0;

    private SearchView chemicalSearch;

    private boolean workSpaceExpanded = false;
    private boolean chemicalChosen = false;

    //Unit Conversions
    public static final double feetToMeters = 0.3048;


    public double lengthValueNum = 0;
    public double widthValueNum = 0;
    public double heightValueNum = 0;
    public double amountSpilledValueNum = 0;
    public double roomVentRateValueNum = 0;
    public double volume;
    public double initialHSC;

    public double safeHours;
    public double safeMinutes;
    public double safeSeconds;
    public double IDLH;
    public double PEL;
    public double Density;
    public double MW;
    public double BPc;
    public double BPf;
    public double LFL;
    public double UFL;
    public double FlashPoint;
    public double VDensity;

    public String mInput;
    public String chemName;

    public boolean introTextRan = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);


        //Find View by Ids
        mRoomUnitsLayout = (LinearLayout) findViewById(R.id.roomUnitsLayout);
        mWorkSpaceLayout = (LinearLayout) findViewById(R.id.workSpaceLayout);
        mChemicalInfoLayout = (LinearLayout) findViewById(R.id.chemicalInfoLayout);

        mCustomCheck = (CheckBox) findViewById(R.id.customCheck);
        mHoodCheck = (CheckBox) findViewById(R.id.hoodCheck);
        mLabCheck = (CheckBox) findViewById(R.id.labCheck);

        mWorkSpaceButton = (Button) findViewById(R.id.workSpaceButton);
        mStartTimerButton = (Button) findViewById(R.id.startTimerButton);
        mFindChemButton = (Button) findViewById(R.id.findChemButton);
        mChemStorageButton = (Button) findViewById(R.id.chemStorageButton);

        mLengthValue = (EditText) findViewById(R.id.lengthValue);
        mWidthValue = (EditText) findViewById(R.id.widthValue);
        mHeightValue = (EditText) findViewById(R.id.heightValue);
        mRoomVentRateValue = (EditText) findViewById(R.id.roomVentRateValue);
        mAmountSpilledValue = (EditText) findViewById(R.id.amountSpilledValue);

        mHours = (TextView) findViewById(R.id.hours);
        mMinutes = (TextView) findViewById(R.id.minutes);
        mSeconds = (TextView) findViewById(R.id.seconds);



        //Standard room size selection
        standards = getResources().getStringArray(R.array.standardSizeList);
        stdSizeSpinner = (Spinner) findViewById(R.id.standardSizeSpinner);
        ArrayAdapter<String> stdSizeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, standards);
        stdSizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stdSizeSpinner.setAdapter(stdSizeAdapter);

        //Room dimension units selection
        roomUnits = getResources().getStringArray(R.array.roomUnitList);
        roomUnitSpinner = (Spinner) findViewById(R.id.roomUnitSpinner);
        ArrayAdapter<String>roomUnitsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, roomUnits);
        roomUnitsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roomUnitSpinner.setAdapter(roomUnitsAdapter);

        roomUnitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                calculateVolume();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Ventilation rate units selection
        ventUnits = getResources().getStringArray(R.array.ventUnitList);
        ventUnitSpinner = (Spinner) findViewById(R.id.ventUnitSpinner);
        ArrayAdapter<String>ventUnitsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ventUnits);
        ventUnitsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ventUnitSpinner.setAdapter(ventUnitsAdapter);

        //Amount Spilled units selection
        amountUnits = getResources().getStringArray(R.array.amountUnits);
        amountSpilledSpinner = (Spinner) findViewById(R.id.amountSpilledSpinner);
        ArrayAdapter<String>amountSpilledUnitsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, amountUnits);
        amountSpilledUnitsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        amountSpilledSpinner.setAdapter(amountSpilledUnitsAdapter);

        amountSpilledSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                calculateSafeTime();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        roomUnitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                calculateSafeTime();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ventUnitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                calculateSafeTime();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        calculateSafeTime();


        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            getAllExtras(extras);
            mFindChemButton.setText(chemName);

            updateLayouts();

            if(extras.containsKey("NotificationMessage"))
            {
                updateLayouts();
            }

            chemicalChosen = true;
            checkNull();
            //calculateVolume();
            calculateSafeTime();

        }


        if(!introTextRan)
        {
            AlertDialog.Builder intro = new AlertDialog.Builder(MainPage.this);
            intro.setTitle("ATTENTION!");
            intro.setMessage(R.string.introMessage);
            intro.setCancelable(false);
            intro.setPositiveButton("I Understand",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    introTextRan = true;
                }
            });

            intro.create();
            intro.show();

            introTextRan = true;
        }

        //Buttons for collapse menus
        mWorkSpaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                workSpaceExpanded = !workSpaceExpanded;
                if(workSpaceExpanded == false)
                {
                    mWorkSpaceLayout.setVisibility(View.GONE);
                    workSpaceChecked = 0;
                }
                else if(workSpaceExpanded == true)
                {
                    mWorkSpaceLayout.setVisibility(View.VISIBLE);
                    workSpaceChecked = 1;
                }
            }
        });

        //Sends user to new activity to search for chemical
        mFindChemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent findChemical = new Intent(MainPage.this, chemSearch.class);
                putAllExtras(findChemical);


                startActivity(findChemical);
            }
        });


        //Choose Hood, Lab or custom, other ones blank
        mHoodCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mLabCheck.isChecked() || mCustomCheck.isChecked())
                {
                    mLabCheck.setChecked(false);    //Lab check set to blank
                    //stdSizeSpinner.setVisibility(View.VISIBLE); //Spinner visible
                    mCustomCheck.setChecked(false); //Custom check set to blank
                    mRoomUnitsLayout.setVisibility(View.GONE);  //Custom layout gone
                    hoodChecked = 1;
                    labChecked = 0;
                    customChecked = 0;
                }
                else
                {
                    if(mHoodCheck.isChecked()) {
                        stdSizeSpinner.setVisibility(View.VISIBLE);
                        hoodChecked = 1;
                        labChecked = 0;
                        customChecked = 0;
                    }
                    else{
                        stdSizeSpinner.setVisibility(View.GONE);
                        hoodChecked = 0;
                    }
                }
            }
        });

        //Choose Hood, Lab or custom, other ones blank
        mLabCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mHoodCheck.isChecked() || mCustomCheck.isChecked())
                {
                    mHoodCheck.setChecked(false);
                    stdSizeSpinner.setVisibility(View.VISIBLE);
                    mCustomCheck.setChecked(false);
                    mRoomUnitsLayout.setVisibility(View.GONE);
                    labChecked = 1;
                    hoodChecked = 0;
                    customChecked = 0;
                }
                else
                {
                    if(mLabCheck.isChecked()) {
                        stdSizeSpinner.setVisibility(View.VISIBLE);
                        labChecked = 1;
                        hoodChecked = 0;
                        customChecked = 0;
                    }
                    else{
                        stdSizeSpinner.setVisibility(View.GONE);
                        labChecked = 0;
                    }
                }

            }
        });

        //Choose Hood, Lab or custom, other ones blank
        mCustomCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mLabCheck.isChecked() || mHoodCheck.isChecked())
                {
                    mLabCheck.setChecked(false);
                    mHoodCheck.setChecked(false);
                    stdSizeSpinner.setVisibility(View.GONE);
                    mRoomUnitsLayout.setVisibility(View.VISIBLE);
                    customChecked = 1;
                    labChecked = 0;
                    hoodChecked = 0;
                }
                else
                {
                    if(mCustomCheck.isChecked()) {
                        mRoomUnitsLayout.setVisibility(View.VISIBLE);
                        customChecked = 1;
                        labChecked = 0;
                        hoodChecked = 0;
                    }
                    else{
                        mRoomUnitsLayout.setVisibility(View.GONE);
                        customChecked = 0;
                    }
                }
            }
        });

        //Start the time activity
        mStartTimerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent startTimer = new Intent(MainPage.this, TimerActivity.class);
                float miliTotal = (float)(safeHours*3600000 + safeMinutes*60000 + safeSeconds*1000);

                startTimer.putExtra("miliTotal", miliTotal);
                startTimer.putExtra("initialHSC",initialHSC);
                putAllExtras(startTimer);
                startActivity(startTimer);

            }
        });

        mChemStorageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent showStorage = new Intent(MainPage.this, chemStorage.class);
                startActivity(showStorage);
            }
        });




        // Watch for changes in Length value
        mLengthValue.addTextChangedListener(new TextWatcher() {
             @Override
             public void beforeTextChanged(CharSequence s, int start, int count, int after) {

             }

             @Override
             public void onTextChanged(CharSequence s, int start, int before, int count) {

             }

             @Override
             public void afterTextChanged(Editable s) {
                checkNull();
                calculateVolume();
                calculateSafeTime();

             }
        });

        // Watch for changes in width value
        mWidthValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                checkNull();
                calculateVolume();
                calculateSafeTime();

            }
        });


        // Watch for changes in height value
        mHeightValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                checkNull();
                calculateVolume();
                calculateSafeTime();

            }
        });

        // Watch for changes in room ventilation rate
        mRoomVentRateValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                checkNull();
                calculateSafeTime();

            }
        });

        // Watches for changes in amount spilled
        mAmountSpilledValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                checkNull();
                calculateSafeTime();
            }
        });


    }


    private void calculateVolume() {

        volume = lengthValueNum * widthValueNum * heightValueNum;
        if(roomUnitSpinner.getSelectedItemPosition() == 0)
        {
            volume = volume * Math.pow(feetToMeters,3);
        }

        volume = round(volume);

    }

    private void calculateSafeTime(){

        double labFlowRate;
        double moleHSC;
        double moleInLab;
        double labVolOverFlow;
        double natLog;
        double volOverflow;
        double roomTemp = 293;
        double R = 0.00008205; //m^3*atm/mol*K
        double amountSpilledGrams = amountSpilledValueNum;
        double ventRateRmsPerHour = roomVentRateValueNum;
        double massChange;
        double moleLFL;

        VDensity = 1 * MW / (R*roomTemp);

        if(roomVentRateValueNum != 0 && amountSpilledValueNum != 0 && lengthValueNum != 0 && widthValueNum != 0 && heightValueNum != 0 && chemicalChosen == true) {

            //Convert vent rate to Rms/Hr
            if(ventUnitSpinner.getSelectedItemPosition() == 0)
            {
                ventRateRmsPerHour = roomVentRateValueNum;
            }
            else if(ventUnitSpinner.getSelectedItemPosition() == 1)
            {
                ventRateRmsPerHour = roomVentRateValueNum/volume;
            }



            //Convert amount spilled to grams for all choices
            if(amountSpilledSpinner.getSelectedItemPosition() == 0) //Liters
            {
                amountSpilledGrams = (amountSpilledValueNum*1000)*Density;
            }
            else if(amountSpilledSpinner.getSelectedItemPosition() == 1) //Milliliters
            {
                amountSpilledGrams = amountSpilledValueNum*Density;
            }
            else if(amountSpilledSpinner.getSelectedItemPosition() == 2) //Gallons
            {
                amountSpilledGrams = (amountSpilledValueNum/264.17)*(1000000)*Density;
            }
            else if(amountSpilledSpinner.getSelectedItemPosition() == 3)
            {
                amountSpilledGrams = amountSpilledValueNum;
            }

            //No if statement for grams because it st ays the same

            //Start Calculations
            labFlowRate = volume * ventRateRmsPerHour;// m^3/hr
            //volOverflow = volume / labFlowRate;
            moleHSC = amountSpilledGrams / MW;
            //moleInLab = ((1 * volume) / (R * roomTemp));
            //initialHSC = moleHSC / moleInLab;
            moleLFL = 0.25*volume*VDensity*LFL;
            massChange = Math.log(moleLFL/moleHSC);
            safeMinutes=-volume/labFlowRate*massChange * 60;

            //safeMinutes = -(volOverflow) * Math.log(PEL / (initialHSC*1000000)) * 60;

            safeMinutes = round(safeMinutes);
            safeSeconds = Math.round((safeMinutes % 1) * 60);
            safeHours = Math.floor(safeMinutes / 60);
            safeMinutes = Math.floor(safeMinutes - (safeHours * 60));


            if (safeHours < 10)
                mHours.setText("0" + (int) safeHours + ":");
            else
                mHours.setText((int) safeHours + ":");

            if (safeMinutes < 10)
                mMinutes.setText("0" + (int) safeMinutes + ":");
            else
                mMinutes.setText((int) safeMinutes + ":");

            if (safeSeconds < 10)
                mSeconds.setText("0" + (int) safeSeconds + "");
            else
                mSeconds.setText((int) safeSeconds + "");
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


    }

    protected double round(double d)
    {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        return Double.valueOf(twoDForm.format(d));
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(hoodIsChecked, hoodChecked);
        outState.putInt(labIsChecked, labChecked);
        outState.putInt(customIsChecked,customChecked);

    }

    public void checkNull()
    {
        if(mRoomVentRateValue.getText().toString().matches(""))
        {
            roomVentRateValueNum = 0;
        }
        else
        {
            roomVentRateValueNum = Double.parseDouble(mRoomVentRateValue.getText().toString());
        }
        if(mAmountSpilledValue.getText().toString().matches(""))
        {
            amountSpilledValueNum = 0;
        }
        else
        {
            amountSpilledValueNum = Double.parseDouble(mAmountSpilledValue.getText().toString());
        }
        if(mHeightValue.getText().toString().matches(""))
        {
            heightValueNum = 0;
        }
        else
        {
            heightValueNum = Double.parseDouble(mHeightValue.getText().toString());
        }
        if (mWidthValue.getText().toString().matches("")) {

            widthValueNum = 0;
        }
        else
        {
            widthValueNum = Double.parseDouble(mWidthValue.getText().toString());
        }
        if (mLengthValue.getText().toString().matches("")) {

            lengthValueNum = 0;
        }
        else
        {
            lengthValueNum = Double.parseDouble(mLengthValue.getText().toString());
        }
    }

    public void updateLayouts()
    {
        if(lengthValueNum != 0)
        {
            mLengthValue.setText(lengthValueNum+"");
        }
        if(widthValueNum != 0)
        {
            mWidthValue.setText(widthValueNum+"");
        }
        if(heightValueNum != 0)
        {
            mHeightValue.setText(heightValueNum+"");
        }
        if(roomVentRateValueNum != 0)
        {
            mRoomVentRateValue.setText(roomVentRateValueNum+"");
        }
        if(amountSpilledValueNum != 0)
        {
            mAmountSpilledValue.setText(amountSpilledValueNum+"");
        }

        if(workSpaceChecked == 1) {

            mWorkSpaceLayout.setVisibility(View.VISIBLE);

            if (hoodChecked == 1) {
                mHoodCheck.setChecked(true);
                mLabCheck.setChecked(false);    //Lab check set to blank
                //stdSizeSpinner.setVisibility(View.VISIBLE); //Spinner visible
                mCustomCheck.setChecked(false); //Custom check set to blank
                //mRoomUnitsLayout.setVisibility(View.GONE);  //Custom layout gone
                hoodChecked = 1;
            }

            if (labChecked == 1) {
                mLabCheck.setChecked(true);
                mHoodCheck.setChecked(false);
                stdSizeSpinner.setVisibility(View.VISIBLE);
                mCustomCheck.setChecked(false);
                // mRoomUnitsLayout.setVisibility(View.GONE);
                labChecked = 1;
            }

            if (customChecked == 1) {
                mCustomCheck.setChecked(true);
                mLabCheck.setChecked(false);
                mHoodCheck.setChecked(false);
                //stdSizeSpinner.setVisibility(View.GONE);
                mRoomUnitsLayout.setVisibility(View.VISIBLE);
                customChecked = 1;
            }
        }
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
        //VDensity = //extras.getDouble ("VDensity");
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

