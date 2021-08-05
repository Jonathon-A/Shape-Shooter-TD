package shape_shooter_td;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import javax.swing.JPanel;

final public class DeadEnemies extends Shape_Shooter_TDAssets implements CreatePanelInterface {

    private final int XCoordinate;
    private final int YCoordinate;
    private int AnimationTime = 0;
    private final Color FillColor;
    private final JPanel Panel;

    public DeadEnemies(final int XCoordinate, final int YCoordinate, final Color FillColor) {
        //Creates new dead enemy at specified position
        this.XCoordinate = XCoordinate;
        this.YCoordinate = YCoordinate;
        this.FillColor = FillColor;
        //Creates panel that displays this dead enemy's current state when painted
        this.Panel = CreatePanel();
    }

    final public int getAnimationTime() {
        //Returns what stage of the death animation this dead enemy is currently at
        return AnimationTime;
    }

    final public void setAnimationTime(final int AnimationTime) {
        //Sets the stage of the death animation for this dead enemy
        this.AnimationTime = AnimationTime;
    }

    final public JPanel getPanel() {
        //Returns this dead enemy's display panel
        return Panel;
    }

    @Override
    final public JPanel CreatePanel() {
        /*
        This method sets the properties of the dead enemy's panel
        It is also used to display and update the displayed dead enemy
        It returns the created dead enemy's panel
         */

        final JPanel NewPanel = new JPanel() {
            @Override
            final protected void paintComponent(final Graphics g) {

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

                //Draws enemy circle shape with radius proportional to the stage of the death animation
                final Ellipse2D Circle = new Ellipse2D.Float((18 + (2 * AnimationTime)), (18 + (2 * AnimationTime)), (64 - (4 * AnimationTime)), (64 - (4 * AnimationTime)));
                gx.setStroke(new BasicStroke(4));
                gx.setColor(FillColor);
                gx.fill(Circle);
                gx.setColor(AssetsColorArray[3]);
                gx.draw(Circle);
            }
        };
        //Sets the properties of the dead enemy's panel
        NewPanel.setBounds(XCoordinate, YCoordinate, 100, 100);
        NewPanel.setOpaque(false);
        NewPanel.setBackground(null);
        NewPanel.setBorder(null);
        NewPanel.setLayout(null);
        //Returns the created dead enemy's panel
        return NewPanel;

    }

}
