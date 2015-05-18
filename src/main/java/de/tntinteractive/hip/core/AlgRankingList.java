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


public abstract class AlgRankingList extends AlgRankingElement {

	private Fraction balance;

	public AlgRankingList(AlgModel algModel, String id) {
		super(algModel, id);
		this.balance = new Fraction(0, 1);
	}

	public AlgStory startNextStory(Fraction storyPointsForRound) {
		if (this.isEmpty()) {
			//can only happen if there is no work left to do
			return null;
		}

		final AlgStory story = this.getFirstFittingStory(storyPointsForRound);
		//TEST
		System.out.println("chose for start " + story);
		story.start();
		//TODO: Nach dem Start nochmal dafür sorgen, dass nicht alle am Anschlag sind (falls beim nächsten Aufruf
		//  der relevantPrefix länger wird
		return story;
	}

	protected abstract boolean isEmpty();

	/**
	 * Returns the highest ranked story that fits still fits into the lists limits given its current balance.
	 * When the balance is zero or negative, even a larger story is allowed (so that higher ranked stories will
	 * get their way before lower ranked ones).
	 * @pre !this.isEmpty()
	 */
	protected abstract AlgStory getFirstFittingStory(Fraction storyPointsForRound);

	public void propagateCosts(Fraction costs) {
		this.balance = this.balance.add(costs);
		System.out.println("balance of " + this.getID() + " is now " + this.balance);
		//TODO propagieren der Kosten nach oben (und korrektes aufteilen)
	}

	public abstract void removeChild(AlgRankingElement toRemove);

	public void subtractFromBalance(Fraction value) {
		this.balance = this.balance.subtract(value);
		System.out.println("balance of " + this.getID() + " is now " + this.balance);
	}

	public Fraction getBalance() {
		return this.balance;
	}
}
