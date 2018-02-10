package pl.ute.culturaltip.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by dominik on 10.02.18.
 */

public abstract class AbstractNavigationActivity extends AbstractClosableActivity {
    private static final int REQUEST_CODE = 1;

    private int forwardButtonId = 0;
    private int backButtonId = 0;
    private Button forwardButton;
    private Button backButton;

    AbstractNavigationActivity(int forwardButtonId, int backButtonId) {
        super(backButtonId);
        this.backButtonId = backButtonId;
        this.forwardButtonId = forwardButtonId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void onResume(boolean forwardButtonIsEnabled, boolean backButtonIsEnabled) {
        super.onResume();
        forwardButton = (Button) findViewById(forwardButtonId);
        backButton = (Button) findViewById(backButtonId);
        forwardButton.setEnabled(forwardButtonIsEnabled);
        backButton.setEnabled(backButtonIsEnabled);

        forwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(createIntentForForward(), REQUEST_CODE);
            }
        });
    }

    protected abstract Intent createIntentForForward();

    protected final Button getForwardButton() {
        return forwardButton;
    }

    protected final Button getBackButton() {
        return backButton;
    }
}
