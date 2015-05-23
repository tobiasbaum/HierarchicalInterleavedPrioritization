package de.tntinteractive.hip.core;

public class DummyBalancesService implements BalancesService {

    @Override
    public Balances getFor(final String id) {
        return new Balances();
    }

    @Override
    public void save(final String id, final Balances b) {
    }

}
