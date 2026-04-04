/*
 * ============================================================
 *  SISTEMA DE MONITORAMENTO AMBIENTAL - Casa Inteligente
 *  Autor  : Marcos (ADS)
 *  Placa  : Arduino UNO
 *  Simulador: Tinkercad Circuits
 *
 *  CORRESPONDÊNCIA COM O DIAGRAMA UML:
 *    Esta placa representa o ArduinoSimulator do módulo Java.
 *    Ela gera os dados equivalentes ao SensorData:
 *      - temperature  → DHT11 (pino D2)
 *      - humidity     → DHT11 (pino D2)
 *      - luminosity   → LDR   (pino A0)
 *      - timestamp    → millis() do Arduino
 *
 *  SAÍDA SERIAL (9600 baud) → lida pelo DataProcessor Java:
 *    Formato: TEMP:XX.X,HUMID:XX.X,LUM:XXXX,TS:XXXXXXXX
 * ============================================================
 */

#include <DHT.h>

// ── Pinos ────────────────────────────────────────────────────
#define PINO_DHT    2        // Pino digital – DHT11
#define TIPO_DHT    DHT11    // Modelo do sensor
#define PINO_LDR    A0       // Pino analógico – LDR

// ── Limites (espelham os do DataProcessor.java) ──────────────
#define TEMP_MAX    30.0     // °C – acima → status "HIGH"
#define TEMP_MIN    15.0     // °C – abaixo → status "LOW"
#define HUMID_MAX   70.0     // %  – acima → status "HIGH"
#define HUMID_MIN   30.0     // %  – abaixo → status "LOW"
#define LUM_MIN     200      // 0-1023 – abaixo → status "LOW"

// ── Intervalo de leitura ─────────────────────────────────────
#define INTERVALO_MS  2000

// ── Objeto sensor DHT11 ──────────────────────────────────────
DHT dht(PINO_DHT, TIPO_DHT);

// ────────────────────────────────────────────────────────────
void setup() {
    Serial.begin(9600);
    dht.begin();
    delay(1000);

    // Cabeçalho informativo (descartado pelo parser Java)
    Serial.println("# ArduinoSimulator - Casa Inteligente");
    Serial.println("# SensorData: TEMP,HUMID,LUM,TS");
}

// ────────────────────────────────────────────────────────────
void loop() {

    // ── Leitura dos sensores (SensorData) ───────────────────
    float temperature = dht.readTemperature();   // °C
    float humidity    = dht.readHumidity();      // %
    int   luminosity  = analogRead(PINO_LDR);    // 0-1023
    unsigned long ts  = millis();                // timestamp

    // ── Validação da leitura ─────────────────────────────────
    if (isnan(temperature) || isnan(humidity)) {
        Serial.println("ERROR:DHT_READ_FAILED");
        delay(INTERVALO_MS);
        return;
    }

    // ── Avaliação local de status (igual ao DataProcessor) ───
    String tempStatus  = evaluateTemperature(temperature);
    String humidStatus = evaluateHumidity(humidity);
    String lumStatus   = evaluateLuminosity(luminosity);

    // ── Envio Serial (formato SensorData CSV) ────────────────
    Serial.print("TEMP:");
    Serial.print(temperature, 1);
    Serial.print(",HUMID:");
    Serial.print(humidity, 1);
    Serial.print(",LUM:");
    Serial.print(luminosity);
    Serial.print(",TS:");
    Serial.print(ts);
    Serial.print(",TEMP_STATUS:");
    Serial.print(tempStatus);
    Serial.print(",HUMID_STATUS:");
    Serial.print(humidStatus);
    Serial.print(",LUM_STATUS:");
    Serial.println(lumStatus);

    delay(INTERVALO_MS);
}

// ── evaluateTemperature → espelha DataProcessor.java ────────
String evaluateTemperature(float temp) {
    if (temp > TEMP_MAX) return "HIGH";
    if (temp < TEMP_MIN) return "LOW";
    return "NORMAL";
}

// ── evaluateHumidity → espelha DataProcessor.java ───────────
String evaluateHumidity(float humidity) {
    if (humidity > HUMID_MAX) return "HIGH";
    if (humidity < HUMID_MIN) return "LOW";
    return "NORMAL";
}

// ── evaluateLuminosity → espelha DataProcessor.java ─────────
String evaluateLuminosity(int lum) {
    if (lum < LUM_MIN) return "LOW";
    return "NORMAL";
}
