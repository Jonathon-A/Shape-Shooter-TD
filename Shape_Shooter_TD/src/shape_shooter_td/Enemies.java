package shape_shooter_td;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JPanel;

final public class Enemies extends Shape_Shooter_TDAssets implements CreatePanelInterface {

    private int XCoordinate = 19;
    private int YCoordinate = 199;
    private int Priority = 0;
    private double HealthPoints;
    private double RotatedDegrees = 0;
    private Arc2D.Double CircleSector = new Arc2D.Double(0, 0, 64, 64, 225, 270, Arc2D.CHORD);
    private final Ellipse2D Circle = new Ellipse2D.Float(0, 0, 64, 64);
    private Polygon Poly = null;
    private Color FillColor;
    private boolean Hover = false;
    private final JPanel Panel;
    private int NextWaypointIndex = 0;
    private boolean BossEnemy = false;

    public Enemies(final double HealthPoints, final Color FillColor) {
        //Creates new enemy at start position with specified health points
        this.HealthPoints = HealthPoints;
        this.FillColor = FillColor;
        //If new enemy has more then 20 hit points
        if (HealthPoints > 20) {
            //Creates polygon shape for new enemy with sides that represent the enemies health points
            this.Poly = CreatePoly();
        }
        //Creates panel that displays the enemy's current state when painted
        this.Panel = CreatePanel();
    }

    final public void setBossEnemy() {
        //This enemy is a boss
        this.BossEnemy = true;
    }

    final public void setHealthPoints(final double HealthPoints) {
        //Updates this enemy's current health
        this.HealthPoints = HealthPoints;
    }

    final public void setNextWaypointIndex(final int NextWaypointIndex) {
        //Sets the next waypoint along the path for this enemy
        this.NextWaypointIndex = NextWaypointIndex;
    }

    final public int getNextWaypointIndex() {
        //Gets the next waypoint along the path for this enemy
        return NextWaypointIndex;
    }

    final public JPanel getPanel() {
        //Returns this enemy's display panel
        return Panel;
    }

    final public int getXCoordinate() {
        //Returns the x coordinate of this enemy's position
        return XCoordinate;
    }

    final public int getYCoordinate() {
        //Returns the y coordinate of this enemy's position
        return YCoordinate;
    }

    final public int getPriority() {
        //Returns targeting priority of this enemy's
        return Priority;
    }

    final public double getHealthPoints() {
        //Gets this enemy's current health
        return HealthPoints;
    }

    final public double getRotatedDegrees() {
        //Gets this enemy's current rotation in degrees
        return RotatedDegrees;
    }

    final public Polygon getPoly() {
        //Returns this enemy's polygon shape
        return Poly;
    }

    final public Arc2D.Double getCircleSector() {
        //Returns this enemy's circle sector shape
        return CircleSector;
    }

    final public Ellipse2D getCircle() {
        //Returns this enemy's circle shape
        return Circle;
    }

    final public Color getFillColor() {
        //Return the colour of this enemy
        return FillColor;
    }

    final public boolean isHover() {
        //Return whether or not the mouse cursor is hovering above this enemy
        return Hover;
    }

    final public void setXCoordinate(final int XCoordinate) {
        //Sets the x coordinate of this enemy's position
        this.XCoordinate = XCoordinate;
    }

    final public void setYCoordinate(final int yXCoordinate) {
        //Sets the y coordinate of this enemy's position
        this.YCoordinate = yXCoordinate;
    }

    final public void setPriority(final int Priority) {
        //Sets the targeting priority of this enemy
        this.Priority = Priority;
    }

    final public void setRotatedDegrees(final double RotatedDegrees) {
        //Sets this enemy's current rotation in degrees
        this.RotatedDegrees = RotatedDegrees;
    }

    final public void UpdatePoly() {
        //Updates this enemy's polygon shape
        this.Poly = CreatePoly();
    }

    final public void UpdateCircleSector() {
        //Updates this enemy's circle sector shape
        this.CircleSector = CreateCircleSector();
    }

    final public void setHover(final boolean hover) {
        //Sets whether or not the mouse cursor is hovering above this enemy
        this.Hover = hover;
    }

    private Arc2D.Double CreateCircleSector() {
        /*
        This method creates a circle sector based on the shapes current rotation
        It is called every time the game loops if the shape has between 10 and 20 health points
        It also returns the generated circle sector 
         */

        final Arc2D.Double CircleSectorOutput = new Arc2D.Double(0, 0, 64, 64, 225 - RotatedDegrees, 270, Arc2D.CHORD);
        //Returns the generated circle sector 
        return CircleSectorOutput;
    }

