package shape_shooter_td;

import javax.swing.JButton;

final public class Tiles {

    private final JButton Button;
    private final String GridPosition;
    private boolean IsEdge = false;
    private int TurretLevel = 0;
    private boolean IsPath = false;
    private int TurretType = 0;

    public Tiles(final JButton button, final String position) {
        //Creates new tile at specified position within the tile grid
        this.Button = button;
        this.GridPosition = position;
    }

    final public void setTurretType(int TurretType) {
        //Sets the type of turret above this tile
        this.TurretType = TurretType;
    }

    final public int getTurretType() {
        //Returns the type of the turret above this tile if there is one
        return TurretType;
    }

    final public void setIsPath(boolean IsPath) {
        //Sets this tile as part of the path
        this.IsPath = IsPath;
    }

    final public void setTurretLevel(int TurretLevel) {
        //Sets the level of the turret above this tile
        this.TurretLevel = TurretLevel;
    }

    final public void setIsEdge(boolean IsEdge) {
        //Sets this tile as part of the edge of the 7 by 7 grid
        this.IsEdge = IsEdge;
    }

    final public JButton getButton() {
        //Returns this tile's button
        return Button;
    }

    final public String getPosition() {
        //Returns the position of this tile within the 7 by 7 grid
        return GridPosition;
    }

    final public boolean IsEdge() {
        //Returns whether or not this tile is on the edge of the 7 by 7 grid
        return IsEdge;
    }

    final public int getTurretLevel() {
        //Returns the level of the turret above this tile if there is one
        return TurretLevel;
    }

    final public boolean getIsPath() {
        //Returns whether or not this tile is part of the path
        return IsPath;
    }
}
