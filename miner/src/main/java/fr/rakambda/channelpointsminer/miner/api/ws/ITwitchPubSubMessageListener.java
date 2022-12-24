package fr.rakambda.channelpointsminer.miner.api.ws;

import fr.rakambda.channelpointsminer.miner.api.ws.data.message.IPubSubMessage;
import fr.rakambda.channelpointsminer.miner.api.ws.data.request.topic.Topic;
import org.jetbrains.annotations.NotNull;

public interface ITwitchPubSubMessageListener{
	void onTwitchMessage(@NotNull Topic topic, @NotNull IPubSubMessage message);
}
