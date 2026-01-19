int add(int a, int b) {
    return a + b;
}

class calculator {
public:
    int value;

    virtual int calculate(int x) {
        return x + value;
    }

    calculator() {
        value = 0;
    }

};

int main() {
    int x = 5;
    int y = 10;
    int z = add(x, y);
    print_int(z);
    return 0;
}


