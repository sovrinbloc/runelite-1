package net.runelite.client.plugins.a.npc;

import net.runelite.api.NPC;
import net.runelite.api.queries.NPCQuery;
import net.runelite.client.plugins.a.Adonai;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public class NPCs
{


	@Nullable
	public NPC findNearestNPC(int... ids)
	{
		assert Adonai.client.isClientThread();

		if (Adonai.client.getLocalPlayer() == null)
		{
			return null;
		}

		return new NPCQuery()
				.idEquals(ids)
				.result(Adonai.client)
				.nearestTo(Adonai.client.getLocalPlayer());
	}

	@Nullable
	public NPC findNearestNPC(String... names)
	{
		assert Adonai.client.isClientThread();

		if (Adonai.client.getLocalPlayer() == null)
		{
			return null;
		}

		return new NPCQuery()
				.nameEquals(names)
				.result(Adonai.client)
				.nearestTo(Adonai.client.getLocalPlayer());
	}

	public NPC findNpc(int id)
	{
		return Adonai.client.getCachedNPCs()[id];
	}

	@Nullable
	public NPC findNearestNPC(Predicate<NPC> filter)
	{
		assert Adonai.client.isClientThread();

		if (Adonai.client.getLocalPlayer() == null)
		{
			return null;
		}

		return new NPCQuery()
				.filter(filter)
				.result(Adonai.client)
				.nearestTo(Adonai.client.getLocalPlayer());
	}
}
