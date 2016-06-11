package com.nwa.smartgym.activities;


import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Created by robin on 11-5-16.
 */
@RunWith(MockitoJUnitRunner.class)
public class SignUpTest{
    private static final String FAKE_EMAIL = "test@email.com";

    @Test
    public void validateEmailAddress() {
        final SignUp.EmailFragment emailFragment = spy(new SignUp.EmailFragment());

        assertTrue(emailFragment.validateEmailData(FAKE_EMAIL));
    }
}

