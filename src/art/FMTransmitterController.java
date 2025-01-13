package art;


import com.fazecast.jSerialComm.SerialPort;

public class FMTransmitterController {

    private SerialPort serialPort;

    public FMTransmitterController(String portName) {
        // Open the serial port
        serialPort = SerialPort.getCommPort(portName);
        serialPort.setComPortParameters(9600, 8, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);
        serialPort.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 0, 0);

        if (serialPort.openPort()) {
            System.out.println("Port opened successfully.");
        } else {
            System.err.println("Failed to open the port.");
        }
    }

    public void sendCommand(String command) {
        try {
            byte[] commandBytes = command.getBytes();
            serialPort.getOutputStream().write(commandBytes);
            serialPort.getOutputStream().flush();
            System.out.println("Command sent: " + command);
        } catch (Exception e) {
            System.err.println("Error sending command: " + e.getMessage());
        }
    }

    public String readResponse() {
        StringBuilder response = new StringBuilder();
        try {
            while (serialPort.bytesAvailable() > 0) {
                char data = (char) serialPort.getInputStream().read();
                response.append(data);
            }
        } catch (Exception e) {
            System.err.println("Error reading response: " + e.getMessage());
        }
        return response.toString();
    }

    public void close() {
        if (serialPort != null && serialPort.isOpen()) {
            serialPort.closePort();
            System.out.println("Port closed.");
        }
    }

    public static void main(String[] args) {
        FMTransmitterController controller = new FMTransmitterController("COM3"); /
        controller.sendCommand("NEXT");
        String response = controller.readResponse();
        System.out.println("Response from transmitter: " + response);
        controller.close();
    }
}
