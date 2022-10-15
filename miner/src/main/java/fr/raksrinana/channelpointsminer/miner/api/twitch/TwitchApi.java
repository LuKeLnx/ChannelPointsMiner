package fr.raksrinana.channelpointsminer.miner.api.twitch;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.raksrinana.channelpointsminer.miner.api.twitch.data.PlayerEvent;
import fr.raksrinana.channelpointsminer.miner.util.json.JacksonUtils;
import kong.unirest.core.UnirestInstance;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Base64;
import java.util.Optional;
import java.util.regex.Pattern;
import static java.nio.charset.StandardCharsets.UTF_8;

@RequiredArgsConstructor
@Log4j2
public class TwitchApi{
	private static final Pattern SETTINGS_URL_PATTERN = Pattern.compile("(https://static.twitchcdn.net/config/settings.*?js)");
	private static final Pattern SPADE_URL_PATTERN = Pattern.compile("\"spade_url\":\"(.*?)\"");
	
	private final UnirestInstance unirest;
	
	@NotNull
	public Optional<URL> getSpadeUrl(@NotNull URL streamerUrl){
		return getStreamerPageContent(streamerUrl)
				.flatMap(content -> extractUrl(SPADE_URL_PATTERN, content).or(() -> extractSpadeFromSettings(content)));
	}
	
	@NotNull
	private Optional<String> getStreamerPageContent(@NotNull URL streamerUrl){
		var response = unirest.get(streamerUrl.toString()).asString();
		
		if(!response.isSuccess()){
			return Optional.empty();
		}
		
		return Optional.of(response.getBody());
	}
	
	@NotNull
	private Optional<URL> extractSpadeFromSettings(@NotNull String content){
		return extractUrl(SETTINGS_URL_PATTERN, content)
				.map(settingsUrl -> {
					var response = unirest.get(settingsUrl.toString()).asString();
					
					if(!response.isSuccess()){
						return null;
					}
					
					return response.getBody();
				})
				.flatMap(c -> extractUrl(SPADE_URL_PATTERN, c));
	}
	
	@NotNull
	private Optional<URL> extractUrl(@NotNull Pattern pattern, @NotNull String content){
		var matcher = pattern.matcher(content);
		if(!matcher.find()){
			return Optional.empty();
		}
		
		try{
			return Optional.of(URI.create(matcher.group(1)).toURL());
		}
		catch(MalformedURLException e){
			log.error("Failed to parse url", e);
			return Optional.empty();
		}
	}
	
	public boolean sendPlayerEvents(@NotNull URL spadeUrl, @NotNull PlayerEvent... events){
		try{
			var requestStr = JacksonUtils.writeAsString(events);
			var requestBase64 = new String(Base64.getEncoder().encode(requestStr.getBytes(UTF_8)), UTF_8);
			var data = "data=" + requestBase64;
			
			var response = unirest.post(spadeUrl.toString())
					.body(data)
					.asEmpty();
			
			return response.isSuccess();
		}
		catch(JsonProcessingException e){
			log.error("Failed to send minute watched", e);
			return false;
		}
	}
}
