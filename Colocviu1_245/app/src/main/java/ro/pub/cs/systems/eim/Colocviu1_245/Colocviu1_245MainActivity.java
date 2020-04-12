package ro.pub.cs.systems.eim.Colocviu1_245;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ro.pub.cs.systems.eim.Colocviu1_245.general.Constants;
import ro.pub.cs.systems.eim.Colocviu1_245.service.Colocviu1_245Service;

public class Colocviu1_245MainActivity extends AppCompatActivity {

    private EditText nextTerm;
    private TextView allTerms;
    private Button addButton;
    private Button computeButton;

    private boolean modified = false;
    private int sum;
    private Integer serviceStatus = Constants.SERVICE_STOPPED;
    private IntentFilter intentFilter = new IntentFilter();

    private class ButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.addButton:
                    modified = true;
                    String num = nextTerm.getText().toString();
                    String all = allTerms.getText().toString();

                    if (!num.isEmpty()) {
                        if (all.isEmpty()) {
                            allTerms.setText(num);
                        } else {
                            allTerms.setText(allTerms.getText() + "+" + num);
                        }
                    }
                    break;
                case R.id.computeButton:
                    modified = false;
                    Intent intent = new Intent(getApplicationContext(), Colocviu1_245SecondaryActivity.class);
                    String allTermsText = allTerms.getText().toString();
                    intent.putExtra(Constants.ALL_TERMS, allTermsText);
                    startActivityForResult(intent, Constants.SECONDARY_ACTIVITY_REQUEST_CODE);
                    break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == Constants.SECONDARY_ACTIVITY_REQUEST_CODE) {
            sum = resultCode;
            Toast.makeText(this, "The activity returned with result " + resultCode, Toast.LENGTH_LONG).show();
            if (sum > Constants.SUM_THRESHOLD && serviceStatus != Constants.SERVICE_STARTED) {
                Intent intentService = new Intent(getApplicationContext(), Colocviu1_245Service.class);
                intentService.putExtra(Constants.SUM, sum);
                getApplicationContext().startService(intentService);
                serviceStatus = Constants.SERVICE_STARTED;
            }
        }
    }

    private MessageBroadcastReceiver messageBroadcastReceiver = new MessageBroadcastReceiver();
    private class MessageBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra(Constants.BROADCAST_RECEIVER_EXTRA);
            Toast.makeText(Colocviu1_245MainActivity.this, "Received message: " + message, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test01_245_main);

        nextTerm = findViewById(R.id.nextTerm);
        allTerms = findViewById(R.id.allTerms);
        addButton = findViewById(R.id.addButton);
        computeButton = findViewById(R.id.computeButton);

        addButton.setOnClickListener(new ButtonClickListener());
        computeButton.setOnClickListener(new ButtonClickListener());

        intentFilter.addAction(Constants.ACTION_BROADCAST);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(messageBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        unregisterReceiver(messageBroadcastReceiver);
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt(Constants.SUM, sum);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (!modified) {
            if (savedInstanceState.containsKey(Constants.SUM)) {
                Integer s = sum;
                allTerms.setText(s.toString());
            } else {
                allTerms.setText("");
            }
        }
    }

    @Override
    protected void onDestroy() {
        Intent intent = new Intent(this, Colocviu1_245Service.class);
        serviceStatus = Constants.SERVICE_STOPPED;
        stopService(intent);
        super.onDestroy();
    }
}
