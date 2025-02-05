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
package de.ovgu.featureide.fm.ui.editors.featuremodel;

import org.eclipse.draw2d.LineBorder;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;

import de.ovgu.featureide.fm.core.preferences.DarkModePreference;

/**
 * Implements some basic graphical methods.
 *
 * @author Thomas Thuem
 */
public class GUIBasics {

	public static Color createColor(int r, int g, int b) {
		return new Color(null, r, g, b);
	}

	public static Color createColor(double r, double g, double b) {
		return new Color(null, (int) (r * 255), (int) (g * 255), (int) (b * 255));
	}

	public static Color invertColor(Color color) {
		return invertColor(color, 1);
	}

	/**
	 * Inverts a given color.
	 *
	 * @param color The color to invert
	 * @param damping Damping factor must be between 0 and 1
	 * @return Inverted color
	 */
	public static Color invertColor(Color color, float damping) {
		if (color == null) {
			return null;
		}

		if (damping > 1) {
			damping = 1;
		} else if (damping < 0) {
			damping = 0;
		}

		final int r = (int) ((255 - color.getRed()) * damping);
		final int g = (int) ((255 - color.getGreen()) * damping);
		final int b = (int) ((255 - color.getBlue()) * damping);

		return new Color(null, r, g, b);
	}

	public static Color invertColorOnDarkTheme(Color color) {
		return DarkModePreference.getInstance().get() ? invertColor(color) : color;
	}

	public static Color invertColorOnDarkTheme(Color color, float damping) {
		return DarkModePreference.getInstance().get() ? invertColor(color, damping) : color;
	}

	public static Color createBorderColor(Color color) {
		final int r = (int) (color.getRed() * 0.75);
		final int g = (int) (color.getGreen() * 0.75);
		final int b = (int) (color.getBlue() * 0.75);
		return new Color(null, r, g, b);
	}

	public static LineBorder createLineBorder(Color color, int width, int style) {
		return new LineBorder(color, width, style);
	}

	public static LineBorder createLineBorder(Color color, int width) {
		return new LineBorder(color, width);
	}

	public static boolean unicodeStringTest(Font swtFont, String s) {
		final FontData fd = swtFont.getFontData()[0];
		final java.awt.Font awtFont = new java.awt.Font(fd.getName(), 0, fd.getHeight());
		for (int i = 0; i < s.length(); i++) {
			if (!awtFont.canDisplay(s.charAt(i))) {
				return false;
			}
		}
		return true;
	}
}
