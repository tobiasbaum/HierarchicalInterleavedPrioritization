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

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AlgRankingListOfStories extends AlgRankingList {

	private final List<AlgStory> elements;

	public AlgRankingListOfStories(
			final AlgModel algModel,
			final String id,
			final RankingComponent correspondingElement,
			final List<AlgStory> children,
			final Set<AlgRankingList> fullyAfter) {
		super(algModel, id, correspondingElement, fullyAfter);
		assert !children.isEmpty();

		this.elements = children;
		for (final AlgStory e : children) {
			e.registerParent(this);
		}
	}

	@Override
	protected boolean isEmpty() {
		return this.elements.isEmpty();
	}

	@Override
	protected AlgStory getFirstFittingStory() {
		return this.elements.get(0);
	}

	@Override
	public void removeChild(final AlgRankingElement toRemove) {
		this.elements.remove(toRemove);
		if (this.elements.isEmpty()) {
			this.removeFromAllParents();
		}
	}

    @Override
    public Map<? extends AlgRankingElement, Fraction> determineRelevantPrefix() {
        return Collections.singletonMap(this.elements.get(0), Fraction.ONE);
    }

    @Override
    public Fraction getWantingDegree(final AlgStory story) {
        final int index = this.elements.indexOf(story);
        if (index >= 0) {
            return new Fraction(this.elements.size() - index, this.elements.size());
        } else {
            return Fraction.ZERO;
        }
    }
}
