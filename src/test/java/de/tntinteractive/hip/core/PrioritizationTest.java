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

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;

public class PrioritizationTest {

	private static Matcher<List<? extends RankingComponent>> toIDs(final Matcher<List<String>> idMatcher) {
		return new TypeSafeMatcher<List<? extends RankingComponent>>() {
			@Override
			protected boolean matchesSafely(final List<? extends RankingComponent> item) {
				final List<String> ids = this.toIDs(item);
				return idMatcher.matches(ids);
			}
			private List<String> toIDs(final List<? extends RankingComponent> item) {
				final List<String> ids = new ArrayList<>();
				for (final RankingComponent e : item) {
					ids.add(e.getID());
				}
				return ids;
			}
		    @Override
		    public final void describeTo(final Description description) {
		    	description.appendText("ranking element's IDs");
		    	idMatcher.describeTo(description);
		    }
		    @Override
			protected void describeMismatchSafely(final List<? extends RankingComponent> item, final Description mismatchDescription) {
		    	idMatcher.describeMismatch(this.toIDs(item), mismatchDescription);
		    }
		};
	}

	private static Matcher<Map<? extends RankingComponent, Fraction>> toIDsAndWeights(final Matcher<List<String>> idMatcher) {
		return new TypeSafeMatcher<Map<? extends RankingComponent, Fraction>>() {
			@Override
			protected boolean matchesSafely(final Map<? extends RankingComponent, Fraction> item) {
				final List<String> ids = this.toIDs(item);
				return idMatcher.matches(ids);
			}
			private List<String> toIDs(final Map<? extends RankingComponent, Fraction> item) {
				final List<String> ids = new ArrayList<>();
				for (final Entry<? extends RankingComponent, Fraction> e : item.entrySet()) {
					ids.add(e.getKey().getID());
					ids.add(Integer.toString(e.getValue().multiply(new Fraction(100, 1)).intValue()));
				}
				return ids;
			}
		    @Override
		    public final void describeTo(final Description description) {
		    	description.appendText("ranking element's IDs and weights");
		    	idMatcher.describeTo(description);
		    }
		    @Override
			protected void describeMismatchSafely(
					final Map<? extends RankingComponent, Fraction> item, final Description mismatchDescription) {
		    	idMatcher.describeMismatch(this.toIDs(item), mismatchDescription);
		    }
		};
	}

	private static Matcher<List<? extends RankingComponent>> isRanking(final String... expectedStoryIDs) {
		final Matcher<List<String>> idMatcher = equalTo(Arrays.asList(expectedStoryIDs));
		return toIDs(idMatcher);
	}

	private static Matcher<Map<? extends RankingComponent, Fraction>> isPrefixWithWeights(
			final String... expectedStoryIDsAndWeights) {
		final Matcher<List<String>> idMatcher = equalTo(Arrays.asList(expectedStoryIDsAndWeights));
		return toIDsAndWeights(idMatcher);
	}

	@Test
	public void testSimpleList() {
		final RankingList model = new ModelBuilder()
			.addList(
				"liste",
				"S-1",
				"S-2",
				"S-3")
			.getList("liste");
		assertThat(model.calculateStoryRanking(new DummyBalancesService()),
				isRanking("S-1", "S-2", "S-3"));
	}

	@Test
	public void testSimpleChainedLists() {
		final RankingList model = new ModelBuilder()
			.addList(
				"l1",
				"S-1",
				"S-2",
				"S-3")
			.addList(
				"l2",
				"S-4",
				"S-5",
				"S-6")
			.addList(
				"lges",
				"l2",
				"l1")
			.getList("lges");
		assertThat(model.calculateStoryRanking(new DummyBalancesService()),
				isRanking("S-4", "S-5", "S-6", "S-1", "S-2", "S-3"));
	}

	@Test
	public void testSimpleInterleavedLists() {
		final RankingList model = new ModelBuilder()
			.addList(
				"l1",
				"S-1",
				"S-2",
				"S-3")
			.addList(
				"l2",
				"S-4",
				"S-5",
				"S-6")
			.startList("lges")
			.addElement("l1", 50)
			.addElement("l2", 50)
			.endList()
			.getList("lges");
		assertThat(model.calculateStoryRanking(new DummyBalancesService()),
				isRanking("S-1", "S-4", "S-2", "S-5", "S-3", "S-6"));
	}

	@Test
	public void testStoriesWithDifferentSizes() {
		final RankingList model = new ModelBuilder()
			.startList("l1")
			.addStory("S-1", 2)
			.addStory("S-2", 2)
			.addStory("S-3", 2)
			.endList()
			.startList("l2")
			.addStory("S-4", 1)
			.addStory("S-5", 1)
			.addStory("S-6", 1)
			.endList()
			.startList("lges", 4)
			.addElement("l1", 50)
			.addElement("l2", 50)
			.endList()
			.getList("lges");
		assertThat(model.calculateStoryRanking(new DummyBalancesService()),
				isRanking("S-1", "S-4", "S-5", "S-2", "S-6", "S-3"));
	}

