package SymTable;

import java.util.EmptyStackException;
import java.util.Stack;

public class MultiScopeSymbolTable {
  private Stack<SingleScopeSymbolTable> scopesStack;
  private int scopeLevel;

  public MultiScopeSymbolTable() {
    this.scopesStack = new Stack<>();
    this.scopeLevel = -1;
    enterScope("global");
  }

  public SingleScopeSymbolTable enterScope(String baseName) {
    scopeLevel++;
    String scopeActualName = baseName + "_" + scopeLevel;
    SingleScopeSymbolTable newScopeTable = new SingleScopeSymbolTable(scopeActualName);
    scopesStack.push(newScopeTable);
    System.out.println(
        "--> Scope '" + newScopeTable.getName() + "' entered (Level: " + scopeLevel + ").");
    return newScopeTable;
  }

  public SingleScopeSymbolTable exitScope() throws EmptyStackException {
    if (scopesStack.isEmpty()) {
      throw new EmptyStackException();
    }
    SingleScopeSymbolTable exitedScope = scopesStack.pop();
    scopeLevel--;
    System.out.println(
        "<-- Scope '" + exitedScope.getName() + "' exited (Level: " + scopeLevel + ").");
    return exitedScope;
  }

  public void addSymbolToCurrentScope(String name, String type, String kind)
      throws EmptyStackException {
    if (scopesStack.isEmpty()) {
      throw new EmptyStackException();
    }
    Symbol symbol = new Symbol(name, type, kind);
    scopesStack.peek().addSymbol(symbol);
  }

  public Symbol lookupSymbol(String name) {

    for (int i = scopesStack.size() - 1; i >= 0; i--) {
      SingleScopeSymbolTable scopeTable = scopesStack.get(i);
      Symbol symbol = scopeTable.lookupSymbol(name);
      if (symbol != null) {
        System.out.println(
            "  Symbol '" + name + "' found in Scope '" + scopeTable.getName() + "'.");
        return symbol;
      }
    }
    System.out.println(" Symbol '" + name + "' not found in any Scope.");
    return null;
  }

  public SingleScopeSymbolTable getCurrentScope() {
    if (scopesStack.isEmpty()) {
      return null;
    }
    return scopesStack.peek();
  }

  @Override
  public String toString() {
    return "MultiScopeSymbolTable(active_scopes="
        + scopesStack.size()
        + ", current_scope_level="
        + scopeLevel
        + ")";
  }
}
