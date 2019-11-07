/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mvc.model;

import java.util.Stack;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;

/**
 *
 * @author Administrator
 */
public class lineDraw implements shapeDraw {

    @Override
    public void draw(GraphicsContext g, boolean filled, int x1, int y1, int x2, int y2, boolean Released, Stack<Shape> undoHistory) {
        

        g.strokeLine(x1, y1, x2, y2);

        if (Released) {
            undoHistory.push(new Line(x1, y1, x2, y2));
        }

    }
}
