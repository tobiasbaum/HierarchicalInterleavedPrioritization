package de.tntinteractive.hip.core;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class BalancesTest {

    @Test
    public void testToStringAndParse() {
        assertThat(Balances.parse("").toStringWithoutZeroes(), is(""));
        assertThat(Balances.parse("PSY-5;0").toStringWithoutZeroes(), is(""));
        assertThat(Balances.parse("PSY-1;2\nPSY-2;3 1/2").toStringWithoutZeroes(), is("PSY-1;2\nPSY-2;3 1/2\n"));
        assertThat(Balances.parse("PSY-1;0\nPSY-2;3 1/2").toStringWithoutZeroes(), is("PSY-2;3 1/2\n"));
        assertThat(Balances.parse("PSY-1;0\nPSY-2;3 1/2\n\nPSY-5;1/2").toStringWithoutZeroes(), is("PSY-2;3 1/2\nPSY-5;1/2\n"));
        assertThat(Balances.parse("PSY-17;10\r\nPSY-18;20").toStringWithoutZeroes(), is("PSY-17;10\nPSY-18;20\n"));
    }

}
