package com.example.deadlockdetection.ProcessNode;

import com.example.deadlockdetection.Config.BusMsg;
import com.example.deadlockdetection.Config.MyEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import net.sf.json.JSONObject;

import java.util.Objects;

public class AddProcessDialogController {
    EventBus eventBus;
    Stage stage;
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    public void setEventBus(EventBus eventBus) {
        this.eventBus = eventBus;
        eventBus.register(this);
    }


    @FXML
    private Button addProcessBtnN;

    @FXML
    private Button addProcessBtnY;

    @FXML
    private TextField processNameTextFiled;

    @FXML
    void addProcessBtnNOnAction(ActionEvent event) {
        stage.close();
    }

    @FXML
    void addProcessBtnYOnAction(ActionEvent event) {
        String processName= processNameTextFiled.getText();
        if(processName.equals("")){
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setTitle("错误");
            alert.setHeaderText("输入错误");
            alert.setContentText("输入不能为空");
            alert.showAndWait();
        }
        else{
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("processName",processName);
            eventBus.post(new MyEvent(BusMsg.ADD_PROCESS,jsonObject));
            stage.close();
        }
    }
}
