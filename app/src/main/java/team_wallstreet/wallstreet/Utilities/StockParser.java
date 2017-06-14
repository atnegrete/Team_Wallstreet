package team_wallstreet.wallstreet.Utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

/**
 * Created by TJ on 6/13/2017.
 */

public class StockParser {

    private static final int SIZE_OF_HISTORY = 365;

    String StockCode;
    ArrayList<Date> DateList;
    double[] OpenList, HighList, LowList, CloseList, CloseAdjList;
    int[] VolumeList;

    public void StockParser() {

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
    public void StockParser(String stockCSV, String code) {

        StockParser();
        StockCode = code;

        int count = 0;
        Scanner scanner = new Scanner(stockCSV);
        Scanner tokenizer;

        // Moves scanner past csv headers
        scanner.nextLine();

        while (scanner.hasNextLine()) {
            parseStockString(scanner.nextLine(), count);
            count++;
        }

        scanner.close();
    }

    private void parseStockString(String data, int index) {

        Scanner scanner = new Scanner(data);

        // Change string to a Date and store in date.
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd");
            DateList.add(df.parse(scanner.next()));
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
