package main;

public class Vector2d {
    final int x;
    final int y;

    Vector2d(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public String toString() {
        return "(" + this.x + "," + this.y + ")";
    }

    boolean precedes(Vector2d other) {
        if (this.x <= other.x && this.y <= other.y)
            return true;
        return false;
    }

    boolean follows(Vector2d other) {
        if (this.x >= other.x && this.y >= other.y)
            return true;
        return false;
    }

    public Vector2d upperRight(Vector2d other) {
        int xm, ym;
        if (this.x >= other.x)
            xm = this.x;
        else
            xm = other.x;
        if (this.y >= other.y)
            ym = this.y;
        else
            ym = other.y;
        return new Vector2d(xm, ym);
    }

    public Vector2d lowerLeft(Vector2d other) {
        int xm, ym;
        if (this.x <= other.x)
            xm = this.x;
        else
            xm = other.x;
        if (this.y <= other.y)
            ym = this.y;
        else
            ym = other.y;
        return new Vector2d(xm, ym);
    }

    public Vector2d add(Vector2d other) {
        int xm = this.x + other.x;
        int ym = this.y + other.y;
        return new Vector2d(xm, ym);
    }

    public Vector2d subtract(Vector2d other) {
        int xm = this.x - other.x;
        int ym = this.y - other.y;
        return new Vector2d(xm, ym);
    }

    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (!(other instanceof main.Vector2d))
            return false;
        Vector2d oth = (Vector2d) other;
        if (this.x == oth.x && this.y == oth.y)
            return true;
        return false;
    }

    public Vector2d opposite() {
        return new Vector2d(-this.x, -this.y);
    }

    public int hashCode() {
        int hash = 13;
        hash += this.x * 31;
        hash += this.y * 17;
        return hash;
    }

    public static int compare(Vector2d v1, Vector2d v2) {
        if (v1.x > v2.x) return 1;
        else if (v1.x < v2.x) return -1;
        else {
            if (v1.y > v2.y) return 1;
            else if (v1.y < v2.y) return -1;
            else return 0;
        }
    }
}
