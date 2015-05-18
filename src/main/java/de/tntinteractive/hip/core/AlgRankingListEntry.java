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

public class AlgRankingListEntry {

	private final AlgRankingList item;
	private final Fraction weight;

	public AlgRankingListEntry(AlgRankingList item, Fraction weight) {
		assert weight.compareTo(Fraction.ZERO) >= 0;
		assert weight.compareTo(Fraction.ONE) <= 0;
		this.item = item;
		this.weight = weight;
	}

	public AlgRankingList getItem() {
		return this.item;
	}

	public Fraction getWeight() {
		return this.weight;
	}

	@Override
	public String toString() {
		return this.item + "@" + this.weight.multiply(new Fraction(100, 1)) + "%";
	}

}
