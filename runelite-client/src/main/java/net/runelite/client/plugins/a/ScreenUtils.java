package net.runelite.client.plugins.a;

import net.runelite.api.Point;
import net.runelite.api.*;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.api.widgets.WidgetItem;

import java.awt.*;

public class ScreenUtils
{
	public static boolean isOnScreen(TileObject point)
	{
		assert Adonai.client != null;

		return Adonai.client.getCanvas().contains(point.getCanvasLocation().getX(), point.getCanvasLocation().getY());
	}

	public static boolean isOnScreen(WidgetItem point)
	{
		assert Adonai.client != null;

		return Adonai.client.getCanvas().contains(point.getCanvasLocation().getX(), point.getCanvasLocation().getY());
	}

	public static boolean isOnScreen(Actor actor)
	{
		assert Adonai.client != null;

		// that was my last resort that worked
		Adonai.client.getCanvas().getLocationOnScreen();
		Adonai.client.getCanvas().isShowing();
		Adonai.client.getCanvas().getMousePosition();
		Adonai.client.getCanvasHeight();
		Adonai.client.getCanvasWidth();
		Point actorPoint = Perspective.localToCanvas(Adonai.client, actor.getLocalLocation(), Adonai.client.getPlane());
		assert actorPoint != null;
		return isOnScreen(actorPoint);
	}

	public static boolean isOnScreen(Point point)
	{
		assert Adonai.client != null;

		return (point.getX() >= 0 && point.getX() <= Adonai.client.getCanvasWidth() && point.getY() >= 0 &&
				point.getY() <= Adonai.client.getCanvasHeight());
	}

	public static boolean isOnScreen(LocalPoint lp, int plane)
	{
		assert Adonai.client != null;

		if (isOnMinimap(lp))
		{
			return true;
		}
		return isOnTile(lp, plane);
	}

	public static boolean isOnScreen(WorldPoint wp, int plane)
	{
		assert Adonai.client != null;

		return isOnScreen(LocalPoint.fromWorld(Adonai.client, wp), plane);
	}

	public static boolean isOnMinimap(LocalPoint lp)
	{
		assert Adonai.client != null;

		Point posOnMinimap = Perspective.localToMinimap(Adonai.client, lp);

		if (posOnMinimap == null)
		{
			return false;
		}
		return true;
	}

	public static boolean isOnMinimap(WorldPoint wp)
	{
		assert Adonai.client != null;

		return isOnMinimap(LocalPoint.fromWorld(Adonai.client, wp));
	}

	public static boolean isOnTile(LocalPoint lp, int plane)
	{
		assert Adonai.client != null;

		Point posOnCanvas = Perspective.localToCanvas(Adonai.client, lp, plane);

		if (posOnCanvas == null)
		{
			return false;
		}
		return true;
	}

	public static boolean isOnTile(WorldPoint wp, int plane)
	{
		assert Adonai.client != null;

		return isOnTile(LocalPoint.fromWorld(Adonai.client, wp), plane);
	}


	private java.awt.Point tileCenter(WorldPoint b)
	{
		Client client = Adonai.client;

		if (b.getPlane() != client.getPlane())
		{
			return null;
		}

		LocalPoint lp = LocalPoint.fromWorld(client, b);
		if (lp == null)
		{
			return null;
		}

		Polygon poly = Perspective.getCanvasTilePoly(client, lp);
		if (poly == null)
		{
			return null;
		}

		int cx = poly.getBounds().x + poly.getBounds().width / 2;
		int cy = poly.getBounds().y + poly.getBounds().height / 2;
		return new java.awt.Point(cx, cy);
	}

	public static Point mapWorldPointToGraphicsPoint(WorldPoint worldPoint)
	{
		assert Adonai.client != null;

		RenderOverview ro = Adonai.client.getRenderOverview();

		if (!ro.getWorldMapData().surfaceContainsPosition(worldPoint.getX(), worldPoint.getY()))
		{
			return null;
		}

		float pixelsPerTile = ro.getWorldMapZoom();

		Widget map = Adonai.client.getWidget(WidgetInfo.WORLD_MAP_VIEW);
		if (map != null)
		{
			Rectangle worldMapRect = map.getBounds();

			int widthInTiles = (int) Math.ceil(worldMapRect.getWidth() / pixelsPerTile);
			int heightInTiles = (int) Math.ceil(worldMapRect.getHeight() / pixelsPerTile);

			Point worldMapPosition = ro.getWorldMapPosition();

			//Offset in tiles from anchor sides
			int yTileMax = worldMapPosition.getY() - heightInTiles / 2;
			int yTileOffset = (yTileMax - worldPoint.getY() - 1) * -1;
			int xTileOffset = worldPoint.getX() + widthInTiles / 2 - worldMapPosition.getX();

			int xGraphDiff = ((int) (xTileOffset * pixelsPerTile));
			int yGraphDiff = (int) (yTileOffset * pixelsPerTile);

			//Center on tile.
			yGraphDiff -= pixelsPerTile - Math.ceil(pixelsPerTile / 2);
			xGraphDiff += pixelsPerTile - Math.ceil(pixelsPerTile / 2);

			yGraphDiff = worldMapRect.height - yGraphDiff;
			yGraphDiff += (int) worldMapRect.getY();
			xGraphDiff += (int) worldMapRect.getX();

			return new Point(xGraphDiff, yGraphDiff);
		}
		return null;
	}
}
