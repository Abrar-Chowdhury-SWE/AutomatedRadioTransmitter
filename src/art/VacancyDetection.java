package art;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VacancyDetection {

    private static final int VACANCY_THRESHOLD = 30;

    public List<Double> findVacantFrequencies(String filePath) {
        List<Double> vacantFrequencies = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                double frequency = Double.parseDouble(parts[0]);
                int signalStrength = Integer.parseInt(parts[1]);

                if (signalStrength < VACANCY_THRESHOLD) {
                    vacantFrequencies.add(frequency);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        return vacantFrequencies;
    }

    public Double getNextVacantFrequency(List<Double> vacantFrequencies) {
        return vacantFrequencies.isEmpty() ? null : vacantFrequencies.get(0);
    }

    public static void main(String[] args) {
        VacancyDetection detection = new VacancyDetection();
        String filePath = "frequency_scan_results.csv";
        List<Double> vacantFrequencies = detection.findVacantFrequencies(filePath);
        System.out.println("Vacant Frequencies: " + vacantFrequencies);

        Double nextFrequency = detection.getNextVacantFrequency(vacantFrequencies);
        if (nextFrequency != null) {
            System.out.println("Next Vacant Frequency: " + nextFrequency + " MHz");
        }
    }
}
 {
    
}
