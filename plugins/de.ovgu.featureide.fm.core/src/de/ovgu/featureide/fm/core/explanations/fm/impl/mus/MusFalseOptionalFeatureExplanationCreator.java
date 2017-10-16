/* FeatureIDE - A Framework for Feature-Oriented Software Development
 * Copyright (C) 2005-2017  FeatureIDE team, University of Magdeburg, Germany
 *
 * This file is part of FeatureIDE.
 *
 * FeatureIDE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FeatureIDE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FeatureIDE.  If not, see <http://www.gnu.org/licenses/>.
 *
 * See http://featureide.cs.ovgu.de/ for further information.
 */
package de.ovgu.featureide.fm.core.explanations.fm.impl.mus;

import java.util.Set;

import org.prop4j.explain.solvers.MusExtractor;

import de.ovgu.featureide.fm.core.base.FeatureUtils;
import de.ovgu.featureide.fm.core.base.IFeature;
import de.ovgu.featureide.fm.core.base.IFeatureModel;
import de.ovgu.featureide.fm.core.explanations.fm.FalseOptionalFeatureExplanation;
import de.ovgu.featureide.fm.core.explanations.fm.FalseOptionalFeatureExplanationCreator;

/**
 * Implementation of {@link FalseOptionalFeatureExplanationCreator} using a {@link MusExtractor MUS extractor}.
 *
 * @author Timo G&uuml;nther
 */
public class MusFalseOptionalFeatureExplanationCreator extends MusFeatureModelExplanationCreator implements FalseOptionalFeatureExplanationCreator {

	/** The false-optional feature in the feature model. */
	private IFeature falseOptionalFeature;

	/**
	 * Constructs a new instance of this class.
	 */
	public MusFalseOptionalFeatureExplanationCreator() {
		this(null);
	}

	/**
	 * Constructs a new instance of this class.
	 *
	 * @param fm the feature model context
	 */
	public MusFalseOptionalFeatureExplanationCreator(IFeatureModel fm) {
		this(fm, null);
	}

	/**
	 * Constructs a new instance of this class.
	 *
	 * @param fm the feature model context
	 * @param falseOptionalFeature the false-optional feature in the feature model
	 */
	public MusFalseOptionalFeatureExplanationCreator(IFeatureModel fm, IFeature falseOptionalFeature) {
		super(fm);
		setFalseOptionalFeature(falseOptionalFeature);
	}

	@Override
	public IFeature getFalseOptionalFeature() {
		return falseOptionalFeature;
	}

	@Override
	public void setFalseOptionalFeature(IFeature falseOptionalFeature) {
		this.falseOptionalFeature = falseOptionalFeature;
	}

	@Override
	public FalseOptionalFeatureExplanation getExplanation() throws IllegalStateException {
		final MusExtractor oracle = getOracle();
		final FalseOptionalFeatureExplanation explanation;
		oracle.push();
		try {
			oracle.addAssumption(getFalseOptionalFeature().getName(), false);
			oracle.addAssumption(FeatureUtils.getParent(getFalseOptionalFeature()).getName(), true);
			explanation = getExplanation(oracle.getMinimalUnsatisfiableSubsetIndexes());
		} finally {
			oracle.pop();
		}
		return explanation;
	}

	@Override
	protected FalseOptionalFeatureExplanation getExplanation(Set<Integer> clauseIndexes) {
		return (FalseOptionalFeatureExplanation) super.getExplanation(clauseIndexes);
	}

	@Override
	protected FalseOptionalFeatureExplanation getConcreteExplanation() {
		return new FalseOptionalFeatureExplanation(getFalseOptionalFeature());
	}
}
