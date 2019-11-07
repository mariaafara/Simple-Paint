package mvc.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Stack;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;

import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.StackPane;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.imageio.ImageIO;
import mvc.View.View;
import mvc.model.shapeDraw;

public class viewController {
//  shapeDraw shapeDraw1;

    private boolean Released = false;

    Stack<Shape> undoHistory;
    Stack<Shape> redoHistory;
    Stack<Text> undoText;
    Stack<Text> redoText;
    private boolean dragging;     // is a drag in progress?
    private int startX, startY;   // start point of drag
    private int prevX, prevY;     // previous mouse location during a drag
    private int currentX, currentY;  // current mouse position during a drag

    View p;

    public viewController(View p) {
        this.p = p;
        undoHistory = new Stack();
        redoHistory = new Stack();
    }

    public void mousePressed(View p, MouseEvent e) {

        Released = false;

        startX = prevX = currentX = (int) e.getX();
        startY = prevY = currentY = (int) e.getY();
        dragging = true;
        p.gc.setStroke(p.primaryColor);  // Make sure we are drawing with the right color.
        p.gc.setFill(p.secondaryColor);
        p.overlayGraphics.setStroke(p.primaryColor);
        p.overlayGraphics.setFill(p.secondaryColor);
        p.sampleLine.setStroke(p.primaryColor);
        if (p.drowbtn.isSelected()) {
            p.gc.setStroke(p.primaryColor);
            p.gc.beginPath();
            p.gc.lineTo(e.getX(), e.getY());
            undoHistory.push(new Line(e.getX(), e.getY(), e.getX(), e.getY()));
            redoHistory.clear();
            Shape lastUndo = undoHistory.lastElement();
            lastUndo.setFill(p.gc.getFill());
            lastUndo.setFill(p.secondaryColor);
            lastUndo.setStroke(p.gc.getStroke());
            lastUndo.setStrokeWidth(p.gc.getLineWidth());

        } else if (p.rubberbtn.isSelected()) {
            double lineWidth = p.gc.getLineWidth();
            p.gc.setFill(Color.WHITE);
            p.gc.fillRect(startX - 5, startY - 5, p.slider.getValue(), p.slider.getValue());
            p.sampleLine.setStroke(Color.WHITE);
            //            undoHistory.push(new Rectangle(prevX - 5, prevY - 5, lineWidth, lineWidth));
        } else if (p.textbtn.isSelected()) {
            double d = p.gc.getLineWidth();
            p.gc.setLineWidth(1);
            int sol;
            sol = (int) ((p.slider.getValue() + 5) * 4);
            p.gc.setFont(javafx.scene.text.Font.font((String) p.combobox.getValue(), sol));

            p.gc.setStroke(p.primaryColor);
            p.gc.setFill(p.secondaryColor);
            p.gc.fillText(p.text.getText(), e.getX(), e.getY());
            p.gc.strokeText(p.text.getText(), e.getX(), e.getY());
            p.gc.setLineWidth(d);
        }

    }

    public void mouseDragged(View p, MouseEvent e) {

        Released = false;
        if (!dragging) {
            return;
        }

        currentX = (int) e.getX();
        currentY = (int) e.getY();
        if (p.drowbtn.isSelected()) {
            p.gc.strokeLine(prevX, prevY, currentX, currentY);

            undoHistory.push(new Line(prevX, prevY, currentX, currentY));
            redoHistory.clear();
            Shape lastUndo = undoHistory.lastElement();
            lastUndo.setFill(p.gc.getFill());
            lastUndo.setFill(p.secondaryColor);
            lastUndo.setStroke(p.gc.getStroke());
            lastUndo.setStrokeWidth(p.gc.getLineWidth());

        } else if (p.rubberbtn.isSelected()) {
            double lineWidth = p.gc.getLineWidth();
            p.gc.setFill(Color.WHITE);
            p.gc.setStroke(Color.BLACK);
            p.gc.fillRect(prevX - 5, prevY - 5, p.slider.getValue(), p.slider.getValue());
            //           undoHistory.push(new Rectangle(prevX - 5, prevY - 5, lineWidth, lineWidth));
        } else {
            p.overlayGraphics.clearRect(0, 0, p.overlay.getWidth(), p.overlay.getHeight());
            putCurrentShape(p.overlayGraphics);
        }
        prevX = currentX;
        prevY = currentY;

    }

    public void mouseReleased(View p, MouseEvent e) {

        Released = true;
        dragging = false;
        if (!p.drowbtn.isSelected()
                && !p.rubberbtn.isSelected()) {
            putCurrentShape(p.gc);
            p.overlayGraphics.clearRect(0, 0, p.overlay.getWidth(), p.overlay.getHeight());
            redoHistory.clear();
            Shape lastUndo = undoHistory.lastElement();
            lastUndo.setFill(p.gc.getFill());
            lastUndo.setFill(p.secondaryColor);
            lastUndo.setStroke(p.gc.getStroke());
            lastUndo.setStrokeWidth(p.gc.getLineWidth());
        }
    }

