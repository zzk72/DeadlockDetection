package com.example.deadlockdetection.ProcessNode;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class ProcessNodeShape extends Group{
    double centerX;
    double centerY;
    double transX=0;
    double transY=0;
    private double scale=1.1;
    String processName;
    Color commonColor=Color.rgb(140, 80, 210, 0.5);
    Color mouseEnteredColor=Color.rgb(90, 20, 255, 0.8);
//    List<String> resourceList;//资源列表，向这些资源申请资源
    public ProcessNodeShape(double centerX, double centerY, String processName) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.processName = processName;

        createProcessNode();

        //鼠标拖拽时移动图形
        this.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                transLocation(event.getSceneX(), event.getSceneY());//传入鼠标在scene中的坐标
            }
        });
        //鼠标停止拖拽时，更新图形的中心点坐标
//        this.setOnMouseReleased(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                updateTransXY(event.getSceneX(), event.getSceneY());
//            }
//        });
        // 鼠标经过时更改图形的颜色并显示进程名
        this.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                changeColor();
                changeSize(scale);
            }
        });
        this.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                changeColor();
                changeSize(1.0);
            }
        });
    }

    private void transLocation(double sceneX, double sceneY) {
        // 计算图形的新位置
        transX = sceneX - centerX;
        transY = sceneY - centerY;

        // 更新图形的位置,setTranslateX和setTranslateY是相对于原来的位置进行移动,仍然保留原来的位置信息，而setLayoutX和setLayoutY则是直接设置图形的位置
        this.setTranslateX(transX);
        this.setTranslateY(transY);
        System.out.println("transX:"+transX+" transY:"+transY);
    }
//    private void updateTransXY(double sceneX, double sceneY) {
//        // 更新图形的中心点坐标
//        centerX = sceneX;
//        centerY = sceneY;
//        this.setLayoutX(centerX);
//        this.setLayoutY(centerY);
//    }
    private void createProcessNode() {
        // 创建进程的主体部分（圆形）
        Circle processBody = new Circle(centerX, centerY, 20);
        processBody.setFill(commonColor);
        processBody.setStroke(commonColor);

        // 在进程主体上方显示进程名
        Text processNameText = new Text(centerX - 20, centerY - 25, processName);

        this.getChildren().addAll(processBody, processNameText);
    }

    private void changeSize(double s) {//按比例缩放
        this.setScaleX(s);
        this.setScaleY(s);
    }
    private void changeColor() {
        // 更改图形的颜色
        Circle processBody = (Circle) this.getChildren().get(0);
        if(processBody.getFill()==commonColor) {
            processBody.setFill(mouseEnteredColor);
        }else {
            processBody.setFill(commonColor);
        }
    }

}
