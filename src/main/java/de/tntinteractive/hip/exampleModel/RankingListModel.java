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

package de.tntinteractive.hip.exampleModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.tntinteractive.hip.core.RankingComponent;
import de.tntinteractive.hip.core.RankingList;

public class RankingListModel extends RankingList {

	private final String id;
	private final ArrayList<RankingComponent> elements;
	private final Map<String, Integer> weights;
	private final int defaultWeight;
	private final Set<RankingList> fullyAfter = new HashSet<>();
	private final int roundSize;

	public RankingListModel(final String listId, final int defaultWeight, final int roundSize) {
		assert defaultWeight >= 0 && defaultWeight <= 100;
		this.id = listId;
		this.elements = new ArrayList<>();
		this.weights = new HashMap<>();
		this.defaultWeight = defaultWeight;
		this.roundSize = roundSize;
	}

	public void addElement(final RankingComponent element, final Integer weight) {
		assert weight == null || (weight >= 0 && weight <= 100);
		this.elements.add(element);
		if (weight != null) {
			this.weights.put(element.getID(), weight);
		}
	}

	@Override
	public String getID() {
		return this.id;
	}

	@Override
	public List<? extends RankingComponent> getElements() {
		return this.elements;
	}

	@Override
	public int getWeightInPercent(final RankingComponent e) {
		final Integer weight = this.weights.get(e.getID());
		return weight == null ? this.defaultWeight : weight;
	}

	public void setFullyAfter(final RankingList list) {
		this.fullyAfter .add(list);
	}

	@Override
	public Collection<? extends RankingList> getFullyAfter() {
		return this.fullyAfter;
	}

    @Override
    public int getRoundSize() {
        return this.roundSize;
    }

}
