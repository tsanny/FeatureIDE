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
package de.ovgu.featureide.ui.visualization;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

import de.ovgu.featureide.core.IFeatureProject;
import de.ovgu.featureide.fm.core.base.FeatureUtils;
import de.ovgu.featureide.fm.core.base.IFeature;
import de.ovgu.featureide.fm.core.base.impl.ConfigFormatManager;
import de.ovgu.featureide.fm.core.configuration.Configuration;
import de.ovgu.featureide.fm.core.filter.FeatureSetFilter;
import de.ovgu.featureide.fm.core.filter.HiddenFeatureFilter;
import de.ovgu.featureide.fm.core.filter.base.IFilter;
import de.ovgu.featureide.fm.core.filter.base.InverseFilter;
import de.ovgu.featureide.fm.core.filter.base.OrFilter;
import de.ovgu.featureide.fm.core.functional.Functional;
import de.ovgu.featureide.fm.core.io.manager.FeatureModelManager.FeatureModelSnapshot;
import de.ovgu.featureide.fm.core.io.manager.SimpleFileHandler;

/**
 * Configurations Analysis utils
 *
 * @author Jabier Martinez
 */
public class ConfigAnalysisUtils {

	/**
	 * Get a matrix configurations/features
	 *
	 * @param featureProject
	 * @param featureList
	 * @return boolean[][]
	 * @throws CoreException
	 */
	public static boolean[][] getConfigsMatrix(IFeatureProject featureProject, List<String> featureList) throws CoreException {
		final Collection<IFile> configs = new ArrayList<IFile>();
		// check that they are config files
		final IFolder configsFolder = featureProject.getConfigFolder();
		for (final IResource res : configsFolder.members()) {
			if (res instanceof IFile) {
				if (((IFile) res).getName().endsWith(".config")) {
					configs.add((IFile) res);
				}
			}
		}

		final boolean[][] matrix = new boolean[configs.size()][featureList.size()];
		int iconf = 0;
		for (final IFile config : configs) {
			final Configuration configuration = new Configuration(featureProject.getFeatureModel());
			SimpleFileHandler.load(Paths.get(config.getLocationURI()), configuration, ConfigFormatManager.getInstance());
			final Set<String> configFeatures = configuration.getSelectedFeatureNames();
			int ifeat = 0;
			for (final String f : featureList) {
				matrix[iconf][ifeat] = configFeatures.contains(f);
				ifeat++;
			}
			iconf++;
		}

		return matrix;
	}

	/**
	 * No core nor hidden features
	 *
	 * @param featureProject
	 * @return list of feature names
	 */
	public static List<String> getNoCoreNoHiddenFeatures(IFeatureProject featureProject) {
		final FeatureModelSnapshot snapshot = featureProject.getFeatureModelManager().getSnapshot();
		final IFilter<IFeature> coreFeatureFilter = new FeatureSetFilter(snapshot.getAnalyzer().getCoreFeatures());
		final IFilter<IFeature> hiddenFeatureFilter = new HiddenFeatureFilter();
		final IFilter<IFeature> noCoreNoHiddenFilter = new InverseFilter<>(new OrFilter<>(Arrays.asList(hiddenFeatureFilter, coreFeatureFilter)));
		return Functional.mapToList(snapshot.getObject().getFeatures(), noCoreNoHiddenFilter, FeatureUtils.GET_FEATURE_NAME);
	}
}
