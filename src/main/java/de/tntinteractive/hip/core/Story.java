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


public abstract class Story extends RankingComponent {

	public abstract Fraction getStoryPoints();

    /**
     * Calculates the new balances that result from starting this story and saves them in the
     * given {@link BalancesService}.
     */
    public final void start(final BalancesService balancesService) {
        final AlgModel model = new AlgModel(balancesService);
        new AlgProxy<AlgStory>(new FakeProxy<>(this, this.getID()), model).get().start();
    }
}