    private Polygon CreatePoly() {
        /*
        This method creates a polygon with sides based on the health and current rotation of the enemy
        If the enemy has more then 100 health points then the polygon will be spiky
        If there is an even number of sides then it make half of polygon and duplicates to other side so it is horizontally symmetrical
        It is called every time the game loops if the shape has more then 20 health points
        It also returns the polygon generated
         */

        //Calculates the number of sides of the new polygon from the enemy's health
        int Sides = (int) Math.ceil(HealthPoints / 10);
        //Current rotation of enemy
        final double Rotation = RotatedDegrees;
        //Centre and Radius of new polygon
        final double centreX = (32);
        final double centreY = (32);
        double radius = 32;
        //If more then 10 sides
        if (Sides > 10) {
            //If odd number of sides
            if (Sides % 2 != 0) {
                //Adds another side
                Sides++;
            }
        }
        //X coordinates of polygon vertices Array
        final int[] XVertices = new int[Sides];
        //Y coordinates of polygon vertices Array
        final int[] YVertices = new int[Sides];
        //If more then 10 sides
        if (Sides > 10) {
            //Loops through all vertices 
            for (int PolygonPoint = 0; PolygonPoint < Sides; PolygonPoint++) {
                //Decreases radius for every other point
                if ((PolygonPoint % 2 == 0)) {
                    radius = (int) (32 * 0.8);
                } else {
                    radius = 32;
                }
                //Calculates coordinate of polygon point
                XVertices[PolygonPoint] = (int) (centreX + radius * Math.cos(2 * PolygonPoint * Math.PI / Sides));
                YVertices[PolygonPoint] = (int) (centreY + radius * Math.sin(2 * PolygonPoint * Math.PI / Sides));

            }
        }//If 10 or less sides 
        else {
            //If even number of sides
            if (Sides % 2 == 0) {

                //Creates first half of polygon
                //Loops through half of all vertices
                for (int Vertex = 0; Vertex < Sides / 2; Vertex++) {
                    //Calculates coordinate of polygon point
                    XVertices[Vertex] = (int) (centreX + radius * Math.cos(2 * Vertex * Math.PI / Sides));
                    YVertices[Vertex] = (int) (centreY - radius * Math.sin(2 * Vertex * Math.PI / Sides));
                }
                //Creates second half of polygon
                //Loops through half of all vertices
                for (int Vertex = 0; Vertex < Sides / 2; Vertex++) {
                    //Calculates coordinate of polygon point
                    XVertices[Vertex + Sides / 2] = (int) (2 * radius) - XVertices[Vertex];
                    YVertices[Vertex + Sides / 2] = (int) (2 * radius) - YVertices[Vertex];
                }

            }//If odd number of sides
            else {
                //Loops through all vertices
                for (int Vertex = 0; Vertex < Sides; Vertex++) {
                    //Calculates coordinate of polygon point
                    XVertices[Vertex] = (int) (centreX + radius * Math.cos(2 * Vertex * Math.PI / Sides));
                    YVertices[Vertex] = (int) (centreY - radius * Math.sin(2 * Vertex * Math.PI / Sides));
                }

            }
        }
        double InitialRotation = 0;
        //If 10 or less sides 
        if (Sides <= 10) {
            //Calculates angle between two vertices in radians
            double DeltaWidth = XVertices[Sides - 1] - XVertices[Sides - 2];
            double DeltaHeight = YVertices[Sides - 2] - YVertices[Sides - 1];
            InitialRotation = Math.atan(DeltaHeight / DeltaWidth);
        }
        //Creates an array of coordinates points of the non rotated vertices
        final ArrayList<Point2D> NonRotatedPoints = new ArrayList();
        //Loops through all vertices 
        for (int PolygonPoint = 0; PolygonPoint < Sides; PolygonPoint++) {
            //Adds the coordinate of the vertex to the array of non rotated points
            NonRotatedPoints.add(new Point2D.Double(XVertices[PolygonPoint], YVertices[PolygonPoint]));
        }

        //Creates an array of coordinates points of the rotated vertices
        final Point2D[] RotatedPoints = new Point2D[Sides];

        //Rotate non rotated vertices around the center
        final AffineTransform Transform = AffineTransform.getRotateInstance(InitialRotation + Math.toRadians(Rotation), centreX, centreY);
        Transform.transform(NonRotatedPoints.toArray(new Point2D[0]), 0, RotatedPoints, 0, Sides);

        //X coordinates of rotated polygon vertices Array
        final int[] RotatedXVertices = new int[Sides];
        //Y coordinates of rotated polygon vertices Array
        final int[] RotatedYVertices = new int[Sides];
        //Loops through all vertices
        for (int Vertex = 0; Vertex < Sides; Vertex++) {
            //Rounds coordinates of rotated polygon point to nearest point
            RotatedXVertices[Vertex] = (int) RotatedPoints[Vertex].getX();
            RotatedYVertices[Vertex] = (int) RotatedPoints[Vertex].getY();
        }
        //Creates polygon using the rotated vertices
        final Polygon PLGN = new Polygon(RotatedXVertices, RotatedYVertices, Sides);
        //Returns the generated polygon
        return PLGN;
    }

