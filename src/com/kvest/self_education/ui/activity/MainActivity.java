package com.kvest.self_education.ui.activity;

import android.accounts.*;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.kvest.self_education.R;
import com.kvest.self_education.platform.Authenticator;

import java.io.IOException;

public class MainActivity extends Activity {
    private static final int NEW_ACCOUNT = 1;
    private static final int EXISTING_ACCOUNT = 2;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        findViewById(R.id.get_account).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printAccounts();
            }
        });
        findViewById(R.id.delete_password).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePassword();
            }
        });
        findViewById(R.id.delete_token).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteToken();
            }
        });
        findViewById(R.id.get_token).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getToken();
            }
        });
    }

    private void printAccounts() {
        AccountManager accountManager = AccountManager.get(this);
        Account[] accounts = accountManager.getAccountsByType(AuthenticatorActivity.PARAM_ACCOUNT_TYPE);

        if (accounts.length == 0) {
            final Intent i = new Intent(this, AuthenticatorActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            startActivityForResult(i, NEW_ACCOUNT);
        } else {
            for (Account account : accounts) {
                Log.d("KVEST_TAG", "akk=[" + account.toString() + "];" + accountManager.getPassword(account));
                Log.d("KVEST_TAG", "user data=" + accountManager.getUserData(account, Authenticator.GENDER_KEY));
            }

            String password = accountManager.getPassword(accounts[0]);
            if (password == null) {
                final Intent i = new Intent(this, AuthenticatorActivity.class);
                i.putExtra(AuthenticatorActivity.PARAM_USER, accounts[0].name);
                startActivityForResult(i, EXISTING_ACCOUNT);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == NEW_ACCOUNT) {
            if (resultCode == Activity.RESULT_OK) {
                Log.d("KVEST_TAG", "RESULT_OK");
                printAccounts();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.d("KVEST_TAG", "RESULT_CANCELED");
            } else {
                Log.d("KVEST_TAG", "UNKNOWN RESULT");
            }
        }
    }

    private void deleteToken() {
        AccountManager.get(this).invalidateAuthToken(AuthenticatorActivity.PARAM_ACCOUNT_TYPE, "this_is_token");
    }

    private void deletePassword() {
        AccountManager accountManager = AccountManager.get(this);
        Account[] accounts = accountManager.getAccountsByType(AuthenticatorActivity.PARAM_ACCOUNT_TYPE);
        if (accounts.length > 0) {
            AccountManager.get(this).setPassword(accounts[0], null);
        }
    }

    private void getToken() {
        AccountManager accountManager = AccountManager.get(this);
        Account[] accounts = accountManager.getAccountsByType(AuthenticatorActivity.PARAM_ACCOUNT_TYPE);

        if (accounts.length > 0) {
            AccountManagerFuture<Bundle> result = accountManager.getAuthToken(accounts[0], AuthenticatorActivity.PARAM_AUTHTOKEN_TYPE, null, this,  new AccountManagerCallback<Bundle>() {
                @Override
                public void run(AccountManagerFuture<Bundle> future) {
                    try {
                        Bundle result = future.getResult();
                        Log.d("KVEST_TAG", "result_of_login=" + result.toString());
                    } catch (IOException ioEx) {
                        Log.d("KVEST_TAG", "ioEx=" + ioEx.getMessage());
                    } catch (OperationCanceledException oce) {
                        Log.d("KVEST_TAG", "oce=" + oce.getMessage());
                    } catch (AuthenticatorException ae) {
                        Log.d("KVEST_TAG", "ae=" + ae.getMessage());
                    }
                }
            }, null);
        }
    }
}
