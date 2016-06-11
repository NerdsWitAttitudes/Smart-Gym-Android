package com.nwa.smartgym.activities;


import static org.mockito.Mockito.*;
import static org.junit.Assert.*;


import android.content.Context;
import android.text.Editable;
import android.widget.EditText;

import com.nwa.smartgym.R;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Created by robin on 11-5-16.
 */
@RunWith(MockitoJUnitRunner.class)
public class SignUpTest {
    private static final String FAKE_EMAIL = "test@email.com";
    private static final String FAKE_PASSWORD = "testing123";

    @Test
    public void validateEmailAddress() {
        final SignUp.EmailFragment emailFragment = spy(new SignUp.EmailFragment());
        final SignUp signUp = spy(new SignUp());

        final EditText emailView = mock(EditText.class);
        doReturn(emailView).when(signUp).findViewById(R.id.email_sign_up_prompt);
        when(emailView.getText()).thenReturn(Editable.Factory.getInstance().newEditable(FAKE_EMAIL));


        assertTrue(emailFragment.validateEmailData());

        verify(emailFragment).getActivity().findViewById(R.id.email_sign_up_prompt);
        verify(emailView).getText();
    }
}

