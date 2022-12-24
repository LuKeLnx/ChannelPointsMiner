package fr.rakambda.channelpointsminer.miner.api.chat.irc;

import fr.rakambda.channelpointsminer.miner.tests.ParallelizableTest;
import org.kitteh.irc.client.library.event.connection.ClientConnectionClosedEvent;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ParallelizableTest
@ExtendWith(MockitoExtension.class)
class TwitchIrcConnectionHandlerTest{
	private static final String USERNAME = "username";
	
	private final TwitchIrcConnectionHandler tested = new TwitchIrcConnectionHandler(USERNAME);
	
	@Mock
	private ClientConnectionClosedEvent clientConnectionClosedEvent;
	
	@Test
	void reconnectIfAttemptCanBeMade(){
		when(clientConnectionClosedEvent.canAttemptReconnect()).thenReturn(true);
		
		assertDoesNotThrow(() -> tested.onClientConnectionCLoseEvent(clientConnectionClosedEvent));
		
		verify(clientConnectionClosedEvent).setAttemptReconnect(true);
	}
	
	@Test
	void cannotReconnect(){
		when(clientConnectionClosedEvent.canAttemptReconnect()).thenReturn(false);
		
		assertDoesNotThrow(() -> tested.onClientConnectionCLoseEvent(clientConnectionClosedEvent));
		
		verify(clientConnectionClosedEvent, never()).setAttemptReconnect(anyBoolean());
	}
}