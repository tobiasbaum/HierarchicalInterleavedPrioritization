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
import java.util.List;


public class AlgProxy<T extends AlgRankingElement> extends Proxy<T> {

    private final Proxy<? extends RankingComponent> component;
    private final AlgModel model;

    public AlgProxy(final Proxy<? extends RankingComponent> e, final AlgModel algModel) {
        this.component = e;
        this.model = algModel;
    }

    @Override
    public T get() {
        if (this.model.contains(this.component.getID())) {
            return (T) this.model.get(this.component.getID());
        }

        if (this.component.get() instanceof Story) {
            return (T) this.mapStory();
        } else {
            return (T) this.mapList();
        }
    }

    private AlgRankingElement mapList() {
        final RankingList l = (RankingList) this.component.get();

        if (l.getElements().isEmpty() || l.getElements().get(0).get() instanceof Story) {
            return new AlgRankingListOfStories(
                    this.model,
                    this.component.getID(),
                    l,
                    this.mapParents(l),
                    this.mapStoryChildren(l),
                    l.getExclusionGroup());
        } else {
            return new AlgRankingListOfLists(
                    this.model,
                    this.component.getID(),
                    l,
                    this.mapParents(l),
                    this.mapListEntryChildren(l),
                    l.getExclusionGroup(),
                    l.getRoundSize(),
                    this.model.getBalancesService());
        }
    }

    private List<AlgRankingListEntry> mapListEntryChildren(final RankingList l) {
        final List<AlgRankingListEntry> children = new ArrayList<>();
        for (final Proxy<? extends RankingComponent> e : l.getElements()) {
            final Proxy<AlgRankingList> algE = new AlgProxy<>(e, this.model);
            children.add(new AlgRankingListEntry(algE, new Fraction(l.getWeightInPercent(e.getID()), 100)));
        }
        return children;
    }

    private List<Proxy<AlgStory>> mapStoryChildren(final RankingList l) {
        final List<Proxy<AlgStory>> children = new ArrayList<>();
        for (final Proxy<? extends RankingComponent> e : l.getElements()) {
            final Proxy<AlgStory> algE = new AlgProxy<>(e, this.model);
            children.add(algE);
        }
        return children;
    }

    private List<Proxy<AlgRankingList>> mapParents(final RankingComponent l) {
        final List<Proxy<AlgRankingList>> parents = new ArrayList<>();
        for (final Proxy<? extends RankingList> e : l.getParents()) {
            final Proxy<AlgRankingList> algE = new AlgProxy<>(e, this.model);
            parents.add(algE);
        }
        return parents;
    }

    private AlgStory mapStory() {
        final Story s = (Story) this.component.get();
        return new AlgStory(this.model, s.getID(), s, this.mapParents(s), s.getStoryPoints());
    }

    @Override
    public String getID() {
        return this.component.getID();
    }

}
