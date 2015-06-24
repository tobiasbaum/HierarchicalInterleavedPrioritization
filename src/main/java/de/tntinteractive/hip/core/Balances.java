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

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * Contains the balances for the children of a ranking list.
 */
public class Balances {

    private final Map<String, Fraction> balancesPerChild = new TreeMap<>();

    public Balances() {
    }

    public Balances(final Map<String, Fraction> balancesPerChild) {
        this.balancesPerChild.putAll(balancesPerChild);
    }

    public static Balances parse(final String s) {
        final Balances ret = new Balances();
        for (final String line : s.split("\n")) {
            final String lt = line.trim();
            if (lt.isEmpty()) {
                continue;
            }
            final int semiIndex = lt.lastIndexOf(';');
            ret.balancesPerChild.put(
                lt.substring(0, semiIndex).trim(),
                Fraction.parse(lt.substring(semiIndex + 1).trim()));
        }
        return ret;
    }

    /**
     * Returns a String representation of this object that can be parsed with {@link #parse(String)}.
     * Entries with a balance of zero are not written.
     */
    public String toStringWithoutZeroes() {
        final StringBuilder ret = new StringBuilder();
        for (final Entry<String, Fraction> e : this.balancesPerChild.entrySet()) {
            if (!e.getValue().equals(Fraction.ZERO)) {
                ret.append(e.getKey()).append(';').append(e.getValue()).append('\n');
            }
        }
        return ret.toString();
    }

    public Balances copy() {
        return new Balances(this.balancesPerChild);
    }

    /**
     * Lowers the balances of the relevant prefix relative to the entries' weights until
     * there is room in at least one of the slots.
     */
    public void stripDown(final Map<Proxy<AlgRankingList>, Fraction> relevantPrefix, final Fraction roundSize) {
        while (this.thereIsNoRoomInAnySlot(relevantPrefix, roundSize)) {
            this.subtractRelativeRoundSizeFromEveryEntry(relevantPrefix, roundSize);
        }
    }

    private boolean thereIsNoRoomInAnySlot(
            final Map<Proxy<AlgRankingList>, Fraction> relevantPrefix,
            final Fraction roundSize) {
        for (final Entry<Proxy<AlgRankingList>, Fraction> e : relevantPrefix.entrySet()) {
            if (this.thereIsStillRoom(e, roundSize)) {
                return false;
            }
        }
        return true;
    }

    private void subtractRelativeRoundSizeFromEveryEntry(
            final Map<Proxy<AlgRankingList>, Fraction> relevantPrefix,
            final Fraction roundSize) {
        final Fraction negRoundSize = roundSize.multiply(new Fraction(-1, 1));
        for (final Entry<Proxy<AlgRankingList>, Fraction> e : relevantPrefix.entrySet()) {
            this.adjustBalance(e.getKey(), negRoundSize.multiply(e.getValue()));
        }
    }

    /**
     * Returns true if there is still room left for the given entry (assuming the given round size).
     */
    public boolean thereIsStillRoom(final Entry<Proxy<AlgRankingList>, Fraction> e, final Fraction roundSize) {
        return this.getBalanceFor(e.getKey().getID()).compareTo(e.getValue().multiply(roundSize)) < 0;
    }

    /**
     * Returns the balance for the given ID.
     * If the balance is unknown, zero is returned.
     */
    public Fraction getBalanceFor(final String id) {
        final Fraction ret = this.balancesPerChild.get(id);
        return ret == null ? Fraction.ZERO : ret;
    }

    /**
     * Adds the given amount to the balance for the given child.
     */
    public void adjustBalance(final Proxy<AlgRankingList> child, final Fraction amount) {
        this.balancesPerChild.put(child.getID(), this.getBalanceFor(child.getID()).add(amount));
    }

}
