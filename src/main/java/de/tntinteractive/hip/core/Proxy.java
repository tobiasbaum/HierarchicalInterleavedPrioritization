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
 * A proxy to allow lazy loading of {@link RankingComponent}s.
 *
 * @param <E> Type of the component.
 */
public abstract class Proxy<E> {

    /**
     * Returns the real component. Might load it lazily on first invocation.
     */
    public abstract E get();

    public abstract String getID();

    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof Proxy)) {
            return false;
        }
        final Proxy<?> p = (Proxy<?>) o;
        return this.getID().equals(p.getID());
    }

    @Override
    public int hashCode() {
        return this.getID().hashCode();
    }

    @Override
    public String toString() {
        return this.getClass().getName() + " for " + this.getID();
    }
}
