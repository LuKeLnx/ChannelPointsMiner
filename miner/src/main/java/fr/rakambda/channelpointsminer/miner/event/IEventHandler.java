package fr.rakambda.channelpointsminer.miner.event;

public interface IEventHandler extends AutoCloseable{
	void onEvent(IEvent event);
	
	@Override
	default void close(){}
}
