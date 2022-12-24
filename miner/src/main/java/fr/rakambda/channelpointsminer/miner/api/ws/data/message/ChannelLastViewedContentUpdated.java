package fr.rakambda.channelpointsminer.miner.api.ws.data.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import fr.rakambda.channelpointsminer.miner.api.ws.data.message.channellastviewedcontentupdated.ChannelLastViewedContentUpdatedData;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@JsonTypeName("channel-last-viewed-content-updated")
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString
public class ChannelLastViewedContentUpdated extends IPubSubMessage{
	@JsonProperty("data")
	private ChannelLastViewedContentUpdatedData data;
}
