import java.util.ArrayList;

public class JottTreeNode {
    private JottElement jottElement;
    private ArrayList<JottTreeNode> jottChildren = new ArrayList<JottTreeNode>();


    public JottTreeNode(JottElement jottElement) {
        this.jottElement = jottElement;
        jottChildren = new ArrayList<>();
    }

    public JottElement getJottElement() {
        return jottElement;
    }

    public void addChild(JottTreeNode childNode) {
        jottChildren.add(childNode);
    }

    public ArrayList<JottTreeNode> getChildren() {
        return jottChildren;
    }

}