    public void sliderr(Observable e) {
        double width = p.slider.getValue();
        if (p.textbtn.isSelected()) {
            p.gc.setLineWidth(1);
            p.gc.setFont(Font.font(p.slider.getValue()));
            p.overlayGraphics.setLineWidth(1);
            p.overlayGraphics.setFont(Font.font(p.slider.getValue()));
            p.line_width.setText(String.format("%.1f", width));
            //return;
        }
        p.line_width.setText(String.format("%.1f", width));
        p.overlayGraphics.setLineWidth(width);
        p.gc.setLineWidth(width);

    }

    public void doOpenImage() {
        FileChooser openFile = new FileChooser();
        openFile.setTitle("Open File");
        File file = openFile.showOpenDialog(p.window);
        if (file != null) {
            try {
                InputStream io = new FileInputStream(file);
                Image image = new Image(io);
                if (image.isError()) {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR,
                            "Sorry, an error occurred while\ntrying to load the file:\n"
                            + image.getException().getMessage());
                    errorAlert.showAndWait();
                    return;
                }
                undoHistory.removeAllElements();
                redoHistory.removeAllElements();
                p.gc.drawImage(image, 0, 0, p.canvas.getWidth(), p.canvas.getHeight());
            } catch (IOException ex) {
                System.out.println("Error!");
            }
        }
    }

    public void doClearCanvas() {

        p.gc.setFill(p.backgroundColor);
        undoHistory.removeAllElements();
        redoHistory.removeAllElements();
        p.gc.fillRect(0, 0, p.canvas.getWidth(), p.canvas.getHeight());

    }

    public void doOpenNew() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("New File");
        alert.setHeaderText("New File");
        alert.setContentText("Are you sure that you want to exit this file, and create a new file?\nUnsaved changes will be lost.");

        Optional<ButtonType> res = alert.showAndWait();
        if (res.get() != ButtonType.OK) {
            return;
        }
    }

    public void doSaveImage() {
        Stage stage = new Stage(StageStyle.UTILITY);
        p.fileChooser.setTitle("Save Image As");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PNG files", "*.png");
        p.fileChooser.getExtensionFilters().add(extFilter);
        Image snapshot = p.canvas.snapshot(null, null);
        File file = p.fileChooser.showSaveDialog(stage);
        String filepath = file.getAbsolutePath();
        if (file != null) {
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "png", file);
            } catch (IOException ex) {
                System.out.println("Error!");
            }
        }
    }

    public Parent createZoomPane(final Group group) {
        final double SCALE_DELTA = 1.1;
        final StackPane zoomPane = new StackPane();

        zoomPane.getChildren().add(group);

        final ScrollPane scroller = new ScrollPane();
        // scroller.setFitToHeight(true);
        // scroller.setFitToWidth(true);
        //Always show vertical scroll bar scrollPane
        scroller.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        // Horizontal scroll bar is only displayed when needed
        scroller.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        final Group scrollContent = new Group(zoomPane);
        scroller.setContent(scrollContent);

        scroller.viewportBoundsProperty().addListener(new ChangeListener<Bounds>() {
            @Override
            public void changed(ObservableValue<? extends Bounds> observable,
                    Bounds oldValue, Bounds newValue) {
                zoomPane.setMinSize(newValue.getWidth(), newValue.getHeight());
            }
        });
//1080, 650
        scroller.setPrefViewportWidth(256);
        scroller.setPrefViewportHeight(256);

        zoomPane.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                event.consume();

                if (event.getDeltaY() == 0) {
                    return;
                }

                double scaleFactor = (event.getDeltaY() > 0) ? SCALE_DELTA
                        : 1 / SCALE_DELTA;

                // amount of scrolling in each direction in scrollContent coordinate
                // units
                Point2D scrollOffset = figureScrollOffset(scrollContent, scroller);

                group.setScaleX(group.getScaleX() * scaleFactor);
                group.setScaleY(group.getScaleY() * scaleFactor);

                // move viewport so that old center remains in the center after the
                // scaling
                repositionScroller(scrollContent, scroller, scaleFactor, scrollOffset);

            }
        });

        // Panning via drag....
//      
        // final ObjectProperty<Point2D> lastMouseCoordinates = new SimpleObjectProperty<Point2D>();
