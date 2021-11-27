package com.example.taskhandler;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Builder;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class GuiBuilder implements Builder<Region> {
    TaskHandler taskHandlerTemps = new TaskHandler();
    Stage stageAbout = new Stage();
    Stage stageTask = new Stage();
    ListView listViewCenterLeft = addList();
    ListView listViewCenter = addList();
    ListView listViewCenterRight = addList();
    String savePath = "C:\\Users\\lesli\\Desktop\\data.txt";
    @Override
    public Region build(){
        VBox root = new VBox();
        root.getChildren().add(mainMenuBar());
        root.getChildren().add(top());
        root.getChildren().add(center());
        root.getChildren().add(bottom());
        root.setPadding(new Insets(0,0,10,0));
        return root;
    }

    private Node top(){
        HBox topLayout = new HBox();
        return topLayout;
    }

    private Node center(){
        HBox centerLayout = new HBox();
        getListViewCenterLeft().setContextMenu(contextMenu(getListViewCenterLeft(), getListViewCenter()));
        getListViewCenter().setContextMenu(contextMenu(getListViewCenter(), getListViewCenterRight()));
        getListViewCenterRight().setContextMenu(contextMenu(getListViewCenterRight(), null));
        getListViewCenterLeft().addEventHandler(MouseEvent.MOUSE_CLICKED, event -> taskHandlerTemps.setListViewSection("To do"));
        getListViewCenter().addEventHandler(MouseEvent.MOUSE_CLICKED, event -> taskHandlerTemps.setListViewSection("On Progress"));
        getListViewCenterRight().addEventHandler(MouseEvent.MOUSE_CLICKED, event -> taskHandlerTemps.setListViewSection("Completed"));
        centerLayout.getChildren().add(centerChildrenLayout("To do", getListViewCenterLeft()));
        centerLayout.getChildren().add(centerChildrenLayout("On Progress",getListViewCenter()));
        centerLayout.getChildren().add(centerChildrenLayout("Completed", getListViewCenterRight()));
        return centerLayout;
    }

    private ListView getListViewCenterLeft(){
        return listViewCenterLeft;
    }

    private ListView getListViewCenter(){
        return listViewCenter;
    }

    private ListView getListViewCenterRight(){
        return listViewCenterRight;
    }

    private Node centerChildrenLayout(String text, ListView listView){
        VBox centerLayoutChildren = new VBox();
        centerLayoutChildren.getChildren().add(addText(text));
        centerLayoutChildren.getChildren().add(listView);
        centerLayoutChildren.setAlignment(Pos.CENTER);
        //centerLayoutChildren.setSpacing(5);
        centerLayoutChildren.setPadding(new Insets(0,10,0,10));
        return centerLayoutChildren;
    }

    private Node bottom(){
        HBox bottomLayout = new HBox();
        return bottomLayout;
    }

    private ContextMenu contextMenu(ListView FirstListView, ListView SecondListView){
        ContextMenu genericContextMenu = new ContextMenu();
        MenuItem menuItemNew = new MenuItem("New");
        MenuItem menuItemModify = new MenuItem("Modify");
        MenuItem menuItemDelete = new MenuItem("Delete");
        MenuItem menuItemGoForward = new MenuItem("Go forward");
        genericContextMenu.getItems().addAll(menuItemNew, menuItemModify, menuItemDelete, menuItemGoForward);
        menuItemNew.setOnAction(event -> {
            NewWindow("New Task",FirstListView);
        });
        menuItemModify.setOnAction(event -> {
            if(!FirstListView.getItems().isEmpty() && FirstListView.getSelectionModel().getSelectedItem() != null)
            NewWindow("Modify", FirstListView);
        });
        menuItemDelete.setOnAction(event -> {
            FirstListView.getItems().removeAll(FirstListView.getSelectionModel().getSelectedItems());
        });
        menuItemGoForward.setOnAction(event -> {
            if(!FirstListView.getItems().isEmpty() && FirstListView.getSelectionModel().getSelectedItem() != null){
                if(SecondListView != null){
                    SwitchListView(FirstListView, SecondListView);
                }
            }
        });
        return genericContextMenu;
    }

    private void NewWindow(String WindowName, ListView FirstListView){
        if(!stageTask.isShowing()){
            VBox vboxDialog = new VBox();
            Scene dialogScene = new Scene(vboxDialog, 250,200);
            stageTask.setScene(dialogScene);
            stageTask.setResizable(false);
            stageTask.setTitle(WindowName);
            stageTask.show();
            vboxDialog.getChildren().add(NewTaskWindowLayout(stageTask, FirstListView, WindowName));
            vboxDialog.setPadding(new Insets(5,5,5,5));
        }else{
            stageTask.toFront();
        }

    }

    private Node NewTaskWindowLayout(Stage stage, ListView listView, String WindowName){
        AnchorPane anchorPane = new AnchorPane();
        Button SaveBtn = addButton("Save");
        Button CloseBtn = addButton("Close");
        TextArea NewTaskTextArea = addTextArea();
        NewTaskTextArea.setMaxSize(200, 150);
        anchorPane.setTopAnchor(NewTaskTextArea, 5d);
        anchorPane.setLeftAnchor(NewTaskTextArea, 5d);
        anchorPane.setRightAnchor(NewTaskTextArea, 5d);
        anchorPane.setTopAnchor(SaveBtn, 160d);
        anchorPane.setLeftAnchor(SaveBtn, 75d);
        anchorPane.setTopAnchor(CloseBtn, 160d);
        anchorPane.setRightAnchor(CloseBtn, 75d);
        anchorPane.getChildren().addAll(NewTaskTextArea, SaveBtn, CloseBtn);
        if(WindowName == "Modify"){
            NewTaskTextArea.setText(listView.getSelectionModel().getSelectedItem().toString());
        }
        SaveBtn.setOnAction(event -> {
            if(WindowName == "Modify"){
                listView.getItems().remove(listView.getSelectionModel().getSelectedItem());
            }
            if(NewTaskTextArea.getText() == ""){
                stage.close();
            }
            else
            {
                listView.getItems().add(NewTaskTextArea.getText());
                stage.close();
            }
        });
        CloseBtn.setOnAction(event -> {
            stage.close();
        });
        return anchorPane;
    }

    public void SwitchListView(ListView FirstListView, ListView SecondListView){
        Object temp = FirstListView.getSelectionModel().getSelectedItem();
        FirstListView.getItems().remove(temp);
        SecondListView.getItems().add(temp);
    }

    //Elements method
    private ListView<String> addList(){
        ListView<String> generalListView = new ListView<>();
        generalListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        generalListView.setEditable(true);
        return generalListView;
    }

    private Text addText(String text){
        Text genericText = new Text(text);
        return genericText;
    }

    private TextArea addTextArea(){
        TextArea genericTextArea = new TextArea();
        return genericTextArea;
    }

    private Button addButton(String nameBtn){
        Button genericButton = new Button(nameBtn);
        return genericButton;
    }

    private MenuBar mainMenuBar(){
        Menu menuFile = new Menu("File");
        MenuItem menuItemSave = new MenuItem("Save");
        //menuFile.getItems().add(menuItemSave);

        Menu menuHelp = new Menu("Help");
        MenuItem menuItemAbout = new MenuItem("About");
        menuHelp.getItems().add(menuItemAbout);

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(menuFile);
        menuBar.getMenus().add(menuHelp);

        menuItemSave.setOnAction(event -> {
            String tempListViewLeft = getListViewCenterLeft().getItems().toString();
            String tempListViewCenter = getListViewCenter().getItems().toString();
            String tempListViewRight = getListViewCenterRight().getItems().toString();
            System.out.println(tempListViewLeft);
            System.out.println(tempListViewCenter);
            System.out.println(tempListViewRight);

            File myFile = new File(savePath);
            try{
                FileWriter myWriter = new FileWriter(myFile);
                if(myFile.createNewFile()){
                    System.out.println("File created");
                    myWriter.write(tempListViewLeft);
                    myWriter.write(tempListViewCenter);
                    myWriter.write(tempListViewRight);
                    myWriter.close();
                }else{
                    myWriter.write(tempListViewLeft);
                    myWriter.write(tempListViewCenter);
                    myWriter.write(tempListViewRight);
                    myWriter.close();
                }
            }catch(IOException e){
                System.out.println("An Error Occurred!");
                e.printStackTrace();
            }

        });
        menuItemAbout.setOnAction(event -> {
            if(!stageAbout.isShowing()){
                GridPane gridPane = new GridPane();
                Scene dialogScene = new Scene(gridPane, 250,200);
                stageAbout.setScene(dialogScene);
                stageAbout.setResizable(false);
                stageAbout.setTitle("About");
                stageAbout.show();
                gridPane.setAlignment(Pos.CENTER);
                gridPane.setVgap(10);
                gridPane.add(new Text("Developer: Les"),0,0);
                gridPane.add(new Text("Made with JavaFX"),0,1);
            }else{
                stageAbout.toFront();
            }
        });
        return menuBar;
    }
}
