package com.example.go4lunch;

import com.example.go4lunch.Model.Users.User;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

public class UserTest {

    @Test
    public void GetUserTest() {
        User user = new User("testUid","testUsername",null);

        assertEquals("testUid", user.getUid());
        assertEquals("testUsername",user.getUsername());
        assertNull(user.getUrlPicture());

    }

    @Test
    public void setUserTest() {

        User user = new User();

        user.setUid("1111");
        user.setUsername("test_username");
        user.setUrlPicture("url_picture");


        assertEquals("1111", user.getUid());
        assertEquals("test_username", user.getUsername());
        assertEquals("url_picture", user.getUrlPicture());

    }
}
