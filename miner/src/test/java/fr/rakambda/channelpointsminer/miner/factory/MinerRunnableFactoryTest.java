package fr.rakambda.channelpointsminer.miner.factory;

import fr.rakambda.channelpointsminer.miner.miner.IMiner;
import fr.rakambda.channelpointsminer.miner.runnable.SendMinutesWatched;
import fr.rakambda.channelpointsminer.miner.runnable.StreamerConfigurationReload;
import fr.rakambda.channelpointsminer.miner.runnable.SyncInventory;
import fr.rakambda.channelpointsminer.miner.runnable.UpdateStreamInfo;
import fr.rakambda.channelpointsminer.miner.runnable.WebSocketPing;
import fr.rakambda.channelpointsminer.miner.tests.ParallelizableTest;
import org.assertj.core.api.Assertions;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ParallelizableTest
@ExtendWith(MockitoExtension.class)
class MinerRunnableFactoryTest{
	@Mock
	private IMiner miner;
	@Mock
	private StreamerSettingsFactory streamerSettingsFactory;
	
	@Test
	void createUpdateStreamInfo(){
		Assertions.assertThat(MinerRunnableFactory.createUpdateStreamInfo(miner)).isNotNull()
				.isInstanceOf(UpdateStreamInfo.class);
	}
	
	@Test
	void createSendMinutesWatched(){
		Assertions.assertThat(MinerRunnableFactory.createSendMinutesWatched(miner)).isNotNull()
				.isInstanceOf(SendMinutesWatched.class);
	}
	
	@Test
	void createWebSocketPing(){
		Assertions.assertThat(MinerRunnableFactory.createWebSocketPing(miner)).isNotNull()
				.isInstanceOf(WebSocketPing.class);
	}
	
	@Test
	void createSyncInventory(){
		Assertions.assertThat(MinerRunnableFactory.createSyncInventory(miner)).isNotNull()
				.isInstanceOf(SyncInventory.class);
	}
	
	@Test
	void createStreamerConfigurationReload(){
		Assertions.assertThat(MinerRunnableFactory.createStreamerConfigurationReload(miner, streamerSettingsFactory, false)).isNotNull()
				.isInstanceOf(StreamerConfigurationReload.class);
	}
}