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
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;

public class PrioritizationTest {

	private static Matcher<List<? extends RankingElement>> toIDs(final Matcher<List<String>> idMatcher) {
		return new TypeSafeMatcher<List<? extends RankingElement>>() {
			@Override
			protected boolean matchesSafely(List<? extends RankingElement> item) {
				final List<String> ids = this.toIDs(item);
				return idMatcher.matches(ids);
			}
			private List<String> toIDs(List<? extends RankingElement> item) {
				final List<String> ids = new ArrayList<>();
				for (final RankingElement e : item) {
					ids.add(e.getID());
				}
				return ids;
			}
		    @Override
		    public final void describeTo(Description description) {
		    	description.appendText("ranking element's IDs");
		    	idMatcher.describeTo(description);
		    }
		    @Override
			protected void describeMismatchSafely(List<? extends RankingElement> item, Description mismatchDescription) {
		    	idMatcher.describeMismatch(this.toIDs(item), mismatchDescription);
		    }
		};
	}

	private static Matcher<List<? extends RankingElement>> isRanking(String... expectedStoryIDs) {
		final Matcher<List<String>> idMatcher = equalTo(Arrays.asList(expectedStoryIDs));
		return toIDs(idMatcher);
	}

	private static Fraction roundSize(int size) {
		return new Fraction(size, 1);
	}

	private static Fraction defaultSettings() {
		return roundSize(2);
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
		assertThat(model.calculateStoryRanking(defaultSettings()),
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
		assertThat(model.calculateStoryRanking(defaultSettings()),
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
		assertThat(model.calculateStoryRanking(defaultSettings()),
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
			.startList("lges")
			.addElement("l1", 50)
			.addElement("l2", 50)
			.endList()
			.getList("lges");
		assertThat(model.calculateStoryRanking(roundSize(4)),
				isRanking("S-1", "S-4", "S-5", "S-2", "S-6", "S-3"));
	}
}