	@Test
	public void testFullyAfter() {
		final RankingList model = new ModelBuilder()
			.startList("l1")
			.addStory("S-1", 1)
			.addStory("S-2", 1)
			.addStory("S-3", 1)
            .exclusionGroup("exc")
			.endList()
			.startList("l2")
			.addStory("S-4", 1)
			.endList()
			.startList("l3")
			.addStory("S-5", 1)
			.addStory("S-6", 1)
			.addStory("S-7", 1)
            .exclusionGroup("exc")
			.endList()
			.startList("lges")
			.addElement("l1", 50)
			.addElement("l2", 50)
			.addElement("l3", 50)
			.endList()
			.getList("lges");
		assertThat(model.calculateStoryRanking(new DummyBalancesService()),
				isRanking("S-1", "S-4", "S-2", "S-3", "S-5", "S-6", "S-7"));
	}

	@Test
	public void testMoreComplexStructure() {
		final RankingList model = new ModelBuilder()
			.startList("Project1")
			.addStory("PSY-1", 5)
			.addStory("PSY-2", 5)
			.addStory("PSY-3", 5)
			.endList()
			.startList("Project2")
			.addStory("PSY-4", 5)
			.addStory("PSY-5", 5)
			.endList()
			.startList("Projects", 21)
			.addElement("Project1", 50)
			.addElement("Project2", 50)
			.endList()
			.startList("Impediments")
			.addStory("IM-4", 1)
			.addStory("IM-2", 13)
			.addStory("IM-3", 1)
			.addStory("IM-1", 1)
			.addStory("IM-5", 1)
			.endList()
			.startList("Bugs")
			.addStory("PSY-6", new Fraction(1, 2))
			.addStory("PSY-7", new Fraction(1, 2))
			.endList()
			.startList("lges", 30)
			.addElement("Projects", 70)
			.addElement("Impediments", 15)
			.addElement("Bugs", 15)
			.endList()
			.getList("lges");
		assertThat(model.calculateStoryRanking(new DummyBalancesService()),
				isRanking(
						"PSY-1",
						"PSY-2",
						"PSY-3",
						"PSY-4",
						"PSY-5",
						"IM-4",
						"IM-2",
						"IM-3",
						"IM-1",
						"IM-5",
						"PSY-6",
				        "PSY-7"));
	}

    @Test
    public void testWithEmptyLists() {
        final RankingList model = new ModelBuilder()
            .addList("l1", "S-1")
            .addList("l2")
            .addList("l31", "S-2", "S-3")
            .addList("l32")
            .addList("l33", "S-4")
            .addList("l3", "l31", "l32", "l33")
            .startList("lges")
            .addElement("l3", 50)
            .addElement("l2", 40)
            .addElement("l1", 30)
            .endList()
            .getList("lges");
        assertThat(model.calculateStoryRanking(new DummyBalancesService()),
                isRanking(
                        "S-2",
                        "S-3",
                        "S-1",
                        "S-4"));
    }

    @Test
    public void testMixingWithRealisticData() {
        final RankingList model = new ModelBuilder()
            .addList("l2")
            .addList("l31", "S-2", "S-3")
            .addList("l32")
            .addList("l33", "S-4")
            .addList("l3", "l31", "l32", "l33")

            .addList("kritische Bugs")
            .addList("Einzelthemen für Kunden")
            .addList("unkritische Bugs")

            .startList("Prio übergeordnet", 15)
            .addElement("kritische Bugs", 100)
            .addElement("Projekte", 60)
            .addElement("Einzelthemen für Kunden", 10)
            .addElement("Usability", 10)
            .addElement("Impediments", 10)
            .addElement("unkritische Bugs", 5)
            .addElement("strategische Themen", 5)
            .endList()
            .getList("Prio übergeordnet");

//        Prio übergeordnet
//            kritische Bugs (100%; Zählerstand 0; 0 offene Einträge)
//            Projekte (60%; Zählerstand 0)
//                Projekt SPS Schweiz - Meilenstein 1 (40%; Zählerstand 0; 14 offene Einträge)
//                Projekt VWFS (30%; Zählerstand 0; 16 offene Einträge)
//                P2 für Ricoh (30%; Zählerstand 0; 5 offene Einträge)
//                Storage-Sicherheit (50%; Zählerstand 0; 8 offene Einträge)
//                Umstellung Otto - Meilenstein 1 (50%; Zählerstand 0; 0 offene Einträge)
//                Maschinenstatus-Reporting (BA) (50%; Zählerstand 0; 3 offene Einträge)
//                Allianz offene Punkte (50%; Zählerstand 0; 8 offene Einträge)
//                Projekt SPS Schweiz - Meilenstein 2 (50%; Zählerstand 0; 13 offene Einträge)
//                Umstellung Otto - Meilenstein 2 (50%; Zählerstand 0; 4 offene Einträge)
//                Rest BA (50%; Zählerstand 0; 2 offene Einträge)
//                T-Systems (50%; Zählerstand 0; 49 offene Einträge)
//                Commerzbank Rest (50%; Zählerstand 0; 18 offene Einträge)
//                Umstellung KRZ Minden/Ravensberg/Lippe (50%; Zählerstand 0; 2 offene Einträge)
//            Einzelthemen für Kunden (10%; Zählerstand 0; 0 offene Einträge)
//            Usability/Qualitätsverbesserungen (10%; Zählerstand 0; 37 offene Einträge)
//            Impediments (10%; Zählerstand 0; 82 offene Einträge)
//            unkritische Bugs (5%; Zählerstand 0; 151 offene Einträge)
//            strategische Themen (5%; Zählerstand 0)
//                Staging (Guide) (100%; Zählerstand 0; 4 offene Einträge)

    }

