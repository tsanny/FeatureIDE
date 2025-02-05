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
package de.ovgu.featureide.fm.core.io.manager;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;

import de.ovgu.featureide.fm.core.io.EclipseFileSystem;

/**
 * Informs {@link AFileManager file managers} about a file change.
 *
 * @author Sebastian Krieter
 */
public class EclipseFileChangeVisitor implements IResourceDeltaVisitor {

	@Override
	public boolean visit(IResourceDelta delta) {
		if (((delta.getKind() == IResourceDelta.CHANGED) && ((delta.getFlags() & (IResourceDelta.CONTENT | IResourceDelta.REPLACED)) != 0))) {
			final IResource resource = delta.getResource();
			if (resource instanceof IFile) {
				final IFileManager<?> instance = AFileManager.getInstance(EclipseFileSystem.getPath(resource));
				if (instance != null) {
					instance.read();
				}
			}
		}
		return true;
	}
}