    private int GameLoops = 0;

    @Override
    final public JPanel CreatePanel() {
        /*
        This method sets the properties of the enemy's panel
        It is also used to display and update the displayed enemy
        It returns the created enemy's panel
         */

        final Random Rand = new Random();
        //Creates enemy panel
        final JPanel NewPanel = new JPanel() {
            @Override
            final protected void paintComponent(final Graphics g) {
                //Calculates number of sides of the enemy shape based on its health
                final int Sides = (int) Math.ceil(HealthPoints / 10);
                //If enemy is alive
                if (Sides > 0) {
                    g.translate(18, 18);
                    final Graphics2D gx = (Graphics2D) g;

                    //If antialiasing is set to true
                    if (Shape_Shooter_TD.MainClass.isANTIALIASING()) {
                        //Adds antialiasing to graphics 
                        gx.addRenderingHints(ANTIALIASING);
                    }
                    switch (Shape_Shooter_TD.MainClass.getRenderQuality()) {
                        //If render quality is set to speed
                        case (1):
                            //Sets graphics render quality to speed
                            gx.addRenderingHints(SPEED_RENDER);
                            break;
                        //Otherwise if render quality is set to default
                        case (2):
                            //Sets graphics render quality to default
                            gx.addRenderingHints(DEFAULT_RENDER);
                            break;
                        default:
                        //Otherwise if render quality is set to quality
                        case (3):
                            //Sets graphics render quality to quality
                            gx.addRenderingHints(QUALITY_RENDER);
                            break;
                    }
                    //If the enemy shape is a chord circle
                    if (Sides == 2) {
                        //If the enemy is hovered over
                        if (Hover) {
                            //Draws large red border around enemy chord circle shape 
                            gx.setStroke(new BasicStroke(12));
                            gx.setColor(AssetsColorArray[4]);
                            gx.draw(CircleSector);
                        }
                        //Draws enemy chord circle shape
                        gx.setStroke(new BasicStroke(4));
                        gx.setColor(FillColor);
                        gx.fill(CircleSector);
                        gx.setColor(AssetsColorArray[3]);
                        gx.draw(CircleSector);

                    } else {
                        //If the enemy shape is a circle
                        if (Sides == 1) {
                            //If the enemy is hovered over
                            if (Hover) {
                                //Draws large red border around enemy circle shape 
                                gx.setStroke(new BasicStroke(12));
                                gx.setColor(AssetsColorArray[4]);
                                gx.draw(Circle);
                            }
                            //Draws enemy circle shape
                            gx.setStroke(new BasicStroke(4));
                            gx.setColor(FillColor);
                            gx.fill(Circle);
                            gx.setColor(AssetsColorArray[3]);
                            gx.draw(Circle);

                        }//If the enemy shape is a polygon
                        else {
                            //If the enemy is hovered over
                            if (Hover) {
                                //Draws large red border around enemy polygon shape 
                                gx.setStroke(new BasicStroke(12));
                                gx.setColor(AssetsColorArray[4]);
                                gx.drawPolygon(Poly);
                            }
                            //Draws enemy polygon shape
                            gx.setStroke(new BasicStroke(4));
                            gx.setColor(FillColor);
                            gx.fillPolygon(Poly);
                            gx.setColor(AssetsColorArray[3]);
                            gx.drawPolygon(Poly);

                        }
                    }
                    //Draws enemy health value inside its shape
                    gx.setStroke(new BasicStroke(0));
                    gx.setFont(new Font("Verdana", Font.BOLD, 18));
                    final FontMetrics metrics = gx.getFontMetrics(new Font("Verdana", Font.BOLD, 18));
                    final int Textx = 0 + (64 - metrics.stringWidth(String.valueOf((int) Math.ceil(HealthPoints)))) / 2;
                    final int Texty = 0 + ((64 - metrics.getHeight()) / 2) + metrics.getAscent();
                    gx.drawString(String.valueOf((int) Math.ceil(HealthPoints)), Textx, Texty);
                }
                //If the enemy is a boss
                if (BossEnemy) {
                    //Every 50 game loops
                    if (GameLoops >= 50) {
                        //Changes the colour of the boss enemy
                        FillColor = new Color(Rand.nextInt(200) + 55, Rand.nextInt(200) + 55, Rand.nextInt(200) + 55);
                        GameLoops = 0;
                    } else {
                        GameLoops++;
                    }
                }
            }
        };
        //Sets the properties of the enemy's panel
        NewPanel.setBounds(XCoordinate, YCoordinate, 100, 100);
        NewPanel.setOpaque(false);
        NewPanel.setBackground(null);
        NewPanel.setBorder(null);
        NewPanel.setLayout(null);
        //Returns the created enemy's panel
        return NewPanel;
    }

}
