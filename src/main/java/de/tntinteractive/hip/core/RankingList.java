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
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public abstract class RankingList extends RankingComponent {

	public final List<? extends Story> calculateStoryRanking(final BalancesService balancesService) {
		final AlgRankingList algList = this.toAlgModel(new AlgModel(new TemporaryBalanceDecorator(balancesService)));
		//TODO algList kann null sein
		final List<Story> ret = new ArrayList<>();
		AlgStory next;
		while ((next = algList.startNextStory()) != null) {
			ret.add(algList.getModel().getStory(next));
		}
		return ret;
	}

	@Override
	protected AlgRankingList toAlgModel(final AlgModel algModel) {
		if (algModel.contains(this.getID())) {
			return (AlgRankingList) algModel.get(this.getID());
		}
		if (this.getElements().isEmpty()) {
			return null;
		}

		if (this.getElements().get(0) instanceof Story) {
			final List<AlgStory> children = this.mapStoryChildren(algModel);
			if (children.isEmpty()) {
				return null;
			}
			return new AlgRankingListOfStories(
			        algModel,
			        this.getID(),
			        this,
			        children,
			        this.mapFullyAfter(algModel));
		} else {
			final List<AlgRankingListEntry> children = this.mapListEntryChildren(algModel);
			if (children.isEmpty()) {
				return null;
			}
			return new AlgRankingListOfLists(
			        algModel,
			        this.getID(),
			        this,
			        children,
			        this.mapFullyAfter(algModel),
			        this.getRoundSize(),
			        algModel.getBalancesService());
		}
	}

	private List<AlgRankingListEntry> mapListEntryChildren(final AlgModel algModel) {
		final List<AlgRankingListEntry> children = new ArrayList<>();
		for (final RankingComponent e : this.getElements()) {
			final AlgRankingList algE = (AlgRankingList) e.toAlgModel(algModel);
			if (algE != null) {
				children.add(new AlgRankingListEntry(algE, new Fraction(this.getWeightInPercent(e), 100)));
			}
		}
		return children;
	}

	private List<AlgStory> mapStoryChildren(final AlgModel algModel) {
		final List<AlgStory> children = new ArrayList<>();
		for (final RankingComponent e : this.getElements()) {
			final AlgStory algE = (AlgStory) e.toAlgModel(algModel);
			if (algE != null) {
				children.add(algE);
			}
		}
		return children;
	}

	private Set<AlgRankingList> mapFullyAfter(final AlgModel algModel) {
		final Set<AlgRankingList> ret = new HashSet<>();
		for (final RankingList l : this.getFullyAfter()) {
			ret.add(l.toAlgModel(algModel));
		}
		return ret;
	}

	/**
	 * Returns the current relevant prefix (as orderered map) including the respective weights,
	 * i.e. all non-empty children until a the weight sum reaches 1 (aka 100 percent). The resulting
	 * weights always sum to 1, which means the sometimes the last weight will be shrunk or enlarged.
	 */
	public Map<? extends RankingComponent, Fraction> determineRelevantPrefix() {
		final AlgModel m = new AlgModel(new DummyBalancesService());
		final AlgRankingList l = this.toAlgModel(m);
		final Map<? extends AlgRankingElement, Fraction> algResult = l.determineRelevantPrefix();
		final LinkedHashMap<RankingComponent, Fraction> result = new LinkedHashMap<>();
		for (final Entry<? extends AlgRankingElement, Fraction> e : algResult.entrySet()) {
			result.put(m.getReverse(e.getKey()), e.getValue());
		}
		return result;
	}

	public abstract int getWeightInPercent(RankingComponent e);

	public abstract List<? extends RankingComponent> getElements();

	/**
	 * Returns the number of stories points that form a "round" in round robin scheduling. The bigger this number,
	 * the longer the streaks from the same child list.
	 */
	public abstract int getRoundSize();

	public abstract Collection<? extends RankingList> getFullyAfter();

}
