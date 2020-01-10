public class PVector {

    public double x;
    public double y;

    PVector(double x, double y){
        this.x = x;
        this.y = y;
    }
    public static PVector subs(PVector p1, PVector p2){
        return new PVector(p1.x-p2.x,p1.y-p2.y);
    }
    public void add(PVector p){
        x += p.x;
        y += p.y;
    }
    @Override
    public String toString() {
        return "PVector{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
    public double magnitude(){
        return  Math.sqrt(x*x + y*y);
    }
    public double normalize(double b){
        double mag = magnitude();
        if(mag > 0){
            x = x / mag;
            y= y / mag;
        }
        x*=b;
        y*=b;
        return mag;
    }
    public double normalize(){
        double mag = magnitude();
        if(mag > 0){
            x = x / mag;
            y= y / mag;
        }
        return mag;
    }
}
