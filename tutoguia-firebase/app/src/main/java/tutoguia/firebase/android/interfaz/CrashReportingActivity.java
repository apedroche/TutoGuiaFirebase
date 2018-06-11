package tutoguia.firebase.android.interfaz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.crash.FirebaseCrash;

import tutoguia.firebase.android.R;

public class CrashReportingActivity extends AppCompatActivity {

    private int firstNumber = 0, secondNumber = 0;
    private String operation = null;

    // Elementos visuales de la pantalla
    private TextView resultText, exceptionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crash_reporting);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        resultText = (TextView) findViewById(R.id.resultText);
        exceptionText = (TextView) findViewById(R.id.exceptionText);
    }

    /**
     * Reinicia la calculadora
     *
     * @param view Botón sobre el que se ha pulsado
     */
    public void clear(View view){
        firstNumber = 0;
        secondNumber = 0;
        operation = null;
        resultText.setText("0");
    }

    /**
     * Añade un dígito. Cada botón numérico tiene un tag asociado al número a añadir.
     *
     * @param view Botón sobre el que se ha pulsado
     */
    public void addDigit(View view){
        if(operation==null){
            firstNumber = firstNumber * 10 + Integer.parseInt(((Button)view).getTag().toString());
            resultText.setText(String.valueOf(firstNumber));
        }
        else{
            secondNumber = secondNumber * 10 + Integer.parseInt(((Button)view).getTag().toString());
            resultText.setText(String.valueOf(secondNumber));
        }
    }

    /**
     * Añade una operación a realizar
     *
     * @param view Botón sobre el que se ha pulsado
     */
    public void addOperation(View view){
        if(operation != null){
            checkResult(null);
        }
        operation = ((Button)view).getTag().toString();
    }

    /**
     * Realiza la operación completa y muestra el resultado.
     * Si se ha pulsado el botón = antes de asignar una operación, salta una excepción leve.
     * Si se intenta hacer una división entre 0, salta una excepción grave.
     *
     * @param view Botón sobre el que se ha pulsado
     */
    public void checkResult(View view){
        if(operation==null){
            exceptionText.setText(exceptionText.getText().toString() + "No se ha seleccionado ninguna operación\n");
            FirebaseCrash.report(new Exception("No se ha seleccionado ninguna operación"));
            return;
        }
        switch (operation){
            case "/":
                firstNumber = firstNumber / secondNumber;
                break;
            case "X":
                firstNumber = firstNumber * secondNumber;
                break;
            case "-":
                firstNumber = firstNumber - secondNumber;
                break;
            case "+":
                firstNumber = firstNumber + secondNumber;
                break;
            default:
        }
        operation = null;
        secondNumber = 0;
        resultText.setText(String.valueOf(firstNumber));
    }
}
