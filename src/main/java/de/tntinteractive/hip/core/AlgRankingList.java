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

import java.util.Collection;
import java.util.Map;
import java.util.Set;


public abstract class AlgRankingList extends AlgRankingElement {

	private final Set<AlgRankingList> fullyAfter;

	public AlgRankingList(final AlgModel algModel, final String id, final RankingComponent correspondingElement,
			final Set<AlgRankingList> fullyAfter) {
		super(algModel, id, correspondingElement);
		this.fullyAfter = fullyAfter;
	}

	public boolean shallBeFullyAfterOneOf(final Collection<AlgRankingList> entries) {
		for (final AlgRankingList e : entries) {
			if (this.fullyAfter.contains(e)) {
				return true;
			}
		}
		return false;
	}

	public AlgStory startNextStory() {
		if (this.isEmpty()) {
			//can only happen if there is no work left to do
			return null;
		}

		final AlgStory story = this.getFirstFittingStory();
		//TEST
		System.out.println("chose for start " + story);
		story.start();
		//TODO: Nach dem Start nochmal dafür sorgen, dass nicht alle am Anschlag sind (falls beim nächsten Aufruf
		//  der relevantPrefix länger wird
		return story;
	}

	protected abstract boolean isEmpty();

	/**
	 * Returns the highest ranked story that fits still fits into the lists limits given its current balance.
	 * When the balance is zero or negative, even a larger story is allowed (so that higher ranked stories will
	 * get their way before lower ranked ones).
	 * @pre !this.isEmpty()
	 */
	protected abstract AlgStory getFirstFittingStory();

	public abstract void removeChild(AlgRankingElement toRemove);

	/**
	 * Returns the lists prefix which's weight sums up to 100 percent.
	 * The returned map is ordered by relevance, and the values always sum up to 1 (aka 100%).
	 */
	public abstract Map<? extends AlgRankingElement, Fraction> determineRelevantPrefix();

	/**
	 * Returns a number that characterizes how much this list wants the given story.
	 * The number is between 1 (wants more than anything else) and 0 (does not want at all).
	 */
    public abstract Fraction getWantingDegree(final AlgStory story);

}
