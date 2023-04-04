package com.example.tanksgameserver.socketmodel.models;

import com.example.tanksgameserver.socketmodel.GameState;
import org.apache.commons.math.geometry.Rotation;
import org.apache.commons.math.geometry.Vector3D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TankBodyModel {
    private static final List<Vector3D[]> startVertices = Arrays.asList(new Vector3D[][]{
            {new Vector3D(0, 1, 0), new Vector3D(-0.6, 0.4, 0), new Vector3D(0.6, 0.4, 0)},
            {new Vector3D(-0.6, 0.4, 0), new Vector3D(-0.6, -1, 0), new Vector3D(0.6, -1, 0)},
            {new Vector3D(-0.6, 0.4, 0), new Vector3D(0.6, 0.4, 0), new Vector3D(0.6, -1, 0)}
    });

    private final List<Vector3D[]> triangles;

    public TankBodyModel(Vector3D pos, double rotateAngle) {
        this.triangles = new ArrayList<>();
        setModel(pos, rotateAngle);
    }

    public TankBodyModel() {
        this.triangles = new ArrayList<>();
    }

    public void setModel(Vector3D pos, double rotateAngle) {
        Rotation rotation = new Rotation(GameState.Z_AXIS_VEC, rotateAngle);
        this.triangles.clear();
        for (Vector3D[] triangle : startVertices) {
            Vector3D[] triangleVertices = new Vector3D[3];
            int i = 0;
            for (Vector3D vertex : triangle) {
                triangleVertices[i++] = rotation.applyTo(vertex).add(pos);
            }
            triangles.add(triangleVertices);
        }
    }

    public boolean isInside(Vector3D point) {
        for (Vector3D[] triangle : this.triangles) {
            if (pointIsInTriangle(point, triangle))
                return true;
        }
        return false;
    }

    private boolean pointIsInTriangle(Vector3D point, Vector3D[] triangle) {
        int count = 0;
        count += intersectsLine(point, triangle[1], triangle[2]);
        count += intersectsLine(point, triangle[0], triangle[2]);
        count += intersectsLine(point, triangle[0], triangle[1]);
        return count % 2 == 1;
    }

    private static final double EPS = 0.000001;
    private int intersectsLine(Vector3D target, Vector3D a, Vector3D b) {
        double c1 = b.getX() - a.getX(), c2 = b.getY() - a.getY();
        double intersectX;
        try {
            intersectX = (c2 * a.getX() - c1 * a.getY() + c1 * (target.getY() - target.getX())) / (c2 - c1);
        } catch (NumberFormatException e) {
            return 0;
        }

        double intersectY = intersectX + target.getY() - target.getX();

        double minX = Math.min(a.getX(), b.getX()), maxX = Math.max(a.getX(), b.getX());
        double minY = Math.min(a.getY(), b.getY()), maxY = Math.max(a.getY(), b.getY());
        minX -= EPS;
        maxX += EPS;
        minY -= EPS;
        maxY += EPS;

        return intersectX >= minX && intersectX <= maxX
                && intersectY >= minY && intersectY <= maxY
                && intersectX >= target.getX() ? 1 : 0;
    }
}
