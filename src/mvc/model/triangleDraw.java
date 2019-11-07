/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mvc.model;

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
public class triangleDraw implements shapeDraw{

    @Override
    public void draw(GraphicsContext g, boolean filled, int x1, int y1, int x2, int y2,boolean Released,Stack<Shape> undoHistory) {
     if (x1 == x2 || y1 == y2) {
            return;
        }
        int x = Math.min(x1, x2);    // get upper left corner, (x,y)
        int y = Math.min(y1, y2);
        int w = Math.abs(x1 - x2);  // get width and height
        int h = Math.abs(y1 - y2);
        Point2D thirdPoint;
        double temp = Math.abs(x1 - x2);
        if (x1 < x2) {
            thirdPoint = new Point2D(x2 - (temp * 2), y2);
//            thirdPoint = new Point2D(x1 , y2);
        } else {
            thirdPoint = new Point2D(x2 + (temp * 2), y2);
            //        thirdPoint = new Point2D(x1 , y2);
        }
        g.fillPolygon(new double[]{x1, x2, thirdPoint.getX()}, new double[]{y1, y2, thirdPoint.getY()}, 3);
        g.strokePolygon(new double[]{x1, x2, thirdPoint.getX()}, new double[]{y1, y2, thirdPoint.getY()}, 3);
        if (Released) {
            undoHistory.push(new Polygon(new double[]{x1, y1, x2, y2, thirdPoint.getX(), thirdPoint.getY()}));
        }   
    }
}
