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

import java.util.List;
import java.util.Set;

public abstract class AlgRankingElement {

	private final AlgModel model;
	private final String id;
	private final List<Proxy<AlgRankingList>> parents;

	public AlgRankingElement(final AlgModel algModel, final String id, final RankingComponent correspondingElement,
	        final List<Proxy<AlgRankingList>> parents) {
		this.model = algModel;
		this.id = id;
		this.parents = parents;
		algModel.register(this, correspondingElement);
	}

	public String getID() {
		return this.id;
	}

	public AlgModel getModel() {
		return this.model;
	}

	public List<Proxy<AlgRankingList>> getParents() {
		return this.parents;
	}

	public void removeFromAllParents() {
		for (final Proxy<AlgRankingList> parent : this.getParents()) {
			parent.get().removeChild(new FakeProxy<AlgRankingElement>(this, this.getID()));
		}
	}

	/**
	 * Determines all ancestors (including itself) that are a list of lists and adds them to the given buffer.
	 */
    protected final void addListOfListsAncestors(final Set<? super AlgRankingListOfLists> buffer) {
        if (this instanceof AlgRankingListOfLists) {
            buffer.add((AlgRankingListOfLists) this);
        }
        for (final Proxy<AlgRankingList> parent : this.getParents()) {
            parent.get().addListOfListsAncestors(buffer);
        }
    }

	@Override
	public String toString() {
		return this.id;
	}

}
