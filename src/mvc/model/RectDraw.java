/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mvc.model;

import java.util.Stack;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

/**
 *
 * @author Administrator
 */
public class RectDraw implements shapeDraw{

    @Override
    public void draw(GraphicsContext g, boolean filled, int x1, int y1, int x2, int y2,boolean Released,Stack<Shape> undoHistory) {
     if (x1 == x2 || y1 == y2) {
            return;
        }
        int x = Math.min(x1, x2);    // get upper left corner, (x,y)
        int y = Math.min(y1, y2);
        int w = Math.abs(x1 - x2);  // get width and height
        int h = Math.abs(y1 - y2);
        g.fillRoundRect(x, y, w, h, 0, 0);//arc width arc height
        g.strokeRoundRect(x, y, w, h, 0, 0);
        if (Released) {
            Rectangle rect = new Rectangle(x, y, w, h);
          
            undoHistory.push(rect);
        }   
    }
}
