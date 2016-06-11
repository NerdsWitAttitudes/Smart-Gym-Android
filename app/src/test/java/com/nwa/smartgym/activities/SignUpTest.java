package com.nwa.smartgym.activities;


import static org.mockito.Mockito.*;
import static org.junit.Assert.*;


import android.content.Context;
import android.test.AndroidTestCase;
import android.text.Editable;
import android.text.SpannableStringBuilder;
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
public class SignUpTest{
    private static final String FAKE_EMAIL = "test@email.com";
    private static final String FAKE_PASSWORD = "testing123";

    @Test
    public void validateEmailAddress() {
        final SignUp.EmailFragment emailFragment = spy(new SignUp.EmailFragment());

        assertTrue(emailFragment.validateEmailData(FAKE_EMAIL));
    }
}

