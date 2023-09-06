package com.example.deadlockdetection;

import atlantafx.base.theme.PrimerLight;
import atlantafx.base.theme.NordDark;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
public class DDMain extends Application {
    private double mouseX;//相对于root的坐标
    private double mouseY;//相对于root的坐标
    private Scene scene;
    private BorderPane root;
    private Canvas canvas;
    private GraphicsContext gc;
    private Bounds rootBounds;
    private Bounds canvasBounds;

    @Override
    public void start(Stage primaryStage) throws IOException {
        // 创建一个箭头形状
        Group root = new Group();
        scene = new Scene(root, 400, 400);

        // 创建箭头的主体部分（三角形）
        Polygon arrowHead = new Polygon(
                20, 0,
                0, 10,
                20, 20
        );
        arrowHead.setFill(Color.RED);

        // 创建箭头的尾部部分（矩形）
        Rectangle arrowTail = new Rectangle(20, 5, 150, 10);
        arrowTail.setFill(Color.RED);

        // 将箭头的主体部分和尾部部分组合成一个箭头形状
        Group arrow = new Group(arrowHead, arrowTail);
        arrow.setLayoutX(100); // 设置箭头在场景中的位置
        arrow.setLayoutY(100);
        arrow.setOnMouseClicked((MouseEvent event) -> {
            // 在点击时改变颜色
            if (arrowHead.getFill() == Color.RED) {
                arrowHead.setFill(Color.BLUE);
                arrowTail.setFill(Color.BLUE);
            } else {
                arrowHead.setFill(Color.RED);
                arrowTail.setFill(Color.RED);
            }
        });
        root.getChildren().add(arrow);


        Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
        //setObject();
        //update(gc);
        primaryStage.setTitle("DDA");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void setSceneEvent(Scene scene){//设置场景事件
        scene.setOnMouseDragged(event -> {
            mouseX=event.getX();
            mouseY=event.getY();
        });
    }
    private void paint(GraphicsContext gc) {//param: GraphicsContext is 画笔
        gc.setFill(javafx.scene.paint.Color.RED);//设置填充颜色
        gc.fillRect(mouseX-canvasBounds.getMinX(),mouseY-canvasBounds.getMinY(),50,50);//填充
    }
    private void update(GraphicsContext gc){//更新画布
        AnimationTimer timer=new AnimationTimer() {
            @Override
            public void handle(long now) {//重写handle方法，屏幕每刷新一次就会调用一次该方法
                gc.clearRect(0,0,canvas.getWidth(),canvas.getHeight());
                paint(gc);
            }
        };
        timer.start();
    }

    private void setObject() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(DDMain.class.getResource("homeView.fxml"));
        //设置根节点
         root = fxmlLoader.load();
         rootBounds=root.getBoundsInParent();
        //设置子组件的controller
        DDController controller = fxmlLoader.getController();
        controller.setRoot(root);
        //获取画布
        canvas=(Canvas)root.lookup("#canvas");
        //绑定画布的宽高
        canvas.widthProperty().bind(root.widthProperty());
        canvas.heightProperty().bind(root.heightProperty());
        canvasBounds=canvas.getBoundsInParent();
        //设置子组件的canvas
        controller.setCanvas(canvas);
        gc=canvas.getGraphicsContext2D();
        scene = new Scene(root);

        setSceneEvent(scene);
    }



    public static void main(String[] args) {
        launch();
    }
}