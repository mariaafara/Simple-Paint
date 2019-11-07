/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mvc.model;

import java.util.Stack;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;

/**
 *
 * @author lenovo
 */
public class dashedLineDraw implements shapeDraw {

    @Override
    public void draw(GraphicsContext g, boolean filled, int x1, int y1, int x2, int y2, boolean Released, Stack<Shape> undoHistory) {
        g.setLineDashes(35);
        g.strokeLine(x1, y1, x2, y2);

        if (Released) {
            Line l=new Line(x1, y1, x2, y2);
            l.getStrokeDashArray().addAll(3.0,7.0,3.0,7.0);
    
            undoHistory.push(l);
        }

    }
}
