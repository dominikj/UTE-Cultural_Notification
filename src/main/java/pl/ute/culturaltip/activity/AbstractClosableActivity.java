package pl.ute.culturaltip.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by dominik on 10.02.18.
 */

public abstract class AbstractClosableActivity extends AppCompatActivity {

    int closeButtonId = 0;

    AbstractClosableActivity(int closeButtonId) {
        super();
        this.closeButtonId = closeButtonId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        Button closeButton = (Button) findViewById(closeButtonId);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });
        super.onResume();
    }

    protected Context getContext() {
        return this;
    }
}
