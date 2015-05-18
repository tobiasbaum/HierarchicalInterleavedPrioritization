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

	public final class ListBuilder {

		private final RankingListModel list;

		private ListBuilder(RankingListModel list) {
			this.list = list;
		}

		public ListBuilder addElement(String string, int weight) {
			this.list.addElement(ModelBuilder.this.getElementOrCreateStory(string), weight);
			return this;
		}

		public ListBuilder addStory(String string, int storyPoints) {
			this.list.addElement(ModelBuilder.this.getOrCreateStory(string, new Fraction(storyPoints, 1)), null);
			return this;
		}

		public ModelBuilder endList() {
			return ModelBuilder.this;
		}

	}

	private final Map<String, RankingElement> elements = new HashMap<>();

	public ModelBuilder addList(String listId, String... storiesOrListIDs) {
		assert !this.elements.containsKey(listId);
		final RankingListModel list = new RankingListModel(listId, 100);
		for (final String elementId : storiesOrListIDs) {
			list.addElement(this.getElementOrCreateStory(elementId), null);
		}
		this.elements.put(listId, list);
		return this;
	}

	private RankingElement getElementOrCreateStory(String id) {
		if (!this.elements.containsKey(id)) {
			this.elements.put(id, new StoryModel(id, Fraction.ONE));
		}
		return this.elements.get(id);
	}

	private StoryModel getOrCreateStory(String id, Fraction storyPoints) {
		if (!this.elements.containsKey(id)) {
			this.elements.put(id, new StoryModel(id, storyPoints));
		}
		assert ((StoryModel) this.elements.get(id)).getStoryPoints().equals(storyPoints);
		return (StoryModel) this.elements.get(id);
	}

	public RankingList getList(String listId) {
		return (RankingList) this.elements.get(listId);
	}

	public ListBuilder startList(String listId) {
		this.addList(listId);
		return new ListBuilder((RankingListModel) this.elements.get(listId));
	}

}
