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

	private final Map<String, RankingComponent> originElements = new HashMap<>();
	private final Map<String, AlgRankingElement> elements = new HashMap<>();
    private final BalancesService balancesService;

	public AlgModel(final BalancesService balancesService) {
	    this.balancesService = balancesService;
	}

	void register(final AlgRankingElement e, final RankingComponent correspondingElement) {
		assert e.getID().equals(correspondingElement.getID());
		this.elements.put(e.getID(), e);
		this.originElements.put(e.getID(), correspondingElement);
	}

	public Story getStory(final AlgStory algStory) {
		return (Story) this.originElements.get(algStory.getID());
	}

	public RankingComponent getReverse(final Proxy<? extends AlgRankingElement> algElement) {
		return this.originElements.get(algElement.getID());
	}

	public boolean contains(final String id) {
		return this.elements.containsKey(id);
	}

	public AlgRankingElement get(final String id) {
		return this.elements.get(id);
	}

    public AlgRankingElement getAndCheckExistence(final String id) {
        final AlgRankingElement element = this.get(id);
        if (element == null) {
            throw new RuntimeException(String.format(
                    "Story %s is missing in model. Existing stories: %s",
                    id,
                    this.elements.keySet()));
        }
        return element;
    }

    public BalancesService getBalancesService() {
        return this.balancesService;
    }

}
