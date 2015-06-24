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
import java.util.List;


public abstract class AlgRankingList extends AlgRankingElement {

	private final String exclusionGroupId;

	public AlgRankingList(final AlgModel algModel, final String id, final RankingComponent correspondingElement,
	        final List<Proxy<AlgRankingList>> parents, final String exclusionGroupId) {
		super(algModel, id, correspondingElement, parents);
		this.exclusionGroupId = exclusionGroupId;
	}

	public boolean isInSameExclusionGroupAsOneOf(final Collection<Proxy<AlgRankingList>> entries) {
		for (final Proxy<AlgRankingList> e : entries) {
			if (this.exclusionGroupId != null && this.exclusionGroupId.equals(e.get().exclusionGroupId)) {
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

		final AlgStory story = this.getFirstFittingStory().get();
		story.start();
		return story;
	}

	protected abstract boolean isEmpty();

	/**
	 * Returns the highest ranked story that fits still fits into the lists limits given its current balance.
	 * When the balance is zero or negative, even a larger story is allowed (so that higher ranked stories will
	 * get their way before lower ranked ones).
	 * @pre !this.isEmpty()
	 */
	protected abstract Proxy<AlgStory> getFirstFittingStory();

	public abstract void removeChild(Proxy<AlgRankingElement> toRemove);

	/**
	 * Returns a number that characterizes how much this list wants the given story.
	 * The number is between 1 (wants more than anything else) and 0 (does not want at all).
	 */
    public abstract Fraction getWantingDegree(final Proxy<AlgStory> story);

    /**
     * Removes recursively all empty sublists (so that every remaining list ultimately contains at least one story).
     */
    public abstract void removeEmptyDescendants();

}
