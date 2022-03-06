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
        POSTMoveResponseSchema.Move moveToFood = getMoveToClosestFood(moveReq.getBoard(), 3, moveReq.getYou().getHead());
        List<POSTMoveResponseSchema.Move> possibleMoves = determineEligibleMoves(moveReq.getYou(), moveReq.getBoard().getSnakes(), moveReq.getBoard());
        boolean foodMovePossible = possibleMoves.stream().anyMatch(move -> move.value().equals(moveToFood.value()));

        if(foodMovePossible) return new POSTMoveResponseSchema().withMove(moveToFood).withShout("I guess I will move " + moveToFood.value());

        POSTMoveResponseSchema.Move move = possibleMoves.get(new Random().nextInt(possibleMoves.size()));
        return new POSTMoveResponseSchema().withMove(move).withShout("I guess I will move " + move.value());
    }

    public List<POSTMoveResponseSchema.Move> determineEligibleMoves(BattleSnakeSchema ourSnake, List<BattleSnakeSchema> enemySnakes, BoardSchema board) {
        return null;
    }

    public POSTMoveResponseSchema.Move getMoveToClosestFood(BoardSchema boardSchema, int maxNumberOfSteps, CoordinateSchema ourHead) {
        int[][] graph = moveUtilities.build2DGraph(boardSchema.getSnakes(), boardSchema.getFood(), boardSchema.getWidth(), boardSchema.getHeight(), -1, 2);
        return moveUtilities.shortestPathToTarget(graph, -1, 2, ourHead, maxNumberOfSteps);
    }
}
