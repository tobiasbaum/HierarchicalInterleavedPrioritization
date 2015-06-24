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
