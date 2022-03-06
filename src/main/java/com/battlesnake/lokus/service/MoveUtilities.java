package com.battlesnake.lokus.service;

import com.battlesnake.codegen.models.CoordinateSchema;
import com.battlesnake.codegen.models.POSTMoveResponseSchema;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
}
