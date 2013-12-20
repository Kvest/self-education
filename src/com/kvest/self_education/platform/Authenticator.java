package com.kvest.self_education.platform;

import android.accounts.*;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.kvest.self_education.ui.activity.AuthenticatorActivity;

/**
 * Created with IntelliJ IDEA.
 * User: roman
 * Date: 12/17/13
 * Time: 3:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class Authenticator extends AbstractAccountAuthenticator {
    public static final String GENDER_KEY = "gender";
    private Context context;

    // Simple constructor
    public Authenticator(Context context) {
        super(context);

        this.context = context;
    }

    @Override
    public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
        Log.d("KVEST_TAG", "editProperties[" + accountType + "]");
        throw new UnsupportedOperationException();
    }

    @Override
    public Bundle addAccount(AccountAuthenticatorResponse response, String accountType, String authTokenType, String[] requiredFeatures, Bundle options) throws NetworkErrorException {
        Log.d("KVEST_TAG", "editProperties[" + accountType + "]");

        final Intent intent = new Intent(context, AuthenticatorActivity.class);
        intent.putExtra(AuthenticatorActivity.PARAM_AUTHTOKEN_TYPE, authTokenType);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
        final Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);
        return bundle;
    }

    @Override
    public Bundle confirmCredentials(AccountAuthenticatorResponse response, Account account, Bundle options) throws NetworkErrorException {
        Log.d("KVEST_TAG", "confirmCredentials");

        return null;
    }

    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        if (options != null) {
            for (String key : options.keySet()) {
                Log.d("KVEST_TAG", key);
            }
        }
        Log.d("KVEST_TAG", "getAuthToken[" + authTokenType + "] " + (options != null ? options.toString() : ""));

        if (!authTokenType.equals(AuthenticatorActivity.PARAM_AUTHTOKEN_TYPE)) {
            final Bundle result = new Bundle();
            result.putString(AccountManager.KEY_ERROR_MESSAGE, "invalid authTokenType");
            return result;
        }

        final AccountManager am = AccountManager.get(context);
        final String password = am.getPassword(account);

        if (password != null) {
            boolean verified = false;
            String token = "";

            try {
                Thread.sleep(3000);
                token = "this_is_token";
                verified = true;
            } catch (InterruptedException ie) {}


            if (verified) {
                final Bundle result = new Bundle();
                result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
                result.putString(AccountManager.KEY_ACCOUNT_TYPE, AuthenticatorActivity.PARAM_ACCOUNT_TYPE);
                result.putString(AccountManager.KEY_AUTHTOKEN, token);

                return result;
            }
        }

        // Password is missing or incorrect
        final Intent intent = new Intent(context, AuthenticatorActivity.class);
        intent.putExtra(AuthenticatorActivity.PARAM_USER, account.name);
        intent.putExtra(AuthenticatorActivity.PARAM_AUTHTOKEN_TYPE, authTokenType);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
        final Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);
        return bundle;
    }

    @Override
    public String getAuthTokenLabel(String authTokenType) {
        Log.d("KVEST_TAG", "getAuthTokenLabel[" + authTokenType + "]");
        throw new UnsupportedOperationException();
    }

    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        Log.d("KVEST_TAG", "updateCredentials[" + authTokenType + "]");
        throw new UnsupportedOperationException();
    }

    @Override
    public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account, String[] features) throws NetworkErrorException {
        Log.d("KVEST_TAG", "hasFeatures");
        throw new UnsupportedOperationException();
    }
}
