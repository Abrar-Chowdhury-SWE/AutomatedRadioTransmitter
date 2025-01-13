package art;


import com.fazecast.jSerialComm.SerialPort;
import java.io.FileWriter;
import java.io.IOException;

public class FrequencyScanner {

    private SerialPort serialPort;
    private static final double START_FREQUENCY = 87.5;
    private static final double END_FREQUENCY = 108.0;
    private static final double STEP = 0.1;

    public FrequencyScanner(String portName) {
        serialPort = SerialPort.getCommPort(portName);
        serialPort.setComPortParameters(9600, 8, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);
        serialPort.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 0, 0);

        if (serialPort.openPort()) {
            System.out.println("Port opened successfully.");
        } else {
            System.err.println("Failed to open the port.");
        }
    }

    public void scanFrequencies() {
        try (FileWriter writer = new FileWriter("frequency_scan_results.csv")) {
            writer.write("Frequency (MHz),Signal Strength\n");

            for (double freq = START_FREQUENCY; freq <= END_FREQUENCY; freq += STEP) {
                sendCommand("SET_FREQ:" + String.format("%.1f", freq));
                String response = readResponse();
                int signalStrength = parseSignalStrength(response);

                writer.write(freq + "," + signalStrength + "\n");
                System.out.println("Frequency: " + freq + " MHz, Signal Strength: " + signalStrength);
            }
        } catch (IOException e) {
            System.err.println("Error writing to log file: " + e.getMessage());
        } finally {
            closePort();
        }
    }

    private void sendCommand(String command) {
        try {
            serialPort.getOutputStream().write(command.getBytes());
            serialPort.getOutputStream().flush();
        } catch (IOException e) {
            System.err.println("Error sending command: " + e.getMessage());
        }
    }

    private String readResponse() {
        StringBuilder response = new StringBuilder();
        try {
            while (serialPort.bytesAvailable() > 0) {
                response.append((char) serialPort.getInputStream().read());
            }
        } catch (IOException e) {
            System.err.println("Error reading response: " + e.getMessage());
        }
        return response.toString();
    }

    private int parseSignalStrength(String response) {
        if (response.startsWith("SIGNAL:")) {
            return Integer.parseInt(response.split(":")[1].trim());
        }
        return 0; // Default to 0 if no valid response
    }

    private void closePort() {
        if (serialPort != null && serialPort.isOpen()) {
            serialPort.closePort();
            System.out.println("Port closed.");
        }
    }

    public static void main(String[] args) {
        FrequencyScanner scanner = new FrequencyScanner("COM3"); 
        scanner.scanFrequencies();
    }
}