//        scrollContent.setOnMousePressed(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                lastMouseCoordinates.set(new Point2D(event.getX(), event.getY()));
//            }
//        });
//        scrollContent.setOnMouseDragged(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                double deltaX = event.getX() - lastMouseCoordinates.get().getX();
//                double extraWidth = scrollContent.getLayoutBounds().getWidth() - scroller.getViewportBounds().getWidth();
//                double deltaH = deltaX * (scroller.getHmax() - scroller.getHmin()) / extraWidth;
//                double desiredH = scroller.getHvalue() - deltaH;
//                scroller.setHvalue(Math.max(0, Math.min(scroller.getHmax(), desiredH)));
//
//                double deltaY = event.getY() - lastMouseCoordinates.get().getY();
//                double extraHeight = scrollContent.getLayoutBounds().getHeight() - scroller.getViewportBounds().getHeight();
//                double deltaV = deltaY * (scroller.getHmax() - scroller.getHmin()) / extraHeight;
//                double desiredV = scroller.getVvalue() - deltaV;
//                scroller.setVvalue(Math.max(0, Math.min(scroller.getVmax(), desiredV)));
//            }
//        });
        return scroller;
    }

    public Point2D figureScrollOffset(Node scrollContent, ScrollPane scroller) {
        double extraWidth = scrollContent.getLayoutBounds().getWidth() - scroller.getViewportBounds().getWidth();
        double hScrollProportion = (scroller.getHvalue() - scroller.getHmin()) / (scroller.getHmax() - scroller.getHmin());
        double scrollXOffset = hScrollProportion * Math.max(0, extraWidth);
        double extraHeight = scrollContent.getLayoutBounds().getHeight() - scroller.getViewportBounds().getHeight();
        double vScrollProportion = (scroller.getVvalue() - scroller.getVmin()) / (scroller.getVmax() - scroller.getVmin());
        double scrollYOffset = vScrollProportion * Math.max(0, extraHeight);
        return new Point2D(scrollXOffset, scrollYOffset);
    }

    public void repositionScroller(Node scrollContent, ScrollPane scroller, double scaleFactor, Point2D scrollOffset) {
        double scrollXOffset = scrollOffset.getX();
        double scrollYOffset = scrollOffset.getY();
        double extraWidth = scrollContent.getLayoutBounds().getWidth() - scroller.getViewportBounds().getWidth();
        if (extraWidth > 0) {
            double halfWidth = scroller.getViewportBounds().getWidth() / 2;
            double newScrollXOffset = (scaleFactor - 1) * halfWidth + scaleFactor * scrollXOffset;
            scroller.setHvalue(scroller.getHmin() + newScrollXOffset * (scroller.getHmax() - scroller.getHmin()) / extraWidth);
        } else {
            scroller.setHvalue(scroller.getHmin());
        }
        double extraHeight = scrollContent.getLayoutBounds().getHeight() - scroller.getViewportBounds().getHeight();
        if (extraHeight > 0) {
            double halfHeight = scroller.getViewportBounds().getHeight() / 2;
            double newScrollYOffset = (scaleFactor - 1) * halfHeight + scaleFactor * scrollYOffset;
            scroller.setVvalue(scroller.getVmin() + newScrollYOffset * (scroller.getVmax() - scroller.getVmin()) / extraHeight);
        } else {
            scroller.setHvalue(scroller.getHmin());
        }
    }

    public void doUndo() {
        if (!undoHistory.empty()) {
            p.gc.setFill(Color.WHITE);
            p.gc.fillRect(0, 0, 1080, 650);
            Shape removedShape = undoHistory.lastElement();
            javafx.scene.paint.Paint tempColor = removedShape.getFill();
            javafx.scene.paint.Paint intColor = removedShape.getStroke();
            double strWidth = removedShape.getStrokeWidth();
            if (removedShape.getClass() == Line.class) {
                Line tempLine = (Line) removedShape;
                tempLine.setFill(p.gc.getFill());
                tempLine.setStroke(p.gc.getStroke());
                tempLine.setStrokeWidth(p.gc.getLineWidth());
                redoHistory.push(new Line(tempLine.getStartX(), tempLine.getStartY(), tempLine.getEndX(), tempLine.getEndY()));
            } else if (removedShape.getClass() == Rectangle.class) {
                Rectangle tempRect = (Rectangle) removedShape;
                tempRect.setFill(p.gc.getFill());
                tempRect.setStroke(p.gc.getStroke());
                tempRect.setStrokeWidth(p.gc.getLineWidth());
                Rectangle temp = new Rectangle(tempRect.getX(), tempRect.getY(), tempRect.getWidth(), tempRect.getHeight());
                temp.setArcHeight(tempRect.getArcHeight());
                temp.setArcWidth(tempRect.getArcWidth());
                redoHistory.push(temp);
            } else if (removedShape.getClass() == Circle.class) {
                Circle tempCirc = (Circle) removedShape;
                tempCirc.setStrokeWidth(p.gc.getLineWidth());
                tempCirc.setFill(p.gc.getFill());
                tempCirc.setStroke(p.gc.getStroke());
                redoHistory.push(new Circle(tempCirc.getCenterX(), tempCirc.getCenterY(), tempCirc.getRadius()));
            } else if (removedShape.getClass() == Ellipse.class) {
                Ellipse tempElps = (Ellipse) removedShape;
                tempElps.setFill(p.gc.getFill());
                tempElps.setStroke(p.gc.getStroke());
                tempElps.setStrokeWidth(p.gc.getLineWidth());
                redoHistory.push(new Ellipse(tempElps.getCenterX(), tempElps.getCenterY(), tempElps.getRadiusX(), tempElps.getRadiusY()));
            } else if (removedShape.getClass() == Polygon.class) {
                Polygon temp = (Polygon) removedShape;
                removedShape.setFill(p.gc.getFill());
                removedShape.setStroke(p.gc.getStroke());
                removedShape.setStrokeWidth(p.gc.getLineWidth());

                int nbpoints = temp.getPoints().size();
                System.err.println("redo:" + nbpoints);
                if (nbpoints == 6) {//riangle
                    redoHistory.push(new Polygon(new double[]{temp.getPoints().get(0), temp.getPoints().get(1), temp.getPoints().get(2), temp.getPoints().get(3), temp.getPoints().get(4), temp.getPoints().get(5)}));
                } else if (nbpoints == 8) {//diamond
                    redoHistory.push(new Polygon(new double[]{temp.getPoints().get(0), temp.getPoints().get(1), temp.getPoints().get(2), temp.getPoints().get(3), temp.getPoints().get(4), temp.getPoints().get(5), temp.getPoints().get(6), temp.getPoints().get(7)}));
                } else if (nbpoints == 10) {
                    redoHistory.push(new Polygon(new double[]{temp.getPoints().get(0), temp.getPoints().get(1), temp.getPoints().get(2), temp.getPoints().get(3), temp.getPoints().get(4), temp.getPoints().get(5), temp.getPoints().get(6), temp.getPoints().get(7), temp.getPoints().get(8), temp.getPoints().get(9)}));
                } else if (nbpoints == 20) {
                    redoHistory.push(new Polygon(new double[]{temp.getPoints().get(0), temp.getPoints().get(1), temp.getPoints().get(2), temp.getPoints().get(3), temp.getPoints().get(4), temp.getPoints().get(5), temp.getPoints().get(6), temp.getPoints().get(7), temp.getPoints().get(8), temp.getPoints().get(9), temp.getPoints().get(10), temp.getPoints().get(11), temp.getPoints().get(12), temp.getPoints().get(13), temp.getPoints().get(14), temp.getPoints().get(15), temp.getPoints().get(16), temp.getPoints().get(17), temp.getPoints().get(18), temp.getPoints().get(19)}));
                } else if (nbpoints == 44) {
                    double[] d = new double[44];
                    for (int j = 0; j < nbpoints; j++) {

                        d[j] = temp.getPoints().get(j);
                        System.err.println("****" + d[j] + "****");
                    }
                    redoHistory.push(new Polygon(d));

                } else if (nbpoints == 12) {
                    redoHistory.push(new Polygon(new double[]{temp.getPoints().get(0), temp.getPoints().get(1), temp.getPoints().get(2), temp.getPoints().get(3), temp.getPoints().get(4), temp.getPoints().get(5), temp.getPoints().get(6), temp.getPoints().get(7), temp.getPoints().get(8), temp.getPoints().get(9), temp.getPoints().get(10), temp.getPoints().get(11)}));
                } else if (nbpoints == 80) {
                    double[] d = new double[80];
                    for (int j = 0; j < nbpoints; j++) {
                        d[j] = temp.getPoints().get(j);
                    }
                    redoHistory.push(new Polygon(d));
                } else if (nbpoints == 32) {
                    double[] d = new double[32];
                    for (int j = 0; j < nbpoints; j++) {
                        d[j] = temp.getPoints().get(j);
                    }
                    redoHistory.push(new Polygon(d));
                }

            }
            Shape lastRedo = redoHistory.lastElement();
            lastRedo.setFill(tempColor);
            lastRedo.setStroke(intColor);
            lastRedo.setStrokeWidth(strWidth);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            undoHistory.pop();
            for (int i = 0; i < undoHistory.size(); i++) {
                Shape shape = undoHistory.elementAt(i);
                if (shape.getClass() == Line.class) {
                    Line temp = (Line) shape;
                    p.gc.setLineWidth(temp.getStrokeWidth());
                    p.gc.setStroke(temp.getStroke());
                    p.gc.setFill(temp.getFill());
                    p.gc.strokeLine(temp.getStartX(), temp.getStartY(), temp.getEndX(), temp.getEndY());
                } else if (shape.getClass() == Rectangle.class) {
                    Rectangle temp = (Rectangle) shape;
                    p.gc.setLineWidth(temp.getStrokeWidth());
                    p.gc.setStroke(temp.getStroke());
                    p.gc.setFill(temp.getFill());
                    p.gc.fillRoundRect(temp.getX(), temp.getY(), temp.getWidth(), temp.getHeight(), temp.getArcWidth(), temp.getArcHeight());
                    p.gc.strokeRoundRect(temp.getX(), temp.getY(), temp.getWidth(), temp.getHeight(), temp.getArcWidth(), temp.getArcHeight());
                } else if (shape.getClass() == Circle.class) {
                    Circle temp = (Circle) shape;
                    p.gc.setLineWidth(temp.getStrokeWidth());
                    p.gc.setStroke(temp.getStroke());
                    p.gc.setFill(temp.getFill());
                    p.gc.fillOval(temp.getCenterX(), temp.getCenterY(), temp.getRadius(), temp.getRadius());
                    p.gc.strokeOval(temp.getCenterX(), temp.getCenterY(), temp.getRadius(), temp.getRadius());
                } else if (shape.getClass() == Ellipse.class) {
                    Ellipse temp = (Ellipse) shape;
                    p.gc.setLineWidth(temp.getStrokeWidth());
                    p.gc.setStroke(temp.getStroke());
                    p.gc.setFill(temp.getFill());
                    p.gc.fillOval(temp.getCenterX(), temp.getCenterY(), temp.getRadiusX(), temp.getRadiusY());
                    p.gc.strokeOval(temp.getCenterX(), temp.getCenterY(), temp.getRadiusX(), temp.getRadiusY());
                } else if (shape.getClass() == Polygon.class) {
                    //6 =triangle , 8 = diamond ,10=pentagon,6 = hexagon,
                    Polygon temp = (Polygon) shape;
                    int nbpoints = temp.getPoints().size();
                    p.gc.setLineWidth(temp.getStrokeWidth());
                    p.gc.setStroke(temp.getStroke());
                    p.gc.setFill(temp.getFill());
                    if (nbpoints == 6) {
                        p.gc.fillPolygon(new double[]{temp.getPoints().get(0), temp.getPoints().get(2), temp.getPoints().get(4)}, new double[]{temp.getPoints().get(1), temp.getPoints().get(3), temp.getPoints().get(5)}, 3);
                        p.gc.strokePolygon(new double[]{temp.getPoints().get(0), temp.getPoints().get(2), temp.getPoints().get(4)}, new double[]{temp.getPoints().get(1), temp.getPoints().get(3), temp.getPoints().get(5)}, 3);
                    } else if (nbpoints == 8) {
                        p.gc.fillPolygon(new double[]{temp.getPoints().get(0), temp.getPoints().get(2), temp.getPoints().get(4), temp.getPoints().get(6)}, new double[]{temp.getPoints().get(1), temp.getPoints().get(3), temp.getPoints().get(5), temp.getPoints().get(7)}, 4);
                        p.gc.strokePolygon(new double[]{temp.getPoints().get(0), temp.getPoints().get(2), temp.getPoints().get(4), temp.getPoints().get(6)}, new double[]{temp.getPoints().get(1), temp.getPoints().get(3), temp.getPoints().get(5), temp.getPoints().get(7)}, 4);
                    } else if (nbpoints == 10) {
                        p.gc.fillPolygon(new double[]{temp.getPoints().get(0), temp.getPoints().get(2), temp.getPoints().get(4), temp.getPoints().get(6), temp.getPoints().get(8)}, new double[]{temp.getPoints().get(1), temp.getPoints().get(3), temp.getPoints().get(5), temp.getPoints().get(7), temp.getPoints().get(9)}, 5);
                        p.gc.strokePolygon(new double[]{temp.getPoints().get(0), temp.getPoints().get(2), temp.getPoints().get(4), temp.getPoints().get(6), temp.getPoints().get(8)}, new double[]{temp.getPoints().get(1), temp.getPoints().get(3), temp.getPoints().get(5), temp.getPoints().get(7), temp.getPoints().get(9)}, 5);

                    } else if (nbpoints == 20) {
                        p.gc.fillPolygon(new double[]{temp.getPoints().get(0), temp.getPoints().get(2), temp.getPoints().get(4), temp.getPoints().get(6), temp.getPoints().get(8), temp.getPoints().get(10), temp.getPoints().get(12), temp.getPoints().get(14), temp.getPoints().get(16), temp.getPoints().get(18)}, new double[]{temp.getPoints().get(1), temp.getPoints().get(3), temp.getPoints().get(5), temp.getPoints().get(7), temp.getPoints().get(9), temp.getPoints().get(11), temp.getPoints().get(13), temp.getPoints().get(15), temp.getPoints().get(17), temp.getPoints().get(19)}, 10);
                        p.gc.strokePolygon(new double[]{temp.getPoints().get(0), temp.getPoints().get(2), temp.getPoints().get(4), temp.getPoints().get(6), temp.getPoints().get(8), temp.getPoints().get(10), temp.getPoints().get(12), temp.getPoints().get(14), temp.getPoints().get(16), temp.getPoints().get(18)}, new double[]{temp.getPoints().get(1), temp.getPoints().get(3), temp.getPoints().get(5), temp.getPoints().get(7), temp.getPoints().get(9), temp.getPoints().get(11), temp.getPoints().get(13), temp.getPoints().get(15), temp.getPoints().get(17), temp.getPoints().get(19)}, 10);

                    } else if (nbpoints == 44) {
                        System.err.println("//****" + "in redo" + "****//"
                                + "");
                        double[] x = new double[22];
                        double[] y = new double[22];
                        int k = 0, z = 0;
                        for (int j = 0; j < nbpoints; j++) {

                            if (j % 2 == 0) {
                                x[k] = temp.getPoints().get(j);
                                k++;
                            } else {
                                y[z] = temp.getPoints().get(j);
                                z++;
                            }
                        }
                        p.gc.fillPolygon(x, y, 22);
                        p.gc.strokePolygon(x, y, 22);
                    } else if (nbpoints == 12) {
                        p.gc.fillPolygon(new double[]{temp.getPoints().get(0), temp.getPoints().get(2), temp.getPoints().get(4), temp.getPoints().get(6), temp.getPoints().get(8), temp.getPoints().get(10)}, new double[]{temp.getPoints().get(1), temp.getPoints().get(3), temp.getPoints().get(5), temp.getPoints().get(7), temp.getPoints().get(9), temp.getPoints().get(11)}, 6);
                        p.gc.strokePolygon(new double[]{temp.getPoints().get(0), temp.getPoints().get(2), temp.getPoints().get(4), temp.getPoints().get(6), temp.getPoints().get(8), temp.getPoints().get(10)}, new double[]{temp.getPoints().get(1), temp.getPoints().get(3), temp.getPoints().get(5), temp.getPoints().get(7), temp.getPoints().get(9), temp.getPoints().get(11)}, 6);

                    } else if (nbpoints == 80) {
                        double[] x = new double[40];
                        double[] y = new double[40];
                        int k = 0, z = 0;
                        for (int j = 0; j < nbpoints; j++) {

                            if (j % 2 == 0) {
                                x[k] = temp.getPoints().get(j);
                                k++;
                            } else {
                                y[z] = temp.getPoints().get(j);
                                z++;
                            }
                        }
                        p.gc.fillPolygon(x, y, 40);
                        p.gc.strokePolygon(x, y, 40);
                    } else if (nbpoints == 32) {

                        double[] x = new double[16];
                        double[] y = new double[16];
                        int k = 0, z = 0;
                        for (int j = 0; j < nbpoints; j++) {

                            if (j % 2 == 0) {
                                x[k] = temp.getPoints().get(j);
                                k++;
                            } else {
                                y[z] = temp.getPoints().get(j);
                                z++;
                            }
                        }
                        p.gc.fillPolygon(x, y, 16);
                        p.gc.strokePolygon(x, y, 16);
                    }
                }
            }
        } else {
            System.out.println("there is no action to undo");
        }
    }

    public void doRedo() {
        if (!redoHistory.empty()) {
            Shape shape = redoHistory.lastElement();
            p.gc.setLineWidth(shape.getStrokeWidth());
            p.gc.setStroke(shape.getStroke());
            p.gc.setFill(shape.getFill());
            redoHistory.pop();
            if (shape.getClass() == Line.class) {
                Line tempLine = (Line) shape;
                p.gc.strokeLine(tempLine.getStartX(), tempLine.getStartY(), tempLine.getEndX(), tempLine.getEndY());
                undoHistory.push(new Line(tempLine.getStartX(), tempLine.getStartY(), tempLine.getEndX(), tempLine.getEndY()));
            } else if (shape.getClass() == Rectangle.class) {
                Rectangle tempRect = (Rectangle) shape;
                p.gc.fillRoundRect(tempRect.getX(), tempRect.getY(), tempRect.getWidth(), tempRect.getHeight(), tempRect.getArcWidth(), tempRect.getArcHeight());
                p.gc.strokeRoundRect(tempRect.getX(), tempRect.getY(), tempRect.getWidth(), tempRect.getHeight(), tempRect.getArcWidth(), tempRect.getArcHeight());
                Rectangle temp = new Rectangle(tempRect.getX(), tempRect.getY(), tempRect.getWidth(), tempRect.getHeight());
                temp.setArcHeight(tempRect.getArcHeight());
                temp.setArcWidth(tempRect.getArcWidth());
                undoHistory.push(temp);
            } else if (shape.getClass() == Circle.class) {
                Circle tempCirc = (Circle) shape;
                p.gc.fillOval(tempCirc.getCenterX(), tempCirc.getCenterY(), tempCirc.getRadius(), tempCirc.getRadius());
                p.gc.strokeOval(tempCirc.getCenterX(), tempCirc.getCenterY(), tempCirc.getRadius(), tempCirc.getRadius());
                undoHistory.push(new Circle(tempCirc.getCenterX(), tempCirc.getCenterY(), tempCirc.getRadius()));
            } else if (shape.getClass() == Ellipse.class) {
                Ellipse tempElps = (Ellipse) shape;
                p.gc.fillOval(tempElps.getCenterX(), tempElps.getCenterY(), tempElps.getRadiusX(), tempElps.getRadiusY());
                p.gc.strokeOval(tempElps.getCenterX(), tempElps.getCenterY(), tempElps.getRadiusX(), tempElps.getRadiusY());
                undoHistory.push(new Ellipse(tempElps.getCenterX(), tempElps.getCenterY(), tempElps.getRadiusX(), tempElps.getRadiusY()));
            } else if (shape.getClass() == Polygon.class) {
                Polygon temp = (Polygon) shape;
                int nbpoints = temp.getPoints().size();
                System.err.println("redo:" + nbpoints);
                if (nbpoints == 6) {
                    p.gc.fillPolygon(new double[]{temp.getPoints().get(0), temp.getPoints().get(2), temp.getPoints().get(4)}, new double[]{temp.getPoints().get(1), temp.getPoints().get(3), temp.getPoints().get(5)}, 3);
                    p.gc.strokePolygon(new double[]{temp.getPoints().get(0), temp.getPoints().get(2), temp.getPoints().get(4)}, new double[]{temp.getPoints().get(1), temp.getPoints().get(3), temp.getPoints().get(5)}, 3);
                    undoHistory.push(new Polygon(new double[]{temp.getPoints().get(0), temp.getPoints().get(1), temp.getPoints().get(2), temp.getPoints().get(3), temp.getPoints().get(4), temp.getPoints().get(5)}));
                } else if (nbpoints == 8) {
                    p.gc.fillPolygon(new double[]{temp.getPoints().get(0), temp.getPoints().get(2), temp.getPoints().get(4), temp.getPoints().get(6)}, new double[]{temp.getPoints().get(1), temp.getPoints().get(3), temp.getPoints().get(5), temp.getPoints().get(7)}, 4);
                    p.gc.strokePolygon(new double[]{temp.getPoints().get(0), temp.getPoints().get(2), temp.getPoints().get(4), temp.getPoints().get(6)}, new double[]{temp.getPoints().get(1), temp.getPoints().get(3), temp.getPoints().get(5), temp.getPoints().get(7)}, 4);
                    undoHistory.push(new Polygon(new double[]{temp.getPoints().get(0), temp.getPoints().get(1), temp.getPoints().get(2), temp.getPoints().get(3), temp.getPoints().get(4), temp.getPoints().get(5), temp.getPoints().get(6), temp.getPoints().get(7)}));
                } else if (nbpoints == 10) {
                    p.gc.fillPolygon(new double[]{temp.getPoints().get(0), temp.getPoints().get(2), temp.getPoints().get(4), temp.getPoints().get(6), temp.getPoints().get(8)}, new double[]{temp.getPoints().get(1), temp.getPoints().get(3), temp.getPoints().get(5), temp.getPoints().get(7), temp.getPoints().get(9)}, 5);
                    p.gc.strokePolygon(new double[]{temp.getPoints().get(0), temp.getPoints().get(2), temp.getPoints().get(4), temp.getPoints().get(6), temp.getPoints().get(8)}, new double[]{temp.getPoints().get(1), temp.getPoints().get(3), temp.getPoints().get(5), temp.getPoints().get(7), temp.getPoints().get(9)}, 5);
                    undoHistory.push(new Polygon(new double[]{temp.getPoints().get(0), temp.getPoints().get(1), temp.getPoints().get(2), temp.getPoints().get(3), temp.getPoints().get(4), temp.getPoints().get(5), temp.getPoints().get(6), temp.getPoints().get(7), temp.getPoints().get(8), temp.getPoints().get(9)}));
                } else if (nbpoints == 20) {
                    p.gc.fillPolygon(new double[]{temp.getPoints().get(0), temp.getPoints().get(2), temp.getPoints().get(4), temp.getPoints().get(6), temp.getPoints().get(8), temp.getPoints().get(10), temp.getPoints().get(12), temp.getPoints().get(14), temp.getPoints().get(16), temp.getPoints().get(18)}, new double[]{temp.getPoints().get(1), temp.getPoints().get(3), temp.getPoints().get(5), temp.getPoints().get(7), temp.getPoints().get(9), temp.getPoints().get(11), temp.getPoints().get(13), temp.getPoints().get(15), temp.getPoints().get(17), temp.getPoints().get(19)}, 10);
                    p.gc.strokePolygon(new double[]{temp.getPoints().get(0), temp.getPoints().get(2), temp.getPoints().get(4), temp.getPoints().get(6), temp.getPoints().get(8), temp.getPoints().get(10), temp.getPoints().get(12), temp.getPoints().get(14), temp.getPoints().get(16), temp.getPoints().get(18)}, new double[]{temp.getPoints().get(1), temp.getPoints().get(3), temp.getPoints().get(5), temp.getPoints().get(7), temp.getPoints().get(9), temp.getPoints().get(11), temp.getPoints().get(13), temp.getPoints().get(15), temp.getPoints().get(17), temp.getPoints().get(19)}, 10);
                    undoHistory.push(new Polygon(new double[]{temp.getPoints().get(0), temp.getPoints().get(1), temp.getPoints().get(2), temp.getPoints().get(3), temp.getPoints().get(4), temp.getPoints().get(5), temp.getPoints().get(6), temp.getPoints().get(7), temp.getPoints().get(8), temp.getPoints().get(9), temp.getPoints().get(10), temp.getPoints().get(11), temp.getPoints().get(12), temp.getPoints().get(13), temp.getPoints().get(14), temp.getPoints().get(15), temp.getPoints().get(16), temp.getPoints().get(17), temp.getPoints().get(18), temp.getPoints().get(19)}));
                } else if (nbpoints == 44) {//henda star
                    double[] x = new double[22];
                    double[] y = new double[22];
                    double[] d = new double[44];
                    int k = 0, z = 0;
                    for (int j = 0; j < nbpoints; j++) {

                        if (j % 2 == 0) {
                            x[k] = temp.getPoints().get(j);
                            System.out.println("doRedo() " + x[k]);
                            k++;
                        } else {
                            y[z] = temp.getPoints().get(j);
                            System.out.println("doRedo() " + y[z]);
                            z++;
                        }
                        d[j] = temp.getPoints().get(j);
                    }
                    p.gc.fillPolygon(x, y, 22);
                    p.gc.strokePolygon(x, y, 22);

                    undoHistory.push(new Polygon(d));

                } else if (nbpoints == 12) {
                    p.gc.fillPolygon(new double[]{temp.getPoints().get(0), temp.getPoints().get(2), temp.getPoints().get(4), temp.getPoints().get(6), temp.getPoints().get(8), temp.getPoints().get(10)}, new double[]{temp.getPoints().get(1), temp.getPoints().get(3), temp.getPoints().get(5), temp.getPoints().get(7), temp.getPoints().get(9), temp.getPoints().get(11)}, 6);
                    p.gc.strokePolygon(new double[]{temp.getPoints().get(0), temp.getPoints().get(2), temp.getPoints().get(4), temp.getPoints().get(6), temp.getPoints().get(8), temp.getPoints().get(10)}, new double[]{temp.getPoints().get(1), temp.getPoints().get(3), temp.getPoints().get(5), temp.getPoints().get(7), temp.getPoints().get(9), temp.getPoints().get(11)}, 6);
                    undoHistory.push(new Polygon(new double[]{temp.getPoints().get(0), temp.getPoints().get(1), temp.getPoints().get(2), temp.getPoints().get(3), temp.getPoints().get(4), temp.getPoints().get(5), temp.getPoints().get(6), temp.getPoints().get(7), temp.getPoints().get(8), temp.getPoints().get(9), temp.getPoints().get(10), temp.getPoints().get(11)}));
                } else if (nbpoints == 80) {//henda star
                    double[] x = new double[40];
                    double[] y = new double[40];
                    double[] d = new double[80];
                    int k = 0, z = 0;
                    for (int j = 0; j < nbpoints; j++) {

                        if (j % 2 == 0) {
                            x[k] = temp.getPoints().get(j);
                            k++;
                        } else {
                            y[z] = temp.getPoints().get(j);
                            z++;
                        }
                        d[j] = temp.getPoints().get(j);
                    }
                    p.gc.fillPolygon(x, y, 40);
                    p.gc.strokePolygon(x, y, 40);
                    undoHistory.push(new Polygon(d));
                } else if (nbpoints == 32) {//henda star
                    double[] x = new double[16];
                    double[] y = new double[16];
                    double[] d = new double[32];
                    int k = 0, z = 0;
                    for (int j = 0; j < nbpoints; j++) {

                        if (j % 2 == 0) {
                            x[k] = temp.getPoints().get(j);
                            k++;
                        } else {
                            y[z] = temp.getPoints().get(j);
                            z++;
                        }
                        d[j] = temp.getPoints().get(j);
                    }
                    p.gc.fillPolygon(x, y, 16);
                    p.gc.strokePolygon(x, y, 16);
                    undoHistory.push(new Polygon(d));
                }
            }
            Shape lastUndo = undoHistory.lastElement();
            lastUndo.setFill(p.gc.getFill());
            lastUndo.setStroke(p.gc.getStroke());
            lastUndo.setStrokeWidth(p.gc.getLineWidth());
        } else {
            System.out.println("there is no action to redo");
        }
    }

    private void putCurrentShape(GraphicsContext g) {

        shapeDraw shape = null;
        shapeFactory sf = new shapeFactory();
        if (p.linebtn.isSelected()) {
            shape = sf.getShape("Line");
        } else if (p.elpslebtn.isSelected()) {
            shape = sf.getShape("Oval");
        } else if (p.rectbtn.isSelected()) {
            shape = sf.getShape("Rectangle");
        } else if (p.roundrectbtn.isSelected()) {
            shape = sf.getShape("RoundRectangle");
        } else if (p.circlebtn.isSelected()) {
            shape = sf.getShape("Circle");
        } else if (p.squareBtn.isSelected()) {
            shape = sf.getShape("Square");
        } else if (p.triaBtn.isSelected()) {
            shape = sf.getShape("Triangle");
        } else if (p.isopentagonbtn.isSelected()) {
            shape = sf.getShape("RightPentagon");
        } else if (p.hexagonbtn.isSelected()) {
            shape = sf.getShape("Hexagon");
        } else if (p.diamondbtn.isSelected()) {
            shape = sf.getShape("Diamond");
        } else if (p.pentagonbtn.isSelected()) {
            shape = sf.getShape("Pentagon");
        } else if (p.lrighttriabtn.isSelected()) {
            shape = sf.getShape("Triangle90");
        } else if (p.starbtn.isSelected()) {
            shape = sf.getShape("Star");
        } else if (p.heptabtn.isSelected()) {
            shape = sf.getShape("hepta");
        } else if (p.hendabtn.isSelected()) {
            shape = sf.getShape("henda");
        } else if (p.pentabtn.isSelected()) {
            shape = sf.getShape("penta");
        }

        shape.draw(g, true, startX, startY, currentX, currentY, Released, undoHistory);
    }

}
