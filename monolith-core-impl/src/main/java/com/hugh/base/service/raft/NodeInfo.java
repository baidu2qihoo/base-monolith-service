package com.hugh.base.service.raft;

public class NodeInfo {
    private String nodeId;
    private String host;
    private int port;
    private String lastHeartbeat;
    public String getNodeId(){return nodeId;} public void setNodeId(String n){this.nodeId=n;}
    public String getHost(){return host;} public void setHost(String h){this.host=h;}
    public int getPort(){return port;} public void setPort(int p){this.port=p;}
    public String getLastHeartbeat(){return lastHeartbeat;} public void setLastHeartbeat(String l){this.lastHeartbeat=l;}
}
