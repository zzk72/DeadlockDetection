package com.example.deadlockdetection.edge;

import com.example.deadlockdetection.ProcessNode.ProcessNodeShape;
import com.example.deadlockdetection.ResourceNode.ResourceNodeShape;
import javafx.application.Platform;
import lombok.Data;

@Data
public class Edge {
    private String startNodeName;
    private String endNodeName;
    private EdgeArrowShape edgeArrowShape=null;
    private ResourceNodeShape resourceNodeShape=null;
    private ProcessNodeShape processNodeShape=null;
    private boolean isApplyEdge;
    private boolean isShow=true;
    private int timeStep;
    public Edge(String startNodeName, String endNodeName, EdgeArrowShape edgeArrowShape, ResourceNodeShape resourceNodeShape, ProcessNodeShape processNodeShape) {
        this.startNodeName = startNodeName;
        this.endNodeName = endNodeName;
        this.edgeArrowShape = edgeArrowShape;
        this.resourceNodeShape = resourceNodeShape;
        this.processNodeShape = processNodeShape;
        this.isApplyEdge = edgeArrowShape.isApplyEdge();
    }
    public void setVisibility(boolean visibility) throws InterruptedException {
        isShow=visibility;
        edgeArrowShape.setVisible(visibility);
        if(!visibility){
            Platform.runLater(() -> {
                try {
                    edgeArrowShape.disappear();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        System.out.println("edge:"+startNodeName+"->"+endNodeName+" visibility:"+visibility);
    }


}
