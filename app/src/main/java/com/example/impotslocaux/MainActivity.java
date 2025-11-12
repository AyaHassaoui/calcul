package com.example.impotslocaux;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    private EditText nomInput, adresseInput; // <-- NEW
    private EditText surfaceInput, piecesInput;
    private CheckBox piscineCheckbox;
    private TextView resultView;
    private final DecimalFormat df = new DecimalFormat("#0.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // NEW
        nomInput = findViewById(R.id.input_nom);
        adresseInput = findViewById(R.id.input_adresse);

        surfaceInput = findViewById(R.id.input_surface);
        piecesInput = findViewById(R.id.input_pieces);
        piscineCheckbox = findViewById(R.id.checkbox_piscine);
        resultView = findViewById(R.id.result);

        Button calc = findViewById(R.id.button_calcul);
        calc.setOnClickListener(v -> calculer());
    }

    private void calculer() {
        // 1) Validation basique
        String nom = nomInput.getText().toString().trim();           // NEW
        String adresse = adresseInput.getText().toString().trim();   // NEW
        String sSurface = normalize(surfaceInput.getText().toString());
        String sPieces  = piecesInput.getText().toString().trim();

        if (TextUtils.isEmpty(nom)) {
            nomInput.setError(getString(R.string.err_nom));
            nomInput.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(adresse)) {
            adresseInput.setError(getString(R.string.err_adresse));
            adresseInput.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(sSurface)) {
            surfaceInput.setError(getString(R.string.err_surface));
            surfaceInput.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(sPieces)) {
            piecesInput.setError(getString(R.string.err_pieces));
            piecesInput.requestFocus();
            return;
        }

        double surface;
        int pieces;
        try {
            surface = Double.parseDouble(sSurface);
            pieces = Integer.parseInt(sPieces);
        } catch (NumberFormatException e) {
            Toast.makeText(this, R.string.err_format, Toast.LENGTH_SHORT).show();
            return;
        }
        if (surface < 0 || pieces < 0) {
            Toast.makeText(this, R.string.err_negatifs, Toast.LENGTH_SHORT).show();
            return;
        }

        // 2) Calculs
        boolean piscine = piscineCheckbox.isChecked();
        double impotBase = surface * 2;
        double supplement = pieces * 50 + (piscine ? 100 : 0);
        double total = impotBase + supplement;

        // 3) Affichage (entête + détails)
        String header = getString(R.string.res_header, nom, adresse);
        String details = getString(R.string.res_format,
                df.format(impotBase),
                df.format(supplement),
                df.format(total)
        );
        resultView.setText(header + "\n\n" + details);
    }

    // Accepte "120,5" ou "120.5"
    private String normalize(String s) {
        return s.trim().replace(',', '.');
    }
}
