package com.example.deadlockdetection;

import com.google.common.eventbus.EventBus;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import net.sf.json.JSONObject;

public class AddResourceDialogController {
    EventBus eventBus;

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

    }

    @FXML
    void addResourceBtnYOnAction(ActionEvent event) {
        String resourceName=resourceNameTextFiled.getText();
        String resourceNumber=resourceNumerTextFiled.getText();
        if(resourceName.equals("")||resourceNumber.equals("")){
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setTitle("错误");
            alert.setHeaderText("输入错误");
            alert.setContentText("输入不能为空");
            alert.showAndWait();
        }else{
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("resourceName",resourceName);
            jsonObject.put("resourceNumber",resourceNumber);
            eventBus.post(new MyEvent("addResourceBtnYOnAction",jsonObject));
        }
    }

    public void setEventBus(EventBus eventBus) {
        this.eventBus = eventBus;
    }
}
