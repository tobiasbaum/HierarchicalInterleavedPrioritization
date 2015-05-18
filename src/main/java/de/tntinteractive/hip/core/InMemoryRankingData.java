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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class InMemoryRankingData extends RankingData {

	private final Set<String> startedStories = new HashSet<>();
	private final Map<String, Integer> balances = new HashMap<>();

	@Override
	public InMemoryRankingData copyInMemory() {
		final InMemoryRankingData ret = new InMemoryRankingData();
		ret.startedStories.addAll(this.startedStories);
		return ret;
	}

	public void markAsStarted(Story story) {
		this.startedStories.add(story.getID());
	}

	@Override
	public boolean isOpen(Story e) {
		return !this.startedStories.contains(e.getID());
	}

	@Override
	public int getListBalance(RankingList childList) {
		final Integer balance = this.balances.get(childList.getID());
		return balance == null ? 0 : balance;
	}

	public void clearListBalances() {
		this.balances.clear();
	}

}
