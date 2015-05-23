package de.tntinteractive.hip.core;

/**
 * Allows reading and writing of balance data for ranking lists.
 */
public interface BalancesService {

    /**
     * Returns the balance information for the list with the given ID.
     */
    public abstract Balances getFor(final String id);

    /**
     * Saves the given balance information for the list with the given ID.
     */
    public abstract void save(String id, Balances b);

}
