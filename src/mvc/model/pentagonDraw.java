/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mvc.model;

import java.util.Stack;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

/**
 *
 * @author Administrator
 */
public class pentagonDraw implements shapeDraw{

    @Override
    public void draw(GraphicsContext g, boolean filled, int x1, int y1, int x2, int y2,boolean Released,Stack<Shape> undoHistory) {
         int xC=x1-(x2-x1);
            int yC=y2;
            double xE=0.62*(x2-x1)+x1;
            double yE=1.62*(y2-y1)+y2;
            double xD=x1-0.62*(x1-xC);
            double yD=yE;
            g.fillPolygon(new double[]{x1, x2, xE,xD,xC}, new double[]{y1, y2, yE,yD,yC}, 5);
            g.strokePolygon(new double[]{x1, x2,xE,xD,xC}, new double[]{y1, y2, yE,yD,yC}, 5);
        if (Released) {
            undoHistory.push(new Polygon(new double[]{x1, y1, x2, y2, xE, yE, xD, yD, xC, yC}));
        }
           
    }
}
