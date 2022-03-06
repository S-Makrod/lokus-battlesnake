package com.battlesnake.lokus.service;

import com.battlesnake.codegen.models.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

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

    public POSTMoveResponseSchema decisionMaker(POSTMoveRequestSchema moveReq) {
        POSTMoveResponseSchema.Move moveToFood = getMoveToClosestFood(moveReq.getYou().getHead(), moveReq.getBoard().getFood(), 3);
        List<POSTMoveResponseSchema.Move> possibleMoves = determineEligibleMoves(moveReq.getYou(), moveReq.getBoard().getSnakes(), moveReq.getBoard());
        boolean foodMovePossible = possibleMoves.stream().anyMatch(move -> move.value().equals(moveToFood.value()));

        if(foodMovePossible) return new POSTMoveResponseSchema().withMove(moveToFood).withShout("I guess I will move " + moveToFood.value());
        
        POSTMoveResponseSchema.Move move = possibleMoves.get(new Random().nextInt(possibleMoves.size()));
        return new POSTMoveResponseSchema().withMove(move).withShout("I guess I will move " + move.value());
    }

    public POSTMoveResponseSchema.Move getMoveToClosestFood(CoordinateSchema currentHead, List<CoordinateSchema> coordinates, int maximumRadius) {
        List<CoordinateSchema> eligibleCoordinates = moveUtilities.filterCoordinatesExceedingRadius(currentHead, coordinates, maximumRadius);
        CoordinateSchema closestCoordinate = moveUtilities.getClosestCoordinate(currentHead, eligibleCoordinates);
        return moveUtilities.nextMoveToTarget(currentHead, closestCoordinate);
    }

    public List<POSTMoveResponseSchema.Move> determineEligibleMoves(BattleSnakeSchema ourSnake, List<BattleSnakeSchema> enemySnakes, BoardSchema board) {
        return null;
    }
}
