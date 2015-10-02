package com.stevenkovach.chemapp;

/**
 * Created by Jonny on 4/22/2015.
 */
public class CalcData {


    protected String mFuel;
    protected double mIDLH;
    protected double mPEL;
    protected double mDensity;
    protected double mMW;
    protected double mBOc;
    protected double mBPf;
    protected double mLEL;
    protected double mUEL;
    protected double mFlashPoint;




    public CalcData(String name, double a, double b,double c, double d, double e, double f, double g, double h, double i) {

          mFuel=name;
          mIDLH=a;
          mPEL=b;
          mDensity=c;
          mMW=d;
          mBOc=e;
          mBPf=f;
          mLEL=g;
          mUEL=h;
          mFlashPoint=i;

    }

    @Override
    public String toString() {
        return "Name: " + mFuel + "\nScore: " + Double.toString(mIDLH) + "\nDate: " + Double.toString(mPEL);
    }



}


