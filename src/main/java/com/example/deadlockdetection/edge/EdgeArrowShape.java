package com.example.deadlockdetection.edge;

import com.example.deadlockdetection.Utilities.MyEvent;
import com.example.deadlockdetection.Utilities.Point;
import javafx.scene.Group;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.QuadCurve;
import lombok.Data;


@Data
public class EdgeArrowShape extends Group {
    private QuadCurve arrowCurve;
    private Polygon arrowHead;
    private double startX;
    private double startY;
    private double endX;
    private double endY;
    boolean isApplyEdge;
//    String startNodeName;
//    String endNodeName;
    BorderPane root;
    double offset;
    Color commonColor;
    Color commonApplyColor =Color.rgb(255,0,0,1);
    Color commonRequestColor=Color.rgb(0,0,255,0.8);
    public EdgeArrowShape(Point startPoint, Point endPoint, boolean isApplyEdge,  BorderPane root) {
        this.startX = startPoint.getX();
        this.startY = startPoint.getY();
        this.endX = endPoint.getX();
        this.endY = endPoint.getY();
        this.isApplyEdge = isApplyEdge;
//        this.startNodeName = startNodeName;
//        this.endNodeName = endNodeName;
        this.root = root;
        if(isApplyEdge) {
            offset = 10;
            commonColor = commonRequestColor;
        }
        else {
            offset = -10;
            commonColor = commonApplyColor;
        }
        createArrow();
    }

    private void createArrow() {
        // 创建箭头的曲线
        arrowCurve = new QuadCurve(startX, startY, (startX + endX) / 2+offset, (startY + endY) / 2+offset, endX, endY);
        arrowCurve.setStroke(commonColor);
        arrowCurve.setStrokeWidth(1);
        arrowCurve.setFill(null); // 设置曲线内部不填充颜色

        // 创建箭头的箭头部分
        double arrowLength = 15; // 箭头的长度
        double angle = Math.atan2(endY - startY, endX - startX);
        arrowHead = new Polygon();
        arrowHead.getPoints().addAll(
                endX - arrowLength * Math.cos(angle - Math.PI / 8), endY - arrowLength * Math.sin(angle - Math.PI / 8),
                endX, endY,
                endX - arrowLength * Math.cos(angle + Math.PI / 8), endY - arrowLength * Math.sin(angle + Math.PI / 8)
        );
        arrowHead.setFill(commonColor);

        // 添加箭头的曲线和箭头部分到当前Group
        root.getChildren().addAll(arrowCurve, arrowHead);
    }
    public void disappear() throws InterruptedException {
        root.getChildren().removeAll(arrowCurve,arrowHead);
    }
    public void setVisibility(MyEvent myEvent) throws InterruptedException {
        System.out.println("edgeArrowShape received a message");
        if(myEvent.getType().equals("edgeVisibility")){
            if(myEvent.getData().toString().equals("true")){
                arrowCurve.setVisible(true);
                arrowHead.setVisible(true);
            }else{
                arrowCurve.setVisible(false);
                arrowHead.setVisible(false);
            }
        }
    }
}
