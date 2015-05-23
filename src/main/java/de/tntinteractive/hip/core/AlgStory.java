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

import java.util.HashSet;
import java.util.Set;

public class AlgStory extends AlgRankingElement {

	private final Fraction storyPoints;

	public AlgStory(
	        final AlgModel algModel,
	        final String id,
	        final RankingComponent correspondingElement,
	        final Fraction storyPoints) {
		super(algModel, id, correspondingElement);
		assert storyPoints != null;
		this.storyPoints = storyPoints;
	}

	public void start() {
		this.adjustAncestorBalances();
		this.removeFromAllParents();
	}

	private void adjustAncestorBalances() {
        final Set<AlgRankingListOfLists> listsWithStory = this.determineListsWithStory();
        for (final AlgRankingListOfLists list : listsWithStory) {
            list.adjustBalances(this);
        }
    }

    private Set<AlgRankingListOfLists> determineListsWithStory() {
        final Set<AlgRankingListOfLists> ret = new HashSet<>();
        this.addListOfListsAncestors(ret);
        return ret;
    }

    public Fraction getStoryPoints() {
		return this.storyPoints;
	}
}
