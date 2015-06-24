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
import java.util.Collection;

public class Fraction implements Comparable<Fraction> {

	public static final Fraction ZERO = new Fraction(0, 1);
	public static final Fraction ONE = new Fraction(1, 1);

	private final BigInteger numerator;
	private final BigInteger denominator;

	public Fraction(final int numerator, final int denominator) {
		this(BigInteger.valueOf(numerator), BigInteger.valueOf(denominator));
	}

	public Fraction(final BigInteger numerator, final BigInteger denominator) {
		BigInteger gcd = numerator.gcd(denominator);
		if (denominator.signum() < 0) {
			gcd = gcd.negate();
		}
		this.numerator = numerator.divide(gcd);
		this.denominator = denominator.divide(gcd);
	}

	public static Fraction parse(final String s) {
	    if (s.contains(" ")) {
	        final String[] parts = s.split(" ");
	        final BigInteger full = new BigInteger(parts[0]);
	        final Fraction rest = parse(parts[1]);
	        return new Fraction(full, BigInteger.ONE).add(rest.multiply(new Fraction(full.signum(), 1)));
	    } else if (s.contains("/")) {
            final String[] parts = s.split("/");
            final BigInteger num = new BigInteger(parts[0]);
            final BigInteger denom = new BigInteger(parts[1]);
            return new Fraction(num, denom);
	    } else {
	        return new Fraction(new BigInteger(s), BigInteger.ONE);
	    }
	}

	@Override
	public int compareTo(final Fraction f) {
		return this.numerator.multiply(f.denominator).compareTo(f.numerator.multiply(this.denominator));
	}

	@Override
	public boolean equals(final Object o) {
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
	    final BigInteger[] divAndRem = this.numerator.divideAndRemainder(this.denominator);
	    if (divAndRem[1].equals(BigInteger.ZERO)) {
            return divAndRem[0].toString();
	    } else if (divAndRem[0].equals(BigInteger.ZERO)) {
            return divAndRem[1] + "/" + this.denominator;
	    } else {
	        return divAndRem[0] + " " + divAndRem[1].abs() + "/" + this.denominator;
	    }
	}

	public Fraction add(final Fraction f) {
		return new Fraction(
				this.numerator.multiply(f.denominator).add(this.denominator.multiply(f.numerator)),
				this.denominator.multiply(f.denominator));
	}

	public Fraction subtract(final Fraction f) {
		return new Fraction(
				this.numerator.multiply(f.denominator).subtract(this.denominator.multiply(f.numerator)),
				this.denominator.multiply(f.denominator));
	}

	public Fraction multiply(final Fraction f) {
		return new Fraction(
				this.numerator.multiply(f.numerator),
				this.denominator.multiply(f.denominator));
	}

    public Fraction divide(final Fraction f) {
        return new Fraction(
                this.numerator.multiply(f.denominator),
                this.denominator.multiply(f.numerator));
    }

    public int intValue() {
        return this.numerator.divide(this.denominator).intValue();
    }

    public static Fraction min(final Fraction f1, final Fraction f2) {
        return f1.compareTo(f2) <= 0 ? f1 : f2;
    }

    public static Fraction max(final Fraction f1, final Fraction f2) {
        return f1.compareTo(f2) <= 0 ? f2 : f1;
    }

    public static Fraction sum(final Collection<Fraction> values) {
        Fraction ret = Fraction.ZERO;
        for (final Fraction f : values) {
            ret = ret.add(f);
        }
        return ret;
    }

}
