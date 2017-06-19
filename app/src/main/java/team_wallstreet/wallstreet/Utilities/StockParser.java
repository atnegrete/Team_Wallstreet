package team_wallstreet.wallstreet.Utilities;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

/**
 * Created by TJ on 6/13/2017.
 */

public class StockParser {

    private static final int SIZE_OF_HISTORY = 10*365;

    String StockCode;
    ArrayList<Date> DateList;
    double[] OpenList, HighList, LowList, CloseList, CloseAdjList;
    int[] VolumeList;

    public StockParser() {

        DateList = new ArrayList();
        OpenList = new double[SIZE_OF_HISTORY];
        HighList = new double[SIZE_OF_HISTORY];
        LowList = new double[SIZE_OF_HISTORY];
        CloseList = new double[SIZE_OF_HISTORY];
        CloseAdjList = new double [SIZE_OF_HISTORY];
        VolumeList = new int[SIZE_OF_HISTORY];
    }

    /**
     *  Parse CSV file into respective components and store them in a HashMap
     *  (Date, Open, High, Low, Close, Adj Close, Volume)
     *  (Date (2012-05-21), double, double, double, double, double, int)
     *
     * @param stockCSV Csv file obtained from Yahoo Stocks.
     * @param code stock code.
     */
    public StockParser(String stockCSV, String code) {

        this();
        StockCode = code;

        int count = 0;
        Scanner scanner = new Scanner(stockCSV);

        // Moves scanner past csv headers
        System.out.println(scanner.nextLine());

        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();
            parseStockString(input, count);
            count++;
        }

        scanner.close();
    }

    private void parseStockString(String data, int index) {

        Scanner scanner = new Scanner(data);
        scanner.useDelimiter(",");

        // Change string to a Date and store in date.
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String date = scanner.next();
            DateList.add(df.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
            DateList.add(null);
        }

        OpenList[index] = scanner.nextDouble();
        HighList[index] = scanner.nextDouble();
        LowList[index] = scanner.nextDouble();
        CloseList[index] = scanner.nextDouble();
        CloseAdjList[index] = scanner.nextDouble();
        VolumeList[index] = scanner.nextInt();

        String input = String.format("Parsed Input: %s, %s, %s, %s, %s, %s, %s", getDateList().get(index),
                OpenList[index], HighList[index], LowList[index], CloseList[index], CloseAdjList[index], VolumeList[index]);

        Log.d("Data", input);

        scanner.close();
    }

    public ArrayList<Date> getDateList() {
        return DateList;
    }

    public double[] getOpenList() {
        return OpenList;
    }

    public double[] getHighList() {
        return HighList;
    }

    public double[] getLowList() {
        return LowList;
    }

    public double[] getCloseList() {
        return CloseList;
    }

    public double[] getCloseAdjList() {
        return CloseAdjList;
    }

    public int[] getVolumeList() {
        return VolumeList;
    }

    public String getStockCode() {
        return StockCode;
    }
}
