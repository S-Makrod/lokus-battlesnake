package com.battlesnake.lokus.service;

import com.battlesnake.codegen.models.BattleSnakeSchema;
import com.battlesnake.codegen.models.CoordinateSchema;
import com.battlesnake.codegen.models.POSTMoveResponseSchema;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
class Coordinate {
    private Coordinate parentNode;
    private int x;
    private int y;
}

@Service
public class MoveUtilities {

    public POSTMoveResponseSchema.Move nextMoveToTarget(CoordinateSchema source, CoordinateSchema target) {
        if (target.getY() - source.getY() > 0) return POSTMoveResponseSchema.Move.UP;
        if (target.getY() - source.getY() < 0) return POSTMoveResponseSchema.Move.DOWN;
        if (target.getX() - source.getX() > 0) return POSTMoveResponseSchema.Move.LEFT;
        if (target.getX() - source.getX() < 0) return POSTMoveResponseSchema.Move.RIGHT;
        return null;
    }

    public CoordinateSchema getClosestCoordinate(CoordinateSchema source, List<CoordinateSchema> targetCoordinates) {
        CoordinateSchema closestCoordinate = null;
        Double distanceToClosestCoordinate = null;
        for (CoordinateSchema tempTarget : targetCoordinates) {
            double distanceFromSourceToTempTarget = distanceToCoordinate(source, tempTarget);
            if (distanceToClosestCoordinate == null || distanceToClosestCoordinate > distanceFromSourceToTempTarget) {
                closestCoordinate = tempTarget;
                distanceToClosestCoordinate = distanceFromSourceToTempTarget;
            }
        }
        return closestCoordinate;
    }

    public List<CoordinateSchema> filterCoordinatesExceedingRadius(CoordinateSchema source, List<CoordinateSchema> targets, double maxRadius) {
        return targets
                .stream()
                .filter(target
                        -> (distanceToCoordinate(source, target) <= maxRadius))
                .collect(Collectors.toList());
    }

    public double distanceToCoordinate(CoordinateSchema source, CoordinateSchema target) {
        return Math.sqrt(Math.pow(target.getY() - source.getY(), 2) + Math.pow(target.getX() - source.getX(), 2));
    }

    public int[][] build2DGraph(List<BattleSnakeSchema> snakes, List<CoordinateSchema> foodCoordinates, int boardWidth, int boardHeight, int wallRepresentation, int targetRepresentation) {
        int[][] graph = new int[boardWidth][boardHeight];

        // Add snakes to graph
        snakes.forEach(battleSnake -> {
            graph[battleSnake.getHead().getX()][battleSnake.getHead().getY()] = wallRepresentation;

            battleSnake.getBody().forEach(bodyCoordinate -> {
                graph[bodyCoordinate.getX()][bodyCoordinate.getY()] = wallRepresentation;
            });
        });

        // Add food to graph
        foodCoordinates.forEach(foodCoordinate -> {
            graph[foodCoordinate.getX()][foodCoordinate.getY()] = targetRepresentation;
        });

        return graph;
    }

    public POSTMoveResponseSchema.Move findFirstDirectionMoved(Coordinate coordinate) {
        Coordinate nextMoveCoordinate = coordinate;
        Coordinate headCoordinate = nextMoveCoordinate.getParentNode();
        while (headCoordinate.getParentNode() != null) {
            nextMoveCoordinate = headCoordinate;
            headCoordinate = headCoordinate.getParentNode();
        }

        // Determine the direction of the next move starting from the head
        if (nextMoveCoordinate.getX() > headCoordinate.getX()) return POSTMoveResponseSchema.Move.RIGHT;
        if (nextMoveCoordinate.getX() < headCoordinate.getX()) return POSTMoveResponseSchema.Move.LEFT;
        if (nextMoveCoordinate.getY() > headCoordinate.getY()) return POSTMoveResponseSchema.Move.UP;
        if (nextMoveCoordinate.getY() < headCoordinate.getY()) return POSTMoveResponseSchema.Move.DOWN;

        return null;
    }

    public POSTMoveResponseSchema.Move shortestPathToTarget(int[][] graph, int wallRepresentation, int targetRepresentation, CoordinateSchema head, int maxNumberOfSteps) {
        Queue<Coordinate> nextDepthQueue = new LinkedList<>();
        Queue<Coordinate> currentDepthQueue = new LinkedList<>();
        nextDepthQueue.add(new Coordinate(null, head.getX(), head.getY()));

        boolean[][] wasVisited = new boolean[graph.length][graph[0].length];
        int currentNumberOfSteps = -1;

        while ((!nextDepthQueue.isEmpty()) && (currentNumberOfSteps < maxNumberOfSteps)) {
            currentNumberOfSteps++;
            currentDepthQueue = nextDepthQueue;
            nextDepthQueue = new LinkedList<>();
            while (!currentDepthQueue.isEmpty()) {
                Coordinate currCord = currentDepthQueue.remove();

                if (wasVisited[currCord.getX()][currCord.getY()]) continue;
                wasVisited[currCord.getX()][currCord.getY()] = true;

                // Target has been found
                if (graph[currCord.getX()][currCord.getY()] == targetRepresentation) {
                    return findFirstDirectionMoved(currCord);
                }

                // Add adjacent nodes to queue
                if (currCord.getX() > 0 && graph[currCord.getX()-1][currCord.getY()] != wallRepresentation) nextDepthQueue.add(new Coordinate(currCord, currCord.getX()-1, currCord.getY()));
                if (currCord.getX() < graph.length-1 && graph[currCord.getX()+1][currCord.getY()] != wallRepresentation) nextDepthQueue.add(new Coordinate(currCord, currCord.getX()+1, currCord.getY()));
                if (currCord.getY() > 0 && graph[currCord.getX()][currCord.getY()-1] != wallRepresentation) nextDepthQueue.add(new Coordinate(currCord, currCord.getX(), currCord.getY()-1));
                if (currCord.getY() < graph.length-1 && graph[currCord.getX()][currCord.getY()+1] != wallRepresentation) nextDepthQueue.add(new Coordinate(currCord, currCord.getX(), currCord.getY()+1));
            }
        }

        // No available targets found
        return null;
    }
}
