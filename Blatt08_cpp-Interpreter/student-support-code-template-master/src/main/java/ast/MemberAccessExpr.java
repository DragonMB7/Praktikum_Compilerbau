package ast;


public class MemberAccessExpr extends Expression
{
    private String objectName;
    private String memberName;

    public MemberAccessExpr(String objectName, String memberName,
                            int line, int col) {
        super(line, col);
        this.objectName = objectName;
        this.memberName = memberName;
    }

    public String getObjectName() { return objectName; }
    public String getMemberName() { return memberName; }
}
