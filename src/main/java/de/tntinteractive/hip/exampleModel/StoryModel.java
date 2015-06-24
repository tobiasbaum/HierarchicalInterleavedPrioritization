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
import java.util.List;

import de.tntinteractive.hip.core.FakeProxy;
import de.tntinteractive.hip.core.Fraction;
import de.tntinteractive.hip.core.Proxy;
import de.tntinteractive.hip.core.RankingList;
import de.tntinteractive.hip.core.Story;

public class StoryModel extends Story {

	private final String id;
	private final Fraction storyPoints;
    private final List<Proxy<? extends RankingList>> parents = new ArrayList<>();

	public StoryModel(final String storyId, final Fraction storyPoints) {
		assert storyPoints != null;
		this.id = storyId;
		this.storyPoints = storyPoints;
	}

	@Override
	public String getID() {
		return this.id;
	}

	@Override
	public Fraction getStoryPoints() {
		return this.storyPoints;
	}

    @Override
    public List<Proxy<? extends RankingList>> getParents() {
        return this.parents;
    }

    public void addParentHelper(final RankingListModel t) {
        this.parents.add(new FakeProxy<>(t, t.getID()));
    }
}
