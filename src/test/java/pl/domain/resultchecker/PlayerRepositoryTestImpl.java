package pl.domain.resultchecker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerRepositoryTestImpl implements PlayerRepository {

    private final Map<String, Player> playerList = new ConcurrentHashMap<>();

    @Override
    public List<Player> saveAll(List<Player> players) {
        players.forEach(player -> playerList.put(player.hash(), player));
        return players;
    }

    @Override
    public Optional<Player> findById(String hash) {
        return Optional.ofNullable(playerList.get(hash));
    }

}
