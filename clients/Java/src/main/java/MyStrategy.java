import model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class MyStrategy {
    public Action getAction(PlayerView playerView, DebugInterface debugInterface) {
        var myPlayer = Arrays.stream(playerView.getPlayers())
                .filter(x -> x.getId() == playerView.getMyId())
                .findFirst().get();


        var entityActions = new HashMap<Integer, EntityAction>();
        for (Entity gameObject : playerView.getEntities()) {
            if (gameObject.getPlayerId() != null && playerView.getMyId() == gameObject.getPlayerId()) {
                if (gameObject.getEntityType() == EntityType.BUILDER_UNIT) {
                    entityActions.put(
                            gameObject.getId(),
                            builderHarvestResources(playerView.getMapSize()));
                } else if (gameObject.getEntityType() == EntityType.BUILDER_BASE) {
                    if (myPlayer.getResource() > 20) {
                        entityActions.put(
                                gameObject.getId(),
                                orderBuilder(gameObject.getPosition().getX(), gameObject.getPosition().getY())
                        );
                    }
                }

            }

        }

        return new Action(entityActions);
    }

    private EntityAction orderBuilder(int x, int y) {
        return new EntityAction(
                null,
                new BuildAction(EntityType.BUILDER_UNIT, new Vec2Int(x-1, y)),
                null,
                null
        );
    }

    public void debugUpdate(PlayerView playerView, DebugInterface debugInterface) {
        debugInterface.send(new DebugCommand.Clear());
        debugInterface.getState();
    }

    private EntityAction builderHarvestResources(int autoAttackRange) {
        EntityType[] entityTypeArr = new EntityType[1];
        entityTypeArr[0] = EntityType.RESOURCE;

        return new EntityAction(
                null,
                null,
                new AttackAction(0, new AutoAttack(autoAttackRange, entityTypeArr)),
                null
        );


    }
}