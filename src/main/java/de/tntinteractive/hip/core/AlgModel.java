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

public class AlgModel {

	private final Map<String, Story> stories = new HashMap<>();
	private final Map<String, AlgRankingElement> elements = new HashMap<>();

	void register(AlgRankingElement e) {
		this.elements.put(e.getID(), e);
	}

	void registerMapping(Story story, AlgStory algStory) {
		this.stories.put(algStory.getID(), story);
	}

	public Story getStory(AlgStory algStory) {
		return this.stories.get(algStory.getID());
	}

	public boolean contains(String id) {
		return this.elements.containsKey(id);
	}

	public AlgRankingElement get(String id) {
		return this.elements.get(id);
	}

}
