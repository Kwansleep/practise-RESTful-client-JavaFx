package ca.cmpt213.asn5.tokimon_client.ui;

import ca.cmpt213.asn5.tokimon_client.model.Tokimon;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;


public class TokimonDisplayAdapter {

    static private double SCALE_LONG = 3;
    static private double SCALE_SHORT = 1;

    static public VBox shortFromTokimon(Tokimon tokimon){
        VBox vBox = new VBox();

        Ellipse tokimonShape = new Ellipse(tokimon.getWeight()*SCALE_SHORT,tokimon.getHeight() * SCALE_SHORT);

        Color c = Color.web(tokimon.getColor());
        tokimonShape.setStroke(c);
        tokimonShape.setFill(c);

        vBox.getChildren().add(tokimonShape);
        vBox.getChildren().add(new Label(tokimon.getName()));

        return vBox;
    }

    static public AnchorPane longFromTokimon(Tokimon tokimon){
        AnchorPane anchorPane = new AnchorPane();

        Ellipse tokimonShape = new Ellipse(tokimon.getWeight() * SCALE_LONG,tokimon.getHeight() * SCALE_LONG);

        Color c = Color.web(tokimon.getColor());
        tokimonShape.setStroke(Color.BLACK);
        tokimonShape.setFill(c);

        StackPane shapeWrapper = new StackPane(tokimonShape);
        shapeWrapper.setAlignment(Pos.BOTTOM_CENTER);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(shapeWrapper);
        scrollPane.setPrefHeight(200);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        shapeWrapper.prefWidthProperty().bind(scrollPane.widthProperty().multiply(0.9));
        shapeWrapper.prefHeightProperty().bind(scrollPane.heightProperty().multiply(0.9));

        anchorPane.getChildren().add(scrollPane);
        AnchorPane.setTopAnchor(scrollPane,0.0);
        AnchorPane.setLeftAnchor(scrollPane,0.0);
        AnchorPane.setRightAnchor(scrollPane,0.0);

        GridPane infoGrid = new GridPane();
        infoGrid.setVgap(3);
        infoGrid.setHgap(20);
        // labels
        infoGrid.add(new Label("id:"),0,1);
        infoGrid.add(new Label("name:"),0,2);
        infoGrid.add(new Label("weight:"),0,3);
        infoGrid.add(new Label("height:"),0,4);
        infoGrid.add(new Label("ability:"),0,5);
        infoGrid.add(new Label("strength:"),0,6);
        infoGrid.add(new Label("color:"),0,7);
        infoGrid.add(new Label("Link:"),0,8);
        // tokimon info
        infoGrid.add(new Label(""+tokimon.getId()),1,1);
        infoGrid.add(new Label(tokimon.getName()),1,2);
        infoGrid.add(new Label(""+tokimon.getWeight()),1,3);
        infoGrid.add(new Label(""+tokimon.getHeight()),1,4);
        infoGrid.add(new Label(tokimon.getAbility()),1,5);
        infoGrid.add(new Label(""+tokimon.getStrength()),1,6);
        infoGrid.add(new Label(tokimon.getColor()),1,7);
        Hyperlink link = new Hyperlink("link");
        link.setOnAction(v->{
            try{
                // how to launch hyperlink referenced, https://stackoverflow.com/questions/10967451/open-a-link-in-browser-with-java-button
                Desktop.getDesktop().browse(new URL(Main.SERVER_ADDRESS + tokimon.getId()).toURI());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        });
        infoGrid.add(link,1,8);

        infoGrid.setStyle("-fx-border-color: black;-fx-background-color: #eaeaea");
        infoGrid.setPadding(new Insets(0,0,0,20));
        infoGrid.setMaxWidth(200.0);

        StackPane infoWrapper = new StackPane(infoGrid);
        infoWrapper.setAlignment(Pos.BOTTOM_CENTER);

        anchorPane.getChildren().add(infoWrapper);
        AnchorPane.setBottomAnchor(infoWrapper,0.0);
        AnchorPane.setLeftAnchor(infoWrapper,0.0);
        AnchorPane.setRightAnchor(infoWrapper,0.0);

        return anchorPane;
    }
}
