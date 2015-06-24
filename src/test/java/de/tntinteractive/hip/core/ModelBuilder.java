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
import java.util.Map;

import de.tntinteractive.hip.exampleModel.RankingListModel;
import de.tntinteractive.hip.exampleModel.StoryModel;

public class ModelBuilder {

	private static final int DEFAULT_ROUND_SIZE = 2;

    public final class ListBuilder {

		private final RankingListModel list;

		private ListBuilder(final RankingListModel list) {
			this.list = list;
		}

		public ListBuilder addElement(final String string, final int weight) {
			this.list.addElement(ModelBuilder.this.getElementOrCreateStory(string), weight);
			return this;
		}

		public ListBuilder addStory(final String string, final int storyPoints) {
			return this.addStory(string, new Fraction(storyPoints, 1));
		}

		public ListBuilder addStory(final String string, final Fraction storyPoints) {
			this.list.addElement(ModelBuilder.this.getOrCreateStory(string, storyPoints), null);
			return this;
		}

		public ListBuilder exclusionGroup(final String groupName) {
			this.list.setExclusionGroup(groupName);
			return this;
		}

		public ModelBuilder endList() {
			return ModelBuilder.this;
		}

	}

	private final Map<String, RankingComponent> elements = new HashMap<>();

    public ModelBuilder addList(final String listId, final String... storiesOrListIDs) {
        return this.addList(listId, DEFAULT_ROUND_SIZE, storiesOrListIDs);
    }

	public ModelBuilder addList(final String listId, final int roundSize, final String... storiesOrListIDs) {
		assert !this.elements.containsKey(listId);
		final RankingListModel list = new RankingListModel(listId, 100, roundSize);
		for (final String elementId : storiesOrListIDs) {
			list.addElement(this.getElementOrCreateStory(elementId), null);
		}
		this.elements.put(listId, list);
		return this;
	}

	private RankingComponent getElementOrCreateStory(final String id) {
		if (!this.elements.containsKey(id)) {
			this.elements.put(id, new StoryModel(id, Fraction.ONE));
		}
		return this.elements.get(id);
	}

	private StoryModel getOrCreateStory(final String id, final Fraction storyPoints) {
		if (!this.elements.containsKey(id)) {
			this.elements.put(id, new StoryModel(id, storyPoints));
		}
		assert ((StoryModel) this.elements.get(id)).getStoryPoints().equals(storyPoints);
		return (StoryModel) this.elements.get(id);
	}

	public RankingList getList(final String listId) {
		return (RankingList) this.elements.get(listId);
	}

    public Story getStory(final String storyId) {
        return (Story) this.elements.get(storyId);
    }

	public ListBuilder startList(final String listId, final int roundSize) {
		this.addList(listId, roundSize);
		return new ListBuilder((RankingListModel) this.elements.get(listId));
	}

    public ListBuilder startList(final String listId) {
        return this.startList(listId, 2);
    }
}
