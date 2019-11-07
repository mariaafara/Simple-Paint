/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mvc.model;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import java.util.Stack;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

/**
 *
 * @author Administrator
 */
public class hexagonDraw implements shapeDraw{

    @Override
    public void draw(GraphicsContext g, boolean filled, int x1, int y1, int x2, int y2,boolean Released,Stack<Shape> undoHistory) {
             if (x1 == x2 || y1 == y2) {
            return;
        }
        Point2D fifthPoint, fourthPoint, thirdPoint, first, second, center, six;///lthird hye lfourth wl fourth hye lthird
        center = new Point2D(x1, y1);
        int size = (int) Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
        first = pointy_hex_corner(center, size, 1);
        second = pointy_hex_corner(center, size, 2);
        thirdPoint = pointy_hex_corner(center, size, 3);
        fourthPoint = pointy_hex_corner(center, size, 4);
        fifthPoint = pointy_hex_corner(center, size, 5);
        six = pointy_hex_corner(center, size, 6);

        g.fillPolygon(new double[]{first.getX(), second.getX(), thirdPoint.getX(), fourthPoint.getX(), fifthPoint.getX(), six.getX()}, new double[]{first.getY(), second.getY(), thirdPoint.getY(), fourthPoint.getY(), fifthPoint.getY(), six.getY()}, 6);
        g.strokePolygon(new double[]{first.getX(), second.getX(), thirdPoint.getX(), fourthPoint.getX(), fifthPoint.getX(), six.getX()}, new double[]{first.getY(), second.getY(), thirdPoint.getY(), fourthPoint.getY(), fifthPoint.getY(), six.getY()}, 6);
        if (Released) {
            undoHistory.push(new Polygon(new double[]{first.getX(), first.getY(), second.getX(), second.getY(), thirdPoint.getX(), thirdPoint.getY(), fourthPoint.getX(), fourthPoint.getY(), fifthPoint.getX(), fifthPoint.getY(), six.getX(), six.getY()}));
        }
    }

    private Point2D pointy_hex_corner(Point2D center, int size, int i) {
        double angle_deg = 60 * i - 30;
        double angle_rad = Math.PI / 180 * angle_deg;
        return new Point2D(center.getX() + size * cos(angle_rad), center.getY() + size * sin(angle_rad));
    }
}
