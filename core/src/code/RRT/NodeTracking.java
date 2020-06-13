package code.RRT;

public class NodeTracking {

    Pointer pointer; // Coordinates of a cell
    int distance; // Distance of the cell from the source

    NodeTracking(Pointer pointer, int distance){
        this.pointer = pointer;
        this.distance = distance;
    }
}
