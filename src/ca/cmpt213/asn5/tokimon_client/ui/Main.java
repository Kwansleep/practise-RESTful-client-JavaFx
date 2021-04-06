package ca.cmpt213.asn5.tokimon_client.ui;


import ca.cmpt213.asn5.tokimon_client.model.Tokimon;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.Gson;

public class Main extends Application {

    // Constants
    final static public String SERVER_ADDRESS = "http://localhost:8080/api/tokimon/";
    private static final String SIDE_BAR_COLOR = "#2cdfe6";
    private static final String MAIN_BACKGROUND_COLOR = "#33f5ff";
    private static final String SUB_BACKGROUND_COLOR = "#30c6fc";
    final private double PANEL_PERCENT = 0.2;
    final private long WINDOW_WIDTH = 480;
    final private long WINDOW_HEIGHT = 640;
    final private String SIDE_BUTTON_STYLE = "-fx-background-color: " + SIDE_BAR_COLOR;

    // UI elements
    final private VBox selectionPanel = new VBox();
    final private HBox root = new HBox();
    final private AnchorPane contentBox = new AnchorPane();
    // UI elements that needs refresh;
    private Text contentTitle;
    ScrollPane scrollPane;

    Pane contentDisplay = new Pane();
    final List<Pane> scenes = new ArrayList<>();
    final List<String> sceneTitles = new ArrayList<>();
    ComboBox<Tokimon> comboBox;

    @Override
    public void start(Stage primaryStage) throws Exception{
        //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("TokiTool-client");

        setUpSelectionPanel(primaryStage);
        root.getChildren().add(selectionPanel);

        setUpContentBox(primaryStage);
        root.getChildren().add(contentBox);

        primaryStage.setScene(new Scene(root, WINDOW_HEIGHT, WINDOW_WIDTH));
        primaryStage.show();

        createScenes();
    }

