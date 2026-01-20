class calculator {
public:
    int value;

    int add(int n) {
        value = value + n;
        return value;
    }
};

int main() {
    print_int(111);

    calculator c;

    c.value = 5;

    c.add(10);

    print_int(c.value);

    return 0;
}
