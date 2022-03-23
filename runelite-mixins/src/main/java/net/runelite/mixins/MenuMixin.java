/*
 * Copyright (c) 2019, Ron Young <https://github.com/raiyni>
 * All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *     list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *     this list of conditions and the following disclaimer in the documentation
 *     and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.runelite.mixins;

import net.runelite.api.MenuEntry;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.WidgetPressed;
import net.runelite.api.mixins.FieldHook;
import net.runelite.api.mixins.Inject;
import net.runelite.api.mixins.Mixin;
import net.runelite.api.mixins.Shadow;
import net.runelite.api.widgets.menu.MenuRow;
import net.runelite.api.widgets.menu.ContextMenu;
import net.runelite.rs.api.RSClient;
import net.runelite.rs.api.RSFont;
import net.runelite.rs.api.RSMenuAction;

@Mixin(RSClient.class)
public abstract class MenuMixin implements RSClient
{
	protected static final int MENU_BORDER_OUTER_2010 = 0x6D6A5B;
	protected static final int MENU_BORDER_INNER_2010 = 0x524A3D;
	protected static final int MENU_PADDING_2010 = 0x2B2622;
	protected static final int MENU_BACKGROUND_2010 = 0x2B271C;
	protected static final int MENU_TEXT_2010 = 0xC6B895;
	protected static final int MENU_HEADER_GRADIENT_TOP_2010 = 0x322E22;
	protected static final int MENU_HEADER_GRADIENT_BOTTOM_2010 = 0x090A04;
	protected static final int ORIGINAL_BG = 0x5D5447;

	@Shadow("client")
	private static RSClient client;

	@Shadow("tempMenuAction")
	private static RSMenuAction tempMenuAction;

	@Inject
	@Override
	public void drawAdonaiMenu(int alpha)
	{
		int x = getMenuX();
		int y = getMenuY();
		int w = getMenuWidth();
		int h = getMenuHeight();

		// Outside border
		rasterizerDrawHorizontalLineAlpha(x + 2, y, w - 4, MENU_BORDER_OUTER_2010, alpha);
		rasterizerDrawHorizontalLineAlpha(x + 2, y + h - 1, w - 4, MENU_BORDER_OUTER_2010, alpha);
		rasterizerDrawVerticalLineAlpha(x, y + 2, h - 4, MENU_BORDER_OUTER_2010, alpha);
		rasterizerDrawVerticalLineAlpha(x + w - 1, y + 2, h - 4, MENU_BORDER_OUTER_2010, alpha);

		// Padding
		rasterizerDrawRectangleAlpha(x + 1, y + 5, w - 2, h - 6, MENU_PADDING_2010, alpha);
		rasterizerDrawHorizontalLineAlpha(x + 1, y + 17, w - 2, MENU_PADDING_2010, alpha);
		rasterizerDrawCircleAlpha(x + 2, y + h - 3, 0, MENU_PADDING_2010, alpha);
		rasterizerDrawCircleAlpha(x + w - 3, y + h - 3, 0, MENU_PADDING_2010, alpha);

		// Header
		rasterizerDrawGradientAlpha(
				x + 2,
				y + 1,
				w - 4,
				16,
				MENU_HEADER_GRADIENT_TOP_2010,
				MENU_HEADER_GRADIENT_BOTTOM_2010,
				alpha,
				alpha
		);
		rasterizerFillRectangleAlpha(x + 1, y + 1, 2, 4, MENU_PADDING_2010, alpha);
		rasterizerFillRectangleAlpha(x + w - 3, y + 1, 2, 4, MENU_PADDING_2010, alpha);

		// Inside border
		rasterizerDrawHorizontalLineAlpha(x + 2, y + 18, w - 4, MENU_BORDER_INNER_2010, alpha);
		rasterizerDrawHorizontalLineAlpha(x + 3, y + h - 3, w - 6, MENU_BORDER_INNER_2010, alpha);
		rasterizerDrawVerticalLineAlpha(x + 2, y + 18, h - 21, MENU_BORDER_INNER_2010, alpha);
		rasterizerDrawVerticalLineAlpha(x + w - 3, y + 18, h - 21, MENU_BORDER_INNER_2010, alpha);

		// Options background
		rasterizerFillRectangleAlpha(x + 3, y + 19, w - 6, h - 22, MENU_BACKGROUND_2010, alpha);

		// Corner insets
		rasterizerDrawCircleAlpha(x + 1, y + 1, 0, MENU_BORDER_OUTER_2010, alpha);
		rasterizerDrawCircleAlpha(x + w - 2, y + 1, 0, MENU_BORDER_OUTER_2010, alpha);
		rasterizerDrawCircleAlpha(x + 1, y + h - 2, 0, MENU_BORDER_OUTER_2010, alpha);
		rasterizerDrawCircleAlpha(x + w - 2, y + h - 2, 0, MENU_BORDER_OUTER_2010, alpha);

		RSFont font = getFontBold12();
		font.drawTextLeftAligned("Choose Option", x + 3, y + 14, MENU_TEXT_2010, -1);

		int mouseX = getMouseX();
		int mouseY = getMouseY();

		int count = getMenuOptionCount();
		String[] targets = getMenuTargets();
		String[] options = getMenuOptions();

		MenuEntry[] menuEntries = getMenuEntries();

		for (int i = 0; i < count; i++)
		{
			int rowY = y + (count - 1 - i) * 15 + 31;

			String s = options[i];
			if (!targets[i].isEmpty())
			{
				s += " " + targets[i];
			}

			font.drawTextLeftAligned(s, x + 3, rowY, MENU_TEXT_2010, -1);

			// Highlight current option
			if (mouseX > x && mouseX < w + x && mouseY > rowY - 13 && mouseY < rowY + 3)
			{
				rasterizerFillRectangleAlpha(x + 3, rowY - 12, w - 6, 15, 0xffff00, 120);
			}
		}
	}


	@Inject
	@Override
	public void draw2010Menu(int alpha)
	{
		int x = getMenuX();
		int y = getMenuY();
		int w = getMenuWidth();
		int h = getMenuHeight();

		// Outside border
		rasterizerDrawHorizontalLineAlpha(x + 2, y, w - 4, MENU_BORDER_OUTER_2010, alpha);
		rasterizerDrawHorizontalLineAlpha(x + 2, y + h - 1, w - 4, MENU_BORDER_OUTER_2010, alpha);
		rasterizerDrawVerticalLineAlpha(x, y + 2, h - 4, MENU_BORDER_OUTER_2010, alpha);
		rasterizerDrawVerticalLineAlpha(x + w - 1, y + 2, h - 4, MENU_BORDER_OUTER_2010, alpha);

		// Padding
		rasterizerDrawRectangleAlpha(x + 1, y + 5, w - 2, h - 6, MENU_PADDING_2010, alpha);
		rasterizerDrawHorizontalLineAlpha(x + 1, y + 17, w - 2, MENU_PADDING_2010, alpha);
		rasterizerDrawCircleAlpha(x + 2, y + h - 3, 0, MENU_PADDING_2010, alpha);
		rasterizerDrawCircleAlpha(x + w - 3, y + h - 3, 0, MENU_PADDING_2010, alpha);

		// Header
		rasterizerDrawGradientAlpha(
				x + 2,
				y + 1,
				w - 4,
				16,
				MENU_HEADER_GRADIENT_TOP_2010,
				MENU_HEADER_GRADIENT_BOTTOM_2010,
				alpha,
				alpha
		);
		rasterizerFillRectangleAlpha(x + 1, y + 1, 2, 4, MENU_PADDING_2010, alpha);
		rasterizerFillRectangleAlpha(x + w - 3, y + 1, 2, 4, MENU_PADDING_2010, alpha);

		// Inside border
		rasterizerDrawHorizontalLineAlpha(x + 2, y + 18, w - 4, MENU_BORDER_INNER_2010, alpha);
		rasterizerDrawHorizontalLineAlpha(x + 3, y + h - 3, w - 6, MENU_BORDER_INNER_2010, alpha);
		rasterizerDrawVerticalLineAlpha(x + 2, y + 18, h - 21, MENU_BORDER_INNER_2010, alpha);
		rasterizerDrawVerticalLineAlpha(x + w - 3, y + 18, h - 21, MENU_BORDER_INNER_2010, alpha);

		// Options background
		rasterizerFillRectangleAlpha(x + 3, y + 19, w - 6, h - 22, MENU_BACKGROUND_2010, alpha);

		// Corner insets
		rasterizerDrawCircleAlpha(x + 1, y + 1, 0, MENU_BORDER_OUTER_2010, alpha);
		rasterizerDrawCircleAlpha(x + w - 2, y + 1, 0, MENU_BORDER_OUTER_2010, alpha);
		rasterizerDrawCircleAlpha(x + 1, y + h - 2, 0, MENU_BORDER_OUTER_2010, alpha);
		rasterizerDrawCircleAlpha(x + w - 2, y + h - 2, 0, MENU_BORDER_OUTER_2010, alpha);

		RSFont font = getFontBold12();
		font.drawTextLeftAligned("Choose Option", x + 3, y + 14, MENU_TEXT_2010, -1);

		int mouseX = getMouseX();
		int mouseY = getMouseY();

		int count = getMenuOptionCount();
		String[] targets = getMenuTargets();
		String[] options = getMenuOptions();
		for (int i = 0; i < count; i++)
		{
			int rowY = y + (count - 1 - i) * 15 + 31;

			String s = options[i];
			if (!targets[i].isEmpty())
			{
				s += " " + targets[i];
			}

			font.drawTextLeftAligned(s, x + 3, rowY, MENU_TEXT_2010, -1);

			// Highlight current option
			if (mouseX > x && mouseX < w + x && mouseY > rowY - 13 && mouseY < rowY + 3)
			{
				rasterizerFillRectangleAlpha(x + 3, rowY - 12, w - 6, 15, 0xffffff, 80);
			}
		}
	}

	/**
	 * getAdonaiMenu executes the same style of command to draw the menu itself, except it does
	 * not have the drawing. That means it gets all the data as to where the menu rows are
	 * referring to. menuEntry contains that information that connects the menu to the objects.
	 *
	 * ActionParam0 & Param1 both point to the x-y elements in the Scene Tile Array to get the object
	 */
	// project: figure out how the target is added to this.
	@Inject
	@Override
	public ContextMenu getAdonaiMenu()
	{
		int x = getMenuX();
		int y = getMenuY();
		int w = getMenuWidth();
		int h = getMenuHeight();
		ContextMenu contextMenu = new ContextMenu(x, y, w, h);
		int mouseX = getMouseX();
		int mouseY = getMouseY();
		contextMenu.setMousePositions(mouseX, mouseY);
		int count = getMenuOptionCount();
		contextMenu.setMenuOptionCount(count);

		String[] targets = getMenuTargets();
		String[] options = getMenuOptions();

		MenuEntry[] menuEntries = getMenuEntries();
		contextMenu.setMenuEntries(menuEntries);
		for (int i = 0; i < count; i++)
		{
			int rowY = y + (count - 1 - i) * 15 + 31;

			String s = options[i];
			if (!targets[i].isEmpty())
			{
				s += " " + targets[i];
			}

			// menuEntry holds all the required things in the click menu to get the
			// tile, and the object.
			MenuEntry menuEntry = menuEntries[i];
			if (menuEntry.getOption().contains("Walk here") || menuEntry.getOption().contains("Walk Cancel"))
			{
				// set argument0, action1 = blank
			}
			MenuRow menuRow = new MenuRow(
					x + 3,
					rowY - 12,
					w - 6,
					15,
					i,
					options[i],
					targets[i],
					s,
					menuEntry.getIdentifier(),
					menuEntry.getOpcode(),
					menuEntry.getActionParam0(),
					menuEntry.getActionParam1(),
					menuEntry,
					menuEntry.getMenuAction()
							.getId(),
					menuEntry.getIdentifier(),
					menuEntry.getMenuAction()
			);
			contextMenu.addMenuRowToTarget(options[i], menuRow);

			// todo: remove this -- for debugging only
			if (menuEntry.getIdentifier() != 0)
				System.out.println("Id: " + menuEntry.getIdentifier() + ", Menu information: actionParam0 " + menuEntry.getActionParam0() + " param1 + " + menuEntry.getParam1());
				System.out.println("Menu row: " + menuRow);
		}
		return contextMenu;
	}


	@Inject
	@Override
	public ContextMenu get2010Menu()
	{
		int x = getMenuX();
		int y = getMenuY();
		int w = getMenuWidth();
		int h = getMenuHeight();
		ContextMenu contextMenu = new ContextMenu(x, y, w, h);

		int mouseX = getMouseX();
		int mouseY = getMouseY();

		contextMenu.setMousePositions(mouseX, mouseY);

		int count = getMenuOptionCount();
		contextMenu.setMenuOptionCount(count);

		String[] targets = getMenuTargets();
		String[] options = getMenuOptions();

		MenuEntry[] menuEntries = getMenuEntries();
		contextMenu.setMenuEntries(menuEntries);
		for (int i = 0; i < count; i++)
		{
			int rowY = y + (count - 1 - i) * 15 + 31;

			String s = options[i];
			if (!targets[i].isEmpty())
			{
				s += " " + targets[i];
			}

			MenuEntry menuEntry = menuEntries[i];
			contextMenu.addMenuRowToTarget(options[i], new MenuRow(
					x + 3,
					rowY - 12,
					w - 6,
					15,
					i,
					options[i],
					targets[i],
					s,
					menuEntry.getIdentifier(),
					menuEntry.getOpcode(),
					menuEntry.getActionParam0(),
					menuEntry.getActionParam1(),
					menuEntry,
					menuEntry.getMenuAction()
							.getId(),
					menuEntry.getIdentifier(),
					menuEntry.getMenuAction()
			));
		}
		return contextMenu;
	}

	@Inject
	@Override
	public void drawOriginalMenu(int alpha)
	{
		int x = getMenuX();
		int y = getMenuY();
		int w = getMenuWidth();
		int h = getMenuHeight();
		rasterizerFillRectangleAlpha(x, y, w, h, ORIGINAL_BG, alpha);
		rasterizerDrawRectangleAlpha(x, y, w, h, ORIGINAL_BG, alpha);
		rasterizerFillRectangleAlpha(x + 1, y + 1, w - 2, 16, 0, alpha);
		rasterizerDrawRectangleAlpha(x + 1, y + 18, w - 2, h - 19, 0, alpha);
		RSFont font = getFontBold12();
		font.drawTextLeftAligned("Choose Option", x + 3, y + 14, ORIGINAL_BG, -1);

		int mouseX = getMouseX();
		int mouseY = getMouseY();

		int count = getMenuOptionCount();
		String[] targets = getMenuTargets();
		String[] options = getMenuOptions();
		for (int i = 0; i < count; i++)
		{
			int rowY = y + (count - 1 - i) * 15 + 31;

			String s = options[i];
			if (!targets[i].isEmpty())
			{
				s += " " + targets[i];
			}

			int highlight = 0xFFFFFF;
			if (mouseX > x && mouseX < w + x && mouseY > rowY - 13 && mouseY < rowY + 3)
			{
				highlight = 0xFFFF00;
			}

			font.drawTextLeftAligned(s, x + 3, rowY, highlight, -1);
		}
	}

	@Inject
	@Override
	public ContextMenu getOriginalMenu()
	{
		int x = getMenuX();
		int y = getMenuY();
		int w = getMenuWidth();
		int h = getMenuHeight();
		ContextMenu contextMenu = new ContextMenu(x, y, w, h);

		int mouseX = getMouseX();
		int mouseY = getMouseY();
		contextMenu.setMousePositions(mouseX, mouseY);
		int count = getMenuOptionCount();
		contextMenu.setMenuOptionCount(count);

		String[] targets = getMenuTargets();
		String[] options = getMenuOptions();

		MenuEntry[] menuEntries = getMenuEntries();
		contextMenu.setMenuEntries(menuEntries);

		for (int i = 0; i < count; i++)
		{
			int rowY = y + (count - 1 - i) * 15 + 31;

			String s = options[i];
			if (!targets[i].isEmpty())
			{
				s += " " + targets[i];
			}


			MenuEntry menuEntry = menuEntries[i];

			contextMenu.addMenuRowToTarget(options[i], new MenuRow(
					x + 3,
					rowY - 12,
					w - 6,
					15,
					i,
					options[i],
					targets[i],
					s,
					menuEntry.getIdentifier(),
					menuEntry.getOpcode(),
					menuEntry.getActionParam0(),
					menuEntry.getActionParam1(),
					menuEntry,
					menuEntry.getMenuAction()
							.getId(),
					menuEntry.getIdentifier(),
					menuEntry.getMenuAction()
			));
		}
		return contextMenu;
	}

	@Inject
	@Override
	public MenuEntryAdded getLeftClickMenuEntry()
	{
		final int i = getMenuOptionCount() - 1;

		return new MenuEntryAdded(
				getMenuOptions()[i],
				getMenuTargets()[i],
				getMenuIdentifiers()[i],
				getMenuOpcodes()[i],
				getMenuArguments1()[i],
				getMenuArguments2()[i],
				getMenuForceLeftClick()[i]
		);
	}

	@Inject
	@Override
	public void setLeftClickMenuEntry(final MenuEntryAdded entry)
	{
		final int i = getMenuOptionCount() - 1;
		getMenuOptions()[i] = entry.getOption();
		getMenuTargets()[i] = entry.getTarget();
		getMenuIdentifiers()[i] = entry.getIdentifier();
		getMenuOpcodes()[i] = entry.getOpcode();
		getMenuArguments1()[i] = entry.getParam0();
		getMenuArguments2()[i] = entry.getParam1();
		getMenuForceLeftClick()[i] = entry.isForceLeftClick();
	}

	@Inject
	@FieldHook("tempMenuAction")
	public static void onTempMenuActionChanged(int idx)
	{
		if (tempMenuAction != null)
		{
			client.getCallbacks()
					.post(WidgetPressed.INSTANCE);
		}
	}

	@Inject
	@Override
	public void setTempMenuEntry(MenuEntry entry)
	{
		if (entry == null || tempMenuAction == null)
		{
			return;
		}

		tempMenuAction.setOption(entry.getOption());
		tempMenuAction.setOpcode(entry.getType().getId());
		tempMenuAction.setIdentifier(entry.getIdentifier());
		tempMenuAction.setParam0(entry.getParam0());
		tempMenuAction.setParam1(entry.getParam1());
	}
}
