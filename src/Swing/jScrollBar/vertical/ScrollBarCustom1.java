
package swing.jScrollBar.vertical;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JScrollBar;

public class ScrollBarCustom1 extends JScrollBar{
    
    public ScrollBarCustom1() {
        setUI(new ModernScrollBarVertical());
        setPreferredSize(new Dimension(8, 8));
        setForeground(new Color(187,187, 187));
        setBackground(Color.WHITE);
    }
    
}
