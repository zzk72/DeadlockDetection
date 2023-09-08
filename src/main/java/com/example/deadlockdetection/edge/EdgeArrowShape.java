package com.example.deadlockdetection.edge;

import com.example.deadlockdetection.Config.Point;
import javafx.scene.Group;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import lombok.Data;

@Data
public class EdgeArrowShape extends Group {
    private Line arrowLine;
    private Polygon arrowHead;
    private double startX;
    private double startY;
    private double endX;
    private double endY;
    String edgeType;
    String startNodeName;
    String endNodeName;
    BorderPane root;


    public EdgeArrowShape(Point startPoint, Point endPoint, String edgeType, String startNodeName, String endNodeName, BorderPane root) {
        this.startX = startPoint.getX();
        this.startY = startPoint.getY();
        this.endX = endPoint.getX();
        this.endY = endPoint.getY();
        this.edgeType = edgeType;
        this.startNodeName = startNodeName;
        this.endNodeName = endNodeName;
        this.root = root;
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
        root.getChildren().addAll(arrowLine, arrowHead);
    }


}
