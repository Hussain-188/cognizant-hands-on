public class ForecastingTest {

    public static void main(String[] args) {
        double currentValue = 1000.0;
        double[] growthRates = {0.05, 0.04, 0.06};
        int periodsAhead = 5;

        double forecast = ForecastingAlgorithms.forecastFutureValue(currentValue, growthRates, periodsAhead);

        System.out.println("Financial Forecasting Result:");
        System.out.println("Current Value: " + currentValue);
        System.out.print("Growth Rates: ");
        for (int i = 0; i < growthRates.length; i++) {
            System.out.print((growthRates[i] * 100) + "%");
            if (i < growthRates.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println();
        System.out.println("Periods Ahead: " + periodsAhead);
        System.out.printf("Forecasted Future Value: %.2f%n", forecast);
    }
}