	@Test
	public void testGetRelevantPrefixOnlyOne() {
		final RankingList model = new ModelBuilder()
			.addList("l1", "S-1")
			.addList("l2", "S-2")
			.addList(
				"lges",
				"l2",
				"l1")
			.getList("lges");
		assertThat(model.determineRelevantPrefix(),
				isPrefixWithWeights("l2", "100"));
	}

	@Test
	public void testGetRelevantPrefixTwoExact() {
		final RankingList model = new ModelBuilder()
			.addList("l1", "S-1")
			.addList("l2", "S-2")
			.startList("lges")
			.addElement("l1", 50)
			.addElement("l2", 50)
			.endList()
			.getList("lges");
		assertThat(model.determineRelevantPrefix(),
				isPrefixWithWeights("l1", "50", "l2", "50"));
	}

	@Test
	public void testGetRelevantPrefixInexactTooMuch() {
		final RankingList model = new ModelBuilder()
			.addList("l1", "S-1")
			.addList("l2", "S-2")
			.addList("l3", "S-3")
			.startList("lges")
			.addElement("l3", 50)
			.addElement("l2", 40)
			.addElement("l1", 30)
			.endList()
			.getList("lges");
		assertThat(model.determineRelevantPrefix(),
				isPrefixWithWeights("l3", "50", "l2", "40", "l1", "10"));
	}

	@Test
	public void testGetRelevantPrefixInexactTooShort() {
		final RankingList model = new ModelBuilder()
			.addList("l1", "S-1")
			.startList("lges")
			.addElement("l1", 50)
			.endList()
			.getList("lges");
		assertThat(model.determineRelevantPrefix(),
				isPrefixWithWeights("l1", "100"));
	}

	@Test
	public void testGetRelevantPrefixEmptyListIsLeftOut() {
		final RankingList model = new ModelBuilder()
			.addList("l1", "S-1")
			.addList("l2")
			.addList("l3", "S-3")
			.startList("lges")
			.addElement("l3", 50)
			.addElement("l2", 40)
			.addElement("l1", 30)
			.endList()
			.getList("lges");
		assertThat(model.determineRelevantPrefix(),
				isPrefixWithWeights("l3", "70", "l1", "30"));
	}

    @Test
    public void testGetRelevantPrefixWithFullyAfter() {
        final RankingList model = new ModelBuilder()
            .startList("l1")
            .addStory("S-1", 1)
            .exclusionGroup("e1")
            .endList()
            .startList("l2")
            .addStory("S-2", 1)
            .exclusionGroup("e1")
            .endList()
            .startList("lges")
            .addElement("l1", 50)
            .addElement("l2", 50)
            .endList()
            .getList("lges");
        assertThat(model.determineRelevantPrefix(),
                isPrefixWithWeights("l1", "100"));
    }

    @Test
    public void testStart() {
        final Story story = new ModelBuilder()
            .addList("l1", "S-1", "S-2")
            .addList("l2", "S-3")
            .startList("lges")
            .addElement("l1", 100)
            .addElement("l2", 100)
            .endList()
            .getStory("S-2");
        final TemporaryBalanceDecorator cache = new TemporaryBalanceDecorator(new DummyBalancesService());
        story.start(cache);
        assertThat(cache.getFor("l1").toStringWithoutZeroes(), is(""));
        assertThat(cache.getFor("l2").toStringWithoutZeroes(), is(""));
        assertThat(cache.getFor("lges").toStringWithoutZeroes(), is("l1;1\n"));
    }
}
