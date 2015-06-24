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

import java.util.List;

public class AlgRankingListOfStories extends AlgRankingList {

	private final List<Proxy<AlgStory>> elements;

	public AlgRankingListOfStories(
			final AlgModel algModel,
			final String id,
			final RankingComponent correspondingElement,
			final List<Proxy<AlgRankingList>> parents,
			final List<Proxy<AlgStory>> children,
			final String exclusionGroupId) {
		super(algModel, id, correspondingElement, parents, exclusionGroupId);

		this.elements = children;
	}

	@Override
	protected boolean isEmpty() {
		return this.elements.isEmpty();
	}

	@Override
	protected Proxy<AlgStory> getFirstFittingStory() {
		return this.elements.get(0);
	}

	@Override
	public void removeChild(final Proxy<AlgRankingElement> toRemove) {
		this.elements.remove(toRemove);
		if (this.elements.isEmpty()) {
			this.removeFromAllParents();
		}
	}

    @Override
    public Fraction getWantingDegree(final Proxy<AlgStory> story) {
        final int index = this.elements.indexOf(story);
        if (index >= 0) {
            return new Fraction(this.elements.size() - index, this.elements.size());
        } else {
            return Fraction.ZERO;
        }
    }

    @Override
    public void removeEmptyDescendants() {
        //hat keine Kinder, die leer sein könnten
    }
}
