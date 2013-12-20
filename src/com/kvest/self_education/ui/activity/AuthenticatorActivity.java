package com.kvest.self_education.ui.activity;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import com.kvest.self_education.R;
import com.kvest.self_education.platform.Authenticator;

/**
 * Created with IntelliJ IDEA.
 * User: roman
 * Date: 12/18/13
 * Time: 10:27 AM
 * To change this template use File | Settings | File Templates.
 */
public class AuthenticatorActivity extends AccountAuthenticatorActivity {
    public static final String PARAM_AUTHTOKEN_TYPE = "authtokenType";
    public static final String PARAM_ACCOUNT_TYPE = "com.kvest.self_education";
    public static final String PARAM_USER = "user";
    public static final String PARAM_USER_DATA = "user_data";

    private EditText email;
    private EditText password;
    private RadioGroup gender;

    private boolean requestNewAccount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authenticator_layout);

        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        gender = (RadioGroup)findViewById(R.id.gender);

        findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        String userName = getIntent().getStringExtra(PARAM_USER);
        email.setText(userName);

        requestNewAccount = (userName == null);
    }

    private void login() {
        if (!isUserInputValid()) {
            return;
        }

        findViewById(R.id.login).setEnabled(false);
        findViewById(R.id.close).setEnabled(false);
        final Context context = this;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ie){}

                finishLogin(context);
                setResult(RESULT_OK);
                finish();
            }
        }).start();
    }

    private void finishLogin(Context context) {
        Account account = new Account(email.getText().toString(), PARAM_ACCOUNT_TYPE);
        if (requestNewAccount) {
            final Bundle userData = new Bundle();
            userData.putInt(Authenticator.GENDER_KEY, gender.getCheckedRadioButtonId());
            AccountManager.get(context).addAccountExplicitly(account, password.getText().toString(), userData);
        } else {
            AccountManager.get(context).setPassword(account, password.getText().toString());
        }
    }

    private boolean isUserInputValid() {
        boolean result = true;

        if (TextUtils.isEmpty(email.getText())) {
            email.setError("Empty field");
            result = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText()).matches()) {
            email.setError("Not valid email");
            result = false;
        }

        if (TextUtils.isEmpty(password.getText())) {
            password.setError("Empty field");
            result = false;
        }

        return result;
    }
}
