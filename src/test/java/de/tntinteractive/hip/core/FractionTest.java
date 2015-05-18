/**
    This file is part of Hierarchical Interleaved Prioritization.

    Hierarchical Interleaved Prioritization is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Hierarchical Interleaved Prioritization is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Hierarchical Interleaved Prioritization. If not, see <http://www.gnu.org/licenses/>.
 */

package de.tntinteractive.hip.core;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class FractionTest {

	private static Fraction f(int num, int den) {
		return new Fraction(num, den);
	}

	@Test
	public void testEquals() {
		assertEquals(f(1, 1), f(2, 2));
		assertEquals(f(1, 1), f(-1, -1));
		assertEquals(f(2, 1), f(4, 2));
		assertEquals(f(3, 6), f(1, 2));
		assertEquals(f(3, 7), f(9, 21));
	}

	@Test
	public void testAdd() {
		assertEquals(f(1, 1).add(f(2, 2)), f(2, 1));
		assertEquals(f(1, 2).add(f(1, 4)), f(3, 4));
	}

	@Test
	public void testSubtract() {
		assertEquals(f(1, 1).subtract(f(2, 2)), f(0, 1));
		assertEquals(f(1, 2).subtract(f(1, 4)), f(1, 4));
	}

	@Test
	public void testMultiply() {
		assertEquals(f(1, 1).multiply(f(2, 2)), f(1, 1));
		assertEquals(f(1, 2).multiply(f(1, 4)), f(1, 8));
		assertEquals(f(3, 2).multiply(f(1, 7)), f(3, 14));
	}

	@Test
	public void testCompareTo() {
		assertThat(f(1, 1).compareTo(f(2, 2)), is(0));
		assertThat(f(2, 2).compareTo(f(2, 2)), is(0));
		assertThat(f(2, 2).compareTo(f(1, 1)), is(0));
		assertThat(f(3, 5).compareTo(f(1, 2)), greaterThan(0));
		assertThat(f(3, 5).compareTo(f(3, 6)), greaterThan(0));
		assertThat(f(1, 2).compareTo(f(3, 5)), lessThan(0));
		assertThat(f(3, 6).compareTo(f(3, 5)), lessThan(0));
	}

}
