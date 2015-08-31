/* FeatureIDE - A Framework for Feature-Oriented Software Development
 * Copyright (C) 2005-2015  FeatureIDE team, University of Magdeburg, Germany
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
package de.ovgu.featureide.fm.ui.editors.featuremodel.operations;

import static de.ovgu.featureide.fm.core.localization.StringTable.DELETE;

import java.util.LinkedList;

import de.ovgu.featureide.fm.core.base.FeatureUtils;
import de.ovgu.featureide.fm.core.base.IConstraint;
import de.ovgu.featureide.fm.core.base.IFeature;
import de.ovgu.featureide.fm.core.base.IFeatureModel;
import de.ovgu.featureide.fm.ui.FMUIPlugin;

/**
 * Operation to delete a feature from the model.
 * 
 * @author Fabian Benduhn
 */
public class FeatureDeleteOperation extends AbstractFeatureModelOperation {

	private IFeature feature;
	private IFeature oldParent;
	private int oldIndex;
	private LinkedList<IFeature> oldChildren;
	private boolean deleted = false;
	private IFeature replacement;

	public FeatureDeleteOperation(IFeatureModel featureModel, IFeature feature) {
		super(featureModel, DELETE);
		this.feature = feature;
		this.replacement = null;
	}

	public FeatureDeleteOperation(IFeatureModel featureModel, IFeature feature, IFeature replacement) {
		super(featureModel, DELETE);
		this.feature = feature;
		this.replacement = replacement;
	}

	@Override
	protected void redo() {
		feature = featureModel.getFeature(feature.getName());
		oldParent = feature.getStructure().getParent().getFeature();
		if (oldParent != null) {
			oldIndex = oldParent.getStructure().getChildIndex(feature.getStructure());
		}
		oldChildren = new LinkedList<IFeature>();
		oldChildren.addAll(FeatureUtils.convertToFeatureList(feature.getStructure().getChildren()));

		if (oldParent != null) {
			oldParent = featureModel.getFeature(oldParent.getName());
		}
		LinkedList<IFeature> oldChildrenCopy = new LinkedList<IFeature>();

		for (IFeature f : oldChildren) {
			if (!f.getName().equals(feature.getName())) {
				IFeature oldChild = featureModel.getFeature(f.getName());
				oldChildrenCopy.add(oldChild);
			}
		}

		oldChildren = oldChildrenCopy;
		if (feature == featureModel.getStructure().getRoot()) {
			featureModel.getStructure().replaceRoot(featureModel.getStructure().getRoot().removeLastChild());
			deleted = true;
		} else {
			deleted = featureModel.deleteFeature(feature);
		}

		//Replace feature name in constraints
		if (replacement != null) {
			for (IConstraint c : featureModel.getConstraints()) {
				if (c.getContainedFeatures().contains(feature)) {
					c.getNode().replaceFeature(feature, replacement);
				}
			}
		}
	}

	@Override
	protected void undo() {
		try {
			if (!deleted) {
				return;
			}

			if (oldParent != null) {
				oldParent = featureModel.getFeature(oldParent.getName());
			}
			LinkedList<IFeature> oldChildrenCopy = new LinkedList<IFeature>();

			for (IFeature f : oldChildren) {
				if (!f.getName().equals(feature.getName())) {
					IFeature child = featureModel.getFeature(f.getName());
					if (child != null && child.getStructure().getParent() != null) {
						child.getStructure().getParent().removeChild(child.getStructure());
					}
					oldChildrenCopy.add(child);
				}
			}

			oldChildren = oldChildrenCopy;

			feature.getStructure().setChildren(FeatureUtils.convertToFeatureStructureList(oldChildren));
			if (oldParent != null) {
				oldParent.getStructure().addChildAtPosition(oldIndex, feature.getStructure());
			} else {
				featureModel.getStructure().setRoot(feature.getStructure());
			}
			featureModel.addFeature(feature);

			//Replace Featurename in Constraints
			if (replacement != null) {
				for (IConstraint c : featureModel.getConstraints()) {
					if (c.getContainedFeatures().contains(replacement)) {
						c.getNode().replaceFeature(replacement, feature);
					}
				}
			}
		} catch (Exception e) {
			FMUIPlugin.getDefault().logError(e);
		}
	}

	@Override
	public boolean canUndo() {
		return oldParent == null || featureModel.getFeature(oldParent.getName()) != null;
	}
}
