package pl.ute.culturaltip.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.dominik.ute.R;

/**
 * Created by dominik on 10.02.18.
 */

public abstract class AbstractActivity extends Activity {

    int closeButtonId = 0;

    AbstractActivity(int closeButtonId) {
        super();
        this.closeButtonId = closeButtonId;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Button closeButton = (Button) findViewById(closeButtonId);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });
    }
}
