/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mvc.controller;

import java.util.HashMap;
import javafx.scene.shape.Shape;
import mvc.model.CircleDraw;
import mvc.model.HendaStarDraw;
import mvc.model.HeptaStarDraw;
import mvc.model.PentaStarDraw;
import mvc.model.RectDraw;
import mvc.model.StarDraw;
import mvc.model.dashedLineDraw;
import mvc.model.diamondDraw;
import mvc.model.hexagonDraw;
import mvc.model.lineDraw;
import mvc.model.ovalDraw;
import mvc.model.pentagonDraw;
import mvc.model.rightPentagonDraw;
import mvc.model.roundRectDraw;
import mvc.model.shapeDraw;
import mvc.model.squareDraw;
import mvc.model.triangle90Draw;
import mvc.model.triangleDraw;

/**
 *
 * @author Administrator
 */
    public class shapeFactory {
         private static final HashMap shapeMap = new HashMap();

    //use getShape method to get object of type shape 
    public shapeDraw getShape(String shapeType) {
        shapeDraw s;
          s=(shapeDraw) shapeMap.get(shapeType);
       
        switch (shapeType) {
            case ("Oval"):
                if(s == null){
                    s=new ovalDraw();
                    shapeMap.put(shapeType, s);
                }
                    return s;
            case ("Rectangle"):
                if(s == null){
                    s=new RectDraw();
                    shapeMap.put(shapeType, s);
                }
                    return s;
                
            case ("RoundRectangle"):
                if(s == null){
                    s=new roundRectDraw();
                    shapeMap.put(shapeType, s);
                }
                    return s;
            
            case ("Circle"):
             if(s == null){
                    s=new CircleDraw();
                    shapeMap.put(shapeType, s);
                    
                }
                    return s;
               
            case ("Square"):
                if(s == null){
                    s=new squareDraw();
                    shapeMap.put(shapeType, s);
                }
                    return s;
                
              
            case ("Triangle"):
                if(s == null){
                    s=new triangleDraw();
                    shapeMap.put(shapeType, s);
                }
                    return s;
                
            case ("Triangle90"):
                if(s == null){
                    s=new triangle90Draw();
                    shapeMap.put(shapeType, s);
                }
                    return s;
                
            case ("RightPentagon"):
                if(s == null){
                    s=new rightPentagonDraw();
                    shapeMap.put(shapeType, s);
                }
                    return s;
               
            case ("Hexagon"):
                if(s == null){
                    s=new hexagonDraw();
                    shapeMap.put(shapeType, s);
                }
                    return s;
               
            case ("Diamond"):
                if(s == null){
                    s=new diamondDraw();
                    shapeMap.put(shapeType, s);
                }
                    return s;
               
            case ("Pentagon"):
                if(s == null){
                    s=new pentagonDraw();
                    shapeMap.put(shapeType, s);
                }
                    return s;
               
            case ("Line"):
                if(s == null){
                    s=new lineDraw();
                    shapeMap.put(shapeType, s);
                }
                    return s;
               
            case ("dashedLine"):
                if(s == null){
                    s=new dashedLineDraw();
                    shapeMap.put(shapeType, s);
                }
                    return s;
               
            case ("Star"):
                if(s == null){
                    s=new StarDraw();
                    shapeMap.put(shapeType, s);
                }
                    return s;
               
            case ("hepta"):
                if(s == null){
                    s=new HeptaStarDraw();
                    shapeMap.put(shapeType, s);
                }
                    return s;
                
            case ("penta"):
                if(s == null){
                    s=new PentaStarDraw();
                    shapeMap.put(shapeType, s);
                }
                    return s;
                
            case ("henda"):
                if(s == null){
                    s=new HendaStarDraw();
                    shapeMap.put(shapeType, s);
                }
                    return s;
                
        }

        return null;
    }
}
