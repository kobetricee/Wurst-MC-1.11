/*
 * Copyright � 2014 - 2017 | Wurst-Imperium | All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package net.wurstclient.gui.options.keybinds;

import java.io.IOException;
import java.util.Map.Entry;
import java.util.Arrays;
import java.util.TreeSet;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNo;
import net.wurstclient.WurstClient;
import net.wurstclient.files.ConfigFiles;
import net.wurstclient.options.KeybindManager;

public final class GuiKeybindManager extends GuiScreen
{
	private final GuiScreen prevScreen;
	
	private GuiKeybindList listGui;
	private GuiButton addButton;
	private GuiButton editButton;
	private GuiButton removeButton;
	private GuiButton backButton;
	
	public GuiKeybindManager(GuiScreen prevScreen)
	{
		this.prevScreen = prevScreen;
	}
	
	@Override
	public void initGui()
	{
		listGui = new GuiKeybindList(mc, this);
		listGui.registerScrollButtons(7, 8);
		listGui.elementClicked(-1, false, 0, 0);
		
		buttonList.add(addButton =
			new GuiButton(0, width / 2 - 102, height - 52, 100, 20, "Add"));
		buttonList.add(editButton =
			new GuiButton(1, width / 2 + 2, height - 52, 100, 20, "Edit"));
		buttonList.add(removeButton =
			new GuiButton(2, width / 2 - 102, height - 28, 100, 20, "Remove"));
		buttonList.add(backButton =
			new GuiButton(3, width / 2 + 2, height - 28, 100, 20, "Back"));
		buttonList.add(new GuiButton(4, 8, 8, 100, 20, "Reset Keybinds"));
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException
	{
		if(!button.enabled)
			return;
		
		switch(button.id)
		{
			case 0:
			mc.displayGuiScreen(new GuiKeybindChange(this, null));
			break;
			
			case 1:
			Entry<String, TreeSet<String>> entry =
				WurstClient.INSTANCE.keybinds.entrySet().toArray(
					new Entry[WurstClient.INSTANCE.keybinds.size()])[listGui
						.getSelectedSlot()];
			mc.displayGuiScreen(new GuiKeybindChange(this, entry));
			break;
			
			case 2:
			Entry<String, String> entry1 =
				WurstClient.INSTANCE.keybinds.entrySet().toArray(
					new Entry[WurstClient.INSTANCE.keybinds.size()])[listGui
						.getSelectedSlot()];
			WurstClient.INSTANCE.keybinds.remove(entry1.getKey());
			ConfigFiles.KEYBINDS.save();
			break;
			
			case 3:
			// force-add GUI keybind if missing
			if(!WurstClient.INSTANCE.keybinds
				.containsValue(new TreeSet<>(Arrays.asList(".t navigator"))))
			{
				WurstClient.INSTANCE.keybinds.put("LCONTROL", ".t navigator");
				ConfigFiles.KEYBINDS.save();
			}
			
			mc.displayGuiScreen(prevScreen);
			break;
			
			case 4:
			mc.displayGuiScreen(new GuiYesNo(this,
				"Are you sure you want to reset your keybinds?",
				"This cannot be undone!", 0));
			break;
		}
	}
	
	@Override
	public void confirmClicked(boolean confirmed, int id)
	{
		if(confirmed)
		{
			WurstClient.INSTANCE.keybinds = new KeybindManager();
			ConfigFiles.KEYBINDS.save();
		}
		
		mc.displayGuiScreen(this);
	}
	
	@Override
	public void handleMouseInput() throws IOException
	{
		super.handleMouseInput();
		listGui.handleMouseInput();
	}
	
	@Override
	protected void mouseClicked(int x, int y, int button) throws IOException
	{
		if(y >= 36 && y <= height - 57)
			if(x >= width / 2 + 140 || x <= width / 2 - 126)
				listGui.elementClicked(-1, false, 0, 0);
			
		super.mouseClicked(x, y, button);
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException
	{
		if(keyCode == Keyboard.KEY_RETURN)
			actionPerformed(editButton.enabled ? editButton : addButton);
		else if(keyCode == Keyboard.KEY_ESCAPE)
			actionPerformed(backButton);
	}
	
	@Override
	public void updateScreen()
	{
		boolean enabled = listGui.getSelectedSlot() > -1
			&& listGui.getSelectedSlot() < listGui.getSize();
		
		editButton.enabled = enabled;
		removeButton.enabled = enabled;
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		drawDefaultBackground();
		listGui.drawScreen(mouseX, mouseY, partialTicks);
		
		drawCenteredString(fontRendererObj, "Keybind Manager", width / 2, 8,
			0xffffff);
		drawCenteredString(fontRendererObj, "Keybinds: " + listGui.getSize(),
			width / 2, 20, 0xffffff);
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
}
