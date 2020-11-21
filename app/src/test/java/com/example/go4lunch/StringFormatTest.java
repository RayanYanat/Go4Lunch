package com.example.go4lunch;

import com.example.go4lunch.utils.StringFormat;

import org.junit.Assert;
import org.junit.Test;



public class StringFormatTest {

    @Test
    public void checkStringFormat() {

        String expectedFormat ="string format test";

        String str1 = "string ";
        String str2 = "format ";
        String str3 = "test";

        String actualFormat = StringFormat.getFormattedString(str1,str2,str3);

        Assert.assertEquals(expectedFormat, actualFormat);

    }

    @Test
    public void checkStringFormat2() {

        String expectedFormat ="string format ";

        String str1 = "string ";
        String str2 = "format ";


        String actualFormat = StringFormat.getFormattedString2(str1,str2);

        Assert.assertEquals(expectedFormat, actualFormat);

    }
}
