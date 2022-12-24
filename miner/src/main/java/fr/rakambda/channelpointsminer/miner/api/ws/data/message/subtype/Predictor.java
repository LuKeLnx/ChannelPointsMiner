package fr.rakambda.channelpointsminer.miner.api.ws.data.message.subtype;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import fr.rakambda.channelpointsminer.miner.util.json.ISO8601ZonedDateTimeDeserializer;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.time.ZonedDateTime;

@Getter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Predictor{
	@JsonProperty("id")
	private String id;
	@JsonProperty("event_id")
	private String eventId;
	@JsonProperty("outcome_id")
	private String outcomeId;
	@JsonProperty("channel_id")
	private String channelId;
	@JsonProperty("points")
	private int points;
	@JsonProperty("predicted_at")
	@JsonDeserialize(using = ISO8601ZonedDateTimeDeserializer.class)
	private ZonedDateTime predictedAt;
	@JsonProperty("updated_at")
	@JsonDeserialize(using = ISO8601ZonedDateTimeDeserializer.class)
	private ZonedDateTime updatedAt;
	@JsonProperty("user_id")
	private String userId;
	@JsonProperty("result")
	private Result result;
	@JsonProperty("user_display_name")
	private String userDisplayName;
}
