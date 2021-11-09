package fr.raksrinana.channelpointsminer.handler.data;

import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@EqualsAndHashCode
@ToString
public class PlacedPrediction{
	@NotNull
	private String eventId;
	@Nullable
	private Prediction prediction;
	private int amount;
}
