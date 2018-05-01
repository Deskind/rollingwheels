package deskind.com.rollingwheels.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import deskind.com.rollingwheels.R;
import deskind.com.rollingwheels.activities.MnActivity;

public class CurrencyTokenFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.set_currency_token, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        final EditText userText = view.findViewById(R.id.tv_new_token);
        Button tokenDone = view.findViewById(R.id.btn_token_done);
        tokenDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //write new token to shared preferences file "tokens" key is "currency_token"
                MnActivity activity = (MnActivity)getActivity();
                activity.setCurrencyToken(userText.getText().toString());

                //write new token to shared preferences file "tokens" key is "currency_token"
                SharedPreferences tokens = activity.getSharedPreferences("tokens", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = tokens.edit().putString("currency_token", userText.getText().toString());
                editor.commit();

                //Message
                Toast.makeText(getContext(), "Done!", Toast.LENGTH_SHORT).show();

                getFragmentManager().popBackStackImmediate();

            }
        });
    }
}
