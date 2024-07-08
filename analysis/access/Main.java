
package analysis.access;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        CrimeDataFetcher fetcher = new CrimeDataFetcher();
        StolenCarDataFetcher stolenCarDataFetcher = new StolenCarDataFetcher(fetcher);
        src.DataAccess.AutoTheftCalculator calculator = new src.DataAccess.AutoTheftCalculator(stolenCarDataFetcher);

        double latitude = 43.64444078400003;
        double longitude = -79.40006913299997;
        double radius = 5000.0;

        // stolen car info within past year
        List<src.DataAccess.StolenCarData> pastYearData = calculator.getStolenCarDataWithinRadiusPastYear(latitude, longitude, radius);
        pastYearData.sort(Comparator.comparing(src.DataAccess.StolenCarData::getOccDate)); // sort by date

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        System.out.println("All Auto Theft in the past year within the radius：");
        int index = 1;
        for (src.DataAccess.StolenCarData data : pastYearData) {
            double distance = calculator.calculateDistance(latitude, longitude, data.getLatitude(), data.getLongitude());
            String formattedDate = dateFormat.format(data.getOccDate());
            System.out.printf("#%d, occur date: %s, distance from you: %.2f meters%n", index++, formattedDate, distance);
        }

        // all stolen car info in the data set
        List<src.DataAccess.StolenCarData> allData = calculator.getStolenCarDataWithinRadius(latitude, longitude, radius);
        allData.sort(Comparator.comparing(src.DataAccess.StolenCarData::getOccDate));

        System.out.println("ALL known Auto Theft within the radius：");
        index = 1;
        for (src.DataAccess.StolenCarData data : allData) {
            double distance = calculator.calculateDistance(latitude, longitude, data.getLatitude(), data.getLongitude());
            String formattedDate = dateFormat.format(data.getOccDate());
            System.out.printf("#%d, occur date: %s, distance from you: %.2f meters%n", index++, formattedDate, distance);
        }

        // probability of being stolen
        double lambda = pastYearData.size(); // λ is the number of thefts within the radius in the past year
        double probability = calculator.calculatePoissonProbability(lambda, 1);

        // warning if the probability is over 10%
        if (probability > 0.1) {
            System.out.println("WARNING: Don't park here!");
        } else {
            System.out.println("Safe to park here!");
        }
    }
}
