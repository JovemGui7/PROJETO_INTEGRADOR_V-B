// ============================================================
//  SISTEMA DE MONITORAMENTO AMBIENTAL - Casa Inteligente
//  Autor  : Marcos (ADS)
//  Módulo : Java - Processamento de Dados IoT
//  Diagrama: Main → ArduinoSimulator, DataProcessor, ConsolePublisher
//            ArduinoSimulator → SensorData
//            DataProcessor → ProcessedData
//            ConsolePublisher → ProcessedData
// ============================================================


// ────────────────────────────────────────────────────────────
//  CLASSE: SensorData
//  Representa os dados brutos lidos pelos sensores do Arduino.
// ────────────────────────────────────────────────────────────
class SensorData {

    private double temperature;
    private double humidity;
    private double luminosity;
    private long   timestamp;

    public SensorData(double temperature, double humidity,
                      double luminosity, long timestamp) {
        this.temperature = temperature;
        this.humidity    = humidity;
        this.luminosity  = luminosity;
        this.timestamp   = timestamp;
    }

    public double getTemperature() { return temperature; }
    public double getHumidity()    { return humidity;    }
    public double getLuminosity()  { return luminosity;  }
    public long   getTimestamp()   { return timestamp;   }
}


// ────────────────────────────────────────────────────────────
//  CLASSE: ProcessedData
//  Representa os dados processados com avaliação de status.
// ────────────────────────────────────────────────────────────
class ProcessedData {

    private double temperature;
    private double humidity;
    private double luminosity;
    private String temperatureStatus;
    private String humidityStatus;
    private String luminosityStatus;
    private long   timestamp;

    public ProcessedData(double temperature, double humidity,
                         double luminosity, String temperatureStatus,
                         String humidityStatus, String luminosityStatus,
                         long timestamp) {
        this.temperature       = temperature;
        this.humidity          = humidity;
        this.luminosity        = luminosity;
        this.temperatureStatus = temperatureStatus;
        this.humidityStatus    = humidityStatus;
        this.luminosityStatus  = luminosityStatus;
        this.timestamp         = timestamp;
    }

    // Getters
    public double getTemperature()       { return temperature;       }
    public double getHumidity()          { return humidity;          }
    public double getLuminosity()        { return luminosity;        }
    public String getTemperatureStatus() { return temperatureStatus; }
    public String getHumidityStatus()    { return humidityStatus;    }
    public String getLuminosityStatus()  { return luminosityStatus;  }
    public long   getTimestamp()         { return timestamp;         }

    /**
     * Serializa os dados processados em formato JSON simples.
     * Pode ser usado para envio a APIs ou exibição em interface.
     */
    public String toJSON() {
        return String.format(
            "{" +
            "\"temperature\": %.1f, " +
            "\"humidity\": %.1f, " +
            "\"luminosity\": %.1f, " +
            "\"temperatureStatus\": \"%s\", " +
            "\"humidityStatus\": \"%s\", " +
            "\"luminosityStatus\": \"%s\", " +
            "\"timestamp\": %d" +
            "}",
            temperature, humidity, luminosity,
            temperatureStatus, humidityStatus, luminosityStatus,
            timestamp
        );
    }
}


// ────────────────────────────────────────────────────────────
//  CLASSE: ArduinoSimulator
//  Simula a leitura dos sensores físicos do Arduino UNO.
//  Em produção, esta classe seria substituída por comunicação
//  serial real via porta COM/USB com a biblioteca jSerialComm.
// ────────────────────────────────────────────────────────────
class ArduinoSimulator {

    private static final java.util.Random rng = new java.util.Random();

    /**
     * Simula a leitura dos três sensores e retorna um SensorData.
     * Intervalos realistas:
     *   Temperatura : 10.0 a 40.0 °C
     *   Umidade     : 20.0 a 90.0 %
     *   Luminosidade: 0.0  a 1023.0 (valor analógico Arduino A0)
     */
    public SensorData readSensors() {
        double temperature = 10.0 + rng.nextDouble() * 30.0;
        double humidity    = 20.0 + rng.nextDouble() * 70.0;
        double luminosity  = rng.nextDouble() * 1023.0;
        long   timestamp   = System.currentTimeMillis();

        return new SensorData(
            Math.round(temperature * 10.0) / 10.0,
            Math.round(humidity    * 10.0) / 10.0,
            Math.round(luminosity  * 10.0) / 10.0,
            timestamp
        );
    }
}


// ────────────────────────────────────────────────────────────
//  CLASSE: DataProcessor
//  Processa os dados brutos do SensorData, avalia cada
//  grandeza e produz um ProcessedData com os status.
// ────────────────────────────────────────────────────────────
class DataProcessor {

    // Limites de temperatura (°C)
    private static final double TEMP_MAX = 30.0;
    private static final double TEMP_MIN = 15.0;

    // Limites de umidade (%)
    private static final double HUMID_MAX = 70.0;
    private static final double HUMID_MIN = 30.0;

