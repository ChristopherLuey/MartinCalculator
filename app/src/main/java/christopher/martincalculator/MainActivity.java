package christopher.martincalculator;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Stack;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    Button equals, clear;
    TextView output;
    Stack<Integer> builder;
    Boolean[] operation;
    Integer first, second;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize stack to store numbers
        builder = new Stack<>();

        // Initialize array to store active operations\
        operation = new Boolean[]{false, false, false, false};

        // Store Buttons in array
        Button[] buttons = new Button[]{
                findViewById(R.id.zero),
                findViewById(R.id.one),
                findViewById(R.id.two),
                findViewById(R.id.three),
                findViewById(R.id.four),
                findViewById(R.id.five),
                findViewById(R.id.six),
                findViewById(R.id.seven),
                findViewById(R.id.eight),
                findViewById(R.id.nine)
        };

        output = findViewById(R.id.output);
        equals = findViewById(R.id.equals);
        clear = findViewById(R.id.clear);

        output.setText("");

        // Store operator Buttons in array
        Button[] operators = new Button[]{
                findViewById(R.id.plus),
                findViewById(R.id.minus),
                findViewById(R.id.multiply),
                findViewById(R.id.divide)
        };

        // Handle all operators
        for (int i = 0; i < 4; i++) {
            String[] operatorStrings = new String[]{"+", "-", "x", "/"};
            int finalI = i;
            
            operators[i].setOnClickListener(v -> {
                if (!builder.isEmpty() && !Arrays.asList(operation).contains(true)) {
                    operation[finalI] = true;
                    first = compute(builder);
                    builder.clear();
                    output.setText(output.getText().toString().concat(operatorStrings[finalI]));
                }

                if (Arrays.asList(operation).contains(true)) {
                    operation = new Boolean[]{false, false, false, false};
                    operation[finalI] = true;
                    for (String s1 : operatorStrings) {
                        output.setText(output.getText().toString().replace(s1, operatorStrings[finalI]));
                    }
                }

            });
        }

        // When 0-9 pressed
        for (int i = 0; i <= 9; i++) {
            int finalI = i;
            buttons[i].setOnClickListener(view -> {
                if (!(builder.isEmpty() && finalI == 0)) {
                    builder.push(finalI);
                    output.setText(output.getText().toString().concat(String.valueOf(finalI)));
                }
            });
        }

        // When clear button clicked
        clear.setOnClickListener(v -> {
            if (builder.isEmpty()) {
                operation = new Boolean[]{false, false, false, false};
                output.setText("");

            } else {
                builder.clear();
                if (Arrays.asList(operation).contains(true)) {
                    output.setText(first.toString() + new String[]{"+", "-", "x", "/"}[Arrays.asList(operation).indexOf(true)]);
                } else {
                    output.setText("");
                }
            }
        });

        // When equals button clicked
        equals.setOnClickListener(v -> {
            if (!builder.isEmpty() && Arrays.asList(operation).contains(true)) {
                second = compute(builder);
                int value = 0;
                switch (Arrays.asList(operation).indexOf(true)) {
                    case 0:
                        value = second + first;
                        break;
                    case 1:
                        value = first - second;
                        break;
                    case 2:
                        value = first * second;
                        break;
                    case 3:
                        value = first / second;
                        break;
                }

                // Convert result back into Stack
                builder.clear();
                String number = String.valueOf(value);
                for (int i = 0; i < number.length(); i++) {
                    builder.push(Character.digit(number.charAt(i), 10));
                }

                // Reset variables
                output.setText(String.valueOf(value));
                second = null;
                first = null;
                operation = new Boolean[]{false, false, false, false};
            }
        });
    }

    // Helper function to turn stack into integer
    public int compute(Stack<Integer> s) {
        int n = 0, power = 1;
        while (!s.isEmpty()) {
            n += (s.pop() * power);
            power *= 10;
        }
        return n;
    }
}