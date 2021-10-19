package net.runelite.client.plugins.a.utils;

import net.runelite.api.ChatMessageType;
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;

import javax.inject.Inject;
import java.text.MessageFormat;

public class ChatMessages
{
	@Inject
	public static ChatMessageManager chatMessageManager;

	public static void initialize(ChatMessageManager chatMessageManager)
	{
		ChatMessages.chatMessageManager = chatMessageManager;
	}

	public static void sendChatMessage(String pattern, Object... arguments)
	{
		sendChatMessage(MessageFormat.format(pattern, arguments));
	}

	public static String messageArgs(String pattern, Object... arguments)
	{
		return MessageFormat.format(pattern, arguments);
	}

	public static QueuedMessage toChatMessage(String pattern, Object... arguments)
	{
		return QueuedMessage.builder()
				.type(ChatMessageType.CONSOLE)
				.runeLiteFormattedMessage(MessageFormat.format(pattern, arguments))
				.build();
	}

	public static void sendChatMessage(QueuedMessage message)
	{
		chatMessageManager.queue(message);
	}

	public static void sendChatMessage(String chatMessage)
	{
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