    // Rest API
    private void postAddTokimon(Tokimon tokimon) {
        try {
            URL url = new URL(SERVER_ADDRESS + "add");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type","application/json");

            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());

            Gson gson = new Gson();
            String jsonTokimon = gson.toJson(tokimon);
            //jsonTokimon = jsonTokimon.replace("\"","\\\"");
            System.out.println(jsonTokimon);
            out.write(jsonTokimon);
            out.flush();
            out.close();

            connection.connect();
            System.out.println(connection.getResponseCode());
            connection.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private ObservableList<Tokimon> getTokimons(){
        ObservableList<Tokimon> tokimons = FXCollections.observableArrayList();

        URL url = null;
        try {
            url = new URL(SERVER_ADDRESS + "all");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String output;
            // read all lines , https://stackoverflow.com/questions/28977308/read-all-lines-with-bufferedreader

            output =  br.lines().collect(Collectors.joining());

            System.out.println(output);

            Tokimon[] tokimonsArray = new Gson().fromJson(output,Tokimon[].class);
            tokimons.addAll(tokimonsArray);

            System.out.println(connection.getResponseCode());
            connection.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return tokimons;
    }
    private void postChangeTokimon(long id, Tokimon tokimon) {
        try {
            URL url = new URL(SERVER_ADDRESS + "change/" + id);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type","application/json");

            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());

            Gson gson = new Gson();
            String jsonTokimon = gson.toJson(tokimon);
            out.write(jsonTokimon);
            out.flush();
            out.close();

            connection.connect();
            System.out.println(connection.getResponseCode());
            connection.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void deleteTokimon(Long id) {
        try {
            URL url = new URL(SERVER_ADDRESS + id);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("DELETE");

            System.out.println(connection.getResponseCode());
            connection.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // set up UI
    // different "Scenes"
    private void createScenes() {
        // default scene
        StackPane defaultScene = new StackPane();
        Label defaultLabel = new Label("Default");
        defaultScene.getChildren().add(defaultLabel);
        defaultScene.setAlignment(defaultLabel,Pos.CENTER);
        setColor(defaultScene,Color.valueOf(SUB_BACKGROUND_COLOR));
        scenes.add(defaultScene);
        sceneTitles.add("default scene");

        // Scene 1
        // using gridPane for input form, referenced here http://www.java2s.com/Tutorials/Java/JavaFX_How_to/GridPane/Use_GridPane_to_layout_input_form.htm
        GridPane addTokimonPane = createAddTokimonPane();
        setColor(addTokimonPane,Color.valueOf(SUB_BACKGROUND_COLOR));
        scenes.add(addTokimonPane);
        sceneTitles.add("Add Tokimon");

        // Scene 2
        Pane changeTokimonPane = createChangeTokimonPane();
        setColor(changeTokimonPane,Color.valueOf(SUB_BACKGROUND_COLOR));
        scenes.add(changeTokimonPane);
        sceneTitles.add("Change Tokimon");

        // Scene 3
        Pane viewTokimonPane = createViewTokimonPane();
        setColor(viewTokimonPane,Color.valueOf(SUB_BACKGROUND_COLOR));
        scenes.add(viewTokimonPane);
        sceneTitles.add("View Tokimon");

        // Scene 4
        Pane deleteTokimonPane = createDeleteTokimonPane();
        setColor(deleteTokimonPane,Color.valueOf(SUB_BACKGROUND_COLOR));
        scenes.add(deleteTokimonPane);
        sceneTitles.add("Delete Tokimon");

        assert(scenes.size() == sceneTitles.size());
        for(Pane pane:scenes){
            pane.prefWidthProperty().bind(contentDisplay.widthProperty());
            pane.prefHeightProperty().bind(contentDisplay.heightProperty());
        }
    }
    private VBox createChangeTokimonPane() {

        VBox pane = new VBox();
        // EDIT GRID
        GridPane editGrid = new GridPane();
        editGrid.setHgap(5);
        editGrid.setVgap(5);
        // create Input Labels
        editGrid.add(new Label("id:"),0,1);
        editGrid.add(new Label("name:"),0,2);
        editGrid.add(new Label("weight:"),0,3);
        editGrid.add(new Label("height:"),0,4);
        editGrid.add(new Label("ability:"),0,5);
        editGrid.add(new Label("strength:"),0,6);
        editGrid.add(new Label("color:"),0,7);
        // create Input fields
        Text inputID = new Text("N/A");
        editGrid.add(inputID,2,1);
        TextField inputName = new TextField();
        inputName.setDisable(true);
        editGrid.add(inputName,2,2);
        TextField inputWeight = new TextField();
        inputWeight.setDisable(true);
        editGrid.add(inputWeight,2,3);
        TextField inputHeight = new TextField();
        inputHeight.setDisable(true);
        editGrid.add(inputHeight,2,4);
        TextField inputAbility = new TextField();
        inputAbility.setDisable(true);
        editGrid.add(inputAbility,2,5);
        TextField inputStrength = new TextField();
        inputStrength.setDisable(true);
        editGrid.add(inputStrength,2,6);
        TextField inputColor = new TextField();
        inputColor.setDisable(true);
        editGrid.add(inputColor,2,7);

        // add button
        Button add = new Button("change");

        add.addEventHandler(MouseEvent.MOUSE_CLICKED, v->{

            long id = Long.parseLong(inputID.getText());
            String name = inputName.getText();
            double weight = Double.parseDouble(inputWeight.getText());
            double height = Double.parseDouble(inputHeight.getText());
            String ability = inputAbility.getText();
            int strength = Integer.parseInt(inputStrength.getText());
            String color = inputColor.getText();

            postChangeTokimon(id,new Tokimon(id,name,weight,height,ability,strength,color));
        });

        Button clear = new Button("clear");
        HBox buttonBox = new HBox(add,clear);
        // dummy box
        Pane gridDummy = new Pane();
        gridDummy.setPrefHeight(10);
        gridDummy.prefWidthProperty().bind(contentBox.widthProperty().multiply(0.15));
        editGrid.add(gridDummy,3,9);
        editGrid.add(buttonBox,4,9,1,2);

        editGrid.setPadding(new Insets(10));
        setColor(editGrid,Color.valueOf(SUB_BACKGROUND_COLOR));


        // Selection box
        HBox editSelection = new HBox();
        editSelection.setMaxWidth(Double.MAX_VALUE);
        editSelection.setPrefHeight(80);
        editSelection.getChildren().add(new Label("Tokimon to change: "));
        editSelection.setAlignment(Pos.CENTER);

        // add selection comboBox
        comboBox = new ComboBox<>();
        comboBox.setItems(getTokimons());
        editSelection.getChildren().add(comboBox);

        // add button
        Button select = new Button("Select");
        select.addEventHandler(MouseEvent.MOUSE_CLICKED,v->{

            Tokimon tokimon = comboBox.getValue();

            inputID.setText(String.valueOf(tokimon.getId()));
            inputID.setDisable(false);
            inputName.setText(tokimon.getName());
            inputName.setDisable(false);
            inputWeight.setText(String.valueOf(tokimon.getWeight()));
            inputWeight.setDisable(false);
            inputHeight.setText(String.valueOf(tokimon.getHeight()));
            inputHeight.setDisable(false);
            inputAbility.setText(tokimon.getAbility());
            inputAbility.setDisable(false);
            inputStrength.setText(String.valueOf(tokimon.getStrength()));
            inputStrength.setDisable(false);
            inputColor.setText(tokimon.getColor());
            inputColor.setDisable(false);

            System.out.println("selected a tokimon");
        });

        editSelection.getChildren().add(select);

        pane.getChildren().add(editSelection);
        pane.getChildren().add(editGrid);
        return pane;
    }
    private GridPane createAddTokimonPane() {
        GridPane pane = new GridPane();

        pane.setHgap(5);
        pane.setVgap(5);

        // create Input Labels
        pane.add(new Label("id:"),0,1);
        pane.add(new Label("name:"),0,2);
        pane.add(new Label("weight:"),0,3);
        pane.add(new Label("height:"),0,4);
        pane.add(new Label("ability:"),0,5);
        pane.add(new Label("strength:"),0,6);
        pane.add(new Label("color:"),0,7);
        // create Input fields
        TextField inputID = new TextField();
        pane.add(inputID,2,1);
        TextField inputName = new TextField();
        pane.add(inputName,2,2);
        TextField inputWeight = new TextField();
        pane.add(inputWeight,2,3);
        TextField inputHeight = new TextField();
        pane.add(inputHeight,2,4);
        TextField inputAbility = new TextField();
        pane.add(inputAbility,2,5);
        TextField inputStrength = new TextField();
        pane.add(inputStrength,2,6);
        TextField inputColor = new TextField();
        pane.add(inputColor,2,7);

        // default values
        inputID.setText("1");
        inputName.setText("TokiName");
        inputWeight.setText("10");
        inputHeight.setText("12");
        inputAbility.setText("water");
        inputStrength.setText("4");
        inputColor.setText("red");


        // add button
        Button add = new Button("add");
        add.addEventHandler(MouseEvent.MOUSE_CLICKED, v->{

            long id = Long.parseLong(inputID.getText());
            String name = inputName.getText();
            double weight = Double.parseDouble(inputWeight.getText());
            double height = Double.parseDouble(inputHeight.getText());
            String ability = inputAbility.getText();
            int strength = Integer.parseInt(inputStrength.getText());
            String color = inputColor.getText();

            postAddTokimon(new Tokimon(id,name,weight,height,ability,strength,color));
        });
        Button clear = new Button("clear");
        HBox buttonBox = new HBox(add,clear);
        // dummy box
        Pane gridDummy = new Pane();
        gridDummy.setPrefHeight(10);
        gridDummy.prefWidthProperty().bind(contentBox.widthProperty().multiply(0.15));
        pane.add(gridDummy,3,9);
        pane.add(buttonBox,4,9,1,2);

        pane.setPadding(new Insets(50));

        return pane;
    }
    private VBox createDeleteTokimonPane() {
        VBox pane = new VBox();
        HBox deleteSelection = new HBox();
        deleteSelection.setMaxWidth(Double.MAX_VALUE);
        deleteSelection.setPrefHeight(100);
        deleteSelection.getChildren().add(new Label("Tokimon to delete: "));
        deleteSelection.setAlignment(Pos.CENTER);
        // add textfield
        TextField input = new TextField();
        input.setPromptText("Tokimon id (eg. \"1\" )");
        deleteSelection.getChildren().add(input);

        // add button
        Button delete = new Button("Delete");
        delete.addEventHandler(MouseEvent.MOUSE_CLICKED,v->{
            Long id = Long.parseLong(input.getText());
            deleteTokimon(id);
            pane.getChildren().remove(1);
            pane.getChildren().add(getTokimonTable());
            System.out.println("refresh");
        });
        deleteSelection.getChildren().add(delete);

        pane.getChildren().add(deleteSelection);
        return pane;
    }
    private Pane createViewTokimonPane(){
        scrollPane = new ScrollPane();

        scrollPane.setContent(loadTokimonShapes());
        scrollPane.setPannable(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        Pane wrapper = new Pane();
        scrollPane.prefHeightProperty().bind(wrapper.heightProperty());
        scrollPane.prefWidthProperty().bind(wrapper.widthProperty());
        wrapper.getChildren().add(scrollPane);
        return wrapper;
    }

    private VBox loadTokimonShapes(){
        ObservableList<Tokimon> tokimons = getTokimons();
        VBox content = new VBox();
        int position = 0;
        for(Tokimon tokimon: tokimons){
            VBox item = TokimonDisplayAdapter.shortFromTokimon(tokimon);
            int finalPosition = position;

            item.getChildren().get(0).addEventHandler(MouseEvent.MOUSE_CLICKED, v->{
                showIndividualTokimon(tokimons, finalPosition);
            });
            content.getChildren().add(item);
            position++;
        }
        return content;
    }

    private void showIndividualTokimon(ObservableList<Tokimon> tokimons,int pos){
        Tokimon thisTokimon = tokimons.get(pos);
        AnchorPane singleInfo = TokimonDisplayAdapter.longFromTokimon(thisTokimon);
        singleInfo.prefWidthProperty().bind(contentDisplay.widthProperty());
        singleInfo.prefHeightProperty().bind(contentDisplay.heightProperty());

        Button back = new Button("return");
        back.addEventHandler(MouseEvent.MOUSE_CLICKED,v->{
            changePanel(3);
        });
        singleInfo.getChildren().add(back);
        AnchorPane.setBottomAnchor(back,5.0);
        AnchorPane.setRightAnchor(back,10.0);

        contentDisplay.getChildren().clear();
        contentDisplay.getChildren().add(singleInfo);
    }

    // main UI
    private void changePanel(int id){
        if(id >= scenes.size() || id < 0){
            id = 0;
        }

        Pane newScene = scenes.get(id);
        String title = sceneTitles.get(id);

        contentTitle.setText(title);
        contentDisplay.getChildren().clear();
        contentDisplay.getChildren().add(newScene);
    }
    private void setUpContentBox(Stage primaryStage) {

        // title text
        contentTitle = new Text("Welcome to TokiTool client");
        contentTitle.setFont(Font.font("Aerial", FontWeight.BOLD, FontPosture.ITALIC,24));

        StackPane contentTitlePane = new StackPane(contentTitle);
        contentTitlePane.setAlignment(contentTitle,Pos.CENTER);
        contentBox.getChildren().add(contentTitlePane);
        setColor(contentTitlePane,Color.valueOf(MAIN_BACKGROUND_COLOR));
        contentTitlePane.setPrefHeight( 40);
        // Position pane
        AnchorPane.setTopAnchor(contentTitlePane,40.0);
        AnchorPane.setLeftAnchor(contentTitlePane,40.0);
        AnchorPane.setRightAnchor(contentTitlePane,40.0);

        setColor(contentDisplay,Color.valueOf(MAIN_BACKGROUND_COLOR));
        contentBox.getChildren().add(contentDisplay);
        AnchorPane.setTopAnchor(contentDisplay,100.0);
        AnchorPane.setLeftAnchor(contentDisplay,10.0);
        AnchorPane.setRightAnchor(contentDisplay,10.0);
        AnchorPane.setBottomAnchor(contentDisplay,10.0);

        setColor(contentBox,Color.valueOf(MAIN_BACKGROUND_COLOR));
        contentBox.prefWidthProperty().bind(primaryStage.widthProperty().multiply(1 - PANEL_PERCENT));
        contentBox.setMaxHeight(Double.MAX_VALUE);

    }
    private void setUpSelectionPanel(Window stage){
        // Buttons
        Button addTokimonPage = new Button("Add");
        addTokimonPage.setStyle(SIDE_BUTTON_STYLE);
        addTokimonPage.setMaxWidth(Double.MAX_VALUE);
        addTokimonPage.addEventHandler(MouseEvent.MOUSE_CLICKED,MouseEvent ->{
            changePanel(1);
        });
        selectionPanel.getChildren().add(addTokimonPage);

        Button changeTokimonPage = new Button("Change");
        changeTokimonPage.setStyle(SIDE_BUTTON_STYLE);
        changeTokimonPage.setMaxWidth(Double.MAX_VALUE);
        changeTokimonPage.addEventHandler(MouseEvent.MOUSE_CLICKED,MouseEvent ->{
            changePanel(2);
            if(comboBox != null){
                comboBox.setItems(getTokimons());
            }
        });
        selectionPanel.getChildren().add(changeTokimonPage);

        Button viewTokimonPage = new Button("View");
        viewTokimonPage.setStyle(SIDE_BUTTON_STYLE);
        viewTokimonPage.setMaxWidth(Double.MAX_VALUE);
        viewTokimonPage.addEventHandler(MouseEvent.MOUSE_CLICKED,MouseEvent ->{
            changePanel(3);
            if(scrollPane != null){
                scrollPane.setContent(loadTokimonShapes());
            }
        });
        selectionPanel.getChildren().add(viewTokimonPage);

        Button deleteTokimonPage = new Button("Delete");
        deleteTokimonPage.setStyle(SIDE_BUTTON_STYLE);
        deleteTokimonPage.setMaxWidth(Double.MAX_VALUE);
        deleteTokimonPage.addEventHandler(MouseEvent.MOUSE_CLICKED,MouseEvent ->{
            changePanel(4);
            if(scenes.get(4).getChildren().size() > 1){
                scenes.get(4).getChildren().remove(1);
            }
            scenes.get(4).getChildren().add(getTokimonTable());
        });
        selectionPanel.getChildren().add(deleteTokimonPage);


        selectionPanel.setSpacing(10);
        selectionPanel.setAlignment(Pos.CENTER);
        // size to window referenced, https://stackoverflow.com/questions/9408244/how-to-set-vbox-size-to-window-size-in-javafx
        selectionPanel.prefHeightProperty().bind(stage.heightProperty());
        selectionPanel.prefWidthProperty().bind(stage.widthProperty().multiply(PANEL_PERCENT));


        selectionPanel.setAlignment(Pos.CENTER);
        Pane dummy = new Pane();
        dummy.prefHeightProperty().bind(selectionPanel.heightProperty().multiply(0.3));
        selectionPanel.getChildren().add(dummy);
        setColor(selectionPanel,Color.valueOf(SIDE_BAR_COLOR));
    }
    private void setColor(Pane pane,Color color){
        pane.setBackground(new Background(new BackgroundFill(color,CornerRadii.EMPTY, Insets.EMPTY)));
    }
    private TableView<Tokimon> getTokimonTable(){

        // Setup columns
        TableColumn<Tokimon,Long> idColumn = new TableColumn<>("id");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Tokimon,Long> nameColumn = new TableColumn<>("name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Tokimon,Long> weightColumn = new TableColumn<>("weight");
        weightColumn.setCellValueFactory(new PropertyValueFactory<>("weight"));

        TableColumn<Tokimon,Long> heightColumn = new TableColumn<>("height");
        heightColumn.setCellValueFactory(new PropertyValueFactory<>("height"));

        TableColumn<Tokimon,Long> abilityColumn = new TableColumn<>("ability");
        abilityColumn.setCellValueFactory(new PropertyValueFactory<>("ability"));

        TableColumn<Tokimon,Long> strengthColumn = new TableColumn<>("strength");
        strengthColumn.setCellValueFactory(new PropertyValueFactory<>("strength"));

        TableColumn<Tokimon,Long> colorColumn = new TableColumn<>("color");
        colorColumn.setCellValueFactory(new PropertyValueFactory<>("color"));

        TableView<Tokimon> tableView = new TableView<>();
        tableView.setItems(getTokimons());
        tableView.getColumns().addAll(idColumn,nameColumn,weightColumn,heightColumn,abilityColumn,strengthColumn,colorColumn);

        return tableView;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
