int dangerous_function() {
    print("DANGER! This should not run!");
    return 0;
}

int main() {
    bool safe = true;

    print("Test Short-Circuit OR:");
    // Since 'safe' is true, dangerous_function() MUST NOT be called
    if (safe || dangerous_function()) {
        print("Safe passed");
    }

    print("Test Short-Circuit AND:");
    bool lie = false;
    // Since 'lie' is false, dangerous_function() MUST NOT be called
    if (lie && dangerous_function()) {
        // Nothing
    } else {
        print("Lie passed");
    }

    return 0;
}
