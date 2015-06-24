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
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class AlgRankingListOfLists extends AlgRankingList {

    private final List<AlgRankingListEntry> elements;
    private final Fraction roundSize;
    private final BalancesService balancesService;

    public AlgRankingListOfLists(
            final AlgModel algModel,
            final String id,
            final RankingComponent correspondingElement,
            final List<Proxy<AlgRankingList>> parents,
            final List<AlgRankingListEntry> children,
            final String exclusionGroupId,
            final int roundSize,
            final BalancesService balancesService) {
        super(algModel, id, correspondingElement, parents, exclusionGroupId);
        assert !children.isEmpty();

        this.elements = children;
        this.roundSize = new Fraction(roundSize, 1);
        this.balancesService = balancesService;
    }

    @Override
    protected boolean isEmpty() {
        return this.elements.isEmpty();
    }

    @Override
    protected Proxy<AlgStory> getFirstFittingStory() {
        final Map<Proxy<AlgRankingList>, Fraction> relevantPrefix = this.determineRelevantPrefix();
        final Balances b = this.balancesService.getFor(this.getID());
        b.stripDown(relevantPrefix, this.roundSize);
        for (final Entry<Proxy<AlgRankingList>, Fraction> e : relevantPrefix.entrySet()) {
            if (b.thereIsStillRoom(e, this.roundSize)) {
                return e.getKey().get().getFirstFittingStory();
            }
        }
        throw new RuntimeException("should not happen");
    }

    @Override
    public void removeChild(final Proxy<AlgRankingElement> toRemove) {
        final Iterator<AlgRankingListEntry> iter = this.elements.iterator();
        while (iter.hasNext()) {
            final AlgRankingListEntry e = iter.next();
            if (e.getItem().equals(toRemove)) {
                iter.remove();
            }
        }
        if (this.elements.isEmpty()) {
            this.removeFromAllParents();
        }
    }

    /**
     * Returns the lists prefix which's weight sums up to 100 percent.
     * The returned map is ordered by relevance, and the values always sum up to 1 (aka 100%).
     */
    public Map<Proxy<AlgRankingList>, Fraction> determineRelevantPrefix() {
        final Map<Proxy<AlgRankingList>, Fraction> ret = new LinkedHashMap<>();
        Fraction sum = Fraction.ZERO;
        final Iterator<AlgRankingListEntry> iter = this.elements.iterator();
        while (iter.hasNext() && sum.compareTo(Fraction.ONE) < 0) {
            final AlgRankingListEntry e = iter.next();
            if (e.getItem().get().isInSameExclusionGroupAsOneOf(ret.keySet())) {
                continue;
            }
            if (e.getItem().get().isEmpty()) {
                continue;
            }
            ret.put(e.getItem(), e.getWeight());
            sum = sum.add(e.getWeight());
        }
        return this.adjustLastWeight(ret);
    }

    private Map<Proxy<AlgRankingList>, Fraction> adjustLastWeight(
            final Map<Proxy<AlgRankingList>, Fraction> ret) {
        final Fraction sum = Fraction.sum(ret.values());
        if (sum.compareTo(Fraction.ONE) > 0) {
            final Proxy<AlgRankingList> lastKey = this.determineLastKey(ret);
            ret.put(lastKey, ret.get(lastKey).subtract(sum.subtract(Fraction.ONE)));
        } else {
            final Proxy<AlgRankingList> firstKey = ret.keySet().iterator().next();
            ret.put(firstKey, ret.get(firstKey).subtract(sum.subtract(Fraction.ONE)));
        }
        return ret;
    }

    private Proxy<AlgRankingList> determineLastKey(final Map<Proxy<AlgRankingList>, Fraction> ret) {
        final Iterator<Proxy<AlgRankingList>> iter = ret.keySet().iterator();
        while (true) {
            final Proxy<AlgRankingList> l = iter.next();
            if (!iter.hasNext()) {
                return l;
            }
        }
    }

    /**
     * Adds the given story's story points to the balances of the child lists that wanted it. If this applies to
     * multiple children, the story points are split (taking into account how much each of the children wanted it).
     *
     * @pre The story is contained in at least one child list.
     */
    public void adjustBalances(final AlgStory story) {
        final Map<Proxy<AlgRankingList>, Fraction> wanting = new HashMap<>();
        for (final AlgRankingListEntry e : this.elements) {
            final Fraction wantingDegree = e.getItem().get().getWantingDegree(new FakeProxy<>(story, story.getID()));
            if (!wantingDegree.equals(Fraction.ZERO)) {
                wanting.put(e.getItem(), wantingDegree);
            }
        }

        final Fraction sum = Fraction.sum(wanting.values());
        final Balances b = this.balancesService.getFor(this.getID()).copy();
        for (final Entry<Proxy<AlgRankingList>, Fraction> wantingEntry : wanting.entrySet()) {
            b.adjustBalance(
                    wantingEntry.getKey(),
                    wantingEntry.getValue().divide(sum).multiply(story.getStoryPoints()));
        }
        b.stripDown(this.determineRelevantPrefix(), this.roundSize);
        this.balancesService.save(this.getID(), b);
    }

    @Override
    public Fraction getWantingDegree(final Proxy<AlgStory> story) {
        //this could be much more sophisticated (e.g. taking the weights into account)
        Fraction max = Fraction.ZERO;
        for (int i = 0; i < this.elements.size(); i++) {
            final Fraction curElementWantingDegree = this.elements.get(i).getItem().get().getWantingDegree(story);
            final Fraction curUpperLimit = new Fraction(this.elements.size() - 1, this.elements.size());
            max = Fraction.max(max, Fraction.min(curElementWantingDegree, curUpperLimit));
        }
        return max;
    }

    @Override
    public void removeEmptyDescendants() {
        final Iterator<AlgRankingListEntry> iter = this.elements.iterator();
        while (iter.hasNext()) {
            final AlgRankingList e = iter.next().getItem().get();
            e.removeEmptyDescendants();
            if (e.isEmpty()) {
                iter.remove();
            }
        }
    }
}
