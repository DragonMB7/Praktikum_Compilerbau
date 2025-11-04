import java.util.ArrayList;

public class MyGrammarVisitor extends myGrammarBaseVisitor<Object>{

    @Override
    public Object visitNUMExpr(myGrammarParser.NUMExprContext ctx) {
        return ctx.NUMBER().getText();  // Terminal zu String
    }

    @Override
    public Object visitIDExpr(myGrammarParser.IDExprContext ctx) {
        return ctx.ID().getText();   // Terminal zu String
    }

    @Override
    public Object visitSTRExpr(myGrammarParser.STRExprContext ctx) {
        return ctx.STRING().getText();   // Terminal zu String
    }

    @Override
    public Object visitMulExpr(myGrammarParser.MulExprContext ctx) {
        String left = String.valueOf(ctx.expr(0));
        String right = String.valueOf(ctx.expr(1));

        return left + " * " + right;
    }

    @Override
    public Object visitDivExpr(myGrammarParser.DivExprContext ctx) {
        String left = String.valueOf(ctx.expr(0));
        String right = String.valueOf(ctx.expr(1));

        return left + " / " + right;
    }

    @Override
    public Object visitAddExpr(myGrammarParser.AddExprContext ctx) {
        String left = String.valueOf(ctx.expr(0));
        String right = String.valueOf(ctx.expr(1));

        return left + " + " + right;
    }

    @Override
    public Object visitSubExpr(myGrammarParser.SubExprContext ctx) {
        String left = String.valueOf(ctx.expr(0));
        String right = String.valueOf(ctx.expr(1));

        return left + " - " + right;
    }

    @Override
    public Object visitGreExpr(myGrammarParser.GreExprContext ctx) {
        String left = String.valueOf(ctx.expr(0));
        String right = String.valueOf(ctx.expr(1));

        return left + " > " + right;
    }

    @Override
    public Object visitSmaExpr(myGrammarParser.SmaExprContext ctx) {
        String left = String.valueOf(ctx.expr(0));
        String right = String.valueOf(ctx.expr(1));

        return left + " < " + right;
    }

    @Override
    public Object visitEquExpr(myGrammarParser.EquExprContext ctx) {
        String left = String.valueOf(ctx.expr(0));
        String right = String.valueOf(ctx.expr(1));

        return left + " == " + right;
    }

    @Override
    public Object visitNEquExpr(myGrammarParser.NEquExprContext ctx) {
        String left = String.valueOf(ctx.expr(0));
        String right = String.valueOf(ctx.expr(1));

        return left + " != " + right;
    }

    @Override
    public Object visitVardecl(myGrammarParser.VardeclContext ctx) {
        // Typ
        String type = visit(ctx.type()).toString();

        // Variablenname
        String id = ctx.ID().getText();

        // Optionaler Ausdruck
        if (ctx.expr() != null) {
            String expr = visit(ctx.expr()).toString();
            return type + " " + id + " := " + expr;
        } else {
            return type + " " + id;
        }
    }

    @Override
    public Object visitAssign(myGrammarParser.AssignContext ctx) {

        String id = ctx.ID().getText();
        String expr = visit(ctx.expr()).toString();

        return id + " := " + expr;

    }

    @Override
    public Object visitCond(myGrammarParser.CondContext ctx) {
        String condition = visit(ctx.expr()).toString();

        // condblock
        ArrayList<String> ifStatements = new ArrayList<>();
        for (myGrammarParser.StmtContext stmtCtx : ctx.condblock.stmt()) {
            ifStatements.add(visit(stmtCtx).toString());
        }

        // elseblock (optional)
        ArrayList<String> elseStatements = new ArrayList<>();
        if (ctx.elseblock != null) {
            for (myGrammarParser.StmtContext stmtCtx : ctx.elseblock.stmt()) {
                elseStatements.add(visit(stmtCtx).toString());
            }
        }

        return "if " + condition + " do " + ifStatements + " else " + elseStatements + " end";
    }

    @Override
    public Object visitwhile(myGrammarParser.WhileContext ctx) {
        String condition = visit(ctx.expr()).toString();

        // condblock
        ArrayList<String> statements = new ArrayList<>();
        for (myGrammarParser.StmtContext stmtCtx : ctx.stmt()) {
            statements.add(visit(stmtCtx).toString());
        }

        return "while " + condition + " do " + statements + " end";

    }

    @Override
    public Object visitStart(myGrammarParser.StartContext ctx) {
        ArrayList<String> statements = new ArrayList<>();

        for (myGrammarParser.StmtContext stmtCtx : ctx.stmt()) {
            String result = visit(stmtCtx).toString();
            statements.add(result);
        }

        return String.join("\n", statements);
    }


}
