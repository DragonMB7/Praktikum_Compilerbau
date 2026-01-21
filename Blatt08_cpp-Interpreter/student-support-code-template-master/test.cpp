class counter {
public:
    int count;

    void set(int n) {
        count = n;
    }

    // ATTENTION : Espace obligatoire entre void et increment
    void increment() {
        count = count + 1;
    }
};

int main() {
    counter c1;
    counter c2;

    // Initialisation
    c1.set(10);
    c2.set(100);

    // Tests
    c1.increment(); // c1 devient 11
    c1.increment(); // c1 devient 12

    // Affichage
    print_int(c1.count); // Doit afficher 12
    print_int(c2.count); // Doit afficher 100

    return 0;
}
