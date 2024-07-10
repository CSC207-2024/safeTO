package backend.app.src.main.java.dataAccess;

import backend.app.src.main.java.access.CrimeDataFetcher;
import backend.app.src.main.java.access.CrimeDataConverter;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.List;

public class CarStealingModelDemo {
    public static void main(String[] args) {
        CrimeDataFetcher fetcher = new CrimeDataFetcher();
        CrimeDataConverter converter = new CrimeDataConverter();

        CrimeDataFetcherInterface<StolenCarData> stolenCarDataFetcher = new StolenCarDataFetcher(fetcher, converter);
        AutoTheftCalculator autoTheftCalculator = new AutoTheftCalculator(stolenCarDataFetcher);

        double latitude = 43.64444078400003;
        double longitude = -79.40006913299997;
        double radius = 200.0;
        int threshold = 10; // 设置阈值

        // 获取过去一年的被盗汽车信息
        List<StolenCarData> pastYearData = autoTheftCalculator.getCrimeDataWithinRadiusPastYear(latitude, longitude, radius);
        pastYearData.sort(Comparator.comparing(StolenCarData::getOccDate)); // 按日期排序

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        System.out.println("All Auto Theft in the past year within the radius:");
        int index = 1;
        for (StolenCarData data : pastYearData) {
            double distance = autoTheftCalculator.calculateDistance(latitude, longitude, data.getLatitude(), data.getLongitude());
            String formattedDate = dateFormat.format(data.getOccDate());
            System.out.printf("#%d, occur date: %s, distance from you: %.2f meters%n", index++, formattedDate, distance);
        }

        // 获取数据集中所有的被盗汽车信息
        List<StolenCarData> allData = autoTheftCalculator.getCrimeDataWithinRadius(latitude, longitude, radius);
        allData.sort(Comparator.comparing(StolenCarData::getOccDate));

        System.out.println("ALL known Auto Theft within the radius:");
        index = 1;
        for (StolenCarData data : allData) {
            double distance = autoTheftCalculator.calculateDistance(latitude, longitude, data.getLatitude(), data.getLongitude());
            String formattedDate = dateFormat.format(data.getOccDate());
            System.out.printf("#%d, occur date: %s, distance from you: %.2f meters%n", index++, formattedDate, distance);
        }

        // 计算每年的平均发生率
        double lambda = autoTheftCalculator.calculateAnnualAverageIncidents(allData); // 使用每年的平均发生率作为λ值
        double probability = autoTheftCalculator.calculatePoissonProbability(lambda, threshold);

        // 显示概率和建议
        System.out.printf("Based on past data, if you park here, there's a %.2f%% chance that your car will be stolen more than %d time(s) within a year. We recommend considering alternative parking locations.%n", probability * 100, threshold);

        if (probability > 0.1) {
            System.out.println("WARNING: Don't park here!");
        } else {
            System.out.println("Safe to park here!");
        }
    }
}