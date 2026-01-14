package ast;

import java.util.ArrayList;

public record Program(ArrayList<stmt> statements) implements ASTNode {
}
