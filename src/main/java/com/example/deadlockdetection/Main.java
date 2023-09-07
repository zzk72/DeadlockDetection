package com.example.deadlockdetection;

import atlantafx.base.theme.PrimerLight;

import com.example.deadlockdetection.edge.Arrow;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
public class Main extends Application {
    private double mouseX;//相对于root的坐标
    private double mouseY;//相对于root的坐标
    private Scene scene;
    private BorderPane root;

    private Bounds rootBounds;

    @Override
    public void start(Stage primaryStage) throws IOException {
//        // 创建一个箭头形状
//        Group root = new Group();
//        scene = new Scene(root, 400, 400);
//
//        // 创建箭头的主体部分（三角形）
//        Polygon arrowHead = new Polygon(
//                20, 0,
//                0, 10,
//                20, 20
//        );
//        arrowHead.setFill(Color.RED);
//
//        // 创建箭头的尾部部分（矩形）
//        Rectangle arrowTail = new Rectangle(20, 5, 150, 10);
//        arrowTail.setFill(Color.RED);
//
//        // 将箭头的主体部分和尾部部分组合成一个箭头形状
//        Group arrow = new Group(arrowHead, arrowTail);
//        arrow.setLayoutX(100); // 设置箭头在场景中的位置
//        arrow.setLayoutY(100);
//        arrow.setOnMouseClicked((MouseEvent event) -> {
//            // 在点击时改变颜色
//            if (arrowHead.getFill() == Color.RED) {
//                arrowHead.setFill(Color.BLUE);
//                arrowTail.setFill(Color.BLUE);
//            } else {
//                arrowHead.setFill(Color.RED);
//                arrowTail.setFill(Color.RED);
//            }
//        });
//        root.getChildren().add(arrow);
//
//

        //Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());

        setObject();
        primaryStage.setTitle("DDA");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void setSceneEvent(Scene scene){//设置场景事件
        scene.setOnMouseDragged(event -> {
            mouseX=event.getX();
            mouseY=event.getY();
        });
        scene.setOnMouseMoved(event -> {
            mouseX=event.getX();
            mouseY=event.getY();
        });
        scene.setOnMouseClicked(event -> {
            mouseX=event.getX();
            mouseY=event.getY();
        });
    }


    private void setObject() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("homeView.fxml"));
        //设置根节点
         root = fxmlLoader.load();
         rootBounds=root.getBoundsInParent();
        //设置子组件的controller
        HomeController controller = fxmlLoader.getController();
        controller.setRoot(root);

        scene = new Scene(root);
        setSceneEvent(scene);
    }



    public static void main(String[] args) {
        launch();
    }
}