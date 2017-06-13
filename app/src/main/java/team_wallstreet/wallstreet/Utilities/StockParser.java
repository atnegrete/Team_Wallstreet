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

    private ArrayList<Date> date;
    private double[] open, high, low, close, closeAdj;
    private int[] volume;

    /**
     *  Parse CSV file into respective components and store them in a HashMap
     *  (Date, Open, High, Low, Close, Adj Close, Volume)
     *  (Date (2012-05-21), double, double, double, double, double, int)
     *
     * @param stockCSV Csv file obtained from Yahoo Stocks.
     */
    public void StockParser(String stockCSV) {




        String entry;
        Scanner scanner = new Scanner(stockCSV);
        Scanner tokenizer;

        // Moves scanner past csv headers
        scanner.nextLine();

        while (scanner.hasNextLine()) {
            entry = scanner.nextLine();
            tokenizer = new Scanner(entry);

            // Change string to a Date and store in date.
            try {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd");
                date.add(df.parse(tokenizer.next()));
            } catch (ParseException e) {
                e.printStackTrace();
                date.add(null);
            }



            tokenizer.close();
        }
        scanner.close();
    }

}
