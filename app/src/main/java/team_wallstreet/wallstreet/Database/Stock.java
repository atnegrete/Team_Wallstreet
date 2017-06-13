package team_wallstreet.wallstreet.Database;

import java.util.Date;

/**
 * Created by TJ on 6/13/2017.
 */

public class Stock {

    Date Date;
    double Open;
    double High;
    double Low;
    double Close;
    double AdjClose;
    int Volume;

    /** (Date, Open, High, Low, Close, Adj Close, Volume)
     *  (Date (2012-05-21), double, double, double, double, double, int)
     *
     */
    public void Stock(Date d, double o, double h, double l,
                      double c, double aC, int v) {
        Date = d;
        Open = o;
        High = h;
        Low = l;
        Close = c;
        AdjClose = aC;
        Volume = v;
    }

    public Date getDate() {
        return Date;
    }

    public double getOpen() {
        return Open;
    }

    public double getHigh() {
        return High;
    }

    public double getLow() {
        return Low;
    }

    public double getClose() {
        return Close;
    }

    public double getAdjClose() {
        return AdjClose;
    }

    public int getVolume() {
        return Volume;
    }

    public void setDate(Date d) {
        this.Date = d;
    }

    public void setOpen(double o) {
        this.Open = o;
    }

    public void setHigh(double h) {
        this.High = h;
    }

    public void setLow(double l) {
        this.Low = l;
    }

    public void setClose(double c) {
        this.Close = c;
    }

    public void setAdjClose(double aC) {
        this.AdjClose = aC;
    }

    public void setVolume(int v) {
        this.Volume = v;
    }
}