    // Limite mínimo de luminosidade (valor analógico 0-1023)
    private static final double LUM_MIN = 200.0;

    /**
     * Processa um SensorData e retorna um ProcessedData completo.
     */
    public ProcessedData process(SensorData data) {
        String tempStatus  = evaluateTemperature(data.getTemperature());
        String humidStatus = evaluateHumidity(data.getHumidity());
        String lumStatus   = evaluateLuminosity(data.getLuminosity());

        return new ProcessedData(
            data.getTemperature(),
            data.getHumidity(),
            data.getLuminosity(),
            tempStatus,
            humidStatus,
            lumStatus,
            data.getTimestamp()
        );
    }

    /**
     * Avalia o status da temperatura.
     */
    private String evaluateTemperature(double temp) {
        if (temp > TEMP_MAX) return "HIGH";
        if (temp < TEMP_MIN) return "LOW";
        return "NORMAL";
    }

    /**
     * Avalia o status da umidade.
     */
    private String evaluateHumidity(double humidity) {
        if (humidity > HUMID_MAX) return "HIGH";
        if (humidity < HUMID_MIN) return "LOW";
        return "NORMAL";
    }

    /**
     * Avalia o status da luminosidade.
     */
    private String evaluateLuminosity(double lum) {
        if (lum < LUM_MIN) return "LOW";
        return "NORMAL";
    }
}


// ────────────────────────────────────────────────────────────
//  CLASSE: ConsolePublisher
//  Responsável por publicar os dados processados no console.
//  Em uma versão futura, esta classe poderia publicar via
//  MQTT, REST API ou diretamente na interface mobile.
// ────────────────────────────────────────────────────────────
class ConsolePublisher {

    private int contador = 0;

    /**
     * Publica os dados processados formatados no console.
     */
    public void publish(ProcessedData data) {
        contador++;
        java.time.Instant inst = java.time.Instant.ofEpochMilli(data.getTimestamp());
        java.time.LocalDateTime dt = java.time.LocalDateTime
            .ofInstant(inst, java.time.ZoneId.systemDefault());
        String hora = dt.format(
            java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss"));

        System.out.println("\n┌─ Leitura #" + contador + " ─── " + hora + " ───────────────────");
        System.out.printf( "│  Temperatura : %5.1f °C  [%s]%n",
            data.getTemperature(), data.getTemperatureStatus());
        System.out.printf( "│  Umidade     : %5.1f %%   [%s]%n",
            data.getHumidity(), data.getHumidityStatus());
        System.out.printf( "│  Luminosidade: %6.1f   [%s]%n",
            data.getLuminosity(), data.getLuminosityStatus());
        System.out.println("│");
        System.out.println("│  JSON → " + data.toJSON());
        System.out.println("└───────────────────────────────────────────────");

        // Exibe alertas quando o status não é NORMAL
        boolean alerta = false;
        if (!data.getTemperatureStatus().equals("NORMAL")) {
            System.out.println("  ⚠  ALERTA: Temperatura " +
                (data.getTemperatureStatus().equals("HIGH") ? "acima" : "abaixo") +
                " do limite!");
            alerta = true;
        }
        if (!data.getHumidityStatus().equals("NORMAL")) {
            System.out.println("  ⚠  ALERTA: Umidade " +
                (data.getHumidityStatus().equals("HIGH") ? "alta" : "baixa") + "!");
            alerta = true;
        }
        if (!data.getLuminosityStatus().equals("NORMAL")) {
            System.out.println("  ⚠  ALERTA: Luminosidade baixa!");
            alerta = true;
        }
        if (!alerta) {
            System.out.println("  ✔  Ambiente em condições normais.");
        }
    }
}


// ────────────────────────────────────────────────────────────
//  CLASSE: Main
//  Ponto de entrada do sistema. Orquestra o fluxo:
//  ArduinoSimulator → DataProcessor → ConsolePublisher
// ────────────────────────────────────────────────────────────
public class Main {

    public static void main(String[] args) throws InterruptedException {

        System.out.println("╔═══════════════════════════════════════════════╗");
        System.out.println("║   SISTEMA DE MONITORAMENTO - CASA INTELIGENTE ║");
        System.out.println("║   Módulo Java  |  Arduino UNO + DHT11 + LDR   ║");
        System.out.println("╚═══════════════════════════════════════════════╝");

        // Instancia os componentes conforme o diagrama UML
        ArduinoSimulator  simulator = new ArduinoSimulator();
        DataProcessor     processor = new DataProcessor();
        ConsolePublisher  publisher = new ConsolePublisher();

        int totalLeituras = 6;

        for (int i = 0; i < totalLeituras; i++) {

            // 1. ArduinoSimulator gera SensorData
            SensorData rawData = simulator.readSensors();

            // 2. DataProcessor processa e produz ProcessedData
            ProcessedData processed = processor.process(rawData);

            // 3. ConsolePublisher publica os dados
            publisher.publish(processed);

            Thread.sleep(1000); // Intervalo entre leituras
        }

        System.out.println("\n  Monitoramento encerrado.");
    }
}
