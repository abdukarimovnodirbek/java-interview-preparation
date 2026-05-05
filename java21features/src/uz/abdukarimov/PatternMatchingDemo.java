package uz.abdukarimov;

public class PatternMatchingDemo {
    //------------------------ instanceof pattern ------------------------
    //--------------------------------------------------------------------
/*    Java 15 va oldin

// Cast kerak, ko'p takrorlanish
if (obj instanceof String) {
        String s = (String) obj; // cast
        System.out.println(s.length());
    }

if (shape instanceof Circle) {
        Circle c = (Circle) shape;
        double area = Math.PI * c.radius()
                * c.radius();
    }

Java 16+ (pattern matching)

// Cast avtomatik, o'zgaruvchi bind
if (obj instanceof String s) {
    System.out.println(s.length());
}

if (shape instanceof Circle c) {
    double area = Math.PI * c.radius()
                       * c.radius();
}

// Guard bilan — AND shart
if (obj instanceof String s && s.length() > 5) {
    System.out.println("Uzun: " + s);
}

    //------------------------ switch pattern ----------------------------
    //--------------------------------------------------------------------
Java 15 va oldin

String format(Object obj) {
    if (obj instanceof Integer) {
        return "int: " + (Integer) obj;
    } else if (obj instanceof Double) {
        return "double: " + (Double) obj;
    } else if (obj instanceof String) {
        return "str: " + (String) obj;
    } else {
        return "unknown";
    }
}

Java 21 (switch pattern)

String format(Object obj) {
    return switch (obj) {
        case Integer i -> "int: " + i;
        case Double d  -> "double: " + d;
        case String s  -> "str: " + s;
        case null      -> "null";
        default        -> "unknown";
    };
}

// Guard (when) bilan:
String classify(Object obj) {
    return switch (obj) {
        case Integer i when i < 0  -> "manfiy";
        case Integer i when i == 0 -> "nol";
        case Integer i             -> "musbat";
        default                    -> "boshqa";
    };
}

    //------------------------ record pattern ----------------------------
    //--------------------------------------------------------------------
Java 20 va oldin
record Point(int x, int y) {}
record Line(Point a, Point b) {}

void printX(Object obj) {
  if (obj instanceof Point p) {
    System.out.println(p.x());
  }
  if (obj instanceof Line l) {
    Point a = l.a();
    System.out.println(a.x());
  }
}

Java 21 (record patterns)

// Record ichidagi qiymatlar to'g'ridan bind
void printX(Object obj) {
  if (obj instanceof Point(var x, var y)) {
    System.out.println(x); // to'g'ridan!
  }

  // Nested record pattern!
  if (obj instanceof
      Line(Point(var x1, var y1),
           Point(var x2, var y2))) {
    System.out.println(x1 + "," + y1);
  }
}

// Switch + record pattern
String describe(Shape s) {
  return switch (s) {
    case Circle(var r)      -> "doira r="+r;
    case Rect(var w, var h) -> w+"x"+h;
  };
}
*/
}
