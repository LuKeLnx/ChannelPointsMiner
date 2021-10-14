package fr.raksrinana.twitchminer.api.ws;

import fr.raksrinana.twitchminer.api.ws.data.request.topic.Topics;
import fr.raksrinana.twitchminer.api.ws.data.response.MessageResponse;
import fr.raksrinana.twitchminer.api.ws.data.response.TwitchWebSocketResponse;
import fr.raksrinana.twitchminer.factory.TimeFactory;
import fr.raksrinana.twitchminer.factory.TwitchWebSocketClientFactory;
import lombok.extern.log4j.Log4j2;
import org.java_websocket.client.WebSocketClient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import static java.time.temporal.ChronoUnit.MINUTES;
import static org.java_websocket.framing.CloseFrame.ABNORMAL_CLOSE;
import static org.java_websocket.framing.CloseFrame.NORMAL;

@Log4j2
public class TwitchWebSocketPool implements AutoCloseable, TwitchWebSocketListener{
	private static final int MAX_TOPIC_PER_CLIENT = 50;
	private static final int SOCKET_TIMEOUT_MINUTES = 5;
	
	private final Collection<TwitchWebSocketClient> clients;
	private final List<TwitchMessageListener> listeners;
	
	public TwitchWebSocketPool(){
		clients = new ArrayList<>();
		listeners = new ArrayList<>();
	}
	
	public void ping(){
		clients.stream()
				.filter(client -> TimeFactory.now().isAfter(client.getLastPong().plus(SOCKET_TIMEOUT_MINUTES, MINUTES)))
				.forEach(client -> client.close(ABNORMAL_CLOSE, "Timeout reached"));
		
		clients.stream()
				.filter(WebSocketClient::isOpen)
				.filter(client -> !client.isClosing())
				.forEach(TwitchWebSocketClient::ping);
	}
	
	public void listenTopic(@NotNull Topics topics){
		var isListened = topics.getTopics().stream().anyMatch(t -> clients.stream().anyMatch(c -> c.isTopicListened(t)));
		if(isListened){
			log.debug("Topic {} is already being listened", topics);
			return;
		}
		getAvailableClient().listenTopic(topics);
	}
	
	@NotNull
	private TwitchWebSocketClient getAvailableClient(){
		return clients.stream()
				.filter(client -> client.getTopicCount() < MAX_TOPIC_PER_CLIENT)
				.findAny()
				.orElseGet(this::createNewClient);
	}
	
	@NotNull
	private TwitchWebSocketClient createNewClient(){
		try{
			var client = TwitchWebSocketClientFactory.createClient();
			client.addListener(this);
			client.connectBlocking();
			clients.add(client);
			return client;
		}
		catch(Exception e){
			log.error("Failed to create new websocket");
			throw new RuntimeException(e);
		}
	}
	
	public void addListener(@NotNull TwitchMessageListener listener){
		listeners.add(listener);
	}
	
	@Override
	public void onWebSocketMessage(@NotNull TwitchWebSocketResponse response){
		if(response instanceof MessageResponse m){
			var topic = m.getData().getTopic();
			var message = m.getData().getMessage();
			listeners.forEach(l -> l.onTwitchMessage(topic, message));
		}
	}
	
	@Override
	public void onWebSocketClosed(@NotNull TwitchWebSocketClient client, int code, @Nullable String reason, boolean remote){
		clients.remove(client);
		if(code != NORMAL){
			var allTopics = client.getTopics().stream().collect(new Topics.TopicsCollector());
			listenTopic(allTopics);
		}
	}
	
	public int getClientCount(){
		return clients.size();
	}
	
	@Override
	public void close(){
		clients.forEach(WebSocketClient::close);
	}
}
