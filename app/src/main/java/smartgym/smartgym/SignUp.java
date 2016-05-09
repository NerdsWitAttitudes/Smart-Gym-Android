package smartgym.smartgym;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nwa.smartgym.R;

import java.util.ArrayList;
import java.util.List;

import smartgym.api.AuthAPI;
import smartgym.api.ServiceGenerator;
import smartgym.lib.DefaultPageAdapter;
import smartgym.lib.NonSwipeableViewPager;
import smartgym.models.HTTPResponse;
import smartgym.models.SignUpData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUp extends FragmentActivity {

    private static NonSwipeableViewPager mViewPager;
    private DefaultPageAdapter mPageAdapter;

    private static SignUpTask mSignUpTask;
    private static Context context;

    // UI references.
    private static EditText mPasswordView;
    private static EditText mPasswordConfirmView;
    private static EditText mEmailView;
    private static EditText mFirstNameView;
    private static EditText mLastNameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        List<Fragment> fragments = listFragments();
        mPageAdapter = new DefaultPageAdapter(getSupportFragmentManager(), fragments);
        SignUp.context = getApplicationContext();

        mViewPager = (NonSwipeableViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mPageAdapter);
    }

    private List<Fragment> listFragments() {
        List<Fragment> fragmentList = new ArrayList<Fragment>();
        fragmentList.add(TermsFragment.newInstance());
        fragmentList.add(EmailFragment.newInstance());
        fragmentList.add(PersonalFragment.newInstance());
        fragmentList.add(PasswordFragment.newInstance());
        return fragmentList;
    }

    @Override
    public void onBackPressed() {
        // If on the first page the back button should go back to the previous activity
        if (mViewPager.getCurrentItem() == 0) {
            finish();
        }

        mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1, true);
    }


    public static class TermsFragment extends Fragment {
        public static TermsFragment newInstance() {
            TermsFragment fragment = new TermsFragment();
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_sign_up_terms, container, false);

            Button getStartedButton = (Button) rootView.findViewById(R.id.get_started_sign_up_button);
            getStartedButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1, true);
                }
            });

            return rootView;
        }
    }

    public static class PasswordFragment extends Fragment {
        public static PasswordFragment newInstance() {
            PasswordFragment fragment = new PasswordFragment();
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_sign_up_password, container, false);

            // Define views
            mPasswordView = (EditText) rootView.findViewById(R.id.password_sign_up_prompt);
            mPasswordConfirmView = (EditText) rootView.findViewById(R.id.password_confirm_sign_up_prompt);

            Button getStartedButton = (Button) rootView.findViewById(R.id.sign_up_button);
            getStartedButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (validatePasswordData()) {
                        mSignUpTask = new SignUpTask();
                        mSignUpTask.doInBackground();
                    }
                }
            });

            return rootView;
        }

        private Boolean validatePasswordData() {
            String password = mPasswordView.getText().toString();
            String passwordConfirm = mPasswordConfirmView.getText().toString();

            if (password == passwordConfirm) {
                mPasswordConfirmView.setError(getString(R.string.error_passwords_do_not_match));
            } else if (password.isEmpty()) {
                mPasswordView.setError(getString(R.string.error_pasword_required));
            } else if (password.contains(" ")) {
                mPasswordView.setError(getString(R.string.error_password_contains_spaces));
            } else if (!password.matches(".*[\\p{L}]+.*]")) {
                // Regex checking if the password contains at least one letter.
                mPasswordView.setError(getString(R.string.error_password_no_letters));
            } else if (!password.matches(".*\\d+.*")) {
                // Regex checking if the password contains at least one number.
                mPasswordView.setError(getString(R.string.error_password_no_numbers));
            } else if (password.length() < 8) {
                mPasswordView.setError(getString(R.string.error_password_too_short));
            } else {
                return true;
            }

            return false;
        }
    }

    public static class PersonalFragment extends Fragment {
        public static PersonalFragment newInstance() {
            PersonalFragment fragment = new PersonalFragment();
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_sign_up_personal, container, false);

            // Define views
            mFirstNameView = (EditText) rootView.findViewById(R.id.first_name_sign_up_prompt);
            mLastNameView = (EditText) rootView.findViewById(R.id.last_name_sign_up_prompt);

            Button getStartedButton = (Button) rootView.findViewById(R.id.next_personal_sign_up_button);
            getStartedButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (validatePersonalData()) {
                        mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1, true);
                    }
                }
            });

            return rootView;
        }

        private Boolean validatePersonalData() {
            String firstName = mFirstNameView.getText().toString();
            String lastName = mLastNameView.getText().toString();

            if (firstName.isEmpty()) {
                mFirstNameView.setError(getString(R.string.error_first_name_required));
            } else if(lastName.isEmpty()) {
                mLastNameView.setError(getString(R.string.error_last_name_required));
            } else {
                return true;
            }

            return false;
        }
    }

    public static class EmailFragment extends Fragment {
        public static EmailFragment newInstance() {
            EmailFragment fragment = new EmailFragment();
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_sign_up_email, container, false);

            // Define views
            mEmailView = (EditText) rootView.findViewById(R.id.email_sign_up_prompt);

            Button getStartedButton = (Button) rootView.findViewById(R.id.next_email_sign_up_button);
            getStartedButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (validateEmailData()) {
                        mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1, true);
                    }
                }
            });

            return rootView;
        }

        private Boolean validateEmailData() {
            String email = mEmailView.getText().toString();

            if (email.isEmpty()) {
                mEmailView.setError(getString(R.string.error_email_required));
            } else if (!email.contains("@")) {
                mEmailView.setError(getString(R.string.error_invalid_email));
            } else {
                return true;
            }

            // This is only reached if the field is invalid
            return false;
        }
    }

    public static class SignUpTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            SignUpData signUpData = gatherSignUpData();
            AuthAPI signUpService = ServiceGenerator.createSmartGymService(AuthAPI.class);
            Call<HTTPResponse> call = signUpService.signUp(signUpData);

            call.enqueue(new Callback<HTTPResponse>() {

                @Override
                public void onResponse(Call<HTTPResponse> call, Response<HTTPResponse> response) {
                    System.out.println(response.code());
                    if (response.code() == 200) {
                        return;
                    } else {
                        System.out.println(response.code());
                        raiseGenericError();
                    }
                }

                @Override
                public void onFailure(Call<HTTPResponse> call, Throwable t) {
                    raiseGenericError();
                }

                private void raiseGenericError() {

                    Toast toast = Toast.makeText(context,
                            context.getResources().getString(R.string.server_500_message),
                            Toast.LENGTH_SHORT);
                    toast.show();
                }
            });
            return true;
        }

        private SignUpData gatherSignUpData() {
            mPasswordView.getText();

            String password = mPasswordView.getText().toString();
            String passwordConfirm = mPasswordConfirmView.getText().toString();
            String email = mEmailView.getText().toString();
            String firstName = mFirstNameView.getText().toString();
            String lastName = mLastNameView.getText().toString();

            return new SignUpData(password, passwordConfirm, email, firstName, lastName);
        }
    }
}
