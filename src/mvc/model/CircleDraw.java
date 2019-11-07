/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mvc.model;

import java.util.Stack;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

/**
 *
 * @author Administrator
 */
public class CircleDraw implements shapeDraw{

    @Override
    public void draw(GraphicsContext g, boolean filled, int x1, int y1, int x2, int y2,boolean Released,Stack<Shape> undoHistory) {
       
        if (x1 == x2 || y1 == y2) {
            return;
        }
        int x = Math.min(x1, x2);    // get upper left corner, (x,y)
        int y = Math.min(y1, y2);
        int w = Math.abs(x1 - x2);  // get width and height
        int h = Math.abs(y1 - y2);
        //       circ.setRadius((Math.abs(e.getX() - circ.getCenterX()) + Math.abs(e.getY() - circ.getCenterY())) / 2);
        double xx = Math.abs(x1 - x2) + Math.abs(y1 - y2) / 2;
        int centerX, centerY;
        if (x1 > x2) {
            centerX = x2;
        } else {
            centerX = x1;
        }
        if (y1 > y2) {
            centerY = y2;
        } else {
            centerY = y1;
        }
        g.fillOval(centerX, centerY, xx, xx);
        g.strokeOval(centerX, centerY, xx, xx);
        if (Released) {
            undoHistory.push(new Circle(centerX, centerY, xx));
        }
        
        
    }
    
}
