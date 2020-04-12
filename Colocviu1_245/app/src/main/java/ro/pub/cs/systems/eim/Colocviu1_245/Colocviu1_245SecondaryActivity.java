package ro.pub.cs.systems.eim.Colocviu1_245;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import ro.pub.cs.systems.eim.Colocviu1_245.general.Constants;

public class Colocviu1_245SecondaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent != null && intent.getExtras().containsKey(Constants.ALL_TERMS)) {
            String allTerms = intent.getStringExtra(Constants.ALL_TERMS);
            int sum = 0;

            for (int index = 0; index < allTerms.length(); index++) {
                if(allTerms.charAt(index) != '+') {
                    int num = Character.getNumericValue(allTerms.charAt(index));
                    sum += num;
                }
            }

            setResult(sum, null);
            finish();
        }
    }
}
