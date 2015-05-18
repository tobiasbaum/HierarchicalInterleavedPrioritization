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
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ListLimits {

	private final Map<AlgRankingListEntry, Fraction> limits = new LinkedHashMap<>();

	public Collection<AlgRankingListEntry> getRelevantEntries() {
		return this.limits.keySet();
	}

	public Fraction getFor(AlgRankingListEntry childList) {
		return this.limits.get(childList);
	}

	public void subtractFromBalance() {
		for (final Entry<AlgRankingListEntry, Fraction> entry : this.limits.entrySet()) {
			entry.getKey().getItem().subtractFromBalance(entry.getValue());
		}
	}

	public void put(AlgRankingListEntry element, Fraction limit) {
		this.limits.put(element, limit);
	}

	@Override
	public String toString() {
		return this.limits.toString();
	}

}
