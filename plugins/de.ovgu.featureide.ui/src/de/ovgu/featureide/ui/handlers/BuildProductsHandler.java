/* FeatureIDE - A Framework for Feature-Oriented Software Development
 * Copyright (C) 2005-2019  FeatureIDE team, University of Magdeburg, Germany
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
package de.ovgu.featureide.ui.handlers;

import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.PlatformUI;

import de.ovgu.featureide.core.IFeatureProject;
import de.ovgu.featureide.ui.actions.generator.BuildProductsWizard;
import de.ovgu.featureide.ui.actions.generator.IConfigurationBuilderBasics;
import de.ovgu.featureide.ui.handlers.base.AFeatureProjectHandler;

/**
 * Builds T-Wise configurations with SPLCATool for a selected feature project.
 *
 * @author Jens Meinicke
 * @author Sebastian Krieter
 */
public class BuildProductsHandler extends AFeatureProjectHandler implements IConfigurationBuilderBasics {

	@Override
	protected void singleAction(IFeatureProject featureProject) {
		final BuildProductsWizard wizard = new BuildProductsWizard(featureProject);
		final WizardDialog dialog = new WizardDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), wizard);
		dialog.create();
		dialog.open();
	}
}
