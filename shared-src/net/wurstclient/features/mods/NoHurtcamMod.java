/*
 * Copyright � 2014 - 2017 | Wurst-Imperium | All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package net.wurstclient.features.mods;

@Mod.Info(tags = "no hurtcam, no hurt cam", help = "Mods/NoHurtcam")
@Mod.Bypasses
public final class NoHurtcamMod extends Mod
{
	public NoHurtcamMod()
	{
		super("NoHurtcam", "Disables the annoying effect when you get hurt.");
	}
}
