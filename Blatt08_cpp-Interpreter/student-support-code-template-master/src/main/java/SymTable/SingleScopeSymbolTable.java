package SymTable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SingleScopeSymbolTable {
    private String name;
    private Map<String, Symbol> symbols;

    public SingleScopeSymbolTable(String name) {
        this.name = name;
        this.symbols = new HashMap<>();
    }

    public void addSymbol(Symbol symbol) {
        if (symbols.containsKey(symbol.getName())) {
            // potential compiler exeptions
            throw new RuntimeException("Fehler: Symbol '" + symbol.getName() + "' ist bereits in Scope '" + this.name + "' deklariert.");
        }
        symbols.put(symbol.getName(), symbol);
        System.out.println("  [Scope '" + this.name + "'] Symbol '" + symbol.getName() + "' hinzugefügt.");
    }

    public Symbol lookupSymbol(String name) {
        return symbols.get(name);
    }

    public String getName() {
        return name;
    }

    public Set<String> getSymbolNames() {
        return symbols.keySet();
    }

    @Override
    public String toString() {
        return "SingleScopeSymbolTable(name='" + name + "', symbols=" + getSymbolNames() + ")";
    }
}
