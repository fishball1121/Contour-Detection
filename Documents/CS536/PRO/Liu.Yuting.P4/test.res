int x;
int y;
struct Node {
    int data;
    bool flag;
    struct Node left;
    struct Node right;
};

struct Map {
    struct Node root;
    int mapValue;
};

struct Map mymap;
struct Node mynode;
int GivenFunc(int x, bool b) {
}

void VoidFunc() {
}

int func(int data, bool f, int value) {
    struct Map x;
    int mymap;
    x(Map).mapValue(int) = y(int);
    x(Map).root(Node).data(int) = data(int);
    x(Map).root(Node).flag(bool) = f(bool);
    x(Map).mapValue(int) = mymap(int);
    x(Map).mapValue(int) = GivenFunc(int,bool->int)(mymap(int), y(int));
    x(Map).mapValue(int) = func(int,bool,int->int)(mymap(int), f(bool), y(int));
    x(Map).root(Node).data(int) = VoidFunc(->void)();
    x(Map).root(Node).left(Node) = mynode(Node);
    x(Map).root(Node).right(Node).right(Node) = mynode(Node).left(Node);
}

struct Point {
    int x;
    int y;
};

void g() {
    int a;
    bool b;
    struct Point p;
    p(Point).x(int) = a(int);
    b(bool) = (a(int) == 3);
    func(int,bool,int->int)((a(int) + (p(Point).y(int) * 2)), b(bool));
    g(->void)();
}

int testif(int x, int a, int b) {
    if ((x(int) == 0)) {
        return x(int);
    }
    x(int) = ((a(int) * 2) + b(int));
    return x(int);
}

bool testAssign(int x, bool a) {
    if ((a(bool) == true)) {
        x(int) = 1;
        return true;
    }
    else {
        x(int) = (-1);
        return false;
    }
    return true;
}

void testifelse() {
    int b;
    bool c;
    if ((b(int) == (-1))) {
        x(int) = ((4 + (3 * 5)) - y(int));
        while (c(bool)) {
            y(int) = ((y(int) * 2) + x(int));
        }
    }
    else {
        x(int) = 0;
    }
}

int print(int x) {
    return x(int);
}

void testIncAndDec(int x, bool Inc) {
    if ((Inc(bool) == true)) {
        x(int)++;
    }
    else {
        x(int)--;
    }
    return;
}

int testReadAndWriteInt(int readVal, int writeVal) {
    cin >> readVal(int);
    cout << writeVal(int);
    return (readVal(int) + writeVal(int));
}

bool testReadAndWriteBool(bool readFlag, bool writeFlag) {
    cin >> readFlag(bool);
    cout << writeFlag(bool);
    return (writeFlag(bool) || readFlag(bool));
}

struct readFile {
    int data_val;
};

struct writeFile {
    int data_val;
};

void testReadANdWriteStruct(int data, int writeStr) {
    struct readFile r;
    struct writeFile w;
    r(readFile).data_val(int) = data(int);
    cin >> r(readFile).data_val(int);
    w(writeFile).data_val(int) = writeStr(int);
    cout << (w(writeFile).data_val(int) + r(readFile).data_val(int));
    return;
}

int testFnCall(int x) {
    int y;
    return print(int->int)((x(int) - (3 * y(int))));
}

void testwhile(int count) {
    while ((count(int) != 0)) {
        cout << count(int);
        count(int)--;
    }
    return;
}

void testExp() {
    int x;
    int y;
    int i;
    int c;
    bool f;
    bool g;
    bool flag;
    i(int) = 23;
    f(bool) = true;
    g(bool) = false;
    f(bool) = testExp(->void)("string");
    x(int) = (x(int) * 2);
    y(int) = ((y(int) + 2) * x(int));
    x(int) = (((((x(int) + y(int)) / 2) * 3) + 10) - (c(int) / 2));
    f(bool) = (g(bool) && ((flag(bool) == (!f(bool))) >= x(int)));
    if ((i(int) != (x(int) + y(int)))) {
        f(bool) = (i(int) > (x(int) * 2));
    }
    else {
        f(bool) = g(bool);
    }
    f(bool) = (((x(int) - flag(bool)) + g(bool)) || ((!g(bool)) && ((flag(bool) <= g(bool)) + c(int))));
    f(bool) = ((-x(int)) + 2);
    f(bool) = testwhile(int->void)(mymap(Map).root(Node).left(Node));
    return;
}

