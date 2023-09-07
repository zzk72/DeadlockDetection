package com.example.deadlockdetection.edge;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;

public class EdgeArrowShape extends Group {
    private Line arrowLine;
    private Polygon arrowHead;
    private double startX;
    private double startY;
    private double endX;
    private double endY;


    public EdgeArrowShape(double startX, double startY, double endX, double endY) {
        this.startX = startX;
        this.startY = startY;
        this.endX=endX;
        this.endY=endY;
        createArrow();
    }

    private void createArrow() {
        // 创建箭头的线段
        arrowLine = new Line(startX, startY, endX, endY);
        arrowLine.setStroke(Color.BLACK);

        // 计算箭头的角度
        double angle = Math.atan2(endY - startY, endX - startX);

        // 创建箭头的箭头部分
        double arrowLength = 15; // 箭头的长度
        arrowHead = new Polygon();
        arrowHead.getPoints().addAll(
                endX - arrowLength * Math.cos(angle - Math.PI / 6), endY - arrowLength * Math.sin(angle - Math.PI / 6),
                endX, endY,
                endX - arrowLength * Math.cos(angle + Math.PI / 6), endY - arrowLength * Math.sin(angle + Math.PI / 6)
        );
        arrowHead.setFill(Color.BLACK);

        // 添加箭头的线段和箭头部分到当前Group
        getChildren().addAll(arrowLine, arrowHead);
    }


}
