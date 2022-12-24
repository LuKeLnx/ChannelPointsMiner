package fr.rakambda.channelpointsminer.miner.util.json;

import com.fasterxml.jackson.databind.JsonDeserializer;
import fr.rakambda.channelpointsminer.miner.tests.ParallelizableTest;
import org.junit.jupiter.api.Test;
import java.time.Instant;
import static org.assertj.core.api.Assertions.assertThat;

@ParallelizableTest
class TwitchTimestampDeserializerTest extends DeserializerTest<Instant>{
	@Test
	void stringValue(){
		assertThat(deserialize("\"%s\"".formatted("1633717410.096392"))).isEqualTo(Instant.parse("2021-10-08T18:23:30.096392Z"));
	}
	
	@Test
	void empty(){
		assertThat(deserialize("\"\"")).isNull();
	}
	
	@Override
	protected JsonDeserializer<Instant> getDeserializer(){
		return new TwitchTimestampDeserializer();
	}
}