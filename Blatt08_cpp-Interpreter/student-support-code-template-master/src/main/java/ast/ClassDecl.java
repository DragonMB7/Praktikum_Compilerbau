package ast;

import java.util.ArrayList;

public record ClassDecl(String name, ArrayList<VarDecl> fields, ArrayList<FuncDecl> methods) implements stmt {
}
