package ast;

import java.util.List;

public class ClassDecl extends Statement {
    private String name;
    private String baseClass;
    private List<Member> members;

    public ClassDecl(String name, String baseClass, List<Member> members,
                     int line, int col) {
        super(line, col);
        this.name = name;
        this.baseClass = baseClass;
        this.members = members;
    }

    public String getName() { return name; }
    public String getBaseClass() { return baseClass; }
    public List<Member> getMembers() { return members; }
}
