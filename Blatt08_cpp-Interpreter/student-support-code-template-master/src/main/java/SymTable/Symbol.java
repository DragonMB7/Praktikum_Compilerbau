package SymTable;

import java.util.Objects; // Für Objects.hash() und Objects.equals()

public class Symbol {
    private String name;
    private String type;
    private String kind;
    // Room for more possible distinct values

    public Symbol(String name, String type, String kind) {
        this.name = name;
        this.type = type;
        this.kind = kind;
    }

    public String getName() { return name; }
    public String getType() { return type; }
    public String getKind() { return kind; }


    @Override
    public String toString() {
        return "Symbol(name='" + name + "', type='" + type + "', kind='" + kind + "')";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Symbol symbol = (Symbol) o;
        return Objects.equals(name, symbol.name) &&
            Objects.equals(type, symbol.type) &&
            Objects.equals(kind, symbol.kind);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type, kind);
    }
}
