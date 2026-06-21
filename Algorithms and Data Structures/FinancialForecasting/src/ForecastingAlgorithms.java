public class ForecastingAlgorithms {

    public static double forecastFutureValue(double currentValue, double[] growthRates, int periodsAhead) {
        validateInputs(currentValue, growthRates, periodsAhead);

        Double[] memo = new Double[periodsAhead + 1];
        return currentValue * recursiveGrowthMultiplier(growthRates, periodsAhead, memo);
    }

    private static double recursiveGrowthMultiplier(double[] growthRates, int periodsAhead, Double[] memo) {
        if (periodsAhead == 0) {
            return 1.0;
        }

        if (memo[periodsAhead] != null) {
            return memo[periodsAhead];
        }

        int growthIndex = (periodsAhead - 1) % growthRates.length;
        double multiplier = recursiveGrowthMultiplier(growthRates, periodsAhead - 1, memo)
                * (1 + growthRates[growthIndex]);

        memo[periodsAhead] = multiplier;
        return multiplier;
    }

    private static void validateInputs(double currentValue, double[] growthRates, int periodsAhead) {
        if (currentValue < 0) {
            throw new IllegalArgumentException("Current value must be non-negative.");
        }

        if (periodsAhead < 0) {
            throw new IllegalArgumentException("Periods ahead must be zero or greater.");
        }

        if (growthRates == null || growthRates.length == 0) {
            throw new IllegalArgumentException("At least one growth rate is required.");
        }

        for (double growthRate : growthRates) {
            if (growthRate <= -1.0) {
                throw new IllegalArgumentException("Growth rates must be greater than -100%.");
            }
        }
    }
}