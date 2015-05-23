package de.tntinteractive.hip.core;

import java.util.HashMap;
import java.util.Map;

/**
 * Decorates another {@link BalancesService} so that reads are cached and writes are cache-only.
 */
public class TemporaryBalanceDecorator implements BalancesService {

    private final BalancesService decorated;
    private final Map<String, Balances> balances = new HashMap<>();

    public TemporaryBalanceDecorator(final BalancesService decorated) {
        this.decorated = decorated;
    }

    @Override
    public Balances getFor(final String id) {
        Balances ret = this.balances.get(id);
        if (ret == null) {
            ret = this.decorated.getFor(id);
            this.balances.put(id, ret);
        }
        return ret;
    }

    @Override
    public void save(final String id, final Balances b) {
        this.balances.put(id, b);
    }

}
