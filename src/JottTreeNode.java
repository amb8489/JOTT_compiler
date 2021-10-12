import java.util.ArrayList;

public class JottTreeNode {
    private JottElement jottElement;
    private Token token;

    private ArrayList<JottTreeNode> jottChildren = new ArrayList<JottTreeNode>();
    private boolean isTerminal;

    public JottTreeNode(JottElement jottElement) {
        this.jottElement = jottElement;
        jottChildren = new ArrayList<>();
        isTerminal = false;
    }

    public JottTreeNode(Token token) {
        this.token = token;
        jottChildren = new ArrayList<>();
        isTerminal = true;
    }

    public JottElement getJottElement() {
        return jottElement;
    }

    public Token getToken() {
        return token;
    }

    public Boolean isTerminal() {
        return isTerminal;
    }

    public void setTerminal(boolean isTerminal) {
        this.isTerminal = isTerminal;
    }

    public void addChild(JottTreeNode childNode) {
        jottChildren.add(childNode);
    }

    public ArrayList<JottTreeNode> getChildren() {
        return jottChildren;
    }

}
