package com.stevenkovach.chemapp;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;


public class TimerActivity extends Activity {

    private Button btnStop;
    private TextView textViewTime;
    private TextView mIdlhTimerValue;
    private TextView mIdlhTimerText;

    public float miliTotal;

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
    public double initialHSC;

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

    public boolean timerStopped = false;
    public boolean introTextRan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);


        btnStop = (Button) findViewById(R.id.btnStop);
        textViewTime = (TextView) findViewById(R.id.textViewTime);
        mIdlhTimerValue = (TextView) findViewById(R.id.idlhTimerValue);
        mIdlhTimerText = (TextView) findViewById(R.id.idlhTimerText);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {

            miliTotal = extras.getFloat("miliTotal");

            getAllExtras(extras);
            initialHSC = extras.getDouble("initialHSC");
            mIdlhTimerText.setText(":  " + initialHSC);
            mIdlhTimerValue.setTextSize(mIdlhTimerText.getTextSize());
        }


        String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours((long) miliTotal),
                TimeUnit.MILLISECONDS.toMinutes((long) miliTotal) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours((long) miliTotal)),
                TimeUnit.MILLISECONDS.toSeconds((long) miliTotal) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) miliTotal)));

        textViewTime.setText(hms);

        final CounterClass timer = new CounterClass((long)miliTotal, 1000);

        timer.start();

        btnStop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(!timerStopped)
                {
                    timer.cancel();
                    timerStopped = true;
                    btnStop.setText("Start Over");

                }
                else if(timerStopped)
                {
                    timer.start();
                    timerStopped = false;
                    btnStop.setText("Stop");
                }

            }
        });


    }


    public class CounterClass extends CountDownTimer {

        int colorStart;
        int colorEnd;
        public CounterClass(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            miliTotal = millisInFuture;
            colorStart = Color.parseColor("#FF3030");
            colorEnd = Color.parseColor("#24D330");
        }


        @Override
        public void onTick(long millisUntilFinished) {

            float millis = millisUntilFinished;
            float millisprecenttotaltime = ((millis)/miliTotal)*100;
            String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours((long) millis),
                    TimeUnit.MILLISECONDS.toMinutes((long) millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours((long) millis)),
                    TimeUnit.MILLISECONDS.toSeconds((long) millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) millis)));
            System.out.println(hms);
            textViewTime.setText(hms);

            getWindow().getDecorView().getRootView().setBackgroundColor(interpolateColor(colorEnd, colorStart,
                    (millisprecenttotaltime / 100f)));




        }

        @Override
        public void onFinish() {
            // TODO Auto-generated method stub
            getWindow().getDecorView().getRootView().setBackgroundResource(R.drawable.frame_0232);

            Notification.Builder builder = new Notification.Builder(getApplicationContext());
            builder.setContentText("Timer is Done");
            builder.setWhen(System.currentTimeMillis());
            builder.setSmallIcon(R.drawable.ic_launcher);
            builder.setTicker("Timer is Done!");
            builder.setContentTitle("Timer is Done!");
            builder.setContentText("It is safe to enter the room");

            builder.setAutoCancel(true);
            builder.setDefaults(Notification.DEFAULT_ALL);

            Intent timerDone = new Intent(TimerActivity.this, MainPage.class);
            timerDone.putExtra("NotificationMessage", 1);
            timerDone.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            putAllExtras(timerDone);
            PendingIntent contentIntent = PendingIntent.getActivity(TimerActivity.this, 0, timerDone, PendingIntent.FLAG_UPDATE_CURRENT);

            builder.setContentIntent(contentIntent);
            Notification notification = builder.build();

            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(1,notification);

            textViewTime.setText("Completed");
            btnStop.setVisibility(View.GONE);
        }



    }

    private float interpolate(final float a, final float b,
                              final float proportion) {
        return (a + ((b - a) * proportion));
    }

    private int interpolateColor(final int a, final int b,
                                 final float proportion) {
        final float[] hsva = new float[3];
        final float[] hsvb = new float[3];
        Color.colorToHSV(a, hsva);
        Color.colorToHSV(b, hsvb);
        for (int i = 0; i < 3; i++) {
            hsvb[i] = interpolate(hsva[i], hsvb[i], proportion);
        }
        return Color.HSVToColor(hsvb);
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
