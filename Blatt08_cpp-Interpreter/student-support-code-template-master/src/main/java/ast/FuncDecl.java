package ast;

import java.util.ArrayList;

public record FuncDecl(String name, ArrayList<Param> params, ArrayList<stmt> body) implements stmt {
}
