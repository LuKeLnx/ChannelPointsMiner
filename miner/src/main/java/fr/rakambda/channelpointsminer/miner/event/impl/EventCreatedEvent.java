package fr.rakambda.channelpointsminer.miner.event.impl;

import fr.rakambda.channelpointsminer.miner.api.discord.data.Field;
import fr.rakambda.channelpointsminer.miner.api.ws.data.message.subtype.Event;
import fr.rakambda.channelpointsminer.miner.event.AbstractLoggableStreamerEvent;
import fr.rakambda.channelpointsminer.miner.miner.IMiner;
import fr.rakambda.channelpointsminer.miner.streamer.Streamer;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import java.util.Collection;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@ToString
public class EventCreatedEvent extends AbstractLoggableStreamerEvent{
	private final Event event;
	
	public EventCreatedEvent(@NotNull IMiner miner, @NotNull Streamer streamer, @NotNull Event event){
		super(miner, streamer, event.getCreatedAt().toInstant());
		this.event = event;
	}
	
	@Override
	@NotNull
	public String getAsLog(){
		return "Prediction created [%s]".formatted(event.getTitle());
	}
	
	@Override
	@NotNull
	protected String getEmoji(){
		return "📑";
	}
	
	@Override
	protected int getEmbedColor(){
		return COLOR_PREDICTION;
	}
	
	@Override
	@NotNull
	protected String getEmbedDescription(){
		return "Prediction created";
	}
	
	@Override
	@NotNull
	protected Collection<? extends Field> getEmbedFields(){
		return List.of(Field.builder()
				.name("Title")
				.value(event.getTitle())
				.build());
	}
}
