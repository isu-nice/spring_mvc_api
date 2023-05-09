package com.codestates.hemcrest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;


public class JUnitAndHamcrestTest {

    @DisplayName("Hello Junit Test")
    @Test
    public void assertionTest1() {
        String actual = "Hello, Junit";
        String expected = "Hello, Junit";

        // JUnit
        assertEquals(expected, actual);
        // Hamcrest
        assertThat(actual, is(equalTo(expected)));
    }

    static class CryptoCurrency {
        public static Map<String, String> map = new HashMap<>();

        static {
            map.put("BTC", "Bitcoin");
            map.put("ETH", "Ethereum");
            map.put("ADA", "ADA");
            map.put("POT", "Polkadot");
        }
    }

    private String getCryptoCurrency(String unit) {
        return CryptoCurrency.map.get(unit).toUpperCase();
    }

    @DisplayName("AssertionNull() Test")
    @Test
    public void assertNotNullTest() {
        String currencyName = getCryptoCurrency("ETH");

        // JUnit
        assertNotNull(currencyName, "should be not null");
        // Hamcrest
        assertThat(currencyName, is(notNullValue()));
        // assertThat(currencyName, is(nullValue()));
    }

    @DisplayName("throws NullPointerException when map.get()")
    @Test
    public void assertionThrowExceptionTest() {
        // JUnit
        assertThrows(NullPointerException.class,
                () -> getCryptoCurrency("XRP"));

        // Hamcrest
        Throwable actualException = assertThrows(NullPointerException.class,
                () -> getCryptoCurrency("XRP"));
        assertThat(actualException.getClass(), is(NullPointerException.class));
    }
}


