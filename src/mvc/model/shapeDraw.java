/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mvc.model;

import java.util.Stack;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Shape;

/**
 *
 * @author Administrator
 */
public interface shapeDraw {
    void draw(GraphicsContext g, boolean filled, int x1, int y1, int x2, int y2,boolean released,Stack<Shape> undoHistory);
}
