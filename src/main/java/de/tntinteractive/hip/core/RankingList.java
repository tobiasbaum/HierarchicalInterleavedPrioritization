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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public abstract class RankingList extends RankingComponent {


    public final List<? extends Story> calculateStoryRanking(final BalancesService balancesService) {
		final AlgRankingList algList = (AlgRankingList)
		        this.toAlgModel(new AlgModel(new TemporaryBalanceDecorator(balancesService)));
		algList.removeEmptyDescendants();
		final List<Story> ret = new ArrayList<>();
		AlgStory next;
		while ((next = algList.startNextStory()) != null) {
			ret.add(algList.getModel().getStory(next));
		}
		return ret;
	}

	/**
	 * Returns the current relevant prefix (as orderered map) including the respective weights,
	 * i.e. all non-empty children until a the weight sum reaches 1 (aka 100 percent). The resulting
	 * weights always sum to 1, which means the sometimes the last weight will be shrunk or enlarged.
	 */
	public Map<? extends RankingComponent, Fraction> determineRelevantPrefix() {
	    assert this.getElements().get(0).get() instanceof RankingList;

		final AlgModel m = new AlgModel(new DummyBalancesService());
		final AlgRankingList l = (AlgRankingList) this.toAlgModel(m);
		final Map<Proxy<AlgRankingList>, Fraction> algResult =
		        ((AlgRankingListOfLists) l).determineRelevantPrefix();
		final LinkedHashMap<RankingComponent, Fraction> result = new LinkedHashMap<>();
		for (final Entry<Proxy<AlgRankingList>, Fraction> e : algResult.entrySet()) {
			result.put(m.getReverse(e.getKey()), e.getValue());
		}
		return result;
	}

	public abstract int getWeightInPercent(String childId);

	public abstract List<Proxy<? extends RankingComponent>> getElements();

	/**
	 * Returns the number of stories points that form a "round" in round robin scheduling. The bigger this number,
	 * the longer the streaks from the same child list.
	 */
	public abstract int getRoundSize();

	/**
	 * Returns the identifier of the "exclusion group". All lists in the same exclusion group cannot be interleaved.
	 * A return value of null means "no exclusion group".
	 */
	public abstract String getExclusionGroup();

}
