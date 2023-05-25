
package swing.jScrollBar.horizontal;

import swing.jScrollBar.vertical.ModernScrollBarVertical;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JScrollBar;

public class ScrollBarCustom2 extends JScrollBar{
    
    public ScrollBarCustom2() {
        setUI(new ModernScrollBarVertical());
        setPreferredSize(new Dimension(8, 8));
        setForeground(new Color(187,187, 187));
        setBackground(Color.WHITE);
        setUnitIncrement(10);
    }
    @Override
    public int getOrientation(){
        return HORIZONTAL;
    }
    
}
