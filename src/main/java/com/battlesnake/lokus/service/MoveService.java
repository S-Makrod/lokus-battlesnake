package com.battlesnake.lokus.service;

import com.battlesnake.codegen.models.BattleSnakeSchema;
import com.battlesnake.codegen.models.BoardSchema;
import com.battlesnake.codegen.models.CoordinateSchema;
import com.battlesnake.codegen.models.POSTMoveResponseSchema;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MoveService {
    private final MoveUtilities moveUtilities;

    public POSTMoveResponseSchema.Move getMoveToClosestFood(BoardSchema boardSchema, int maxNumberOfSteps, CoordinateSchema ourHead) {
        int[][] graph = moveUtilities.build2DGraph(boardSchema.getSnakes(), boardSchema.getFood(), boardSchema.getWidth(), boardSchema.getHeight(), -1, 2);
        return moveUtilities.shortestPathToTarget(graph, -1, 2, ourHead, maxNumberOfSteps);
    }
}
