package mvc.View;

import mvc.controller.viewController;

public class View {
    public void start() throws FileNotFoundException {
      Stage  window = primaryStage;
        /*--------------btncircle image-------------*/
        FileInputStream input1 = new FileInputStream("src/mvc/view/images/circle.png");
        Image image1 = new Image(input1);
        ImageView imageView_circle = new ImageView(image1);
        imageView_circle.setFitWidth(20);
        imageView_circle.setFitHeight(20);
        /* ----------btns---------- */
        rectbtn = new ToggleButton("", imageView_rectangle);//circle
        circlebtn = new ToggleButton("", imageView_circle);//rectangle
        ToggleButton[] toolsArr = {drowbtn, rubberbtn, linebtn, rectbtn};
        ToggleGroup toolbucket = new ToggleGroup();
        bucketbtn.setToggleGroup(toolbucket);
        ToggleGroup tools = new ToggleGroup();
        for (ToggleButton tool : toolsArr) {
            tool.setMinWidth(40);
            tool.setMinHeight(40);
            //tool.setStyle("-fx-background-color:#D3D3D3");
            tool.setToggleGroup(tools);
            tool.setCursor(Cursor.HAND);
        }
        FlowPane flow1 = new FlowPane();
        flow1.getChildren().add(drowbtn);
        flow1.getChildren().add(rubberbtn);
        flow1.getChildren().add(linebtn);
        flow1.setVgap(1);
        flow1.setHgap(1);
    
       TextArea text = new TextArea();
        text.setPrefRowCount(1);
        text.setPromptText("Enter Text");
        //Slider slider = new Slider(1, 50, 3);
        slider = new Slider(MINSTROKE, MAXSTROKE, DEFAULTSTROKE);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
       
      ComboBox  combobox = new ComboBox();
        ArrayList<String> a = new ArrayList<>();
        for (int i = 0; i < javafx.scene.text.Font.getFamilies().size(); i++) {
            a.add(javafx.scene.text.Font.getFamilies().get(i));
        }
        combobox.getItems().addAll(a);
        combobox.setValue(javafx.scene.text.Font.getFamilies().get(0));

        sampleLine.strokeWidthProperty().bind(slider.valueProperty());
        sampleLine.setStroke(Color.BLACK);
        StackPane stackpane = new StackPane();
        stackpane.setPrefHeight(MAXSTROKE);
        stackpane.setPrefWidth(sampleLine.getEndX() + MAXSTROKE);
        stackpane.setAlignment(Pos.CENTER);
        stackpane.getChildren().add(sampleLine);
        Label line_color = new Label("Black");
        Label text_type = new Label("Font");
    
        stackpane.setStyle("-fx-border-color: black;");
        stackpane.setPrefWidth(50.0);
        stackpane.setPrefHeight(50.0);
        Label fill_color = new Label("Transparent");
        line_width = new Label("3.0");
        //***************************************************************************
        VBox all = new VBox(15);
        all.setPadding(new Insets(2));
        all.setStyle("-fx-background-color: #D3D3D3");
        all.setPrefWidth(130);
        VBox btns = new VBox(1);
        VBox btns1 = new VBox(1);
        VBox vboxtext = new VBox();
        btns.getChildren().addAll(flow1,flow7);
        btns.setPadding(new Insets(2));
        btns.setStyle("-fx-background-color: #D3D3D3");
        btns.setPrefWidth(135);
        vboxtext.getChildren().addAll(text, combobox);//text_type,
        vboxtext.visibleProperty().bind(textbtn.selectedProperty());
        vboxtext.managedProperty().bind(textbtn.selectedProperty());
        //***************************************************************************
        btns1.setPadding(new Insets(20));
        btns1.getChildren().addAll(stackpane, slider);//, linesbox);
        btns1.setPadding(new Insets(2));
        btns1.setStyle("-fx-background-color: #D3D3D3");
        btns1.setPrefWidth(135);
        all.getChildren().addAll(btns, vboxtext, btns1);
        /* ----------Drow Canvas---------- */
        canvas = new Canvas(1080, 650);
        gc = canvas.getGraphicsContext2D();
        gc.setLineWidth(1);
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, 1080, 650);
        overlay = new Canvas(1080, 650);
        overlayGraphics = overlay.getGraphicsContext2D();
        overlayGraphics.setLineWidth(2);
        StackPane canvasHolder = new StackPane(canvas, overlay);
        viewController controller = new viewController(this);
//******************************************************************************
        overlay.setOnMousePressed(e -> {
            controller.mousePressed(this, e);  });
        overlay.setOnMouseDragged(e -> {
            controller.mouseDragged(this, e); });
        overlay.setOnMouseReleased(e -> {
            controller.mouseReleased(this, e); });
        slider.valueProperty().addListener(e -> {
            controller.sliderr(e);});
        /* ----------STAGE & SCENE---------- */
        pane = new BorderPane();
        pane.setLeft(all);
        // pane.setCenter(canvasHolder)
        HBox hboxbottom = new HBox();
        hboxbottom.setStyle("-fx-background-color: #D3D3D3;");
//********************************************************************************
        final ToggleGroup groupcolor = new ToggleGroup();
        ToggleButton tb1 = new ToggleButton("");
        tb1.setToggleGroup(groupcolor);
        tb1.setSelected(true);
        ToggleButton tb2 = new ToggleButton("");
        tb2.setToggleGroup(groupcolor);
        tb2.setBorder(Border.EMPTY);
        tb1.setOnAction(event -> {
            tb2.setBorder(Border.EMPTY);  });
        VBox vl = new VBox();
        VBox vf = new VBox();
        vl.getChildren().addAll(tb1, line_color);
        vf.getChildren().addAll(tb2, fill_color);
        tb1.setBackground(...);
		//*********************************************************************************
        HBox hbottom = new HBox();
        hbottom.setPadding(new Insets(20));
        hbottom.setSpacing(10);
        FlowPane flow = new FlowPane();
        flow.setVgap(5);
        flow.setHgap(5);
        flow.setPrefWrapLength(980);	
        hbottom.getChildren().addAll(vl, vf, flow);
        hboxbottom.getChildren().add(hbottom);
//********************************************************************************
        bucketbtn.setOnAction(event -> {
            if (bucketbtn.isSelected() == true) {
                secondaryColor = primaryColor;
                fill_color.setText("" + fill_text);
            } else { }
        });
//********************************************************************************
    BorderPane pane = new BorderPane();
        pane.setLeft(btns);
        pane.setCenter(CanvasHolder);
        pane.setBottom(hboxbottom);
//*********************************************************************************
        MenuBar menuBar = new MenuBar();
        pane.setTop(menuBar);
        Menu menu_save = new Menu("");
        menu_save.setGraphic(imageView_save);
        Menu fileMenu = new Menu("File");
        MenuItem saveMenuItem = new MenuItem("Save", imageView_save);
        MenuItem exitMenuItem = new MenuItem("Exit");
        newMenuItem.setOnAction(e -> {
            controller.doOpenNew();
            controller.doClearCanvas();});
        openMenuItem.setOnAction(e -> {
            controller.doOpenImage(); });
        saveMenuItem.setOnAction(e -> {
            controller.doSaveImage(); });
       
        Label undolabel = new Label("undo");
        Label redolabel = new Label("redo");
        undolabel.setOnMouseClicked(mouseEvent -> {
            controller.doUndo(); });
        redolabel.setOnMouseClicked(mouseEvent -> {
            controller.doRedo(); });

        Menu menu_undo = new Menu("");
        menu_undo.setGraphic(undolabel);
        Menu menu_redo = new Menu("");
        menu_redo.setGraphic(redolabel);
        fileMenu.getItems().addAll(newMenuItem, saveMenuItem, openMenuItem, new SeparatorMenuItem(), exitMenuItem);
         menuBar.getMenus().add(fileMenu);
        menuBar.getMenus().add(menu_undo);
        menuBar.getMenus().add(menu_redo);
        zoomInMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.I));

    }

}
