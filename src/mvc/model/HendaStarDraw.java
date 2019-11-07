

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mvc.model;

import java.util.ArrayList;
import java.util.Stack;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

/**
 *
 * @author lenovo
 */
public class HendaStarDraw implements shapeDraw {

    @Override
    public void draw(GraphicsContext g, boolean filled, int x1, int y1, int x2, int y2, boolean Released, Stack<Shape> undoHistory) {
        if (x1 == x2 || y1 == y2) {
            return;
        }

        double radius = (int) Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
        double [] x= new double[22];
        double []y = new double[22];
        //createStar(400, 200, 20, 60, 8, 0)
        //        g.fill(createStar(400, 400, 40, 60, 10, 0));

        double a, b;
        
        
        ArrayList<Point2D> arr = createStar(x1, y1,radius, 1.10*radius, 11, 0);
        for (int i = 0; i < 11 * 2; i++) {
            x[i] = arr.get(i).getX();

            y[i] = arr.get(i).getY();
            //  System.out.println("1-->" + x[i] + " - " + y[i]);
        }
        g.fillPolygon(x, y, 22);
        g.strokePolygon(x, y, 22);
        
        double []d =new double[44];
        for(int i=0,j=0;i<44;i++,j++){
            d[i]=arr.get(j).getX();
            d[++i]=arr.get(j).getY();
        }
          if (Released) {
              System.out.println("saveddddddd to undo");
            undoHistory.push(new Polygon(d));
        }
        
        
    }

    private ArrayList<Point2D> createStar(int centerX, int centerY,
            double innerRadius, double outerRadius, int numRays,
            double startAngleRad) {

        System.out.println("-------------------------------------");
        double deltaAngleRad = Math.PI / numRays;

        ArrayList<Point2D> listOfPoints = new ArrayList<>();

        for (int i = 0; i < numRays * 2; i++) {
            double angleRad = startAngleRad + i * deltaAngleRad;
            double ca = Math.cos(angleRad);
            double sa = Math.sin(angleRad);
            double relX = ca;
            double relY = sa;
            if ((i & 1) == 0) {
                relX *= outerRadius;
                relY *= outerRadius;
            } else {
                relX *= innerRadius;
                relY *= innerRadius;
            }

            listOfPoints.add(new Point2D(centerX + relX, centerY + relY));
            System.out.println("----------->" + listOfPoints.get(i).getX());
        }
        //  path.closePath();
        return listOfPoints;
    }

}

