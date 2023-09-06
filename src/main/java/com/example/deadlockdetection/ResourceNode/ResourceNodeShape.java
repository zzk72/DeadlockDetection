package com.example.deadlockdetection.ResourceNode;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.List;

public class ResourceNodeShape extends Group {
    private double centerX;
    private double centerY;
    double transX=0;
    double transY=0;
    private String resName;
    private int resNum;

    Color commonColor=Color.rgb(150,27,10,0.8);
    Color mouseEnteredColor=Color.rgb(255,0,0,1);
    //缩放比例
    double scale=1.1;

//    List<String> processList;//进程列表，向这些进程提供分配资源

    public ResourceNodeShape(double centerX, double centerY, String resName, int resNum) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.resName = resName;
        this.resNum = resNum;

        createResourceNode();

        //鼠标拖拽时移动图形
        this.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                updateLocation(event.getSceneX(), event.getSceneY());
            }
        });

        this.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                // 鼠标经过时更改图形的颜色
                changeColor();
                //鼠标经过时改变图形的大小
                changeSize(scale);
            }
        });
        // 鼠标离开时恢复图形的颜色
        this.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                changeColor();
                //鼠标离开时恢复图形的大小
                changeSize(1.0);
            }
        });
    }

    private void changeSize(double s) {//按比例缩放
        this.setScaleX(s);
        this.setScaleY(s);
    }

    private void updateLocation(double sceneX, double sceneY) {
        // 计算图形的新位置
        transX = sceneX - centerX;
        transY = sceneY - centerY;

        // 更新图形的位置
        this.setTranslateX(transX);
        this.setTranslateY(transY);
        System.out.println(centerX+" "+centerY);
        System.out.println(transX+" "+transY);
    }

    private void createResourceNode() {
        // 计算容器的边长（正方形）
        double containerSize = 30 + Math.ceil(Math.sqrt(resNum)) * 30; // 使用向上取整确保能容纳所有圆点
        Rectangle container = new Rectangle(centerX - containerSize / 2, centerY - containerSize / 2, containerSize, containerSize);
        container.setFill(Color.rgb(31,180,60,0.6));
        container.setStroke(Color.rgb(8,90,20,0.5));

        // 计算圆点排列的行数和列数
        int numRows = (int) Math.ceil(Math.sqrt(resNum));
        int numCols = (int) Math.ceil((double) resNum / numRows);

        // 计算每个圆点之间的间隔
        double circleSpacingX = containerSize / numCols;
        double circleSpacingY = containerSize / numRows;


        // 在容器上方显示资源名
        Text resourceNameText = new Text(centerX - containerSize / 2, centerY - containerSize / 2 - 5, resName);
        this.getChildren().addAll(container, resourceNameText);

        // 在容器内部创建圆点（按方形网格排列）
        double circleRadius = 10;
        double circleX = centerX - containerSize / 2 + circleSpacingX / 2;
        double circleY = centerY - containerSize / 2 + circleSpacingY / 2;
        for (int i = 0; i < resNum; i++) {
            Circle circle = new Circle(circleX, circleY, circleRadius);
            circle.setFill(commonColor);
            this.getChildren().add(circle);

            circleX += circleSpacingX;
            if ((i + 1) % numCols == 0) {
                circleX = centerX - containerSize / 2 + circleSpacingX / 2;
                circleY += circleSpacingY;
            }
        }
    }
    // 自定义方法来更改图形的颜色
    private void changeColor() {
        // 遍历图形内的所有圆点，并更改它们的颜色
        for (int i = 2; i < this.getChildren().size(); i++) {
            Circle circle = (Circle) this.getChildren().get(i);
            if (circle.getFill() == commonColor) {
                circle.setFill(mouseEnteredColor);
            }else{
                circle.setFill(commonColor);
            }
        }
    }
}
