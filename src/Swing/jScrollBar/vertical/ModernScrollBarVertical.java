
package swing.jScrollBar.vertical;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.plaf.basic.BasicScrollBarUI;

public class ModernScrollBarVertical extends BasicScrollBarUI {
    
    private final int THUMB_SIZE = 160;
    
    @Override
    protected Dimension getMaximumThumbSize() {
        if (scrollbar.getOrientation() == JScrollBar.VERTICAL){
            return new Dimension(0, THUMB_SIZE);
        } else {
            return new Dimension(THUMB_SIZE, 0);
        }
    }
    @Override
    protected Dimension getMinimumThumbSize() {
        if (scrollbar.getOrientation() == JScrollBar.VERTICAL){
            return new Dimension(0, THUMB_SIZE);
        } else {
            return new Dimension(THUMB_SIZE, 0);
        }
    } 
    
    @Override
    protected JButton createIncreaseButton(int i){
        return new ScrollBarButton();
    }
    
    @Override
    protected JButton createDecreaseButton(int i){
        return new ScrollBarButton();
    }
    
    @Override
    protected void paintTrack(Graphics grphcs, JComponent jc, Rectangle rct){
        Graphics2D g2 = (Graphics2D) grphcs;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int orientation = scrollbar.getOrientation();
        int size; int x; int y; int width; int height;
        if (orientation == JScrollBar.VERTICAL){
            size = rct.width / 2;
            x = rct.x + ((rct.width - size) / 2);
            y = rct.y;
            width = size;
            height = rct.height;
        } else {
            size = rct.height / 2;
            y = rct.y + ((rct.height - size) / 2);
            x = 0;
            width = rct.width;
            height = size;
        }
        g2.setColor(new Color(240, 240, 240));
        g2.fillRect(x, y, width, height);
    }
    
    @Override
    protected void paintThumb(Graphics grphcs, JComponent jc, Rectangle rct){
        Graphics2D g2 = (Graphics2D) grphcs;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int x = rct.x;
        int y = rct.y;
        int width = rct.width;
        int height = rct.height;
        if (scrollbar.getOrientation() == JScrollBar.VERTICAL){
            y += 8;
            height -= 16;
        } else {
            x += 8;
            width -= 16;
        }
        g2.setColor(scrollbar.getForeground());
        g2.fillRoundRect(x, y, width, height, 10, 10);
    }
    
    private class ScrollBarButton extends JButton {
        
        public ScrollBarButton() {
            setBorder(BorderFactory.createEmptyBorder());
        }
        
        @Override
        public void paint(Graphics grphcs){
            
        }
    }
    
}
