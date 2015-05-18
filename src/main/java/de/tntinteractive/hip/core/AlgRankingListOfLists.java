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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AlgRankingListOfLists extends AlgRankingList {

	private final List<AlgRankingListEntry> elements;

	public AlgRankingListOfLists(AlgModel algModel, String id, List<AlgRankingListEntry> children) {
		super(algModel, id);
		assert !children.isEmpty();

		this.elements = children;
		for (final AlgRankingListEntry e : children) {
			e.getItem().registerParent(this);
		}
	}

	@Override
	protected boolean isEmpty() {
		return this.elements.isEmpty();
	}

	@Override
	protected AlgStory getFirstFittingStory(Fraction storyPointsForRound) {
		final ListLimits limits = this.determineLimitsOfRelevantPrefix(storyPointsForRound);
		//TEST
		System.out.println("limits = " + limits);
		for (int i = 0; i < 2; i++) {

			for (final AlgRankingListEntry entry : limits.getRelevantEntries()) {
				final Fraction limitForChild = limits.getFor(entry);
				final AlgRankingList childList = entry.getItem();
				final AlgStory firstFromChild = childList.getFirstFittingStory(limitForChild);
				if (childList.getBalance().add(firstFromChild.getStoryPoints()).compareTo(limitForChild) <= 0) {
					//TEST
					System.out.println("chose " + firstFromChild);
					return firstFromChild;
				}
			}
			limits.subtractFromBalance();
		}
		throw new RuntimeException("should not happen");
	}

	private ListLimits determineLimitsOfRelevantPrefix(Fraction storyPointsForRound) {
		Fraction weightSum = Fraction.ZERO;
		final ListLimits limits = new ListLimits();
		final List<AlgRankingListEntry> ret = new ArrayList<>();
		for (final AlgRankingListEntry e : this.elements) {
			ret.add(e);
			final Fraction currentWeight = Fraction.min(e.getWeight(), Fraction.ONE.subtract(weightSum));
			limits.put(e, storyPointsForRound.multiply(currentWeight));
			weightSum = weightSum.add(e.getWeight());
			if (weightSum.compareTo(Fraction.ONE) >= 0) {
				break;
			}
		}
		return limits;
	}

	@Override
	public void removeChild(AlgRankingElement toRemove) {
		final Iterator<AlgRankingListEntry> iter = this.elements.iterator();
		while (iter.hasNext()) {
			final AlgRankingListEntry e = iter.next();
			if (e.getItem() == toRemove) {
				iter.remove();
			}
		}
		if (this.elements.isEmpty()) {
			this.removeFromAllParents();
		}
	}

}
