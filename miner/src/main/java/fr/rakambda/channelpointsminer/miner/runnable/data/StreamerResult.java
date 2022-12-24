package fr.rakambda.channelpointsminer.miner.runnable.data;

import fr.rakambda.channelpointsminer.miner.streamer.Streamer;
import fr.rakambda.channelpointsminer.miner.streamer.StreamerSettings;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

@RequiredArgsConstructor
@Getter
public class StreamerResult{
	private final String username;
	private final Supplier<StreamerSettings> streamerSettingsSupplier;
	private final Function<StreamerSettings, Optional<Streamer>> streamerSupplier;
}
