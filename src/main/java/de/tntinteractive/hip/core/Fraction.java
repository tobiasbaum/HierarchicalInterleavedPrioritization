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

import java.math.BigInteger;

public class Fraction implements Comparable<Fraction> {

	public static final Fraction ZERO = new Fraction(0, 1);
	public static final Fraction ONE = new Fraction(1, 1);

	private final BigInteger numerator;
	private final BigInteger denominator;

	public Fraction(int numerator, int denominator) {
		this(BigInteger.valueOf(numerator), BigInteger.valueOf(denominator));
	}

	public Fraction(BigInteger numerator, BigInteger denominator) {
		BigInteger gcd = numerator.gcd(denominator);
		if (denominator.signum() < 0) {
			gcd = gcd.negate();
		}
		this.numerator = numerator.divide(gcd);
		this.denominator = denominator.divide(gcd);
	}

	@Override
	public int compareTo(Fraction f) {
		return this.numerator.multiply(f.denominator).compareTo(f.numerator.multiply(this.denominator));
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Fraction)) {
			return false;
		}
		final Fraction f = (Fraction) o;
		return this.compareTo(f) == 0;
	}

	@Override
	public int hashCode() {
		return this.numerator.hashCode() + this.denominator.hashCode();
	}

	@Override
	public String toString() {
		return this.numerator + "/" + this.denominator;
	}

	public Fraction add(Fraction f) {
		return new Fraction(
				this.numerator.multiply(f.denominator).add(this.denominator.multiply(f.numerator)),
				this.denominator.multiply(f.denominator));
	}

	public Fraction subtract(Fraction f) {
		return new Fraction(
				this.numerator.multiply(f.denominator).subtract(this.denominator.multiply(f.numerator)),
				this.denominator.multiply(f.denominator));
	}

	public Fraction multiply(Fraction f) {
		return new Fraction(
				this.numerator.multiply(f.numerator),
				this.denominator.multiply(f.denominator));
	}

	public static Fraction min(Fraction f1, Fraction f2) {
		return f1.compareTo(f2) <= 0 ? f1 : f2;
	}

}
