package net.runelite.client.external.adonaicore.utils;

import net.runelite.api.ChatMessageType;
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;

import java.text.MessageFormat;

public class Messages
{
	public static ChatMessageManager chatMessageManager;

	public static void initialize(ChatMessageManager chatMessageManager)
	{
		Messages.chatMessageManager = chatMessageManager;
	}

	public static void sendChatMessage(String pattern, Object... arguments)
	{
		sendChatMessage(MessageFormat.format(pattern, arguments));
	}

	public static String format(String pattern, Object... arguments)
	{
		int i = 0;
		while (pattern.contains("{}"))
		{
			pattern = pattern.replaceFirst("\\{\\}", "{" + i + "}");
			i++;
		}
		return MessageFormat.format(pattern, arguments);
	}

	private static QueuedMessage toChatMessage(String pattern, Object... arguments)
	{
		return QueuedMessage.builder()
				.type(ChatMessageType.CONSOLE)
				.runeLiteFormattedMessage(MessageFormat.format(pattern, arguments))
				.build();
	}

	public static void sendChatMessage(QueuedMessage message)
	{
		assert chatMessageManager != null;

		chatMessageManager.queue(message);
	}

	public static void sendChatMessage(String chatMessage)
	{
		assert chatMessageManager != null;

		final String message = new ChatMessageBuilder()
				.append(ChatColorType.HIGHLIGHT)
				.append(chatMessage)
				.build();

		chatMessageManager.queue(
				QueuedMessage.builder()
						.type(ChatMessageType.CONSOLE)
						.runeLiteFormattedMessage(message)
						.build());
	}
}
