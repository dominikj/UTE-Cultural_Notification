package pl.ute.culturaltip.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.dominik.ute.R;

import static pl.ute.culturaltip.constants.Constants.Friend.FRIEND_NAME;
import static pl.ute.culturaltip.constants.Constants.Friend.FRIEND_PHONE;

/**
 * Created by dominik on 09.02.18.
 */

public class AddFriendAcitivity extends AbstractActivity {

    public AddFriendAcitivity() {
        super(R.id.close_add_btn);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_add_friend);
        super.onCreate(savedInstanceState);

        Button saveNewFriend = (Button) findViewById(R.id.save_add_btn);

        saveNewFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent friendData = new Intent();
                EditText friendName = (EditText) findViewById(R.id.name_text_field);
                EditText friendPhone = (EditText) findViewById(R.id.phone_text_field);

                if (validateFriendData(friendName, friendPhone)) {
                    return;
                }

                friendData.putExtra(FRIEND_NAME, friendName.getText().toString().trim());
                friendData.putExtra(FRIEND_PHONE, friendPhone.getText().toString().trim());
                setResult(Activity.RESULT_OK, friendData);
                finish();
            }
        });
    }

    boolean validateFriendData(EditText friendName, EditText friendPhone) {
        boolean hasErrors = false;
        String textError = getString(R.string.empty_field_error);

        if (friendName.getText() == null || friendName.getText().toString().trim().isEmpty()) {
            friendName.setError(textError);
            hasErrors = true;
        }
        if (friendPhone.getText() == null || friendPhone.getText().toString().trim().isEmpty()) {
            friendPhone.setError(textError);
            hasErrors = true;
        }
        return hasErrors;
    }
}
