package net.runelite.client.external.adonaicore.wrappers;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Point;
import net.runelite.api.events.MenuOpened;
import net.runelite.client.plugins.adonaicore.Adonai;
import net.runelite.client.external.adonaicore.toolbox.Calculations;

@Slf4j
public class MenuRow
{
	net.runelite.api.widgets.menu.MenuRow row;

	MenuOpened menu;

	public Point mousePosition;
	public MenuRow(net.runelite.api.widgets.menu.MenuRow row, MenuOpened menu)
	{
		this.row = row;
		this.menu = menu;
	}

	public net.runelite.api.widgets.menu.MenuRow getRow()
	{
		return row;
	}

	public java.awt.Point getCanvasPosition()
	{
		return this.row.getPosition();
	}

	public java.awt.Point getAbsolutePosition()
	{
		return Calculations.add(this.row.getPosition(), Calculations.convertToPoint(Adonai.client.getMouseCanvasPosition()));
	}

}