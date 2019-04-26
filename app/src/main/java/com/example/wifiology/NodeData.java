package com.example.wifiology;

public class NodeData implements Comparable<NodeData>{

    private String name;
    private int id;
    private String location;
    private String description;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public NodeData(String n, int i, String l, String d){name = n; id = i; location = l; description = d;}

    public String getName(){return name;}

    public void setName(String n){name = n;}

    @Override
    public int compareTo(NodeData data) {
        return name.compareTo(data.getName());

    }
}
