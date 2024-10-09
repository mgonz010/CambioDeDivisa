import org.json.JSONObject;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CambioDeDivisa extends JFrame {

    private JTextField amountField;
    private JComboBox<String> fromCurrency;
    private JComboBox<String> toCurrency;
    private JLabel resultLabel;

    public CambioDeDivisa() {
        setTitle("Convertidor de Divisas");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 2));
        setLocationRelativeTo(null);

        // Monedas que se obtendran de la API de ExchangeRate importantes
        String[] currencies = {"USD", "EUR", "GBP", "JPY", "AUD", "CAD", "CHF", "CNY", "INR", "MXN"};

        // Etiquetas de la ventana de dialogo
        JLabel amountLabel = new JLabel("Monto:");
        JLabel fromLabel = new JLabel("Moneda origen:");
        JLabel toLabel = new JLabel("Moneda a convertir:");
        resultLabel = new JLabel("Resultado: ");

        // Campos de entrada de informacion
        amountField = new JTextField();
        fromCurrency = new JComboBox<>(currencies);
        toCurrency = new JComboBox<>(currencies);

        // Botón de conversión
        JButton convertButton = new JButton("CONVERTIR");

        // Acción del botón
        convertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                convertCurrency();
            }
        });

        // Añadir componentes a la ventana
        add(amountLabel);
        add(amountField);
        add(fromLabel);
        add(fromCurrency);
        add(toLabel);
        add(toCurrency);
        add(convertButton);
        add(resultLabel);

        setVisible(true);
    }

    private void convertCurrency() {
        String apiKey = "4c92d3f192bea84ed49dfaa1";  // API Key propia Magg de ExchangeRate
        String baseUrl = "https://v6.exchangerate-api.com/v6/" + apiKey + "/pair/";

        try {
            double amount = Double.parseDouble(amountField.getText());
            String from = fromCurrency.getSelectedItem().toString();
            String to = toCurrency.getSelectedItem().toString();

            String urlStr = baseUrl + from + "/" + to;
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }

            in.close();
            conn.disconnect();

            // Parsear respuesta JSON
            JSONObject jsonResponse = new JSONObject(content.toString());
            double exchangeRate = jsonResponse.getDouble("conversion_rate");

            // Calcular el valor convertido
            double result = amount * exchangeRate;

            // Mostrar el resultado
            resultLabel.setText(String.format("%.2f", result) + " " + to);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CambioDeDivisa());
    }
}
