package com.example.deadlockdetection.ResourceNode;

import com.example.deadlockdetection.Config.BusMsg;
import com.example.deadlockdetection.Config.MyEvent;
import com.google.common.eventbus.EventBus;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import net.sf.json.JSONObject;

public class AddResourceDialogController {
    EventBus eventBus;
    Stage stage;
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    public void setEventBus(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @FXML
    private Button addResourceBtnN;

    @FXML
    private Button addResourceBtnY;

    @FXML
    private TextField resourceNameTextFiled;

    @FXML
    private TextField resourceNumerTextFiled;


    @FXML
    void addResourceBtnNOnAction(ActionEvent event) {
        stage.close();
    }

    @FXML
    void addResourceBtnYOnAction(ActionEvent event) {
        String resName=resourceNameTextFiled.getText();
        String resNum=resourceNumerTextFiled.getText();
        if(resName.equals("")||resNum.equals("")){
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setTitle("错误");
            alert.setHeaderText("输入错误");
            alert.setContentText("输入不能为空");
            alert.showAndWait();
        }else{
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("resName",resName);
            jsonObject.put("resNum",resNum);
            eventBus.post(new MyEvent(BusMsg.ADD_RESOURCE,jsonObject));
            stage.close();
        }
    }



}
