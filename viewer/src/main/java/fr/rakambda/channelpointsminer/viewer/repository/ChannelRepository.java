package fr.rakambda.channelpointsminer.viewer.repository;

import fr.rakambda.channelpointsminer.viewer.repository.entity.ChannelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChannelRepository extends JpaRepository<ChannelEntity, String>{
}
