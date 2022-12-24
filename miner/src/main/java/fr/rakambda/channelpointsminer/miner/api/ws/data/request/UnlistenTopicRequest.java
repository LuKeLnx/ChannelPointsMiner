package fr.rakambda.channelpointsminer.miner.api.ws.data.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.rakambda.channelpointsminer.miner.api.ws.data.request.topic.Topics;
import fr.rakambda.channelpointsminer.miner.util.CommonUtils;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public class UnlistenTopicRequest extends ITwitchWebSocketRequest{
	private static final int NONCE_LENGTH = 30;
	
	@JsonProperty("nonce")
	private String nonce;
	@JsonProperty("data")
	private Topics data;
	
	public UnlistenTopicRequest(@NotNull Topics topics){
		super("UNLISTEN");
		data = topics;
		nonce = CommonUtils.randomAlphanumeric(NONCE_LENGTH);
	}
}
