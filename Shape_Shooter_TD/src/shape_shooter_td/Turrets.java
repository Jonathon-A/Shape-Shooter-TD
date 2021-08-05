package shape_shooter_td;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.swing.JPanel;

final public class Turrets extends Shape_Shooter_TDAssets implements CreatePanelInterface {

    private final int XCoordinate;
    private final int YCoordinate;
    private final JPanel Panel;
    private Enemies TargetEnemy = null;
    private int TurretLevel;
    private final int TurretType;
    private double RotatedRadians = 0;

    public Turrets(final int XCoordinate, final int YCoordinate, final int TurretLevel, final int TurretType) {
        //Creates new turret of specified type and level at the specified position
        this.XCoordinate = XCoordinate;
        this.YCoordinate = YCoordinate;
        this.TurretLevel = TurretLevel;
        this.TurretType = TurretType;
        this.Panel = CreatePanel();
    }

    final public int getXCoordinate() {
        //Returns the x coordinate of this turret's position
        return XCoordinate;
    }

    final public int getYCoordinate() {
        //Returns the y coordinate of this turret's position
        return YCoordinate;
    }

    final public JPanel getPanel() {
         //Returns this turret's display panel
        return Panel;
    }

    final public Enemies getTargetEnemy() {
        //Returns this turret's targeted enemy
        return TargetEnemy;
    }

    final public void setTargetEnemy(final Enemies TargetEnemy) {
        //Sets this turret's targeted enemy
        this.TargetEnemy = TargetEnemy;
    }

    final public int getTurretLevel() {
        //Returns this turret's upgrade level
        return TurretLevel;
    }

    final public void setTurretLevel(final int TurretLevel) {
        //Sets this turret's upgrade level
        this.TurretLevel = TurretLevel;
    }

    final public int getTurretType() {
        //Returns this turret's type
        return TurretType;
    }

    final public double getRotatedRadians() {
        //Returns this turret's rotation angle in radians
        return RotatedRadians;
    }

    final public void setRotatedRadians(final double RotatedRadians) {
        //Sets this turret's rotation angle in radians
        this.RotatedRadians = RotatedRadians;
    }

    final public double getAngle() {
        /*
        This method calculates the bearing between the turret and its target
        This angle is used to aim the turret
        It returns the calculated angle
         */

        //New turret bearing equals its current bearing
        double Angle = RotatedRadians;
        //If the turret has a target
        if (TargetEnemy != null) {
            //Finds bearing between the turret and its targeted enemy in radians
            Angle = Math.atan2(TargetEnemy.getYCoordinate() - YCoordinate, TargetEnemy.getXCoordinate() - XCoordinate);
            Angle = Angle + Math.PI / 2.0;
        }
        //Returns the calculated angle
        return Angle;
    }

    @Override
    final public JPanel CreatePanel() {
        /*
        This method sets the properties of the turret panel
        It is also used to display and update the displayed turret
        It returns the created turret panel
         */

        JPanel NewPanel = null;
        try {
            //Finds image of turret based on turret type
            final BufferedImage TurretImage = CreateBufferedImage("TurretHead" + TurretType + ".png");
            //Creates turrets panel
            NewPanel = new JPanel() {
                @Override
                protected void paintComponent(final Graphics g) {
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
                        default:
                        //Otherwise if render quality is set to default
                        case (2):
                            //Sets graphics render quality to default
                            gx.addRenderingHints(DEFAULT_RENDER);
                            break;
                        //Otherwise if render quality is set to quality
                        case (3):
                            //Sets graphics render quality to quality
                            gx.addRenderingHints(QUALITY_RENDER);
                            break;
                    }
                    //Draws turret image rotated by amount equal to the turrets bearing angle
                    gx.rotate(RotatedRadians, 50, 50);
                    gx.drawImage(TurretImage, new AffineTransform(), null);
                }

            };
            //Sets the properties of the turret panel
            NewPanel.setBounds(XCoordinate, YCoordinate, 100, 100);
            NewPanel.setOpaque(false);
            NewPanel.setBackground(null);
            NewPanel.setLayout(null);
            NewPanel.setBorder(null);

        } catch (IOException e) {
            System.out.println("Drawing turret error" + e);
        }
        //Returns the created turret panel
        return NewPanel;
    }
}
