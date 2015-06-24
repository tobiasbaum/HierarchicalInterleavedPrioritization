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
