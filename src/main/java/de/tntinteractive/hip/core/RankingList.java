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
import java.util.List;

public abstract class RankingList extends RankingElement {

	public final List<? extends Story> calculateStoryRanking(Fraction storyPointsPerRound) {
		final AlgRankingList algList = this.toAlgModel(new AlgModel());
		//TODO algList kann null sein
		final List<Story> ret = new ArrayList<>();
		AlgStory next;
		while ((next = algList.startNextStory(storyPointsPerRound)) != null) {
			ret.add(algList.getModel().getStory(next));
		}
		return ret;
	}

	@Override
	protected AlgRankingList toAlgModel(AlgModel algModel) {
		if (algModel.contains(this.getID())) {
			return (AlgRankingList) algModel.get(this.getID());
		}
		if (this.getElements().isEmpty()) {
			return null;
		}

		if (this.getElements().get(0) instanceof Story) {
			final List<AlgStory> children = new ArrayList<>();
			for (final RankingElement e : this.getElements()) {
				final AlgStory algE = (AlgStory) e.toAlgModel(algModel);
				if (algE != null) {
					children.add(algE);
				}
			}
			if (children.isEmpty()) {
				return null;
			}
			return new AlgRankingListOfStories(algModel, this.getID(), children);
		} else {
			final List<AlgRankingListEntry> children = new ArrayList<>();
			for (final RankingElement e : this.getElements()) {
				final AlgRankingList algE = (AlgRankingList) e.toAlgModel(algModel);
				if (algE != null) {
					children.add(new AlgRankingListEntry(algE, new Fraction(this.getWeightInPercent(e), 100)));
				}
			}
			if (children.isEmpty()) {
				return null;
			}
			return new AlgRankingListOfLists(algModel, this.getID(), children);
		}
	}

	public abstract int getWeightInPercent(RankingElement e);

	public abstract List<? extends RankingElement> getElements();

}
