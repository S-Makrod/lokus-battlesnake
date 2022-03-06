package com.battlesnake.lokus.service;

import com.battlesnake.codegen.models.CoordinateSchema;
import com.battlesnake.codegen.models.POSTMoveResponseSchema;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MoveService {
    private final MoveUtilities moveUtilities;

  /*
    1. Determine the eligible moves (running into walls, other snakes) -> UP, DOWN
    2. If we want to go food call Adam's function -> RIGHT
    3. Final decision maker
  */

  // todo functions:
  // POSTMoveResponseSchema.Move decisionMaker(POSTMoveRequestSchema) 
    
  // determineEligibleMoves(BattleSnakeSchema ourSnake, List<BattleSnakeSchema> enemySnakes, Board board)

    public POSTMoveResponseSchema.Move getMoveToClosestFood(CoordinateSchema currentHead, List<CoordinateSchema> coordinates, int maximumRadius) {
        List<CoordinateSchema> eligibleCoordinates = moveUtilities.filterCoordinatesExceedingRadius(currentHead, coordinates, maximumRadius);
        CoordinateSchema closestCoordinate = moveUtilities.getClosestCoordinate(currentHead, eligibleCoordinates);
        return moveUtilities.nextMoveToTarget(currentHead, closestCoordinate);
    }
}
