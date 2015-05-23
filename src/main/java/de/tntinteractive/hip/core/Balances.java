package de.tntinteractive.hip.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Contains the balances for the children of a ranking list.
 */
public class Balances {

    private final Map<String, Fraction> balancesPerChild = new HashMap<>();

    public Balances() {
    }

    public Balances(final Map<String, Fraction> balancesPerChild) {
        this.balancesPerChild.putAll(balancesPerChild);
    }

    public Balances copy() {
        return new Balances(this.balancesPerChild);
    }

    /**
     * Lowers the balances of the relevant prefix relative to the entries' weights until
     * there is room in at least one of the slots.
     */
    public void stripDown(final Map<AlgRankingList, Fraction> relevantPrefix, final Fraction roundSize) {
        while (this.thereIsNoRoomInAnySlot(relevantPrefix, roundSize)) {
            this.subtractRelativeRoundSizeFromEveryEntry(relevantPrefix, roundSize);
        }
    }

    private boolean thereIsNoRoomInAnySlot(
            final Map<AlgRankingList, Fraction> relevantPrefix,
            final Fraction roundSize) {
        for (final Entry<AlgRankingList, Fraction> e : relevantPrefix.entrySet()) {
            if (this.thereIsStillRoom(e, roundSize)) {
                return false;
            }
        }
        return true;
    }

    private void subtractRelativeRoundSizeFromEveryEntry(
            final Map<AlgRankingList, Fraction> relevantPrefix,
            final Fraction roundSize) {
        final Fraction negRoundSize = roundSize.multiply(new Fraction(-1, 1));
        for (final Entry<AlgRankingList, Fraction> e : relevantPrefix.entrySet()) {
            this.adjustBalance(e.getKey(), negRoundSize.multiply(e.getValue()));
        }
    }

    /**
     * Returns true if there is still room left for the given entry (assuming the given round size).
     */
    public boolean thereIsStillRoom(final Entry<AlgRankingList, Fraction> e, final Fraction roundSize) {
        return this.getBalanceFor(e.getKey().getID()).compareTo(e.getValue().multiply(roundSize)) < 0;
    }

    private Fraction getBalanceFor(final String id) {
        final Fraction ret = this.balancesPerChild.get(id);
        return ret == null ? Fraction.ZERO : ret;
    }

    /**
     * Adds the given amount to the balance for the given child.
     */
    public void adjustBalance(final AlgRankingList child, final Fraction amount) {
        this.balancesPerChild.put(child.getID(), this.getBalanceFor(child.getID()).add(amount));
    }

}
